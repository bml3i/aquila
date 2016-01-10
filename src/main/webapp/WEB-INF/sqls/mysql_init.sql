/* 0. initiation */

drop database if exists aquila;
create database aquila charset=utf8;

use aquila;


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
	product_id bigint not null,
	product_name varchar(64) not null,
	month_sale_amount int not null,
	product_price decimal(8,2) not null,
	fav_count int not null,
	shop_name varchar(32) not null,
	active_flg boolean default 1,
	create_datetime timestamp null,
	primary key (id)
);

insert into products (product_id, product_name, month_sale_amount, product_price, fav_count, shop_name, active_flg, create_datetime) values (123456, '测试宝贝', 1200, 11.5, 101, '测试商店', true, '2015-10-01 00:00:00');

/* 6. categories */
create table categories (
	id int not null auto_increment,
	product_table_id int not null,
	category_name varchar(32) not null,
	category_price decimal(8,2) not null, 
	category_stock_number int not null,
	primary key (id),
	foreign key (product_table_id) references products (id)
);

insert into categories (product_table_id, category_name, category_price, category_stock_number) values (1, '白色款', 10.10, 100);
insert into categories (product_table_id, category_name, category_price, category_stock_number) values (1, '黑色款', 12.00, 100);
insert into categories (product_table_id, category_name, category_price, category_stock_number) values (1, '红色款', 25.00, 100);
insert into categories (product_table_id, category_name, category_price, category_stock_number) values (1, '蓝色款', 30.00, 100);


/* 7. rank_search_queue */
create table rank_search_queue (
	id int not null auto_increment, 
	keyword varchar(32) not null,
	create_datetime timestamp null,
	primary key (id)
);

insert into rank_search_queue (keyword, create_datetime) values ('魔方', '2015-12-01 00:00:00');


/* 8. rank_search_types */
create table rank_search_types (
	id int not null auto_increment, 
	name varchar(32) not null,
	description varchar(32),
	sort_type varchar(32) not null,
	primary key (id)
);

insert into rank_search_types (name, description, sort_type) values ('DEFAULT', '默认排序', 'default');
insert into rank_search_types (name, description, sort_type) values ('RENQI', '人气排序', 'renqi-desc');
insert into rank_search_types (name, description, sort_type) values ('XIAOLIANG', '销量排序', 'sale-desc');
insert into rank_search_types (name, description, sort_type) values ('XINYONG', '信用排序', 'credit-desc');

/* 9. rank_search_queue_type */
create table rank_search_queue_type (
	rank_search_queue_id int not null,
	rank_search_type_id int not null,
	primary key (rank_search_queue_id, rank_search_type_id),
	foreign key (rank_search_queue_id) references rank_search_queue (id),
	foreign key (rank_search_type_id) references rank_search_types (id)
);

insert into rank_search_queue_type (rank_search_queue_id, rank_search_type_id) values (1, 1);
insert into rank_search_queue_type (rank_search_queue_id, rank_search_type_id) values (1, 2);
insert into rank_search_queue_type (rank_search_queue_id, rank_search_type_id) values (1, 3);


/* 10. jobs */
create table jobs (
	id int not null auto_increment, 
	class_name varchar(128) not null,
	description varchar(32),
	active_flg boolean default 1,
	run_status varchar(4) not null, /* C=Completed, I=In-Process, F=Failure */
	create_datetime timestamp null,
	start_datetime timestamp null,
	end_datetime timestamp null,
	min_interval_minute int default 0 not null,
	primary key (id)
);

insert into jobs (class_name, description, active_flg, run_status, create_datetime, start_datetime, end_datetime, min_interval_minute) values ('club.magicfun.aquila.job.RankSearchJob', 'Rank Search Job', 1, 'C', '2015-12-01 00:00:00', null, null, 120);
insert into jobs (class_name, description, active_flg, run_status, create_datetime, start_datetime, end_datetime, min_interval_minute) values ('club.magicfun.aquila.job.ProductSearchJob', 'Product Search Job', 0, 'C', '2015-12-01 00:00:00', null, null, 2);

-- extract proxy and validate it
insert into jobs (class_name, description, active_flg, run_status, create_datetime, start_datetime, end_datetime, min_interval_minute) values ('club.magicfun.aquila.job.GetAgentsJob', 'Get Agents Job', 0, 'C', '2015-12-01 00:00:00', null, null, 5);

-- test baidu auto-click
insert into jobs (class_name, description, active_flg, run_status, create_datetime, start_datetime, end_datetime, min_interval_minute) values ('club.magicfun.aquila.job.BaiduAutoClickJob', 'Baidu Auto-Click Job', 0, 'C', '2015-12-01 00:00:00', null, null, 1);


/* 11. ranks */
create table ranks (
	id int not null auto_increment, 
	rank_search_queue_id int not null,
	rank_search_type_id int not null,
	rank_num int not null,
	product_id bigint not null,
	product_name varchar(64) not null,
	product_price decimal(8,2) not null,
	deal_count int not null,
	shop_name varchar(32) not null,
	create_datetime timestamp not null,
	primary key (id)
);

/* 12. product_search_queue */
create table product_search_queue (
	id int not null auto_increment, 
	product_id bigint not null,
	retry_cnt smallint default 0 not null,
	create_datetime timestamp null,
	primary key (id)
);

-- TAOBAO
insert into product_search_queue (product_id, retry_cnt, create_datetime) values (44057413538, 0, '2015-12-01 00:00:00');
insert into product_search_queue (product_id, retry_cnt, create_datetime) values (42453456250, 0, '2015-12-01 00:00:00');
insert into product_search_queue (product_id, retry_cnt, create_datetime) values (41129702139, 0, '2015-12-01 00:00:00');
insert into product_search_queue (product_id, retry_cnt, create_datetime) values (1771185060, 0, '2015-12-01 00:00:00');
insert into product_search_queue (product_id, retry_cnt, create_datetime) values (37833049213, 0, '2015-12-01 00:00:00');
insert into product_search_queue (product_id, retry_cnt, create_datetime) values (38403988528, 0, '2015-12-01 00:00:00');

-- TMALL
insert into product_search_queue (product_id, retry_cnt, create_datetime) values (5968030997, 0, '2015-12-01 00:00:00');
insert into product_search_queue (product_id, retry_cnt, create_datetime) values (523767556371, 0, '2015-12-01 00:00:00');
insert into product_search_queue (product_id, retry_cnt, create_datetime) values (524740490317, 0, '2015-12-01 00:00:00');
insert into product_search_queue (product_id, retry_cnt, create_datetime) values (521299241246, 0, '2015-12-01 00:00:00');
insert into product_search_queue (product_id, retry_cnt, create_datetime) values (520878856645, 0, '2015-12-01 00:00:00');
insert into product_search_queue (product_id, retry_cnt, create_datetime) values (45855398633, 0, '2015-12-01 00:00:00');

/* 13. agents */
create table agents (
	id int not null auto_increment, 
	ip_address varchar(16) not null,
	port_num smallint not null,
	description varchar(64),
	active_flg boolean default 0,
	retry_cnt smallint default 0 not null,
	delay bigint null,
	create_datetime timestamp not null,
	update_datetime timestamp null,
	primary key (id)
);


/* 14. my_logs */
create table my_logs (
	id int not null auto_increment, 
	log_type varchar(32) not null,
	text varchar(1024) null,
	create_datetime timestamp not null,
	primary key (id)
);
