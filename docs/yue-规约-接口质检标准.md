## 为什么要有接口质检标准，接口质检标准是什么？
　　接口质检标准是检验API是否标准的一种规范，可帮助我们统一接口管理。

## 接口命名规范
　　URI命名风格采用增强Restful API接口形式。
- GET 查询接口（用于查询数据）
- POST 提交接口（用于插入数据、提交安全加密数据等）
- PUT 更新接口（用于更新数据，具有破坏性）
- DELETE 删除接口（用于删除数据，具有破坏性）

### GET规范
- /user/get 用户-单个（获得单条数据）
- /user/list 用户-列表（获得列表数据）
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