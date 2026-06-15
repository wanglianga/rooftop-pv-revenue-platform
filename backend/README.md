# PV Platform Backend

基于 Spring Boot 3.2 的光伏平台后端项目骨架。

## 原始需求

> 在 d:\code\solocoder-0608-wl\wl-328\backend 目录下创建完整的 Spring Boot 3.2 项目核心结构：
> 
> 1. 创建 pom.xml，包含依赖：spring-boot-starter-web, spring-boot-starter-data-jpa, spring-boot-starter-validation, mysql-connector-java, lombok, springfox-boot-starter(或springdoc-openapi-starter-webmvc-ui)
> 2. 创建 application.yml，配置 MySQL (端口3306, 数据库 pv_platform, 用户名root, 密码123456), 服务端口8080
> 3. 创建主启动类 PvPlatformApplication.java 在 com.pvplatform 包下
> 4. 创建 WebConfig.java 配置跨域
> 5. 创建 Result.java 统一响应类
> 6. 创建 GlobalExceptionHandler.java 全局异常处理
> 
> 返回创建的文件列表和核心内容。

> 在 d:\code\solocoder-0608-wl\wl-328\backend\src\main\java\com\pvplatform\service 目录下创建以下 Service 类（不带 Lombok）：
> 
> ### 核心 Service 类：
> 1. **SysUserService** - 用户服务：
>    - login(username, password): 登录验证，返回用户信息
>    - listUsers(): 获取所有用户
>    - getUserById(id): 根据ID获取用户
>    - saveUser(user): 保存/更新用户
>    - deleteUser(id): 删除用户
> 
> 2. **RoofAreaService** - 屋面区域服务：
>    - listRoofAreas(): 获取所有屋面区域
>    - getRoofAreaById(id): 根据ID获取
>    - saveRoofArea(roofArea): 保存/更新
>    - deleteRoofArea(id): 删除
> 
> 3. **PvStringService** - 组件串服务：
>    - listByRoofAreaId(roofAreaId): 按屋面区域查询
>    - savePvString(pvString): 保存/更新
>    - deletePvString(id): 删除
> 
> 4. **InverterService** - 逆变器服务：
>    - listByRoofAreaId(roofAreaId): 按屋面区域查询
>    - saveInverter(inverter): 保存/更新
>    - deleteInverter(id): 删除
> 
> 5. **GridApplicationService** - 并网申请服务：
>    - listByStatus(status): 按状态查询
>    - submitApplication(application): 提交申请
>    - auditApplication(id, status, opinion, feedbackBy): 供电公司审核
>    - setGridNo(id, gridNo): 设置并网编号
> 
> 6. **MeterReadingService** - 电表读数服务：
>    - listByInverterId(inverterId): 按逆变器查询
>    - saveMeterReading(reading): 保存读数
>    - getMonthlySummary(year, month): 月度汇总
> 
> 7. **PowerCurveService** - 发电曲线服务：
>    - listByInverterIdAndDate(inverterId, date): 按逆变器和日期查询
>    - savePowerCurve(curve): 保存曲线数据
>    - getDailyCurve(inverterId, date): 获取日发电曲线
> 
> 8. **WarrantyService** - 质保信息服务：
>    - listByDevice(deviceType, deviceId): 按设备查询
>    - saveWarranty(warranty): 保存质保
>    - getExpiringWarranties(days): 获取即将到期的质保
> 
> 9. **RevenueAllocationService** - 收益分配服务：
>    - calculateAllocation(period, rule): 计算收益分配
>    - listByPeriod(period): 按期间查询
>    - listByOwnerId(ownerId): 按业主查询
>    - saveAllocation(allocation): 保存分配记录
> 
> 10. **VoteTopicService** - 业委员会议题服务：
>     - createTopic(topic): 创建议题
>     - listTopics(): 获取所有议题
>     - publishTopic(id): 发布议题
>     - closeTopic(id): 关闭议题
> 
> 11. **VoteRecordService** - 表决记录服务：
>     - vote(topicId, ownerId, result, remark): 提交表决
>     - listByTopicId(topicId): 按议题查询
>     - getVoteResult(topicId): 统计表决结果
> 
> 12. **OwnerNoticeService** - 业主告知服务：
>     - publishNotice(notice): 发布告知
>     - listNotices(): 获取所有告知
>     - listByTargetAudience(audience): 按受众查询
> 
> 13. **ConstructionRecordService** - 施工记录服务：
>     - listByRoofAreaId(roofAreaId): 按屋面区域查询
>     - saveRecord(record): 保存施工记录
>     - listByStage(stage): 按施工阶段查询
> 
> 所有 Service 使用 @Service 注解，注入对应的 Repository，手动实现 getter/setter。返回创建的文件列表。

