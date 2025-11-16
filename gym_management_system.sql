SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
SET GLOBAL event_scheduler = ON;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `admin_account` int NOT NULL COMMENT '管理员账号',
  `admin_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '管理员密码',
  `is_admin` boolean NULL DEFAULT 1 COMMENT '是否为管理员',
  PRIMARY KEY (`admin_account`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1001, '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 1);
INSERT INTO `admin` VALUES (1002, '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 0);
INSERT INTO `admin` VALUES (1003, '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 0);



-- ----------------------------
-- Table structure for common_site_reservation
-- ----------------------------
DROP TABLE IF EXISTS `common_site_reservation`;
CREATE TABLE `common_site_reservation`  (
  `reservation_date` date NOT NULL COMMENT '预约日期',
  `period` int NULL DEFAULT NULL COMMENT '预约时段',
  `member_account` int NOT NULL COMMENT '会员账号',
  PRIMARY KEY (`reservation_date`, `member_account`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of common_site_reservation
-- ----------------------------
INSERT INTO `common_site_reservation` VALUES ('2025-06-01', 3, 202100788);
INSERT INTO `common_site_reservation` VALUES ('2025-06-02', 2, 202132539);
INSERT INTO `common_site_reservation` VALUES ('2025-06-03', 3, 202186416);

ALTER TABLE `common_site_reservation`
ADD COLUMN `signed_in` BOOLEAN NOT NULL DEFAULT 0 COMMENT '是否已签到';


CREATE EVENT IF NOT EXISTS clear_common_site_reservation_monthly
ON SCHEDULE EVERY 1 MONTH
STARTS '2024-07-01 00:00:00'
DO
  TRUNCATE TABLE common_site_reservation;



-- ----------------------------
-- Table structure for super_site_reservation
-- ----------------------------
DROP TABLE IF EXISTS `super_site_reservation`;
CREATE TABLE `super_site_reservation`  (
  `reservation_date` date NOT NULL COMMENT '预约日期',
  `period` int NULL DEFAULT NULL COMMENT '预约时段',
  `member_account` int NOT NULL COMMENT '会员账号',
  PRIMARY KEY (`reservation_date`, `member_account`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of super_site_reservation
-- ----------------------------
INSERT INTO `super_site_reservation` VALUES ('2025-06-01', 1, 202009867);
INSERT INTO `super_site_reservation` VALUES ('2025-06-02', 2, 202100788);
INSERT INTO `super_site_reservation` VALUES ('2025-06-03', 3, 202132539);



CREATE EVENT IF NOT EXISTS clear_super_site_reservation_monthly
ON SCHEDULE EVERY 1 MONTH
STARTS '2024-07-01 00:00:00'
DO
  TRUNCATE TABLE super_site_reservation;




-- ----------------------------
-- Table structure for plan
-- ----------------------------
DROP TABLE IF EXISTS `plan`;
CREATE TABLE `plan`  (
  `member_account` int NOT NULL COMMENT '学员账号',
  `coach_account` int NOT NULL COMMENT '教练账号',
  `message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '健身计划内容',
  PRIMARY KEY (`member_account`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of plan
-- ----------------------------
INSERT INTO `plan` VALUES (202100788, 2001, '每个练胸日做平板和上斜卧推，每个动作5个正式组，每个正式组做6~8 reps');
INSERT INTO `plan` VALUES (202132539, 2002, '每个练腿日做深蹲和硬拉，每个动作5个正式组，每个正式组做6~8 reps');
INSERT INTO `plan` VALUES (202186416, 2003, '每个练背日做引体向上和俯身划船，每个动作5个正式组，每个正式组做6~8 reps');

-- ----------------------------
-- Table structure for commentary
-- ----------------------------
DROP TABLE IF EXISTS `commentary`;
CREATE TABLE `commentary` (
  `member_account` INT NOT NULL COMMENT '会员账号',
  `message` VARCHAR(255) NOT NULL COMMENT '评价内容',
  `comment_date` DATE NOT NULL COMMENT '评价日期',
  PRIMARY KEY (`member_account`, `comment_date`)
) ENGINE=InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of commentary
-- ----------------------------

INSERT INTO `commentary` (`member_account`, `message`, `comment_date`) VALUES
(202100788, '环境很好，器材齐全。', '2025-05-01'),
(202186416, '教练很专业，服务态度好。', '2025-05-02'),
(202153468, '健身房很干净，体验不错。', '2025-05-03');


-- ----------------------------
-- Table structure for classorder
-- ----------------------------
DROP TABLE IF EXISTS `course_reservation`;
CREATE TABLE `course_reservation` (
  `member_account` INT NOT NULL COMMENT '会员账号',
  `coach_account` INT NOT NULL COMMENT '教练账号',
  `reservation_date` DATE NOT NULL COMMENT '预约日期',
  `period` INT NOT NULL COMMENT '预约时段',
  PRIMARY KEY (`member_account`, `coach_account`, `reservation_date`, `period`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程预约表';

INSERT INTO `course_reservation` (`member_account`, `coach_account`, `reservation_date`, `period`) VALUES
(202100788, 2001, '2025-06-10', 1),
(202132539, 2002, '2025-06-11', 2),
(202186416, 2003, '2025-06-12', 3);

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`  (
  `member_account` int NOT NULL COMMENT '会员账号',
  `member_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi' COMMENT '会员密码',
  `member_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '会员姓名',
  `member_gender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '会员性别',
  `member_age` int NULL DEFAULT NULL COMMENT '会员年龄',
  `member_height` int NULL DEFAULT NULL COMMENT '会员身高',
  `member_weight` int NULL DEFAULT NULL COMMENT '会员体重',
  `member_phone` bigint NULL DEFAULT NULL COMMENT '会员电话',
  `card_time` date NULL DEFAULT NULL COMMENT '办卡时间',
  `card_class` int NULL DEFAULT NULL COMMENT '购买课时',
  `card_next_class` int NULL DEFAULT NULL COMMENT '剩余课时',
  `is_super` BOOLEAN NULL DEFAULT 0 COMMENT '是否为超级会员',
  PRIMARY KEY (`member_account`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of member
-- ----------------------------
INSERT INTO `member` VALUES (202009867, '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '张三', '女', 24, 182, 60, 13515548482, '2020-06-05', 40, 40, 1);
INSERT INTO `member` VALUES (202100788, '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '李四', '男', 31, 178, 60, 13131554873, '2021-01-01', 50, 50, 1);
INSERT INTO `member` VALUES (202132539, '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '王五', '男', 31, 178, 60, 13154875489, '2021-01-01', 40, 40, 1);
INSERT INTO `member` VALUES (202186416, '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '马六', '女', 23, 160, 45, 13124576857, '2021-01-16', 30, 30, 1);
INSERT INTO `member` VALUES (202106725, '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Tom', '男', 24, 178, 88, 13758784959, '2021-02-26', 30, 30, 1);
INSERT INTO `member` VALUES (202183406, '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Tylor', '女', 19, 170, 60, 13786457488,'2021-02-27', 30, 30, 0);
INSERT INTO `member` VALUES (202176587, '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Jack', '男', 33, 177, 90, 13767546666, '2021-02-27', 30, 30, 0);
INSERT INTO `member` VALUES (202156754, '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Mike', '男', 36, 166, 67, 13786532448, '2021-02-28', 30, 30, 0);
INSERT INTO `member` VALUES (202153468, '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Emma', '女', 25, 173, 44, 13786457124,  '2021-03-01', 50, 50, 0);
INSERT INTO `member` VALUES (202121345, '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Ava', '女', 28, 160, 40, 13754457488, '2021-03-02', 30, 30, 0);
INSERT INTO `member` VALUES (202189776, '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Chloe', '女', 27, 170, 50, 13986337489,  '2021-03-23', 30, 30, 0);
INSERT INTO `member` VALUES (202123664, '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Lily', '女', 25, 165, 51, 15986457423,  '2021-03-27', 30, 30, 0);


-- ----------------------------
-- Table structure for coach
-- ----------------------------
DROP TABLE IF EXISTS `coach`;
CREATE TABLE `coach`  (
  `coach_account` int NOT NULL COMMENT '教练账号',
  `coach_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi' COMMENT '教练密码',
  `coach_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '教练姓名',
  `coach_gender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '教练性别',
  `coach_age` int NULL DEFAULT NULL COMMENT '教练年龄',
  PRIMARY KEY (`coach_account`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of coach
-- ----------------------------
INSERT INTO `coach` VALUES (2001, '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '李明教练', '女', 24);
INSERT INTO `coach` VALUES (2002, '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '张华教练', '男', 31);
INSERT INTO `coach` VALUES (2003, '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '彭丽教练', '男', 31);

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`  (
  `employee_account` int NOT NULL COMMENT '员工账号',
  `employee_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '员工姓名',
  `employee_gender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '员工性别',
  `employee_age` int NULL DEFAULT NULL COMMENT '员工年龄',
  `entry_time` date NULL DEFAULT NULL COMMENT '入职时间',
  `staff` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '职务',
  `employee_message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`employee_account`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES (101038721,  '教练1', '女', 26, '2016-06-29', '健身教练', '健美冠军');
INSERT INTO `employee` VALUES (101068283,  '教练2', '男', 34, '2020-01-06', '健身教练', '职业教练');
INSERT INTO `employee` VALUES (101053687,  '教练3', '男', 30, '2020-06-06', '健身教练', '职业教练');
INSERT INTO `employee` VALUES (101045354,  '教练4', '男', 24, '2021-01-07', '健身教练', '职业教练');
INSERT INTO `employee` VALUES (101058973,  '保洁1', '女', 48, '2019-08-24', '保洁员', '模范员工');
INSERT INTO `employee` VALUES (101034208,  '保洁2', '女', 48, '2010-08-01', '保洁员', '');

-- ----------------------------
-- Table structure for equipment
-- ----------------------------
DROP TABLE IF EXISTS `equipment`;
CREATE TABLE `equipment`  (
  `equipment_id` int NOT NULL AUTO_INCREMENT COMMENT '器材id',
  `equipment_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '器材名称',
  `equipment_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '器材位置',
  `equipment_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '器材状态',
  `equipment_message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '器材备注信息',
  PRIMARY KEY (`equipment_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of equipment
-- ----------------------------
INSERT INTO `equipment` VALUES (1, '哑铃1', '1号房间', '正常', '');
INSERT INTO `equipment` VALUES (2, '杠铃1', '2号房间', '损坏', '待维修');
INSERT INTO `equipment` VALUES (3, '跑步机1', '2号房间', '维修中', '联系厂家维修');
INSERT INTO `equipment` VALUES (4, '跑步机2', '2号房间', '正常', '');
INSERT INTO `equipment` VALUES (5, '跑步机3', '2号房间', '正常', '');
INSERT INTO `equipment` VALUES (6, '杠铃1', '1号房间', '正常', '');
INSERT INTO `equipment` VALUES (7, '杠铃2', '1号房间', '正常', '');



SET FOREIGN_KEY_CHECKS = 1;
