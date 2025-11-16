# 健身房管理系统

一个基于 Spring Boot 开发的健身房综合管理系统，支持管理员、会员和教练三种角色的完整业务流程管理。

## 📋 项目简介

本系统是一个功能完善的健身房管理平台，实现了会员管理、课程预约、场地预约、健身计划管理、器材管理、员工管理等核心功能。系统采用前后端分离的架构设计，使用 Thymeleaf 作为模板引擎，提供友好的用户界面。

## ✨ 主要功能

### 👨‍💼 管理员功能
- **会员管理**：会员信息的增删改查、会员账号管理
- **教练管理**：教练信息的增删改查
- **员工管理**：员工信息的增删改查
- **器材管理**：健身器材的增删改查、器材状态管理
- **课程预约管理**：查看和管理所有课程预约记录
- **场地预约管理**：管理普通场地和超级场地的预约记录
- **数据统计**：
  - 场地预约统计（按日期、时段统计）
  - 教练课程统计（按教练、日期、时段统计）
- **评价管理**：查看和删除会员评价

### 👤 会员功能
- **个人信息管理**：查看和修改个人信息
- **课程预约**：预约教练课程、查看已预约课程、取消预约
- **场地预约**：
  - 普通场地预约（所有会员）
  - 超级场地预约（仅限超级会员）
  - 场地签到功能
  - 查看预约记录、取消预约
- **健身计划查看**：查看教练为自己制定的健身计划
- **评价功能**：提交对健身房的评价、查看所有评价

### 🏋️ 教练功能
- **个人信息管理**：查看和修改个人信息
- **课程预约管理**：查看自己的课程预约、取消预约
- **健身计划管理**：
  - 为会员创建健身计划
  - 查看自己创建的所有健身计划
  - 编辑健身计划
  - 删除健身计划

## 🛠️ 技术栈

- **后端框架**：Spring Boot 2.5.3
- **持久层框架**：MyBatis 2.2.0
- **数据库**：MySQL 8.0.25
- **模板引擎**：Thymeleaf
- **安全框架**：Spring Security
- **构建工具**：Maven
- **Java 版本**：JDK 1.8

## 📦 项目结构

```
gym-management-system/
├── src/
│   ├── main/
│   │   ├── java/com/milotnt/
│   │   │   ├── controller/          # 控制器层
│   │   │   │   ├── AdminController.java
│   │   │   │   ├── UserController.java
│   │   │   │   ├── CoachController.java
│   │   │   │   └── ...
│   │   │   ├── service/            # 服务层
│   │   │   ├── mapper/             # 数据访问层
│   │   │   ├── pojo/               # 实体类
│   │   │   ├── config/             # 配置类
│   │   │   └── util/               # 工具类
│   │   └── resources/
│   │       ├── application.yml     # 应用配置
│   │       ├── mybatis/            # MyBatis 配置和映射文件
│   │       ├── templates/          # HTML 模板文件
│   │       └── static/             # 静态资源
│   └── test/                       # 测试代码
├── gym_management_system.sql       # 数据库初始化脚本
├── pom.xml                         # Maven 依赖配置
└── README.md                       # 项目说明文档
```

## 🚀 快速开始

### 环境要求

- JDK 1.8 或更高版本
- Maven 3.6+
- MySQL 8.0+
- IDE（推荐 IntelliJ IDEA 或 Eclipse）

### 安装步骤

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd gym-management-system
   ```

2. **创建数据库**
   ```sql
   CREATE DATABASE gym_management_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **导入数据库**
   ```bash
   mysql -u root -p gym_management_system < gym_management_system.sql
   ```

4. **配置数据库连接**
   
   编辑 `src/main/resources/application.yml`，修改数据库连接信息：
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/gym_management_system?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
       username: root        # 修改为你的数据库用户名
       password: CC123       # 修改为你的数据库密码
   ```

5. **编译项目**
   ```bash
   mvn clean install
   ```

6. **运行项目**
   ```bash
   mvn spring-boot:run
   ```
   
   或者直接运行主类：`com.milotnt.GymManagementSystemApplication`

7. **访问系统**
   
   打开浏览器访问：`http://localhost:8080`
   
   - 管理员登录页面：`http://localhost:8080/`
   - 会员登录页面：`http://localhost:8080/toUserLogin`
   - 教练登录页面：`http://localhost:8080/toCoachLogin`

### 默认账号

根据数据库初始化脚本，系统预设了以下测试账号（密码已加密，默认密码请查看数据库或联系管理员）：

- **管理员账号**：1001, 1002, 1003
- **会员账号**：202100788, 202132539, 202186416 等
- **教练账号**：2001, 2002, 2003

> ⚠️ **注意**：生产环境请务必修改默认密码！

## 📊 数据库设计

### 主要数据表

- `admin` - 管理员表
- `member` - 会员表
- `coach` - 教练表
- `employee` - 员工表
- `equipment` - 器材表
- `course_reservation` - 课程预约表
- `common_site_reservation` - 普通场地预约表
- `super_site_reservation` - 超级场地预约表
- `plan` - 健身计划表
- `commentary` - 评价表

### 数据库特性

- 使用 MySQL 事件调度器自动清理过期预约记录（每月自动清理）
- 支持超级会员特权（`is_super` 字段）
- 场地预约支持签到功能（`signed_in` 字段）

## 🔧 配置说明

### 端口配置

默认端口为 8080，如需修改，在 `application.yml` 中取消注释并修改：

```yaml
server:
  port: 8888
```

### Thymeleaf 配置

开发环境下已禁用缓存，便于调试：

```yaml
spring:
  thymeleaf:
    cache: false
```

生产环境建议设置为 `true` 以提升性能。

## 📝 功能特性说明

### 场地预约规则

- **普通场地**：所有会员均可预约
- **超级场地**：仅限超级会员（`is_super = 1`）预约
- **预约限制**：
  - 每个时段最多 2 人预约
  - 每个会员每天只能预约一次
  - 可提前 3 天预约

### 课程预约规则

- 可提前 7 天预约
- 每个时段每个教练只能被一个会员预约
- 会员不能重复预约同一时段的课程

### 签到功能

- 会员可在预约的时段进行签到
- 系统根据当前时间和预约时段自动判断可签到记录
- 时段划分：
  - 时段 1：上午（9:00-11:00）
  - 时段 2：下午（15:00-17:00）
  - 时段 3：晚上（19:00-21:00）

## 🧪 测试

运行测试：

```bash
mvn test
```

## 📄 许可证

本项目采用 MIT 许可证，详见 [LICENSE](LICENSE) 文件。

## 👥 贡献

欢迎提交 Issue 和 Pull Request！

## 📮 联系方式

如有问题或建议，请通过 Issue 反馈。

---

**开发时间**：2025年 9月 
**最后更新**：2025年 11月

