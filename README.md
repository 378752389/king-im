# king-im

仿微信桌面端IM项目；



## 前端

### 功能实现

#### 登录
1. 基于token的jwt登录认证机制；
2. token过期自动刷新；

#### 好友管理

1. 基于`pinia`进行好友列表管理；
2. 好友详情页面：包含好友基本信息、备注、个性签名展示；
3. 更新好友信息：更新好友备注；
4. 好友列表右键进行好友删除；
5. 好友列表右键进行特别关注；（加星置顶）

#### 社交管理

1. 朋友、群搜索：基于名称或号码进行搜索、展示搜索结果列表、延迟请求和请求取消；
2. 添加好友、群，并实时渲染到好友列表、群列表；

#### 聊天会话管理

1. 基于`pinia`进行群聊列表管理；
2. 默认群头像：群图片未设置基于群名称自动创建；
3. 群聊信息详情页：修改群信息、发送消息、退出群聊、删除群聊；
4. 群成员列表：显示群成员，可以邀请好友进群；
5. 群列表右击删除聊天记录、群置顶、退出群聊；
6. 超5分钟未聊天插入时间消息提示

#### 文件上传

1. 点击按钮文件上传；
2. 拖拽文件上传；


### 前端设计难点

1. `jwt token`过期自动刷新；
2. 聊天会话数据结构设计、消息结构存储，新消息通知；
3. 基于`pinia`进行群聊、和好友列表管理；
4. 群聊 `@` 功能实现；


## todo
- [x] 聊天消息列表中，两天消息之间间隔超过5分钟，将插入一条时间提示消息（前端实现）
- [ ] 群聊at接受对象侧边栏弹出被at提示
- [x] 登录页面样式设计
- [x] 注册功能;<span style="color: red;">邮箱注册后期实现，暂时先用请求方式注册</span>
- [ ] 目前采用本地消息推送，打算改造为分布式消息推送
- [ ] 重新设计消息接口（目前为拉群最近1000条消息）
   1. 离线消息：消息状态为未发送的消息
   2. 拉取消息：拉取最近500条私聊消息、目前加入的各个群聊100条最新消息
- [ ] 添加控制消息，比如异步拉去消息时后端在进行数据同步时需要发送loading命令，提示用户暂时不要进行刷新操作；
- [ ] 个人信息填写页面
- [ ] 后端日志进行traceId配置，uid配置