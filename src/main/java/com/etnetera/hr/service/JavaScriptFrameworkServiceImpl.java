package com.etnetera.hr.service;

import com.etnetera.hr.data.HypeLevel;
import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.dto.JavaScriptFrameworkDTO;
import com.etnetera.hr.exception.JavascriptFrameworkDuplicateException;
import com.etnetera.hr.exception.JavascriptFrameworkNotFoundException;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.SetJoin;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JavaScriptFrameworkServiceImpl implements JavaScriptFrameworkService {

    private final JavaScriptFrameworkRepository repository;

    public JavaScriptFrameworkServiceImpl(JavaScriptFrameworkRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean exists(String frameworkName) {
        return repository.findByName(frameworkName).isPresent();
    }

    @Override
    public JavaScriptFrameworkDTO createFramework(JavaScriptFrameworkDTO dto) {

        if (exists(dto.getName())) {
            throw new JavascriptFrameworkDuplicateException("Framework already exists");
        }

        JavaScriptFramework framework = new JavaScriptFramework();

        framework.setName(dto.getName());
        framework.setVersion(dto.getVersion());
        framework.setHypeLevel(dto.getHypeLevel());
        framework.setDeprecationDate(dto.getDeprecationDate());

        return new JavaScriptFrameworkDTO(repository.save(framework));
    }

    @Override
    public List<JavaScriptFrameworkDTO> findAllFrameworks() {
        return repository.findAll().stream()
                .map(JavaScriptFrameworkDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<JavaScriptFrameworkDTO> findFrameworkById(Long frameworkId) {
        var framework = repository.findById(frameworkId);
        return framework.map(JavaScriptFrameworkDTO::new);
    }

    @Override
    @Transactional
    public void update(Long frameworkId, JavaScriptFrameworkDTO dto) {

        var framework = repository.findById(frameworkId)
                .orElseThrow(() -> new JavascriptFrameworkNotFoundException("Framework does not exists"));

        framework.setName(dto.getName());
        framework.setVersion(dto.getVersion());
        framework.setDeprecationDate(dto.getDeprecationDate());
        framework.setHypeLevel(dto.getHypeLevel());

        repository.save(framework);
    }

    @Override
    @Transactional
    public void delete(Long frameworkId) {
        if (!repository.existsById(frameworkId)) {
            throw new JavascriptFrameworkNotFoundException("Framework does not exist");
        }

        repository.deleteById(frameworkId);
    }

    @Override
    @Transactional
    public void addVersion(String name, String newVersion) {

        var framework = repository.findByName(name)
                .orElseThrow(() -> new JavascriptFrameworkNotFoundException("Framework does not exists"));

        var originalVersions = framework.getVersion();
        if (originalVersions.contains(newVersion)) {
            throw new JavascriptFrameworkDuplicateException("Version already exists");
        }
        var versions = new HashSet<>(originalVersions);
        versions.add(newVersion);
        framework.setVersion(versions);
        repository.save(framework);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JavaScriptFrameworkDTO> search(String name, String version, LocalDate deprecationDate, HypeLevel hypeLevel) {

        return repository.findAll(((root, query, criteriaBuilder) -> {

            Predicate where = criteriaBuilder.conjunction();

            if (name != null) {
                var nameLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), '%' + name.toLowerCase() + '%');
                where = criteriaBuilder.and(where, nameLike);
            }
            if (version != null) {
                SetJoin<JavaScriptFramework, String> joinVersion = root.joinSet("version");
                var versionEq = criteriaBuilder.and(joinVersion.in(Set.of(version)));
                where = criteriaBuilder.and(where, versionEq);
            }
            if (deprecationDate != null) {
                var deprecationDateEq = criteriaBuilder.equal(root.get("deprecationDate"), deprecationDate);
                where = criteriaBuilder.and(where, deprecationDateEq);
            }
            if (hypeLevel != null) {
                var hypeLevelEq = criteriaBuilder.equal(root.get("hypeLevel"), hypeLevel);
                where = criteriaBuilder.and(where, hypeLevelEq);
            }

            return where;
        })).stream()
                .map(JavaScriptFrameworkDTO::new)
                .collect(Collectors.toList());
    }

}
