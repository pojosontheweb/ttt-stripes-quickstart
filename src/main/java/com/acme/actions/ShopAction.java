package com.acme.actions;

import com.acme.model.Product;
import com.acme.model.ProductDao;
import com.acme.views.ShopBody;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;

import java.util.List;

@StrictBinding(
	deny = "product.*"
)
@UrlBinding("/shop")
public class ShopAction extends BaseActionBean implements ValidationErrorHandler{

	@Validate(required = true, on = "buy")
	private Product product;

	@DefaultHandler
	@DontValidate
	public Resolution display() {
		List<Product> products = ProductDao.getInstance().findProducts();
		return pageTemplate(new ShopBody(products));
	}

	public Resolution buy() {
		getContext().getMessages().add(
			new LocalizableMessage(getClass().getName() + ".productAdded", product.getName())
		);
		return new RedirectResolution(getClass()).flash(this);
	}

	@Override
	public Resolution handleValidationErrors(ValidationErrors errors) throws Exception {
		return display();
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
