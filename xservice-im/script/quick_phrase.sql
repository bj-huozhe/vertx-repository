/*
 Navicat Premium Data Transfer

 Source Server         : 10.10.10.178(测试)
 Source Server Type    : MySQL
 Source Server Version : 50626
 Source Host           : 10.10.10.178
 Source Database       : mc_admin

 Target Server Type    : MySQL
 Target Server Version : 50626
 File Encoding         : utf-8

 Date: 10/18/2017 10:50:58 AM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `quick_phrase`
-- ----------------------------
DROP TABLE IF EXISTS `quick_phrase`;
CREATE TABLE `quick_phrase` (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `userId` int(16) DEFAULT NULL,
  `content` varchar(64) DEFAULT NULL,
  `createTime` bigint(16) DEFAULT NULL,
  `identity` int(2) DEFAULT NULL,
  `title` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userId` (`userId`,`identity`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
