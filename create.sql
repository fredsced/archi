
    create table history (
       id bigint not null auto_increment,
        creation_time datetime,
        event varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table pcolumn (
       id bigint not null auto_increment,
        name varchar(30),
        rank_order integer,
        project_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table project (
       id bigint not null auto_increment,
        name varchar(30),
        primary key (id)
    ) engine=InnoDB;

    create table task (
       id bigint not null auto_increment,
        name varchar(100),
        rank_order integer,
        priority integer not null,
        column_id bigint,
        primary key (id)
    ) engine=InnoDB;

    alter table project 
       add constraint UK_3k75vvu7mevyvvb5may5lj8k7 unique (name);

    alter table pcolumn 
       add constraint FK8su4njjiygfxwomfrqefhger1 
       foreign key (project_id) 
       references project (id);

    alter table task 
       add constraint FKlv3j12nl4npyghmfckam38vqp 
       foreign key (column_id) 
       references pcolumn (id);
