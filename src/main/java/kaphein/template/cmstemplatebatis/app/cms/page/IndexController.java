package kaphein.template.cmstemplatebatis.app.cms.page;

import kaphein.template.cmstemplatebatis.web.PageController;
import org.springframework.web.bind.annotation.GetMapping;

@PageController
public class IndexController
{
    @GetMapping("/cms")
    public String index()
    {
        return "cms/index";
    }
}
