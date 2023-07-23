package kaphein.template.cmstemplatebatis.auth;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

import kaphein.template.cmstemplatebatis.persistence.AbstractEntity;
import org.apache.commons.collections4.MapUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RememberMeToken extends AbstractEntity
{
    public RememberMeToken(Map<String, Object> map)
    {
        assignMap(map);
    }

    @Override
    public Object getPrimaryKey()
    {
        return getSeriesId();
    }

    @Override
    public AbstractEntity assignMap(Map<String, Object> map)
    {
        if(null != map)
        {
            if(map.containsKey("seriesId"))
            {
                setSeriesId(MapUtils.getString(map, "seriesId", null));
            }

            if(map.containsKey("loginName"))
            {
                setLoginName(MapUtils.getString(map, "loginName", null));
            }

            if(map.containsKey("tokenValue"))
            {
                setTokenValue(MapUtils.getString(map, "tokenValue", null));
            }

            if(map.containsKey("lastUsedAt"))
            {
                setLastUsedAt(convertToLocalDateTime(map.getOrDefault("lastUsedAt", null)));
            }
        }

        return this;
    }

    @Override
    public Map<String, Object> toMap(Collection<String> propertyNames, Supplier<Map<String, Object>> mapSupplier)
    {
        final Map<String, Object> map = mapSupplier.get();

        if(null == propertyNames || propertyNames.contains("seriesId"))
        {
            map.put("seriesId", getSeriesId());
        }

        if(null == propertyNames || propertyNames.contains("loginName"))
        {
            map.put("loginName", getLoginName());
        }

        if(null == propertyNames || propertyNames.contains("tokenValue"))
        {
            map.put("tokenValue", getTokenValue());
        }

        if(null == propertyNames || propertyNames.contains("lastUsedAt"))
        {
            map.put("lastUsedAt", getLastUsedAt());
        }

        return map;
    }

    private String seriesId;

    private String loginName;

    private String tokenValue;

    private LocalDateTime lastUsedAt;
}
