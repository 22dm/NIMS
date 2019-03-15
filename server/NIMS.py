#!/usr/bin/python
# -*- coding: utf-8 -*-

import json
import sqlite3
from urllib.parse import quote, unquote

from flask import Flask, make_response, request, send_file
from openpyxl import Workbook

app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False


@app.route('/getTags')
def getTags():
    uid = request.args.get('id')

    conn = sqlite3.connect('NIMS.db')
    cursor = conn.cursor()
    cursor.execute('select * from id_tag where id=\'' + uid + '\' order by tag')
    personTagList = cursor.fetchall()
    orgNames = list(set([tag[1] for tag in personTagList]))

    cursor.execute('select * from id_tag where id=\'@\' order by tag')
    orgTagList = cursor.fetchall()

    cursor.execute('select * from id_tag where id=\'*\' order by tag')
    schoolBoardTagList = cursor.fetchall()

    personTagList.extend(orgTagList)
    personTagList.extend(schoolBoardTagList)

    schoolBoardOrgNames = list(set([tag[1] for tag in schoolBoardTagList]) - set(orgNames))

    cursor.execute('select * from org where superadmin=\'' + uid + '\'')
    orgSuperAdmin = list(set([tag[0] for tag in cursor.fetchall()]))

    cursor.execute('select * from favor where id=\'' + uid + '\'')
    orgFavor = list(set([favor[0] for favor in cursor.fetchall()]))
    cursor.close()
    conn.close()

    res = {'tags': [{'orgName': org,
                     'superAdmin': 1 if org in orgSuperAdmin else 0,
                     'favor': 1 if org in orgFavor else 0,
                     'tag': [{tag[2]: tag[3]} for tag in personTagList if tag[1] == org]}
                    for org in orgNames] +
                    [{'orgName': org,
                    'superAdmin': 1 if org in orgSuperAdmin else 0,
                    'favor': 1 if org in orgFavor else 0,
                    'tag': [{tag[2]: tag[3]} for tag in schoolBoardTagList if tag[1] == org]}
                    for org in schoolBoardOrgNames]}

    return json.dumps(res)


@app.route('/editTags', methods=['GET', 'POST'])
def editTags():
    edits = json.loads(unquote(str(request.get_data())[2:-1]))

    oname = edits['orgName']
    tags = edits['tags']
    changes = edits['change']

    conn = sqlite3.connect('NIMS.db')
    cursor = conn.cursor()

    for tag in tags:
        pos = tags.index(tag)
        cursor.execute('select * from id_tag where oname=\'' +
                       oname + '\' and tag = \'' + tag + '\'')
        tagDict = {tag[0]: tag[3] for tag in cursor.fetchall()}

        for change in changes:
            value = change['value'][pos]
            if value != "":
                if change['id'] in tagDict:
                    if value[0] == '+':
                        value = str(
                            int(tagDict[change['id']]) + int(value[1:]))
                    elif value[0] == '-':
                        value = str(
                            int(tagDict[change['id']]) - int(value[1:]))
                    elif value == "del":
                        cursor.execute('delete from id_tag where id = \'' +
                                   change['id'] + '\' and oname = \'' + oname + '\' and tag = \'' + tag + '\'')
                        continue;
                    cursor.execute('update id_tag set val = ' + value + ' where id = \'' +
                                   change['id'] + '\' and oname = \'' + oname + '\' and tag = \'' + tag + '\'')
                else:
                    value = str(int(value))
                    cursor.execute('insert into id_tag values (\'' +
                                   change['id'] + '\', \'' + oname + '\', \'' + tag + '\',\'' + value + '\')')

    cursor.close()
    conn.commit()

    return json.dumps({"status": 0})


@app.route('/getXlsx')
def getXlsx():
    oname = request.args.get('oname')

    conn = sqlite3.connect('NIMS.db')

    cursor = conn.cursor()
    cursor.execute('select * from id_tag where oname=\'' + oname + '\'')
    tagList = cursor.fetchall()
    cursor.close()

    tagNames = sorted(list(set([x[2] for x in tagList])))
    stuIds = sorted(list(set([x[0] for x in tagList])))

    workbook = Workbook()
    booksheet = workbook.active
    booksheet.append(['学号', *tagNames])

    for id in stuIds:
        booksheet.append([id])
    for tag in tagList:
        booksheet.cell(stuIds.index(tag[0]) + 2,
                       tagNames.index(tag[2]) + 2).value = tag[3]

    fileName = oname + "_NIMS.xlsx"
    workbook.save(fileName)

    conn.close()

    response = make_response(send_file(fileName))
    response.headers["Content-Disposition"] = "attachment; filename={0}; filename*=utf-8''{0}".format(
        quote(fileName))
    return response


@app.route('/favor', methods=['GET', 'POST'])
def favor():
    favorData = json.loads(unquote(str(request.get_data())[2:-1]))

    uid = favorData['id']
    oname = favorData['oname']
    res = {}

    conn = sqlite3.connect('NIMS.db')
    cursor = conn.cursor()

    cursor.execute('select * from favor where oname=\'' +
                   oname + '\' and id = \'' + uid + '\'')
    favorList = cursor.fetchall()

    if not favorList:
        cursor.execute('insert into favor values (\'' +
                       oname + '\', \'' + uid + '\')')
        res["status"] = 1
    else:
        cursor.execute('delete from favor where oname=\'' +
                       oname + '\' and id = \'' + uid + '\'')
        res["status"] = 0

    cursor.close()
    conn.commit()

    return json.dumps(res)


if __name__ == '__main__':
    app.run('0.0.0.0', port='2333', debug=True)
