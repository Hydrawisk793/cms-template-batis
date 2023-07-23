package kaphein.template.cmstemplatebatis;

import java.util.*;
import java.util.stream.Collectors;

public class ParameterInvalidException extends RuntimeException
{
    public ParameterInvalidException(String parameterName)
    {
        this(parameterName, Optional.empty(), null);
    }

    public ParameterInvalidException(String parameterName, Throwable cause)
    {
        this(parameterName, Optional.empty(), cause);
    }

    public ParameterInvalidException(String parameterName, Object parameterValue)
    {
        this(parameterName, Optional.ofNullable(parameterValue), null);
    }

    public ParameterInvalidException(String parameterName, Object parameterValue, Throwable cause)
    {
        this(parameterName, Optional.ofNullable(parameterValue), cause);
    }

    private ParameterInvalidException(String parameterName, Optional<Object> parameterValue, Throwable cause)
    {
        super(cause);

        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
    }

    public String getParameterName()
    {
        return parameterName;
    }

    public Optional<Object> getParameterValue()
    {
        return parameterValue;
    }

    @Override
    public String getMessage()
    {
        return String.format(
            "Parameter '%s' is %s",
            parameterName,
            (parameterValue.isPresent() ? "invalid" : "missing or not in the expected format")
        );
    }

    public Map<String, Object> toMap()
    {
        final List<Map.Entry<String, Object>> entries = new LinkedList<>();
        entries.add(new AbstractMap.SimpleImmutableEntry<>("parameterName", parameterName));
        parameterValue.ifPresent(o -> entries.add(new AbstractMap.SimpleImmutableEntry<>("parameterValue", o)));

        return entries
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static final long serialVersionUID = 468276140891880908L;

    private final String parameterName;

    private final transient Optional<Object> parameterValue;
}
