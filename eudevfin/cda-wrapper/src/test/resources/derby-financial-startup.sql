
delete from FINANCIAL_TRANSACTIONS;
delete from organization;

insert into organization (id, name) values (1, 'CDA Test Org 1');
insert into organization (id, name) values (2, 'CDA Test Org 2');

insert into FINANCIAL_TRANSACTIONS (id, amount, description, ORGANIZATION_ID) values (1, 1000, 'Test transaction', 1);
insert into FINANCIAL_TRANSACTIONS (id, amount, description, ORGANIZATION_ID) values (2, 1000, 'Test transaction', 1);
insert into FINANCIAL_TRANSACTIONS (id, amount, description, ORGANIZATION_ID) values (3, 1000, 'Test transaction', 1);
insert into FINANCIAL_TRANSACTIONS (id, amount, description, ORGANIZATION_ID) values (4, 1000, 'Test transaction', 1);
insert into FINANCIAL_TRANSACTIONS (id, amount, description, ORGANIZATION_ID) values (5, 1000, 'Test transaction', 2);
insert into FINANCIAL_TRANSACTIONS (id, amount, description, ORGANIZATION_ID) values (6, 1000, 'Test transaction', 2);
insert into FINANCIAL_TRANSACTIONS (id, amount, description, ORGANIZATION_ID) values (7, 1000, 'Test transaction', 2);
insert into FINANCIAL_TRANSACTIONS (id, amount, description, ORGANIZATION_ID) values (8, 1000, 'Test transaction', 2);
insert into FINANCIAL_TRANSACTIONS (id, amount, description, ORGANIZATION_ID) values (9, 1000, 'Test transaction', 2);
insert into FINANCIAL_TRANSACTIONS (id, amount, description, ORGANIZATION_ID) values (10, 1000, 'Test transaction', 2);

