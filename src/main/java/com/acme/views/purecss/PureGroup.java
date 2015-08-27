package com.acme.views.purecss;

import com.pojosontheweb.ttt.ITemplate;
import com.pojosontheweb.ttt.stripes.Form;

import java.util.function.Function;

public final class PureGroup implements Function<Form, ITemplate> {

	private final String name;
	private final PGroupTemplateProducer templateProducer;

	public PureGroup(String name, PGroupTemplateProducer templateProducer) {
		this.name = name;
		this.templateProducer = templateProducer;
	}

	public String getName() {
		return name;
	}

	@Override
	public ITemplate apply(Form form) {
		return templateProducer.make(form, name);
	}

	@FunctionalInterface
	public interface PGroupTemplateProducer {
		ITemplate make(Form form, String name);
	}
}
