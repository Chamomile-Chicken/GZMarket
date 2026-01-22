package com.cc.gzmarket.common.exception;

import org.springframework.http.HttpStatus;

import com.cc.gzmarket.common.base.BaseErrorCode;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

	private final BaseErrorCode errorCode;

	public BusinessException(BaseErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public BusinessException(BaseErrorCode errorCode, Throwable cause) {
		super(errorCode.getMessage(), cause);
		this.errorCode = errorCode;
	}

	// 상황별 메시지를 바꿔서 던지고 싶을 때 (예: "이미 존재하는 이메일입니다")
	public BusinessException(BaseErrorCode errorCode, String overrideMessage) {
		super(overrideMessage);
		this.errorCode = errorCode;
	}

	public String getCode() {
		return errorCode.getCode();
	}

	public HttpStatus getHttpStatus() {
		return errorCode.getHttpStatus();
	}
}
