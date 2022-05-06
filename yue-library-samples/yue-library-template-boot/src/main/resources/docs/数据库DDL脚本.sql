/*
 Navicat Premium Data Transfer

 Source Server         : yue-test
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : ylyue.cn:3306
 Source Schema         : yue_library_template_boot

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 21/07/2021 13:44:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for table_example
-- ----------------------------
DROP TABLE IF EXISTS `table_example`;
CREATE TABLE `table_example`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '有序主键：单表时数据库自增，分布式时雪花自增',
  `bkey` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务主键（如：UUID、身份证自然主键）',
  `sort_idx` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序索引',
  `create_user_id` bigint UNSIGNED NOT NULL COMMENT '创建用户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据插入时间',
  `update_user_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '修改用户ID',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据更新时间',
  `delete_user_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '删除用户ID',
  `delete_time` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '数据删除时间戳：默认为0，未删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_bkey`(`bkey`) USING BTREE COMMENT '唯一业务键约束'
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '建表规范示例：提供基础字段规范' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of table_example
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '有序主键：单表时数据库自增，分布式时雪花自增',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据插入时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据更新时间',
  `delete_time` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '数据删除时间戳：默认为0，未删除',
  `cellphone` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户手机号码',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户密码',
  `nickname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `head_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像',
  `birthday` date NULL DEFAULT NULL COMMENT '用户生日',
  `sex` enum('man','girl') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户性别：man 男，girl 女',
  `user_status` enum('normal','frozen','del') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'normal' COMMENT '用户状态：normal 正常，frozen 冻结，del 删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_cellphone`(`cellphone`) USING BTREE COMMENT '唯一约束'
) ENGINE = InnoDB AUTO_INCREMENT = 185 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户-CRUD示例' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (10, '2018-08-07 02:30:48', '2021-07-21 11:59:00', 0, '18523146310', 'uyJPQmQgEaVE99McVoV2zg==', '张三丰', NULL, 'ss.png', NULL, 'man', 'normal');
INSERT INTO `user` VALUES (18, '2018-07-18 09:10:46', '2021-07-21 11:59:00', 0, '18523146311', '8uXhwp4c3mYwizx/FKfY6wQRltvBd6Oc0qZvYZa+nfQ=', '测试', NULL, 'http://www.baidu.com', NULL, 'man', 'normal');
INSERT INTO `user` VALUES (21, '2018-07-26 07:25:56', '2021-07-21 11:59:00', 0, '18523146313', '2eZzUNIsix73h9MmhbmVow==', '张双双', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (23, '2018-07-30 16:17:45', '2021-07-21 11:59:00', 0, '18523146314', '2eZzUNIsix73h9MmhbmVow==', '白俊毅', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (24, '2018-07-30 16:20:04', '2021-07-21 11:59:00', 0, '18523146315', '2eZzUNIsix73h9MmhbmVow==', '测试测试', NULL, 'http://pcil57ri4.bkt.clouddn.com/FgP9TmgLFWztHHGfFyE9EQ1gaSiG', NULL, 'man', 'normal');
INSERT INTO `user` VALUES (26, '2018-08-01 06:48:42', '2021-07-21 11:59:00', 0, '18523146317', '2eZzUNIsix73h9MmhbmVow==', '胡令', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (28, '2018-08-07 02:02:42', '2021-07-21 11:59:00', 0, '18523146318', '2eZzUNIsix73h9MmhbmVow==', '18523146366', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (34, '2018-08-07 02:32:14', '2021-07-21 11:59:00', 0, '18523146319', '2eZzUNIsix73h9MmhbmVow==', '18723126410', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (35, '2018-08-07 02:38:57', '2021-07-21 11:59:00', 0, '18523146320', '2eZzUNIsix73h9MmhbmVow==', '123456', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (36, '2018-08-16 10:03:30', '2021-07-21 11:59:00', 0, '18523146321', '2eZzUNIsix73h9MmhbmVow==', '18223357996', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (37, '2018-09-04 07:57:53', '2021-07-21 11:59:00', 0, '18523146322', '2eZzUNIsix73h9MmhbmVow==', '1', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (38, '2018-09-04 07:58:18', '2021-07-21 11:59:00', 0, '18523146323', '2eZzUNIsix73h9MmhbmVow==', '1', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (39, '2018-09-04 07:58:21', '2021-07-21 11:59:01', 0, '18523146324', '2eZzUNIsix73h9MmhbmVow==', '1', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (40, '2018-09-04 08:01:23', '2021-07-21 11:59:01', 0, '18523146325', '2eZzUNIsix73h9MmhbmVow==', '1', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (61, '2018-09-20 02:57:21', '2021-07-21 11:59:01', 0, '18523146326', '2eZzUNIsix73h9MmhbmVow==', '行走的CD', NULL, 'http://pcil57ri4.bkt.clouddn.com/FgX8QWRoA6ySMNG3xE01aHg_szWY', NULL, 'man', 'normal');
INSERT INTO `user` VALUES (65, '2019-04-11 14:56:39', '2021-07-21 11:59:01', 0, '18523146327', '2eZzUNIsix73h9MmhbmVow==', '测试', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (66, '2019-04-26 15:05:29', '2021-07-21 11:59:01', 0, '18523146328', '2eZzUNIsix73h9MmhbmVow==', '1', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (68, '2019-04-26 15:43:39', '2021-07-21 11:59:01', 0, '18523146329', '2eZzUNIsix73h9MmhbmVow==', '1', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (69, '2019-04-26 17:15:30', '2021-07-21 11:59:01', 0, '18523146330', '2eZzUNIsix73h9MmhbmVow==', '18523146361', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (70, '2019-04-29 11:06:31', '2021-07-21 11:59:01', 0, '18523146331', '2eZzUNIsix73h9MmhbmVow==', '15215044536', '402848460@qq.com', NULL, NULL, 'man', 'normal');
INSERT INTO `user` VALUES (76, '2019-05-06 17:25:52', '2021-07-21 11:59:01', 0, '18523146332', '2eZzUNIsix73h9MmhbmVow==', '15736535451', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (77, '2019-05-06 17:31:10', '2021-07-21 11:59:01', 0, '18523146333', '2eZzUNIsix73h9MmhbmVow==', '15736535441', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (78, '2019-05-06 18:21:15', '2021-07-21 11:59:01', 0, '18523146334', '2eZzUNIsix73h9MmhbmVow==', '15736565656', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (79, '2019-05-29 14:33:34', '2021-07-21 11:59:01', 0, '18523146335', '2eZzUNIsix73h9MmhbmVow==', '晨曦', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (80, '2019-06-05 17:45:06', '2021-07-21 11:59:02', 0, '18523146336', '2eZzUNIsix73h9MmhbmVow==', '水帘洞', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (81, '2019-06-06 10:02:20', '2021-07-21 11:59:02', 0, '18523146337', '2eZzUNIsix73h9MmhbmVow==', '水帘洞', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (95, '2019-07-02 10:12:31', '2021-07-21 11:59:02', 0, '15123555303', NULL, '某某', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (97, '2019-07-02 15:35:52', '2021-07-21 11:59:02', 0, '18523146316', 'uyJPQmQgEaVE99McVoV2zg==', '18523146316', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (134, '2019-07-04 10:06:46', '2021-07-21 11:59:02', 0, '15723156255', 'v0/S0Hi4OZFgycPaQ0JFPg==', 'abcd', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (165, '2019-07-10 18:32:49', '2021-07-21 11:59:02', 0, '18983749649', 'dtMGSWev/NQ3dvVaFNnLzw==', '穆漪梅', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (170, '2019-07-18 16:39:41', '2021-07-21 11:59:02', 0, '19923293135', 'uyJPQmQgEaVE99McVoV2zg==', 'abcd', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (174, '2019-07-24 09:31:11', '2021-07-21 11:59:02', 0, '18883877079', 'uyJPQmQgEaVE99McVoV2zg==', 'abcd', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (175, '2019-07-24 15:59:31', '2021-07-21 11:59:02', 0, '18716280028', 'uyJPQmQgEaVE99McVoV2zg==', 'abcd', NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (181, '2019-07-26 17:05:00', '2021-07-21 11:59:02', 0, '18024649918', 'uyJPQmQgEaVE99McVoV2zg==', NULL, NULL, NULL, NULL, NULL, 'normal');
INSERT INTO `user` VALUES (184, '2019-10-12 23:49:02', '2021-07-21 11:59:02', 0, '18523146312', '2eZzUNIsix73h9MmhbmVow==', 'ylyue', 'yl-yue@qq.com', 'https://dcloud.ylyue.cn/yue-library/_images/logo.png', '1996-05-15', 'man', 'normal');

SET FOREIGN_KEY_CHECKS = 1;
