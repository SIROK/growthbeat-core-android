package com.growthbeat.http;

import java.util.Map;

public class HttpRequest {

	private String method;
	private String path;
	private Map<String, String> headers;
	private Map<String, Object> parameters;

	public HttpRequest() {
		super();
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public HttpRequest withMethod(String method) {
		this.method = method;
		return this;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public HttpRequest withPath(String path) {
		this.path = path;
		return this;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public HttpRequest withHeaders(Map<String, String> headers) {
		this.headers = headers;
		return this;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public HttpRequest withParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
		return this;
	}

}
