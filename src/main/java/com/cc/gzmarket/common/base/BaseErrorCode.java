package com.cc.gzmarket.common.base;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {
	String getCode();
	String getMessage();
	HttpStatus getHttpStatus();
}
