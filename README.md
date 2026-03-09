# HTUOJ - 在线判题系统

<div align="center">

一个基于 Spring Boot + Vue 的在线编程判题系统（Online Judge）

[![Java](https://img.shields.io/badge/Java-8+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-2.6-green.svg)](https://vuejs.org/)
[![MyBatis-Plus](https://img.shields.io/badge/MyBatis--Plus-3.5.3-blue.svg)](https://baomidou.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

</div>

## 📖 项目简介

HTUOJ 是一个功能完整的在线判题系统，支持用户在线编程、提交代码、自动判题、参加竞赛等功能。系统采用前后端分离架构，后端基于 Spring Boot 微服务架构，前端使用 Vue.js 开发。

## ✨ 主要功能

### 🎯 核心功能
- **题目管理** - 支持多种编程语言（C/C++/Java/Python 等）的题目创建与管理
- **在线判题** - 实时提交代码，自动判题并返回结果（AC/WA/TLE/RE 等）
- **竞赛系统** - 创建比赛、报名参赛、实时排名
- **题解社区** - 用户可发布题解、文章，支持点赞、收藏、评论

### 📊 用户功能
- 用户注册/登录（JWT 认证）
- 个人主页与做题统计
- 关注系统与消息通知
- 代码提交历史与能力分析

### 🤖 判题服务
- 多语言代码编译与运行
- 沙箱隔离确保安全
- 测试用例自动比对
- 判题结果实时推送（WebSocket）

### 📈 数据统计
- 题目难度分布分析
- 用户能力雷达图
- 竞赛排名与积分系统
- 提交记录可视化展示

## 🛠️ 技术栈

### 后端技术

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.7.6 | 核心框架 |
| Spring Cloud | 2021.0.3 | 微服务架构 |
| Spring Cloud Alibaba | 2021.0.4.0 | 微服务生态 |
| MyBatis-Plus | 3.5.3.1 | ORM 框架 |
| ShardingSphere | 5.3.2 | 分库分表 |
| Redis | - | 缓存 |
| Redisson | 3.13.6 | Redis 客户端 |
| RabbitMQ | - | 消息队列 |
| Elasticsearch | 7.12.1 | 全文搜索 |
| JWT | 0.9.1 | 身份认证 |
| Sa-Token | - | 权限认证 |
| WebSocket | - | 实时通信 |
| 阿里云 OSS | - | 文件存储 |

### 前端技术

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 2.6.14 | 渐进式框架 |
| Vue Router | 3.6.5 | 路由管理 |
| Pinia | 2.2.4 | 状态管理 |
| Element UI | 2.15.14 | PC 端 UI 组件 |
| Vant | 2.13.6 | 移动端 UI 组件 |
| CodeMirror | 5.65.18 | 代码编辑器 |
| ECharts | 5.5.1 | 数据可视化 |
| G2Plot | 2.4.32 | 图表库 |
| Axios | 1.7.7 | HTTP 请求 |
| Mavon Editor | 2.10.4 | Markdown 编辑器 |

## 📁 项目结构

```
htuoj/
├── htuoj-project      # 核心业务模块（题目、比赛、用户等）
├── htuoj-dispatch     # 判题任务调度模块
├── htuoj-common       # 公共模块（工具类、常量等）
├── htuoj-judge        # 判题核心模块（代码执行、沙箱）
├── htuoj-gateway      # 网关模块
└── htuoj-vue          # Vue 前端项目
```

## 🚀 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis
- RabbitMQ
- Elasticsearch（可选）
- Node.js 14+

### 后端部署

1. 克隆项目
```bash
git clone https://github.com/yourusername/htuoj.git
cd htuoj
```

2. 修改配置文件
```bash
# 编辑 application-dev.yaml 配置数据库、Redis 等连接信息
# 编辑 shardingsphere-config-dev.yaml 配置分库分表
```

3. 导入数据库
```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE htuoj DEFAULT CHARACTER SET utf8mb4;"
# 导入 SQL 文件
mysql -u root -p htuoj < sql/htuoj.sql
```

4. 构建项目
```bash
# 编译后端
mvn clean package -DskipTests
```

5. 启动服务
```bash
# 启动主应用
java -jar htuoj-project/target/htuoj-project-0.0.1-SNAPSHOT.jar
```

### 前端部署

1. 进入前端目录
```bash
cd htuoj-vue
```

2. 安装依赖
```bash
npm install
```

3. 启动开发服务器
```bash
npm run serve
```

4. 构建生产版本
```bash
npm run build
```

## 📸 功能展示

<!-- 使用 Github 图床或者自定义图片链接 -->
<!-- ![首页](docs/images/home.png) -->
<!-- ![题目列表](docs/images/problem-list.png) -->
<!-- ![代码提交](docs/images/submit.png) -->
<!-- ![判题结果](docs/images/result.png) -->

## ⚙️ 配置文件说明

### application-dev.yaml

| 配置项 | 说明 |
|--------|------|
| server.port | 服务端口 |
| spring.datasource | 数据源配置 |
| spring.redis | Redis 配置 |
| aliyun.oss | 阿里云 OSS 配置 |
| jwt | JWT 密钥配置 |
| sa-token | Sa-Token 认证配置 |

## 🤝 参与贡献

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 开源协议

MIT License

## 👨‍💻 开发团队

- HTUOJ Team

## 📧 联系方式

- Email: support@htuoj.cn
- 官网：https://htuoj.cn

## 🙏 致谢

感谢以下开源项目：

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Vue.js](https://vuejs.org/)
- [MyBatis-Plus](https://baomidou.com/)
- [Element UI](https://element.eleme.cn/)
- [CodeMirror](https://codemirror.net/)

---

<div align="center">

如果这个项目对你有帮助，请给一个 ⭐️ Star 支持！

</div>
