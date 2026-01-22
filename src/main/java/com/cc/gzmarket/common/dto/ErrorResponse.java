package com.cc.gzmarket.common.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.cc.gzmarket.common.base.BaseErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ErrorResponseDTO")
public record ErrorResponse(

	@Schema(description = "성공 여부", example = "false")
	boolean success,

	@Schema(description = "에러 코드", example = "FIELD_ERROR")
	String code,

	@Schema(description = "에러 메시지", example = "입력값 검증 오류가 발생했습니다")
	String message,

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Schema(description = "필드 에러(검증 오류 등에서만 포함)")
	List<FieldErrorResponse> fieldErrors,

	@Schema(description = "응답 시간", example = "2025-01-15T10:30:00")
	LocalDateTime timestamp
) {
	// null timestamp 방지(실수로 null 넣어도 now로 보정)
	public ErrorResponse {
		if (timestamp == null) {
			timestamp = LocalDateTime.now();
		}
	}

	public static ErrorResponse of(BaseErrorCode errorCode) {
		return new ErrorResponse(false, errorCode.getCode(), errorCode.getMessage(), null, LocalDateTime.now());
	}

	public static ErrorResponse of(BaseErrorCode ec, String overrideMessage) {
		return new ErrorResponse(false, ec.getCode(), overrideMessage, null, LocalDateTime.now());
	}

	public static ErrorResponse of(BaseErrorCode ec, List<FieldErrorResponse> fieldErrors) {
		return new ErrorResponse(false, ec.getCode(), ec.getMessage(), fieldErrors, LocalDateTime.now());
	}
}