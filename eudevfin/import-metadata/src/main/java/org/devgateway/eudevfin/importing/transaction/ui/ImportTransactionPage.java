/**
 *
 */
package org.devgateway.eudevfin.importing.transaction.ui;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.Files;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.financial.FileWrapper;
import org.devgateway.eudevfin.financial.FileWrapperContent;
import org.devgateway.eudevfin.importing.transaction.TransactionImporterEngine;
import org.devgateway.eudevfin.importing.transaction.TransactionImporterEngine.TransformationResult;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.devgateway.eudevfin.ui.common.components.MultiFileUploadField;
import org.devgateway.eudevfin.ui.common.components.util.MondrianCDACacheUtil;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.springframework.util.CollectionUtils;
import org.wicketstuff.annotation.mount.MountPath;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;

/**
 * @author alexandru-m-g
 *
 */
@MountPath(value = "/import-transactions")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_SUPERVISOR)
public class ImportTransactionPage extends HeaderFooter {

	public static final String TEMPLATE_PATH = "/templates/transaction-import-template.xls";

	@SpringBean
	TransactionImporterEngine importerEngine;

	@SpringBean
	MondrianCDACacheUtil mondrianCDACacheUtil;

	private NotificationPanel feedbackPanel;

	public ImportTransactionPage() {
		super();
		this.generalInit();
	}

	private void generalInit() {
		final Model<ImportFileUpload> model = Model.of(new ImportFileUpload());
		final Form<ImportFileUpload> form = new Form<ImportTransactionPage.ImportFileUpload>("import-transactions-form", model);
		this.add(form);

		final MultiFileUploadField bisUploadDocumentation = new MultiFileUploadField("import-file-upload", new PropertyModel<Collection<FileWrapper>>(model, "files"));
		bisUploadDocumentation.maxFiles(1);
		form.add(bisUploadDocumentation);

		final BootstrapSubmitButton button = new BootstrapSubmitButton("import-transactions-form-submit", new StringResourceModel("import-transactions-form.submit", this, null)) {

			@Override
			protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
				final ImportFileUpload fileUpload = (ImportFileUpload) form.getInnermostModel().getObject();
				final Collection<FileWrapper> files = fileUpload.getFiles();
				if ( !CollectionUtils.isEmpty(files) ) {
					final FileWrapperContent fileContent = files.iterator().next().getContent();
					final ByteArrayInputStream iStream = new ByteArrayInputStream(fileContent.getBytes());
					final TransformationResult result = ImportTransactionPage.this.importerEngine.process(iStream);

					if ( result.isSuccess() ) {
						ImportTransactionPage.this.mondrianCDACacheUtil.flushMondrianCDACache();
						this.info(new NotificationMessage(new StringResourceModel("import-transactions.message.saved", ImportTransactionPage.this, null, null)));
					}
					else {
						if ( result.getColNum() != null && result.getColNum() >= 0) {
							this.error(new NotificationMessage(new StringResourceModel("import-transactions.message.error-with-col-num",
									ImportTransactionPage.this, null, result.getRowNum(), result.getColNum(), result.getMessage())));
						} else {
							this.error(new NotificationMessage(new StringResourceModel("import-transactions.message.error", ImportTransactionPage.this, null, result.getRowNum(), result.getMessage())));
						}
					}
				}
				else {
					this.error(new NotificationMessage(new StringResourceModel("import-transactions.message.nofiles", ImportTransactionPage.this, null, null)));
				}
				target.add(ImportTransactionPage.this.feedbackPanel);
				//				super.onSubmit();
			}

			@Override
			protected void onError(final AjaxRequestTarget target, final Form<?> form) {
				this.error(new NotificationMessage(new StringResourceModel("import-transactions.message.problem", ImportTransactionPage.this, null, null)));
				target.add(ImportTransactionPage.this.feedbackPanel);
			}

		};
		button.setLabel(Model.of(""));
		form.add(button);

		this.feedbackPanel = new NotificationPanel("feedback");
		this.feedbackPanel.setOutputMarkupId(true);
		//		this.feedbackPanel.hideAfter(Duration.seconds(5));
		this.add(this.feedbackPanel);

		final Label templateMessage = new Label("template-message", new StringResourceModel("import-transactions.template.message", this, null));
		this.add(templateMessage);

		final AbstractReadOnlyModel<File> fileModel = new AbstractReadOnlyModel<File>() {

			private static final long serialVersionUID = 7371372698329811889L;

			@Override
			public File getObject() {
				File file;
				try
				{
					file = File.createTempFile("import-template", ".xls");

					final InputStream is = this.getClass().getResourceAsStream(TEMPLATE_PATH);
					Files.writeTo(file, is);

				}
				catch (final IOException e)
				{
					throw new RuntimeException(e);
				}

				return file;
			}
		};

		final DownloadLink templateLink = new DownloadLink("template-link", fileModel);
		templateLink.add(new Label("template-link-name", new StringResourceModel("import-transactions.template.link.name", this, null)) );
		this.add(templateLink);

		this.populateWarningMessages();

	}

	private void populateWarningMessages() {
		final Label templateMessage = new Label("template-warnings-title", new StringResourceModel("import-transactions.message.warning.title", this, null));
		this.add(templateMessage);

		final List<WarningMessage> items = new ArrayList<WarningMessage>();
		items.add(new WarningMessage(new StringResourceModel("import-transactions.message.warning.filesize", this, null), null));
		items.add(new WarningMessage(new StringResourceModel("import-transactions.message.warning.not-twice", this, null), null));
		items.add(new WarningMessage(new StringResourceModel("import-transactions.message.warning.approved", this, null), null));
		items.add(new WarningMessage(new StringResourceModel("import-transactions.message.warning.dates", this, null), null));
		items.add(new WarningMessage(new StringResourceModel("import-transactions.message.warning.currency", this, null), "http://en.wikipedia.org/wiki/ISO_4217"));
		items.add(new WarningMessage(new StringResourceModel("import-transactions.message.warning.financial", this, null), null));

		final ListView<WarningMessage> warnings = new ListView<WarningMessage>("warning-list", items  ) {

			@Override
			protected void populateItem(final ListItem<WarningMessage> item) {
				final WarningMessage wm = item.getModelObject();
				item.add(new Label("warning-list-item", wm.getMessage()));

				final ExternalLink link = new ExternalLink("warning-list-item-link", wm.getUrl(), wm.getUrl() );
				if ( wm.getUrl() == null ) {
					link.setVisible(false);
				}
				item.add(link);

			}

		};

		this.add(warnings);
	}

	public class WarningMessage implements Serializable {
		private static final long serialVersionUID = 2294112728584396314L;
		private final StringResourceModel message;
		private final String url;

		public WarningMessage(final StringResourceModel message, final String url) {
			super();
			this.message = message;
			this.url = url;
		}
		public StringResourceModel getMessage() {
			return this.message;
		}
		public String getUrl() {
			return this.url;
		}


	}


	public class ImportFileUpload implements Serializable{
		private static final long serialVersionUID = 9016179136038912538L;
		Collection<FileWrapper> files;
		public Collection<FileWrapper> getFiles() {
			if ( this.files == null ) {
				this.files = new ArrayList<FileWrapper>();
			}
			return this.files;
		}
		public void setFiles(final List<FileWrapper> files) {
			this.files = files;
		}

	}

}
