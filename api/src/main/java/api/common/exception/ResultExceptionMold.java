package api.common.exception;

import api.common.error.ErrorCodeMold;

public interface ResultExceptionMold {

    ErrorCodeMold getErrorCodeMold();
    String getErrorDescription();
}
