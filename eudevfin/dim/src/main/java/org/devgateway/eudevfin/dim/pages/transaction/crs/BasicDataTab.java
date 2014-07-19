/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.dim.pages.transaction.crs;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.ChannelCategory;
import org.devgateway.eudevfin.metadata.common.util.CategoryConstants;
import org.devgateway.eudevfin.ui.common.Constants;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.components.PreviewableFormPanel;
import org.devgateway.eudevfin.ui.common.components.TextAreaInputField;
import org.devgateway.eudevfin.ui.common.events.Field12ChangedEventPayload;
import org.devgateway.eudevfin.ui.common.events.Field13ChangedEventPayload;
import org.devgateway.eudevfin.ui.common.permissions.PermissionAwareComponent;
import org.devgateway.eudevfin.ui.common.providers.AreaChoiceProvider;
import org.devgateway.eudevfin.ui.common.providers.CategoryProviderFactory;
import org.devgateway.eudevfin.ui.common.providers.OrganizationChoiceProvider;
import org.devgateway.eudevfin.ui.common.validators.BilateralField10CodeValidator;
import org.devgateway.eudevfin.ui.common.validators.BilateralField10TypeOfAidCodeValidator;
import org.devgateway.eudevfin.ui.common.validators.Field11CodeValidator;
import org.devgateway.eudevfin.ui.common.validators.Field12CodeValidator;
import org.devgateway.eudevfin.ui.common.validators.Field8CodeValidator;
import org.devgateway.eudevfin.ui.common.validators.MultilateralField10CodeValidator;
import org.devgateway.eudevfin.ui.common.validators.MultilateralField10TypeOfAidCodeValidator;
import org.devgateway.eudevfin.ui.common.validators.TypeOfAidField13ChannelCodeValidator;
import org.devgateway.eudevfin.ui.common.validators.TypeOfFinanceField12TypeOfAidCodeValidator;

import com.vaynberg.wicket.select2.ChoiceProvider;

/**
 * @author aartimon@developmentgateway.org
 * @since 01 November 2013
 */
public class BasicDataTab extends PreviewableFormPanel implements PermissionAwareComponent {
	private static final long serialVersionUID = 8923172292469016906L;
	public static final String KEY = "tabs.basic";
	public static final String VALIDATIONKEY_MULTILATERAL_CHANNEL_CODE = "validation.multilateralChannelCode";
	public static final String VALIDATIONKEY_BILATERAL_FIELD_10_CODE = "validation.bilateralField10Code";
	public static final String VALIDATIONKEY_MULTILATERAL_FIELD_10_CODE = "validation.multilateralField10Code";
	public static final String VALIDATIONKEY_FIELD_11_CODE = "validation.field11Code";
	public static final String VALIDATIONKEY_FIELD_12_CODE = "validation.field12Code";
	public static final String VALIDATIONKEY_FIELD_12_TYPE_OF_AID_CODE = "validation.field12TypeOfAidCode";
	public static final String VALIDATIONKEY_B01_1XXXX_CODE = "validation.decorateB01For1xxxx";
	public static final String VALIDATIONKEY_B01_2XXXX_CODE = "validation.decorateB01For2xxxx";
	public static final String VALIDATIONKEY_B01_BIMULTI3_CODE = "validation.decorateB01ForBiMulti3";
	public static final String VALIDATIONKEY_B02_BIMULTI2_CODE = "validation.decorateB02ForBiMulti2";

	protected PageParameters parameters;

	@SpringBean
	private OrganizationChoiceProvider organizationProvider;
	// @SpringBean
	// private ChannelCategoryChoiceProvider channelProvider;
	@SpringBean
	private CategoryProviderFactory categoryFactory;
	@SpringBean
	private AreaChoiceProvider areaProvider;
	
	DropDownField<Category> typeOfAid;
	DropDownField<Category> bilateralMultilateral;
	DropDownField<Category> typeOfFinance;

