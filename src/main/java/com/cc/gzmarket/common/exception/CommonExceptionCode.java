package com.cc.gzmarket.common.exception;

import org.springframework.http.HttpStatus;

import com.cc.gzmarket.common.base.BaseErrorCode;

import lombok.Getter;

@Getter
public enum CommonExceptionCode implements BaseErrorCode {

	FIELD_ERROR(HttpStatus.BAD_REQUEST, "입력값 검증 오류가 발생했습니다"),
	MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "필수 파라미터가 누락되었습니다"),
	INVALID_FORMAT(HttpStatus.BAD_REQUEST, "요청 형식이 올바르지 않습니다"),
	TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "파라미터 타입이 올바르지 않습니다"),

	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다"),
	NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다"),
	FORBIDDEN(HttpStatus.FORBIDDEN, "해당 리소스에 대한 접근 권한이 없습니다"),

	EXTERNAL_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "외부 API 호출 중 오류가 발생했습니다"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다");

	private final HttpStatus httpStatus;
	private final String message;

	CommonExceptionCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

	@Override
	public String getCode() {
		return name();
	}
}