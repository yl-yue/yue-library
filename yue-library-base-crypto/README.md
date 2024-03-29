## 介绍
　　base-crypto库基于 [hutool-crypto](https://hutool.cn/docs/#/crypto/概述) 进行二次封装，提供单例加解密与yml配置。

## 快速开始
### 引入模块
`yue-library-dependencies`作为父项目，在`pom.xml`文件中添加：
``` pom
<dependencies>
	<dependency>
		<groupId>ai.ylyue</groupId>
		<artifactId>yue-library-base-crypto</artifactId>
	</dependency>
</dependencies>
```

### 配置
目前提供AES、RSA单例配置
```yml
yue:
  crypto:
    aes-keyt: 
    rsa-private-keyt: 
    rsa-public-keyt: 
```

### 使用
`SecureSingleton` 用于获得单例加解密。如果不需要配置默认提供的单例（如：动态获得密钥动态加密），可以直接使用： `SecureUtil` 工具，具体参考 [hutool文档](https://hutool.cn/docs/#/crypto/加密解密工具-SecureUtil)
```java
// AES加密
AES aes = SecureSingleton.getAES();
aes.encryptBase64("");
aes.decryptStr("");

// RSA加密-公钥加密，私有解密
RSA rsa = SecureSingleton.getRSA();
rsa.encryptBcd("", KeyType.PublicKey);
rsa.decryptStrFromBcd("", KeyType.PrivateKey);
```

更多方法请参阅API文档...