	public BasicDataTab(String id, PageParameters parameters) {
		super(id);
		this.parameters = parameters;
		addComponents();
		addExtensionPanel1();
		addExtensionPanel2();
		addExtensionPanel3();
	}

	protected void addExtensionPanel3() {
		add(new WebMarkupContainer("extensionPanel3").setVisibilityAllowed(false));
	}

	/**
	 * Placeholder in order to extend the tab after the field with no 7
	 */
	protected void addExtensionPanel1() {
		add(new WebMarkupContainer("extensionPanel1").setVisibilityAllowed(false));
	}

	/**
	 * Placeholder in order to extend the tab after last field
	 */
	protected void addExtensionPanel2() {
		add(new WebMarkupContainer("extensionPanel2").setVisibilityAllowed(false));
	}

	private void addComponents() {
		DropDownField<Area> recipient = new DropDownField<>("7recipient", new RWComponentPropertyModel<Area>(
				"recipient"), areaProvider);
		add(recipient);

		DropDownField<ChannelCategory> channelOfDelivery = new DropDownField<ChannelCategory>("8channelDelivery",
				new RWComponentPropertyModel<ChannelCategory>("channel"),
				(ChoiceProvider) categoryFactory.get(CategoryConstants.CHANNEL_TAG)) {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {				
				super.onUpdate(target);
				typeOfAid.getField().clearInput();
				target.add(typeOfAid);
			}
		};

		String transactionType = parameters.get(Constants.PARAM_TRANSACTION_TYPE).toString("");

		channelOfDelivery.getField().add(new Field8CodeValidator(transactionType) {
			private static final long serialVersionUID = 691836793052873358L;

			@Override
			protected ValidationError decorate(ValidationError error, IValidatable<Category> validatable) {
				error.addKey(VALIDATIONKEY_MULTILATERAL_CHANNEL_CODE);
				return super.decorate(error, validatable);
			}
		});
		add(channelOfDelivery);

		typeOfAid = new DropDownField<Category>("13typeOfAid",
				new RWComponentPropertyModel<Category>("typeOfAid"),
				categoryFactory.get(CategoryConstants.ALL_TYPE_OF_AID_TAG)) {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				bilateralMultilateral.getField().clearInput();
				typeOfFinance.getField().clearInput();
				target.add(bilateralMultilateral);
				target.add(typeOfFinance);
				Category modelObject = this.getField().getModelObject();				
				if (modelObject != null)
					send(getPage(), Broadcast.DEPTH,
							new Field13ChangedEventPayload(target, modelObject.getDisplayableCode()));
			}
		};

		typeOfAid.getField().add(new TypeOfAidField13ChannelCodeValidator(transactionType, channelOfDelivery) {

			@Override
			protected IValidationError decorateB01For1xxxx(ValidationError error, IValidatable<Category> validatable) {
				error.addKey(VALIDATIONKEY_B01_1XXXX_CODE);
				return super.decorateB01For1xxxx(error, validatable);
			}

			@Override
			protected IValidationError decorateB01For2xxxx(ValidationError error, IValidatable<Category> validatable) {
				error.addKey(VALIDATIONKEY_B01_2XXXX_CODE);
				return super.decorateB01For2xxxx(error, validatable);
			}

		});

		add(typeOfAid);

