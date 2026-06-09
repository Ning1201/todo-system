# 后端开发指南

## 项目结构

```
backend/
├── src/main/java/com/todo/
│   ├── entity/                    # 实体类
│   │   ├── User.java
│   │   ├── Todo.java
│   │   ├── Label.java
│   │   └── TodoComment.java
│   ├── dto/                       # 数据传输对象
│   │   ├── LoginRequest.java
│   │   ├── LoginResponse.java
│   │   ├── UserDTO.java
│   │   ├── TodoDTO.java
│   │   ├── CreateTodoRequest.java
│   │   ├── UpdateTodoRequest.java
│   │   ├── PageRequest.java
│   │   ├── PageResponse.java
│   │   └── ApiResponse.java
│   ├── mapper/                    # MyBatis 映射
│   │   ├── UserMapper.java
│   │   ├── TodoMapper.java
│   │   ├── TodoCommentMapper.java
│   │   └── LabelMapper.java
│   ├── service/                   # 业务服务接口
│   │   ├── UserService.java
│   │   └── TodoService.java
│   ├── service/impl/              # 业务服务实现
│   │   ├── UserServiceImpl.java
│   │   └── TodoServiceImpl.java
│   ├── controller/                # 控制器
│   │   ├── AuthController.java
│   │   └── TodoController.java
│   ├── security/                  # 安全认证
│   │   ├── JwtTokenProvider.java
│   │   ├── JwtInterceptor.java
│   │   └── SecurityContextHolder.java
│   ├── exception/                 # 异常处理
│   │   ├── BusinessException.java
│   │   ├── UnauthorizedException.java
│   │   └── GlobalExceptionHandler.java
│   ├── config/                    # 配置类
│   │   └── WebMvcConfig.java
│   └── TodoSystemApplication.java # 启动类
├── src/main/resources/
│   ├── application.yml            # 应用配置
│   └── mybatis/
│       └── mapper/                # MyBatis XML 映射文件
└── pom.xml                        # Maven 依赖
```

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- IDE: IntelliJ IDEA / Eclipse

### 2. 数据库初始化

```bash
# 创建数据库并导入初始数据
mysql -u root -p < database/todo_system.sql
```

### 3. 配置应用

编辑 `backend/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/todo_system
    username: root
    password: your_password
```

### 4. 启动应用

```bash
# 方式一：使用 Maven
cd backend
mvn clean install
mvn spring-boot:run

# 方式二：使用 IDE
- 右键点击 TodoSystemApplication.java
- 选择 Run 'TodoSystemApplication.main()'
```

应用将在 `http://localhost:8080` 启动。

## 核心模块说明

### Entity (实体类)

实体类对应数据库表结构：

- **User**: 用户表
  - `id`: 用户ID
  - `username`: 用户名
  - `password`: 加密密码
  - `email`: 邮箱
  - `role`: 角色 (USER/ADMIN)
  - `status`: 状态 (ACTIVE/INACTIVE/LOCKED)

- **Todo**: 待办事项表
  - `id`: 待办ID
  - `title`: 标题
  - `creatorId`: 创建者ID
  - `assigneeId`: 分配者ID
  - `status`: 状态 (DRAFT/PENDING/IN_PROGRESS/COMPLETED/ARCHIVED/DELETED)
  - `priority`: 优先级 (LOW/MEDIUM/HIGH/URGENT)

### DTO (数据传输对象)

DTO 用于请求/响应数据的封装：

- **LoginRequest**: 登录请求
- **LoginResponse**: 登录响应（包含 Token 和用户信息）
- **CreateTodoRequest**: 创建待办请求
- **UpdateTodoRequest**: 更新待办请求
- **TodoDTO**: 待办信息响应
- **PageResponse**: 分页响应包装

### Mapper (数据访问层)

Mapper 使用 MyBatis 注解进行数据库操作：

```java
// 示例：UserMapper
@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);
}
```

### Service (业务逻辑层)

Service 接口定义业务操作，实现类包含具体逻辑：

```java
// 示例：TodoService
public interface TodoService {
    Todo create(CreateTodoRequest request, Long creatorId);
    Todo update(Long id, UpdateTodoRequest request, Long userId);
    // ...权限控制逻辑
}
```

**权限控制规则已内置在 Service 实现中：**

