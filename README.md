# 光伏平台 (PV Platform)

基于 Spring Boot + Vue 3 的分布式光伏管理平台。

## 原始需求

> 请实现屋面光伏并网与业主收益平台，Vue3 页面给业委会、光伏施工方、供电公司、物业财务和运维人员使用，Spring Boot 接口保存屋面区域、组件串、逆变器、并网申请、电表读数、发电曲线、质保信息和收益分配。业委会发起屋面使用议题、表决规则和业主告知；施工方记录勘测、支架安装、接线、调试和并网整改；供电公司反馈验收意见、并网编号和抄表口径；物业财务按楼栋、面积或约定规则分配公共收益。这个产品要让公共屋面的建设、发电、检修和收益分摊都有证据，业主看到的不只是一个总收益数字，还能知道哪台设备影响了哪段入账。
> 新增发电量预测与收益预结算：系统基于历史发电曲线、天气预报（多云/晴天/雨天）和组件衰减系数，自动预测未来7天每日发电量，并生成对应的收益预结算单供物业财务参考；预结算单需标注预测依据（引用历史同期数据和未来天气数据），当实际发电量与预测值偏差超过15%时，系统自动标记该时段为"发电异常窗口"并推送至运维人员排查；运维人员完成排查后需填写偏差原因（如天气突变、设备故障、遮挡新增等），系统将原因关联至收益预结算单的修正记录中，供后续收益争议调取使用。

## 技术栈

### 后端
- **框架**: Spring Boot 3.4.0
- **JDK**: Java 21
- **ORM**: Spring Data JPA + Hibernate
- **数据库**: MySQL 8.0
- **构建工具**: Maven 3.9+
- **API 文档**: SpringDoc OpenAPI 2.7.0 (Swagger UI)

### 前端
- **框架**: Vue 3.4.0
- **构建工具**: Vite 5.0.0
- **UI 组件**: Element Plus 2.4.4
- **路由**: Vue Router 4.2.5
- **状态管理**: Pinia 2.1.7
- **HTTP 客户端**: Axios 1.6.2
- **图表**: ECharts 5.4.3

## 目录结构

```
wl-328/
├── backend/                          # 后端项目
│   ├── src/main/java/com/pvplatform/
│   │   ├── PvPlatformApplication.java
│   │   ├── common/                   # 通用模块
│   │   ├── config/                   # 配置模块
│   │   ├── controller/               # 控制器层
│   │   ├── entity/                   # 实体类
│   │   ├── exception/                # 异常处理
│   │   ├── repository/               # 数据访问层
│   │   └── service/                  # 业务逻辑层
│   ├── src/main/resources/
│   │   └── application.yml
│   ├── Dockerfile
│   ├── docker-compose.yml
│   ├── .dockerignore
│   ├── pom.xml
│   └── README.md
├── frontend/                         # 前端项目
│   ├── src/
│   │   ├── router/
│   │   ├── stores/
│   │   ├── utils/
│   │   ├── views/
│   │   ├── App.vue
│   │   └── main.js
│   ├── Dockerfile
│   ├── .dockerignore
│   ├── nginx.conf
│   ├── package.json
│   └── vite.config.js
├── Dockerfile                        # 根目录Dockerfile（默认构建后端）
├── docker-compose.yml                # 根目录编排文件
├── .dockerignore
└── README.md
```

## 启动方式

### 前置要求

- **JDK**: 17 或更高版本
- **Maven**: 3.9 或更高版本
- **Node.js**: 18 或更高版本
- **MySQL**: 8.0 或更高版本（Docker 启动方式自动拉起）
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

#### 4. 访问地址

- 前端页面: http://localhost
- 后端接口: http://localhost:8080
- Swagger UI 文档: http://localhost:8080/swagger-ui.html
- API 文档 JSON: http://localhost:8080/api-docs

---

### 本地启动方式

#### 后端启动

##### 1. 安装依赖并构建

```bash
cd backend
mvn clean install -DskipTests
```

##### 2. 启动应用

```bash
mvn spring-boot:run
```

或直接运行 jar 包：

```bash
java -jar target/pv-platform-1.0.0.jar
```

#### 前端启动

##### 1. 安装依赖

```bash
cd frontend
npm install
```

##### 2. 启动开发服务器

```bash
npm run dev
```

