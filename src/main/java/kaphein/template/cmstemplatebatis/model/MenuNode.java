package kaphein.template.cmstemplatebatis.model;

import kaphein.template.cmstemplatebatis.persistence.EntityFieldValueInvalidException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.MapUtils;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

@Setter
@Getter
@NoArgsConstructor
public class MenuNode extends Menu
{
    @Override
    public void setTypeDiscriminator(Integer typeDiscriminator)
    {
        if(null != typeDiscriminator && 2 != typeDiscriminator)
        {
            throw new EntityFieldValueInvalidException(getClass().getSimpleName(), "typeDiscriminator", typeDiscriminator);
        }

        setTypeDiscriminatorUnsafe(typeDiscriminator);
    }

    @Override
    public MenuNode assignMap(Map<String, Object> map)
    {
        super.assignMap(map);

        if(null != map)
        {
            if(map.containsKey("childIndexPath"))
            {
                setChildIndexPath(MapUtils.getString(map, "childIndexPath", null));
            }

            if(map.containsKey("menuNamePath"))
            {
                setMenuNamePath(MapUtils.getString(map, "menuNamePath", null));
            }

            if(map.containsKey("menuDepth"))
            {
                setMenuDepth(MapUtils.getLong(map, "menuDepth", null));
            }
        }

        return this;
    }

    @Override
    public Map<String, Object> toMap(Collection<String> propertyNames, Supplier<Map<String, Object>> mapSupplier)
    {
        final Map<String, Object> map = super.toMap(propertyNames, mapSupplier);

        if(null == propertyNames || propertyNames.contains("childIndexPath"))
        {
            map.put("childIndexPath", getChildIndexPath());
        }

        if(null == propertyNames || propertyNames.contains("menuNamePath"))
        {
            map.put("menuNamePath", getMenuNamePath());
        }

        if(null == propertyNames || propertyNames.contains("menuDepth"))
        {
            map.put("menuDepth", getMenuDepth());
        }

        return map;
    }

    private String childIndexPath;

    private String menuNamePath;

    private Long menuDepth;
}
