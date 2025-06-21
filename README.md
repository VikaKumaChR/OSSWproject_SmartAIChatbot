# AI Chat Assistant Backend

A robust Spring Boot application that serves as the backend for the AI Chat Assistant platform. This project provides RESTful APIs for user authentication, conversation management, and AI model integration.

## 🚀 Technologies

- **Framework**: [Spring Boot](https://spring.io/projects/spring-boot) 2.7.x
- **Language**: Java 17
- **Database**: MySQL 8.0
- **ORM**: [MyBatis-Plus](https://baomidou.com/) 3.5.x
- **Authentication**: Spring Security with JWT
- **Cache**: Redis
- **Message Queue**: RabbitMQ
- **API Documentation**: Knife4j (Swagger)
- **File Storage**: MinIO
- **AI Integration**: LangChain4j with OpenAI/Alibaba DashScope
- **Email Service**: Spring Mail

## 📋 Features

- User authentication and authorization with JWT
- Email verification and password reset
- User profile management
- Conversation and message management
- Real-time chat with AI models
- File upload and management
- Admin dashboard and user management
- API documentation with Swagger/Knife4j
- Comprehensive logging and exception handling

## 🛠️ Project Setup

### Prerequisites

- JDK 17
- Maven 3.6+
- MySQL 8.0+
- Redis
- RabbitMQ
- MinIO (optional, for file storage)

### Database Setup

1. Create a MySQL database named `hd_chat`
2. The application will automatically create tables on startup using the SQL scripts in `src/main/resources/db`

### Configuration

Create an `application-dev.yml` file in the `src/main/resources` directory with your local configuration:

```yaml
spring:
  datasource:
    url: jdbc:mysql://45.207.192.41:3306/hd_chat?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
  
  redis:
    host: 45.207.192.41
    port: 6379
    password: your_redis_password
    
  rabbitmq:
    host: 45.207.192.41
    port: 5672
    username: guest
    password: guest
    
  mail:
    host: smtp.example.com
    port: 587
    username: your_email@example.com
    password: your_email_password
    
minio:
  endpoint: http://45.207.192.41:9000
  accessKey: your_access_key
  secretKey: your_secret_key
  bucketName: ai-pan
  avatarBucketName: avatar
  
ai:
  model:
    api-key: your_api_key
    model-id: your_model_id
    endpoint: your_endpoint
```

### Building and Running

```bash
# Clone the repository
git clone https://github.com/VikaKumaChR/OSSWproject_SmartAIChatbot.git
cd ai-chat-assistant-backend

# Build the project
mvn clean package -DskipTests

# Run the application
java -jar target/hd-chat-0.0.1-SNAPSHOT.jar
```

Alternatively, you can run it directly with Maven:

```bash
mvn spring-boot:run
```

### Docker Deployment

A Dockerfile is provided for containerization:

```bash
# Build the Docker image
docker build -t ai-chat-backend .

# Run the container
docker run -p 8087:8087 ai-chat-backend
```

## 📁 Project Structure

```
src/main/
├── java/com/xingyang/chat/
│   ├── config/           # Configuration classes
│   ├── controller/       # REST API controllers
│   ├── exception/        # Exception handling
│   ├── mapper/           # MyBatis mappers
│   ├── model/            # Data models (entities, DTOs)
│   ├── security/         # Security configuration and JWT
│   ├── service/          # Business logic
│   ├── util/             # Utility classes
│   └── ChatApplication.java  # Main application class
│
└── resources/
    ├── db/               # Database scripts
    ├── mapper/           # MyBatis XML mapper files
    ├── static/           # Static resources
    ├── templates/        # Email templates
    ├── application.yml   # Main configuration
    └── application-prod.yml # Production configuration
```

## 🔒 Authentication

The application uses JWT (JSON Web Token) for authentication. The authentication flow is as follows:

1. User registers with email, username, and password
2. Email verification is sent to the user
3. User verifies email and can then log in
4. Upon successful login, a JWT token is issued
5. The token must be included in the `Authorization` header for protected endpoints

## 🌐 API Documentation

API documentation is available through Swagger UI at:

```
http://localhost:8087/api/swagger-ui/index.html
```

This provides a comprehensive interface to explore and test all available endpoints.

## 🧪 Testing

```bash
# Run tests
mvn test

# Run tests with coverage report
mvn test jacoco:report
```

## 🚢 Deployment

### Production Configuration

For production deployment, update the `application-prod.yml` file with appropriate settings for your production environment.

### Security Considerations

1. Change all default passwords and secrets
2. Enable HTTPS in production
3. Configure proper CORS settings
4. Review and adjust rate limiting settings

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request
