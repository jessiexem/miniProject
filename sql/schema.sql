-- drop database if exists
drop schema if exists dictionarydb;

create schema dictionarydb;

use dictionarydb;

create table user (
    user_id int not null auto_increment,
    username varchar(32) not null,
    password varchar(256) not null,
    created_date datetime default (CURRENT_TIMESTAMP),

    primary key(user_id)
);

create table user_details (

    user_id int not null,
    first_name varchar(128) not null,
    last_name varchar(128) not null,
    email varchar(128) not null,
    gender enum ('M','F'),

    primary key(user_id),

    constraint fk_user_id_1
    foreign key (user_id)
    references user(user_id)
);

create table user_quiz_activity (

    quiz_activity_id int not null auto_increment,
    user_id int not null,
    test_type varchar(16) not null,
    difficulty_level int,
    quiz_score int,
    start_time datetime default (CURRENT_TIMESTAMP),
    end_time datetime,

    primary key(quiz_activity_id),

    constraint fk_user_id_2
        foreign key (user_id)
        references user(user_id)

);

create table favourite (
	record_id int not null auto_increment,
    user_id int not null,
    word varchar(32) not null,
    created_date datetime default (CURRENT_TIMESTAMP),

    primary key(record_id),

    constraint fk_user_id_3
        foreign key (user_id)
        references user(user_id)
);