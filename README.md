## 接口摆渡项目数据流程图

![橙讯图片_1661306488652](C:\Users\Administrator\Downloads\橙讯图片_1661306488652.png)

## 核心依赖

|  spring.boot  | 2.5.0  |
| :-----------: | :----: |
|    knife4j    | 2.0.9  |
|   zookeeper   | 3.8.0  |
|    curator    | 5.1.0  |
|   fastjson    | 2.0.11 |
| selenium-java | 4.4.0  |
|     mysql     | 8.0.19 |
|    hutool     | 5.8.5  |

## 模板说明

~~~lua
```lua
interface-ferry  -- http://10.4.7.50/guoht/interface-ferry

interface-ferry
├── common -- 系统公共模块
└── consumer -- 外网接口服务消费公共模块
     ├── report-del-consumer -- 报删消费服务
     ├── screenshot-consumer -- 截图消费服务
     ├── again-collection-consumer -- 重新采集消费服务
└── demo -- starter项目引用式列
     ├── curator-client-demo -- zk客户端curator封装式列
     └── ftp-demo -- FTP连接池客户端封装式列
├── entity -- 系统实体类模块
└── file-watcher -- 文件监听公共模块
     ├── inside-file-watcherr -- 内网文件监听服务
     └── outside-file-watcherr -- 外网文件监听服务
└── starter -- springboot第三方自定义starter
     ├── curator-client-spring-boot-starter -- zk客户端curator封装starter
     └── ftp-spring-boot-starter -- FTP连接池客户端封装starter
```
~~~

