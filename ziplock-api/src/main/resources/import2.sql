
INSERT INTO Comparison (id, name) VALUES (1, 'Metadata');
INSERT INTO Comparison (id, name) VALUES (2, 'Data');
INSERT INTO Comparison (id, name) VALUES (3, 'Count');
INSERT INTO Comparison (id, name) VALUES (4, 'Existence');

INSERT INTO Relation (id, name, query) VALUES (1, 'RelationA_1', null);
INSERT INTO Relation (id, name, query) VALUES (2, 'RelationA_2', null);
INSERT INTO Relation (id, name, query) VALUES (3, 'RelationB_1', null);
INSERT INTO Relation (id, name, query) VALUES (4, 'RelationB_2', null);
INSERT INTO Relation (id, name, query) VALUES (5, 'RelationC_1', 'select * from C_1');
INSERT INTO Relation (id, name, query) VALUES (6, 'RelationC_2', 'select * from C_2');

INSERT INTO FieldType (id, long_name, short_name) VALUES (1, 'String', 'String');
INSERT INTO FieldType (id, long_name, short_name) VALUES (2, 'Long', 'Long');
INSERT INTO FieldType (id, long_name, short_name) VALUES (3, 'Date', 'Date');
INSERT INTO FieldType (id, long_name, short_name) VALUES (4, 'DateTime', 'DateTime');
INSERT INTO FieldType (id, long_name, short_name) VALUES (5, 'Time', 'Time');
INSERT INTO FieldType (id, long_name, short_name) VALUES (6, 'Timestamp', 'Timestamp');

INSERT INTO DateFormat (id, name, pattern) VALUES (1, 'American', 'mm/dd/yyyy');
INSERT INTO DateFormat (id, name, pattern) VALUES (2, 'default', 'yyyy-MM-dd HH:mm:ss');

INSERT INTO OrderDirection (id, label) VALUES (1, 'Ascending');
INSERT INTO OrderDirection (id, label) VALUES (2, 'Descending');

INSERT INTO TestType (id, label) VALUES (1, 'Full');
INSERT INTO TestType (id, label) VALUES (2, 'Incremental');
INSERT INTO TestType (id, label) VALUES (3, 'Historic');

INSERT INTO RelationMap (id, relationA_id, relationB_id) VALUES (1, 1, 2);
INSERT INTO RelationMap (id, relationA_id, relationB_id) VALUES (2, 1, 1);
INSERT INTO RelationMap (id, relationA_id, relationB_id) VALUES (3, 2, 2);
INSERT INTO RelationMap (id, relationA_id, relationB_id) VALUES (4, 3, 4);
INSERT INTO RelationMap (id, relationA_id, relationB_id) VALUES (5, 5, 6);

INSERT INTO DatasourceType (id, Label) VALUES (1, 'MySql');
INSERT INTO DatasourceType (id, Label) VALUES (2, 'Hive2');
INSERT INTO DatasourceType (id, Label) VALUES (3, 'Oracle');
INSERT INTO DatasourceType (id, Label) VALUES (4, 'Sql Server');
INSERT INTO DatasourceType (id, Label) VALUES (5, 'Netezza');
INSERT INTO DatasourceType (id, Label) VALUES (6, 'Vertica');

INSERT INTO Datasource (id, database_schema, hive_principal_queue, host, name, password, port, url, username, datasourceType_id) VALUES (1, 'QA_UED_QBO_PSA', null, 'nztwinfin01.bosptc.intuit.com', 'IAC Netezza', 'Uedc_123#', '5480', 'jdbc:netezza://nztwinfin01.bosptc.intuit.com:5480/QA_UED_QBO_PSA', 'uedc_etl', 5);
INSERT INTO Datasource (id, database_schema, hive_principal_queue, host, name, password, port, url, username, datasourceType_id) VALUES (2, 'psplt2prod', null, 'pprdpsplt2-drdb.corp.intuit.net', 'PSP prod', 'Intuit2014', '1521', 'jdbc:oracle:thin:@//pprdpsplt2-drdb.corp.intuit.net:1521/psplt2prod', 'iacetl', 3);

INSERT INTO Dataset (id, name, datasourceA_id, datasourceB_id) VALUES (1, 'QBO', 1, 2);

INSERT INTO RelationMapConfig (id, fetch_amount, dateFormatA_id, dateFormatB_id, orderDirectionA_id, orderDirectionB_id, relationMap_id, testTypeA_id, testTypeB_id) VALUES (1, 100, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO RelationMapConfig (id, fetch_amount, dateFormatA_id, dateFormatB_id, orderDirectionA_id, orderDirectionB_id, relationMap_id, testTypeA_id, testTypeB_id) VALUES (2, 100, 2, 2, 2, 2, 4, 1, 1);

INSERT INTO Reporter (id, cluster_name, host, idx, name, port, protocol, verbosity_level) VALUES (1, null, 'host', 'index', 'email', null, null, 'high');
INSERT INTO Reporter (id, cluster_name, host, idx, name, port, protocol, verbosity_level) VALUES (2, null, null, null, 'directory', null, null, 'low');
INSERT INTO Reporter (id, cluster_name, host, idx, name, port, protocol, verbosity_level) VALUES (3, null, null, null, 'graphite', null, null, 'high');

INSERT INTO Config (id, debug, email, label, dataset_id) VALUES (1, 0, 'albert_chau@intuit.com', 'seed', 1);

INSERT INTO Config_Reporter (Config_id, reporters_id) VALUES (1, 1);
INSERT INTO Config_Reporter (Config_id, reporters_id) VALUES (1, 2);
INSERT INTO Config_RelationMapConfig (configs_id, relationMapConfigs_id) VALUES (1, 1);
INSERT INTO Config_RelationMapConfig (configs_id, relationMapConfigs_id) VALUES (1, 2);

INSERT INTO RunStatus (id, label) VALUES (1, 'Running');
INSERT INTO RunStatus (id, label) VALUES (2, 'Queued');
INSERT INTO RunStatus (id, label) VALUES (3, 'Finished');
INSERT INTO RunStatus (id, label) VALUES (4, 'Errored');

INSERT INTO Run (id, created_date, end_timestamp, start_timestamp, config_id, runStatus_id) VALUES (1, '2015-11-03 17:01:22', '2015-11-05 17:01:29', '2015-11-04 17:01:35', 1, 2);

