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

