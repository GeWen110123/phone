/*
 Navicat Premium Data Transfer

 Source Server         : 本地shujuku
 Source Server Type    : MySQL
 Source Server Version : 80100
 Source Host           : localhost:3306
 Source Schema         : phone

 Target Server Type    : MySQL
 Target Server Version : 80100
 File Encoding         : 65001

 Date: 18/11/2025 15:51:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `douyin_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '抖音账号ID',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '时间',
  `json_string` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '{\r\n\"id\":\"id\",\r\n  \"nickname\": \"昵称\",\r\n  \"avatar\": \"头像URL\",\r\n  \"like_count\": \"获赞数\",\r\n  \"following_count\": \"关注数\",\r\n  \"follower_count\": \"粉丝数\",\r\n  \"video_count\": \"作品数\",\r\n  \"region\": \"地区\",\r\n  \"gender\": \"性别\",\r\n  \"age\": \"年龄\",\r\n  \"school\": \"学校\",\r\n  \"bio\": \"个人简介\",\r\n  \"verified_info\": \"实名信息\"\r\n}',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `douyin_id`(`douyin_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '抖音账号信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of account
-- ----------------------------

-- ----------------------------
-- Table structure for account_video
-- ----------------------------
DROP TABLE IF EXISTS `account_video`;
CREATE TABLE `account_video`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `account_id` bigint(0) NOT NULL COMMENT '所属账号',
  `video_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '视频ID',
  `title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '视频标题',
  `like_count` bigint(0) NULL DEFAULT 0 COMMENT '点赞数',
  `comment_count` bigint(0) NULL DEFAULT 0 COMMENT '评论数',
  `share_count` bigint(0) NULL DEFAULT 0 COMMENT '转发数',
  `collect_count` bigint(0) NULL DEFAULT 0 COMMENT '收藏数',
  `video_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '视频链接',
  `publish_time` datetime(0) NULL DEFAULT NULL COMMENT '发布时间',
  `comment` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '评论',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `video_id`(`video_id`) USING BTREE,
  INDEX `fk_video_account`(`account_id`) USING BTREE,
  CONSTRAINT `fk_video_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '账号视频表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of account_video
-- ----------------------------

-- ----------------------------
-- Table structure for dev_inquire
-- ----------------------------
DROP TABLE IF EXISTS `dev_inquire`;
CREATE TABLE `dev_inquire`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `dev_id` int(0) NOT NULL COMMENT '设备id',
  `user` int(0) NOT NULL COMMENT '操作用户',
  `time` datetime(0) NOT NULL COMMENT '操作时间',
  `type` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '操作状态（安装app，重启，开机，策略）',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备操作日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dev_inquire
-- ----------------------------

-- ----------------------------
-- Table structure for device
-- ----------------------------
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `device_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '设备唯一码/序列号',
  `device_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '设备名称（可修改）',
  `model` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '手机型号',
  `system_version` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '系统版本',
  `kernel_version` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '内核版本',
  `storage_used` bigint(0) NULL DEFAULT 0 COMMENT '硬盘已占用（MB）',
  `storage_total` bigint(0) NULL DEFAULT 0 COMMENT '硬盘总容量（MB）',
  `app_count` int(0) NULL DEFAULT 0 COMMENT '已安装APP数量',
  `status` tinyint(0) NULL DEFAULT 0 COMMENT '状态: 0未连接 1已连接',
  `last_online_time` datetime(0) NULL DEFAULT NULL COMMENT '最后在线时间',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `device_code`(`device_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '设备信息综合表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device
-- ----------------------------

-- ----------------------------
-- Table structure for device_group
-- ----------------------------
DROP TABLE IF EXISTS `device_group`;
CREATE TABLE `device_group`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分组名',
  `device_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备名_设备id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device_group
-- ----------------------------

-- ----------------------------
-- Table structure for location_info
-- ----------------------------
DROP TABLE IF EXISTS `location_info`;
CREATE TABLE `location_info`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `location_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '地点名',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `json_string` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '{\r\n  \"description\": \"地点描述\",\r\n  \"business_hours\": \"营业时间\",\r\n  \"address\": \"详细地址\",\r\n  \"content_count\": \"内容数量\"\r\n}',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '地区信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of location_info
-- ----------------------------

-- ----------------------------
-- Table structure for location_video
-- ----------------------------
DROP TABLE IF EXISTS `location_video`;
CREATE TABLE `location_video`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `location_id` bigint(0) NOT NULL COMMENT '地点ID',
  `creat_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `json_string` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '{\r\n  \"video_id\": \"视频ID\",\r\n  \"title\": \"标题\",\r\n  \"like_count\": \"点赞数\",\r\n  \"comment_count\": \"评论数\"\r\n}',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_location_video`(`location_id`) USING BTREE,
  CONSTRAINT `fk_location_video` FOREIGN KEY (`location_id`) REFERENCES `location_info` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '地区视频表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of location_video
-- ----------------------------

-- ----------------------------
-- Table structure for strategy_assign
-- ----------------------------
DROP TABLE IF EXISTS `strategy_assign`;
CREATE TABLE `strategy_assign`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `strategy_id` bigint(0) NOT NULL COMMENT '策略ID',
  `target_type` enum('device','group') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目标类型',
  `target_id` bigint(0) NOT NULL COMMENT '设备或组ID',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `interval_sec` int(0) NULL DEFAULT NULL COMMENT '执行间隔(秒)',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_strategy_assign_template`(`strategy_id`) USING BTREE,
  CONSTRAINT `strategy_assign_ibfk_1` FOREIGN KEY (`strategy_id`) REFERENCES `strategy_template` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '策略分配表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of strategy_assign
-- ----------------------------

-- ----------------------------
-- Table structure for strategy_log
-- ----------------------------
DROP TABLE IF EXISTS `strategy_log`;
CREATE TABLE `strategy_log`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `strategy_id` bigint(0) NOT NULL COMMENT '策略ID',
  `target_type` enum('device','group') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目标类型',
  `target_id` bigint(0) NOT NULL COMMENT '目标ID',
  `status` enum('success','fail') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'success' COMMENT '执行结果',
  `log_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '执行日志详情',
  `execute_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '执行时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_strategy_log_template`(`strategy_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '策略执行日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of strategy_log
-- ----------------------------

-- ----------------------------
-- Table structure for strategy_template
-- ----------------------------
DROP TABLE IF EXISTS `strategy_template`;
CREATE TABLE `strategy_template`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `strategy_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '策略名称',
  `script_path` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '策略脚本路径',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '策略描述',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '策略模板表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of strategy_template
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint(0) NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 103, 'admin', '若依', '00', 'ry@163.com', '15888888888', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '192.168.150.147', '2025-10-30 14:08:21', 'admin', '2024-05-31 07:59:41', '', '2025-10-30 06:08:20', '管理员');
INSERT INTO `sys_user` VALUES (2, 105, 'ry', '若依', '00', 'ry@qq.com', '15666666666', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2024-05-31 07:59:41', 'admin', '2024-05-31 07:59:41', '', NULL, '测试员');

-- ----------------------------
-- Table structure for video_comment
-- ----------------------------
DROP TABLE IF EXISTS `video_comment`;
CREATE TABLE `video_comment`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `video_id` bigint(0) NOT NULL COMMENT '视频ID',
  `comment_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论ID',
  `commenter_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评论者抖音ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '评论内容',
  `like_count` bigint(0) NULL DEFAULT 0 COMMENT '点赞数',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '评论时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `comment_id`(`comment_id`) USING BTREE,
  INDEX `fk_comment_video`(`video_id`) USING BTREE,
  CONSTRAINT `fk_comment_video` FOREIGN KEY (`video_id`) REFERENCES `account_video` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '视频评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of video_comment
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
