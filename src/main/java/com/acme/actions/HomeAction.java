package com.acme.actions;

import com.acme.views.HomeBody;
import net.sourceforge.stripes.action.*;

/**
 * Home action, forwards to the home body template.
 */
@UrlBinding("/home")
public class HomeAction extends BaseActionBean {

    @DefaultHandler
	@DontBind
    public Resolution display() {
        return pageTemplate(new HomeBody());
    }

}