##### 3. 访问地址

- 前端页面: http://localhost:5173
- 后端接口: http://localhost:8080
- Swagger UI 文档: http://localhost:8080/swagger-ui.html

---

### MySQL 配置说明

本地启动前请确保 MySQL 已启动，并创建数据库：

```sql
CREATE DATABASE pv_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

## 核心 Service 说明

### 1. SysUserService - 用户服务
- `login(username, password)`: 用户登录验证
- `listUsers()`: 获取所有用户列表
- `getUserById(id)`: 根据ID查询用户
- `saveUser(user)`: 保存或更新用户
- `deleteUser(id)`: 删除用户

### 2. RoofAreaService - 屋面区域服务
- `listRoofAreas()`: 获取所有屋面区域
- `getRoofAreaById(id)`: 根据ID查询屋面区域
- `saveRoofArea(roofArea)`: 保存或更新屋面区域
- `deleteRoofArea(id)`: 删除屋面区域

### 3. PvStringService - 组件串服务
- `listByRoofAreaId(roofAreaId)`: 按屋面区域查询组件串
- `savePvString(pvString)`: 保存或更新组件串
- `deletePvString(id)`: 删除组件串

### 4. InverterService - 逆变器服务
- `listByRoofAreaId(roofAreaId)`: 按屋面区域查询逆变器
- `saveInverter(inverter)`: 保存或更新逆变器
- `deleteInverter(id)`: 删除逆变器

### 5. GridApplicationService - 并网申请服务
- `listByStatus(status)`: 按状态查询并网申请
- `submitApplication(application)`: 提交并网申请
- `auditApplication(id, status, opinion, feedbackBy)`: 供电公司审核
- `setGridNo(id, gridNo)`: 设置并网编号

### 6. MeterReadingService - 电表读数服务
- `listByInverterId(inverterId)`: 按逆变器查询电表读数
- `saveMeterReading(reading)`: 保存电表读数
- `getMonthlySummary(year, month)`: 获取月度发电汇总

### 7. PowerCurveService - 发电曲线服务
- `listByInverterIdAndDate(inverterId, date)`: 按逆变器和日期查询发电曲线
- `savePowerCurve(curve)`: 保存发电曲线数据
- `getDailyCurve(inverterId, date)`: 获取日发电曲线

### 8. WarrantyService - 质保信息服务
- `listByDevice(deviceType, deviceId)`: 按设备查询质保信息
- `saveWarranty(warranty)`: 保存质保信息
- `getExpiringWarranties(days)`: 获取即将到期的质保

### 9. RevenueAllocationService - 收益分配服务
- `calculateAllocation(period, rule)`: 计算收益分配
- `listByPeriod(period)`: 按期间查询分配记录
- `listByOwnerId(ownerId)`: 按业主查询分配记录
- `saveAllocation(allocation)`: 保存分配记录

### 10. VoteTopicService - 业委员会议题服务
- `createTopic(topic)`: 创建表决议题
- `listTopics()`: 获取所有议题
- `publishTopic(id)`: 发布议题
- `closeTopic(id)`: 关闭议题

### 11. VoteRecordService - 表决记录服务
- `vote(topicId, ownerId, result, remark)`: 提交表决
- `listByTopicId(topicId)`: 按议题查询表决记录
- `getVoteResult(topicId)`: 统计表决结果

### 12. OwnerNoticeService - 业主告知服务
- `publishNotice(notice)`: 发布业主告知
- `listNotices()`: 获取所有告知
- `listByTargetAudience(audience)`: 按受众查询告知

### 14. ConstructionRecordService - 施工记录服务
- `listByRoofAreaId(roofAreaId)`: 按屋面区域查询施工记录
- `saveRecord(record)`: 保存施工记录
- `listByStage(stage)`: 按施工阶段查询记录

## 注意事项

1. 本地启动时请确保 MySQL 服务已启动，数据库 `pv_platform` 已创建
2. JPA 配置为 `ddl-auto: update`，会自动根据实体类更新表结构
3. 生产环境建议关闭 `show-sql` 和 `format_sql`
4. Docker 启动方式会自动拉起 MySQL 容器并初始化数据库
5. 所有 Service 类均未使用 Lombok，手动实现了 getter/setter 方法
6. Service 依赖注入采用 setter 方式，配合 `@Autowired` 注解
