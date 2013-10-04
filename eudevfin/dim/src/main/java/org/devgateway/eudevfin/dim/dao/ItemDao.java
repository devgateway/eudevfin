package org.devgateway.eudevfin.dim.dao;

import java.util.List;

import org.devgateway.eudevfin.domain.Item;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class ItemDao {

	@Autowired
	private SessionFactory sessionFactoryBean;

	@Transactional
	public void createItems() {
		Session session = sessionFactoryBean.getCurrentSession();
		Item i1 = new Item();
		i1.setPrice(1000);
		i1.setProduct("mumu");
		i1.setQuantity(10);
		session.save(i1);
		session.flush();
	}

	@Transactional
	public List<Item> findItems() {
		Session session = sessionFactoryBean.getCurrentSession();
		Criteria criteria = session.createCriteria(Item.class);
		return criteria.list();
	}
}
