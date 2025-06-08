package com.xingyang.chat.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Swagger URL Printer - Logs API documentation URLs on application startup
 *
 * @author XingYang
 */
@Slf4j
@Component
public class SwaggerUrlPrinter implements ApplicationRunner {

    @Value("${server.port:8080}")
    private int serverPort;
    
    @Value("${server.servlet.context-path:/api}")
    private String contextPath;
    
    @Override
    public void run(ApplicationArguments args) {
        log.info("✅ API documentation is available at:");
        log.info("📝 Knife4j URL: http://localhost:{}{}/doc.html", serverPort, contextPath);
        log.info("📘 Swagger UI URL: http://localhost:{}{}/swagger-ui/index.html", serverPort, contextPath);
        log.info("🌐 OpenAPI JSON: http://localhost:{}{}/v3/api-docs", serverPort, contextPath);
        log.info("🔍 OpenAPI YAML: http://localhost:{}{}/v3/api-docs.yaml", serverPort, contextPath);
        log.info("📋 API Groups:");
        log.info("  - All APIs: http://localhost:{}{}/swagger-ui/index.html", serverPort, contextPath);
        log.info("  - Public APIs: http://localhost:{}{}/swagger-ui/index.html?urls.primaryName=public", serverPort, contextPath);
        log.info("  - Admin APIs: http://localhost:{}{}/swagger-ui/index.html?urls.primaryName=admin", serverPort, contextPath);
        log.info("  - Chat APIs: http://localhost:{}{}/swagger-ui/index.html?urls.primaryName=chat", serverPort, contextPath);
        log.info("  - All APIs (raw): http://localhost:{}{}/swagger-ui/index.html?urls.primaryName=all-apis", serverPort, contextPath);
    }
} 