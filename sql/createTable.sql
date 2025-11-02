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

create table file
(
    id          int      auto_increment comment '笔记id'
        primary key,
    file_name   varchar(255) not null comment '笔记名',
    file_url    text null comment '笔记地址',
    create_time datetime not null comment '创建时间'
)
    comment '笔记表';
