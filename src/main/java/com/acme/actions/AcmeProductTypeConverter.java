package com.acme.actions;

import com.acme.model.Product;
import com.acme.model.ProductDao;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

/**
 * Type converter for our Product objects : loads them
 * from the DAO.
 */
public class AcmeProductTypeConverter implements TypeConverter<Product> {

	@Override
	public void setLocale(Locale locale) {
		// unused
	}

	@Override
	public Product convert(String input, Class<? extends Product> targetType, Collection<ValidationError> errors) {
		Long l;
		try {
			l = Long.parseLong(input);
		} catch(NumberFormatException e) {
			return null;
		}
		Optional<Product> p = ProductDao.getInstance().getProduct(l);
		if (p.isPresent()) {
			return p.get();
		}
		return null;
	}

}
