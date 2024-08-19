# king-im

仿微信桌面端IM项目；



## 前端

### 功能实现

#### 登录
1. 基于token的jwt登录认证机制；
2. token过期自动刷新；


#### 会话管理
1. 本地会话数据结构设计；
2. 会话列表：新消息提示，最近消息日期显示；
3. 多种消息类型适配：文字、图片、音频、视频、其他文件；
4. 文本域监听；

#### 好友管理

1. 基于`pinia`进行好友列表管理；
2. 好友详情页面：包含好友基本信息、备注、个性签名展示；
3. 更新好友信息：更新好友备注；
4. 好友列表右键进行好友删除；
5. 好友列表右键进行特别关注；（加星置顶）

#### 社交管理

1. 朋友、群搜索：基于名称或号码进行搜索、展示搜索结果列表、延迟请求和请求取消；
2. 添加好友、群，并实时渲染到好友列表、群列表；

#### 会话管理

1. 基于`pinia`进行群聊列表管理；
2. 默认群头像：群图片未设置基于群名称自动创建；
3. 群聊信息详情页：修改群信息、发送消息、退出群聊、删除群聊；
4. 群成员列表：显示群成员，可以邀请好友进群；
5. 群列表右击删除聊天记录、群置顶、退出群聊；

#### 文件上传

1. 点击按钮文件上传；
2. 拖拽文件上传；


### 前端设计难点

1. `jwt token`过期自动刷新；
2. 聊天会话数据结构设计、消息结构存储，新消息通知；
3. 基于`pinia`进行群聊、和好友列表管理；
4. 群聊 `@` 功能实现；
