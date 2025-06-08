USE hd_chat;

-- Insert default admin user (password: admin123, bcrypted)
INSERT INTO `user` (username, password, nickname, avatar, email, status, create_time, update_time)
VALUES ('admin', '$2a$10$JI5xYYgsBOtYQHzVnN1oG.rwPCbsQ6HI.p4W8RvDgNVK5LpVpZJrW', 'Admin', 'https://example.com/avatar.jpg', 'admin@example.com', 1, NOW(), NOW());

-- Insert roles
INSERT INTO `role` (name, code, description, create_time, update_time)
VALUES ('Administrator', 'ADMIN', 'System administrator', NOW(), NOW());

INSERT INTO `role` (name, code, description, create_time, update_time)
VALUES ('User', 'USER', 'Regular user', NOW(), NOW());

-- Assign admin role to admin user
INSERT INTO `user_role` (user_id, role_id, create_time, update_time)
VALUES (1, 1, NOW(), NOW());

-- Insert AI models
INSERT INTO `model` (name, provider, type, endpoint, model_version, token_limit, avatar, default_params, status, sort, create_time, update_time)
VALUES ('DeepSeek Chat', 'DeepSeek', 'text', 'api/v1/chat/completions', 'v1', 8192, 'https://example.com/deepseek.png', 
        '{"temperature": 0.7, "top_p": 0.9, "max_tokens": 2048}', 1, 1, NOW(), NOW());

INSERT INTO `model` (name, provider, type, endpoint, model_version, token_limit, avatar, default_params, status, sort, create_time, update_time)
VALUES ('DeepSeek Coder', 'DeepSeek', 'text', 'api/v1/chat/completions', 'v1', 8192, 'https://example.com/deepseek-coder.png', 
        '{"temperature": 0.5, "top_p": 0.95, "max_tokens": 4096}', 1, 2, NOW(), NOW());

INSERT INTO `model` (name, provider, type, endpoint, model_version, token_limit, avatar, default_params, status, sort, create_time, update_time)
VALUES ('OpenAI GPT-4', 'OpenAI', 'text', 'v1/chat/completions', 'gpt-4', 8192, 'https://example.com/openai.png', 
        '{"temperature": 0.7, "top_p": 1, "max_tokens": 2048}', 1, 3, NOW(), NOW());

-- Insert sample conversation
INSERT INTO `conversation` (title, user_id, model_id, model_config, status, create_time, update_time, create_by)
VALUES ('Introduction Chat', 1, 1, '{"temperature": 0.7}', 1, NOW(), NOW(), 1);

-- Insert sample messages
INSERT INTO `message` (conversation_id, content, role, token_count, type, create_time, update_time, create_by)
VALUES (1, 'Hello, can you introduce yourself?', 'user', 10, 'text', NOW(), NOW(), 1);

INSERT INTO `message` (conversation_id, content, role, token_count, type, parent_id, create_time, update_time, create_by)
VALUES (1, 'Hello! I am DeepSeek, an advanced AI assistant designed to provide helpful, harmless, and honest responses to your questions and requests. I can assist with a wide range of tasks including answering questions, generating creative content, providing information, and having thoughtful conversations. How can I help you today?', 'assistant', 62, 'text', 1, NOW(), NOW(), 1); 