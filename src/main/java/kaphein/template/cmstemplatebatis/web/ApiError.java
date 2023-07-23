package kaphein.template.cmstemplatebatis.web;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 *  API 호출로 인해 발생한 오류를 JSON으로 표현한다.
 *
 *  @param <T> `data`필드의 타입
 */
public class ApiError<T>
{
    public ApiError()
    {
        this(ApiErrorCode.INTERNAL_ERROR, null, null);
    }

    @SuppressWarnings({"unchecked"})
    public ApiError(ApiError<T> src)
    {
        this(src.code, src.message, (T)src.data);
    }

    public ApiError(ApiErrorCode code)
    {
        this(code, null, null);
    }

    public ApiError(ApiErrorCode code, String message)
    {
        this(code, message, null);
    }

    public ApiError(ApiErrorCode code, String message, T data)
    {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ApiErrorCode getCode()
    {
        return code;
    }

    public String getMessage()
    {
        return message;
    }

    @SuppressWarnings("unchecked")
    public T getData()
    {
        return (T)data;
    }

    public Map<String, Object> toMap()
    {
        return toMap(HashMap::new);
    }

    public Map<String, Object> toMap(Supplier<Map<String, Object>> mapSupplier)
    {
        final Map<String, Object> map = mapSupplier.get();
        map.put("code", code.getCode());
        map.put("message", message);
        map.put("data", data);

        return map;
    }

    /**
     *  오류 코드
     */
    private final ApiErrorCode code;

    /**
     *  오류 메시지
     */
    private final String message;

    /**
     *  오류와 관련된 추가적인 메타데이터
     */
    private final Object data;
}
