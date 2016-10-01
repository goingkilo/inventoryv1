package com.kilo.microkit.db.dao;

import com.kilo.microkit.db.model.Product;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class ProductDAO extends AbstractDAO<Product> {

	public ProductDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public List<Product> getInventoryItems(String category) {
		Criteria cr = currentSession().createCriteria(Product.class);
		cr.add(Restrictions.eq("category", category));
		cr.add(Restrictions.eq("inStock", "true"));
		return cr.list();
	}


	public List<Product> getInventoryItemsByLink(String link) {
		Criteria cr = currentSession().createCriteria(Product.class);
		cr.add(Restrictions.like("link", "%" + link + "%"));
		return cr.list();
	}

	public void saveMany(List<Product> products, String category) {
		for( Product product : products) {
			try {
				if( category != null ) {
					if (product.getCategory() == null || product.getCategory().equalsIgnoreCase("")) {

						product.setCategory(category);
					}
				}
				persist(product);
			}catch (RuntimeException e) {
				e.printStackTrace();;
			}
		}
	}

	public void saveOne(Product product) {
		persist(product);
	}
}
