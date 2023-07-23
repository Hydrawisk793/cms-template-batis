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
public class RolePermissionEntry extends AbstractEntity
{
    @Override
    public Object getPrimaryKey()
    {
        return getEntryUid();
    }

    @Override
    public RolePermissionEntry assignMap(Map<String, Object> map)
    {
        if(null != map)
        {
            if(map.containsKey("entryUid"))
            {
                setEntryUid(MapUtils.getLong(map, "entryUid", null));
            }

            if(map.containsKey("roleName"))
            {
                setRoleName(MapUtils.getString(map, "roleName", null));
            }

            if(map.containsKey("permissionName"))
            {
                setPermissionName(MapUtils.getString(map, "permissionName", null));
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

        if(null == propertyNames || propertyNames.contains("roleName"))
        {
            map.put("roleName", getRoleName());
        }

        if(null == propertyNames || propertyNames.contains("permissionName"))
        {
            map.put("permissionName", getPermissionName());
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

    private String roleName;

    private String permissionName;

    private LocalDateTime createdAt;

    private Long createdBy;
}
