var pathToCdaFile = '/some/path.cda';
var testTableDefinition,
    testTable;

testTableDefinition = {
    colHeaders: ["Col 1", "Col 2", "Amount"],
    colTypes: ['numeric', 'string', 'numeric'],
    colFormats: ['%d', '%s', '%d'],
    colWidths: ['30%', '40', '30%'],
    colSortable: [true, true, true],
    // sortBy: [[2, 'desc']],	for now the sort on CDA is not working 
    tableStyle: "themeroller",
    paginationType: "full_numbers",
    paginateServerside: false,
    paginate: true,
    filter: true,
    info: true,
    sort: true,
    lengthChange: false,
    displayLength: 4,
    // query properties
    path: pathToCdaFile,                 // I don't think we will need this property
    dataAccessId: "simpleSQLQuery"
};

testTable = {
    name: "testTable",
    type: "tableComponent",
    listeners:[],
    parameters: [],
    chartDefinition: testTableDefinition,
    htmlObject: "test-table",
    executeAtStart: true,
    preChange: function (value) {
        console.log('>>> preChange');

        return value;
    },
    postFetch: function (values) {
        console.log('>>> postFetch');
        // debugger;
        console.log(values);

        return values;
    },
    preExecution: function () {
        console.log('>>> preExecution');

        return undefined;
    },
    postExecution: function () {
        console.log('>>> postExecution');
    }
};

// The components to be loaded into the dashboard withing the [] separated by ,
var components = [testTable];

// The initial dashboard load function definition
var load = function () {
    Dashboards.init(components);
};

// The initial dashboard load function execution
load();