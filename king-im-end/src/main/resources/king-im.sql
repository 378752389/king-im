/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.6
 Source Server Type    : MySQL
 Source Server Version : 100218
 Source Host           : 192.168.1.6:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 100218
 File Encoding         : 65001

 Date: 09/08/2024 20:48:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_log
-- ----------------------------
DROP TABLE IF EXISTS `t_log`;
CREATE TABLE `t_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `operator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作人',
  `log` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作日志',
  `create_time` datetime NULL DEFAULT NULL,
  `ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1820357521508937731 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_log
-- ----------------------------
INSERT INTO `t_log` VALUES (1810954032529813506, 'admin', '上传文件：control.jpg, 文件大小：177751bytes', '2024-07-10 16:27:41', '');
INSERT INTO `t_log` VALUES (1810958498008203265, 'admin', '上传文件：control.jpg, 文件大小：177751bytes', '2024-07-10 16:45:26', '');
INSERT INTO `t_log` VALUES (1810958625888272386, 'admin', '上传文件：control.jpg, 文件大小：177751bytes', '2024-07-10 16:45:57', '');
INSERT INTO `t_log` VALUES (1810958767798366210, 'admin', '上传文件：control.jpg, 文件大小：177751bytes', '2024-07-10 16:46:30', '');
INSERT INTO `t_log` VALUES (1810960214111862785, 'admin', '上传文件：control.jpg, 文件大小：177751bytes', '2024-07-10 16:52:15', '');
INSERT INTO `t_log` VALUES (1810967602437046274, 'admin', '上传文件：control.jpg, 文件大小：177751bytes文件下载路径：http://localhost/im-upload/20240710/admin/bee712c53af43a70e0732f92ee588752.jpg', '2024-07-10 17:21:37', '');
INSERT INTO `t_log` VALUES (1810969550552842242, 'admin', '上传文件：control.jpg, 文件大小：177751bytes文件下载路径：http://localhost/im-upload/20240710/admin/bee712c53af43a70e0732f92ee588752.jpg', '2024-07-10 17:29:21', '');
INSERT INTO `t_log` VALUES (1816745655721381890, 'admin', '上传文件：OIP-C.jpg, 文件大小：8235bytes文件下载路径：http://localhost/im-upload/20240726/admin/aca9ae24904c8cc49c3a689403c08858.jpg', '2024-07-26 16:01:32', '');
INSERT INTO `t_log` VALUES (1818195536138854401, 'admin', '上传文件：note.png, 文件大小：10234bytes文件下载路径：http://localhost/im-upload/20240730/admin/5a9bbc66373b9f027e724405d9868552.png', '2024-07-30 16:02:50', '');
INSERT INTO `t_log` VALUES (1818198421241077762, 'admin', '上传文件：note.png, 文件大小：10234bytes文件下载路径：http://localhost/im-upload/20240730/admin/5a9bbc66373b9f027e724405d9868552.png', '2024-07-30 16:14:18', '');
INSERT INTO `t_log` VALUES (1818206713979871233, 'admin', '上传文件：note.png, 文件大小：10234bytes文件下载路径：http://localhost/im-upload/20240730/admin/5a9bbc66373b9f027e724405d9868552.png', '2024-07-30 16:47:15', '');
INSERT INTO `t_log` VALUES (1818206767553716225, 'admin', '上传文件：note.png, 文件大小：10234bytes文件下载路径：http://localhost/im-upload/20240730/admin/5a9bbc66373b9f027e724405d9868552.png', '2024-07-30 16:47:28', '');
INSERT INTO `t_log` VALUES (1818206989289877506, 'admin', '上传文件：note.png, 文件大小：10234bytes文件下载路径：http://localhost/im-upload/20240730/admin/5a9bbc66373b9f027e724405d9868552.png', '2024-07-30 16:48:21', '');
INSERT INTO `t_log` VALUES (1818207056155471874, 'admin', '上传文件：note.png, 文件大小：10234bytes文件下载路径：http://localhost/im-upload/20240730/admin/5a9bbc66373b9f027e724405d9868552.png', '2024-07-30 16:48:37', '');
INSERT INTO `t_log` VALUES (1818208105293508609, 'admin', '上传文件：note.png, 文件大小：10234bytes文件下载路径：http://localhost/im-upload/20240730/admin/5a9bbc66373b9f027e724405d9868552.png', '2024-07-30 16:52:47', '');
INSERT INTO `t_log` VALUES (1818208274244268034, 'admin', '上传文件：note.png, 文件大小：10234bytes文件下载路径：http://localhost/im-upload/20240730/admin/5a9bbc66373b9f027e724405d9868552.png', '2024-07-30 16:53:27', '');
INSERT INTO `t_log` VALUES (1818211745966080001, 'admin', '上传文件：note.png, 文件大小：10234bytes文件下载路径：http://localhost/im-upload/20240730/admin/5a9bbc66373b9f027e724405d9868552.png', '2024-07-30 17:07:15', '');
INSERT INTO `t_log` VALUES (1818212023469621250, 'admin', '上传文件：note.png, 文件大小：10234bytes文件下载路径：http://localhost/im-upload/20240730/admin/5a9bbc66373b9f027e724405d9868552.png', '2024-07-30 17:08:21', '');
INSERT INTO `t_log` VALUES (1818212351795544066, 'admin', '上传文件：note.png, 文件大小：10234bytes文件下载路径：http://localhost/im-upload/20240730/admin/5a9bbc66373b9f027e724405d9868552.png', '2024-07-30 17:09:40', '');
INSERT INTO `t_log` VALUES (1818212949915897857, 'admin', '上传文件：note.png, 文件大小：10234bytes文件下载路径：http://localhost/im-upload/20240730/admin/5a9bbc66373b9f027e724405d9868552.png', '2024-07-30 17:12:02', '');
INSERT INTO `t_log` VALUES (1818213182708158466, 'admin', '上传文件：note.png, 文件大小：10234bytes文件下载路径：http://localhost/im-upload/20240730/admin/5a9bbc66373b9f027e724405d9868552.png', '2024-07-30 17:12:58', '');
INSERT INTO `t_log` VALUES (1818221730745831426, 'admin', '上传文件：note.png, 文件大小：10234bytes文件下载路径：http://localhost/im-upload/20240730/admin/5a9bbc66373b9f027e724405d9868552.png', '2024-07-30 17:46:56', '');
INSERT INTO `t_log` VALUES (1818221801516322818, 'admin', '上传文件：note.png, 文件大小：10234bytes文件下载路径：http://localhost/im-upload/20240730/admin/5a9bbc66373b9f027e724405d9868552.png', '2024-07-30 17:47:13', '');
INSERT INTO `t_log` VALUES (1818221888288083969, 'admin', '上传文件：note.png, 文件大小：10234bytes文件下载路径：http://localhost/im-upload/20240730/admin/5a9bbc66373b9f027e724405d9868552.png', '2024-07-30 17:47:33', '');
INSERT INTO `t_log` VALUES (1818222051350040577, 'admin', '上传文件：note.png, 文件大小：10234bytes文件下载路径：http://localhost/im-upload/20240730/admin/5a9bbc66373b9f027e724405d9868552.png', '2024-07-30 17:48:12', '');
INSERT INTO `t_log` VALUES (1818464002813046785, 'admin', '上传文件：note.png, 文件大小：10234bytes文件下载路径：http://localhost/im-upload/20240731/admin/5a9bbc66373b9f027e724405d9868552.png', '2024-07-31 09:49:38', '');
INSERT INTO `t_log` VALUES (1818471391435984897, 'admin', '上传文件：note.png, 文件大小：10234bytes文件下载路径：http://localhost/im-upload/20240731/admin/5a9bbc66373b9f027e724405d9868552.png', '2024-07-31 10:18:59', '');
INSERT INTO `t_log` VALUES (1818471471681409025, 'admin', '上传文件：two.mp3, 文件大小：77416bytes文件下载路径：http://localhost/im-upload/20240731/admin/0361605781658531472e0208c30b0a6a.mp3', '2024-07-31 10:19:19', '');
INSERT INTO `t_log` VALUES (1818474041791516674, 'admin', '上传文件：two.mp3, 文件大小：77416bytes文件下载路径：http://localhost/im-upload/20240731/admin/0361605781658531472e0208c30b0a6a.mp3', '2024-07-31 10:29:31', '');
INSERT INTO `t_log` VALUES (1818476238314987522, 'admin', '上传文件：one.mp4, 文件大小：14710639bytes文件下载路径：http://localhost/im-upload/20240731/admin/6874ccbfa922440ec193e7aa9dc10fb4.mp4', '2024-07-31 10:38:15', '');
INSERT INTO `t_log` VALUES (1818476680453349378, 'admin', '上传文件：one.mp4, 文件大小：14710639bytes文件下载路径：http://localhost/im-upload/20240731/admin/6874ccbfa922440ec193e7aa9dc10fb4.mp4', '2024-07-31 10:40:00', '');
INSERT INTO `t_log` VALUES (1818535626681643010, 'wangwu', '上传文件：one.mp4, 文件大小：14710639bytes文件下载路径：http://localhost/im-upload/20240731/wangwu/6874ccbfa922440ec193e7aa9dc10fb4.mp4', '2024-07-31 14:34:14', '');
INSERT INTO `t_log` VALUES (1820304357476823042, 'wangwu', '上传文件：three.mp4, 文件大小：29698230bytes文件下载路径：http://localhost/im-upload/20240805/wangwu/f2b44f21d50e459d6815d9b2a4f76a87.mp4', '2024-08-05 11:42:33', '');
INSERT INTO `t_log` VALUES (1820304418487169026, 'wangwu', '上传文件：one.mp4, 文件大小：14710639bytes文件下载路径：http://localhost/im-upload/20240805/wangwu/6874ccbfa922440ec193e7aa9dc10fb4.mp4', '2024-08-05 11:42:47', '');
INSERT INTO `t_log` VALUES (1820357042817216514, 'admin', '上传文件：note.png, 文件大小：10234bytes文件下载路径：http://localhost/im-upload/20240805/admin/5a9bbc66373b9f027e724405d9868552.png', '2024-08-05 15:11:54', '');
INSERT INTO `t_log` VALUES (1820357521508937730, 'admin', '上传文件：note.png, 文件大小：10234bytes文件下载路径：http://localhost/im-upload/20240805/admin/5a9bbc66373b9f027e724405d9868552.png', '2024-08-05 15:13:48', '');

-- ----------------------------
-- Table structure for t_msg
-- ----------------------------
DROP TABLE IF EXISTS `t_msg`;
CREATE TABLE `t_msg`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `room_id` bigint NULL DEFAULT NULL,
  `from_uid` bigint NULL DEFAULT NULL,
  `to_uid` bigint NULL DEFAULT NULL,
  `type` int NULL DEFAULT NULL COMMENT '1-文本；2-图片；3-音频；4-视频；5-文件；',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `extra` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1 COMMENT '消息状态 1-待发送，2-已发送，3-已读',
  `create_time` datetime NULL DEFAULT NULL,
  `send_terminal` int NULL DEFAULT 1 COMMENT '1-WEB；2-PC；3-APP',
  `refer_msg_id` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '引用消息id',
  `at_uids` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'at 用户列表',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 111 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_msg
-- ----------------------------
INSERT INTO `t_msg` VALUES (1, 1, 1, NULL, 1, '彳亍', NULL, 1, '2024-07-11 10:00:36', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (2, 1, 1, NULL, 1, '不孬', NULL, 1, '2024-07-11 10:00:53', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (3, 1, 1, NULL, 1, '牛皮', NULL, 1, '2024-07-11 10:00:58', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (4, 4, 1, NULL, 1, '很好，很棒', NULL, 1, '2024-07-11 10:27:01', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (5, 4, 1, NULL, 1, 'nice', NULL, 1, '2024-07-11 10:27:21', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (6, 4, 1, NULL, 1, '不行，我得下了', NULL, 1, '2024-07-11 10:27:40', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (7, NULL, 1, 2, 1, '好的', NULL, 1, '2024-07-15 10:25:19', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (8, NULL, 1, 2, 1, '不孬', NULL, 1, '2024-07-15 10:25:29', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (9, NULL, 1, 2, 1, '222', NULL, 1, '2024-07-15 10:45:29', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (10, NULL, 1, 2, 1, '222\n', NULL, 1, '2024-07-15 10:48:04', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (11, NULL, 2, 1, 1, '3333', NULL, 1, '2024-07-15 10:50:59', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (12, NULL, 1, NULL, 1, 'hello, world!', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-26 16:48:54', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (13, NULL, 1, NULL, 1, 'hello, world!', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-26 16:51:45', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (14, NULL, 1, 3, 1, 'hello, world!', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-26 16:57:50', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (15, NULL, 1, 2, 1, 'hello, world!', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-26 17:00:51', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (16, NULL, 1, 1, 1, '22', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-26 17:35:26', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (17, NULL, 1, 1, 1, '22', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-26 17:35:31', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (18, NULL, 1, 1, 1, '22', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-26 17:35:34', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (19, NULL, 1, 1, 1, '22', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-26 17:36:47', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (20, NULL, 1, 1, 1, '22', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-26 17:41:11', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (21, NULL, 1, 1, 1, 'okok', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-26 17:44:11', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (22, NULL, 1, 1, 1, 'asd', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-26 17:44:17', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (23, NULL, 1, 1, 1, 'qq', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-26 17:45:13', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (24, NULL, 1, 1, 1, 'qqqqqqqq', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-26 17:53:17', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (25, NULL, 1, 5, 1, '你好', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 14:17:17', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (26, NULL, 1, 5, 1, '你好', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 14:17:24', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (27, NULL, 1, 5, 1, '不行', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 14:18:18', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (28, NULL, 1, 5, 1, '牛皮', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 14:20:23', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (29, NULL, 1, 5, 1, '权威', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 14:20:50', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (30, NULL, 1, 5, 1, '权威', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 14:20:54', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (31, NULL, 1, 5, 1, '权威', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 14:20:57', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (32, NULL, 1, 5, 1, '权威', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 14:21:04', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (33, NULL, 1, 5, 1, '权威', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 14:21:05', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (34, NULL, 1, 5, 1, '权威\n', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 14:21:05', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (35, NULL, 1, 5, 1, '好', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 14:21:47', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (36, NULL, 1, 5, 1, '请求', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 14:22:59', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (37, NULL, 1, 5, 1, '123 ', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 14:23:42', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (38, NULL, 1, 5, 1, '123', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 14:24:20', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (39, NULL, 1, 5, 1, '123', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 14:24:47', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (40, NULL, 1, 5, 1, '11', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 14:25:06', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (41, NULL, 1, 5, 1, '1231', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 14:27:25', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (42, NULL, 1, 5, 1, '111', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 14:27:45', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (43, NULL, 1, 5, 1, '123', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 14:30:24', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (44, NULL, 1, 5, 1, '1231', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 14:30:47', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (45, NULL, 1, 5, 1, '2131', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 14:30:52', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (46, NULL, 1, 5, 1, '111111', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 14:30:56', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (47, NULL, 1, 5, 1, '111', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 14:31:43', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (48, NULL, 2, 1, 1, '闹纯子', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 15:44:29', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (49, NULL, 5, 1, 1, '123', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 16:50:33', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (50, 4, 1, NULL, 1, '滚蛋', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 17:36:44', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (51, NULL, 4, 1, 1, '滚蛋', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-29 17:40:29', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (52, NULL, 1, 4, 1, '', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-30 16:02:50', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (53, NULL, 1, 4, 1, '', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-30 16:14:18', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (54, NULL, 1, 4, 2, '', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":{\"size\":null,\"name\":null,\"type\":null,\"url\":null},\"fileExtra\":null}', 1, '2024-07-30 16:52:47', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (55, NULL, 1, 4, 2, '', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":{\"size\":null,\"name\":null,\"type\":null,\"url\":null},\"fileExtra\":null}', 1, '2024-07-30 16:53:27', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (56, NULL, NULL, 4, 2, '', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":{\"size\":10234,\"name\":\"note.png\",\"type\":\"image/png\",\"url\":\"http://www.baidu.com\"},\"fileExtra\":null}', 1, NULL, 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (57, NULL, 1, 4, 2, '', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":{\"size\":null,\"name\":null,\"type\":null,\"url\":null},\"fileExtra\":null}', 1, '2024-07-30 17:07:15', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (58, NULL, 1, 4, 2, '', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":{\"size\":null,\"name\":null,\"type\":null,\"url\":null},\"fileExtra\":null}', 1, '2024-07-30 17:08:21', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (59, NULL, 1, 4, 2, '', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":{\"size\":null,\"name\":null,\"type\":null,\"url\":null},\"fileExtra\":null}', 1, '2024-07-30 17:10:19', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (60, NULL, 1, 4, 2, '', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":{\"size\":10234,\"name\":\"note.png\",\"type\":\"image/png\",\"url\":\"http://localhost/im-upload/20240730/admin/5a9bbc66373b9f027e724405d9868552.png\"},\"fileExtra\":null}', 1, '2024-07-30 17:12:08', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (61, NULL, 1, 4, 2, '', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":{\"size\":10234,\"name\":\"note.png\",\"type\":\"image/png\",\"url\":\"http://localhost/im-upload/20240730/admin/5a9bbc66373b9f027e724405d9868552.png\"},\"fileExtra\":null}', 1, '2024-07-30 17:13:01', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (62, NULL, 1, 4, 2, '', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":{\"size\":10234,\"name\":\"note.png\",\"type\":\"image/png\",\"url\":\"http://localhost/im-upload/20240730/admin/5a9bbc66373b9f027e724405d9868552.png\"},\"fileExtra\":null}', 1, '2024-07-30 17:46:56', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (63, NULL, 1, 4, 2, '', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":{\"size\":10234,\"name\":\"note.png\",\"type\":\"image/png\",\"url\":\"http://localhost/im-upload/20240730/admin/5a9bbc66373b9f027e724405d9868552.png\"},\"fileExtra\":null}', 1, '2024-07-30 17:47:13', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (64, NULL, 1, 4, 2, '', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":{\"size\":10234,\"name\":\"note.png\",\"type\":\"image/png\",\"url\":\"http://localhost/im-upload/20240730/admin/5a9bbc66373b9f027e724405d9868552.png\"},\"fileExtra\":null}', 1, '2024-07-30 17:47:33', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (65, NULL, 1, 4, 2, '', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":{\"size\":10234,\"name\":\"note.png\",\"type\":\"image/png\",\"url\":\"http://localhost/im-upload/20240730/admin/5a9bbc66373b9f027e724405d9868552.png\"},\"fileExtra\":null}', 1, '2024-07-30 17:48:12', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (66, NULL, 1, 4, 2, '', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":{\"size\":10234,\"name\":\"note.png\",\"type\":\"image/png\",\"url\":\"http://localhost/im-upload/20240731/admin/5a9bbc66373b9f027e724405d9868552.png\"},\"fileExtra\":null}', 1, '2024-07-31 09:49:38', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (67, NULL, 1, 4, 2, '', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":{\"size\":10234,\"name\":\"note.png\",\"type\":\"image/png\",\"url\":\"http://localhost/im-upload/20240731/admin/5a9bbc66373b9f027e724405d9868552.png\"},\"fileExtra\":null}', 1, '2024-07-31 10:18:59', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (68, NULL, 1, 4, 3, '', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":{\"size\":77416,\"name\":\"two.mp3\",\"type\":\"audio/mpeg\",\"url\":\"http://localhost/im-upload/20240731/admin/0361605781658531472e0208c30b0a6a.mp3\"},\"fileExtra\":null}', 1, '2024-07-31 10:19:19', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (69, NULL, 1, 4, 3, '', '{\"videoExtra\":null,\"audioExtra\":{\"size\":77416,\"second\":0,\"url\":\"http://localhost/im-upload/20240731/admin/0361605781658531472e0208c30b0a6a.mp3\"},\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-31 10:29:31', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (70, NULL, 1, 4, 4, '', '{\"videoExtra\":null,\"audioExtra\":{\"size\":14710639,\"second\":null,\"url\":\"http://localhost/im-upload/20240731/admin/6874ccbfa922440ec193e7aa9dc10fb4.mp4\"},\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-31 10:38:16', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (71, NULL, 1, 4, 4, '', '{\"videoExtra\":{\"size\":14710639,\"url\":\"http://localhost/im-upload/20240731/admin/6874ccbfa922440ec193e7aa9dc10fb4.mp4\",\"coverUrl\":null},\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-31 10:40:00', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (72, NULL, 1, 4, 1, '欧克', NULL, 1, '2024-07-31 12:17:37', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (73, NULL, 1, 4, 1, '滚蛋', NULL, 1, '2024-07-31 12:17:40', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (74, NULL, 1, 4, 1, '钱钱钱', NULL, 1, '2024-07-31 12:17:44', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (75, 4, 3, NULL, 1, 'ojbk', NULL, 1, '2024-07-31 12:30:29', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (76, 4, 3, NULL, 1, 'a', NULL, 1, '2024-07-31 12:30:37', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (77, 4, 3, NULL, 1, 'asdas', NULL, 1, '2024-07-31 12:30:42', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (78, 4, 1, NULL, 1, '行吧，先下了', NULL, 1, '2024-07-31 14:28:25', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (79, 3, 1, NULL, 1, '啊啊', NULL, 1, '2024-07-31 14:28:29', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (80, 3, 1, NULL, 1, '行', NULL, 1, '2024-07-31 14:30:57', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (81, 3, 1, NULL, 1, '你看着办', NULL, 1, '2024-07-31 14:30:59', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (82, 4, 4, NULL, 4, '', '{\"videoExtra\":{\"size\":14710639,\"url\":\"http://localhost/im-upload/20240731/wangwu/6874ccbfa922440ec193e7aa9dc10fb4.mp4\",\"coverUrl\":null},\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-07-31 14:34:14', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (83, 4, 4, NULL, 1, 'ok', NULL, 1, '2024-07-31 14:43:39', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (84, NULL, 4, 1, 1, 'no', NULL, 1, '2024-07-31 16:02:02', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (85, 4, 4, NULL, 1, 'ok', NULL, 1, '2024-07-31 16:06:57', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (86, 4, 4, NULL, 1, 'no', NULL, 1, '2024-07-31 16:07:02', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (87, 4, 4, NULL, 1, '请问', NULL, 1, '2024-07-31 16:09:09', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (88, 4, 4, NULL, 1, '滚', NULL, 1, '2024-07-31 16:10:21', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (89, 3, 4, NULL, 1, '阿松大钱钱钱哇哇哇哇巍峨呃呃嗡嗡嗡娃娃亲青蛙委屈委屈饿王企鹅王企鹅请问我去恶趣味请问请问请问我去饿请问趣味无穷额前问问去恶趣味请问请问请问我去饿请问请问恶趣味恶趣味请问请问请问请问去问我去饿我去恶趣味的撒旦撒旦撒旦阿斯顿撒大苏打撒旦撒大苏打阿大撒的撒大是大苏打趣味无穷萨芬大师傅上的肥肉趣味无穷饿撒打算大苏打', NULL, 1, '2024-07-31 17:37:32', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (90, 3, 4, NULL, 1, '请问请问', NULL, 1, '2024-07-31 17:41:41', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (91, 3, 4, NULL, 1, '去问去问驱蚊器饿我去恶趣味去问去问驱蚊器为请问去问驱蚊器为请问请问而我却恶趣味我去', NULL, 1, '2024-07-31 17:41:47', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (92, 3, 4, NULL, 1, NULL, NULL, 1, '2024-08-01 18:02:58', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (93, 3, 4, NULL, 1, '@king', NULL, 1, '2024-08-02 10:36:15', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (94, 3, 4, NULL, 1, '@', NULL, 1, '2024-08-02 10:37:30', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (95, 4, 4, NULL, 1, 'sfsdsfsdf\n\n\nsdfsdfs\n\n', NULL, 1, '2024-08-02 18:07:35', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (96, 4, 4, NULL, 1, 'sdfsdfsd\n\n\ndfs', NULL, 1, '2024-08-02 18:09:51', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (97, 4, 4, NULL, 1, 'asdasdasd@pepe sdfdsfdsfs', NULL, 1, '2024-08-02 18:11:20', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (98, 4, 4, NULL, 1, 'sdfsdfsdf\n\nddsfdsfs', NULL, 1, '2024-08-02 18:12:42', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (99, 4, 4, NULL, 1, '我不是黄蓉\n我不会武功\n我只要靖哥哥', NULL, 1, '2024-08-05 09:28:46', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (100, 3, 4, NULL, 1, '爱你所爱的人间\n想你所想的明天', NULL, 1, '2024-08-05 11:29:30', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (101, 3, 4, NULL, 1, '撒犯得上发射点', NULL, 1, '2024-08-05 11:31:00', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (102, 3, 4, NULL, 1, '天天\n小李\n宁次', NULL, 1, '2024-08-05 11:31:29', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (103, 3, 4, NULL, 4, '', '{\"videoExtra\":{\"size\":29698230,\"url\":\"http://localhost/im-upload/20240805/wangwu/f2b44f21d50e459d6815d9b2a4f76a87.mp4\",\"coverUrl\":null},\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-08-05 11:42:33', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (104, 3, 4, NULL, 4, '', '{\"videoExtra\":{\"size\":14710639,\"url\":\"http://localhost/im-upload/20240805/wangwu/6874ccbfa922440ec193e7aa9dc10fb4.mp4\",\"coverUrl\":null},\"audioExtra\":null,\"pictureExtra\":null,\"fileExtra\":null}', 1, '2024-08-05 11:42:47', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (105, NULL, 4, 1, 1, 'ojbk', NULL, 1, '2024-08-05 14:09:59', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (106, NULL, 4, 1, 1, 'objk', NULL, 1, '2024-08-05 14:13:19', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (107, NULL, 4, 1, 1, '行', NULL, 1, '2024-08-05 14:21:10', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (108, 4, 4, NULL, 1, '好不好', NULL, 1, '2024-08-05 14:21:18', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (109, 4, 1, NULL, 2, '', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":{\"size\":10234,\"name\":\"note.png\",\"type\":\"image/png\",\"url\":\"http://localhost/im-upload/20240805/admin/5a9bbc66373b9f027e724405d9868552.png\"},\"fileExtra\":null}', 1, '2024-08-05 15:11:54', 1, NULL, NULL);
INSERT INTO `t_msg` VALUES (110, 4, 1, NULL, 2, '', '{\"videoExtra\":null,\"audioExtra\":null,\"pictureExtra\":{\"size\":10234,\"name\":\"note.png\",\"type\":\"image/png\",\"url\":\"http://localhost/im-upload/20240805/admin/5a9bbc66373b9f027e724405d9868552.png\"},\"fileExtra\":null}', 1, '2024-08-05 15:13:48', 1, NULL, NULL);

-- ----------------------------
-- Table structure for t_room
-- ----------------------------
DROP TABLE IF EXISTS `t_room`;
CREATE TABLE `t_room`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `room_limit` int NULL DEFAULT 200,
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `leader_id` bigint NULL DEFAULT NULL,
  `notice` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `avatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '群头像',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_room
