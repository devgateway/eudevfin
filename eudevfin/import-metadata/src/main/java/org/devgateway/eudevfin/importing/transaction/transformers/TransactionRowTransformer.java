/**
 *
 */
package org.devgateway.eudevfin.importing.transaction.transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.importing.transaction.exceptions.TransactionTransformerException;
import org.devgateway.eudevfin.metadata.common.service.AreaService;
import org.devgateway.eudevfin.metadata.common.service.CategoryService;
import org.devgateway.eudevfin.metadata.common.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author alexandru-m-g
 *
 */
@Component
public class TransactionRowTransformer implements IRowTransformer {

	List<ICellTransformer<?>> cellTransformers;

	@Autowired
	ServicesWrapper servicesWrapper;



	@PostConstruct
	private void init() {
		this.cellTransformers = new ArrayList<ICellTransformer<?>>();
		this.createAllCellTransformers();
	}

	@Override
	public CustomFinancialTransaction transform(final List<Object> srcList) {
		if ( srcList.size() != this.cellTransformers.size() ) {
			throw new IllegalArgumentException(String.format(
					"Source list has %d entries when it should have %d",
					srcList.size(), this.cellTransformers.size() ));
		}

		final Map<String, Object> context = new HashMap<String, Object>();

		final CustomFinancialTransaction ctx = new CustomFinancialTransaction();

		for (int i=0; i<srcList.size(); i++) {
			try {
				final Object srcValue = srcList.get(i);
				final ICellTransformer<?> cellTransformer = this.cellTransformers.get(i);
				cellTransformer.populateField(srcValue, ctx, context, this.servicesWrapper);
			}
			catch (final Exception e) {
				final TransactionTransformerException ex = new TransactionTransformerException(e, i);
				throw ex;
			}
		}
		/* Mark the transaction as approved */
		ctx.setDraft(false);
		ctx.setApproved(true);

		/* Add the org of the current user as extending agency */
		if (ctx.getExtendingAgency()==null) 
			ctx.setExtendingAgency(AuthUtils.getOrganizationForCurrentUser());

		return ctx;
	}

	@Override
	public List<ICellTransformer<?>> getCellTransformerList() {
		return this.cellTransformers;
	}

	private void createAllCellTransformers() {
		this.cellTransformers.add(new CellTransformers.ReportingYear());
		this.cellTransformers.add(new CellTransformers.CommitmentDate());
		this.cellTransformers.add(new CellTransformers.DonorCode());
		this.cellTransformers.add(new CellTransformers.ExtendingAgency());
		this.cellTransformers.add(new CellTransformers.CrsId());
		this.cellTransformers.add(new CellTransformers.DonorProjectNumber());
		this.cellTransformers.add(new CellTransformers.NatureOfSubmission());
		this.cellTransformers.add(new CellTransformers.RecipientCountry());
		this.cellTransformers.add(new CellTransformers.Channel());
		this.cellTransformers.add(new CellTransformers.BiMultilateral());
		this.cellTransformers.add(new CellTransformers.TypeOfFlow());
		this.cellTransformers.add(new CellTransformers.TypeOfFinance());
		this.cellTransformers.add(new CellTransformers.TypeOfAid());
		this.cellTransformers.add(new CellTransformers.ShortDescription());
		this.cellTransformers.add(new CellTransformers.Sectors());
		this.cellTransformers.add(new CellTransformers.GeographicalTargetArea());
		this.cellTransformers.add(new CellTransformers.ExpectedStartDate());
		this.cellTransformers.add(new CellTransformers.ExpectedCommpletionDate());
		this.cellTransformers.add(new CellTransformers.Description());
		this.cellTransformers.add(new CellTransformers.GenderEquality());
		this.cellTransformers.add(new CellTransformers.AidToEnvironment());
		this.cellTransformers.add(new CellTransformers.Pdgg());
		this.cellTransformers.add(new CellTransformers.TradeDevelopment());
		this.cellTransformers.add(new CellTransformers.FreestandingTechnicalCooperation());
		this.cellTransformers.add(new CellTransformers.ProgrammeBasedApproach());
		this.cellTransformers.add(new CellTransformers.InvestmentProject());
		this.cellTransformers.add(new CellTransformers.AssociatedFinancing());
		this.cellTransformers.add(new CellTransformers.Biodiversity());
		this.cellTransformers.add(new CellTransformers.ClimateChangeMitigation());
		this.cellTransformers.add(new CellTransformers.ClimateChangeAdaptation());
		this.cellTransformers.add(new CellTransformers.Desertification());
		this.cellTransformers.add(new CellTransformers.CurrencyCode());
		this.cellTransformers.add(new CellTransformers.Commitments());
		this.cellTransformers.add(new CellTransformers.AmountsExtended());
		this.cellTransformers.add(new CellTransformers.AmountsReceived());
		this.cellTransformers.add(new CellTransformers.AmountsUntied());
		this.cellTransformers.add(new CellTransformers.AmountsPartiallyUntied());
		this.cellTransformers.add(new CellTransformers.AmountsTied());
		this.cellTransformers.add(new CellTransformers.AmountOfIrtc());
		this.cellTransformers.add(new CellTransformers.AmountsOfExpertCommitment());
		this.cellTransformers.add(new CellTransformers.AmountsOfExpertExtended());
		this.cellTransformers.add(new CellTransformers.AmountsOfExportCredit());
		this.cellTransformers.add(new CellTransformers.TypeOfRepayment());
		this.cellTransformers.add(new CellTransformers.NumberOfRepayments());
		this.cellTransformers.add(new CellTransformers.InterestRate());
		this.cellTransformers.add(new CellTransformers.SecondInterestRate());
		this.cellTransformers.add(new CellTransformers.FirstRepaymentDate());
		this.cellTransformers.add(new CellTransformers.FinalRepaymentDate());
		this.cellTransformers.add(new CellTransformers.InterestReceived());
		this.cellTransformers.add(new CellTransformers.PrincipalDisbursdeOutstanding());
		this.cellTransformers.add(new CellTransformers.ArrearsOfPrincipal());
		this.cellTransformers.add(new CellTransformers.ArrearsOfInterest());
		this.cellTransformers.add(new CellTransformers.Rmnch());
		this.cellTransformers.add(new CellTransformers.ChannelInstituteName());

	}

	@Component
	public static class ServicesWrapper {

		@Autowired
		CategoryService categoryService;

		@Autowired
		OrganizationService orgService;

		@Autowired
		AreaService areaService;

		public OrganizationService getOrgService() {
			return this.orgService;
		}

		public CategoryService getCategoryService() {
			return this.categoryService;
		}

		public AreaService getAreaService() {
			return this.areaService;
		}


	}

}
