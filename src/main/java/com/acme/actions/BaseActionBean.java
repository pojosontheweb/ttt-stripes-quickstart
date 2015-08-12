package com.acme.actions;

import com.acme.views.PageTemplate;
import com.pojosontheweb.ttt.ITemplate;
import com.pojosontheweb.ttt.stripes.TttResolution;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Base action class. Handles action bean context and
 * global page templating.
 */
public abstract class BaseActionBean implements ActionBean {

    private ActionBeanContext context;

    @Override
    public ActionBeanContext getContext() {
        return context;
    }

    @Override
    public void setContext(ActionBeanContext context) {
        this.context = context;
    }

	/**
	 * Helper method that creates a Resolution to a full blown page for passed body template.
	 * Wraps the body into a PageTemplate that handles the page structure
	 * and manages navigation links. This is equivalent to the stripes:layout feature.
	 *
	 * @param body the body template to be rendered into a page
	 *
	 * @return a resolution to the template
	 */
    protected TttResolution pageTemplate(ITemplate body) {
        return new TttResolution(
            new PageTemplate(this, NAV_ACTIONS, body)
        );
    }

	// list of action beans to be used for navigation
    public static final List<Class<? extends ActionBean>> NAV_ACTIONS =
        Collections.unmodifiableList(
			Arrays.asList(
				HomeAction.class,
				ShopAction.class,
				FeedbackAction.class
			)
		);

}
