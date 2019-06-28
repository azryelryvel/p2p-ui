CREATE TABLE users (
    uuid varchar(36) NOT NULL,
    display_name varchar(10) NOT NULL UNIQUE,
    email varchar(256) NOT NULL,
    password varchar(256) NOT NULL,
    salt varchar(256) NOT NULL,
    locked boolean NOT NULL DEFAULT(0),
    PRIMARY KEY(uuid)
);

CREATE TABLE roles (
    id integer NOT NULL AUTO_INCREMENT,
    uuid varchar(36) NOT NULL,
    role varchar(256) NOT NULL,
    PRIMARY KEY(id),
    CONSTRAINT `fk_user_role` FOREIGN KEY (uuid) REFERENCES users (uuid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE permissions (
    id integer NOT NULL AUTO_INCREMENT,
    permission varchar(256) NOT NULL,
    role varchar(256) NOT NULL,
    PRIMARY KEY(id)
);

INSERT INTO permissions (role, permission) values ('USER', 'view:home');
INSERT INTO permissions (role, permission) values ('USER', 'view:dashboard');
INSERT INTO permissions (role, permission) values ('USER', 'view:downloads');
INSERT INTO permissions (role, permission) values ('USER', 'view:uploads');
INSERT INTO permissions (role, permission) values ('USER', 'view:settings');

INSERT INTO permissions (role, permission) values ('ADMIN', 'view:administration');
INSERT INTO permissions (role, permission) values ('ADMIN', 'users:list,add,remove,edit');

