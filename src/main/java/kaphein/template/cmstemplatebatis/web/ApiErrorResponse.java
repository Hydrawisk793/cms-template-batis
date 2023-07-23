package kaphein.template.cmstemplatebatis.web;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ApiErrorResponse<T>
{
    public ApiErrorResponse()
    {
        this(new ApiError<>());
    }

    public ApiErrorResponse(ApiErrorResponse<T> src)
    {
        this(new ApiError<>(src.error));
    }

    public ApiErrorResponse(ApiErrorCode code)
    {
        this(new ApiError<>(code));
    }

    public ApiErrorResponse(ApiErrorCode code, String message)
    {
        this(new ApiError<>(code, message));
    }

    public ApiErrorResponse(ApiErrorCode code, String message, T data)
    {
        this(new ApiError<>(code, message, data));
    }

    public ApiErrorResponse(ApiError<T> error)
    {
        this.error = error;
    }

    public ApiError<T> getError()
    {
        return error;
    }

    public Map<String, Object> toMap()
    {
        return toMap(HashMap::new);
    }

    public Map<String, Object> toMap(Supplier<Map<String, Object>> mapSupplier)
    {
        final Map<String, Object> map = mapSupplier.get();
        map.put("error", error.toMap());

        return map;
    }

    private final ApiError<T> error;
}
