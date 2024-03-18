package api.common.result;

import api.common.error.ErrorCodeMold;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultWrapper<T> {

    private Result result;
    private T body;

    public static <T> ResultWrapper<T> OK(T body){
        ResultWrapper<T> wrappedResult = new ResultWrapper<>();
        wrappedResult.result = Result.OK();
        wrappedResult.body = body;
        return wrappedResult;
    }

    public static <T> ResultWrapper<T> ERROR(Result result) {
        ResultWrapper<T> wrappedResult = new ResultWrapper<>();
        wrappedResult.result = result;
        return wrappedResult;
    }

    public static <T> ResultWrapper<T> ERROR(ErrorCodeMold errorCodeMold) {
        ResultWrapper<T> wrappedResult = new ResultWrapper<>();
        wrappedResult.result = Result.ERROR(errorCodeMold);
        return wrappedResult;
    }

    public static <T> ResultWrapper<T> ERROR(ErrorCodeMold errorCodeMold,
                                             String description) {
        ResultWrapper<T> wrappedResult = new ResultWrapper<>();
        wrappedResult.result = Result.ERROR(errorCodeMold, description);
        return wrappedResult;
    }

    public static <T> ResultWrapper<T> ERROR(ErrorCodeMold errorCodeMold,
                                             Throwable tx) {
        ResultWrapper<T> wrappedResult = new ResultWrapper<>();
        wrappedResult.result = Result.ERROR(errorCodeMold, tx);
        return wrappedResult;
    }
}
