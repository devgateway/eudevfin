/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.reports.components;

import java.util.Arrays;
import java.util.List;

/**
 * @author aartimon
 * @since 13/12/13
 */
public class TableDefinitionParameters {
    private List<String> colHeaders = Arrays.asList("Col 1", "Col 2", "Amount");
    private List<String> colTypes = Arrays.asList("numeric", "string", "numeric");
    private List<String> colFormats = Arrays.asList("%d", "%s", "%d");
    private List<String> colWidths = Arrays.asList("30%", "40", "30%");
    private List<Boolean> colSortable = Arrays.asList(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
    private Boolean paginate = Boolean.TRUE;
    private Boolean filter = Boolean.TRUE;
    private Boolean info = Boolean.TRUE;
    private String dataAccessId = "simpleSQLQuery";


    private String tableStyle = "themeroller";
    private String paginationType = "full_numbers";
    private Boolean paginateServerside = Boolean.FALSE;
    private Boolean sort = Boolean.TRUE;
    private Boolean lengthChange = Boolean.FALSE;
    private Integer displayLength = 10;
    private String path = "/some/path";
}