```java
// 只有创建者或管理员可以修改
if (!todo.getCreatorId().equals(userId) && !"ADMIN".equals(getUserRole(userId))) {
    throw new UnauthorizedException("您没有权限修改此待办");
}
```

### Controller (控制层)

Controller 处理 HTTP 请求，调用 Service 进行业务处理：

```java
@PostMapping
public ApiResponse<TodoDTO> createTodo(@Valid @RequestBody CreateTodoRequest request) {
    Long creatorId = SecurityContextHolder.getUserId();
    Todo todo = todoService.create(request, creatorId);
    return ApiResponse.success(todoService.convertToDTO(todo));
}
```

### Security (安全认证)

#### JwtTokenProvider

生成和验证 JWT 令牌：

```java
// 生成令牌
String token = jwtTokenProvider.generateToken(userId, username);

// 验证令牌
boolean isValid = jwtTokenProvider.validateToken(token);

// 获取用户ID
Long userId = jwtTokenProvider.getUserIdFromToken(token);
```

#### JwtInterceptor

拦截所有请求，从请求头中提取 JWT 令牌并验证：

```java
// 在请求头中获取令牌
String token = request.getHeader("Authorization").replace("Bearer ", "");

// 验证令牌并设置上下文
if (tokenProvider.validateToken(token)) {
    SecurityContextHolder.setUserId(userId);
}
```

#### SecurityContextHolder

线程本地存储，保存当前用户信息：

```java
// 获取当前用户ID
Long userId = SecurityContextHolder.getUserId();

// 设置用户信息
SecurityContextHolder.setUserId(1L);
SecurityContextHolder.setUsername("admin");

// 清除上下文
SecurityContextHolder.clear();
```

## API 端点

### 认证接口

```http
POST /auth/login
请求：{ "username": "admin", "password": "password" }
响应：{ "token": "...", "user": {...} }
```

### 待办接口

```
POST   /todos                  # 创建待办
GET    /todos/{id}             # 获取待办详情
PUT    /todos/{id}             # 更新待办
PATCH  /todos/{id}/assign      # 分配待办
PATCH  /todos/{id}/priority    # 更新优先级
PATCH  /todos/{id}/start       # 开始处理
PATCH  /todos/{id}/complete    # 标记完成
DELETE /todos/{id}             # 删除待办
GET    /todos/my/created       # 我创建的待办
GET    /todos/my/assigned      # 分配给我的待办
GET    /todos/status/{status}  # 按状态查询
```

详见 `docs/API.md`

## 常见问题

### Q: 如何添加新的 API 端点？

A: 
1. 在 `service/TodoService.java` 中添加方法签名
2. 在 `service/impl/TodoServiceImpl.java` 中实现方法
3. 在 `controller/TodoController.java` 中添加对应的 Controller 方法
4. 在 `mapper/TodoMapper.java` 中添加数据库操作

### Q: 如何修改权限规则？

A: 修改 `service/impl/TodoServiceImpl.java` 中的权限检查逻辑：

```java
// 权限检查示例
if (!todo.getCreatorId().equals(userId) && !"ADMIN".equals(getUserRole(userId))) {
    throw new UnauthorizedException("您没有权限");
}
```

### Q: 如何扩展用户信息？

A: 
1. 修改 `todo_system.sql` 中的 `users` 表结构
2. 更新 `entity/User.java` 添加新字段
3. 更新 `dto/UserDTO.java`
4. 更新 `mapper/UserMapper.java`

## 性能优化建议

1. **数据库索引**: 已在 SQL 脚本中创建常用字段的索引
2. **缓存**: 可使用 Redis 缓存用户信息和待办数据
3. **分页**: 所有列表查询都支持分页
4. **异步处理**: 对于耗时操作可使用 `@Async`

## 测试

建议使用 Postman 或 curl 进行 API 测试：

```bash
# 登录
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password"}'

# 创建待办
curl -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"title":"测试待办","priority":"HIGH"}'
```

## 部署

### Docker 部署

1. 创建 Dockerfile：

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/todo-system-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

2. 构建镜像：

```bash
mvn clean package
docker build -t todo-system .
```

3. 运行容器：

```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/todo_system \
  todo-system
```

## 更新日志

- **v1.0.0** (2023-06-09)
  - 初始版本
  - 完整的待办事项管理功能
  - JWT 认证机制
  - 基于角色的权限控制
