package kaphein.template.cmstemplatebatis.web;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ApiSuccessResponse<T>
{
    public ApiSuccessResponse()
    {
        this(new ApiSuccessResponse<>(null));
    }

    @SuppressWarnings({"unchecked"})
    public ApiSuccessResponse(ApiSuccessResponse<T> src)
    {
        this((T)src.result);
    }

    public ApiSuccessResponse(T result)
    {
        this.result = result;
    }

    @SuppressWarnings("unchecked")
    public T getResult()
    {
        return (T)result;
    }

    public Map<String, Object> toMap()
    {
        return toMap(HashMap::new);
    }

    public Map<String, Object> toMap(Supplier<Map<String, Object>> mapSupplier)
    {
        final Map<String, Object> map = mapSupplier.get();
        map.put("result", result);

        return map;
    }

    @JsonInclude
    private final Object result;
}
