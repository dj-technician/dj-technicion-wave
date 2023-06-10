create table bms_node (
    id bigint auto_increment,
    file_name varchar(255) not null,
    root_path varchar(255) not null,
    primary key (id)
)