-- drop database if exists
drop schema if exists dictionarydb;

create schema dictionarydb;

use dictionarydb;

create table user (
    user_id int not null auto_increment,
    username varchar(32) not null,
    password varchar(256) not null,

    primary key(user_id)
);

create table favourite (
	record_id int not null auto_increment,
    user_id int not null,
    word varchar(32) not null,
    created_date date default (current_date),

    primary key(record_id),

    constraint fk_user_id
        foreign key (user_id)
        references user(user_id)
);