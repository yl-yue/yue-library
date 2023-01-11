# 介绍
基于yue-library的简单SpringBoot示例项目模版，提供基本的CRUD与基础特性使用示例，让初学者更好更快的上手开发。

在属性模板项目之后，可查看官网文档或yue-library-test模块，尝试使用更多yue-library提供的增强特性。

## 启动项目
流程：下载项目 ➡ 导入IDE ➡ 初始化数据库 ➡ 运行项目 ➡ 调用接口测试

`resources/docs`文件夹下存放着如下内容：
- DDL脚本：用于MySQL数据库表初始化
- API接口文档：用于类似Postman的接口测试（提供请求示例）与详细的接口文档（浏览器直接打开即可）

## 模板项目已使用特性介绍
SpringBoot原生能力：
- spring-boot-web：提供对WEB MVC项目基础能力支撑
- spring-boot-jdbc：提供ORM层能力支撑，主要用于操作MySQL数据库
- spring-boot-aop：提供接口访问日志打印

yue-library增强特性：
- RESTful规范：统一接口状态码标识
- 优雅的接口版本控制：`@ApiVersion`
- 出色的服务端校验框架：`private Validator validator`
- 优越的ORM框架：`private Db db`
- 优化的Spring环境配置：`spring.profiles`

## 测试接口
> 详细的接口介绍与测试用例，见`resources/docs`目录下的API接口文档自行测试。

> 测试接口地址：GET http://localhost:8080/auth/v1.2/user/listAll

响应结果：
```json
{
    "code": 200,
    "msg": "成功",
    "flag": true,
    "count": null,
    "data": [
        {
            "id": 10,
            "sortIdx": null,
            "deleteTime": 0,
            "createTime": "2018-08-07 02:30:48",
            "updateTime": "2021-07-21 11:59:00",
            "userId": null,
            "cellphone": "18523246310",
            "password": "uyJPQmQgEaVE99McVoV2zg==",
            "nickname": "张三丰",
            "email": null,
            "headImg": "ss.png",
            "birthday": null,
            "sex": "man",
            "userStatus": "normal"
        },
        {
            "id": 18,
            "sortIdx": null,
            "deleteTime": 0,
            "createTime": "2018-07-18 09:10:46",
            "updateTime": "2021-07-21 11:59:00",
            "userId": null,
            "cellphone": "18522146311",
            "password": "8uXhwp4c3mYwizx/FKfY6wQRltvBd6Oc0qZvYZa+nfQ=",
            "nickname": "测试",
            "email": null,
            "headImg": "http://www.baidu.com",
            "birthday": null,
            "sex": "man",
            "userStatus": "normal"
        }
    ]
}
```

控制台日志：
```log
2021-07-21 14:40:02.940  INFO 40720 --- [nio-8080-exec-1] a.y.l.template.boot.aspect.HttpAspect    : requestIp=0:0:0:0:0:0:0:1
2021-07-21 14:40:02.940  INFO 40720 --- [nio-8080-exec-1] a.y.l.template.boot.aspect.HttpAspect    : requestUri=/auth/v1.2/user/listAll
2021-07-21 14:40:02.940  INFO 40720 --- [nio-8080-exec-1] a.y.l.template.boot.aspect.HttpAspect    : requestMethod=GET
2021-07-21 14:40:02.941  INFO 40720 --- [nio-8080-exec-1] a.y.l.template.boot.aspect.HttpAspect    : requestHandlerMethod=ai.yue.library.template.cloud.controller.user.AuthUserController.listAll()
2021-07-21 14:40:02.977 DEBUG 40720 --- [nio-8080-exec-1] druid.sql.Statement                      : {conn-10001, pstmt-20000} Parameters : []
2021-07-21 14:40:02.977 DEBUG 40720 --- [nio-8080-exec-1] druid.sql.Statement                      : {conn-10001, pstmt-20000} Types : []
2021-07-21 14:40:03.025 DEBUG 40720 --- [nio-8080-exec-1] druid.sql.Statement                      : {conn-10001, pstmt-20000} executed. SELECT * FROM `user` WHERE 1 = 1 
2021-07-21 14:40:03.103 DEBUG 40720 --- [nio-8080-exec-1] druid.sql.Statement                      : {conn-10001, pstmt-20000} closed
```