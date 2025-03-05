// mock的接口返回数据
export default [
  // 登陆接口
  {
    url: '/api/auth/login',
    method: 'post',
    response: {
      "code": 0,
      "message": "成功",
      "data": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdXBlckFkbWluIiwianRpIjoiNGZlMjIzY2Y1OTUyNGU0ZTk1NjlkMThkNTU5YzI3NTUiLCJwZXJtcyI6WyJzeXM6dXNlcjp0bXAiLCIxNjM1MTEwNDE2MjYzMjYyMjA5IiwiMTU3MTEzMTA0MzUzMjM0MTI0OSIsIjE1NDg5MDExMTE5OTk3NzM0NDAiLCIxNTQ4OTAxMTExOTk5NzcwOTI2IiwiMTgxMzk1OTY1ODAxMzQzMzg1OCIsIjE1NDg5MDExMTE5OTk3NzM0NDEiLCIxNTQ4OTAxMTExOTk5NzczNDQyIiwiMTU0ODkwMTExMTk5OTc3MzQ0MyIsIjE1NDg5MDExMTE5OTk3NzM0NDQiLCIxNTQ4OTAxMTExOTk5NzcwNTI2IiwiMTU0ODkwMTExMTk5OTc3MzQ0NSIsIjE2MjMzNzg5OTYwOTk0MTE5NjkiLCIxNTQ4OTAxMTExOTk5NzczMzI2IiwiMTU0ODkwMTExMTk5OTc3MTIyNiIsIjE1NDg5MDExMTE5OTk3NzI0MjYiLCIxNTQ4OTAxMTExOTk5NzczNDM4IiwiMTU0ODkwMTExMTk5OTc3MzQzOSIsIjE1NzExMzIzOTM5OTMzNzE2NDkiLCIxNTQ4OTAxMTExOTk5NzcxMDI2IiwiMTU0ODkwMTExMTk5OTc3MjYyNiIsIjY3OTY2NjU2NjQwZTcxOGY3MGEzMjQ1OCIsIjE1NDg5MDExMTE5OTk3NzI4MjYiLCIxNTcxMTMyMDc2ODUzNjU3NjAxIiwiMTgxMTMyODQwMjk4OTY2MjIxMCIsInN5c19vcmdfYWRkIiwiMTgxMTI5MDkzNzUwMzM4NzY1MCIsIjE1NzExMzA3NTYxNTU0MDgzODYiLCIxNTQ4OTAxMTExOTk5NzczNDMwIiwiMTU3MTEzMDgxMTA1ODg0Nzc0NSIsImluZGV4IiwiMTU0ODkwMTExMTk5OTc3MzQzMSIsInN5c19vcmdfZWRpdCIsIjE1NzExMzE0MjczNjE0ODg4OTgiLCIxNTQ4OTAxMTExOTk5NzczNDMyIiwiMTU0ODkwMTExMTk5OTc3MTcyNiIsIjE1NDg5MDExMTE5OTk3NzM0MzMiLCIxNTQ4OTAxMTExOTk5NzcyMDI2IiwiMTU0ODkwMTExMTk5OTc3MzQzNCIsIjE1NDg5MDExMTE5OTk3NzM0MzUiLCJEbFhnUzNVcHlUIiwiMTU0ODkwMTExMTk5OTc3MjIyNiIsIjE1NDg5MDExMTE5OTk3NzM0MzYiLCIxNTQ4OTAxMTExOTk5NzcxMzI2IiwiMTU0ODkwMTExMTk5OTc3MzQzNyIsIjE1NDg5MDExMTE5OTk3NzM0MjciLCIxNjg5ODkyMjAyNjc5Mzc3OTIxIiwiMTU0ODkwMTExMTk5OTc3MzQyOCIsInN5czpvcmc6bGlzdDMiLCIxODExMzMwMzU5Njk1NDAwOTYxIiwiMTU0ODkwMTExMTk5OTc3MzQyOSIsIjE2MzUxMTA1MzY0NTEwNDMzMjkiLCIxNTcxMTMxNzI3NjU2ODc4MDgxIiwiMTU3MTEzMjY1ODg1MTA4NjMzOCIsIjE1NDg5MDExMTE5OTk3NzE5MjYiLCIxODExMjkwOTM3NDg2NjEwNDM0IiwiMTgxMTgwMDU4NDQyNTUwMDY3NCIsIjE1NzExMzI1NzYxNDM2MDU3NjEiLCIxNjg5ODkxNDA1MzY3MzUzMzQ2Iiwic3lzX29yZ19hZGQyIiwiMTU0ODkwMTExMTk5OTc3Mzk4MCIsIjE4MTEyOTA5Mzc0NDQ2NjczOTMiLCIxNTQ4OTAxMTExOTk5NzcwODI2IiwiMTU0ODkwMTExMTk5OTc3MzIyNiIsIjE1NDg5MDExMTE5OTk3NzM5NzgiLCIxNTQ4OTAxMTExOTk5NzcyNTI2IiwiMTU0ODkwMTExMTk5OTc3Mzk3OSIsIjE1NDg5MDExMTE5OTk3NzI3MjYiLCIxNTQ4OTAxMTExOTk5NzcyOTI2IiwiMTU3MTEzMDk3MzI5NDUyNjQ2NSIsIjE4MTEyOTA5Mzc0Njk4MzMyMTciLCJzeXNfdXNyX3RtcF9saXN0IiwiMTgxMTI5MDkzNzQ5NDk5OTA0MiIsIjE2ODk4OTQzMTY1NTQwNjc5NjkiLCJzeXMtb3JnLWxpc3QiLCJzeXNfb3JnX2RlbGV0ZSIsIjE1NDg5MDExMTE5OTk3NzMyNTQiLCIxNTQ4OTAxMTExOTk5NzcxODI2IiwiMTU0ODkwMTExMTk5OTc3MTQyNiIsIjE1NDg5MDExMTE5OTk3NzIzMjYiLCIxNTcxMTMyNDY4MTc4MDI2NDk3IiwiMTU3MTEzMTU0NDk3Mzk2NzM2MSIsIjE1NzExMzExMzcwMDY2MDAxOTMiLCIxNjg5ODk0NTc3MjM4NDUwMTc3IiwiMTYyMzM3ODY3NTU5MTY3MTgxMCIsIjE1NzExMjk5Mjk5NjE0MDY0NjYiLCI2N2E1NmQxY2QxOTA1OWQxY2M3Mzc1OGUiLCIxNTcxMTI5NTI5NTY0NzU4MDE3Il0sInJvbGVzIjpbIjY3NWY5MDNiZDE5MDFiMzI2YTIyODg2NSIsInN1cGVyYWRtaW4iLCJhZG1pbiIsIndlaXlpY29kZSJdfQ.6GXvYA2ec-7c9RlSGwQI6ZkooQA4hTTzLNiCs-FQrDY",
    }
  },
  // 用户信息
  {
    url: '/api/sys/userCenter/userInfo',
    method: 'post',
    response: {
      "code": 0,
      "message": "成功",
      "data": {
        "account": "superAdmin",
        "nickName": null,
        "avatar": null,
        "name": "superAdmin",
        "roles": [
          "675f903bd1901b326a228865",
          "superadmin",
          "admin",
          "weiyicode"
        ],
        "perms": [
          "sys:user:tmp",
          "1635110416263262209",
          "1571131043532341249",
          "1548901111999773440",
          "1548901111999770926",
          "1813959658013433858",
          "1548901111999773441",
          "1548901111999773442",
          "1548901111999773443",
          "1548901111999773444",
          "1548901111999770526",
          "1548901111999773445",
          "1623378996099411969",
          "1548901111999773326",
          "1548901111999771226",
          "1548901111999772426",
          "1548901111999773438",
          "1548901111999773439",
          "1571132393993371649",
          "1548901111999771026",
          "1548901111999772626",
          "67966656640e718f70a32458",
          "1548901111999772826",
          "1571132076853657601",
          "1811328402989662210",
          "sys_org_add",
          "1811290937503387650",
          "1571130756155408386",
          "1548901111999773430",
          "1571130811058847745",
          "index",
          "1548901111999773431",
          "sys_org_edit",
          "1571131427361488898",
          "1548901111999773432",
          "1548901111999771726",
          "1548901111999773433",
          "1548901111999772026",
          "1548901111999773434",
          "1548901111999773435",
          "DlXgS3UpyT",
          "1548901111999772226",
          "1548901111999773436",
          "1548901111999771326",
          "1548901111999773437",
          "1548901111999773427",
          "1689892202679377921",
          "1548901111999773428",
          "sys:org:list3",
          "1811330359695400961",
          "1548901111999773429",
          "1635110536451043329",
          "1571131727656878081",
          "1571132658851086338",
          "1548901111999771926",
          "1811290937486610434",
          "1811800584425500674",
          "1571132576143605761",
          "1689891405367353346",
          "sys_org_add2",
          "1548901111999773980",
          "1811290937444667393",
          "1548901111999770826",
          "1548901111999773226",
          "1548901111999773978",
          "1548901111999772526",
          "1548901111999773979",
          "1548901111999772726",
          "1548901111999772926",
          "1571130973294526465",
          "1811290937469833217",
          "sys_usr_tmp_list",
          "1811290937494999042",
          "1689894316554067969",
          "sys-org-list",
          "sys_org_delete",
          "1548901111999773254",
          "1548901111999771826",
          "1548901111999771426",
          "1548901111999772326",
          "1571132468178026497",
          "1571131544973967361",
          "1571131137006600193",
          "1689894577238450177",
          "1623378675591671810",
          "1571129929961406466",
          "67a56d1cd19059d1cc73758e",
          "1571129529564758017"
        ]
      }
    },
  },
  // 用户菜单
  {
    url: '/api/sys/userCenter/userMenu',
    method: 'post',
    response:{
      "code": 0,
      "message": "成功",
      "data": [
        {
          "code": "sys_module",
          "parentCode": "0",
          "weight": 1,
          "name": "系统功能",
          "redirect": null,
          "path": "/HaRJlWnPFs",
          "component": "",
          "meta": {
            "icon": "appstore-add-outlined",
            "title": "系统功能Mock",
            "type": 1
          },
          "children": [
            {
              "code": "1548901111999770526",
              "parentCode": "sys_module",
              "weight": 2,
              "name": "系统首页",
              "path": "/home",
              "component": "index/index",
              "meta": {
                "icon": "home-outlined",
                "title": "系统首页",
                "type": 3
              }
            },
            {
              "code": "1548901111999770726",
              "parentCode": "sys_module",
              "weight": 4,
              "name": "组织架构",
              "redirect": null,
              "path": "/org",
              "component": "Layout",
              "meta": {
                "icon": "apartment-outlined",
                "title": "组织架构",
                "type": 2
              },
              "children": [
                {
                  "code": "1548901111999770826",
                  "parentCode": "1548901111999770726",
                  "weight": 5,
                  "name": "组织管理",
                  "path": "/sys/org",
                  "component": "sys/org/index",
                  "meta": {
                    "icon": "cluster-outlined",
                    "title": "组织管理",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999770926",
                  "parentCode": "1548901111999770726",
                  "weight": 6,
                  "name": "用户管理",
                  "path": "/sys/user",
                  "component": "sys/user/index",
                  "meta": {
                    "icon": "user-outlined",
                    "title": "用户管理",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999771026",
                  "parentCode": "1548901111999770726",
                  "weight": 7,
                  "name": "分组管理",
                  "path": "/sys/group",
                  "component": "sys/group/index",
                  "meta": {
                    "icon": "apartment-outlined",
                    "title": "分组管理",
                    "type": 3
                  }
                }
              ]
            },
            {
              "code": "1548901111999771126",
              "parentCode": "sys_module",
              "weight": 8,
              "name": "权限管控",
              "redirect": null,
              "path": "/perm",
              "component": "Layout",
              "meta": {
                "icon": "user-switch-outlined",
                "title": "权限管控",
                "type": 2
              },
              "children": [
                {
                  "code": "1548901111999771226",
                  "parentCode": "1548901111999771126",
                  "weight": 9,
                  "name": "角色管理",
                  "path": "/sys/role",
                  "component": "sys/role/index",
                  "meta": {
                    "icon": "deployment-unit-outlined",
                    "title": "角色管理",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999771326",
                  "parentCode": "1548901111999771126",
                  "weight": 10,
                  "name": "模块管理",
                  "path": "/sys/module",
                  "component": "sys/resource/module/index",
                  "meta": {
                    "icon": "appstore-add-outlined",
                    "title": "模块管理",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999771426",
                  "parentCode": "1548901111999771126",
                  "weight": 11,
                  "name": "菜单管理",
                  "path": "/sys/menu",
                  "component": "sys/resource/menu/index",
                  "meta": {
                    "icon": "pic-left-outlined",
                    "title": "菜单管理",
                    "type": 3
                  }
                }
              ]
            },
            {
              "code": "1548901111999771626",
              "parentCode": "sys_module",
              "weight": 13,
              "name": "基础工具",
              "redirect": null,
              "path": "/tool",
              "component": "Layout",
              "meta": {
                "icon": "tool-outlined",
                "title": "基础工具",
                "type": 2
              },
              "children": [
                {
                  "code": "679665c9640e718f70a32457",
                  "parentCode": "1548901111999771626",
                  "weight": 8,
                  "name": "目录1",
                  "redirect": null,
                  "path": "/mulu1",
                  "component": "Layout",
                  "meta": {
                    "icon": "vertical-align-middle-outlined",
                    "title": "目录1",
                    "type": 2
                  },
                  "children": [
                    {
                      "code": "67966656640e718f70a32458",
                      "parentCode": "679665c9640e718f70a32457",
                      "weight": 4,
                      "name": "文件管理1",
                      "path": "/dev/file/index1",
                      "component": "dev/file/index",
                      "meta": {
                        "icon": "border-outer-outlined",
                        "title": "文件管理1",
                        "type": 3
                      }
                    },
                    {
                      "code": "67a56d1cd19059d1cc73758e",
                      "parentCode": "679665c9640e718f70a32457",
                      "weight": 10,
                      "name": "文件上传2",
                      "path": "/dev/file/index",
                      "component": "dev/file/index",
                      "meta": {
                        "icon": "",
                        "title": "文件上传2",
                        "type": 3
                      }
                    }
                  ]
                },
                {
                  "code": "1548901111999771726",
                  "parentCode": "1548901111999771626",
                  "weight": 14,
                  "name": "文件管理",
                  "path": "/dev/file/index",
                  "component": "dev/file/index",
                  "meta": {
                    "icon": "copy-outlined",
                    "title": "文件管理",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999771826",
                  "parentCode": "1548901111999771626",
                  "weight": 15,
                  "name": "邮件推送",
                  "path": "/dev/email/index",
                  "component": "dev/email/index",
                  "meta": {
                    "icon": "send-outlined",
                    "title": "邮件推送",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999771926",
                  "parentCode": "1548901111999771626",
                  "weight": 16,
                  "name": "短信发送",
                  "path": "/dev/sms/index",
                  "component": "dev/sms/index",
                  "meta": {
                    "icon": "mail-outlined",
                    "title": "短信发送",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999772026",
                  "parentCode": "1548901111999771626",
                  "weight": 17,
                  "name": "站内信息",
                  "path": "/dev/message/index",
                  "component": "dev/message/index",
                  "meta": {
                    "icon": "message-outlined",
                    "title": "站内信息",
                    "type": 3
                  }
                }
              ]
            },
            {
              "code": "1548901111999772126",
              "parentCode": "sys_module",
              "weight": 18,
              "name": "系统运维",
              "redirect": null,
              "path": "/ops",
              "component": "Layout",
              "meta": {
                "icon": "hdd-outlined",
                "title": "系统运维",
                "type": 2
              },
              "children": [
                {
                  "code": "1548901111999772226",
                  "parentCode": "1548901111999772126",
                  "weight": 19,
                  "name": "三方用户",
                  "path": "/auth/third",
                  "component": "auth/third/index",
                  "meta": {
                    "icon": "team-outlined",
                    "title": "三方用户",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999772326",
                  "parentCode": "1548901111999772126",
                  "weight": 20,
                  "name": "数据字典",
                  "path": "/sys/dict",
                  "component": "dev/dict/index",
                  "meta": {
                    "icon": "file-search-outlined",
                    "title": "数据字典",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999772426",
                  "parentCode": "1548901111999772126",
                  "weight": 21,
                  "name": "系统配置",
                  "path": "/sys/config",
                  "component": "dev/config/index",
                  "meta": {
                    "icon": "setting-outlined",
                    "title": "系统配置",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999772526",
                  "parentCode": "1548901111999772126",
                  "weight": 22,
                  "name": "任务调度",
                  "path": "/dev/job",
                  "component": "dev/job/index",
                  "meta": {
                    "icon": "field-time-outlined",
                    "title": "任务调度",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999772626",
                  "parentCode": "1548901111999772126",
                  "weight": 23,
                  "name": "会话管理",
                  "path": "/auth/monitor",
                  "component": "auth/monitor/index",
                  "meta": {
                    "icon": "usergroup-delete-outlined",
                    "title": "会话管理",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999772726",
                  "parentCode": "1548901111999772126",
                  "weight": 24,
                  "name": "系统监控",
                  "path": "/dev/monitor",
                  "component": "dev/monitor/index",
                  "meta": {
                    "icon": "database-outlined",
                    "title": "系统监控",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999772826",
                  "parentCode": "1548901111999772126",
                  "weight": 25,
                  "name": "连接监控",
                  "path": "http://localhost:82/druid/index.html",
                  "component": "",
                  "meta": {
                    "icon": "console-sql-outlined",
                    "title": "连接监控",
                    "type": 5,
                    "url": "http://localhost:82/druid/index.html"
                  }
                },
                {
                  "code": "1548901111999772926",
                  "parentCode": "1548901111999772126",
                  "weight": 26,
                  "name": "接口文档",
                  "path": "http://localhost:82/doc.html",
                  "component": "",
                  "meta": {
                    "icon": "file-word-outlined",
                    "title": "接口文档",
                    "type": 5,
                    "url": "http://localhost:82/doc.html"
                  }
                },
                {
                  "code": "1548901111999773126",
                  "parentCode": "1548901111999772126",
                  "weight": 27,
                  "name": "日志审计",
                  "redirect": null,
                  "path": "/x1vjuegii4",
                  "component": null,
                  "meta": {
                    "icon": "robot-outlined",
                    "title": "日志审计",
                    "type": 2
                  },
                  "children": [
                    {
                      "code": "1548901111999773226",
                      "parentCode": "1548901111999773126",
                      "weight": 28,
                      "name": "访问日志",
                      "path": "/dev/vislog",
                      "component": "dev/log/vislog/index",
                      "meta": {
                        "icon": "bars-outlined",
                        "title": "访问日志",
                        "type": 3
                      }
                    },
                    {
                      "code": "1548901111999773326",
                      "parentCode": "1548901111999773126",
                      "weight": 29,
                      "name": "操作日志",
                      "path": "/dev/oplog",
                      "component": "dev/log/oplog/index",
                      "meta": {
                        "icon": "bars-outlined",
                        "title": "操作日志",
                        "type": 3
                      }
                    }
                  ]
                },
                {
                  "code": "1811800584425500674",
                  "parentCode": "1548901111999772126",
                  "weight": 99,
                  "name": "轮播图",
                  "path": "/dev/slideshow",
                  "component": "dev/slideshow/index",
                  "meta": {
                    "icon": "file-image-outlined",
                    "title": "轮播图",
                    "type": 3
                  }
                }
              ]
            },
            {
              "code": "1548901111999773426",
              "parentCode": "sys_module",
              "weight": 30,
              "name": "开发示例",
              "redirect": null,
              "path": "/exm",
              "component": "Layout",
              "meta": {
                "icon": "project-outlined",
                "title": "开发示例",
                "type": 2
              },
              "children": [
                {
                  "code": "1548901111999773427",
                  "parentCode": "1548901111999773426",
                  "weight": 31,
                  "name": "图标选择",
                  "path": "/iconSelect",
                  "component": "exm/iconSelect/index",
                  "meta": {
                    "icon": "appstore-outlined",
                    "title": "图标选择",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999773428",
                  "parentCode": "1548901111999773426",
                  "weight": 32,
                  "name": "ECK线图",
                  "path": "/chart/eCKXianTu",
                  "component": "exm/chart/eCKXianTu",
                  "meta": {
                    "icon": "appstore-outlined",
                    "title": "ECK线图",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999773429",
                  "parentCode": "1548901111999773426",
                  "weight": 33,
                  "name": "EC仪表图",
                  "path": "/chart/eCYiBiaoTu",
                  "component": "exm/chart/eCYiBiaoTu",
                  "meta": {
                    "icon": "appstore-outlined",
                    "title": "EC仪表图",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999773430",
                  "parentCode": "1548901111999773426",
                  "weight": 34,
                  "name": "EC散点图",
                  "path": "/chart/eCSanDianTu",
                  "component": "exm/chart/eCSanDianTu",
                  "meta": {
                    "icon": "appstore-outlined",
                    "title": "EC散点图",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999773431",
                  "parentCode": "1548901111999773426",
                  "weight": 35,
                  "name": "EC柱状图",
                  "path": "/chart/eCZhuZhuangTu",
                  "component": "exm/chart/eCZhuZhuangTu",
                  "meta": {
                    "icon": "appstore-outlined",
                    "title": "EC柱状图",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999773432",
                  "parentCode": "1548901111999773426",
                  "weight": 36,
                  "name": "EC树形图",
                  "path": "/chart/eCShuXingTu",
                  "component": "exm/chart/eCShuXingTu",
                  "meta": {
                    "icon": "appstore-outlined",
                    "title": "EC树形图",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999773433",
                  "parentCode": "1548901111999773426",
                  "weight": 37,
                  "name": "EC漏斗图",
                  "path": "/chart/eCLouDouTu",
                  "component": "exm/chart/eCLouDouTu",
                  "meta": {
                    "icon": "appstore-outlined",
                    "title": "EC漏斗图",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999773434",
                  "parentCode": "1548901111999773426",
                  "weight": 38,
                  "name": "EC线形图",
                  "path": "/chart/eCXianXingTu",
                  "component": "exm/chart/eCXianXingTu",
                  "meta": {
                    "icon": "appstore-outlined",
                    "title": "EC线形图",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999773435",
                  "parentCode": "1548901111999773426",
                  "weight": 39,
                  "name": "EC饼状图",
                  "path": "/chart/eCBingZhuangTu",
                  "component": "exm/chart/eCBingZhuangTu",
                  "meta": {
                    "icon": "appstore-outlined",
                    "title": "EC饼状图",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999773436",
                  "parentCode": "1548901111999773426",
                  "weight": 40,
                  "name": "G2进度图",
                  "path": "/chart/g2JinDuTu",
                  "component": "exm/chart/g2JinDuTu",
                  "meta": {
                    "icon": "appstore-outlined",
                    "title": "G2进度图",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999773437",
                  "parentCode": "1548901111999773426",
                  "weight": 41,
                  "name": "G2子弹图",
                  "path": "/chart/g2ZiDanTu",
                  "component": "exm/chart/g2ZiDanTu",
                  "meta": {
                    "icon": "appstore-outlined",
                    "title": "G2子弹图",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999773438",
                  "parentCode": "1548901111999773426",
                  "weight": 42,
                  "name": "G2散点图",
                  "path": "/chart/g2SanDianTu",
                  "component": "exm/chart/g2SanDianTu",
                  "meta": {
                    "icon": "appstore-outlined",
                    "title": "G2散点图",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999773439",
                  "parentCode": "1548901111999773426",
                  "weight": 43,
                  "name": "G2柱状图",
                  "path": "/chart/g2ZhuZhuangTu",
                  "component": "exm/chart/g2ZhuZhuangTu",
                  "meta": {
                    "icon": "appstore-outlined",
                    "title": "G2柱状图",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999773440",
                  "parentCode": "1548901111999773426",
                  "weight": 44,
                  "name": "G2漏斗图",
                  "path": "/chart/g2LouDouTu",
                  "component": "exm/chart/g2LouDouTu",
                  "meta": {
                    "icon": "appstore-outlined",
                    "title": "G2漏斗图",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999773441",
                  "parentCode": "1548901111999773426",
                  "weight": 45,
                  "name": "G2折线图",
                  "path": "/chart/g2ZheXianTu",
                  "component": "exm/chart/g2ZheXianTu",
                  "meta": {
                    "icon": "appstore-outlined",
                    "title": "G2折线图",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999773442",
                  "parentCode": "1548901111999773426",
                  "weight": 46,
                  "name": "G2词云图",
                  "path": "/chart/g2CiYunTu",
                  "component": "exm/chart/g2CiYunTu",
                  "meta": {
                    "icon": "appstore-outlined",
                    "title": "G2词云图",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999773443",
                  "parentCode": "1548901111999773426",
                  "weight": 47,
                  "name": "G2面积图",
                  "path": "/chart/g2MianJiTu",
                  "component": "exm/chart/g2MianJiTu",
                  "meta": {
                    "icon": "appstore-outlined",
                    "title": "G2面积图",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999773444",
                  "parentCode": "1548901111999773426",
                  "weight": 48,
                  "name": "G2饼状图",
                  "path": "/chart/g2BingZhuangTu",
                  "component": "exm/chart/g2BingZhuangTu",
                  "meta": {
                    "icon": "appstore-outlined",
                    "title": "G2饼状图",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999773445",
                  "parentCode": "1548901111999773426",
                  "weight": 49,
                  "name": "G2条形图",
                  "path": "/chart/g2TiaoXingTu",
                  "component": "exm/chart/g2TiaoXingTu",
                  "meta": {
                    "icon": "appstore-outlined",
                    "title": "G2条形图",
                    "type": 3
                  }
                },
                {
                  "code": "1689894316554067969",
                  "parentCode": "1548901111999773426",
                  "weight": 99,
                  "name": "高德地图",
                  "path": "/map/gaodeMap",
                  "component": "exm/map/gaodeMap",
                  "meta": {
                    "icon": "environment-outlined",
                    "title": "高德地图",
                    "type": 3
                  }
                },
                {
                  "code": "1689894577238450177",
                  "parentCode": "1548901111999773426",
                  "weight": 99,
                  "name": "百度地图",
                  "path": "/map/baiduMap",
                  "component": "exm/map/baiduMap",
                  "meta": {
                    "icon": "compass-outlined",
                    "title": "百度地图",
                    "type": 3
                  }
                }
              ]
            },
            {
              "code": "1548901111999773250",
              "parentCode": "sys_module",
              "weight": 41,
              "name": "在线开发",
              "redirect": null,
              "path": "/dev",
              "component": "Layout",
              "meta": {
                "icon": "project-outlined",
                "title": "在线开发",
                "type": 2
              },
              "children": [
                {
                  "code": "1548901111999773254",
                  "parentCode": "1548901111999773250",
                  "weight": 45,
                  "name": "代码生成",
                  "path": "gen",
                  "component": "gen/index",
                  "meta": {
                    "icon": "rocket-outlined",
                    "title": "代码生成",
                    "type": 3
                  }
                }
              ]
            },
            {
              "code": "1623378345382506498",
              "parentCode": "sys_module",
              "weight": 50,
              "name": "移动端管理",
              "redirect": null,
              "path": "/mobile",
              "component": "Layout",
              "meta": {
                "icon": "mobile-outlined",
                "title": "移动端管理",
                "type": 2
              },
              "children": [
                {
                  "code": "1623378675591671810",
                  "parentCode": "1623378345382506498",
                  "weight": 51,
                  "name": "模块管理",
                  "path": "/mobile/module/index",
                  "component": "mobile/resource/module/index",
                  "meta": {
                    "icon": "build-outlined",
                    "title": "模块管理",
                    "type": 3
                  }
                },
                {
                  "code": "1623378996099411969",
                  "parentCode": "1623378345382506498",
                  "weight": 52,
                  "name": "菜单管理",
                  "path": "/mobile/menu/index",
                  "component": "mobile/resource/menu/index",
                  "meta": {
                    "icon": "appstore-add-outlined",
                    "title": "菜单管理",
                    "type": 3
                  }
                }
              ]
            }
          ]
        },
        {
          "code": "biz_module",
          "parentCode": "0",
          "weight": 2,
          "name": "业务功能",
          "redirect": null,
          "path": "/EBt14CDVoL",
          "component": "",
          "meta": {
            "icon": "profile-outlined",
            "title": "业务功能",
            "type": 1
          },
          "children": [
            {
              "code": "1813959658013433858",
              "parentCode": "biz_module",
              "weight": 0,
              "name": "业务首页",
              "path": "/biz/index",
              "component": "biz/index/index",
              "meta": {
                "icon": "home-outlined",
                "title": "业务首页",
                "type": 3
              }
            },
            {
              "code": "1689891405367353346",
              "parentCode": "biz_module",
              "weight": 12,
              "name": "业务字典",
              "path": "/biz/dict/index",
              "component": "biz/dict/index",
              "meta": {
                "icon": "read-outlined",
                "title": "业务字典",
                "type": 3
              }
            },
            {
              "code": "1548901111999773977",
              "parentCode": "biz_module",
              "weight": 51,
              "name": "公司架构",
              "redirect": null,
              "path": "/1nlpdpnief",
              "component": null,
              "meta": {
                "icon": "cluster-outlined",
                "title": "公司架构",
                "type": 2
              },
              "children": [
                {
                  "code": "1548901111999773978",
                  "parentCode": "1548901111999773977",
                  "weight": 52,
                  "name": "机构管理",
                  "path": "/biz/org",
                  "component": "biz/org/index",
                  "meta": {
                    "icon": "cluster-outlined",
                    "title": "机构管理",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999773979",
                  "parentCode": "1548901111999773977",
                  "weight": 53,
                  "name": "人员管理",
                  "path": "/biz/user",
                  "component": "biz/user/index",
                  "meta": {
                    "icon": "user-outlined",
                    "title": "人员管理",
                    "type": 3
                  }
                },
                {
                  "code": "1548901111999773980",
                  "parentCode": "1548901111999773977",
                  "weight": 54,
                  "name": "岗位管理",
                  "path": "/biz/position",
                  "component": "biz/position/index",
                  "meta": {
                    "icon": "apartment-outlined",
                    "title": "岗位管理",
                    "type": 3
                  }
                }
              ]
            },
            {
              "code": "1811290937444667393",
              "parentCode": "biz_module",
              "weight": 99,
              "name": "通知公告",
              "path": "/biz/notice",
              "component": "biz/notice/index",
              "meta": {
                "icon": "appstore-outlined",
                "title": "通知公告",
                "type": 3
              }
            }
          ]
        }
      ]
    }
  },
];