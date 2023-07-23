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
public class Action extends AbstractEntity
{
    @Override
    public Object getPrimaryKey()
    {
        return getActionUid();
    }

    @Override
    public Action assignMap(Map<String, Object> map)
    {
        if(null != map)
        {
            if(map.containsKey("actionUid"))
            {
                setActionUid(MapUtils.getLong(map, "actionUid", null));
            }

            if(map.containsKey("actionType"))
            {
                setActionType(MapUtils.getInteger(map, "actionType", null));
            }

            if(map.containsKey("actionDescription"))
            {
                setActionDescription(MapUtils.getString(map, "actionDescription", null));
            }

            if(map.containsKey("permissionUid"))
            {
                setPermissionUid(MapUtils.getLong(map, "permissionUid", null));
            }

            if(map.containsKey("actionUrl"))
            {
                setActionUrl(MapUtils.getString(map, "actionUrl", null));
            }

            if(map.containsKey("regDt"))
            {
                setRegDt(convertToLocalDateTime(map.getOrDefault("regDt", null)));
            }

            if(map.containsKey("regUserUid"))
            {
                setRegUserUid(MapUtils.getLong(map, "regUserUid", null));
            }

            if(map.containsKey("uptDt"))
            {
                setUptDt(convertToLocalDateTime(map.getOrDefault("uptDt", null)));
            }

            if(map.containsKey("uptUserUid"))
            {
                setUptUserUid(MapUtils.getLong(map, "uptUserUid", null));
            }
        }

        return this;
    }

    @Override
    public Map<String, Object> toMap(Collection<String> propertyNames, Supplier<Map<String, Object>> mapSupplier)
    {
        final Map<String, Object> map = mapSupplier.get();

        if(null == propertyNames || propertyNames.contains("actionUid"))
        {
            map.put("actionUid", getActionUid());
        }

        if(null == propertyNames || propertyNames.contains("actionType"))
        {
            map.put("actionType", getActionType());
        }

        if(null == propertyNames || propertyNames.contains("actionDescription"))
        {
            map.put("actionDescription", getActionDescription());
        }

        if(null == propertyNames || propertyNames.contains("permissionUid"))
        {
            map.put("permissionUid", getPermissionUid());
        }

        if(null == propertyNames || propertyNames.contains("actionUrl"))
        {
            map.put("actionUrl", getActionUrl());
        }

        if(null == propertyNames || propertyNames.contains("regDt"))
        {
            map.put("regDt", getRegDt());
        }

        if(null == propertyNames || propertyNames.contains("regUserUid"))
        {
            map.put("regUserUid", getRegUserUid());
        }

        if(null == propertyNames || propertyNames.contains("uptDt"))
        {
            map.put("uptDt", getUptDt());
        }

        if(null == propertyNames || propertyNames.contains("uptUserUid"))
        {
            map.put("uptUserUid", getUptUserUid());
        }

        return map;
    }

    private Long actionUid;

    private Integer actionType;

    private String actionDescription;

    private Long permissionUid;

    private String actionUrl;

    private LocalDateTime regDt;

    private Long regUserUid;

    private LocalDateTime uptDt;

    private Long uptUserUid;
}
