/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.common.hibernate;

import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

public class HibernateStatisticsFactoryBean implements FactoryBean<Statistics> {

  @Autowired
  private EntityManagerFactory entityManagerFactory;

  @Override
  public Statistics getObject() throws Exception {
    SessionFactory sessionFactory = ((HibernateEntityManagerFactory) entityManagerFactory).getSessionFactory();
    return sessionFactory.getStatistics();
  }

  @Override
  public Class<?> getObjectType() {
    return Statistics.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }
}