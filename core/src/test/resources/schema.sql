create schema gift_system;

create table tags
(
    id   int auto_increment
        primary key,
    name varchar(100) not null,
    constraint tags_name_uindex
        unique (name)
);

create table gift_certificates
(
    id               int auto_increment
        primary key,
    name             varchar(100)   not null,
    description      varchar(300)   not null,
    price            decimal(10, 2) not null,
    duration         int            not null,
    create_date      timestamp      not null,
    last_update_date timestamp      not null,
    constraint gift_certificates_name_uindex
        unique (name)
);

create table certificate_tag
(
    certificate_id int not null,
    tag_id         int not null,
    primary key (certificate_id, tag_id)
);