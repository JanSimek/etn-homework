package com.etnetera.hr.dto;

import com.etnetera.hr.data.HypeLevel;
import com.etnetera.hr.data.JavaScriptFramework;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

public class JavaScriptFrameworkDTO {
    private final Long id;

    @NotEmpty
    @Size(max = 30)
    private final String name;

    private final Set<String> version;

    private final LocalDate deprecationDate;

    private final HypeLevel hypeLevel;

    public JavaScriptFrameworkDTO(Long id, String name, Set<String> version, LocalDate deprecationDate, HypeLevel hypeLevel) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.deprecationDate = deprecationDate;
        this.hypeLevel = hypeLevel;
    }

    public JavaScriptFrameworkDTO(JavaScriptFramework framework) {
        this(framework.getId(), framework.getName(), framework.getVersion(), framework.getDeprecationDate(), framework.getHypeLevel());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<String> getVersion() {
        return version;
    }

    public LocalDate getDeprecationDate() {
        return deprecationDate;
    }

    public HypeLevel getHypeLevel() {
        return hypeLevel;
    }
}
