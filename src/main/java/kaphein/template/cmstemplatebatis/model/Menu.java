package kaphein.template.cmstemplatebatis.model;

import kaphein.template.cmstemplatebatis.persistence.AbstractEntity;
import kaphein.template.cmstemplatebatis.persistence.EntityFieldValueInvalidException;
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
public class Menu extends AbstractEntity
{
    @Override
    public Object getPrimaryKey()
    {
        return getMenuUid();
    }

    public void setTypeDiscriminator(Integer typeDiscriminator)
    {
        if(null != typeDiscriminator && 1 != typeDiscriminator)
        {
            throw new EntityFieldValueInvalidException(getClass().getSimpleName(), "typeDiscriminator", typeDiscriminator);
        }

        setTypeDiscriminatorUnsafe(typeDiscriminator);
    }

    protected final void setTypeDiscriminatorUnsafe(Integer typeDiscriminator)
    {
        this.typeDiscriminator = typeDiscriminator;
    }

    @Override
    public Menu assignMap(Map<String, Object> map)
    {
        if(null != map)
        {
            if(map.containsKey("menuUid"))
            {
                setMenuUid(MapUtils.getLong(map, "menuUid", null));
            }

            if(map.containsKey("typeDiscriminator"))
            {
                setTypeDiscriminator(MapUtils.getInteger(map, "typeDiscriminator", null));
            }

            if(map.containsKey("menuName"))
            {
                setMenuName(MapUtils.getString(map, "menuName", null));
            }

            if(map.containsKey("parentMenuUid"))
            {
                setParentMenuUid(MapUtils.getLong(map, "parentMenuUid", null));
            }

            if(map.containsKey("childIndex"))
            {
                setChildIndex(MapUtils.getInteger(map, "childIndex", null));
            }

            if(map.containsKey("menuUrl"))
            {
                setMenuUrl(MapUtils.getString(map, "menuUrl", null));
            }

            if(map.containsKey("displayName"))
            {
                setDisplayName(MapUtils.getString(map, "displayName", null));
            }

            if(map.containsKey("visibility"))
            {
                setVisibility(convertToBoolean(map.getOrDefault("visibility", null)));
            }

            if(map.containsKey("accessDenied"))
            {
                setAccessDenied(convertToBoolean(map.getOrDefault("accessDenied", null)));
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

        if(null == propertyNames || propertyNames.contains("menuUid"))
        {
            map.put("menuUid", getMenuUid());
        }

        if(null == propertyNames || propertyNames.contains("typeDiscriminator"))
        {
            map.put("typeDiscriminator", getTypeDiscriminator());
        }

        if(null == propertyNames || propertyNames.contains("menuName"))
        {
            map.put("menuName", getMenuName());
        }

        if(null == propertyNames || propertyNames.contains("parentMenuUid"))
        {
            map.put("parentMenuUid", getParentMenuUid());
        }

        if(null == propertyNames || propertyNames.contains("childIndex"))
        {
            map.put("childIndex", getChildIndex());
        }

        if(null == propertyNames || propertyNames.contains("menuUrl"))
        {
            map.put("menuUrl", getMenuUrl());
        }

        if(null == propertyNames || propertyNames.contains("displayName"))
        {
            map.put("displayName", getDisplayName());
        }

        if(null == propertyNames || propertyNames.contains("visibility"))
        {
            map.put("visibility", getVisibility());
        }

        if(null == propertyNames || propertyNames.contains("accessDenied"))
        {
            map.put("accessDenied", getAccessDenied());
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

    private Long menuUid;

    private Integer typeDiscriminator;

    private String menuName;

    private Long parentMenuUid;

    private Integer childIndex;

    private String menuUrl;

    private String displayName;

    private Boolean visibility;

    private Boolean accessDenied;

    private LocalDateTime regDt;

    private Long regUserUid;

    private LocalDateTime uptDt;

    private Long uptUserUid;
}
