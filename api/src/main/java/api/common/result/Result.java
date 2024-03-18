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

    private Integer resultCode;
    private String resultMessage;
    private String resultDescription;

    public static Result OK(){
        return Result.builder()
                .resultCode(GeneralErrorCode.OK.getErrorCode())
                .resultMessage(GeneralErrorCode.OK.getDescription())
                .resultDescription("성공")
                .build();
    }

    public static Result ERROR(ErrorCodeMold errorCodeMold){
        return Result.builder()
                .resultCode(errorCodeMold.getErrorCode())
                .resultMessage(errorCodeMold.getDescription())
                .resultDescription("오류 발생")
                .build();
    }

    public static Result ERROR(ErrorCodeMold errorCodeMold,
                               String description){
        return Result.builder()
                .resultCode(errorCodeMold.getErrorCode())
                .resultMessage(errorCodeMold.getDescription())
                .resultDescription(description)
                .build();
    }

    public static Result ERROR(ErrorCodeMold errorCodeMold,
                               Throwable tx){
        return Result.builder()
                .resultCode(errorCodeMold.getErrorCode())
                .resultMessage(errorCodeMold.getDescription())
                .resultDescription(tx.getLocalizedMessage())
                .build();
    }
}
