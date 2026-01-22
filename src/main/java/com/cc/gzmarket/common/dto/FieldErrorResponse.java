package com.cc.gzmarket.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "FieldErrorResponse")
public record FieldErrorResponse(
	@Schema(description = "필드 이름", example = "email")
	String fieldName,

	@Schema(description = "필드 에러 이유", example = "이메일 형식이 아닙니다")
	String reason
) {}
