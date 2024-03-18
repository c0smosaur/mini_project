package api.common.error;

public interface ErrorCodeMold {

    Integer getHttpStatusCode();
    Integer getErrorCode();
    String getDescription();
}
