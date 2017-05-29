package com.restdoc.hello;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author sumit
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Greeting implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String content;

	public Greeting() {

	}

	/**
	 * @param id
	 * @param content
	 */
	public Greeting(Long id, String content) {
		this.id = id;
		this.content = content;
	}

	/**
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

}
