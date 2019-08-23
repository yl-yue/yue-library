## 介绍
　　pay库基于pay-java-parent进行二次封装，**让你真正做到一行代码实现支付聚合**，让你可以不用理解支付怎么对接，只需要专注你的业务

## 快速开始
### 引入模块
1. `yue-library-dependencies`作为父项目，在`pom.xml`文件中添加：
``` pom
<dependencies>
	<dependency>
		<groupId>ai.ylyue</groupId>
		<artifactId>yue-library-pay</artifactId>
	</dependency>
</dependencies>
```

2. 引入 你需要对接的基于`pay-java-parent`支付开发包,具体支付模块 "{module-name}" 为具体的支付渠道的模块名 pay-java-ali，pay-java-wx等
```xml
<dependency>
	<groupId>com.egzosn</groupId>
	<artifactId>{module-name}</artifactId>
</dependency>
```

### 使用
注入Bean `Pay`
更多方法请参阅API文档...