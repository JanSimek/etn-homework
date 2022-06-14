package com.etnetera.hr.service;

import com.etnetera.hr.data.HypeLevel;
import com.etnetera.hr.dto.JavaScriptFrameworkDTO;
import com.etnetera.hr.exception.JavascriptFrameworkDuplicateException;
import com.etnetera.hr.exception.JavascriptFrameworkNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JavaScriptFrameworkService {

    boolean exists(String frameworkName);

    /**
     * Creates a new JavaScriptFramework and saves it into the database
     * @param javascriptFrameworkDTO
     * @return dto based on the newly created entity
     * @throws JavascriptFrameworkDuplicateException if a framework with the same name already exists
     */
    public JavaScriptFrameworkDTO createFramework(JavaScriptFrameworkDTO javascriptFrameworkDTO);

    /**
     * Entire collection of frameworks
     * @return list of javascript frameworks
     */
    List<JavaScriptFrameworkDTO> findAllFrameworks();

    /**
     * Finds saved JavaScriptFramework and returns its dto
     * @param frameworkId id of the framework
     * @return dto if found or Optional.empty() if not
     */
    Optional<JavaScriptFrameworkDTO> findFrameworkById(Long frameworkId);

    /**
     * Updates saved JavaScriptFramework
     * @param frameworkId id of the saved framework
     * @param dto with the updated values
     * @throws JavascriptFrameworkNotFoundException if a framework with the given id does not exist
     */
    void update(Long frameworkId, JavaScriptFrameworkDTO dto);

    /**
     * Deletes saved JavaScriptFramework
     * @param frameworkId id of the framework to be deleted
     * @throws JavascriptFrameworkNotFoundException if a framework with the given id does not exist
     */
    void delete(Long frameworkId);

    /**
     * Adds another version to an existing framework
     * @param name of the framework
     * @param newVersion version to be added
     * @throws JavascriptFrameworkNotFoundException if a framework with the given name does not exist
     * @throws JavascriptFrameworkDuplicateException if the given version already exists
     */
    void addVersion(String name, String newVersion);

    /**
     * Search for a optional based on its properties
     * @param name optional case-insensitive
     * @param version optional
     * @param deprecationDate optional
     * @param hypeLevel optional
     * @return list of matching JavaScriptFramework or an empty list
     */
    List<JavaScriptFrameworkDTO> search(String name, String version, LocalDate deprecationDate, HypeLevel hypeLevel);
}
