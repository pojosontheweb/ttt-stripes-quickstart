package com.acme.model;

import java.math.BigDecimal;
import java.util.*;

public class ProductDao {

    private final List<Product> products = new ArrayList<>();

	private static final ProductDao INSTANCE = new ProductDao();

	public static ProductDao getInstance() {
		return INSTANCE;
	}

	ProductDao() {
        // create a fake list of products...
        products.add(
            new Product(
                1L,
                "Blue Suede Shoes",
				"The infamous Elvis shoes. Very good quality, made in Belgium.", new BigDecimal(99.99),
				Arrays.asList(Category.Wear, Category.Beach),
				"http://media2.popsugar-assets.com/files/2013/07/01/794/n/3019466/c15fd291f7b4c274_groom-style-groom-accessories-suede-shoes.xxxlarge/i/Blue-Suede-Shoes.jpg"
			)
        );
        products.add(
			new Product(
				2L,
				"Spaceship",
				"The one from Star Trek, still flies !",
				new BigDecimal(1200),
				Arrays.asList(Category.Travel, Category.Awesome),
				"http://www.entertainmentearth.com/images/AUTOIMAGES/DC17805lg.jpg"
			)
		);
        products.add(
			new Product(
				3L,
				"Eternal Lighter",
				"Using solar energy, you can smoke forever. Sold without the cigarettes.",
				new BigDecimal(9.95),
				Collections.singletonList(Category.Chill),
				"http://www.castorama.fr/images/products/h/h_514178.jpg"
			)
		);
		products.add(
			new Product(
				4L,
				"Kick-ass Java Web MVC",
				"Stripes and TTT provides an amazing Developer Experience. <em>Static typing</em> really helps a lot, your code has never been so bug-free. A must-have for Java developers.",
				null,
				Collections.singletonList(Category.OpenSource),
				"https://avatars2.githubusercontent.com/u/4349407?v=3&s=48"
			)
		);
		products.add(
			new Product(
				5L,
				"Selmer B700 Trumpet",
				"50 years old and still funky ! Rare model, hunted by the best trumpet players out there. No matter your chops : this trumpet will make you sound just like Miles.",
				new BigDecimal(100000),
				Collections.singletonList(Category.Music),
				"http://accessoires-chiens-chats.pets-dating.com/1410-1929-large/klaxons-avertissement-trompette.jpg"
			)
		);
	}

    public List<Product> findProducts() {
        return Collections.unmodifiableList(products);
    }


	public Optional<Product> getProduct(Long l) {
		return products.stream().filter(p -> p!=null && p.getId().equals(l)).findFirst();
	}
}
