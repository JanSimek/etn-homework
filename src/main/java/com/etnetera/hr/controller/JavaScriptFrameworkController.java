package com.etnetera.hr.controller;

import com.etnetera.hr.data.HypeLevel;
import com.etnetera.hr.dto.JavaScriptFrameworkDTO;
import com.etnetera.hr.exception.JavascriptFrameworkNotFoundException;
import com.etnetera.hr.service.JavaScriptFrameworkService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for managing Javascript Frameworks
 * 
 * @author Etnetera
 */
@RestController
@RequestMapping("api/v1/frameworks")
public class JavaScriptFrameworkController {

	private final JavaScriptFrameworkService service;

	public JavaScriptFrameworkController(JavaScriptFrameworkService service) {
		this.service = service;
	}

	@GetMapping
	public List<JavaScriptFrameworkDTO> frameworks() {
		return service.findAllFrameworks();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public JavaScriptFrameworkDTO createFramework(@RequestBody @Valid JavaScriptFrameworkDTO dto) {

		return service.createFramework(dto);
	}

	@GetMapping("/{frameworkId}")
	public JavaScriptFrameworkDTO getFramework(@PathParam("frameworkId") Long frameworkId) {
		var framework = service.findFrameworkById(frameworkId);

		return framework.orElseThrow(JavascriptFrameworkNotFoundException::new);
	}

	@PutMapping("/{frameworkId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateFramework(@PathVariable("frameworkId") Long frameworkId, @RequestBody JavaScriptFrameworkDTO dto) {
		service.update(frameworkId, dto);
	}

	@DeleteMapping("/{frameworkId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteFramework(@PathVariable("frameworkId") Long frameworkId) {
		service.delete(frameworkId);
	}

	@GetMapping("/search")
	public List<JavaScriptFrameworkDTO> searchFrameworks(@RequestParam(required = false) String name,
														 @RequestParam(required = false) String version,
														 @RequestParam(required = false) LocalDate deprecationDate,
														 @RequestParam(required = false) HypeLevel hypeLevel) {
		return service.search(name, version, deprecationDate, hypeLevel);
	}

	@PostMapping("/{frameworkName}/versions/{version}")
	@ResponseStatus(HttpStatus.CREATED)
	public void createVersion(@PathVariable("frameworkName") String frameworkName, @PathVariable("version") String version) {

		service.addVersion(frameworkName, version);
	}

}
