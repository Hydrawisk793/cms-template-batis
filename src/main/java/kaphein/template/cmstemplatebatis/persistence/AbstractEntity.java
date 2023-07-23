package kaphein.template.cmstemplatebatis.persistence;

import jdk.vm.ci.meta.Local;
import kaphein.template.cmstemplatebatis.LetterCaseUtils;

import java.time.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 엔티티의 기본 클래스
 * 모든 엔티티는 이 클래스를 상속하는 것으로 구현되어야 한다.
 */
public abstract class AbstractEntity
{
    /**
     * 엔티티의 기본키 값을 반환한다.
     *
     * @return 엔티티의 기본키
     */
    public abstract Object getPrimaryKey();

    /**
     * {@link Map}에 지정된 필드 이름 - 값 pair를 각 엔티티의 필드에 대입한다.
     *
     * @param map 필드 이름 - 값 pair로 이뤄진 map 인스턴스
     * @return 엔티티 인스턴스 자기 자신
     */
    public abstract AbstractEntity assignMap(Map<String, Object> map);

    public AbstractEntity assignDatabaseTableRow(Map<String, Object> row)
    {
        return assignMap(row);
    }

    public final Map<String, Object> toMap()
    {
        return toMap(null, HashMap::new);
    }

    public final Map<String, Object> toMap(Supplier<Map<String, Object>> mapSupplier)
    {
        return toMap(null, mapSupplier);
    }

    public final Map<String, Object> toMap(Collection<String> propertyNames)
    {
        return toMap(propertyNames, HashMap::new);
    }

    public abstract Map<String, Object> toMap(
        Collection<String> propertyNames,
        Supplier<Map<String, Object>> mapSupplier
    );

    public final Map<String, Object> toDatabaseTableRow()
    {
        return toDatabaseTableRow(null, HashMap::new);
    }

    public Map<String, Object> toDatabaseTableRow(Supplier<Map<String, Object>> mapSupplier)
    {
        return toMap(null, mapSupplier);
    }

    public Map<String, Object> toDatabaseTableRow(Collection<String> fieldNames)
    {
        return toDatabaseTableRow(fieldNames, HashMap::new);
    }

    public Map<String, Object> toDatabaseTableRow(
        Collection<String> fieldNames,
        Supplier<Map<String, Object>> mapSupplier
    )
    {
        return toMap(fieldNames, mapSupplier);
    }

    /**
     * 지정된 값의 타입을 {@link java.lang.Boolean} 타입으로 변환한다.
     * `null`은 `false`로 변환되지 않고 `null`로 취급된다.
     *
     * @param value 변환하려는 값
     * @return 변환된 값
     */
    protected final Boolean convertToBoolean(Object value)
    {
        return convertToBoolean(value, false);
    }

    /**
     * 지정된 값의 타입을 {@link java.lang.Boolean} 타입으로 변환한다.
     *
     * @param value       변환하려는 값
     * @param nullAsFalse `true`인 경우 `null`은 `false`가 된다.
     * @return 변환된 값
     */
    protected Boolean convertToBoolean(Object value, boolean nullAsFalse)
    {
        Boolean result = null;

        if(null == value)
        {
            result = (nullAsFalse ? false : null);
        }
        else if(value instanceof Number)
        {
            result = 0 != ((Number) value).intValue();
        }
        else if(value instanceof String)
        {
            result = Boolean.valueOf((String) value);
        }
        else
        {
            result = Boolean.valueOf(value.toString());
        }

        return result;
    }

    protected Instant convertToInstant(LocalDateTime value, ZoneId zoneId)
    {
        return (null != value ? value.atZone(zoneId).toInstant() : null);
    }

    protected Instant convertToInstant(OffsetDateTime value)
    {
        return (null != value ? value.toInstant() : null);
    }

    protected Instant convertToInstant(ZonedDateTime value)
    {
        return (null != value ? value.toInstant() : null);
    }

    /**
     * 지정된 값의 타입을 {@link java.time.Instant} 타입으로 변환 한다.
     *
     * @param value  변환하려는 값
     * @param zoneId 변환하려는 값의 타입이 {@link java.time.LocalDateTime} 타입인 경우 해당 일시에 맞는 timezone의 ID. 그 외에는 `null`로 지정될 수 있다.
     * @return 변환된 값
     */
    protected Instant convertToInstant(Object value, ZoneId zoneId)
    {
        Instant result = null;

        if(null != value)
        {
            if(value instanceof LocalDateTime)
            {
                result = convertToInstant((LocalDateTime) value, zoneId);
            }
            else if(value instanceof OffsetDateTime)
            {
                result = convertToInstant((OffsetDateTime) value);
            }
            else if(value instanceof ZonedDateTime)
            {
                result = convertToInstant((ZonedDateTime) value);
            }
            else
            {
                result = Instant.parse(value.toString());
            }
        }

        return result;
    }

    protected LocalDateTime convertToLocalDateTime(Object value)
    {
        LocalDateTime result = null;

        if(null != value)
        {
            if(value instanceof LocalDateTime)
            {
                result = (LocalDateTime) value;
            }
            else if(value instanceof OffsetDateTime)
            {
                result = ((OffsetDateTime) value).toLocalDateTime();
            }
            else if(value instanceof ZonedDateTime)
            {
                result = ((ZonedDateTime) value).toLocalDateTime();
            }
            else
            {
                result = LocalDateTime.parse(value.toString());
            }
        }

        return result;
    }

    /**
     * 엔티티 필드 값을 데이터베이스 컬럼 값으로 변환한다.
     *
     * @param value 변환하려는 값
     * @return 변환된 값
     */
    protected Integer fieldValueToColumnValue(Boolean value)
    {
        return (null == value ? null : (!value ? 0 : 1));
    }

    protected List<String> fieldNamesToColumnNames(Collection<String> fieldNames)
    {
        return fieldNames.stream().map(this::fieldNameToColumnName).collect(Collectors.toList());
    }

    protected String fieldNameToColumnName(String fieldName)
    {
        return LetterCaseUtils.toSnakeCase(fieldName);
    }
}
