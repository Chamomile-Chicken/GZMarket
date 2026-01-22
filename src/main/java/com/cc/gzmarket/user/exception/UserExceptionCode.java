package com.cc.gzmarket.user.exception;

import org.springframework.http.HttpStatus;

import com.cc.gzmarket.common.base.BaseErrorCode;

import lombok.Getter;

@Getter
public enum UserExceptionCode implements BaseErrorCode {

	NOT_FOUND_USER(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다");

	private final HttpStatus httpStatus;
	private final String message;

	UserExceptionCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

	@Override
	public String getCode() {
		return name();
	}
}
