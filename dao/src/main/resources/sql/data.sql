create table gifts.gift_certificate
(
    id               bigint auto_increment
        primary key,
    name             varchar(255)                       not null,
    description      varchar(255)                       not null,
    price            decimal(19, 2)                     not null,
    duration         int                                not null,
    create_date      datetime default CURRENT_TIMESTAMP not null,
    last_update_date datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);

create table gifts.orders
(
    id            bigint auto_increment
        primary key,
    cost          decimal(19, 2)                     not null,
    creation_date datetime default CURRENT_TIMESTAMP not null
);

create table gifts.orders_certificates
(
    order_id       bigint not null,
    certificate_id bigint not null,
    constraint certificate_order_fk
        foreign key (certificate_id) references gifts.gift_certificate (id)
            on delete cascade,
    constraint order_certificate_fk
        foreign key (order_id) references gifts.orders (id)
);

create index certificate_order_fk_idx
    on gifts.orders_certificates (certificate_id);

create index order_certificate_fk_idx
    on gifts.orders_certificates (order_id);

create table gifts.revinfo
(
    rev      int auto_increment
        primary key,
    revtstmp bigint null
);

create table gifts.gift_certificate_aud
(
    id               bigint                             not null,
    rev              int                                not null,
    revtype          tinyint                            null,
    create_date      datetime default CURRENT_TIMESTAMP null,
    description      varchar(255)                       null,
    duration         int                                null,
    last_update_date datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    name             varchar(255)                       null,
    price            decimal(19, 2)                     null,
    primary key (id, rev),
    constraint FKhriym6x1m3uyap2l3lxfmitku
        foreign key (rev) references gifts.revinfo (rev)
);

create table gifts.orders_aud
(
    id            bigint                             not null,
    rev           int                                not null,
    revtype       tinyint                            null,
    cost          decimal(19, 2)                     null,
    creation_date datetime default CURRENT_TIMESTAMP null,
    primary key (id, rev),
    constraint FKinujab7ljkelflu16c9jjch19
        foreign key (rev) references gifts.revinfo (rev)
);

create table gifts.tag
(
    id   bigint auto_increment
        primary key,
    name varchar(255) not null,
    constraint UK_1wdpsed5kna2y38hnbgrnhi5b
        unique (name)
);

create table gifts.certificates_tags
(
    certificate_id bigint not null,
    tag_id         bigint not null,
    constraint certificate_tag_fk
        foreign key (certificate_id) references gifts.gift_certificate (id),
    constraint tag_certificate_fk
        foreign key (tag_id) references gifts.tag (id)
);

create index certificate_tag_fk_idx
    on gifts.certificates_tags (certificate_id);

create index tag_certificate_fk_idx
    on gifts.certificates_tags (tag_id);

create table gifts.tag_aud
(
    id      bigint       not null,
    rev     int          not null,
    revtype tinyint      null,
    name    varchar(255) null,
    primary key (id, rev),
    constraint FKep272jdrgxgmq608l5y3792jn
        foreign key (rev) references gifts.revinfo (rev)
);

create table gifts.token_pair
(
    id                         bigint auto_increment
        primary key,
    access_token               varchar(255) not null,
    refresh_token              varchar(255) not null,
    refresh_token_expired_time timestamp    not null
);

create table gifts.user
(
    id       bigint auto_increment
        primary key,
    login    varchar(150)               not null,
    password varchar(255)               not null,
    role     varchar(50) default 'USER' not null,
    constraint user_name_uindex
        unique (login)
);

create table gifts.user_aud
(
    id       bigint                     not null,
    rev      int                        not null,
    revtype  tinyint                    null,
    login    varchar(150)               null,
    password varchar(150)               null,
    role     varchar(50) default 'USER' null,
    primary key (id, rev),
    constraint FK89ntto9kobwahrwxbne2nqcnr
        foreign key (rev) references gifts.revinfo (rev)
);

create table gifts.users_orders
(
    user_id  bigint not null,
    order_id bigint not null,
    constraint order_user_fk
        foreign key (order_id) references gifts.orders (id),
    constraint user_order_fk
        foreign key (user_id) references gifts.user (id)
);

create index order_user_fk_idx
    on gifts.users_orders (order_id);

create index user_order_fk_idx
    on gifts.users_orders (user_id);

