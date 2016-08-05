insert into branch (id_branch, email, name) values (0, 'la-buena@gmail.com', 'La buena');

insert into biker (id_biker, email, name, stock, phone, id_branch) values (0, 'morales.fernandez.clemente@gmail.com', 'Clemente Morales Fernandez', 23, '7347896184', 1);
insert into biker (id_biker, email, name, stock, phone, id_branch) values (0, 'morales.fernandez.williams@gmail.com', 'William Morales Fernandez', 16, '0447896184', 1);
insert into biker (id_biker, email, name, stock, phone, id_branch) values (0, 'rocke.santa.cruz@gmail.com', 'Rocke Santa Cruz', 41, '0797896184', 1);

insert into la_buena_db.location (id_location, latitude, longitude) values (0, '60.06484', '-135.878906');
insert into la_buena_db.location (id_location, latitude, longitude) values (0, '40.6529161', '-74.5717188');
insert into la_buena_db.location (id_location, latitude, longitude) values (0, '20.6529161', '-50.5717188');

insert into la_buena_db.client (id_client, email, name) values (0, 'flora-chama@gmail.com', 'Flora Chama');
insert into la_buena_db.client (id_client, email, name) values (0, 'genobeba-Rendon@gmail.com', 'Genobeba Rendon');
insert into la_buena_db.client (id_client, email, name) values (0, 'marco-fabian@gmail.com', 'Marco Fabian');

insert into la_buena_db.order (id_order, id_client, id_biker, id_location, quantity) values (0, 1, 1, 1, 3);
insert into la_buena_db.order (id_order, id_client, id_biker, id_location, quantity) values (0, 2, 1, 1, 5);
insert into la_buena_db.order (id_order, id_client, id_biker, id_location, quantity) values (0, 3, 2, 1, 7);