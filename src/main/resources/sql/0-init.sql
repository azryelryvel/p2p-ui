CREATE TABLE users (
    uuid varchar(36) NOT NULL,
    display_name varchar(10) NOT NULL UNIQUE,
    email varchar(256) NOT NULL,
    password varchar(256) NOT NULL,
    password_salt varchar(256) NOT NULL,
    PRIMARY KEY(uuid)
);

CREATE TABLE user_roles (
    role_name varchar(256) NOT NULL,
    uuid varchar(36) NOT NULL,
    PRIMARY KEY(role_name),
    CONSTRAINT `fk_user_role` FOREIGN KEY (uuid) REFERENCES users (uuid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE roles_permissions (
    permission varchar(256) NOT NULL,
    role_name varchar(256) NOT NULL,
    PRIMARY KEY(permission)
);

INSERT INTO roles_permissions (permission, role_name) values ('basic_view', 'user');

INSERT INTO roles_permissions (permission, role_name) values ('list_users', 'admin');
INSERT INTO roles_permissions (permission, role_name) values ('add_users', 'admin');
INSERT INTO roles_permissions (permission, role_name) values ('edit_users', 'admin');
INSERT INTO roles_permissions (permission, role_name) values ('delete_users', 'admin');
INSERT INTO roles_permissions (permission, role_name) values ('edit_role', 'admin');

