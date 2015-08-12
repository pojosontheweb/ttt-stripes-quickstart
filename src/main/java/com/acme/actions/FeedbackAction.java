package com.acme.actions;

import com.acme.views.FeedbackBody;
import com.acme.views.FeedbackDoneBody;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.*;

/**
 * This action shows how to handle FORMs with the TTT Stripes tags.
 * It's a regular Stripes FORM.
 */
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

	/**
	 * Display the feedback form.
	 */
	@DefaultHandler
	@DontBind
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

	/**
	 * Send the feedback (fake, does nothing except output
	 * a message...)
	 */
	@DontBind
	public Resolution sent() {
		return pageTemplate(new FeedbackDoneBody());
	}

	/**
	 * Reset the form
	 * @return
	 */
	@DontBind
	public Resolution reset() {
		return new RedirectResolution(getClass());
	}

	/**
	 * Custom validation method for the "I agree" checkbox
	 * @param errors
	 */
	@ValidationMethod
	public void validateAgreed(ValidationErrors errors) {
		if (!agreed) {
			errors.add("agreed", new LocalizableError(getClass().getName() + ".youMustAgree"));
		}
	}

	/**
	 * Needed because with TTT there ain't no source page...
	 * Simply forwards to the default handler : this one must NOT
	 * validate otherwise you'd go into an infinite loop...
	 */
	@Override
	public Resolution handleValidationErrors(ValidationErrors errors) throws Exception {
		return display();
	}

	//
	// accessors/mutators
	//

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
