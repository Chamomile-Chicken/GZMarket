package com.cc.gzmarket.common.exception;

import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.cc.gzmarket.common.dto.ErrorResponse;
import com.cc.gzmarket.common.dto.FieldErrorResponse;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleCommonException(BusinessException e) {
		log.warn("CommonException - code: {}, message: {}", e.getCode(), e.getMessage());
		return ResponseEntity.status(e.getHttpStatus())
			.body(ErrorResponse.of(e.getErrorCode(), e.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
		List<FieldErrorResponse> fieldErrors = e.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(error -> new FieldErrorResponse(error.getField(), error.getDefaultMessage()))
			.toList();

		return ResponseEntity.badRequest()
			.body(ErrorResponse.of(CommonExceptionCode.FIELD_ERROR, fieldErrors));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException e) {
		List<FieldErrorResponse> fieldErrors = e.getConstraintViolations()
			.stream()
			.map(path -> new FieldErrorResponse(path.getPropertyPath().toString(), path.getMessage()))
			.toList();

		return ResponseEntity.badRequest()
			.body(ErrorResponse.of(CommonExceptionCode.FIELD_ERROR, fieldErrors));
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
		log.warn("지원하지 않는 HTTP 메서드 요청: {}", e.getMethod());

		String supported = (e.getSupportedMethods() == null)
			? ""
			: " (지원 메서드: " + String.join(", ", e.getSupportedMethods()) + ")";

		return ResponseEntity.status(CommonExceptionCode.METHOD_NOT_ALLOWED.getHttpStatus())
			.body(ErrorResponse.of(CommonExceptionCode.METHOD_NOT_ALLOWED,
				CommonExceptionCode.METHOD_NOT_ALLOWED.getMessage() + supported));
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorResponse> handleMissingParameter(MissingServletRequestParameterException e) {
		List<FieldErrorResponse> fieldErrors = Collections.singletonList(
			new FieldErrorResponse(
				e.getParameterName(),
				e.getParameterType() + " 타입의 필수 파라미터입니다"
			)
		);

		return ResponseEntity.badRequest()
			.body(ErrorResponse.of(CommonExceptionCode.MISSING_PARAMETER, fieldErrors));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException e) {
		log.warn("요청 본문 파싱 실패: {}", e.getMessage());
		return ResponseEntity.badRequest()
			.body(ErrorResponse.of(CommonExceptionCode.INVALID_FORMAT));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
		String requiredType = (e.getRequiredType() != null) ? e.getRequiredType().getSimpleName() : "Unknown";

		List<FieldErrorResponse> fieldErrors = Collections.singletonList(
			new FieldErrorResponse(e.getName(), requiredType + " 타입이어야 합니다")
		);

		return ResponseEntity.badRequest()
			.body(ErrorResponse.of(CommonExceptionCode.TYPE_MISMATCH, fieldErrors));
	}
	
	@ExceptionHandler(AuthorizationDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAuthorizationDenied(AuthorizationDeniedException e) {
		log.warn("권한 없음: {}", e.getMessage());
		return ResponseEntity.status(CommonExceptionCode.FORBIDDEN.getHttpStatus())
			.body(ErrorResponse.of(CommonExceptionCode.FORBIDDEN));
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException e) {
		log.warn("존재하지 않는 리소스 요청: {}", e.getResourcePath());
		return ResponseEntity.status(CommonExceptionCode.NOT_FOUND.getHttpStatus())
			.body(ErrorResponse.of(CommonExceptionCode.NOT_FOUND));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception e) {
		log.error("처리되지 않은 예외 발생: {}", e.getClass().getSimpleName(), e);
		return ResponseEntity.status(CommonExceptionCode.INTERNAL_SERVER_ERROR.getHttpStatus())
			.body(ErrorResponse.of(CommonExceptionCode.INTERNAL_SERVER_ERROR));
	}
}