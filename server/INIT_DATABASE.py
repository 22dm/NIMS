#!/usr/bin/python
# -*- coding: utf-8 -*-

import sqlite3

conn = sqlite3.connect('NIMS.db')
cursor = conn.cursor()

cursor.execute(
    'create table id_tag (id varchar(9), oname varchar(60), tag varchar(60), val varchar(60))')
cursor.execute('create table org (oname varchar(60), superadmin varchar(9))')
cursor.execute('create table favor (oname varchar(60), id varchar(9))')

cursor.close()
conn.commit()
