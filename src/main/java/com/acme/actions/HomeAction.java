package com.acme.actions;

import com.acme.views.HomeBody;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/home")
public class HomeAction extends BaseActionBean {

    @DefaultHandler
    public Resolution display() {
        return pageTemplate(new HomeBody());
    }

}
