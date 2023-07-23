package kaphein.template.cmstemplatebatis.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class EntityFieldValueInvalidException extends RuntimeException
{
    public EntityFieldValueInvalidException(String entityName, String fieldName)
    {
        this(entityName, fieldName, Optional.empty(), null);
    }

    public EntityFieldValueInvalidException(String entityName, String fieldName, Throwable cause)
    {
        this(entityName, fieldName, Optional.empty(), cause);
    }

    public EntityFieldValueInvalidException(String entityName, String fieldName, Object fieldValue)
    {
        this(entityName, fieldName, Optional.ofNullable(fieldValue), null);
    }

    public EntityFieldValueInvalidException(String entityName, String fieldName, Object fieldValue, Throwable cause)
    {
        this(entityName, fieldName, Optional.ofNullable(fieldValue), cause);
    }

    private EntityFieldValueInvalidException(
        String entityName,
        String fieldName,
        Optional<Object> fieldValue,
        Throwable cause
    )
    {
        super(cause);

        if(null == entityName)
        {
            throw new IllegalArgumentException("'entityName' cannot be null");
        }

        if(null == fieldName)
        {
            throw new IllegalArgumentException("'fieldName' cannot be null");
        }

        this.entityName = entityName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getEntityName()
    {
        return entityName;
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public Optional<Object> getFieldValue()
    {
        return fieldValue;
    }

    @Override
    public String getMessage()
    {
        String message = null;

        if(fieldValue.isPresent())
        {
            message = String.format("The value %s for %s.%s is invalid", fieldValue.get(), entityName, fieldName);
        }
        else
        {
            message = String.format(
                "The value for %s.%s is missing or not in the expected format",
                entityName,
                fieldName
            );
        }

        return message;
    }

    public Map<String, Object> toMap()
    {
        return toMap(HashMap::new);
    }

    public Map<String, Object> toMap(Supplier<Map<String, Object>> mapSupplier)
    {
        final Map<String, Object> map = mapSupplier.get();

        map.put("entityName", entityName);
        map.put("fieldName", fieldName);

        if(fieldValue.isPresent())
        {
            map.put("fieldValue", fieldValue.get());
        }

        return map;
    }

    private static final long serialVersionUID = -3699617703916461246L;

    private final String entityName;

    private final String fieldName;

    private final transient Optional<Object> fieldValue;
}
