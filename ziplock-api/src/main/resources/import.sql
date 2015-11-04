
insert into people (id, fullName, jobTitle) values (1, 'Ashley', 'asdfsdf');
insert into people (id, fullName, jobTitle) values (2, 'Rachel', 'Hughes');
insert into people (id, fullName, jobTitle) values (3, 'Michelle', 'Collins');
insert into people (id, fullName, jobTitle) values (4, 'Douglas', 'Clark');
insert into people (id, fullName, jobTitle) values (5, 'Justin', 'Rogers');
insert into people (id, fullName, jobTitle) values (6, 'Catherine', 'Knight');
insert into people (id, fullName, jobTitle) values (7, 'Dorothy', 'Lewis');
insert into people (id, fullName, jobTitle) values (8, 'Irene', 'Gomez');
insert into people (id, fullName, jobTitle) values (9, 'Betty', 'Watkins');
insert into people (id, fullName, jobTitle) values (10, 'Gregory', 'Phillips');

insert into job (id, paymentType, perks) values (1, 'Craig', 'Rose');
insert into job (id, paymentType, perks) values (2, 'Brandon', 'Chavez');
insert into job (id, paymentType, perks) values (3, 'Kathy', 'Walker');
insert into job (id, paymentType, perks) values (4, 'Brandon', 'Hamilton');
insert into job (id, paymentType, perks) values (5, 'James', 'Mendoza');
insert into job (id, paymentType, perks) values (6, 'Joe', 'Lopez');
insert into job (id, paymentType, perks) values (7, 'Theresa', 'Gonzalez');
insert into job (id, paymentType, perks) values (8, 'Lisa', 'Ruiz');
insert into job (id, paymentType, perks) values (9, 'Theresa', 'Gonzalez');
insert into job (id, paymentType, perks) values (10, 'Edward', 'Clark');

insert into people_job (people_id, job_id) values (1, 1);
insert into people_job (people_id, job_id) values (1, 2);
insert into people_job (people_id, job_id) values (1, 3);
insert into people_job (people_id, job_id) values (2, 5);
insert into people_job (people_id, job_id) values (5, 6);
insert into people_job (people_id, job_id) values (7, 8);

insert into datasource (id, url, username) values (9, 'Theresa', 'Gonzalez');
insert into datasource (id, url, username) values (10, 'Edward', 'Clark');
