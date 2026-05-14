/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 90500
 Source Host           : 127.0.0.1:3306
 Source Schema         : phone

 Target Server Type    : MySQL
 Target Server Version : 90500
 File Encoding         : 65001

 Date: 08/12/2025 15:54:40
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
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '时间',
  `dev_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备吗',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `douyin_id`(`douyin_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 301 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '抖音账号信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES (1, '44846722259', '2025-11-10 17:31:14', '{\"nickname\":\"清漪\",\"avatar\":\"\",\"likes_count\":\"4\",\"follow_count\":\"68\",\"fans_count\":\"7\",\"works_count\":\"2\",\"region\":\"河南\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:15', 'wsadfwse0121');
INSERT INTO `account` VALUES (2, '90494981867', '2025-11-10 17:31:15', '{\"nickname\":\"用户1290924161505\",\"avatar\":\"\",\"likes_count\":\"0\",\"follow_count\":\"6\",\"fans_count\":\"2\",\"works_count\":\"\",\"region\":\"\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:15', 'wsadfwse0122');
INSERT INTO `account` VALUES (3, '53190665552', '2025-11-10 17:31:15', '{\"nickname\":\"兮辞\",\"avatar\":\"\",\"likes_count\":\"0\",\"follow_count\":\"\",\"fans_count\":\"4\",\"works_count\":\"\",\"region\":\"\",\"gender\":\"女\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:15', 'eee0101');
INSERT INTO `account` VALUES (4, 'dyolaq7s8r8h', '2025-11-10 17:31:15', '{\"nickname\":\"鼠鼠战队\",\"avatar\":\"\",\"likes_count\":\"\",\"follow_count\":\"\",\"fans_count\":\"\",\"works_count\":\"\",\"region\":\"\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:15', 'abcd0101');
INSERT INTO `account` VALUES (5, 'Aifei1188', '2025-11-10 17:31:15', '{\"nickname\":\"小新很好哦～\",\"avatar\":\"\",\"likes_count\":\"62\",\"follow_count\":\"268\",\"fans_count\":\"19\",\"works_count\":\"1\",\"region\":\"江苏\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:15', 'abcd0101');
INSERT INTO `account` VALUES (6, '65728817881', '2025-11-10 17:31:15', '{\"nickname\":\"www\",\"avatar\":\"\",\"likes_count\":\"0\",\"follow_count\":\"2\",\"fans_count\":\"0\",\"works_count\":\"\",\"region\":\"\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:15', 'abcd0101');
INSERT INTO `account` VALUES (7, '27390044293', '2025-11-10 17:31:15', '{\"nickname\":\"用户196348627998\",\"avatar\":\"\",\"likes_count\":\"0\",\"follow_count\":\"\",\"fans_count\":\"\",\"works_count\":\"\",\"region\":\"\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:15', 'abcd0101');
INSERT INTO `account` VALUES (8, '24876273739', '2025-11-10 17:31:15', '{\"nickname\":\"你骂白菇你眼瞎\",\"avatar\":\"\",\"likes_count\":\"274\",\"follow_count\":\"564\",\"fans_count\":\"32\",\"works_count\":\"18\",\"region\":\"陕西\",\"gender\":\"女·19岁\",\"age\":\"女·19岁\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:16', 'wsadfwse0121');
INSERT INTO `account` VALUES (9, '77068216825', '2025-11-10 17:31:16', '{\"nickname\":\"龙…@百川…拄拐高中生\",\"avatar\":\"\",\"likes_count\":\"1415\",\"follow_count\":\"5345\",\"fans_count\":\"300\",\"works_count\":\"1\",\"region\":\"山东\",\"gender\":\"男·18岁\",\"age\":\"男·18岁\",\"school\":\"河南农业大学\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:16', 'wsadfwse0122');
INSERT INTO `account` VALUES (10, '48426919936', '2025-11-10 17:31:16', '{\"nickname\":\"韶院学子——帝琰殇....\",\"avatar\":\"\",\"likes_count\":\"177\",\"follow_count\":\"97\",\"fans_count\":\"17\",\"works_count\":\"159\",\"region\":\"广东\",\"gender\":\"女·18岁\",\"age\":\"女·18岁\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:16', 'eee0101');
INSERT INTO `account` VALUES (11, '43458366953', '2025-11-10 17:31:16', '{\"nickname\":\"草稿纸殉葬\",\"avatar\":\"\",\"likes_count\":\"3\",\"follow_count\":\"342\",\"fans_count\":\"2\",\"works_count\":\"3\",\"region\":\"湖南\",\"gender\":\"女\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:16', 'abcd0101');
INSERT INTO `account` VALUES (12, '21927378798', '2025-11-10 17:31:16', '{\"nickname\":\"澜茗\",\"avatar\":\"\",\"likes_count\":\"0\",\"follow_count\":\"10\",\"fans_count\":\"1\",\"works_count\":\"\",\"region\":\"广西\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:16', 'abcd0101');
INSERT INTO `account` VALUES (13, 'qiqibabale25', '2025-11-10 17:31:16', '{\"nickname\":\"七七八八了\",\"avatar\":\"\",\"likes_count\":\"10\",\"follow_count\":\"1194\",\"fans_count\":\"33\",\"works_count\":\"\",\"region\":\"\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:16', 'abcd0101');
INSERT INTO `account` VALUES (14, 'dyzh1v5oa1lj', '2025-11-10 17:31:16', '{\"nickname\":\"恒真式\",\"avatar\":\"\",\"likes_count\":\"0\",\"follow_count\":\"41\",\"fans_count\":\"10\",\"works_count\":\"\",\"region\":\"\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:16', 'abcd0101');
INSERT INTO `account` VALUES (15, '34737263233', '2025-11-10 17:31:16', '{\"nickname\":\"djjdhddghdhsh\",\"avatar\":\"\",\"likes_count\":\"0\",\"follow_count\":\"14\",\"fans_count\":\"0\",\"works_count\":\"\",\"region\":\"\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:16', 'abcd0101');
INSERT INTO `account` VALUES (16, '84005721734', '2025-11-10 17:31:16', '{\"nickname\":\"几个度u\",\"avatar\":\"\",\"likes_count\":\"0\",\"follow_count\":\"91\",\"fans_count\":\"2\",\"works_count\":\"\",\"region\":\"四川\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:16', 'abcd0101');
INSERT INTO `account` VALUES (17, 'dyk5w3fkfb5z', '2025-11-10 17:31:16', '{\"nickname\":\"小熊饼干\",\"avatar\":\"\",\"likes_count\":\"0\",\"follow_count\":\"89\",\"fans_count\":\"27\",\"works_count\":\"\",\"region\":\"新疆\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:16', 'abcd0101');
INSERT INTO `account` VALUES (18, 'sjusjn5201314', '2025-11-10 17:31:16', '{\"nickname\":\"北笙\",\"avatar\":\"\",\"likes_count\":\"15\",\"follow_count\":\"52\",\"fans_count\":\"18\",\"works_count\":\"7\",\"region\":\"福建\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:16', 'wsadfwse0121');
INSERT INTO `account` VALUES (19, '29626018339', '2025-11-10 17:31:16', '{\"nickname\":\"哆啦追剧\",\"avatar\":\"\",\"likes_count\":\"4\",\"follow_count\":\"16\",\"fans_count\":\"1\",\"works_count\":\"2\",\"region\":\"\",\"gender\":\"女·25岁\",\"age\":\"女·25岁\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:16', 'wsadfwse0122');
INSERT INTO `account` VALUES (20, '97535580250', '2025-11-10 17:31:16', '{\"nickname\":\"清落\",\"avatar\":\"\",\"likes_count\":\"0\",\"follow_count\":\"120\",\"fans_count\":\"13\",\"works_count\":\"\",\"region\":\"江西\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:16', 'eee0101');
INSERT INTO `account` VALUES (21, '75791159749', '2025-11-10 17:31:16', '{\"nickname\":\"瑶少♠\",\"avatar\":\"\",\"likes_count\":\"0\",\"follow_count\":\"5\",\"fans_count\":\"0\",\"works_count\":\"\",\"region\":\"陕西\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:16', 'abcd0101');
INSERT INTO `account` VALUES (22, '97458449754', '2025-11-10 17:31:16', '{\"nickname\":\"吟明语时\",\"avatar\":\"\",\"likes_count\":\"332\",\"follow_count\":\"181\",\"fans_count\":\"35\",\"works_count\":\"35\",\"region\":\"四川\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:16', 'abcd0101');
INSERT INTO `account` VALUES (23, '89926267734', '2025-11-10 17:31:16', '{\"nickname\":\"露悸姩\",\"avatar\":\"\",\"likes_count\":\"103\",\"follow_count\":\"9\",\"fans_count\":\"2\",\"works_count\":\"3\",\"region\":\"山东\",\"gender\":\"女\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:16', 'abcd0101');
INSERT INTO `account` VALUES (24, 'amour.zwj', '2025-11-10 17:31:16', '{\"nickname\":\"暮色微雨\",\"avatar\":\"\",\"likes_count\":\"0\",\"follow_count\":\"14\",\"fans_count\":\"6\",\"works_count\":\"\",\"region\":\"\",\"gender\":\"女\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:16', 'abcd0101');
INSERT INTO `account` VALUES (25, '94281867012', '2025-11-10 17:31:16', '{\"nickname\":\"七月多^\",\"avatar\":\"\",\"likes_count\":\"34\",\"follow_count\":\"88\",\"fans_count\":\"95\",\"works_count\":\"1\",\"region\":\"陕西\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:16', 'abcd0101');
INSERT INTO `account` VALUES (26, 'xiaoxiaosha686', '2025-11-10 17:31:16', '{\"nickname\":\"『微姊』\",\"avatar\":\"\",\"likes_count\":\"23\",\"follow_count\":\"5794\",\"fans_count\":\"104\",\"works_count\":\"\",\"region\":\"陕西\",\"gender\":\"男\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:16', 'abcd0101');
INSERT INTO `account` VALUES (27, '1310984282', '2025-11-10 17:31:16', '{\"nickname\":\"aaaq\",\"avatar\":\"\",\"likes_count\":\"0\",\"follow_count\":\"967\",\"fans_count\":\"40\",\"works_count\":\"\",\"region\":\"福建\",\"gender\":\"女·18岁\",\"age\":\"女·18岁\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:16', 'abcd0101');
INSERT INTO `account` VALUES (28, 'wanqi20081230', '2025-11-10 17:31:16', '{\"nickname\":\"晚风ㅤ\",\"avatar\":\"\",\"likes_count\":\"3\",\"follow_count\":\"52\",\"fans_count\":\"14\",\"works_count\":\"\",\"region\":\"江苏\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:16', 'abcd0101');
INSERT INTO `account` VALUES (29, '41408842203', '2025-11-10 17:31:16', '{\"nickname\":\"建材销售老李（磊哥）\",\"avatar\":\"\",\"likes_count\":\"0\",\"follow_count\":\"\",\"fans_count\":\"0\",\"works_count\":\"\",\"region\":\"\",\"gender\":\"男·49岁\",\"age\":\"男·49岁\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:16', 'abcd0101');
INSERT INTO `account` VALUES (30, '87188763661', '2025-11-10 17:31:16', '{\"nickname\":\"星阑123\",\"avatar\":\"\",\"likes_count\":\"0\",\"follow_count\":\"7\",\"fans_count\":\"1\",\"works_count\":\"\",\"region\":\"陕西\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:16', 'abcd0101');
INSERT INTO `account` VALUES (31, '62404631920', '2025-11-10 17:31:16', '{\"nickname\":\"珍珍\",\"avatar\":\"\",\"likes_count\":\"0\",\"follow_count\":\"337\",\"fans_count\":\"27\",\"works_count\":\"\",\"region\":\"\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:16', 'abcd0101');
INSERT INTO `account` VALUES (32, '23186789248', '2025-11-10 17:31:16', '{\"nickname\":\"车管\",\"avatar\":\"\",\"likes_count\":\"0\",\"follow_count\":\"6\",\"fans_count\":\"4\",\"works_count\":\"\",\"region\":\"\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:16', 'abcd0101');
INSERT INTO `account` VALUES (33, 'wjr814wjr', '2025-11-10 17:31:16', '{\"nickname\":\"虾滑牛肉\",\"avatar\":\"\",\"likes_count\":\"7805\",\"follow_count\":\"290\",\"fans_count\":\"429\",\"works_count\":\"\",\"region\":\"江苏\",\"gender\":\"男·23岁\",\"age\":\"男·23岁\",\"school\":\"福建林业职业技术学院\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:16', 'abcd0101');
INSERT INTO `account` VALUES (34, 'NICKNAME_三角洲行动', '2025-11-10 17:31:17', '{\"nickname\":\"三角洲行动\",\"avatar\":\"\",\"likes_count\":\"3846.3万\",\"follow_count\":\"14\",\"fans_count\":\"571.4万\",\"works_count\":\"1205\",\"region\":\"广东\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"3900限时三角券人人有 洲年外观免费拿！9月21日，三角洲行动一洲年专属福利开启！\",\"custom_bio_items\":{}}', '2025-11-10 17:31:17', 'abcd0101');
INSERT INTO `account` VALUES (35, '23574447586', '2025-11-10 17:31:17', '{\"nickname\":\"..\",\"avatar\":\"\",\"likes_count\":\"1\",\"follow_count\":\"16\",\"fans_count\":\"11\",\"works_count\":\"\",\"region\":\"福建\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:17', 'abcd0101');
INSERT INTO `account` VALUES (36, 'dygl2kyn8nkk', '2025-11-10 17:31:17', '{\"nickname\":\"(●—●)\",\"avatar\":\"\",\"likes_count\":\"0\",\"follow_count\":\"252\",\"fans_count\":\"13\",\"works_count\":\"\",\"region\":\"福建\",\"gender\":\"女\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:17', 'wsadfwse0121');
INSERT INTO `account` VALUES (37, 'donkleft0522', '2025-11-10 17:31:17', '{\"nickname\":\"JDSAr\",\"avatar\":\"\",\"likes_count\":\"657\",\"follow_count\":\"83\",\"fans_count\":\"50\",\"works_count\":\"1\",\"region\":\"福建\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:17', 'wsadfwse0122');
INSERT INTO `account` VALUES (38, '54031727126', '2025-11-10 17:31:17', '{\"nickname\":\"南桥上\",\"avatar\":\"\",\"likes_count\":\"203\",\"follow_count\":\"79\",\"fans_count\":\"32\",\"works_count\":\"10\",\"region\":\"江苏\",\"gender\":\"男\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:17', 'eee0101');
INSERT INTO `account` VALUES (39, 'Zzzzzzzzdw0604', '2025-11-10 17:31:17', '{\"nickname\":\"Oo猪\",\"avatar\":\"\",\"likes_count\":\"709\",\"follow_count\":\"151\",\"fans_count\":\"76\",\"works_count\":\"2\",\"region\":\"福建\",\"gender\":\"女\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:17', 'abcd0101');
INSERT INTO `account` VALUES (40, '1590770993', '2025-11-10 17:31:17', '{\"nickname\":\"阿斗\",\"avatar\":\"\",\"likes_count\":\"3294\",\"follow_count\":\"98\",\"fans_count\":\"501\",\"works_count\":\"13\",\"region\":\"福建\",\"gender\":\"\",\"age\":\"\",\"school\":\"福建理工大学\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:17', 'abcd0101');
INSERT INTO `account` VALUES (41, '2167742280', '2025-11-10 17:31:17', '{\"nickname\":\"我东呐。\",\"avatar\":\"\",\"likes_count\":\"574\",\"follow_count\":\"1419\",\"fans_count\":\"231\",\"works_count\":\"9\",\"region\":\"福建\",\"gender\":\"男·27岁\",\"age\":\"男·27岁\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:17', 'abcd0101');
INSERT INTO `account` VALUES (42, '856048738', '2025-11-10 17:31:17', '{\"nickname\":\"陈勹勹\",\"avatar\":\"\",\"likes_count\":\"202\",\"follow_count\":\"89\",\"fans_count\":\"69\",\"works_count\":\"6\",\"region\":\"福建\",\"gender\":\"男·26岁\",\"age\":\"男·26岁\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:17', 'abcd0101');
INSERT INTO `account` VALUES (43, 'dy1d43eeqf7bh', '2025-11-10 17:31:17', '{\"nickname\":\"枫原万叶\",\"avatar\":\"\",\"likes_count\":\"45\",\"follow_count\":\"119\",\"fans_count\":\"15\",\"works_count\":\"1\",\"region\":\"福建\",\"gender\":\"\",\"age\":\"24岁\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:17', 'abcd0101');
INSERT INTO `account` VALUES (44, '24827224301', '2025-11-10 17:31:17', '{\"nickname\":\"₊˶᳐折雨棠˶᳐੭ﾞ\",\"avatar\":\"\",\"likes_count\":\"535\",\"follow_count\":\"80\",\"fans_count\":\"111\",\"works_count\":\"7\",\"region\":\"福建\",\"gender\":\"女\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:17', 'abcd0101');
INSERT INTO `account` VALUES (45, '23823860309', '2025-11-10 17:31:17', '{\"nickname\":\"说了再见\",\"avatar\":\"\",\"likes_count\":\"43\",\"follow_count\":\"29\",\"fans_count\":\"20\",\"works_count\":\"\",\"region\":\"福建\",\"gender\":\"\",\"age\":\"逆天  陈七岁\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:17', 'abcd0101');
INSERT INTO `account` VALUES (46, 'wsdwq', '2025-11-10 17:31:17', '{\"nickname\":\"Endless^\",\"avatar\":\"\",\"likes_count\":\"1258\",\"follow_count\":\"69\",\"fans_count\":\"111\",\"works_count\":\"4\",\"region\":\"福建\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:17', 'abcd0101');
INSERT INTO `account` VALUES (47, 'wqedfer', '2025-11-10 17:31:17', '{\"nickname\":\"..玛卡巴卡\",\"avatar\":\"\",\"likes_count\":\"7942\",\"follow_count\":\"905\",\"fans_count\":\"1325\",\"works_count\":\"36\",\"region\":\"福建\",\"gender\":\"不要连赞哦！感谢关注......\\n爱开Moto..️的快乐男孩\\n家有美女\\nTNT装药选手（持证上岗）...  更多  \",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:17', 'wsadfwse0121');
INSERT INTO `account` VALUES (48, 'qwedqw', '2025-11-10 17:31:17', '{\"nickname\":\"元气之旅\",\"avatar\":\"\",\"likes_count\":\"6\",\"follow_count\":\"115\",\"fans_count\":\"8\",\"works_count\":\"\",\"region\":\"\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:17', 'wsadfwse0122');
INSERT INTO `account` VALUES (49, '29983844247', '2025-11-10 17:31:17', '{\"nickname\":\"Zz..\",\"avatar\":\"\",\"likes_count\":\"0\",\"follow_count\":\"15\",\"fans_count\":\"5\",\"works_count\":\"\",\"region\":\"\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:17', 'eee0101');
INSERT INTO `account` VALUES (50, '96637935462.', '2025-11-10 17:31:17', '{\"nickname\":\"wzh？\",\"avatar\":\"\",\"likes_count\":\"2.0万\",\"follow_count\":\"498\",\"fans_count\":\"515\",\"works_count\":\"\",\"region\":\"福建\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:17', 'abcd0101');
INSERT INTO `account` VALUES (51, '51574275868', '2025-11-10 17:31:17', '{\"nickname\":\"辽\",\"avatar\":\"\",\"likes_count\":\"3235\",\"follow_count\":\"110\",\"fans_count\":\"83\",\"works_count\":\"4\",\"region\":\"福建\",\"gender\":\"党旗所指 团旗所向\\n男神：@111111\\n家人：@SEVENTEEN_OFFICIAL\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:17', 'abcd0101');
INSERT INTO `account` VALUES (52, '65928323581', '2025-11-10 17:31:17', '{\"nickname\":\"离我远点\",\"avatar\":\"\",\"likes_count\":\"6\",\"follow_count\":\"349\",\"fans_count\":\"39\",\"works_count\":\"\",\"region\":\"福建\",\"gender\":\"男\",\"age\":\"\",\"school\":\"北京市汽车工业总公司职工大学\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:17', 'abcd0101');
INSERT INTO `account` VALUES (53, '1080728324', '2025-11-10 17:31:17', '{\"nickname\":\"九\",\"avatar\":\"\",\"likes_count\":\"0\",\"follow_count\":\"81\",\"fans_count\":\"20\",\"works_count\":\"\",\"region\":\"\",\"gender\":\"男·21岁\",\"age\":\"男·21岁\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:17', 'abcd0101');
INSERT INTO `account` VALUES (54, '1820180879', '2025-11-10 17:31:17', '{\"nickname\":\"前“兔”无量\",\"avatar\":\"\",\"likes_count\":\"307\",\"follow_count\":\"190\",\"fans_count\":\"30\",\"works_count\":\"31\",\"region\":\"甘肃\",\"gender\":\"男·26岁\",\"age\":\"男·26岁\",\"school\":\"湖北理工学院\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:17', 'abcd0101');
INSERT INTO `account` VALUES (55, 'Wxy4480', '2025-11-10 17:31:17', '{\"nickname\":\"海苔肉松饼\",\"avatar\":\"\",\"likes_count\":\"2359.5万\",\"follow_count\":\"47\",\"fans_count\":\"92.9万\",\"works_count\":\"281\",\"region\":\"重庆\",\"gender\":\"女\",\"age\":\"\",\"school\":\"重庆机电职业技术大学\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 17:31:17', 'abcd0101');
INSERT INTO `account` VALUES (291, 'Xxhan10100812.', '2025-11-10 17:36:07', '{\"nickname\":\"Endless^\",\"avatar\":\"\",\"likes_count\":\"1258\",\"follow_count\":\"69\",\"fans_count\":\"111\",\"works_count\":\"4\",\"region\":\"福建\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 09:36:07', 'abcd0101');
INSERT INTO `account` VALUES (292, '87733573', '2025-11-10 17:36:07', '{\"nickname\":\"..玛卡巴卡\",\"avatar\":\"\",\"likes_count\":\"7942\",\"follow_count\":\"905\",\"fans_count\":\"1325\",\"works_count\":\"36\",\"region\":\"福建\",\"gender\":\"不要连赞哦！感谢关注......\\n爱开Moto..️的快乐男孩\\n家有美女\\nTNT装药选手（持证上岗）...  更多  \",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 09:36:07', 'abcd0101');
INSERT INTO `account` VALUES (293, '5334842632943', '2025-11-10 17:36:07', '{\"nickname\":\"元气之旅\",\"avatar\":\"\",\"likes_count\":\"6\",\"follow_count\":\"115\",\"fans_count\":\"8\",\"works_count\":\"\",\"region\":\"\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 09:36:07', 'abcd0101');
INSERT INTO `account` VALUES (294, 'NICKNAME_Apple产品抖音官方自营店32', '2025-11-10 17:36:07', '{\"nickname\":\"Apple产品抖音官方自营店\",\"avatar\":\"\",\"likes_count\":\"10.1万\",\"follow_count\":\"2\",\"fans_count\":\"158.0万\",\"works_count\":\"145\",\"region\":\"北京\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"Apple 官方授权，抖音商城自营\\n精选好物，正品保障\\n商城直发，售后无忧\\n顺丰包邮，多仓速发...  更多  \",\"custom_bio_items\":{}}', '2025-11-10 09:36:07', 'abcd0101');
INSERT INTO `account` VALUES (295, '73033302376e1q2', '2025-11-10 17:36:07', '{\"nickname\":\"叁竖不见\",\"avatar\":\"\",\"likes_count\":\"1981\",\"follow_count\":\"72\",\"fans_count\":\"185\",\"works_count\":\"3\",\"region\":\"福建\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 09:36:07', 'wsadfwse0121');
INSERT INTO `account` VALUES (296, 'NICKNAME_小米官方旗舰店123', '2025-11-10 17:36:07', '{\"nickname\":\"小米官方旗舰店\",\"avatar\":\"\",\"likes_count\":\"2487.6万\",\"follow_count\":\"59\",\"fans_count\":\"1129.7万\",\"works_count\":\"27万\",\"region\":\"广东\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"小米官方旗舰店！！\\n❤️中奖实物奖品用户，奖品30个自然日内发出\\n❤️直播间赠品将在手机签收后7-10个自然日发出\\n✅产品订单及售后问题请咨询在线客服✅..\",\"custom_bio_items\":{}}', '2025-11-10 09:36:07', 'wsadfwse0122');
INSERT INTO `account` VALUES (297, '53348426329', '2025-11-10 17:37:02', '{\"nickname\":\"元气之旅\",\"avatar\":\"\",\"likes_count\":\"6\",\"follow_count\":\"115\",\"fans_count\":\"8\",\"works_count\":\"\",\"region\":\"\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 09:37:01', 'eee0101');
INSERT INTO `account` VALUES (298, 'NICKNAME_Apple产品抖音官方自营店', '2025-11-10 17:37:02', '{\"nickname\":\"Apple产品抖音官方自营店\",\"avatar\":\"\",\"likes_count\":\"10.1万\",\"follow_count\":\"2\",\"fans_count\":\"158.0万\",\"works_count\":\"145\",\"region\":\"北京\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"Apple 官方授权，抖音商城自营\\n精选好物，正品保障\\n商城直发，售后无忧\\n顺丰包邮，多仓速发...  更多  \",\"custom_bio_items\":{}}', '2025-11-10 09:37:01', 'abcd0101');
INSERT INTO `account` VALUES (299, '73033302376', '2025-11-10 17:37:02', '{\"nickname\":\"叁竖不见\",\"avatar\":\"\",\"likes_count\":\"1981\",\"follow_count\":\"72\",\"fans_count\":\"185\",\"works_count\":\"3\",\"region\":\"福建\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"\",\"custom_bio_items\":{}}', '2025-11-10 09:37:01', 'abcd0101');
INSERT INTO `account` VALUES (300, 'NICKNAME_小米官方旗舰店', '2025-11-10 17:37:02', '{\"nickname\":\"小米官方旗舰店\",\"avatar\":\"\",\"likes_count\":\"2487.6万\",\"follow_count\":\"59\",\"fans_count\":\"1129.7万\",\"works_count\":\"27万\",\"region\":\"广东\",\"gender\":\"\",\"age\":\"\",\"school\":\"\",\"real_name_info\":\"\",\"real_name\":\"\",\"signature\":\"小米官方旗舰店！！\\n❤️中奖实物奖品用户，奖品30个自然日内发出\\n❤️直播间赠品将在手机签收后7-10个自然日发出\\n✅产品订单及售后问题请咨询在线客服✅..\",\"custom_bio_items\":{}}', '2025-11-10 09:37:01', 'abcd0101');

-- ----------------------------
-- Table structure for device
-- ----------------------------
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `device_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '设备码',
  `device_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备名（可修改）',
  `model` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机型号',
  `system_version` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '系统版本',
  `kernel_version` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '内核版本',
  `storage_usage` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '硬盘占用',
  `status` enum('1','0') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（已连接/未连接）',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `last_online_time` datetime(0) NULL DEFAULT NULL COMMENT '最后在线时间',
  `users` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '可见人员',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `device_code`(`device_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '设备信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device
-- ----------------------------
INSERT INTO `device` VALUES (1, 'wsadfwse0121', '设备名01', '苹果', '13.0.1', '5.0', '45G', '1', '2025-11-04 10:16:34', '2025-11-07 14:52:59', '2025-11-04 10:16:37', NULL);
INSERT INTO `device` VALUES (2, 'wsadfwse0122', '设备名02', '苹果', '13.0.1', '5.0', '45G', '1', '2025-11-04 10:16:34', '2025-11-06 16:46:50', '2025-11-04 10:16:37', NULL);
INSERT INTO `device` VALUES (3, 'eee0101', '设备名03', '安卓', '9.2.6', '5.0', '60G', '1', '2025-11-04 10:16:34', '2025-11-05 14:52:47', '2025-11-04 10:16:37', NULL);
INSERT INTO `device` VALUES (4, 'abcd0101', '设备名04', '安卓', '9.2.6', '5.0', '60G', '1', '2025-11-04 10:16:34', '2025-11-05 14:52:47', '2025-11-04 10:16:37', NULL);

-- ----------------------------
-- Table structure for device_app
-- ----------------------------
DROP TABLE IF EXISTS `device_app`;
CREATE TABLE `device_app`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `device_id` bigint(0) NOT NULL COMMENT '设备ID',
  `app_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '应用名称',
  `package_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '应用包名',
  `version` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '应用版本',
  `install_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '安装时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '是否安装完成 0未安装  1 已安装',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_device_app_device`(`device_id`) USING BTREE,
  CONSTRAINT `fk_device_app_device` FOREIGN KEY (`device_id`) REFERENCES `device` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '设备安装应用表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device_app
-- ----------------------------
INSERT INTO `device_app` VALUES (1, 1, '抖音', 'douyin.app', '5.5.1', '2025-11-01 10:19:13', '2025-11-05 07:07:50', '1');
INSERT INTO `device_app` VALUES (2, 1, '小红书', 'hoingshu.app', '3.5.1', '2025-11-01 10:19:13', '2025-11-05 07:07:47', '1');
INSERT INTO `device_app` VALUES (3, 1, '油管', 'youguan.app', '1.5.1', '2025-11-01 10:19:13', '2025-11-05 07:35:06', '0');

-- ----------------------------
-- Table structure for device_command
-- ----------------------------
DROP TABLE IF EXISTS `device_command`;
CREATE TABLE `device_command`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `device_id` bigint(0) NOT NULL COMMENT '设备ID',
  `command` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '命令（reboot、factory_reset、install_app等）',
  `params` json NULL COMMENT '命令参数',
  `status` enum('pending','executed','failed') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'pending' COMMENT '状态',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '下发时间',
  `execute_time` datetime(0) NULL DEFAULT NULL COMMENT '执行时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_device_command_device`(`device_id`) USING BTREE,
  CONSTRAINT `fk_device_command_device` FOREIGN KEY (`device_id`) REFERENCES `device` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '设备下发命令表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device_command
-- ----------------------------

-- ----------------------------
-- Table structure for device_group
-- ----------------------------
DROP TABLE IF EXISTS `device_group`;
CREATE TABLE `device_group`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分组名称',
  `device_count` int(0) NULL DEFAULT 0 COMMENT '设备数量',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `dev_id` varchar(2550) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备名_id ,拼接',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `users` varchar(2550) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '可见人员',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '设备分组表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device_group
-- ----------------------------
INSERT INTO `device_group` VALUES (1, '抖音分组', 3, '2025-11-04 10:17:35', '2025-11-07 14:52:59', '设备名123_1,设备名02_2,设备名01_1', 'douyin', NULL);
INSERT INTO `device_group` VALUES (2, '油管01', 3, '2025-11-04 17:12:43', '2025-11-07 14:52:59', '设备名03_3,设备名04_4,设备名01_1', '123', NULL);
INSERT INTO `device_group` VALUES (3, '小红书', 5, '2025-11-04 17:13:59', '2025-11-07 14:52:59', '设备名123_1,设备名02_2,设备名03_3,设备名04_4,设备名01_1', 'xhsccc', NULL);
INSERT INTO `device_group` VALUES (7, 'CC', 0, '2025-11-07 14:29:01', '2025-11-07 06:29:00', '设备名03_3,设备名02_2', '1', NULL);
INSERT INTO `device_group` VALUES (8, '11', 2, '2025-11-07 14:31:10', '2025-11-07 14:52:59', '设备名03_3,设备名01_1', '33', '1,2');
INSERT INTO `device_group` VALUES (9, 'cc', 0, '2025-11-07 14:34:57', '2025-11-07 14:36:41', '设备名02_2', '123', '测试超出_100');

-- ----------------------------
-- Table structure for device_log
-- ----------------------------
DROP TABLE IF EXISTS `device_log`;
CREATE TABLE `device_log`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `device_id` bigint(0) NOT NULL COMMENT '设备ID',
  `action_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作类型（安装、卸载、重启等）',
  `action_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '操作详情',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_device_log_device`(`device_id`) USING BTREE,
  CONSTRAINT `fk_device_log_device` FOREIGN KEY (`device_id`) REFERENCES `device` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '设备活动日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device_log
-- ----------------------------
INSERT INTO `device_log` VALUES (1, 1, '安装', '安装抖音程序', '2025-11-04 02:20:08');
INSERT INTO `device_log` VALUES (2, 1, '分组去除', '此设备从小红书分组中去除', '2025-11-05 15:02:31');
INSERT INTO `device_log` VALUES (3, 2, '分组新增', '此设备添加至 抖音分组 分组', '2025-11-05 15:02:54');
INSERT INTO `device_log` VALUES (4, 2, '分组去除', '此设备从油管01分组中去除', '2025-11-05 15:02:54');
INSERT INTO `device_log` VALUES (5, 2, '分组新增', '此设备添加至 油管01 分组', '2025-11-05 15:03:18');
INSERT INTO `device_log` VALUES (6, 1, '分组去除', '此设备从油管01分组中去除', '2025-11-06 16:46:46');
INSERT INTO `device_log` VALUES (7, 2, '分组去除', '此设备从油管01分组中去除', '2025-11-06 16:46:50');
INSERT INTO `device_log` VALUES (8, 1, '分组新增', '此设备添加至 抖音分组 分组', '2025-11-07 09:23:57');
INSERT INTO `device_log` VALUES (9, 1, '分组新增', '此设备添加至 油管01 分组', '2025-11-07 09:23:57');
INSERT INTO `device_log` VALUES (10, 1, '分组新增', '此设备添加至 小红书 分组', '2025-11-07 09:23:57');

-- ----------------------------
-- Table structure for strategy_log
-- ----------------------------
DROP TABLE IF EXISTS `strategy_log`;
CREATE TABLE `strategy_log`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `strategy_id` bigint(0) NOT NULL COMMENT '策略ID',
  `target_id` bigint(0) NOT NULL COMMENT '目标ID',
  `status` enum('success','fail') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'success' COMMENT '执行结果',
  `log_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '执行日志详情',
  `execute_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '执行时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_strategy_log_template`(`strategy_id`) USING BTREE,
  CONSTRAINT `strategy_log_ibfk_1` FOREIGN KEY (`strategy_id`) REFERENCES `strategy_template` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '策略执行日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of strategy_log
-- ----------------------------
INSERT INTO `strategy_log` VALUES (1, 1, 1, 'success', '设备设备名01进行:策略信息任务操作', '2025-11-11 09:29:48');
INSERT INTO `strategy_log` VALUES (2, 1, 2, 'success', '设备设备名02进行:策略信息任务操作', '2025-11-11 09:29:48');
INSERT INTO `strategy_log` VALUES (3, 1, 3, 'success', '设备设备名03进行:策略信息任务操作', '2025-11-11 09:29:48');
INSERT INTO `strategy_log` VALUES (4, 1, 4, 'success', '设备设备名04进行:策略信息任务操作', '2025-11-11 09:29:48');
INSERT INTO `strategy_log` VALUES (5, 1, 1, 'success', '设备设备名01进行:策略信息任务操作', '2025-11-11 09:33:05');
INSERT INTO `strategy_log` VALUES (6, 1, 2, 'success', '设备设备名02进行:策略信息任务操作', '2025-11-11 09:33:05');
INSERT INTO `strategy_log` VALUES (7, 1, 3, 'success', '设备设备名03进行:策略信息任务操作', '2025-11-11 09:33:05');
INSERT INTO `strategy_log` VALUES (8, 1, 4, 'success', '设备设备名04进行:策略信息任务操作', '2025-11-11 09:33:05');
INSERT INTO `strategy_log` VALUES (9, 1, 1, 'success', '设备设备名01进行:策略信息任务操作', '2025-11-11 09:34:32');
INSERT INTO `strategy_log` VALUES (10, 1, 2, 'success', '设备设备名02进行:策略信息任务操作', '2025-11-11 09:34:32');
INSERT INTO `strategy_log` VALUES (11, 1, 3, 'success', '设备设备名03进行:策略信息任务操作', '2025-11-11 09:34:32');
INSERT INTO `strategy_log` VALUES (12, 1, 4, 'success', '设备设备名04进行:策略信息任务操作', '2025-11-11 09:34:33');
INSERT INTO `strategy_log` VALUES (13, 1, 1, 'success', '设备设备名01进行:策略信息任务操作', '2025-11-11 09:35:13');
INSERT INTO `strategy_log` VALUES (14, 1, 2, 'success', '设备设备名02进行:策略信息任务操作', '2025-11-11 09:35:13');
INSERT INTO `strategy_log` VALUES (15, 1, 3, 'success', '设备设备名03进行:策略信息任务操作', '2025-11-11 09:35:13');
INSERT INTO `strategy_log` VALUES (16, 1, 4, 'success', '设备设备名04进行:策略信息任务操作', '2025-11-11 09:35:13');

-- ----------------------------
-- Table structure for strategy_template
-- ----------------------------
DROP TABLE IF EXISTS `strategy_template`;
CREATE TABLE `strategy_template`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `strategy_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '策略名称',
  `script_path` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '策略方法',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '策略描述',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `dev_id` varchar(2550) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '选择的设备IDs',
  `groups` varchar(2550) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '选择的分组信息',
  `all_dev_ids` varchar(2550) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备IDs + 分组中的 设备id 去重整合  单独的 id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '策略模板表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of strategy_template
-- ----------------------------
INSERT INTO `strategy_template` VALUES (1, '策略信息', 'task.getDouyin', '描述', '2025-11-07 08:05:08', '设备名01_1,设备名02_2,设备名04_4,设备名03_3', '小红书_设备名01_1,小红书_设备名02_2,油管01_设备名04_4', '1,2,4,3,1,2,4');

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor`  (
  `info_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作系统',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '提示消息',
  `login_time` datetime(0) NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `idx_sys_logininfor_s`(`status`) USING BTREE,
  INDEX `idx_sys_logininfor_lt`(`login_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 70 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统访问记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------
INSERT INTO `sys_logininfor` VALUES (1, 'admin', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '退出成功', '2025-11-05 06:03:40');
INSERT INTO `sys_logininfor` VALUES (2, 'admin', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-05 06:03:43');
INSERT INTO `sys_logininfor` VALUES (3, 'admin', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-06 00:49:27');
INSERT INTO `sys_logininfor` VALUES (4, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-06 00:50:05');
INSERT INTO `sys_logininfor` VALUES (5, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '退出成功', '2025-11-06 01:05:32');
INSERT INTO `sys_logininfor` VALUES (6, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-06 01:05:36');
INSERT INTO `sys_logininfor` VALUES (7, 'admin', '192.168.150.114', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-06 01:15:20');
INSERT INTO `sys_logininfor` VALUES (8, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-06 01:51:05');
INSERT INTO `sys_logininfor` VALUES (9, 'admin', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-06 01:59:56');
INSERT INTO `sys_logininfor` VALUES (10, 'admin', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-06 02:49:38');
INSERT INTO `sys_logininfor` VALUES (11, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-06 02:49:43');
INSERT INTO `sys_logininfor` VALUES (12, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-06 05:56:03');
INSERT INTO `sys_logininfor` VALUES (13, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-06 06:40:01');
INSERT INTO `sys_logininfor` VALUES (14, 'admin', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-06 08:09:27');
INSERT INTO `sys_logininfor` VALUES (15, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-07 00:59:01');
INSERT INTO `sys_logininfor` VALUES (16, 'admin', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-07 01:22:36');
INSERT INTO `sys_logininfor` VALUES (17, 'admin', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '退出成功', '2025-11-07 01:26:26');
INSERT INTO `sys_logininfor` VALUES (18, 'user', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-07 01:26:30');
INSERT INTO `sys_logininfor` VALUES (19, 'user', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '退出成功', '2025-11-07 01:26:30');
INSERT INTO `sys_logininfor` VALUES (20, 'user', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-07 01:26:32');
INSERT INTO `sys_logininfor` VALUES (21, 'user', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '退出成功', '2025-11-07 01:26:32');
INSERT INTO `sys_logininfor` VALUES (22, 'user', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-07 01:26:37');
INSERT INTO `sys_logininfor` VALUES (23, 'user', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '退出成功', '2025-11-07 01:26:37');
INSERT INTO `sys_logininfor` VALUES (24, 'user', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-07 01:27:18');
INSERT INTO `sys_logininfor` VALUES (25, 'user', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '退出成功', '2025-11-07 01:27:18');
INSERT INTO `sys_logininfor` VALUES (26, 'user', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-07 01:27:24');
INSERT INTO `sys_logininfor` VALUES (27, 'user', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '退出成功', '2025-11-07 01:27:24');
INSERT INTO `sys_logininfor` VALUES (28, 'user', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-07 01:28:29');
INSERT INTO `sys_logininfor` VALUES (29, 'user', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '退出成功', '2025-11-07 01:47:15');
INSERT INTO `sys_logininfor` VALUES (30, 'admin', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-07 01:47:18');
INSERT INTO `sys_logininfor` VALUES (31, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-07 01:55:11');
INSERT INTO `sys_logininfor` VALUES (32, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-07 02:50:46');
INSERT INTO `sys_logininfor` VALUES (33, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-07 03:28:17');
INSERT INTO `sys_logininfor` VALUES (34, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-07 05:44:19');
INSERT INTO `sys_logininfor` VALUES (35, 'user', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-07 06:02:23');
INSERT INTO `sys_logininfor` VALUES (36, 'user', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '退出成功', '2025-11-07 06:02:24');
INSERT INTO `sys_logininfor` VALUES (37, 'user', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-07 06:03:48');
INSERT INTO `sys_logininfor` VALUES (38, 'user', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-07 06:05:11');
INSERT INTO `sys_logininfor` VALUES (39, 'user', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '退出成功', '2025-11-07 06:19:31');
INSERT INTO `sys_logininfor` VALUES (40, 'user', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-07 06:20:37');
INSERT INTO `sys_logininfor` VALUES (41, 'admin123', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '1', '用户不存在/密码错误', '2025-11-07 06:23:20');
INSERT INTO `sys_logininfor` VALUES (42, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-07 06:23:28');
INSERT INTO `sys_logininfor` VALUES (43, 'user', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-07 08:30:17');
INSERT INTO `sys_logininfor` VALUES (44, 'admin', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-10 01:33:51');
INSERT INTO `sys_logininfor` VALUES (45, 'admin123', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '1', '用户不存在/密码错误', '2025-11-10 01:47:10');
INSERT INTO `sys_logininfor` VALUES (46, 'admin123', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '1', '用户不存在/密码错误', '2025-11-10 01:47:17');
INSERT INTO `sys_logininfor` VALUES (47, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-10 01:47:22');
INSERT INTO `sys_logininfor` VALUES (48, 'admin', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-11 00:54:47');
INSERT INTO `sys_logininfor` VALUES (49, 'admin', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '退出成功', '2025-11-11 01:29:54');
INSERT INTO `sys_logininfor` VALUES (50, 'admin', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-11 01:30:01');
INSERT INTO `sys_logininfor` VALUES (51, 'admin', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '退出成功', '2025-11-11 01:30:41');
INSERT INTO `sys_logininfor` VALUES (52, 'user', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-11 01:30:49');
INSERT INTO `sys_logininfor` VALUES (53, 'user', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '退出成功', '2025-11-11 01:31:04');
INSERT INTO `sys_logininfor` VALUES (54, 'ce11', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-11 01:31:30');
INSERT INTO `sys_logininfor` VALUES (55, 'ce11', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '退出成功', '2025-11-11 01:33:10');
INSERT INTO `sys_logininfor` VALUES (56, 'admin', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-11 01:33:15');
INSERT INTO `sys_logininfor` VALUES (57, 'admin', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '退出成功', '2025-11-11 01:33:40');
INSERT INTO `sys_logininfor` VALUES (58, 'user111', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '1', '用户不存在/密码错误', '2025-11-11 01:33:46');
INSERT INTO `sys_logininfor` VALUES (59, 'user111', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '1', '用户不存在/密码错误', '2025-11-11 01:33:49');
INSERT INTO `sys_logininfor` VALUES (60, 'user', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-11 01:33:59');
INSERT INTO `sys_logininfor` VALUES (61, 'user', '192.168.150.147', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-11 01:35:17');
INSERT INTO `sys_logininfor` VALUES (62, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-11 01:40:54');
INSERT INTO `sys_logininfor` VALUES (63, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-21 05:45:41');
INSERT INTO `sys_logininfor` VALUES (64, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '退出成功', '2025-11-21 05:48:04');
INSERT INTO `sys_logininfor` VALUES (65, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-21 05:48:08');
INSERT INTO `sys_logininfor` VALUES (66, 'admin', '192.168.150.141', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-21 05:48:54');
INSERT INTO `sys_logininfor` VALUES (67, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-21 05:49:19');
INSERT INTO `sys_logininfor` VALUES (68, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-21 07:05:48');
INSERT INTO `sys_logininfor` VALUES (69, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2025-12-08 04:28:05');
INSERT INTO `sys_logininfor` VALUES (70, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2025-12-08 05:51:59');
INSERT INTO `sys_logininfor` VALUES (71, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '退出成功', '2025-12-08 06:14:18');
INSERT INTO `sys_logininfor` VALUES (72, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2025-12-08 06:14:21');
INSERT INTO `sys_logininfor` VALUES (73, 'admin', '192.168.0.27', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-08 06:19:35');
INSERT INTO `sys_logininfor` VALUES (74, 'admin', '192.168.0.27', '内网IP', 'Chrome 14', 'Windows 10', '0', '退出成功', '2025-12-08 06:25:43');
INSERT INTO `sys_logininfor` VALUES (75, 'admin', '192.168.0.27', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-08 06:25:54');
INSERT INTO `sys_logininfor` VALUES (76, 'admin', '192.168.0.27', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-08 07:12:39');

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '模块标题',
  `business_type` int(0) NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求方式',
  `operator_type` int(0) NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '返回参数',
  `status` int(0) NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint(0) NULL DEFAULT 0 COMMENT '消耗时间',
  PRIMARY KEY (`oper_id`) USING BTREE,
  INDEX `idx_sys_oper_log_bt`(`business_type`) USING BTREE,
  INDEX `idx_sys_oper_log_s`(`status`) USING BTREE,
  INDEX `idx_sys_oper_log_ot`(`oper_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '操作日志记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------
INSERT INTO `sys_oper_log` VALUES (1, '操作日志', 9, 'com.phone.web.controller.monitor.SysOperlogController.clean()', 'DELETE', 1, 'admin', NULL, '/monitor/operlog/clean', '192.168.150.147', '内网IP', '{}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-11-11 01:30:16', 0);
INSERT INTO `sys_oper_log` VALUES (2, '任务模板', 2, 'com.phone.module.controller.TaskTemplateController.edit()', 'PUT', 1, 'admin', NULL, '/module/taskTemplate', '127.0.0.1', '内网IP', '{\"scriptPath\":\"方法1\",\"strategyName\":\"测试001\",\"description\":\" 描述11\",\"id\":1,\"params\":{}}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-08 04:33:58', 0);
INSERT INTO `sys_oper_log` VALUES (3, '任务模板', 2, 'com.phone.module.controller.TaskTemplateController.edit()', 'PUT', 1, 'admin', NULL, '/module/taskTemplate', '127.0.0.1', '内网IP', '{\"scriptPath\":\"方法1\",\"strategyName\":\"测试001\",\"description\":\" 描述1\",\"id\":1,\"params\":{}}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-08 04:34:03', 0);
INSERT INTO `sys_oper_log` VALUES (4, '任务模板', 1, 'com.phone.module.controller.TaskTemplateController.add()', 'POST', 1, 'admin', NULL, '/module/taskTemplate', '127.0.0.1', '内网IP', '{\"scriptPath\":\"2\",\"strategyName\":\"2\",\"createTime\":1765168448332,\"description\":\"2\",\"id\":2,\"params\":{}}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-08 04:34:08', 0);
INSERT INTO `sys_oper_log` VALUES (5, 'VideoTags', 1, 'com.phone.module.controller.VideoTagsController.add()', 'POST', 1, 'admin', NULL, '/module/VideoTags', '127.0.0.1', '内网IP', '{\"devId\":\"设备名03_3\",\"douyinId\":\"cc123asd121312\",\"params\":{},\"tags\":\"视频\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-08 07:43:38', 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 103, 'admin', 'admin', '00', 'admin@163.com', '15888888888', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '192.168.0.27', '2025-12-08 15:12:40', 'admin', '2024-05-31 07:59:41', '', '2025-12-08 07:12:39', '管理员');
INSERT INTO `sys_user` VALUES (2, 105, 'user', 'user111', '00', 'user@qq.com', '15666666666', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '192.168.150.147', '2025-11-11 09:35:17', 'admin', '2024-05-31 07:59:41', 'admin', '2025-11-11 01:35:17', '测试员');
INSERT INTO `sys_user` VALUES (100, NULL, 'test', 'admin2222ccc', '00', '', '', '3', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '', NULL, 'admin', '2025-11-05 02:34:09', 'admin', '2025-11-05 06:14:17', NULL);
INSERT INTO `sys_user` VALUES (101, NULL, 'qwe', 'area2', '00', '', '', '3', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '', NULL, 'admin', '2025-11-05 02:34:15', 'admin', '2025-11-05 02:43:37', NULL);
INSERT INTO `sys_user` VALUES (102, NULL, 'ce11', '123123', '00', '', '', '3', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '192.168.150.147', '2025-11-11 09:31:31', 'admin', '2025-11-05 02:47:28', '', '2025-11-11 01:31:30', NULL);

-- ----------------------------
-- Table structure for task_log
-- ----------------------------
DROP TABLE IF EXISTS `task_log`;
CREATE TABLE `task_log`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `strategy_id` bigint(0) NULL DEFAULT NULL COMMENT '策略ID',
  `target_id` bigint(0) NOT NULL COMMENT '目标ID',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '执行结果',
  `log_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '执行日志详情',
  `execute_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '执行时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_strategy_log_template`(`strategy_id`) USING BTREE,
  CONSTRAINT `task_log_ibfk_1` FOREIGN KEY (`strategy_id`) REFERENCES `strategy_template` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务执行日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of task_log
-- ----------------------------
INSERT INTO `task_log` VALUES (1, 1, 1, 'success', '设备设备名01进行:测试00112312任务操作', '2025-11-07 17:34:55');
INSERT INTO `task_log` VALUES (2, 1, 2, 'success', '设备设备名02进行:测试00112312任务操作', '2025-11-07 17:34:55');
INSERT INTO `task_log` VALUES (3, 1, 3, 'success', '设备设备名03进行:测试00112312任务操作', '2025-11-07 17:34:55');
INSERT INTO `task_log` VALUES (4, 1, 4, 'success', '设备设备名04进行:测试00112312任务操作', '2025-11-07 17:34:55');
INSERT INTO `task_log` VALUES (5, 1, 2, 'success', '设备设备名02进行:测试00112312任务操作', '2025-11-07 17:35:16');
INSERT INTO `task_log` VALUES (6, 1, 3, 'success', '设备设备名03进行:测试00112312任务操作', '2025-11-07 17:35:16');
INSERT INTO `task_log` VALUES (7, 1, 4, 'success', '设备设备名04进行:测试00112312任务操作', '2025-11-07 17:35:16');
INSERT INTO `task_log` VALUES (8, 1, 2, 'success', '设备设备名02进行:测试00112312任务操作', '2025-11-07 17:35:37');
INSERT INTO `task_log` VALUES (9, 1, 3, 'success', '设备设备名03进行:测试00112312任务操作', '2025-11-07 17:35:38');
INSERT INTO `task_log` VALUES (10, 1, 4, 'success', '设备设备名04进行:测试00112312任务操作', '2025-11-07 17:35:38');
INSERT INTO `task_log` VALUES (11, 1, 2, 'success', '设备设备名02进行:测试00112312任务操作', '2025-11-07 17:35:39');
INSERT INTO `task_log` VALUES (12, 1, 3, 'success', '设备设备名03进行:测试00112312任务操作', '2025-11-07 17:35:39');
INSERT INTO `task_log` VALUES (13, 1, 4, 'success', '设备设备名04进行:测试00112312任务操作', '2025-11-07 17:35:39');
INSERT INTO `task_log` VALUES (14, 1, 2, 'success', '设备设备名02进行:测试00112312任务操作', '2025-11-07 17:35:52');
INSERT INTO `task_log` VALUES (15, 1, 3, 'success', '设备设备名03进行:测试00112312任务操作', '2025-11-07 17:35:52');
INSERT INTO `task_log` VALUES (16, 1, 4, 'success', '设备设备名04进行:测试00112312任务操作', '2025-11-07 17:35:52');
INSERT INTO `task_log` VALUES (17, 1, 1, 'success', '设备设备名01进行:测试00112312任务操作', '2025-11-10 09:34:53');
INSERT INTO `task_log` VALUES (18, 1, 2, 'success', '设备设备名02进行:测试00112312任务操作', '2025-11-10 09:34:53');
INSERT INTO `task_log` VALUES (19, 1, 3, 'success', '设备设备名03进行:测试00112312任务操作', '2025-11-10 09:34:53');
INSERT INTO `task_log` VALUES (20, 1, 4, 'success', '设备设备名04进行:测试00112312任务操作', '2025-11-10 09:34:53');
INSERT INTO `task_log` VALUES (21, 1, 1, 'success', '设备设备名01进行:测试00112312任务操作', '2025-11-11 09:08:20');
INSERT INTO `task_log` VALUES (22, 1, 2, 'success', '设备设备名02进行:测试00112312任务操作', '2025-11-11 09:08:20');
INSERT INTO `task_log` VALUES (23, 1, 3, 'success', '设备设备名03进行:测试00112312任务操作', '2025-11-11 09:08:20');
INSERT INTO `task_log` VALUES (24, 1, 4, 'success', '设备设备名04进行:测试00112312任务操作', '2025-11-11 09:08:20');
INSERT INTO `task_log` VALUES (25, 1, 2, 'success', '设备设备名02进行:测试00112312任务操作', '2025-11-11 09:10:25');
INSERT INTO `task_log` VALUES (26, 1, 3, 'success', '设备设备名03进行:测试00112312任务操作', '2025-11-11 09:10:25');
INSERT INTO `task_log` VALUES (27, 1, 4, 'success', '设备设备名04进行:测试00112312任务操作', '2025-11-11 09:10:25');

-- ----------------------------
-- Table structure for task_template
-- ----------------------------
DROP TABLE IF EXISTS `task_template`;
CREATE TABLE `task_template`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `strategy_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '策略名称',
  `script_path` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '策略方法',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '策略描述',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `dev_id` varchar(2550) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '选择的设备IDs',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务模板表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of task_template
-- ----------------------------
INSERT INTO `task_template` VALUES (1, '测试001', '方法1', ' 描述1', '2025-11-07 17:18:01', NULL);
INSERT INTO `task_template` VALUES (2, '2', '2', '2', '2025-12-08 12:34:08', NULL);

-- ----------------------------
-- Table structure for video
-- ----------------------------
DROP TABLE IF EXISTS `video`;
CREATE TABLE `video`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `video_index` bigint(0) NOT NULL COMMENT '视频ID',
  `dev_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备id',
  `douyin_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '抖音ID',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '评论内容',
  `video_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '视频地址',
  `image_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '页面截图地址',
  `json_string` varchar(2550) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'json串（视频uuid，点赞，评论，分享，收藏数）',
  `json_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评论文件地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '视频总表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of video
-- ----------------------------
INSERT INTO `video` VALUES (1, 1, 'ec86e946', 'EQLQ', '2025-12-08 13:01:15', '[{\"nickname\":\"帆高\",\"id\":0,\"text\":\"之前是什么[发呆]怎么关注了\",\"time\":\"34分钟前\"},{\"nickname\":\"老杜\",\"id\":1,\"text\":\"这个红车子很漂亮，但是我没得钱买。只能看一看。[赞][赞][赞]\",\"time\":\"2天前\"},{\"nickname\":\"襄城区军哥百货\",\"id\":2,\"text\":\"看着就舒服又飒！\",\"time\":\"昨天11:25\"},{\"nickname\":\"当阳推晋\",\"id\":3,\"text\":\"又飒又美！\",\"time\":\"昨天11:25\"},{\"nickname\":\"追忆早安📷\",\"id\":4,\"text\":\"豪车配美女，你值得拥有\",\"time\":\"2天前\"},{\"nickname\":\"温润的树呀\",\"replyTo\":\"\",\"id\":5,\"text\":\"同问。好离谱[泪奔]\",\"time\":\"10分钟前\"},{\"nickname\":\"用户1505446106171\",\"replyTo\":\"\",\"id\":6,\"text\":\"[流泪]\",\"time\":\"2天前\"},{\"nickname\":\"喔唷呀\",\"id\":7,\"text\":\"车和穿搭都不刻意但超亮眼！\",\"time\":\"昨天11:25\"},{\"nickname\":\"车圈闻所长\",\"replyTo\":\"\",\"id\":8,\"text\":\"[玫瑰]\",\"time\":\"3小时前\"},{\"nickname\":\"辣妈雪雪\",\"id\":9,\"text\":\"这车现在多少钱啊！\",\"time\":\"昨天11:25\"},{\"nickname\":\"忘三里\",\"id\":10,\"text\":\"穿搭好有品味！\",\"time\":\"昨天11:25\"},{\"nickname\":\"成汉\",\"id\":11,\"text\":\"牛逼[赞]\",\"time\":\"2025-12-08 12:59:22\"},{\"nickname\":\"\",\"replyTo\":\"\",\"id\":12,\"text\":\"[玫瑰]\",\"time\":\"3小时前\"},{\"nickname\":\"车圈闻所长\",\"replyTo\":\"\",\"id\":13,\"text\":\"不少钱[呲牙]\",\"time\":\"3小时前\"},{\"nickname\":\"车圈闻所长\",\"replyTo\":\"\",\"id\":14,\"text\":\"谢谢姐妹[色]\",\"time\":\"3小时前\"},{\"nickname\":\"当阳惺吵\",\"id\":15,\"text\":\"老粉来啦～\",\"time\":\"昨天11:25\"},{\"nickname\":\"幸福快乐\",\"id\":16,\"text\":\"好车\",\"time\":\"2天前\"},{\"nickname\":\"用户1505446106171\",\"replyTo\":\"\",\"id\":17,\"text\":\"[耶][耶][舔屏][舔屏][爱心]\",\"time\":\"2天前\"},{\"nickname\":\"车圈闻所长\",\"replyTo\":\"\",\"id\":18,\"text\":\"[色]\",\"time\":\"3小时前\"},{\"nickname\":\"侣蓑衣\",\"id\":19,\"text\":\"有生之年我能开上就好了！\",\"time\":\"昨天11:25\"},{\"nickname\":\"单曲循环\",\"id\":20,\"text\":\"内容太好了[太阳]\",\"time\":\"2天前\"},{\"nickname\":\"用户1505446106171\",\"replyTo\":\"\",\"id\":21,\"text\":\"[耶][耶]\",\"time\":\"2天前\"},{\"nickname\":\"\",\"replyTo\":\"用户1505446106171\",\"id\":22,\"text\":\"[比心]\",\"time\":\"1天前\"},{\"nickname\":\"阿乐\",\"id\":23,\"text\":\"恭喜发财！[玫瑰]多谢回访！[玫瑰]\",\"time\":\"2天前\"},{\"nickname\":\"大地红色集\",\"id\":24,\"text\":\"适配度100分！\",\"time\":\"昨天11:25\"},{\"nickname\":\"花古衣\",\"id\":25,\"text\":\"车主穿搭这么有味道！\",\"time\":\"2025-12-08 12:59:45\"},{\"nickname\":\"车圈闻所长\",\"replyTo\":\"\",\"id\":26,\"text\":\"哈哈加油\",\"time\":\"3小时前\"},{\"nickname\":\"\",\"id\":27,\"text\":\"适配度100分！\",\"time\":\"昨天11:25\"},{\"nickname\":\"依依团购\",\"id\":28,\"text\":\"好看[玫瑰]\",\"time\":\"1天前\"},{\"nickname\":\"wan.521\",\"id\":29,\"text\":\"这品味没话说\",\"time\":\"昨天11:25\"},{\"nickname\":\"阳光女孩\",\"id\":30,\"text\":\"已破百粉，真推[比心]\",\"time\":\"2天前\"},{\"nickname\":\"车圈闻所长\",\"replyTo\":\"\",\"id\":31,\"text\":\"哈哈快关注我\",\"time\":\"3小时前\"},{\"nickname\":\"\",\"id\":32,\"text\":\"这品味没话说\",\"time\":\"昨天11:25\"},{\"nickname\":\"无忧...\",\"id\":33,\"text\":\"488 的质感没话说\",\"time\":\"昨天11:25\"},{\"nickname\":\"好奇.\",\"id\":34,\"text\":\"豪车配美女真漂亮[赞][赞][赞]\",\"time\":\"1天前\"},{\"nickname\":\"你笑一下嘛，\",\"id\":35,\"text\":\"车品好绝啊！\",\"time\":\"昨天11:25\"},{\"nickname\":\"不离不弃\",\"id\":36,\"text\":\"看着就好有品味\",\"time\":\"2025-12-08 12:59:55\"},{\"nickname\":\"车圈闻所长\",\"replyTo\":\"\",\"id\":37,\"text\":\"是的 实车更炫\",\"time\":\"3小时前\"},{\"nickname\":\"闹闹不睡觉\",\"id\":38,\"text\":\"车主穿搭简约又高级！\",\"time\":\"昨天11:29\"},{\"nickname\":\"车圈闻所长\",\"replyTo\":\"\",\"id\":39,\"text\":\"哈哈 谢谢夸奖[玫瑰]\",\"time\":\"3小时前\"},{\"nickname\":\"农村小阿妹\",\"id\":40,\"text\":\"穿搭也超有品！\",\"time\":\"昨天11:25\"},{\"nickname\":\"德灯昏雨\",\"id\":41,\"text\":\"车和穿搭都长在我的审美点上～\",\"time\":\"昨天11:25\"},{\"nickname\":\"永似去如\",\"id\":42,\"text\":\"车帅人更飒！\",\"time\":\"昨天11:25\"},{\"nickname\":\"丽萍。\",\"id\":43,\"text\":\"车品颜值双高\",\"time\":\"昨天11:25\"},{\"nickname\":\"严姨\",\"id\":44,\"text\":\"人车都好漂亮！\",\"time\":\"昨天11:25\"},{\"nickname\":\"单曲循环\",\"id\":45,\"text\":\"内容太好了[比心]\",\"time\":\"1天前\"},{\"nickname\":\"兔姬的草莓园\",\"id\":46,\"text\":\"方向盘握得飒哦\",\"time\":\"昨天11:25\"},{\"nickname\":\"永德菜酸酸\",\"id\":47,\"text\":\"配车主的穿搭刚刚好！\",\"time\":\"昨天11:25\"},{\"nickname\":\"\",\"id\":48,\"text\":\"方向盘握得飒哦\",\"time\":\"昨天11:25\"},{\"nickname\":\"蝴蝶🦋🦋🦋🦋\",\"id\":49,\"text\":\"法拉利质感没得说\",\"time\":\"昨天11:25\"},{\"nickname\":\"德县旧少汉\",\"id\":50,\"text\":\"红法拉！穿搭不错哈\",\"time\":\"昨天11:25\"},{\"nickname\":\"欧萍爱健身\",\"id\":51,\"text\":\"品味真在线！\",\"time\":\"昨天11:25\"},{\"nickname\":\"井开岳典\",\"id\":52,\"text\":\"日常羡慕！\",\"time\":\"昨天11:25\"},{\"nickname\":\"界贝赛\",\"id\":53,\"text\":\"红色488太顶了\",\"time\":\"昨天11:25\"},{\"nickname\":\"好奇宝宝诶。\",\"id\":54,\"text\":\"开着法拉利好惬意！\",\"time\":\"昨天11:25\"},{\"nickname\":\"琪琪不吃菜\",\"id\":55,\"text\":\"这红车也太显气质了\",\"time\":\"昨天11:25\"},{\"nickname\":\"蜜糖系女孩\",\"id\":56,\"text\":\"车和穿搭都长在审美上\",\"time\":\"昨天11:25\"},{\"nickname\":\"\",\"id\":57,\"text\":\"这红车也太显气质了\",\"time\":\"昨天11:25\"},{\"nickname\":\"仙女很瘦啦\",\"id\":58,\"text\":\"别挡我看车！\",\"time\":\"昨天11:25\"},{\"nickname\":\"水果姐姐\",\"id\":59,\"text\":\"让开！我只想看车！\",\"time\":\"昨天11:25\"},{\"nickname\":\"车圈闻所长\",\"replyTo\":\"\",\"id\":60,\"text\":\"[玫瑰]谢谢\",\"time\":\"3小时前\"},{\"nickname\":\"车圈闻所长\",\"replyTo\":\"\",\"id\":61,\"text\":\"[微笑]\",\"time\":\"3小时前\"},{\"nickname\":\"车圈闻所长\",\"replyTo\":\"\",\"id\":62,\"text\":\"[微笑]就不让\",\"time\":\"2025-12-08 13:00:26\"},{\"nickname\":\"崇雅\",\"id\":63,\"text\":\"红车 + 神仙穿搭，也太会选了\",\"time\":\"昨天11:25\"},{\"nickname\":\"哆哆22\",\"id\":64,\"text\":\"法拉利这抹红色太炫了\",\"time\":\"昨天11:25\"},{\"nickname\":\"车圈闻所长\",\"replyTo\":\"\",\"id\":65,\"text\":\"[玫瑰]谢谢啦\",\"time\":\"3小时前\"},{\"nickname\":\"吉彦小亿亿的直播号\",\"id\":66,\"text\":\"送我就好了\",\"time\":\"昨天11:25\"},{\"nickname\":\"李涵～单身\",\"id\":67,\"text\":\"大女主标配啊这车\",\"time\":\"昨天11:25\"},{\"nickname\":\"\",\"replyTo\":\"\",\"id\":68,\"text\":\"[玫瑰]谢谢啦\",\"time\":\"3小时前\"},{\"nickname\":\"车圈闻所长\",\"replyTo\":\"\",\"id\":69,\"text\":\"是的 太炫了\",\"time\":\"3小时前\"},{\"nickname\":\"车圈闻所长\",\"replyTo\":\"\",\"id\":70,\"text\":\"嘿嘿我也想要\",\"time\":\"3小时前\"},{\"nickname\":\"车圈闻所长\",\"replyTo\":\"\",\"id\":71,\"text\":\"谢谢你[色]\",\"time\":\"2025-12-08 13:00:37\"},{\"nickname\":\"泡泡糖~\",\"id\":72,\"text\":\"车帅就算了，穿搭还这么会！\",\"time\":\"昨天11:25\"},{\"nickname\":\"满满的爱\",\"id\":73,\"text\":\"送我我也养不起啊\",\"time\":\"昨天11:25\"},{\"nickname\":\"车圈闻所长\",\"replyTo\":\"\",\"id\":74,\"text\":\"谢谢[色]\",\"time\":\"3小时前\"},{\"nickname\":\"车圈闻所长\",\"replyTo\":\"\",\"id\":75,\"text\":\"[呲牙]那就看看\",\"time\":\"3小时前\"},{\"nickname\":\"襄城区伟哥百货店（个体工商户）\",\"id\":76,\"text\":\"有钱养不起系列\",\"time\":\"昨天11:25\"},{\"nickname\":\"祥芮本\",\"id\":77,\"text\":\"车和穿搭都好绝\",\"time\":\"昨天11:25\"},{\"nickname\":\"牡丹花开\",\"id\":78,\"text\":\"488就应该配美女\",\"time\":\"昨天11:25\"},{\"nickname\":\"车圈闻所长\",\"replyTo\":\"\",\"id\":79,\"text\":\"[色]谢谢\",\"time\":\"2025-12-08 13:00:46\"},{\"nickname\":\"慈成熙\",\"id\":80,\"text\":\"法拉利太酷啦\",\"time\":\"昨天11:25\"},{\"nickname\":\"璐璐姐1\",\"id\":81,\"text\":\"女生也可以很帅\",\"time\":\"昨天11:25\"},{\"nickname\":\"珂珂儿\",\"id\":82,\"text\":\"红车太亮眼了\",\"time\":\"昨天11:25\"},{\"nickname\":\"幸福在当下\",\"id\":83,\"text\":\"488 也太帅了吧\",\"time\":\"昨天11:25\"},{\"nickname\":\"\",\"id\":84,\"text\":\"红车太亮眼了\",\"time\":\"昨天11:25\"},{\"nickname\":\"叮当小蓝人\",\"id\":85,\"text\":\"我也想开\",\"time\":\"2天前\"},{\"nickname\":\"钟岳汇\",\"id\":86,\"text\":\"送我我也能勉强接受\",\"time\":\"昨天11:25\"},{\"nickname\":\"恋风之歌\",\"id\":87,\"text\":\"说实话给我我不敢开\",\"time\":\"昨天11:25\"},{\"nickname\":\"\",\"id\":88,\"text\":\"送我我也能勉强接受\",\"time\":\"昨天11:25\"},{\"nickname\":\"绒琬\",\"id\":89,\"text\":\"488的红太显眼了！\",\"time\":\"昨天11:25\"},{\"nickname\":\"她的镜中花\",\"id\":90,\"text\":\"这车不少钱吧\",\"time\":\"昨天11:25\"},{\"nickname\":\"车圈闻所长\",\"replyTo\":\"\",\"id\":91,\"text\":\"哈哈我来开也是害怕\",\"time\":\"3小时前\"},{\"nickname\":\"车圈闻所长\",\"replyTo\":\"\",\"id\":92,\"text\":\"[呲牙]是的\",\"time\":\"2025-12-08 13:01:00\"},{\"nickname\":\"雍雅凡凡\",\"id\":93,\"text\":\"法拉利果然得配这种神仙穿搭\",\"time\":\"昨天11:25\"},{\"nickname\":\"车圈闻所长\",\"replyTo\":\"\",\"id\":94,\"text\":\"[呲牙]谢谢\",\"time\":\"3小时前\"},{\"nickname\":\"招财猫的手\",\"id\":95,\"text\":\"优秀[赞][赞][赞]\",\"time\":\"1天前\"},{\"nickname\":\"李振英\",\"id\":96,\"text\":\"优秀作品[赞][赞][赞][赞]\",\"time\":\"1天前\"},{\"nickname\":\"爱笑的胖头鱼\",\"id\":97,\"text\":\"健康+快乐+富有每一天[比心][爱心][666][666][爱心][比心]\",\"time\":\"1天前\"},{\"nickname\":\"共田八\",\"id\":98,\"text\":\"好车\",\"time\":\"1天前\"},{\"nickname\":\"美漫漫好物\",\"id\":99,\"text\":\"每天都喜欢来你这逛逛🌹🌹\",\"time\":\"2天前\"},{\"nickname\":\"\",\"replyTo\":\"\",\"id\":100,\"text\":\"[呲牙]谢谢\",\"time\":\"3小时前\"}]', '/profile/video/video_001_1765169907389_part01.mp4', '/profile/images/screenshot_001_1765169941175.png', '{\"likes\":\"42000\",\"shares\":\"\",\"collects\":\"\"}', '/profile/videosDir/video_17651700734140_comments.json');

-- ----------------------------
-- Table structure for video_tags
-- ----------------------------
DROP TABLE IF EXISTS `video_tags`;
CREATE TABLE `video_tags`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `dev_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备id',
  `douyin_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '抖音ID',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '勾选相关需要运行的项目',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '账号需求数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of video_tags
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
