package api.common.result;

import api.common.error.ErrorCodeMold;
import api.common.error.GeneralErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result {

    private Integer code;
    private String message;
    private String description;

    public static Result OK(){
        return Result.builder()
                .code(GeneralErrorCode.OK.getErrorCode())
                .message(GeneralErrorCode.OK.getDescription())
                .description("성공")
                .build();
    }

    public static Result ERROR(ErrorCodeMold errorCodeMold){
        return Result.builder()
                .code(errorCodeMold.getErrorCode())
                .message(errorCodeMold.getDescription())
                .description("오류 발생")
                .build();
    }

    public static Result ERROR(ErrorCodeMold errorCodeMold,
                               String description){
        return Result.builder()
                .code(errorCodeMold.getErrorCode())
                .message(errorCodeMold.getDescription())
                .description(description)
                .build();
    }

    public static Result ERROR(ErrorCodeMold errorCodeMold,
                               Throwable tx){
        return Result.builder()
                .code(errorCodeMold.getErrorCode())
                .message(errorCodeMold.getDescription())
                .description(tx.getLocalizedMessage())
                .build();
    }
}
