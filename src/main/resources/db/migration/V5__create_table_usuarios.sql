
create table usuarios(

        id bigint generated by default as identity not null,
        login varchar(100) not null,
        clave varchar(300) not null,

        primary key(id)

);