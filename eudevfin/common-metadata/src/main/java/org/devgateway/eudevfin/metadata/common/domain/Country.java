/**
 * 
 */
package org.devgateway.eudevfin.metadata.common.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

/**
 * @author Alex
 *
 */
@Entity
@Audited
@DiscriminatorValue("Country")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Country extends Area {

	private static final long serialVersionUID = 4597228380360940999L;



}
