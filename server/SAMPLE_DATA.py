#!/usr/bin/python
# -*- coding: utf-8 -*-

import sqlite3

conn = sqlite3.connect('NIMS.db')
cursor = conn.cursor()

cursor.execute(
    'insert into id_tag values (\'171840773\', \'数学分析\', \'第1次作业\',\'90\')')
cursor.execute(
    'insert into id_tag values (\'171840773\', \'数学分析\', \'第2次作业\',\'95\')')
cursor.execute(
    'insert into id_tag values (\'171840773\', \'数学分析\', \'第3次作业\',\'100\')')
cursor.execute(
    'insert into id_tag values (\'171840773\', \'数学分析\', \'第4次作业\',\'95\')')
cursor.execute(
    'insert into id_tag values (\'171840773\', \'数学分析\', \'第5次作业\',\'95\')')
cursor.execute(
    'insert into id_tag values (\'171840773\', \'数学分析\', \'期中考试\',\'90\')')
cursor.execute(
    'insert into id_tag values (\'171840773\', \'数学分析\', \'管理员\',\'1\')')

cursor.execute(
    'insert into id_tag values (\'171840773\', \'失物招领中心\', \'管理员\',\'1\')')

cursor.execute(
    'insert into id_tag values (\'171840801\', \'数学分析\', \'第1次作业\',\'80\')')
cursor.execute(
    'insert into id_tag values (\'171840801\', \'数学分析\', \'第2次作业\',\'85\')')
cursor.execute(
    'insert into id_tag values (\'171840801\', \'数学分析\', \'第3次作业\',\'80\')')
cursor.execute(
    'insert into id_tag values (\'171840801\', \'数学分析\', \'第4次作业\',\'80\')')
cursor.execute(
    'insert into id_tag values (\'171840801\', \'数学分析\', \'第5次作业\',\'80\')')
cursor.execute(
    'insert into id_tag values (\'171840801\', \'数学分析\', \'期中考试\',\'80\')')

cursor.execute(
    'insert into id_tag values (\'171840802\', \'数学分析\', \'第1次作业\',\'95\')')
cursor.execute(
    'insert into id_tag values (\'171840802\', \'数学分析\', \'第2次作业\',\'100\')')
cursor.execute(
    'insert into id_tag values (\'171840802\', \'数学分析\', \'第3次作业\',\'100\')')
cursor.execute(
    'insert into id_tag values (\'171840802\', \'数学分析\', \'第4次作业\',\'100\')')
cursor.execute(
    'insert into id_tag values (\'171840802\', \'数学分析\', \'第5次作业\',\'100\')')
cursor.execute(
    'insert into id_tag values (\'171840802\', \'数学分析\', \'期中考试\',\'100\')')

cursor.execute(
    'insert into id_tag values (\'171840803\', \'数学分析\', \'第1次作业\',\'45\')')
cursor.execute(
    'insert into id_tag values (\'171840803\', \'数学分析\', \'第2次作业\',\'50\')')
cursor.execute(
    'insert into id_tag values (\'171840803\', \'数学分析\', \'第3次作业\',\'50\')')
cursor.execute(
    'insert into id_tag values (\'171840803\', \'数学分析\', \'第4次作业\',\'40\')')
cursor.execute(
    'insert into id_tag values (\'171840803\', \'数学分析\', \'期中考试\',\'35\')')

cursor.execute(
    'insert into id_tag values (\'171840804\', \'数学分析\', \'第1次作业\',\'90\')')
cursor.execute(
    'insert into id_tag values (\'171840804\', \'数学分析\', \'第2次作业\',\'60\')')
cursor.execute(
    'insert into id_tag values (\'171840804\', \'数学分析\', \'第3次作业\',\'35\')')
cursor.execute(
    'insert into id_tag values (\'171840804\', \'数学分析\', \'第5次作业\',\'75\')')
cursor.execute(
    'insert into id_tag values (\'171840804\', \'数学分析\', \'期中考试\',\'70\')')

cursor.execute(
    'insert into id_tag values (\'171840805\', \'数学分析\', \'第1次作业\',\'85\')')
cursor.execute(
    'insert into id_tag values (\'171840805\', \'数学分析\', \'第2次作业\',\'80\')')
cursor.execute(
    'insert into id_tag values (\'171840805\', \'数学分析\', \'第3次作业\',\'90\')')
cursor.execute(
    'insert into id_tag values (\'171840805\', \'数学分析\', \'第4次作业\',\'85\')')
cursor.execute(
    'insert into id_tag values (\'171840805\', \'数学分析\', \'第5次作业\',\'90\')')
cursor.execute(
    'insert into id_tag values (\'171840805\', \'数学分析\', \'期中考试\',\'95\')')

cursor.execute(
    'insert into id_tag values (\'171840806\', \'数学分析\', \'第1次作业\',\'60\')')
cursor.execute(
    'insert into id_tag values (\'171840806\', \'数学分析\', \'第2次作业\',\'60\')')
cursor.execute(
    'insert into id_tag values (\'171840806\', \'数学分析\', \'第3次作业\',\'60\')')
cursor.execute(
    'insert into id_tag values (\'171840806\', \'数学分析\', \'第4次作业\',\'60\')')
cursor.execute(
    'insert into id_tag values (\'171840806\', \'数学分析\', \'第5次作业\',\'60\')')
cursor.execute(
    'insert into id_tag values (\'171840806\', \'数学分析\', \'期中考试\',\'60\')')

cursor.execute(
    'insert into id_tag values (\'171840773\', \'女装社\', \'剩余经费\',\'100\')')
cursor.execute(
    'insert into id_tag values (\'171840773\', \'女装社\', \'活动时长\',\'100\')')
cursor.execute(
    'insert into id_tag values (\'171840773\', \'女装社\', \'3.16晚会\',\'1\')')
cursor.execute(
    'insert into id_tag values (\'171840773\', \'女装社\', \'管理员\',\'1\')')


cursor.execute(
    'insert into id_tag values (\'171840773\', \'计算系统基础\', \'第1次作业\',\'10\')')
cursor.execute(
    'insert into id_tag values (\'171840773\', \'计算系统基础\', \'第2次作业\',\'10\')')
cursor.execute(
    'insert into id_tag values (\'171840773\', \'计算系统基础\', \'第3次作业\',\'10\')')
cursor.execute(
    'insert into id_tag values (\'171840773\', \'计算系统基础\', \'第4次作业\',\'9\')')
cursor.execute(
    'insert into id_tag values (\'171840773\', \'计算系统基础\', \'第5次作业\',\'9\')')
cursor.execute(
    'insert into id_tag values (\'171840773\', \'计算系统基础\', \'第1次上机作业\',\'10\')')

cursor.execute(
    'insert into id_tag values (\'171840773\', \'《美的历程》阅读\', \'3.15线下\',\'1\')')
cursor.execute(
    'insert into id_tag values (\'171840773\', \'《美的历程》阅读\', \'期中小论文\',\'85\')')
cursor.execute(
    'insert into id_tag values (\'171840773\', \'《美的历程》阅读\', \'管理员\',\'1\')')

cursor.execute(
    'insert into id_tag values (\'171840773\', \'环保社\', \'活动时长\',\'17\')')
cursor.execute(
    'insert into id_tag values (\'171840773\', \'环保社\', \'2019植树节\',\'1\')')
cursor.execute(
    'insert into id_tag values (\'171840773\', \'环保社\', \'发展新社员\',\'5\')')

cursor.execute('insert into org values (\'女装社\', \'171840773\')')

cursor.close()
conn.commit()
