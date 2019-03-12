import sqlite3

conn = sqlite3.connect('NIMS.db')

cursor = conn.cursor()
cursor.execute('create table id_pwd (id varchar(9) primary key, pwd varchar(16))')
cursor.execute('create table id_tag (id varchar(9), oname varchar(60), tag varchar(60), val varchar(10))')
cursor.execute('create table org (oname varchar(60), admin varchar(9))')
cursor.close()
conn.commit()
