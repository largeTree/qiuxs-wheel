-- 行情表
CREATE TABLE `btc_market`(
	`id` BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	`type` TINYINT(2) NOT NULL COMMENT'虚拟币类型',
	`high` DECIMAL(6,6) NOT NULL COMMENT'高价',
	`low` DECIMAL(6,6) NOT NULL COMMENT'低价',
	`sell` DECIMAL(6,6) NOT NULL COMMENT'卖一价',
	`buy` DECIMAL(6,6) NOT NULL COMMENT'买一价',
	`last` DECIMAL(6,6) NOT NULL COMMENT'上次价',
	`vol` DECIMAL(20,6) NOT NULL COMMENT'销量',
	`time` BIGINT(10) NOT NULL COMMENT'btc返回的time字段',
	`createdDate` DATETIME NOT NULL COMMENT'创建时间'
);

-- 挂买单表
CREATE TABLE `btc_buy_order` (
	`id` BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	`type` TINYINT(2) NOT NULL COMMENT'虚拟币类型',
	`btc_order_id` VARCHAR(64) NOT NULL COMMENT'btc挂单id',
	`price` DECIMAL(6,6) NOT NULL COMMENT'单价',
	`num` DECIMAL(6,6) NOT NULL COMMENT'数量',
	`money` DECIMAL(6,6) NOT NULL COMMENT'总价',
	`btc_fee` DECIMAL(6,6) NOT NULL COMMENT'手续费',
	`sale_price` DECIMAL(6,6) NOT NULL COMMENT'超过此价格则可以卖出',
	`flag` TINYINT(2) NOT NULL COMMENT'单据状态',
	`createdDate` DATETIME NOT NULL COMMENT'创建时间',
	`finishDate` DATETIME NOT NULL COMMENT'完成时间',
	`cancelDate` DATETIME NOT NULL COMMENT'取消时间'
);

-- 挂卖单表
CREATE TABLE `btc_sale_order` (
	`id` BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	`type` TINYINT(2) NOT NULL COMMENT'虚拟币类型',
	`btc_order_id` VARCHAR(64) NOT NULL COMMENT'btc挂单id',
	`price` DECIMAL(6,6) NOT NULL COMMENT'单价',
	`num` DECIMAL(6,6) NOT NULL COMMENT'数量',
	`money` DECIMAL(6,6) NOT NULL COMMENT'总价',
	`btc_fee` DECIMAL(6,6) NOT NULL COMMENT'手续费',
	`flag` TINYINT(2) NOT NULL COMMENT'单据状态',
	`createdDate` DATETIME NOT NULL COMMENT'创建时间',
	`finishDate` DATETIME NOT NULL COMMENT'完成时间',
	`cancelDate` DATETIME NOT NULL COMMENT'取消时间'
);

SELECT MAX(`last`) FROM btc_market;