## 技术栈

- **框架**: Spring Boot 3.2.0
- **JDK**: Java 17
- **ORM**: Spring Data JPA + Hibernate
- **数据库**: MySQL 8.0
- **构建工具**: Maven 3.9+
- **API 文档**: SpringDoc OpenAPI 2.3.0 (Swagger UI)
- **简化代码**: Lombok
- **参数校验**: Jakarta Validation (Hibernate Validator)

## 目录结构

```
backend/
├── src/main/java/com/pvplatform/
│   ├── PvPlatformApplication.java      # 主启动类
│   ├── common/
│   │   └── Result.java                 # 统一响应类
│   ├── config/
│   │   └── WebConfig.java              # 跨域配置
│   └── exception/
│       └── GlobalExceptionHandler.java # 全局异常处理
├── src/main/resources/
│   └── application.yml                 # 应用配置
├── Dockerfile
├── docker-compose.yml
├── .dockerignore
├── pom.xml
└── README.md
```

## 启动方式

### 前置要求

- **JDK**: 17 或更高版本
- **Maven**: 3.9 或更高版本
- **MySQL**: 8.0 或更高版本（本地启动或 Docker 启动）
- **Docker & Docker Compose**（Docker 启动方式需要）

---

### Docker 一键启动（推荐）

#### 1. 一键启动所有服务

```bash
docker compose up --build
```

如需后台运行：

```bash
docker compose up --build -d
```

#### 2. 停止并清理服务

```bash
docker compose down
```

#### 3. 查看服务日志

```bash
docker compose logs -f
```

---

### 本地启动方式

#### 1. 安装依赖并构建

```bash
mvn clean install -DskipTests
```

#### 2. 启动应用

```bash
mvn spring-boot:run
```

或直接运行 jar 包：

```bash
java -jar target/pv-platform-1.0.0.jar
```

#### 3. 访问地址

- 应用接口：http://localhost:8080
- Swagger UI 文档：http://localhost:8080/swagger-ui.html
- API 文档 JSON：http://localhost:8080/api-docs

---

### MySQL 配置说明

本地启动前请确保 MySQL 已启动，并创建数据库：

```sql
CREATE DATABASE pv_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

## 核心文件说明

### 1. PvPlatformApplication.java
Spring Boot 主启动类，位于 `com.pvplatform` 根包下。

### 2. WebConfig.java
全局跨域配置，允许所有来源的 GET、POST、PUT、DELETE、OPTIONS 请求，支持携带凭证。

### 3. Result.java
统一响应格式，包含：
- `code`: 状态码（200 成功，500 失败，400 参数错误）
- `message`: 提示信息
- `data`: 响应数据

提供静态方法 `success()`、`success(data)`、`error(message)` 等快速创建响应对象。

### 4. GlobalExceptionHandler.java
全局异常处理器，统一处理以下异常：
- 系统异常（Exception）
- 参数校验失败（MethodArgumentNotValidException）
- 参数绑定失败（BindException）
- 约束校验失败（ConstraintViolationException）
- 非法参数异常（IllegalArgumentException）

所有异常都返回统一的 `Result` 格式响应。

## 注意事项

1. 本地启动时请确保 MySQL 服务已启动，数据库 `pv_platform` 已创建
2. JPA 配置为 `ddl-auto: update`，会自动根据实体类更新表结构
3. 生产环境建议关闭 `show-sql` 和 `format_sql`
4. Docker 启动方式会自动拉起 MySQL 容器并初始化数据库
