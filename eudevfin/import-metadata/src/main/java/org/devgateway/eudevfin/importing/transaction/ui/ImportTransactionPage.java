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

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.DownloadLink;
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
import org.devgateway.eudevfin.ui.common.components.MultiFileUploadField;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.springframework.util.CollectionUtils;
import org.wicketstuff.annotation.mount.MountPath;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;

/**
 * @author alexandru-m-g
 *
 */
@MountPath(value = "/import-transactions")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class ImportTransactionPage extends HeaderFooter {

	public static final String TEMPLATE_PATH = "/templates/transaction-import-template.xls";

	@SpringBean
	TransactionImporterEngine importerEngine;

	public ImportTransactionPage() {
		super();
		this.generalInit();
	}

	private void generalInit() {
		final Model<ImportFileUpload> model = Model.of(new ImportFileUpload());
		final Form<ImportFileUpload> form = new Form<ImportTransactionPage.ImportFileUpload>("import-transactions-form", model) {

			@Override
			protected void onSubmit() {
				final Collection<FileWrapper> files = this.getModelObject().getFiles();
				if ( !CollectionUtils.isEmpty(files) ) {
					final FileWrapperContent fileContent = files.iterator().next().getContent();
					final ByteArrayInputStream iStream = new ByteArrayInputStream(fileContent.getBytes());
					ImportTransactionPage.this.importerEngine.process(iStream);
				}
				super.onSubmit();
			}

		};
		this.add(form);

		final MultiFileUploadField bisUploadDocumentation = new MultiFileUploadField("import-file-upload", new PropertyModel<Collection<FileWrapper>>(model, "files"));
		bisUploadDocumentation.maxFiles(1);
		form.add(bisUploadDocumentation);

		final BootstrapButton button = new BootstrapButton("import-transactions-form-submit", new StringResourceModel("import-transactions-form.submit", this, null), Buttons.Type.Primary);
		button.setLabel(Model.of(""));
		form.add(button);

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
