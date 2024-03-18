package api.common.exception;

import api.common.error.ErrorCodeMold;
import lombok.Getter;

@Getter
public class ResultException extends RuntimeException implements ResultExceptionMold{

    private final ErrorCodeMold errorCodeMold;
    private final String errorDescription;

    // super(errorCodeInterface.getDescription()) ensures that
    // the RuntimeException part of the ApiException object is
    // properly initialized with the error description provided by
    // the errorCodeInterface. This allows RuntimeException to
    // handle the message part of the exception while ApiException
    // handles additional properties specific to your custom exception class.

    public ResultException(ErrorCodeMold errorCodeMold){
        super(errorCodeMold.getDescription());
        this.errorCodeMold = errorCodeMold;
        this.errorDescription = errorCodeMold.getDescription();
    }

    public ResultException(ErrorCodeMold errorCodeMold,
                           String errorDescription){
        super(errorDescription);
        this.errorCodeMold = errorCodeMold;
        this.errorDescription = errorDescription;
    }

    public ResultException(ErrorCodeMold errorCodeMold,
                           Throwable tx){
        super(tx);
        this.errorCodeMold = errorCodeMold;
        this.errorDescription = tx.getLocalizedMessage();
    }

    public ResultException(ErrorCodeMold errorCodeMold,
                           Throwable tx,
                           String errorDescription){
        super(tx);
        this.errorCodeMold = errorCodeMold;
        this.errorDescription = errorDescription;
    }
}
