-- Create database
CREATE DATABASE IF NOT EXISTS hd_chat DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE hd_chat;

-- User table
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary key ID',
  `username` varchar(50) NOT NULL COMMENT 'Username',
  `password` varchar(100) NOT NULL COMMENT 'Password',
  `nickname` varchar(50) DEFAULT NULL COMMENT 'Nickname',
  `avatar` varchar(255) DEFAULT NULL COMMENT 'Avatar URL',
  `email` varchar(100) DEFAULT NULL COMMENT 'Email',
  `phone` varchar(20) DEFAULT NULL COMMENT 'Phone number',
  `status` int(1) DEFAULT 1 COMMENT 'Status (0: disabled, 1: enabled)',
  `last_login_time` datetime DEFAULT NULL COMMENT 'Last login time',
  `create_time` datetime NOT NULL COMMENT 'Creation time',
  `update_time` datetime NOT NULL COMMENT 'Update time',
  `create_by` bigint(20) DEFAULT NULL COMMENT 'Creator ID',
  `update_by` bigint(20) DEFAULT NULL COMMENT 'Updater ID',
  `deleted` int(1) DEFAULT 0 COMMENT 'Logical delete flag (0: not deleted, 1: deleted)',
  `version` int(11) DEFAULT 1 COMMENT 'Version number',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User table';

-- Role table
CREATE TABLE IF NOT EXISTS `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary key ID',
  `name` varchar(50) NOT NULL COMMENT 'Role name',
  `code` varchar(50) NOT NULL COMMENT 'Role code',
  `description` varchar(255) DEFAULT NULL COMMENT 'Role description',
  `create_time` datetime NOT NULL COMMENT 'Creation time',
  `update_time` datetime NOT NULL COMMENT 'Update time',
  `create_by` bigint(20) DEFAULT NULL COMMENT 'Creator ID',
  `update_by` bigint(20) DEFAULT NULL COMMENT 'Updater ID',
  `deleted` int(1) DEFAULT 0 COMMENT 'Logical delete flag (0: not deleted, 1: deleted)',
  `version` int(11) DEFAULT 1 COMMENT 'Version number',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Role table';

-- User-Role relation table
CREATE TABLE IF NOT EXISTS `user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary key ID',
  `user_id` bigint(20) NOT NULL COMMENT 'User ID',
  `role_id` bigint(20) NOT NULL COMMENT 'Role ID',
  `create_time` datetime NOT NULL COMMENT 'Creation time',
  `update_time` datetime NOT NULL COMMENT 'Update time',
  `create_by` bigint(20) DEFAULT NULL COMMENT 'Creator ID',
  `update_by` bigint(20) DEFAULT NULL COMMENT 'Updater ID',
  `deleted` int(1) DEFAULT 0 COMMENT 'Logical delete flag (0: not deleted, 1: deleted)',
  `version` int(11) DEFAULT 1 COMMENT 'Version number',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_role` (`user_id`, `role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User-Role relation table';

-- AI Model table
CREATE TABLE IF NOT EXISTS `model` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary key ID',
  `name` varchar(50) NOT NULL COMMENT 'Model name',
  `provider` varchar(50) NOT NULL COMMENT 'Model provider (e.g., OpenAI, DeepSeek)',
  `type` varchar(20) NOT NULL COMMENT 'Model type (text, image, audio)',
  `endpoint` varchar(255) DEFAULT NULL COMMENT 'API endpoint',
  `model_version` varchar(50) DEFAULT NULL COMMENT 'Model version',
  `token_limit` int(11) DEFAULT NULL COMMENT 'Token limitation',
  `avatar` varchar(255) DEFAULT NULL COMMENT 'Model avatar URL',
  `default_params` text DEFAULT NULL COMMENT 'Default parameters (JSON format)',
  `status` int(1) DEFAULT 1 COMMENT 'Status (0: disabled, 1: enabled)',
  `sort` int(11) DEFAULT 0 COMMENT 'Display order',
  `create_time` datetime NOT NULL COMMENT 'Creation time',
  `update_time` datetime NOT NULL COMMENT 'Update time',
  `create_by` bigint(20) DEFAULT NULL COMMENT 'Creator ID',
  `update_by` bigint(20) DEFAULT NULL COMMENT 'Updater ID',
  `deleted` int(1) DEFAULT 0 COMMENT 'Logical delete flag (0: not deleted, 1: deleted)',
  `version` int(11) DEFAULT 1 COMMENT 'Version number',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI Model table';

-- Conversation table
CREATE TABLE IF NOT EXISTS `conversation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary key ID',
  `title` varchar(255) NOT NULL COMMENT 'Conversation title',
  `user_id` bigint(20) NOT NULL COMMENT 'User ID',
  `model_id` bigint(20) NOT NULL COMMENT 'Model ID',
  `model_config` text DEFAULT NULL COMMENT 'Model configuration (JSON format)',
  `status` int(1) DEFAULT 1 COMMENT 'Status (0: archived, 1: active)',
  `create_time` datetime NOT NULL COMMENT 'Creation time',
  `update_time` datetime NOT NULL COMMENT 'Update time',
  `create_by` bigint(20) DEFAULT NULL COMMENT 'Creator ID',
  `update_by` bigint(20) DEFAULT NULL COMMENT 'Updater ID',
  `deleted` int(1) DEFAULT 0 COMMENT 'Logical delete flag (0: not deleted, 1: deleted)',
  `version` int(11) DEFAULT 1 COMMENT 'Version number',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Conversation table';

-- Message table
CREATE TABLE IF NOT EXISTS `message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary key ID',
  `conversation_id` bigint(20) NOT NULL COMMENT 'Conversation ID',
  `content` text NOT NULL COMMENT 'Message content',
  `role` varchar(20) NOT NULL COMMENT 'Message role (user/assistant/system)',
  `token_count` int(11) DEFAULT 0 COMMENT 'Token count',
  `type` varchar(20) DEFAULT 'text' COMMENT 'Message type (text/image/audio)',
  `parent_id` bigint(20) DEFAULT NULL COMMENT 'Parent message ID',
  `metadata` text DEFAULT NULL COMMENT 'Message metadata (JSON format)',
  `model_id` bigint(20) DEFAULT NULL COMMENT 'Model ID used for this message',
  `create_time` datetime NOT NULL COMMENT 'Creation time',
  `update_time` datetime NOT NULL COMMENT 'Update time',
  `create_by` bigint(20) DEFAULT NULL COMMENT 'Creator ID',
  `update_by` bigint(20) DEFAULT NULL COMMENT 'Updater ID',
  `deleted` int(1) DEFAULT 0 COMMENT 'Logical delete flag (0: not deleted, 1: deleted)',
  `version` int(11) DEFAULT 1 COMMENT 'Version number',
  PRIMARY KEY (`id`),
  KEY `idx_conversation_id` (`conversation_id`) USING BTREE,
  KEY `idx_parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Message table'; 