		bilateralMultilateral = new DropDownField<Category>("10bilateralMultilateral",
				new RWComponentPropertyModel<Category>("biMultilateral"),
				categoryFactory.get(CategoryConstants.BI_MULTILATERAL_TAG)) {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				typeOfAid.getField().clearInput();
				target.add(typeOfAid);
			}
		};		

		bilateralMultilateral.getField().add(new BilateralField10CodeValidator(transactionType) {

			private static final long serialVersionUID = 4247327751054728566L;

			@Override
			protected ValidationError decorate(ValidationError error, IValidatable<Category> validatable) {
				error.addKey(VALIDATIONKEY_BILATERAL_FIELD_10_CODE);
				return super.decorate(error, validatable);
			}

		});

		bilateralMultilateral.getField().add(new BilateralField10TypeOfAidCodeValidator(transactionType, typeOfAid) {
			private static final long serialVersionUID = 4247327751054728566L;

			@Override
			protected ValidationError decorate(ValidationError error, IValidatable<Category> validatable) {
				error.addKey(VALIDATIONKEY_B01_BIMULTI3_CODE);
				return super.decorate(error, validatable);
			}
		});

		bilateralMultilateral.getField().add(new MultilateralField10CodeValidator(transactionType) {

			private static final long serialVersionUID = -2388019323524154047L;

			@Override
			protected ValidationError decorate(ValidationError error, IValidatable<Category> validatable) {
				error.addKey(VALIDATIONKEY_MULTILATERAL_FIELD_10_CODE);
				return super.decorate(error, validatable);
			}

		});

		bilateralMultilateral.getField().add(new MultilateralField10TypeOfAidCodeValidator(transactionType, typeOfAid) {

			private static final long serialVersionUID = -2388019323524154047L;

			@Override
			protected ValidationError decorate(ValidationError error, IValidatable<Category> validatable) {
				error.addKey(VALIDATIONKEY_B02_BIMULTI2_CODE);
				return super.decorate(error, validatable);
			}
		});

		add(bilateralMultilateral);

		DropDownField<Category> typeOfFlow = new DropDownField<>("11typeOfFlow",
				new RWComponentPropertyModel<Category>("typeOfFlow"),
				categoryFactory.get(CategoryConstants.TYPE_OF_FLOW_TAG));
		typeOfFlow.required();
		typeOfFlow.getField().add(new Field11CodeValidator(transactionType) {
			@Override
			protected ValidationError decorate(ValidationError error, IValidatable<Category> validatable) {
				error.addKey(VALIDATIONKEY_FIELD_11_CODE);
				return super.decorate(error, validatable);
			}
		});
		add(typeOfFlow);

		typeOfFinance= new DropDownField<Category>("12typeOfFinance",
				new RWComponentPropertyModel<Category>("typeOfFinance"),
				categoryFactory.get(CategoryConstants.ALL_TYPE_OF_FINANCE_TAG)) {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				typeOfAid.getField().clearInput();
				target.add(typeOfAid);
				Category modelObject = this.getField().getModelObject();				
				if (modelObject != null)
					send(getPage(), Broadcast.DEPTH,
							new Field12ChangedEventPayload(target, modelObject.getDisplayableCode()));
			}
		};

		typeOfFinance.required();
		typeOfFinance.getField().add(new Field12CodeValidator(transactionType) {
			@Override
			protected ValidationError decorate(ValidationError error, IValidatable<Category> validatable) {
				error.addKey(VALIDATIONKEY_FIELD_12_CODE);
				return super.decorate(error, validatable);
			}
		});

		typeOfFinance.getField().add(new TypeOfFinanceField12TypeOfAidCodeValidator(typeOfAid) {
			@Override
			protected ValidationError decorate(ValidationError error, IValidatable<Category> validatable) {
				error.addKey(VALIDATIONKEY_FIELD_12_TYPE_OF_AID_CODE);
				return super.decorate(error, validatable);
			}
		});
		add(typeOfFinance);

		TextAreaInputField activityProjectTitle = new TextAreaInputField("14activityProjectTitle",
				new RWComponentPropertyModel<String>("shortDescription"));
		activityProjectTitle.maxContentLength(150);// max length for activity
													// project title
		add(activityProjectTitle);

		DropDownField<Category> sectorPurposeCode = new DropDownField<>("15sectorPurposeCode",
				new RWComponentPropertyModel<Category>("sector"), categoryFactory.get(CategoryConstants.ALL_SECTOR_TAG));
		add(sectorPurposeCode);
	}

	@Override
	public String getPermissionKey() {
		return KEY;
	}

	@Override
	public void enableRequired() {
	}
}
