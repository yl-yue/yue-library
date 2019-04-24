## 为什么要有接口质检标准，接口质检标准是什么？
　　接口质检标准是检验API是否标准的一种规范，可帮助我们统一接口管理。

## 接口命名规范
　　URI命名风格采用增强Restful API接口形式。
- GET 查询接口（用于查询数据）
- POST 提交接口（用于插入数据、提交安全加密数据等）
- PUT 更新接口（用于更新数据，具有破坏性）
- DELETE 删除接口（用于删除数据，具有破坏性）

### GET规范
- /user/get 用于获得单条数据
- /user/list 用于获得列表数据
- /user/isUser 用于获得Boolean值，确认用户是否存在

### POST规范
- /user/insert 插入数据
- /open/login 加密数据接口

### PUT规范
- /user/updateById 根据id更新用户数据
- /user/updateNickname 更新用户昵称

### DELETE规范
- /user/delete 根据ID删除数据
- /user/deleteByNickname 根据用户昵称删除数据