# NIMS API 规范

## 获取用户全部 Tag

发送：

GET：```<server>/getTags?id=<userId>```

返回：

tags 列表，每一项包含 orgName（组织名称），superAdmin（是否为超级管理员），favor（是否收藏），以及学生在该组织的 tag 列表

``` json
{
  "tags": [
    {
      "orgName": "环保社",
      "superAdmin": 1,
      "favor": 0,
      "tag": [
        {"活动时长": "35"},
        {"发展新社员": "24"},
        {"积分": "300"},
        {"2019植树节活动": "1"}
      ]
    },
    {
      "orgName": "数学分析",
      "superAdmin": 1,
      "favor": 1,
      "tag": [
        {"第一次作业": "90"},
        {"第二次作业": "95"},
        {"上课提问": "3"},
        {"缺课次数": "0"}
      ]
    }
  ]
}
```

## 批量修改 Tag

发送：

POST：```<server>/editTags```

``` json
{
  "orgName": "数学分析",
  "tag": ["上课提问", "缺课次数", "第三次作业"],
  "change": [
    {
      "id": "171840773",
      "value": ["+1", "+1", "100"]
    },
    {
      "id": "171840990",
      "value": ["+1", "", "90"]
    },
    {
      "id": "171840991",
      "value": ["", "", "80"]
    }
  ]
}
```

返回：

``` json
{"status": 0}
```

## 下载 xlsx 表格

发送：

GET：```<server>/getXlsx?id=<userId>```

返回：

xlsx 文件