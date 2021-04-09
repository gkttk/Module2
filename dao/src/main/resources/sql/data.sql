create table if not exists gift_certificate
(
    id               int auto_increment
        primary key,
    name             varchar(155) not null,
    description      varchar(255) not null,
    price            decimal      not null,
    duration         int          not null,
    create_date      varchar(255) not null,
    last_update_date varchar(255) not null
);

create table if not exists tag
(
    id   int auto_increment
        primary key,
    name varchar(155) not null,
    constraint tag_name_uindex
        unique (name)
);

create table if not exists certificates_tags
(
    certificate_id int not null,
    tag_id         int not null,
    constraint certificate_id_fk
        foreign key (certificate_id) references gift_certificate (id)
            on delete cascade,
    constraint tag_id_fk
        foreign key (tag_id) references tag (id)
            on delete cascade
);

DROP PROCEDURE IF EXISTS searchByPartOfDescription;
DROP PROCEDURE IF EXISTS searchByPartOfName;







