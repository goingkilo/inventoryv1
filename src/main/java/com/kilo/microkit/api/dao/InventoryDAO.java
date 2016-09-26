package com.kilo.microkit.api.dao;

import com.kilo.microkit.api.model.InventoryItem;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class InventoryDAO extends AbstractDAO<InventoryItem> {

	public InventoryDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public List<InventoryItem> getInventoryItems() {
		Criteria cr = currentSession().createCriteria(InventoryItem.class);
		return cr.list();
	}

	public List<InventoryItem> getInventoryItemsByLink(String link) {
		Criteria cr = currentSession().createCriteria(InventoryItem.class);
		cr.add(Restrictions.like("link", "%" + link + "%"));
		return cr.list();
	}

	public void saveMany(List<InventoryItem> inventoryItems) {
		for( InventoryItem inventoryItem : inventoryItems ) {
			try {
				persist(inventoryItem);
			}catch (RuntimeException e) {
				e.printStackTrace();;
			}
		}
	}

	public void saveOne(InventoryItem inventoryItem) {
		persist(inventoryItem);
	}
}
