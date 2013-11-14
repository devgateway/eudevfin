package org.devgateway.eudevfin.financial;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKey;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.devgateway.eudevfin.financial.translate.AbstractTranslation;
import org.devgateway.eudevfin.financial.util.FinancialConstants;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

@MappedSuperclass
public abstract class AbstractTranslateable<T extends AbstractTranslation> {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long id=null;
	
	@CreatedBy
	private String createdBy;
	@CreatedDate
	private Date createdDate;

	@Transient
	private String currentLocale;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="parent", cascade=CascadeType.ALL)
//	@JoinColumn(name="PARENT_ID")
	@MapKey(name="locale")
	private Map<String, T> translations;
	
	
	protected void set(String property, Object value) {
		String locale		= (this.currentLocale!=null)?this.currentLocale:FinancialConstants.DEFAULT_LOCALE;
		if ( this.translations == null ) {
			this.translations	= new HashMap<>();
		}
		
		this.attemptSet(locale, property, value);
		
	}
	
	protected Object get(String property) {
		if ( this.translations != null && this.translations.size()>0 ) {
			Object result 	= this.attemptGet(this.currentLocale, property);
			
			if (result == null ) {
				result 		= this.attemptGet(FinancialConstants.DEFAULT_LOCALE, property);
			}
			
			return result;
		}
		return null;
		
	}
	private Object attemptGet (String locale, String property) {
		if ( locale != null ) {
			AbstractTranslation translation = this.translations.get(locale);
			Object result	= translation.get(property);
			return result;	
		}
		return null;
	}
	private void attemptSet (String locale, String property, Object value) {
		
		T translation = this.translations.get(locale);
		if (translation == null) {
			translation = this.newTranslationInstance(); 
			translation.set("parent",this);
			translation.set("locale", locale);
		}
		translation.set(property, value);
		this.translations.put(locale, translation);
	}
	
	protected abstract T newTranslationInstance();

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

	public String getCurrentLocale() {
		return currentLocale;
	}

	public void setCurrentLocale(String currentLocale) {
		this.currentLocale = currentLocale;
	}

	public Map<String, T> getTranslations() {
		return translations;
	}

	public void setTranslations(Map<String, T> translations) {
		this.translations = translations;
	}
	
	

	
	
}
