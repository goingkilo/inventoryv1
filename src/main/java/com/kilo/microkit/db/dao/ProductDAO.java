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

	public List<Product> getProducts(String category) {
		Criteria cr = currentSession().createCriteria(Product.class);
		cr.add(Restrictions.eq("category", category));
		cr.add(Restrictions.eq("inStock", "true"));
		cr.add(Restrictions.eq("available", "true"));
		return cr.list();
	}

	public List<Product> getByBrand(String brand) {
		Criteria cr = currentSession().createCriteria(Product.class);
		cr.add(Restrictions.like("brand", "%" + brand + "%"));
		return cr.list();
	}

	public List<Product> search(String searchTerm) {
		Criteria cr = currentSession().createCriteria(Product.class);
		cr.add( Restrictions.or(
				Restrictions.like("title", "%" + searchTerm + "%"),
				Restrictions.like("desc", "%" + searchTerm + "%")
		));
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
