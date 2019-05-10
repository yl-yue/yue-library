# validator
<p>轻量级服务端校验框架</p>
<p>支持注解、功能齐全、使用简便</p>

## 一、功能简介
主要提供便捷的后台数据校验功能，支持单个字段或参数校验，也支持通过注解校验对象，用法简单。<br>
提供基本的非空、长度、大小等校验方法，也提供一些特殊的正则校验、身份证、电话、邮箱、IP等校验方法。

## 二、用法介绍
目前提供以下校验方法，支持后续持续扩展

  | 注解        | 说明    |
  | :---------: | :------ |
  | @NotNull | 非空校验 |
  | @Max | 最大值校验 |
  | @Min | 最小值校验 |
  | @MaxLength | 最大长度校验，支持集合、数组长度校验 |
  | @MinLength | 最大长度校验，支持集合、数组长度校验 |
  | @IdCard | 身份证校验 |
  | @Email | 邮箱格式校验 |
  | @Phone | 手机号校验 |
  | @IP | IP地址校验 |
  | @Chinese | 中文校验 |
  | @English | 英文校验 |
  | @Regex | 自定义正则校验 |
  | @Date | 日期格式校验 |

### 1. 单个参数验证
```java
ValidateUtils.is("a").notNull();
 
ValidateUtils.is("test").maxLength(20).minLength(4);
 
ValidateUtils.is(50).min(20).max(60);
```

通过and()支持连写（连写直接切换校验对象）

```java
ValidateUtils.is("a").notNull().and("test").maxLength(20).minLength(4).and(50).min(20).max(60);
```
支持自定义错误信息

```java
ValidateUtils.is("test").maxLength(20,"最大长度不能超过20个字").minLength(4,"最小长度不能少于4个字");
```
### 2. 校验整个对象（通过注解）
在类的属性上定义注解，同时支持自定义错误信息
```java
public class User {

    @NotNull(msg = "姓名不能为空")
    @MaxLength(value = 20,msg = "姓名不能超过20个字")
    private String name;

    private Date birthday;

    @IdCard
    private String idcard;

    @Max(30)
    @Min(12)
    private int age;

    @Email
    @MaxLength(50)
    private String email;

    @Phone
    private String phone;

    @Regex("[1-9]([0-9]{5,11})")
    private String qq;
    
    //get... set..
}
```