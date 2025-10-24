create schema lbytech_backend_new;

create table User
(
    id       int auto_increment comment '用户id'
        primary key,
    email    varchar(255) not null comment '用户邮箱',
    password varchar(255) not null comment '用户密码'
)
    comment '用户表';