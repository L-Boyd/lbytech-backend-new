create schema lbytech_backend_new;

create table User
(
    id       int auto_increment comment '用户id'
        primary key,
    email    varchar(255) not null comment '用户邮箱',
    password varchar(255) not null comment '用户密码',
    create_time datetime not null comment '创建时间'
)
    comment '用户表';

create table notebook
(
    id          int      auto_increment comment '笔记id'
        primary key,
    file_name   varchar(255) not null comment '笔记名',
    file_url    text null comment '笔记地址',
    create_time datetime not null comment '创建时间'
)
    comment '笔记表';

alter table notebook
    add thumb_count int default 0 not null comment '点赞数' after file_url;

alter table notebook
    alter column create_time set default (CURRENT_TIMESTAMP);

alter table notebook
    add update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间';

create table thumb_record
(
    id          bigint auto_increment comment '记录id'
        primary key,
    user_email  varchar(255)                       not null comment '用户邮箱',
    notebook_id int                                not null comment '笔记id',
    create_time datetime default current_timestamp not null comment '创建时间',
    constraint thumb_record_uk
        unique (user_email, notebook_id)
)
    comment '点赞记录表';

CREATE TABLE `chat_memory` (
                               `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                               `session_id` VARCHAR(255) NOT NULL COMMENT '会话ID',
                               `content` TEXT NOT NULL COMMENT '消息内容',
                               PRIMARY KEY (`id`),
                               INDEX `idx_session_id` (`session_id`)
) COMMENT='对话记忆表';


CREATE TABLE `comment_info` (
                                    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                                    `user_email` varchar(255) NOT NULL COMMENT '用户Email',
                                    `notebook_id` bigint unsigned NOT NULL COMMENT '笔记id',
                                    `parent_id` bigint unsigned NOT NULL COMMENT '关联的1级评论id，如果是一级评论，则值为0',
                                    `like_count` int unsigned DEFAULT NULL COMMENT '点赞数',
                                    `status` tinyint unsigned DEFAULT NULL COMMENT '状态，0：审核中，1：正常，2：禁止查看, 3：等待人工审核，4：删除',
                                    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    `is_deleted` tinyint unsigned DEFAULT 0 COMMENT '逻辑删除字段，0：未删除，1：已删除',
                                    PRIMARY KEY (`id`)
) COMMENT = '评论信息表';

CREATE TABLE `comment_contents` (
                                    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                                    `comment_id` bigint unsigned NOT NULL COMMENT '关联的评论ID',
                                    `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
                                    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    `is_deleted` tinyint unsigned DEFAULT 0 COMMENT '逻辑删除字段，0：未删除，1：已删除',
                                    PRIMARY KEY (`id`),
                                    UNIQUE KEY `uk_comment_id` (`comment_id`),
                                    CONSTRAINT `fk_comment_content_comment` FOREIGN KEY (`comment_id`) REFERENCES `comment_info` (`id`)
) COMMENT = '评论内容表';

CREATE TABLE `comment_review_result` (
                                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                         `comment_id` bigint NOT NULL COMMENT '关联的评论ID',
                                         `result` varchar(10) NOT NULL COMMENT '审核结果：合规、违规、模糊',
                                         `confidence` double NOT NULL COMMENT '审核置信度，0-1之间的浮点数',
                                         `reason` text COMMENT '审核原因，对审核结果的解释',
                                         `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                         PRIMARY KEY (`id`),
                                         UNIQUE KEY `uk_comment_id` (`comment_id`)
) COMMENT='评论审核结果表';
