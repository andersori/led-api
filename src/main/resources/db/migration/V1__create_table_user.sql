/*SCHEMA user_service CREATED BY FLYWAY*/

CREATE TABLE IF NOT EXISTS user_service.kratera_role
(
   user_id bigint NOT NULL,
   role varchar (20) NOT NULL,
   PRIMARY KEY
   (
      user_id,
      role
   )
);

CREATE TABLE IF NOT EXISTS user_service.kratera_user
(
   kratera_id bigserial NOT NULL,
   email varchar (200) NOT NULL UNIQUE,
   first_name varchar (100) NOT NULL,
   last_name varchar (100),
   password varchar (255) NOT NULL,
   verified int4 NOT NULL,
   PRIMARY KEY (kratera_id)
);

ALTER TABLE IF EXISTS user_service.kratera_role 
	ADD CONSTRAINT FKcqh4t917o1md4u2h4fmghdb93 
	FOREIGN KEY (user_id) 
	REFERENCES user_service.kratera_user;
