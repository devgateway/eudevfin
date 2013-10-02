package org.devgateway.eudevfin.persistence.test;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.devgateway.eudevfin.persistence.constants.Constants;
import org.devgateway.eudevfin.persistence.dao.Item;
import org.devgateway.eudevfin.persistence.repositories.ItemCrudRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;


public class BasicFactorySetup {

	private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory(Constants.PERSISTENCE_UNIT);

	private ItemCrudRepository itemRepository;
	private EntityManager em;

	private Item item;


	@Before
	public void setUp() {

		em = factory.createEntityManager();

		itemRepository = new JpaRepositoryFactory(em).getRepository(ItemCrudRepository.class);

		em.getTransaction().begin();

		item = new Item();	
		item.setPrice(32112);
		item.setProduct("mumu");
		item.setQuantity(2343);

		item = itemRepository.save(item);

	}

	/**
	 * Rollback transaction.
	 */
	@After
	public void tearDown() {

		em.getTransaction().rollback();
	}

	/**
	 * Showing invocation of finder method.
	 */
	@Test
	public void executingFinders() {

		assertEquals(item, itemRepository.findByProduct("mumu").get(0));
		assertEquals(item, itemRepository.findByPrice(32112).get(0));
	}
}
