package kaphein.template.cmstemplatebatis.model;

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
public class UserRoleEntry
    extends AbstractEntity
{
    @Override
    public Object getPrimaryKey()
    {
        return getEntryUid();
    }

    @Override
    public AbstractEntity assignMap(Map<String, Object> map)
    {
        if(null != map)
        {
            if(map.containsKey("entryUid"))
            {
                setEntryUid(MapUtils.getLong(map, "entryUid", null));
            }

            if(map.containsKey("userUid"))
            {
                setUserUid(MapUtils.getLong(map, "userUid", null));
            }

            if(map.containsKey("roleName"))
            {
                setRoleName(MapUtils.getString(map, "roleName", null));
            }

            if(map.containsKey("createdAt"))
            {
                setCreatedAt(convertToLocalDateTime(map.getOrDefault("createdAt", null)));
            }

            if(map.containsKey("createdBy"))
            {
                setCreatedBy(MapUtils.getLong(map, "createdBy", null));
            }
        }

        return this;
    }

    @Override
    public Map<String, Object> toMap(Collection<String> propertyNames, Supplier<Map<String, Object>> mapSupplier)
    {
        final Map<String, Object> map = mapSupplier.get();

        if(null == propertyNames || propertyNames.contains("entryUid"))
        {
            map.put("entryUid", getEntryUid());
        }

        if(null == propertyNames || propertyNames.contains("userUid"))
        {
            map.put("userUid", getUserUid());
        }

        if(null == propertyNames || propertyNames.contains("roleName"))
        {
            map.put("roleName", getRoleName());
        }

        if(null == propertyNames || propertyNames.contains("createdAt"))
        {
            map.put("createdAt", getCreatedAt());
        }

        if(null == propertyNames || propertyNames.contains("createdBy"))
        {
            map.put("createdBy", getCreatedBy());
        }

        return map;
    }

    private Long entryUid;

    private Long userUid;

    private String roleName;

    private LocalDateTime createdAt;

    private Long createdBy;
}
