/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.dim.pages.transaction.crs;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.util.visit.IVisit;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.auth.common.service.PersistedUserService;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.dim.pages.HomePage;
import org.devgateway.eudevfin.dim.pages.transaction.custom.CustomTransactionPage;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.Message;
import org.devgateway.eudevfin.financial.service.CurrencyMetadataService;
import org.devgateway.eudevfin.financial.service.FinancialTransactionService;
import org.devgateway.eudevfin.financial.service.MessageService;
import org.devgateway.eudevfin.financial.util.FinancialTransactionUtil;
import org.devgateway.eudevfin.ui.common.AttributePrepender;
import org.devgateway.eudevfin.ui.common.Constants;
import org.devgateway.eudevfin.ui.common.components.BootstrapCancelButton;
import org.devgateway.eudevfin.ui.common.components.BootstrapDeleteButton;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.devgateway.eudevfin.ui.common.components.tabs.BootstrapJSTabbedPanel;
import org.devgateway.eudevfin.ui.common.components.tabs.DefaultTabWithKey;
import org.devgateway.eudevfin.ui.common.components.tabs.ITabWithKey;
import org.devgateway.eudevfin.ui.common.components.util.MondrianCDACacheUtil;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.devgateway.eudevfin.ui.common.permissions.PermissionAwarePage;
import org.devgateway.eudevfin.ui.common.permissions.RoleActionMapping;
import org.joda.time.LocalDateTime;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@MountPath(value = "/transaction")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class TransactionPage extends HeaderFooter<FinancialTransaction> implements PermissionAwarePage {

    private static final long serialVersionUID = -3616689887136295555L;

    private static final Logger logger = Logger.getLogger(TransactionPage.class);

    public static final String PARAM_TRANSACTION_ID = "transactionId";
    public static final String PARAM_REUSE = "reuse";

    public final String onUnloadScript;


    @SpringBean
    private FinancialTransactionService financialTransactionService;

    @SpringBean
    private CurrencyMetadataService currencyMetadaService;

    @SpringBean
    MondrianCDACacheUtil mondrianCacheUtil;

    @SpringBean
    private MessageService mxService;

    @SpringBean
    private PersistedUserService userService;

    private static final CRSTransactionPermissionProvider componentPermissions = new CRSTransactionPermissionProvider();

    protected final NotificationPanel feedbackPanel;

    protected Form form;

    protected TransactionPageSubmitButton submitButton;

    private Message prepareMessage(FinancialTransaction financialTransaction) {
        boolean tlEditing = AuthUtils.currentUserHasRole(AuthConstants.Roles.ROLE_TEAMLEAD);

        if (financialTransaction.getId() == null) {
            //new transaction
            if (!tlEditing) {
                //we will send message to the team leader
                Message msg = newSystemMessage(AuthUtils.currentUsersTeamLead(), financialTransaction);
                msg.setSubject("New Transaction:" + financialTransaction.getShortDescription());
                return msg;
            }
        } else {
            String creator = financialTransaction.getCreatedBy();
            PersistedUser currentUser = AuthUtils.getCurrentUser();
            //if editor is the same as creator
            boolean creatorEditing = creator.equals(currentUser.getUsername());

            if (tlEditing && creatorEditing)
                return null; //no point in creating a message if tl is editing his transaction

            //load current transaction state in the db
            FinancialTransaction oldFTState = financialTransactionService.findOne(financialTransaction.getId()).getEntity();
            if (oldFTState == null) {
                logger.error("Can't find transaction with id " + financialTransaction.getId() + " in the database!");
                return null;
            }

            //compute needed variables
            boolean customFT = false;
            boolean transactionFinalized = false;
            boolean transactionApproved = false;
            if (financialTransaction instanceof CustomFinancialTransaction) {
                customFT = true;
                CustomFinancialTransaction customFinancialTransaction = (CustomFinancialTransaction) financialTransaction;
                //this shouldn't fail; means we have a transaction edited with CRS Form and with the Custom CRS Form
                CustomFinancialTransaction customOldFTState = (CustomFinancialTransaction) oldFTState;
                transactionFinalized = !customFinancialTransaction.getDraft() && customOldFTState.getDraft();
                transactionApproved = customFinancialTransaction.getApproved() && !customOldFTState.getApproved();
            }

            if (!tlEditing) {
                //Editing user is not team leader, but creator of the transaction
                //we will send message to the team leader
                PersistedUser toUser = AuthUtils.currentUsersTeamLead();
                Message msg = newSystemMessage(toUser, financialTransaction);

                if (customFT && transactionFinalized) {
                    //Generate Finalized Transaction Message
                    msg.setSubject("<span style=\"color: red;\">Transaction Finalized</span>: " + financialTransaction.getShortDescription());
                    return msg;
                }
                //Generate Regular Transaction Edit message
                msg.setSubject("Transaction Edited: " + financialTransaction.getShortDescription());
                return msg;
            } else {
                //Current user is TL but not the creator of the transaction
                //we will send message to the creator of the transaction
                String toUser = financialTransaction.getCreatedBy();
                Message msg = newSystemMessage(toUser, financialTransaction);

                //we must check if the transaction is of type CustomFinancialTransaction
                //else we can't get the approved/final states
                if (customFT && transactionApproved) {
                    //Generate Approved Message
                    msg.setSubject("<span style=\"color: green;\">Transaction Approved</span>: " + financialTransaction.getShortDescription());
                    return msg;
                }
            }
        }
        return null;
    }

    private Message newSystemMessage(PersistedUser user, FinancialTransaction transaction) {
        Message ret = new Message();
        ret.setSendDate(LocalDateTime.now());
        ret.setFrom(AuthUtils.getCurrentUser());
        ret.setTo(user);

        ret.setMessage(buildTransactionLinkMsg(transaction));

        return ret;
    }

    private String buildTransactionLinkMsg(FinancialTransaction transaction) {
        if (transaction.getId() == null)
            return null;
        String link = "?&transactionId=" + transaction.getId();
        if (transaction instanceof CustomFinancialTransaction)
            link = "." + CustomTransactionPage.class.getAnnotation(MountPath.class).value() + link + "&transactionType=" + ((CustomFinancialTransaction) transaction).getFormType();
        else
            link = "." + TransactionPage.class.getAnnotation(MountPath.class).value() + link;

        return "Click <a href=\"" + link + "\">here</a> to see the transaction!";
    }

    private Message newSystemMessage(String username, FinancialTransaction transaction) {
        PersistedUser user = userService.findByUsername(username).getEntity();
        if (user == null) {
            throw new AssertionError("User not found in the database!");
        }
        return newSystemMessage(user, transaction);
    }

    public class TransactionPageSubmitButton extends BootstrapSubmitButton {
        private static final long serialVersionUID = -8310280845870280505L;

        public TransactionPageSubmitButton(String id, IModel<String> model) {
            super(id, model);
        }

        Model<Boolean> shownFirstSection = null;

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
            FinancialTransaction transaction = (FinancialTransaction) form.getInnermostModel().getObject();
            logger.debug("Object:" + transaction);
            logger.info("Trying to save!");
            try {
                FinancialTransaction saved = financialTransactionService.save(transaction).getEntity();
                TransactionPage.this.getModel().setObject(saved);

                // clear the mondrian cache
                mondrianCacheUtil.flushMondrianCDACache();

                info(new NotificationMessage(new StringResourceModel("notification.saved", TransactionPage.this, null, null)));
                target.add(feedbackPanel);
            } catch (Exception e) {
                logger.error("Exception while trying to save:", e);
                error(new NotificationMessage(new StringResourceModel("notification.error", TransactionPage.this, null, null)).hideAfter(Duration.NONE));
                target.add(feedbackPanel);
                return;
            }
            logger.info("Saved ok!");
        }

        @Override
        protected void onError(AjaxRequestTarget target, Form<?> form) {
            shownFirstSection = Model.of(Boolean.FALSE);
            super.onError(target, form);
            error(new NotificationMessage(new StringResourceModel("notification.validationerrors", TransactionPage.this, null, null)));
            target.add(feedbackPanel);
        }

        @Override
        public void componentVisitor(AjaxRequestTarget target, FormComponent component, IVisit<Void> visit) {
            // TODO Auto-generated method stub
            super.componentVisitor(target, component, visit);
            if ((!component.isValid()) && (!shownFirstSection.getObject())) {
                target.focusComponent(component);
                target.appendJavaScript("$('#" + component.getMarkupId()
                        + "').parents('[class~=\"tab-pane\"]').siblings().attr(\"class\", \"tab-pane\");");
                target.appendJavaScript("$('#" + component.getMarkupId()
                        + "').parents('[class~=\"tab-pane\"]').attr(\"class\", \"tab-pane active\");");

                target.appendJavaScript("$('#" + component.getMarkupId()
                        + "').parents('[class~=\"tabbable\"]').children('ul').find('li').attr('class', '');");
                target.appendJavaScript("var idOfSection = $('#"
                        + component.getMarkupId()
                        + "').parents('[class~=\"tab-pane\"]').attr('id');$('#"
                        + component.getMarkupId()
                        + "').parents('[class~=\"tabbable\"]').children('ul').find('a[href=\"#' + idOfSection + '\"]').parent().attr('class', 'active');");

                shownFirstSection.setObject(Boolean.TRUE);
            }
        }
    }


    public class TransactionPageDeleteButton extends BootstrapDeleteButton {
        private static final long serialVersionUID = 1076134119844959564L;

        public TransactionPageDeleteButton(String id, IModel<String> model) {
            super(id, model);
            // TODO Auto-generated constructor stub
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
            FinancialTransaction transaction = (FinancialTransaction) form.getInnermostModel().getObject();
            logger.debug("Object:" + transaction);
            logger.info("Deleting!");
            setResponsePage(HomePage.class);
            try {
                if (transaction.getId() == null) return;
                financialTransactionService.delete(transaction);
                info(new NotificationMessage(new StringResourceModel("notification.deleted", TransactionPage.this, null, null)));
                target.add(feedbackPanel);
                // clear the mondrian cache
                mondrianCacheUtil.flushMondrianCDACache();
            } catch (Exception e) {
                logger.error("Exception while trying to delete:", e);
                return;
            }
            logger.info("Deleted ok!");
        }

    }

    /**
     * Initialized a previously freshly constructed {@link FinancialTransaction}
     *
     * @param transaction
     * @param parameters  the {@link PageParameters}
     */
    public void initializeFinancialTransaction(FinancialTransaction transaction, PageParameters parameters) {
        PersistedUser user = AuthUtils.getCurrentUser();
        FinancialTransactionUtil.initializeFinancialTransaction(transaction, this.currencyMetadaService, AuthUtils.getOrganizationForCurrentUser(), AuthUtils.getIsoCountryForCurrentUser());
    }

    @SuppressWarnings("unchecked")
    public TransactionPage(final PageParameters parameters) {
        super(parameters);

        //override the title
        pageTitle.setDefaultModel(new StringResourceModel(parameters.get(Constants.PARAM_TRANSACTION_TYPE).toString(""), this, null, null));

        onUnloadScript = "window.onbeforeunload = function(e) {\n" +
                "   var message = '" + new StringResourceModel("leaveMessage", this, null, null).getObject() + "';\n" +
                "   e = e || window.event;\n" +
                "   if(e) {\n" +
                "       e.returnValue = message;\n" +   // For IE 8 and old Firefox
                "   }\n" +
                "   return message;\n" +
                "};";

        // TODO: check that transactionType in the request parameters is the
        // same as the loaded transaction's type
        FinancialTransaction financialTransaction = null;

        if (!parameters.get(PARAM_TRANSACTION_ID).isNull()) {
            long transactionId = parameters.get(PARAM_TRANSACTION_ID).toLong();
            financialTransaction = financialTransactionService.findOne(transactionId).getEntity();
        } else {
            financialTransaction = getFinancialTransaction();
            initializeFinancialTransaction(financialTransaction, parameters);
        }

        CompoundPropertyModel<FinancialTransaction> model = new CompoundPropertyModel<FinancialTransaction>(
                financialTransaction);

        setModel(model);

        form = new Form("form");
        add(form);

        List<ITabWithKey> tabList = populateTabList(parameters);

        BootstrapJSTabbedPanel<ITabWithKey> bc = new BootstrapJSTabbedPanel<>("bc", tabList)
                .positionTabs(BootstrapJSTabbedPanel.Orientation.RIGHT);
        form.add(bc);

        submitButton = new TransactionPageSubmitButton("submit", new StringResourceModel("button.submit", this, null, null)) {
            private static final long serialVersionUID = -1909494416938537482L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                logger.info("Submit pressed");
                FinancialTransaction transaction = (FinancialTransaction) form.getInnermostModel().getObject();
                Message message = prepareMessage(transaction);
                super.onSubmit(target, form);

                if (!form.hasError()) {
                    //send the message if any
                    if (message != null)
                        mxService.save(message);
                    setResponsePage(HomePage.class);
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
                target.appendJavaScript(onUnloadScript);
            }
        };

        submitButton.add(new AttributePrepender("onclick", new Model<String>("window.onbeforeunload = null;"), " "));
        form.add(submitButton);

        TransactionPageSubmitButton saveButton = new TransactionPageSubmitButton("save", new StringResourceModel("button.save", this, null, null)) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if (TransactionPage.this instanceof CustomTransactionPage) {
                    CustomTransactionPage ctp = (CustomTransactionPage) TransactionPage.this;
                    ctp.getApproved().getField().setModelObject(false);
                    ctp.getDraft().getField().setModelObject(true);
                    target.add(ctp.getDraft().getField());
                    target.add(ctp.getApproved().getField());
                }

                super.onSubmit(target, form);
            }
        };
        saveButton.setDefaultFormProcessing(false);
        form.add(saveButton);

        TransactionPageDeleteButton transactionPageDeleteButton = new TransactionPageDeleteButton("delete", new StringResourceModel("button.delete", this, null, null));
        MetaDataRoleAuthorizationStrategy.authorize(transactionPageDeleteButton, Component.ENABLE, AuthConstants.Roles.ROLE_TEAMLEAD);
        form.add(transactionPageDeleteButton);

        form.add(new BootstrapCancelButton("cancel", new StringResourceModel("button.cancel", this, null, null)) {
            private static final long serialVersionUID = -3097577464142022353L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                logger.info("Cancel pressed");
                setResponsePage(HomePage.class);
            }

        });

        feedbackPanel = new NotificationPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        feedbackPanel.hideAfter(Duration.seconds(5));
        add(feedbackPanel);
    }

    protected FinancialTransaction getFinancialTransaction() {
        return new FinancialTransaction();
    }

    private List<ITabWithKey> populateTabList(PageParameters parameters) {
        List<Class<? extends Panel>> tabClasses = getTabs();
        ArrayList<ITabWithKey> tabs = new ArrayList<>();
        for (final Class<? extends Panel> p : tabClasses) {
            tabs.add(DefaultTabWithKey.of(p, this, parameters));
        }
        return tabs;
    }

    protected List<Class<? extends Panel>> getTabs() {
        List<Class<? extends Panel>> tabList = new ArrayList<>();
        tabList.add(IdentificationDataTab.class);
        tabList.add(BasicDataTab.class);
        tabList.add(SupplementaryDataTab.class);
        tabList.add(VolumeDataTab.class);
        tabList.add(ForLoansOnlyTab.class);
        return tabList;
    }

    @Override
    protected void onAfterRenderChildren() {
        super.onAfterRenderChildren();

    }

    @Override
    public HashMap<String, RoleActionMapping> getPermissions() {
        return componentPermissions.permissions();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(OnDomReadyHeaderItem.forScript(
                onUnloadScript));
    }
}
