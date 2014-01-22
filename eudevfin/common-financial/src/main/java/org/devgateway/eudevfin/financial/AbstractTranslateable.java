/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.financial;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.common.spring.ContextHelper;
import org.devgateway.eudevfin.financial.translate.AbstractTranslation;
import org.devgateway.eudevfin.financial.util.FinancialConstants;
import org.devgateway.eudevfin.financial.util.LocaleHelperInterface;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;


@MappedSuperclass
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public abstract class AbstractTranslateable<T extends AbstractTranslation<? extends AbstractTranslateable<T>>>
        implements Serializable {

    private static Logger logger = Logger.getLogger(AbstractTranslateable.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id = null;

    @CreatedBy
    private String createdBy;
    @CreatedDate
    private Date createdDate;
    @LastModifiedBy
    private String modifiedBy;
    @LastModifiedDate
    private Date modfiedDate;

    @Transient
    private String locale;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent", cascade = CascadeType.ALL)
    @MapKey(name = "locale")
	protected Map<String, T> translations;


    protected void set(String property, Object value) {
        String locale = this.decideLocaleToUse();
        if (this.translations == null) {
            this.translations = new HashMap<>();
        }

        this.attemptSet(locale, property, value);

    }

    protected Object get(String property) {
        if (this.translations != null && this.translations.size() > 0) {
            String locale = this.decideLocaleToUse();
            Object result = this.attemptGet(locale, property);

            if (result == null) {
                result = this.attemptGet(FinancialConstants.DEFAULT_LOCALE, property);
            }

            return result;
        }
        return null;

    }

    private Object attemptGet(String locale, String property) {
        if (locale != null) {
            AbstractTranslation translation = this.translations.get(locale);
            if (translation != null) {
                Object result = translation.get(property);
                return result;
            }
        }
        return null;
    }

    private void attemptSet(String locale, String property, Object value) {

        T translation = this.translations.get(locale);
        if (translation == null) {
            translation = this.newTranslationInstance();
            translation.set("parent", this);
            translation.set("locale", locale);
        }
        translation.set(property, value);
        this.translations.put(locale, translation);
    }

    private String decideLocaleToUse() {
        logger.debug("Deciding the locale to use");
        if (this.locale != null)
            return this.locale;
        else {
            try {
				LocaleHelperInterface localeHelper = ContextHelper.newInstance().getBean("localeHelperRequest");
                if (localeHelper != null && localeHelper.getLocale() != null) {
                    return localeHelper.getLocale();
                }
            } catch (RuntimeException ex) {
                logger.warn("Problem getting locale helper from request scope: " + ex.getMessage());
            }
        }
        return FinancialConstants.DEFAULT_LOCALE;
    }

    protected abstract T newTranslationInstance();

    /**
     * @return the locale
     */
    public String getLocale() {
        return locale;
    }

    /**
     * @param locale the locale to set
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Map<String, T> getTranslations() {
        return translations;
    }

    public void setTranslations(Map<String, T> translations) {
        this.translations = translations;
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
