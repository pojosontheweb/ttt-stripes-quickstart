package com.acme.actions;

import com.acme.views.FeedbackBody;
import com.acme.views.FeedbackDoneBody;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.*;

@UrlBinding("/feedback")
public class FeedbackAction extends BaseActionBean implements ValidationErrorHandler {

	@Validate(required = true, converter = EmailTypeConverter.class)
	private String email;

	@Validate(required = true, minlength = 10, maxlength = 100)
	private String subject;

	@Validate(maxlength = 1000)
	private String message;

	@Validate(required = true)
	private FeedbackType type;

	private boolean agreed = false;

	@DefaultHandler
	@DontValidate
	public Resolution display() {
		return pageTemplate(new FeedbackBody());
	}

	public Resolution send() {
		// feedback just goes to /dev/null ! we display
		// a message like we're gonna handle it and the usual bs
		getContext().getMessages().add(
			new LocalizableMessage(getClass().getName() + ".feedbackSent", email));

		// redirect to the "sent" event
		return new RedirectResolution(getClass(), "sent");
	}

	@DontValidate
	public Resolution sent() {
		return pageTemplate(new FeedbackDoneBody());
	}

	@DontBind
	public Resolution reset() {
		return new RedirectResolution(getClass());
	}

	@ValidationMethod
	public void validateAgreed(ValidationErrors errors) {
		if (!agreed) {
			errors.add("agreed", new LocalizableError(getClass().getName() + ".youMustAgree"));
		}
	}

	@Override
	public Resolution handleValidationErrors(ValidationErrors errors) throws Exception {
		return display();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public FeedbackType getType() {
		return type;
	}

	public void setType(FeedbackType type) {
		this.type = type;
	}

	public boolean isAgreed() {
		return agreed;
	}

	public void setAgreed(boolean agreed) {
		this.agreed = agreed;
	}
}
