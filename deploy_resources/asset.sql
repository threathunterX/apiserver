-- root/ threathunterMySQL347
-- nebula/threathunterNebula
-- grant all privileges on asset.* to 'nebula'@127.0.0.1
-- flush privileges;

CREATE DATABASE IF NOT EXISTS asset DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

use asset;

CREATE TABLE trunk (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `trunk` varchar(50) DEFAULT NULL COMMENT 'trunk url',
  `status` tinyint(4) DEFAULT NULL COMMENT '0为无效,1为有效',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `trunk_unique` (`trunk`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

create table porter (
	id int not null auto_increment,
	name char(40) not null,
	remark char(255),
	count int,
	`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
	`schema` char(40),
	`key` char(255),
	status TINYINT comment '0为无效,1为有效',
	primary key (id)
)ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;