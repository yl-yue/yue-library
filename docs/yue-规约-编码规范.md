## yue编码规范
　　yue编码规范由[《阿里巴巴Java开发手册》](_media/阿里巴巴Java开发手册_v1.4.0_详尽版.pdf)、[《HTML与CSS规范》](http://codeguide.bootcss.com/)、[《通用开发标准》](#通用开发标准)组成。	

## 后端编码规范
　　服务端编码规范尤为重要，好的编码习惯不光能减少BUG的产生，更能严谨开发人员的逻辑思维，从而减少一些不必要的损失。<br>

- [《阿里巴巴Java开发手册》](http://suo.im/4tsCsN)
- [《通用开发标准》](#通用开发标准)
- [STS（eclipse）JAVA代码模版](https://gitee.com/yl-yue/yue-library/raw/master/docs/_media/STS配置.epf)

> 导入eclipse即可：Import -> preferences -> 选择文件 -> Finish <br>

## 前端编码规范

- [《阿里巴巴Java开发手册》第一节、第四节编程规约](http://suo.im/4tsCsN)
- [《HTML与CSS规范》](http://codeguide.bootcss.com)
- [《通用开发标准》](#通用开发标准)

## <a name="通用开发标准">通用开发标准</a>
### 注释规约
> 对于项目中，重要的模块组成部分，严谨的业务逻辑是很有必要的。（如：用户财富相关等敏感接口）
> 这里不只需要注重业务逻辑，更加需要代码的可维护性。

### 语法规约
> 多层逻辑嵌套的语法是很不美观且难以维护的错误选择。<br>
> 遵循原则：
>> 1. 尽量保持只有一层主逻辑，程序永远是执行到最后一行代码，才是正确执行的原则。
>> 2. 业务逻辑分段表述，第一步，第二步，第三步...直到最后一步完成。不同逻辑、不同语义、不同业务的代码之间插入一个空行分隔开来以提升可读性。

~~注释规约与语法规约示例：~~ <font color=red>待更新！</font>

![编码规范示例1](_images/编码规范示例1.png)
![编码规范示例2](_images/编码规范示例2.png)
![编码规范示例3](_images/编码规范示例3.png)

### 业务命名法
- 业务命名法，以一个业务为一个包（如：红包活动，redbag）
- 一个包中可以包含多个相同业务类

### POJO
- IPO（Interface Param Object）：接口入参对象，建议复杂参数使用
- DO（Data Object）：数据对象一般情况下与数据库表结构一一对应，通过 DAO 层向上传输数据源对象。
- DTO（Data Transfer Object）：数据传输对象，Service 或 Manager 向外传输的对象。使用场景：第三方接口固定返回对象，多表链接对象、特殊业务固定传输对象
- VO（View Object）：业务视图对象又称显示层对象，通常是 Web 向模板渲染引擎层传输的对象。