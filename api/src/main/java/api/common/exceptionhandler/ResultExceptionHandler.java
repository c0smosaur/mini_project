package api.common.exceptionhandler;

import api.common.error.ErrorCodeMold;
import api.common.exception.ResultException;
import api.common.result.ResultWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(value = Integer.MIN_VALUE) // 가장 먼저 실행
public class ResultExceptionHandler {
    @ExceptionHandler(value = ResultException.class)
    public ResponseEntity<ResultWrapper<Object>> resultException(
            ResultException resultException
    ){
        log.error("", resultException);
        ErrorCodeMold errorCode = resultException.getErrorCodeMold();

        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(
                        ResultWrapper.ERROR(errorCode,
                                resultException.getErrorDescription())
                );
    }
}
