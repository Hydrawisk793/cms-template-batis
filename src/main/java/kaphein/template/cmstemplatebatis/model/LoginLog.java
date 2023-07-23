package kaphein.template.cmstemplatebatis.model;

import kaphein.template.cmstemplatebatis.persistence.AbstractEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.MapUtils;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

@Setter
@Getter
@NoArgsConstructor
public class LoginLog
    extends AbstractEntity
{
    @Override
    public Object getPrimaryKey()
    {
        return getLoginLogUid();
    }

    @Override
    public LoginLog assignMap(Map<String, Object> map)
    {
        if(null != map)
        {
            if(map.containsKey("loginLogUid"))
            {
                setLoginLogUid(MapUtils.getLong(map, "loginLogUid", null));
            }

            if(map.containsKey("userUid"))
            {
                setUserUid(MapUtils.getLong(map, "userUid", null));
            }

            if(map.containsKey("loginName"))
            {
                setLoginName(MapUtils.getString(map, "loginName", null));
            }

            if(map.containsKey("ipAddress"))
            {
                setIpAddress(MapUtils.getString(map, "ipAddress", null));
            }

            if(map.containsKey("loginSucceeded"))
            {
                setLoginSucceeded(MapUtils.getBoolean(map, "loginSucceeded", null));
            }

            if(map.containsKey("createdAt"))
            {
                setCreatedAt(convertToLocalDateTime(map.getOrDefault("createdAt", null)));
            }
        }

        return this;
    }

    @Override
    public Map<String, Object> toMap(Collection<String> propertyNames, Supplier<Map<String, Object>> mapSupplier)
    {
        final Map<String, Object> map = mapSupplier.get();

        if(null == propertyNames || propertyNames.contains("loginLogUid"))
        {
            map.put("loginLogUid", getLoginLogUid());
        }

        if(null == propertyNames || propertyNames.contains("userUid"))
        {
            map.put("userUid", getUserUid());
        }

        if(null == propertyNames || propertyNames.contains("loginName"))
        {
            map.put("loginName", getLoginName());
        }

        if(null == propertyNames || propertyNames.contains("ipAddress"))
        {
            map.put("ipAddress", getIpAddress());
        }

        if(null == propertyNames || propertyNames.contains("loginSucceeded"))
        {
            map.put("loginSucceeded", getLoginSucceeded());
        }

        if(null == propertyNames || propertyNames.contains("createdAt"))
        {
            map.put("createdAt", getCreatedAt());
        }

        return map;
    }

    private Long loginLogUid;

    private Long userUid;

    private String loginName;

    private String ipAddress;

    private Boolean loginSucceeded;

    private LocalDateTime createdAt;
}
