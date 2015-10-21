/* 0. initiation */

drop database if exists aquila;
create database aquila charset=utf8;

use aquila;

/*
drop table if exists categories; 
drop table if exists products; 
drop table if exists users; 
drop table if exists group_role;
drop table if exists roles; 
drop table if exists groups;
drop table if exists images;
*/


/* 0.1 images */
create table images (
	id int not null auto_increment,
	name varchar(32),
	content_type varchar(32),
	content mediumblob,
	length int,
	create_date date not null,
	primary key (id)
);

/* 1. groups */
create table groups (
	id int not null auto_increment,
	name varchar(32) not null,
	description varchar(32),
	primary key (id)
);

insert into groups (name, description) values ('GROUP_SYSTEM_ADMIN', '系统管理员组');
insert into groups (name, description) values ('GROUP_USER', '用户组');


/* 2. roles */
create table roles (
	id int not null auto_increment, 
	name varchar(32) not null,
	description varchar(32),
	primary key (id)
);

insert into roles (name, description) values ('ROLE_USER', '注册用户');
insert into roles (name, description) values ('ROLE_USER_GROUP_MANAGER', '用户组管理');
insert into roles (name, description) values ('ROLE_USER_MANAGER', '用户管理');


/* 3. group_role */
create table group_role (
	group_id int not null,
	role_id int not null,
	primary key (group_id, role_id),
	foreign key (group_id) references groups (id),
	foreign key (role_id) references roles (id)
);

insert into group_role (group_id, role_id) values (1, 1);
insert into group_role (group_id, role_id) values (1, 2);
insert into group_role (group_id, role_id) values (1, 3);
insert into group_role (group_id, role_id) values (2, 1);



/* 4. users */
create table users (
    id int not null auto_increment,
    userid varchar(32) not null,
    password varchar(32) not null,
    name varchar(32),
    email varchar(32),
    group_id int not null,
    active_flg boolean default 1,
    create_date date not null,
    update_datetime timestamp null,
    primary key (id),
    foreign key (group_id) references groups (id)
);

insert into users (userid, password, name, email, group_id, active_flg, create_date, update_datetime) values ('admin', 'admin123', '系统管理员', 'admin@gmail.com', 1, true, '2013-06-01', null);
insert into users (userid, password, name, email, group_id, active_flg, create_date, update_datetime) values ('user01', 'user01', 'user01', 'user01@gmail.com', 2, true, '2013-06-01', null);
insert into users (userid, password, name, email, group_id, active_flg, create_date, update_datetime) values ('user02', 'user02', 'user02', 'user02@gmail.com', 2, true, '2013-06-02', null);
insert into users (userid, password, name, email, group_id, active_flg, create_date, update_datetime) values ('user03', 'user03', 'user03', 'user03@gmail.com', 2, true, '2013-06-03', null);
insert into users (userid, password, name, email, group_id, active_flg, create_date, update_datetime) values ('user04', 'user04', 'user04', 'user04@gmail.com', 2, true, '2013-06-04', null);
insert into users (userid, password, name, email, group_id, active_flg, create_date, update_datetime) values ('user05', 'user05', 'user05', 'user05@gmail.com', 2, true, '2013-06-05', null);

/* 5. products */
create table products (
	id int not null auto_increment,
	goods_id int not null,
	product_name varchar(64) not null,
	shop_id int not null,
	shop_name varchar(32) not null,
	month_sale_amount int not null,
	create_datetime timestamp null,
	primary key (id)
);

/* 6. categories */
create table categories (
	id int not null auto_increment,
	product_id int not null,
	category_name varchar(32) not null,
	category_price decimal(8,2) not null, 
	category_stock_number int not null,
	primary key (id),
	foreign key (product_id) references products (id)
);


