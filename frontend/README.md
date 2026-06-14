# 待办事项管理系统 - 前端

基于 Vue.js 3 + Vite 开发的待办事项管理系统前端。

## 快速开始

### 前置要求
- Node.js 16.0+
- npm 7.0+ 或 yarn

### 安装步骤

1. **克隆项目**
```bash
git clone https://github.com/Ning1201/todo-system.git
cd todo-system/frontend
```

2. **安装依赖**
```bash
npm install
```

3. **启动开发服务器**
```bash
npm run dev
```

应用将在 `http://localhost:5173` 启动。

4. **构建生产版本**
```bash
npm run build
```

## 项目结构

```
src/
├── api/                 # API 接口调用
├── stores/              # Pinia 状态管理
├── router/              # Vue Router 路由
├── components/          # Vue 组件
├── views/               # 页面视图
├── App.vue              # 根组件
└── main.js              # 应用入口
```

## 可用脚本

- `npm run dev` - 启动开发服务器
- `npm run build` - 构建生产版本
- `npm run preview` - 预览构建结果
- `npm run lint` - 代码检查

## 开发指南

详见 `../docs/FRONTEND.md`

## 主要特性

- ✅ Vue 3 Composition API
- ✅ Vite 快速构建
- ✅ Pinia 状态管理
- ✅ Vue Router 路由
- ✅ Element Plus UI
- ✅ Axios 请求库
- ✅ JWT 认证

## 部署

详见 `../docs/DEPLOYMENT.md`
