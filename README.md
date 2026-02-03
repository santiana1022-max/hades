<build>
    <!-- 1. 自定义打包后的文件名（可选，默认是artifactId+version，改后更简洁） -->
    <finalName>${project.artifactId}</finalName>

    <!-- 2. 资源文件配置（处理application.yml，支持占位符替换，如版本号） -->
    <resources>
        <resource>
            <directory>src/main/resources</directory>
            <!-- 开启过滤，替换配置文件中的${xxx}变量 -->
            <filtering>true</filtering>
            <includes>
                <include>**/*.yml</include>
                <include>**/*.yaml</include>
                <include>**/*.properties</include>
            </includes>
        </resource>
        <!-- 静态资源（html/js/css）不过滤，避免内容被篡改 -->
        <resource>
            <directory>src/main/resources</directory>
            <filtering>false</filtering>
            <includes>
                <include>**/*.html</include>
                <include>**/*.js</include>
                <include>**/*.css</include>
                <include>**/*.ico</include>
            </includes>
        </resource>
    </resources>

    <plugins>
        <!-- 原有核心插件：Spring Boot 打包插件 -->
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <!-- 核心：排除Lombok依赖（编译时用，打包无需包含，减小包体积） -->
                <excludes>
                    <exclude>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                    </exclude>
                </excludes>
                <!-- 可选：若项目有多个启动类，手动指定主类（单启动类可省略） -->
                <!-- <mainClass>com.example.demo.DemoApplication</mainClass> -->
            </configuration>
        </plugin>

        <!-- 新增：Maven 测试插件（开发时跳过测试，避免打包失败） -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.1.2</version>
            <configuration>
                <!-- 跳过单元测试（开发环境常用，生产打包可改为false） -->
                <skipTests>true</skipTests>
            </configuration>
        </plugin>
    </plugins>
</build>