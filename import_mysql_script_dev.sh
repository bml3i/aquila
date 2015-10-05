#!/bin/bash

mysql -h localhost -u aquila -p --default-character-set=utf8 < ./src/main/webapp/WEB-INF/sqls/mysql_init.sql
