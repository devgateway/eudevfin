/**
 *
 */
package org.devgateway.eudevfin.importing.transaction.transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
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
			final Object srcValue = srcList.get(i);
			final ICellTransformer<?> cellTransformer = this.cellTransformers.get(i);
			cellTransformer.populateField(srcValue, ctx, context, this.servicesWrapper);
		}
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
