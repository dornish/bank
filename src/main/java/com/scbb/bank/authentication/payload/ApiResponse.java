package com.scbb.bank.authentication.payload;

public class ApiResponse {
	private Boolean success;
	private String message;

	public ApiResponse(Boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public ApiResponse() {
	}

	public static ApiResponse create() {
		return new ApiResponse();
	}

	public static ApiResponse create(Boolean success, String message) {
		return new ApiResponse(success, message);
	}

	public Boolean getSuccess() {
		return success;
	}

	public ApiResponse setSuccess(Boolean success) {
		this.success = success;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public ApiResponse setMessage(String message) {
		this.message = message;
		return this;
	}
}
