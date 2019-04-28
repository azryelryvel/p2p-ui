CREATE TABLE users (
    uuid varchar(36) NOT NULL,
    display_name varchar(10) NOT NULL UNIQUE,
    email varchar(256) NOT NULL,
    password varchar(256) NOT NULL,
    salt varchar(256) NOT NULL,
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
    permission varchar(256) NOT NULL,
    role varchar(256) NOT NULL,
    PRIMARY KEY(permission)
);

INSERT INTO roles_permissions (permission, role_name) values ('home_page', 'USER');
INSERT INTO roles_permissions (permission, role_name) values ('dashboard_page', 'USER');
INSERT INTO roles_permissions (permission, role_name) values ('settings_page', 'USER');

INSERT INTO roles_permissions (permission, role_name) values ('list_users', 'ADMIN');
INSERT INTO roles_permissions (permission, role_name) values ('add_users', 'ADMIN');
INSERT INTO roles_permissions (permission, role_name) values ('edit_users', 'ADMIN');
INSERT INTO roles_permissions (permission, role_name) values ('delete_users', 'ADMIN');
INSERT INTO roles_permissions (permission, role_name) values ('edit_role', 'ADMIN');

