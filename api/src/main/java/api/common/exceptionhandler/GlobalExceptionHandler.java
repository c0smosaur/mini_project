package api.common.exceptionhandler;

import api.common.error.GeneralErrorCode;
import api.common.result.ResultWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResultWrapper<Object>> exception(Exception exception) {
        log.error("",exception);

        return ResponseEntity
                .status(500)
                .body(
                        ResultWrapper.ERROR(GeneralErrorCode.SERVER_ERROR)
                );
    }
}
