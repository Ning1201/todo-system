# 待办事项管理系统 - 后端

基于 Spring Boot 3.1.5 + MyBatis 开发的待办事项管理系统后端。

## 快速开始

### 前置要求
- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### 安装步骤

1. **克隆项目**
```bash
git clone https://github.com/Ning1201/todo-system.git
cd todo-system/backend
```

2. **初始化数据库**
```bash
mysql -u root -p < ../database/todo_system.sql
```

3. **配置应用**
编辑 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/todo_system
    username: root
    password: your_password
```

4. **启动应用**
```bash
mvn clean install
mvn spring-boot:run
```

应用将在 `http://localhost:8080` 启动。

## 项目结构

```
src/main/java/com/todo/
├── entity/              # 实体类
├── dto/                 # 数据传输对象
├── mapper/              # MyBatis 映射
├── service/             # 业务服务
├── controller/          # 控制器
├── security/            # 安全认证
├── exception/           # 异常处理
├── config/              # 配置类
└── TodoSystemApplication.java
```

## API 文档

详见 `../docs/API.md`

## 开发指南

详见 `../docs/BACKEND.md`

## 默认用户

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | 123456 | 管理员 |
| user1 | 123456 | 用户 |
| user2 | 123456 | 用户 |

## 主要特性

- ✅ JWT 认证
- ✅ 权限控制
- ✅ RESTful API
- ✅ 异常处理
- ✅ 日志系统
- ✅ MyBatis ORM

## 部署

详见 `../docs/DEPLOYMENT.md`
