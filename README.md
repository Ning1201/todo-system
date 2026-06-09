# 待办事项管理系统

## 项目概览
完整的待办事项管理系统，支持创建、分配、处理、完成、归档等完整业务流程。

### 技术栈
- **前端**: Vue.js 3
- **后端**: Spring Boot + Spring MVC + MyBatis
- **数据库**: MySQL 8.0
- **API规范**: RESTful API

## 核心流程
```
创建待办 → 分配优先级 → 开始处理 → 标记完成 → 归档/删除
```

## 状态流转图
```
草稿 (DRAFT)
  ↓
待分配 (PENDING)
  ↓
进行中 (IN_PROGRESS)
  ↓
已完成 (COMPLETED)
  ↓
已归档 (ARCHIVED) / 已删除 (DELETED)
```

## 业务规则
1. ✅ 只有创建者或管理员可以修改待办内容
2. ✅ 只有分配者可以修改优先级
3. ✅ 状态只能按照流转顺序变更
4. ✅ 已完成的事项可以重新激活

## 项目结构
```
todo-system/
├── backend/                          # Spring Boot 后端
│   ├── src/main/java
│   │   └── com/todo/
│   │       ├── entity/              # 实体类
│   │       ├── dto/                 # 数据传输对象
│   │       ├── controller/          # 控制器
│   │       ├── service/             # 业务服务
│   │       ├── mapper/              # MyBatis映射
│   │       ├── config/              # 配置类
│   │       └── exception/           # 异常处理
│   ├── src/main/resources
│   │   ├── application.yml          # 应用配置
│   │   └── mybatis/                 # MyBatis XML
│   └── pom.xml                      # Maven依赖
├── frontend/                         # Vue.js 前端
│   ├── src/
│   │   ├── components/              # Vue组件
│   │   ├── views/                   # 页面
│   │   ├── api/                     # API调用
│   │   ├── stores/                  # 状态管理
│   │   └── App.vue
│   ├── package.json
│   └── vite.config.js
├── database/                         # 数据库脚本
│   └── todo_system.sql              # 初始化脚本
└── docs/                             # 文档
    └── API.md                        # API文档
```

## 快速开始

### 后端开发
```bash
cd backend
# 配置 application.yml 中的数据库连接
mvn clean install
mvn spring-boot:run
```

### 前端开发
```bash
cd frontend
npm install
npm run dev
```

### 数据库初始化
```bash
# 导入 database/todo_system.sql
mysql -u root -p < database/todo_system.sql
```

## API 文档
详见 `docs/API.md`

## 许可证
MIT
