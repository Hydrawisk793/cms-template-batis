package kaphein.template.cmstemplatebatis.web;

import kaphein.template.cmstemplatebatis.PathUtils;

public class ApiProperties
{
    public ApiProperties()
    {
        setPrefix("/api");
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = PathUtils.join("/", prefix);
    }

    private String prefix;
}
