package com.acme.views.purecss;

import com.pojosontheweb.ttt.stripes.Form;
import com.pojosontheweb.ttt.stripes.Submit;
import net.sourceforge.stripes.action.ActionBean;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PureFormBuilder {

	private final Class<? extends ActionBean> beanClass;
	private final List<PureGroup> groupList = new ArrayList<>();
	private final List<Function> controls = new ArrayList<>();

	private String legend;

	public PureFormBuilder(Class<? extends ActionBean> beanClass) {
		this.beanClass = beanClass;
	}

	public PureFormBuilder setLegend(String legend) {
		this.legend = legend;
		return this;
	}

	public PureFormBuilder group(String name, PureGroup.PGroupTemplateProducer templateProducer) {
		groupList.add(new PureGroup(name, templateProducer));
		return this;
	}

	public PureFormBuilder control(Function<Form,Submit> submitMaker) {
		controls.add(submitMaker);
		return this;
	}

	public PureForm build() {
		return new PureForm(beanClass, legend, groupList, controls);
	}
}
