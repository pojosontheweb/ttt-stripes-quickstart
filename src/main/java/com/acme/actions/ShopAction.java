package com.acme.actions;

import com.acme.model.Product;
import com.acme.model.ProductDao;
import com.acme.views.ShopBody;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;

import java.util.List;

/**
 * "shop" action : displays a list of products, and allows to buy them.
 */
@StrictBinding(
	deny = "product.*"
)
@UrlBinding("/shop")
public class ShopAction extends BaseActionBean implements ValidationErrorHandler{

	/**
	 * The Product to buy, bound by our custom Product Type Converter
	 */
	@Validate(required = true, on = "buy")
	private Product product;

	/**
	 * Display the list of products
	 */
	@DefaultHandler
	@DontBind
	public Resolution display() {
		List<Product> products = ProductDao.getInstance().findProducts();
		return pageTemplate(new ShopBody(products));
	}

	/**
	 * Buy a Product (fake : only adds a message)
	 */
	public Resolution buy() {
		getContext().getMessages().add(
			new LocalizableMessage(getClass().getName() + ".productAdded", product.getName())
		);
		return new RedirectResolution(getClass()).flash(this);
	}

	/**
	 * Required for TTT because we have no source page or anything like it
	 * so we must tell stripes what to do if there's a validation error.
	 * @param errors
	 * @return
	 * @throws Exception
	 */
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
