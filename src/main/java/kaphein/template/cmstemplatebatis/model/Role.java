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
public class Role extends AbstractEntity
{
    @Override
    public Object getPrimaryKey()
    {
        return getRoleName();
    }

    @Override
    public AbstractEntity assignMap(Map<String, Object> map)
    {
        if(null != map)
        {
            if(map.containsKey("roleName"))
            {
                setRoleName(MapUtils.getString(map, "roleName", null));
            }

            if(map.containsKey("displayName"))
            {
                setDisplayName(MapUtils.getString(map, "displayName", null));
            }

            if(map.containsKey("createdAt"))
            {
                setCreatedAt(convertToLocalDateTime(map.getOrDefault("createdAt", null)));
            }

            if(map.containsKey("createdBy"))
            {
                setCreatedBy(MapUtils.getLong(map, "createdBy", null));
            }

            if(map.containsKey("updatedAt"))
            {
                setUpdatedAt(convertToLocalDateTime(map.getOrDefault("updatedAt", null)));
            }

            if(map.containsKey("updatedBy"))
            {
                setUpdatedBy(MapUtils.getLong(map, "updatedBy", null));
            }
        }

        return this;
    }

    @Override
    public Map<String, Object> toMap(Collection<String> propertyNames, Supplier<Map<String, Object>> mapSupplier)
    {
        final Map<String, Object> map = mapSupplier.get();

        if(null == propertyNames || propertyNames.contains("roleName"))
        {
            map.put("roleName", getRoleName());
        }

        if(null == propertyNames || propertyNames.contains("displayName"))
        {
            map.put("displayName", getDisplayName());
        }

        if(null == propertyNames || propertyNames.contains("createdBy"))
        {
            map.put("createdBy", getCreatedBy());
        }

        if(null == propertyNames || propertyNames.contains("updatedAt"))
        {
            map.put("updatedAt", getUpdatedAt());
        }

        if(null == propertyNames || propertyNames.contains("updatedBy"))
        {
            map.put("updatedBy", getUpdatedBy());
        }

        return map;
    }

    private String roleName;

    private String displayName;

    private LocalDateTime createdAt;

    private Long createdBy;

    private LocalDateTime updatedAt;

    private Long updatedBy;
}
