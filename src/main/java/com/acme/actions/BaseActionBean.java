package com.acme.actions;

import com.acme.views.AcmePageBody;
import com.acme.views.PageTemplate;
import com.pojosontheweb.ttt.stripes.TttResolution;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.LocalizableMessage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class BaseActionBean implements ActionBean {

    private ActionBeanContext context;

    @Override
    public ActionBeanContext getContext() {
        return context;
    }

    @Override
    public void setContext(ActionBeanContext context) {
        this.context = (ActionBeanContext)context;
    }

    protected String getMessage(String key, Object... params) {
        return new LocalizableMessage(key, params).getMessage(context.getLocale());
    }

    protected TttResolution pageTemplate(AcmePageBody body) {
        return new TttResolution(
            new PageTemplate(getMessage(getClass().getName() + ".pageTitle"), this, getNavBeanClasses(), body)
        );
    }

    private static final List<Class<? extends ActionBean>> NAV_ACTIONS =
        Collections.unmodifiableList(Arrays.asList(HomeAction.class, ShopAction.class, FeedbackAction.class));

    protected List<Class<? extends ActionBean>> getNavBeanClasses() {
        return NAV_ACTIONS;
    }
}
