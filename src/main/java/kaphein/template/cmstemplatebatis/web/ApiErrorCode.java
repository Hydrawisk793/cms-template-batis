package kaphein.template.cmstemplatebatis.web;

import java.util.Arrays;
import java.util.Optional;

public enum ApiErrorCode
{
    UNAUTHORIZED(-32001),

    METHOD_NOT_FOUND(-32601),

    INVALID_PARAMS(-32602),

    INTERNAL_ERROR(-32603);

    public static Optional<ApiErrorCode> valueOf(int code)
    {
        return Arrays
            .stream(values())
            .filter(legNo -> legNo.getCode() == code)
            .findFirst();
    }

    ApiErrorCode(int code)
    {
        this.code = code;
    }

    public int getCode()
    {
        return code;
    }

    private final int code;
}
