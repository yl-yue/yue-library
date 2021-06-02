/*
 Navicat Premium Data Transfer

 Source Server         : yue-mdp
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : 192.168.3.51:3310
 Source Schema         : yue_library_template_simple

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 17/02/2020 22:13:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for table_example
-- ----------------------------
DROP TABLE IF EXISTS `table_example`;
CREATE TABLE `table_example`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键，单表时自增',
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户ID',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '数据插入时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE COMMENT '用户ID索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '表-示例' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of table_example
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键，单表时自增',
  `role` enum('b2b_买家','b2b_卖家','b2c_买家') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户所属角色',
  `cellphone` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户手机号码',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `head_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像',
  `sex` enum('男','女') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户性别',
  `birthday` date NULL DEFAULT NULL COMMENT '用户生日',
  `user_status` enum('正常','冻结','删除') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '正常' COMMENT '用户状态',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '数据插入时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_cellphone_role`(`cellphone`, `role`) USING BTREE COMMENT '唯一约束'
) ENGINE = InnoDB AUTO_INCREMENT = 185 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户-基础信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (10, 'b2b_买家', '18523146310', 'uyJPQmQgEaVE99McVoV2zg==', '张三丰', NULL, 'ss.png', '男', NULL, '正常', '2018-08-07 02:30:48', '2019-07-16 16:50:22');
INSERT INTO `user` VALUES (18, 'b2b_卖家', '18523146311', '8uXhwp4c3mYwizx/FKfY6wQRltvBd6Oc0qZvYZa+nfQ=', '测试', NULL, 'http://www.baidu.com', '男', NULL, '正常', '2018-07-18 09:10:46', '2019-07-01 10:11:25');
INSERT INTO `user` VALUES (20, 'b2b_买家', '18523146312', '2eZzUNIsix73h9MmhbmVow==', '宋丹丹', NULL, NULL, NULL, NULL, '正常', '2018-07-24 03:48:50', '2019-10-12 22:46:51');
INSERT INTO `user` VALUES (21, 'b2b_买家', '18523146313', '2eZzUNIsix73h9MmhbmVow==', '张双双', NULL, NULL, NULL, NULL, '正常', '2018-07-26 07:25:56', '2019-10-12 22:46:51');
INSERT INTO `user` VALUES (23, 'b2c_买家', '18523146314', '2eZzUNIsix73h9MmhbmVow==', '白俊毅', NULL, NULL, NULL, NULL, '正常', '2018-07-30 16:17:45', '2019-10-12 22:46:51');
INSERT INTO `user` VALUES (24, 'b2b_卖家', '18523146315', '2eZzUNIsix73h9MmhbmVow==', '测试测试', NULL, 'http://pcil57ri4.bkt.clouddn.com/FgP9TmgLFWztHHGfFyE9EQ1gaSiG', '男', NULL, '正常', '2018-07-30 16:20:04', '2019-10-12 22:46:51');
INSERT INTO `user` VALUES (25, 'b2b_卖家', '18523146316', '2eZzUNIsix73h9MmhbmVow==', '0123', NULL, NULL, NULL, NULL, '正常', '2018-07-31 08:22:32', '2019-10-12 22:46:51');
INSERT INTO `user` VALUES (26, 'b2b_买家', '18523146317', '2eZzUNIsix73h9MmhbmVow==', '胡令', NULL, NULL, NULL, NULL, '正常', '2018-08-01 06:48:42', '2019-10-12 22:46:51');
INSERT INTO `user` VALUES (28, 'b2b_卖家', '18523146318', '2eZzUNIsix73h9MmhbmVow==', '18523146366', NULL, NULL, NULL, NULL, '正常', '2018-08-07 02:02:42', '2019-10-12 22:46:51');
INSERT INTO `user` VALUES (34, 'b2b_卖家', '18523146319', '2eZzUNIsix73h9MmhbmVow==', '18723126410', NULL, NULL, NULL, NULL, '正常', '2018-08-07 02:32:14', '2019-10-12 22:46:51');
INSERT INTO `user` VALUES (35, 'b2b_卖家', '18523146320', '2eZzUNIsix73h9MmhbmVow==', '123456', NULL, NULL, NULL, NULL, '正常', '2018-08-07 02:38:57', '2019-10-12 22:46:51');
INSERT INTO `user` VALUES (36, 'b2b_卖家', '18523146321', '2eZzUNIsix73h9MmhbmVow==', '18223357996', NULL, NULL, NULL, NULL, '正常', '2018-08-16 10:03:30', '2019-10-12 22:46:51');
INSERT INTO `user` VALUES (37, 'b2b_卖家', '18523146322', '2eZzUNIsix73h9MmhbmVow==', '1', NULL, NULL, NULL, NULL, '正常', '2018-09-04 07:57:53', '2019-10-12 22:46:51');
INSERT INTO `user` VALUES (38, 'b2b_卖家', '18523146323', '2eZzUNIsix73h9MmhbmVow==', '1', NULL, NULL, NULL, NULL, '正常', '2018-09-04 07:58:18', '2019-10-12 22:46:51');
INSERT INTO `user` VALUES (39, 'b2b_卖家', '18523146324', '2eZzUNIsix73h9MmhbmVow==', '1', NULL, NULL, NULL, NULL, '正常', '2018-09-04 07:58:21', '2019-10-12 22:46:51');
INSERT INTO `user` VALUES (40, 'b2b_卖家', '18523146325', '2eZzUNIsix73h9MmhbmVow==', '1', NULL, NULL, NULL, NULL, '正常', '2018-09-04 08:01:23', '2019-10-12 22:46:51');
INSERT INTO `user` VALUES (61, 'b2b_卖家', '18523146326', '2eZzUNIsix73h9MmhbmVow==', '行走的CD', NULL, 'http://pcil57ri4.bkt.clouddn.com/FgX8QWRoA6ySMNG3xE01aHg_szWY', '男', NULL, '正常', '2018-09-20 02:57:21', '2019-10-12 22:46:51');
INSERT INTO `user` VALUES (65, 'b2b_买家', '18523146327', '2eZzUNIsix73h9MmhbmVow==', '测试', NULL, NULL, NULL, NULL, '正常', '2019-04-11 14:56:39', '2019-10-12 22:46:51');
INSERT INTO `user` VALUES (66, 'b2b_买家', '18523146328', '2eZzUNIsix73h9MmhbmVow==', '1', NULL, NULL, NULL, NULL, '正常', '2019-04-26 15:05:29', '2019-10-12 22:46:51');
INSERT INTO `user` VALUES (68, 'b2b_卖家', '18523146329', '2eZzUNIsix73h9MmhbmVow==', '1', NULL, NULL, NULL, NULL, '正常', '2019-04-26 15:43:39', '2019-10-12 22:46:51');
INSERT INTO `user` VALUES (69, 'b2b_买家', '18523146330', '2eZzUNIsix73h9MmhbmVow==', '18523146361', NULL, NULL, NULL, NULL, '正常', '2019-04-26 17:15:30', '2019-10-12 22:46:51');
INSERT INTO `user` VALUES (70, 'b2b_买家', '18523146331', '2eZzUNIsix73h9MmhbmVow==', '15215044536', '402848460@qq.com', NULL, '男', NULL, '正常', '2019-04-29 11:06:31', '2019-10-12 22:46:51');
INSERT INTO `user` VALUES (76, 'b2b_买家', '18523146332', '2eZzUNIsix73h9MmhbmVow==', '15736535451', NULL, NULL, NULL, NULL, '正常', '2019-05-06 17:25:52', '2019-10-12 22:46:51');
INSERT INTO `user` VALUES (77, 'b2b_买家', '18523146333', '2eZzUNIsix73h9MmhbmVow==', '15736535441', NULL, NULL, NULL, NULL, '正常', '2019-05-06 17:31:10', '2019-10-12 22:46:51');
INSERT INTO `user` VALUES (78, 'b2b_买家', '18523146334', '2eZzUNIsix73h9MmhbmVow==', '15736565656', NULL, NULL, NULL, NULL, '正常', '2019-05-06 18:21:15', '2019-10-12 22:46:51');
INSERT INTO `user` VALUES (79, 'b2b_买家', '18523146335', '2eZzUNIsix73h9MmhbmVow==', '晨曦', NULL, NULL, NULL, NULL, '正常', '2019-05-29 14:33:34', '2019-10-12 22:46:51');
INSERT INTO `user` VALUES (80, 'b2b_买家', '18523146336', '2eZzUNIsix73h9MmhbmVow==', '水帘洞', NULL, NULL, NULL, NULL, '正常', '2019-06-05 17:45:06', '2019-10-12 22:46:51');
INSERT INTO `user` VALUES (81, 'b2b_买家', '18523146337', '2eZzUNIsix73h9MmhbmVow==', '水帘洞', NULL, NULL, NULL, NULL, '正常', '2019-06-06 10:02:20', '2019-10-12 22:46:51');
INSERT INTO `user` VALUES (83, 'b2c_买家', '18523146315', 'uyJPQmQgEaVE99McVoV2zg==', '大慈大悲的李尽', NULL, NULL, NULL, NULL, '正常', '2019-06-13 11:41:29', '2019-07-02 15:16:14');
INSERT INTO `user` VALUES (95, 'b2c_买家', '15123555303', NULL, '某某', NULL, NULL, NULL, NULL, '正常', '2019-07-02 10:12:31', '2019-07-22 16:05:40');
INSERT INTO `user` VALUES (97, 'b2b_买家', '18523146316', 'uyJPQmQgEaVE99McVoV2zg==', '18523146316', NULL, NULL, NULL, NULL, '正常', '2019-07-02 15:35:52', '2019-07-02 15:35:52');
INSERT INTO `user` VALUES (134, 'b2b_买家', '15723156255', 'v0/S0Hi4OZFgycPaQ0JFPg==', 'abcd', NULL, NULL, NULL, NULL, '正常', '2019-07-04 10:06:46', '2019-07-05 14:27:00');
INSERT INTO `user` VALUES (165, 'b2c_买家', '18983749649', 'dtMGSWev/NQ3dvVaFNnLzw==', '穆漪梅', NULL, NULL, NULL, NULL, '正常', '2019-07-10 18:32:49', '2019-08-05 17:23:10');
INSERT INTO `user` VALUES (170, 'b2b_买家', '19923293135', 'uyJPQmQgEaVE99McVoV2zg==', 'abcd', NULL, NULL, NULL, NULL, '正常', '2019-07-18 16:39:41', '2019-07-18 16:39:41');
INSERT INTO `user` VALUES (172, 'b2c_买家', '18523146316', NULL, '弈凌', NULL, 'https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKI5KEavWv78DsicATULToysrNox1Ziaqpgofppc56DYakE1lyHgjpBwqOvpdm6HaZlEBtN4GIPntyg/132', NULL, NULL, '正常', '2019-07-19 11:29:26', '2019-07-19 11:29:26');
INSERT INTO `user` VALUES (173, 'b2c_买家', '19923293135', NULL, '晨曦', NULL, 'https://wx.qlogo.cn/mmopen/vi_32/0OuU7ia6tQtV2jSyHx1Ss9zJFKnEnO811XDSC4AkVE2iaJxVNNHe95jxDISNibImry0haeYpezsXPDZJls7lxntJg/132', NULL, NULL, '正常', '2019-07-19 16:17:09', '2019-07-19 16:17:09');
INSERT INTO `user` VALUES (174, 'b2b_买家', '18883877079', 'uyJPQmQgEaVE99McVoV2zg==', 'abcd', NULL, NULL, NULL, NULL, '正常', '2019-07-24 09:31:11', '2019-07-24 09:31:11');
INSERT INTO `user` VALUES (175, 'b2b_买家', '18716280028', 'uyJPQmQgEaVE99McVoV2zg==', 'abcd', NULL, NULL, NULL, NULL, '正常', '2019-07-24 15:59:31', '2019-07-24 15:59:31');
INSERT INTO `user` VALUES (176, 'b2c_买家', '18716280028', NULL, NULL, NULL, NULL, NULL, NULL, '正常', '2019-07-24 16:09:07', '2019-07-24 16:09:07');
INSERT INTO `user` VALUES (181, 'b2b_买家', '18024649918', 'uyJPQmQgEaVE99McVoV2zg==', NULL, NULL, NULL, NULL, NULL, '正常', '2019-07-26 17:05:00', '2019-07-26 17:05:00');
INSERT INTO `user` VALUES (182, 'b2c_买家', '18523146311', '2eZzUNIsix73h9MmhbmVow==', NULL, NULL, NULL, NULL, NULL, '正常', '2019-10-12 23:45:31', '2019-10-12 23:45:31');
INSERT INTO `user` VALUES (184, 'b2c_买家', '18523146312', '2eZzUNIsix73h9MmhbmVow==', 'ylyue', 'yl-yue@qq.com', 'https://gitee.com/yl-yue/yue-library/raw/master/docs/_images/logo.png', '男', '1996-05-15', '正常', '2019-10-12 23:49:02', '2019-10-12 23:49:02');

SET FOREIGN_KEY_CHECKS = 1;
