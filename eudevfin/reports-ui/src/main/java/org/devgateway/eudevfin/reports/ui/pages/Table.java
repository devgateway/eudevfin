package org.devgateway.eudevfin.reports.ui.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.list.ListView;
import org.devgateway.eudevfin.reports.core.domain.QueryResult;
import org.devgateway.eudevfin.reports.core.service.QueryService;

import java.io.Serializable;
import java.util.List;

/**
 * @author idobre
 * @since 3/11/14
 */

public class Table extends RunMdxQuery implements Serializable {
    private static final Logger logger = Logger.getLogger(Table.class);

    private DataTableDashboard table;

    protected String tableId;
    protected String rowId;
    protected List<String[]> rows;
    protected QueryResult result;

    public Table (QueryService CdaService, String tableId, String rowId, String dataAccessId) {
        super(CdaService);

        this.tableId = tableId;
        this.rowId = rowId;

        table = new DataTableDashboard(tableId);

        this.setParam("dataAccessId", dataAccessId);
    }

    public DataTableDashboard getTable() {
        return table;
    }

    /**
     * Method that process the MDX result (it's similar with postExecution function from CDF plugin)
     * this method should be Override by each object to configure the desired output
     */
    public ListView<String[]> getTableRows () {
        return null;
    }

    public void addTableRows () {
        table.add(this.getTableRows());
    }

    public void setbPaginate(Boolean bPaginate) {
        table.setbPaginate(bPaginate);
    }

    public void setbFilter(Boolean bFilter) {
        table.setbFilter(bFilter);
    }

    public void setbInfo(Boolean bInfo) {
        table.setbInfo(bInfo);
    }

    public void setbSort(Boolean bSort) {
        table.setbSort(bSort);
    }

    public void setbLengthChange(Boolean bLengthChange) {
        table.setbLengthChange(bLengthChange);
    }

    public void setiDisplayLength(int iDisplayLength) {
        table.setiDisplayLength(iDisplayLength);
    }

    public void setbSortableColumn(Boolean bSortableColumn) {
        table.setbSortableColumn(bSortableColumn);
    }
}
