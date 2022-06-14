package com.etnetera.hr.data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Simple data entity describing basic properties of every JavaScript framework.
 * 
 * @author Etnetera
 *
 */
@Entity
public class JavaScriptFramework {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false, length = 30, unique = true)
	private String name;

	@ElementCollection
	private Set<String> version = new HashSet<>();

	private LocalDate deprecationDate;

	@Enumerated(EnumType.STRING)
	private HypeLevel hypeLevel = HypeLevel.NONE;

	public JavaScriptFramework() {
	}

	public JavaScriptFramework(String name, Set<String> version, LocalDate deprecationDate, HypeLevel hypeLevel) {
		this.name = name;
		this.version = version;
		this.deprecationDate = deprecationDate;
		this.hypeLevel = hypeLevel;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDeprecationDate() {
		return deprecationDate;
	}

	public void setDeprecationDate(LocalDate deprecationDate) {
		this.deprecationDate = deprecationDate;
	}

	public Set<String> getVersion() {
		return version;
	}

	public void setVersion(Set<String> version) {
		this.version = version;
	}

	public HypeLevel getHypeLevel() {
		return hypeLevel;
	}

	public void setHypeLevel(HypeLevel hypeLevel) {
		this.hypeLevel = hypeLevel;
	}

	@Override
	public String toString() {
		return "JavaScriptFramework{" +
				"id=" + id +
				", name='" + name + '\'' +
				", version='" + version + '\'' +
				", deprecationDate=" + deprecationDate +
				", hypeLevel=" + hypeLevel +
				'}';
	}
}
