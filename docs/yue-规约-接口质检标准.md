## 为什么要有接口质检标准，接口质检标准是什么？
　　接口质检标准是检验API是否规范的一种标准，可帮助我们统一接口管理。一个比较好的实践方案，应该做到语义简洁明了，看到 URL 就知道是做什么的，就如同 RESTful 中所提到的那样。

## 接口命名规范
　　URI命名风格采用增强 Restful API 接口形式。
- GET 查询接口（用于查询数据）
- POST 提交接口（用于插入数据、提交安全加密数据等）
- PUT 更新接口（用于更新数据，具有破坏性）
- DELETE 删除接口（用于删除数据，具有破坏性）

### GET规范
- /user/get 用户-单个（获得单条数据）
- /user/list 用户-列表（获得列表数据）
- /user/page 用户-分页（获得分页数据）
- /user/isUser 用户-确认（获得Boolean值）

### POST规范
- /user/insert 用户-插入（插入数据）
- /wxPay/app 微信支付-APP（业务操作或涉及加密等）
- /aliPay/app 支付宝支付-APP（业务操作或涉及加密等）
- /open/login 登录（业务操作或涉及加密等）
- /open/logout 登出（业务操作或涉及加密等）

### PUT规范
- /user/updateById 用户-更新-ById（更新单条数据）
- /user/updateNickname 用户-更新-昵称（更新具体值）
- /user/resetPassword 用户-重置密码（更新业务）

### DELETE规范
- /user/delete 用户-删除（删除单条数据）
- /user/deleteByNickname 用户-删除-ByNickname（删除符合条件数据）

## 接口文档规范
- <font color=red>（必须）</font>做到业务逻辑自测
- <font color=red>（必须）</font>提供标准的请求示例与响应示例
- <font color=red>（必须）</font>请求示例配备字段说明，并做好字段规则排序
- <font color=red>（重要）</font>接口字段命名与数据库保持一致
- <font color=red>（重要）</font>响应示例：复杂业务接口提供详细的字段注释
- （推荐）响应示例：日常业务接口提供简单注释