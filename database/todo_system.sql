-- 待办事项管理系统数据库初始化脚本
-- MySQL 8.0+

-- 创建数据库
CREATE DATABASE IF NOT EXISTS todo_system;
USE todo_system;

-- 用户表
CREATE TABLE IF NOT EXISTS `users` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码(加密)',
  `email` VARCHAR(100) NOT NULL UNIQUE COMMENT '邮箱',
  `full_name` VARCHAR(100) COMMENT '全名',
  `role` ENUM('USER', 'ADMIN') DEFAULT 'USER' COMMENT '角色',
  `status` ENUM('ACTIVE', 'INACTIVE', 'LOCKED') DEFAULT 'ACTIVE' COMMENT '状态',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  KEY `idx_username` (`username`),
  KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 待办事项表
CREATE TABLE IF NOT EXISTS `todos` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `title` VARCHAR(200) NOT NULL COMMENT '标题',
  `description` TEXT COMMENT '描述',
  `creator_id` BIGINT NOT NULL COMMENT '创建者ID',
  `assignee_id` BIGINT COMMENT '分配者ID',
  `status` ENUM('DRAFT', 'PENDING', 'IN_PROGRESS', 'COMPLETED', 'ARCHIVED', 'DELETED') DEFAULT 'DRAFT' COMMENT '状态',
  `priority` ENUM('LOW', 'MEDIUM', 'HIGH', 'URGENT') DEFAULT 'MEDIUM' COMMENT '优先级',
  `due_date` DATETIME COMMENT '截止日期',
  `start_date` DATETIME COMMENT '开始日期',
  `end_date` DATETIME COMMENT '完成日期',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted_at` DATETIME COMMENT '删除时间',
  FOREIGN KEY (`creator_id`) REFERENCES `users`(`id`),
  FOREIGN KEY (`assignee_id`) REFERENCES `users`(`id`),
  KEY `idx_creator_id` (`creator_id`),
  KEY `idx_assignee_id` (`assignee_id`),
  KEY `idx_status` (`status`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='待办事项表';

-- 待办事项历史记录表
CREATE TABLE IF NOT EXISTS `todo_history` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `todo_id` BIGINT NOT NULL COMMENT '待办ID',
  `operator_id` BIGINT NOT NULL COMMENT '操作者ID',
  `action` VARCHAR(50) NOT NULL COMMENT '操作类型(CREATE/UPDATE/STATUS_CHANGE/DELETE)',
  `old_value` JSON COMMENT '旧值',
  `new_value` JSON COMMENT '新值',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  FOREIGN KEY (`todo_id`) REFERENCES `todos`(`id`),
  FOREIGN KEY (`operator_id`) REFERENCES `users`(`id`),
  KEY `idx_todo_id` (`todo_id`),
  KEY `idx_operator_id` (`operator_id`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='待办事项历史记录表';

-- 待办事项评论表
CREATE TABLE IF NOT EXISTS `todo_comments` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `todo_id` BIGINT NOT NULL COMMENT '待办ID',
  `author_id` BIGINT NOT NULL COMMENT '作者ID',
  `content` TEXT NOT NULL COMMENT '评论内容',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (`todo_id`) REFERENCES `todos`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`author_id`) REFERENCES `users`(`id`),
  KEY `idx_todo_id` (`todo_id`),
  KEY `idx_author_id` (`author_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='待办事项评论表';

-- 待办事项附件表
CREATE TABLE IF NOT EXISTS `todo_attachments` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `todo_id` BIGINT NOT NULL COMMENT '待办ID',
  `file_name` VARCHAR(255) NOT NULL COMMENT '文件名',
  `file_path` VARCHAR(500) NOT NULL COMMENT '文件路径',
  `file_size` BIGINT COMMENT '文件大小(字节)',
  `file_type` VARCHAR(50) COMMENT '文件类型',
  `uploader_id` BIGINT NOT NULL COMMENT '上传者ID',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  FOREIGN KEY (`todo_id`) REFERENCES `todos`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`uploader_id`) REFERENCES `users`(`id`),
  KEY `idx_todo_id` (`todo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='待办事项附件表';

-- 标签表
CREATE TABLE IF NOT EXISTS `labels` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL UNIQUE COMMENT '标签名',
  `color` VARCHAR(20) COMMENT '标签颜色',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签表';

-- 待办事项标签关系表
CREATE TABLE IF NOT EXISTS `todo_labels` (
  `todo_id` BIGINT NOT NULL COMMENT '待办ID',
  `label_id` BIGINT NOT NULL COMMENT '标签ID',
  PRIMARY KEY (`todo_id`, `label_id`),
  FOREIGN KEY (`todo_id`) REFERENCES `todos`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`label_id`) REFERENCES `labels`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='待办事项标签关系表';

-- 权限表
CREATE TABLE IF NOT EXISTS `permissions` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `todo_id` BIGINT NOT NULL COMMENT '待办ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `permission_type` ENUM('VIEW', 'EDIT', 'MANAGE') DEFAULT 'VIEW' COMMENT '权限类型',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  UNIQUE KEY `uk_todo_user` (`todo_id`, `user_id`),
  FOREIGN KEY (`todo_id`) REFERENCES `todos`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
  KEY `idx_todo_id` (`todo_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- 初始用户数据
INSERT INTO `users` (`username`, `password`, `email`, `full_name`, `role`) VALUES
('admin', '$2a$10$slYQmyNdGzin7olVN3/p2OPST9/PgBkqquzi.Ss7KIUgO2t0jKMm2', 'admin@todo.com', '管理员', 'ADMIN'),
('user1', '$2a$10$slYQmyNdGzin7olVN3/p2OPST9/PgBkqquzi.Ss7KIUgO2t0jKMm2', 'user1@todo.com', '用户1', 'USER'),
('user2', '$2a$10$slYQmyNdGzin7olVN3/p2OPST9/PgBkqquzi.Ss7KIUgO2t0jKMm2', 'user2@todo.com', '用户2', 'USER');

-- 初始标签数据
INSERT INTO `labels` (`name`, `color`) VALUES
('紧急', '#FF0000'),
('重要', '#FF7F00'),
('普通', '#00FF00'),
('低优先级', '#0000FF');

-- 索引优化
ALTER TABLE `todos` ADD INDEX `idx_creator_status` (`creator_id`, `status`);
ALTER TABLE `todos` ADD INDEX `idx_assignee_status` (`assignee_id`, `status`);
ALTER TABLE `todos` ADD INDEX `idx_status_priority` (`status`, `priority`);
