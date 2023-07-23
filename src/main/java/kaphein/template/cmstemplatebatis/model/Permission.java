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
public class Permission extends AbstractEntity
{
    @Override
    public Object getPrimaryKey()
    {
        return getPermissionName();
    }

    @Override
    public Permission assignMap(Map<String, Object> map)
    {
        if(null != map)
        {
            if(map.containsKey("permissionName"))
            {
                setPermissionName(MapUtils.getString(map, "permissionName", null));
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

        if(null == propertyNames || propertyNames.contains("permissionName"))
        {
            map.put("permissionName", getPermissionName());
        }

        if(null == propertyNames || propertyNames.contains("displayName"))
        {
            map.put("displayName", getDisplayName());
        }

        if(null == propertyNames || propertyNames.contains("createdAt"))
        {
            map.put("createdAt", getCreatedAt());
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

    private String permissionName;

    private String displayName;

    private LocalDateTime createdAt;

    private Long createdBy;

    private LocalDateTime updatedAt;

    private Long updatedBy;
}
