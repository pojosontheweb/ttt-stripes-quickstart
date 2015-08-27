package com.acme.views.purecss;

import net.sourceforge.stripes.action.ActionBean;

public class PureCss {

	public static PureFormBuilder form(Class<? extends ActionBean> beanClass) {
		return new PureFormBuilder(beanClass);
	}
}
