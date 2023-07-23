package kaphein.template.cmstemplatebatis.model;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Supplier;

import kaphein.template.cmstemplatebatis.persistence.AbstractEntity;
import org.apache.commons.collections4.MapUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AppUser
    extends AbstractEntity
{
    @Override
    public Object getPrimaryKey()
    {
        return getUserUid();
    }

    @Override
    public AppUser assignMap(Map<String, Object> map)
    {
        if(null != map)
        {
            if(map.containsKey("userUid"))
            {
                setUserUid(MapUtils.getLong(map, "userUid", null));
            }

            if(map.containsKey("userType"))
            {
                setUserType(MapUtils.getInteger(map, "userType", null));
            }

            if(map.containsKey("loginName"))
            {
                setLoginName(MapUtils.getString(map, "loginName", null));
            }

            if(map.containsKey("loginPassword"))
            {
                setLoginPassword(MapUtils.getString(map, "loginPassword", null));
            }

            if(map.containsKey("realName"))
            {
                setRealName(MapUtils.getString(map, "realName", null));
            }

            if(map.containsKey("emailAddress"))
            {
                setEmailAddress(MapUtils.getString(map, "emailAddress", null));
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

            if(map.containsKey("deletedAt"))
            {
                setDeletedAt(convertToLocalDateTime(map.getOrDefault("deletedAt", null)));
            }

            if(map.containsKey("deletedBy"))
            {
                setDeletedBy(MapUtils.getLong(map, "deletedBy", null));
            }
        }

        return this;
    }

    @Override
    public Map<String, Object> toMap(Collection<String> propertyNames, Supplier<Map<String, Object>> mapSupplier)
    {
        final Map<String, Object> map = mapSupplier.get();

        if(null == propertyNames || propertyNames.contains("userUid"))
        {
            map.put("userUid", getUserUid());
        }

        if(null == propertyNames || propertyNames.contains("userType"))
        {
            map.put("userType", getUserType());
        }

        if(null == propertyNames || propertyNames.contains("loginName"))
        {
            map.put("loginName", getLoginName());
        }

        if(null == propertyNames || propertyNames.contains("loginPassword"))
        {
            map.put("loginPassword", getLoginPassword());
        }

        if(null == propertyNames || propertyNames.contains("realName"))
        {
            map.put("realName", getRealName());
        }

        if(null == propertyNames || propertyNames.contains("emailAddress"))
        {
            map.put("emailAddress", getEmailAddress());
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

        if(null == propertyNames || propertyNames.contains("deletedAt"))
        {
            map.put("deletedAt", getDeletedAt());
        }

        if(null == propertyNames || propertyNames.contains("deletedBy"))
        {
            map.put("deletedBy", getDeletedBy());
        }

        return map;
    }

    private Long userUid;

    private Integer userType;

    private String loginName;

    private String loginPassword;

    private String realName;

    private String emailAddress;

    private LocalDateTime createdAt;

    private Long createdBy;

    private LocalDateTime updatedAt;

    private Long updatedBy;

    private LocalDateTime deletedAt;

    private Long deletedBy;
}
