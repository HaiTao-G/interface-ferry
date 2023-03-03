## 接口摆渡项目数据流程图

[![drawio.png](https://i.postimg.cc/QChnFfG2/drawio.png)](https://postimg.cc/MM3Y4bG5)

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
├── if-common -- 系统公共模块
└── if-consumer -- 外网接口服务消费公共模块
     ├── if-report-del-consumer -- 报删消费服务
     ├── if-screenshot-consumer -- 截图消费服务
     ├── if-again-collection-consumer -- 重新采集消费服务
└── if-demo -- starter项目引用式列
     ├── if-curator-client-demo -- zk客户端curator封装式列
     └── if-ftp-demo -- FTP连接池客户端封装式列
├── if-entity -- 系统实体类模块
└── if-file-watcher -- 文件监听公共模块
     ├── if-inside-file-watcherr -- 内网文件监听服务
     └── if-outside-file-watcherr -- 外网文件监听服务
└── if-starter -- springboot第三方自定义starter
     ├── if-curator-client-spring-boot-starter -- zk客户端curator封装starter
     └── if-ftp-spring-boot-starter -- FTP连接池客户端封装starter
```
~~~

