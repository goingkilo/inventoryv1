package com.kilo.microkit.db.dao;

import com.kilo.microkit.db.model.Category;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryDAO extends AbstractDAO<Category> {

	public CategoryDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public List<Category> getCategories() {
		Criteria cr = currentSession().createCriteria(Category.class);
		return cr.list();
	}

	public void saveMany(List<Category> categoryList) {
		for( Category category : categoryList ) {
			try {
				persist(category);
			}catch (RuntimeException e) {
				e.printStackTrace();;
			}
		}
	}

	public void saveMany(Map<String,String> cats) {
		List<Category> l = new ArrayList<Category>();
		for( String k : cats.keySet()) {
			l.add( new Category( k, cats.get(k)));
		}
		saveMany(l);
	}
}