-- ----------------------------
INSERT INTO `t_room` VALUES (1, 'chat群', 200, 'king', 1, '111', NULL, NULL);
INSERT INTO `t_room` VALUES (2, 'test1', 200, 'king', 1, '111', NULL, NULL);
INSERT INTO `t_room` VALUES (3, 'test2', 200, 'king', 1, NULL, NULL, NULL);
INSERT INTO `t_room` VALUES (4, 'test3', 200, 'king', 1, '请不要手下留情！！！', NULL, NULL);
INSERT INTO `t_room` VALUES (6, 'test5', 200, 'w', 2, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nick_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gender` int NULL DEFAULT 0 COMMENT '0-未知；1-男生； 2-女生；',
  `sign` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '个性签名',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `register_type` int NULL DEFAULT NULL,
  `province` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录省份',
  `city` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录城市',
  `open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `last_login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `last_login_time` datetime NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, 'king', 1, '天道酬勤', 'https://picsum.photos/512/512?id=1', 'admin', '$2a$10$nKrPrVpFChqhSzybFKpZ1uUHP0rnRRjAbZ1Qg0UcaClTRg0WnwynW', 1, '广东', '深圳', NULL, '0:0:0:0:0:0:0:1', '2024-08-05 14:21:28', '2024-04-19 14:03:25');
INSERT INTO `t_user` VALUES (2, 'w', 2, '', 'https://picsum.photos/512/512?id=2', 'w', '$2a$10$nKrPrVpFChqhSzybFKpZ1uUHP0rnRRjAbZ1Qg0UcaClTRg0WnwynW', 1, '广东', '深圳', NULL, '0:0:0:0:0:0:0:1', '2024-07-29 16:44:56', '2024-04-19 14:03:25');
INSERT INTO `t_user` VALUES (3, 'ew', 2, '滚开', 'https://picsum.photos/512/512?id=3', 'ew', '$2a$10$nKrPrVpFChqhSzybFKpZ1uUHP0rnRRjAbZ1Qg0UcaClTRg0WnwynW', 1, '广东', '深圳', NULL, '0:0:0:0:0:0:0:1', '2024-07-31 14:31:31', '2024-04-19 14:03:25');
INSERT INTO `t_user` VALUES (4, '棘刺', 1, '这就是伊比利亚的至高之术', 'https://picsum.photos/512/512?id=4', 'jc', '$2a$10$nKrPrVpFChqhSzybFKpZ1uUHP0rnRRjAbZ1Qg0UcaClTRg0WnwynW', 1, '北京', '深圳', NULL, '0:0:0:0:0:0:0:1', '2024-07-31 14:34:56', '2024-04-19 14:03:25');
INSERT INTO `t_user` VALUES (5, '玛恩纳', 1, '他们不配我拔剑！', 'https://picsum.photos/512/512?id=5', 'men', '$2a$10$nKrPrVpFChqhSzybFKpZ1uUHP0rnRRjAbZ1Qg0UcaClTRg0WnwynW', 1, '上海', '深圳', NULL, '0:0:0:0:0:0:0:1', '2024-07-31 11:05:53', '2024-04-19 14:03:25');
INSERT INTO `t_user` VALUES (6, '斯卡蒂', 2, '不许乳蒂', 'https://picsum.photos/512/512?id=6', 'skd', '$2a$10$nKrPrVpFChqhSzybFKpZ1uUHP0rnRRjAbZ1Qg0UcaClTRg0WnwynW', 1, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for t_user_friend
-- ----------------------------
DROP TABLE IF EXISTS `t_user_friend`;
CREATE TABLE `t_user_friend`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL,
  `peer_id` bigint NULL DEFAULT NULL COMMENT '朋友id',
  `create_time` datetime NULL DEFAULT current_timestamp,
  `mark_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_friend
-- ----------------------------
INSERT INTO `t_user_friend` VALUES (1, 1, 2, '2024-07-11 11:11:27', NULL);
INSERT INTO `t_user_friend` VALUES (2, 2, 1, '2024-07-11 11:11:27', NULL);
INSERT INTO `t_user_friend` VALUES (3, 1, 4, '2024-07-12 16:43:26', '鸡翅2324');
INSERT INTO `t_user_friend` VALUES (4, 1, 5, '2024-07-12 16:43:28', NULL);
INSERT INTO `t_user_friend` VALUES (5, 4, 1, '2024-07-11 11:11:27', NULL);
INSERT INTO `t_user_friend` VALUES (6, 5, 1, '2024-07-11 11:11:27', NULL);
INSERT INTO `t_user_friend` VALUES (7, 2, 3, '2024-07-12 16:44:25', NULL);
INSERT INTO `t_user_friend` VALUES (8, 3, 2, '2024-07-12 16:44:25', NULL);
INSERT INTO `t_user_friend` VALUES (21, 4, 5, '2024-08-05 11:27:39', '玛恩纳');
INSERT INTO `t_user_friend` VALUES (22, 5, 4, '2024-08-05 11:27:39', '棘刺');

-- ----------------------------
-- Table structure for t_user_room
-- ----------------------------
DROP TABLE IF EXISTS `t_user_room`;
CREATE TABLE `t_user_room`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL,
  `room_id` bigint NULL DEFAULT NULL,
  `mark_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '群备注',
  `my_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户在群昵称',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `t_user_room__uk`(`user_id` ASC, `room_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_room
-- ----------------------------
INSERT INTO `t_user_room` VALUES (1, 1, 1, '我的测试群', '呵呵');
INSERT INTO `t_user_room` VALUES (2, 1, 2, '我的测试群2', '呵呵');
INSERT INTO `t_user_room` VALUES (3, 1, 3, 'test2', 'king');
INSERT INTO `t_user_room` VALUES (4, 1, 4, 'test3', 'king');
INSERT INTO `t_user_room` VALUES (6, 2, 1, 'chat群', 'w');
INSERT INTO `t_user_room` VALUES (7, 2, 4, 'test3', 'w');
INSERT INTO `t_user_room` VALUES (8, 4, 4, 'test3', '棘刺');
INSERT INTO `t_user_room` VALUES (9, 3, 4, 'test3', 'ew');
INSERT INTO `t_user_room` VALUES (10, 5, 4, 'test3', '玛恩纳');
INSERT INTO `t_user_room` VALUES (11, 4, 3, 'test2', '棘刺');
INSERT INTO `t_user_room` VALUES (12, 5, 3, 'test2', '玛恩纳');
INSERT INTO `t_user_room` VALUES (13, 4, 2, 'test1', '棘刺');
INSERT INTO `t_user_room` VALUES (14, 5, 2, 'test1', '玛恩纳');

SET FOREIGN_KEY_CHECKS = 1;
