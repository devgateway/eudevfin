package org.devgateway.eudevfin.exchange.common.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @author idobre
 * @since 9/5/14
 */

@Entity
@Audited
@javax.persistence.Table(name = ExchangeRateConfiguration.TABLE_NAME)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ExchangeRateConfiguration implements Serializable {
    public static final String TABLE_NAME = "EXCHANGERATECONFIGURATION";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String entityKey;

    private String entityValue;

    @CreatedBy
    private String createdBy;
    @CreatedDate
    private Date createdDate;
    @LastModifiedBy
    private String modifiedBy;
    @LastModifiedDate
    private Date modfiedDate;

    public ExchangeRateConfiguration () {

    }

    public ExchangeRateConfiguration(String entityKey, String entityValue) {
        this.entityKey = entityKey;
        this.entityValue = entityValue;
    }

    @Override
    public String toString() {
        return String.format(
                "ExchangeRateConfiguration[entityKey='%s', entitValue='%s']",
                entityKey, entityValue);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityKey() {
        return entityKey;
    }

    public void setEntityKey(String entityKey) {
        this.entityKey = entityKey;
    }

    public String getEntitValue() {
        return entityValue;
    }

    public void setEntitValue(String entitValue) {
        this.entityValue = entitValue;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModfiedDate() {
        return modfiedDate;
    }

    public void setModfiedDate(Date modfiedDate) {
        this.modfiedDate = modfiedDate;
    }
}
