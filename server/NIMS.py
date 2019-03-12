#!/usr/bin/python
# -*- coding: utf-8 -*-

import json
import sqlite3
from flask import Flask, request, make_response, send_file
from urllib.parse import quote
from openpyxl import Workbook

app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False

@app.route('/auth')
def auth():
  uid = request.args.get('id')
  pwd = request.args.get('pwd')
  authed = verfy(uid, pwd)
  res = {'auth': 1 if authed else 0};
  return json.dumps(res);

def verfy(uid, pwd):
  conn = sqlite3.connect('NIMS.db')
  cursor = conn.cursor()
  cursor.execute('select pwd from id_pwd where id=\'' + uid + '\'')
  values = cursor.fetchall()
  conn.close()
  return values and pwd == values[0][0];

@app.route('/getTags')
def getTags():
  uid = request.args.get('id')
  
  conn = sqlite3.connect('NIMS.db')
  cursor = conn.cursor()
  cursor.execute('select * from id_tag where id=\'' + uid + '\'')
  tagList = cursor.fetchall()
  orgNames = list(set([tag[1] for tag in tagList]))

  cursor.execute('select * from org where admin=\'' + uid + '\'')
  orgAdmin = list(set([tag[0] for tag in cursor.fetchall()]))
  cursor.close()
  conn.close()

  res = {'tags': [{'orgName': org,
                  'admin': 1 if org in orgAdmin else 0,
                  'tag': [{tag[2]: tag[3]} for tag in tagList if tag[1] == org]}
                  for org in orgNames]}

  return json.dumps(res)

@app.route('/editTags', methods=['GET', 'POST'])
def editTags():
  edits = json.loads(request.get_data())

  oname = edits['orgName']
  tags = edits['tags']
  changes = edits['changes']

  conn = sqlite3.connect('NIMS.db')
  cursor = conn.cursor()

  for tag in tags:
    pos = tags.index(tag)
    cursor.execute('select * from id_tag where oname=\'' + oname + '\' and tag = \'' + tag + '\'')
    tagDict = {tag[0]: tag[3] for tag in cursor.fetchall()}

    for change in changes:
      value = change['value'][pos]
      if value != "":
        if change['id'] in tagDict:
          if value[0] == '+':
            value = str(int(tagDict[change['id']]) + int(value[1:]))
          elif value[0] == '-':
            value = str(int(tagDict[change['id']]) - int(value[1:]))
          cursor.execute('update id_tag set val = ' + value + ' where id = \'' + change['id'] + '\' and oname = \'' + oname + '\' and tag = \'' + tag + '\'')
        else:
          cursor.execute('insert into id_tag values (\'' + change['id'] + '\', \'' + oname + '\', \'' + tag + '\',\'' + change['value'][pos] + '\')')

  cursor.close()
  conn.close()

  return json.dumps({"status": 0})

@app.route('/getXlsx')
def getXlsx():
  oname = request.args.get('oname')

  conn = sqlite3.connect('NIMS.db')

  cursor = conn.cursor()
  cursor.execute('select * from id_tag where oname=\'' + oname + '\'')
  tagList = cursor.fetchall()
  cursor.close()

  tagNames = list(set([x[2] for x in tagList]))
  stuIds = list(set([x[0] for x in tagList]))

  workbook = Workbook()
  booksheet = workbook.active
  booksheet.append(['学号', *tagNames])

  for id in stuIds:
    booksheet.append([id])
  for tag in tagList:
    booksheet.cell(stuIds.index(tag[0]) + 2, tagNames.index(tag[2]) + 2).value = tag[3]

  fileName = oname + "_NIMS.xlsx"
  workbook.save(fileName)

  conn.close()

  response = make_response(send_file(fileName))
  response.headers["Content-Disposition"] = "attachment; filename={0}; filename*=utf-8''{0}".format(quote(fileName))
  return response

if __name__ =='__main__':
  app.run('0.0.0.0', port='2333', debug=True)
