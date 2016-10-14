package com.kilo.microkit.db.dao;

import com.kilo.microkit.db.model.MetaInfo;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;

import java.util.List;

public class MetaInfoDAO extends AbstractDAO<MetaInfo> {

	public MetaInfoDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public String getLoadTime() {
		Criteria cr = currentSession().createCriteria(MetaInfo.class);
		List<MetaInfo> l = cr.list();
		return ( l == null || l.size() == 0) ? null : l.get(0).getLoadTime();
	}

	public void setLoadTime( String s) {
		MetaInfo m = new MetaInfo();
		m.setType("loadtime");
		m.setLoadTime(s);
		persist(m);

	}
}
