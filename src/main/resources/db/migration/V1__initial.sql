CREATE TABLE users(
	user_id bigint NOT NULL AUTO_INCREMENT,
	email varchar(100) NOT NULL,
	password varchar(300) NOT NULL,
	role tinyint NOT NULL,
	date_created DATETIME,
	last_updated DATETIME,
	PRIMARY KEY (user_id)
);

CREATE TABLE roles(
	role_id bigint NOT NULL AUTO_INCREMENT,
	name varchar(200) NOT NULL,
	created_at DATETIME,
	updated_at DATETIME,
	PRIMARY KEY (role_id)
);

CREATE TABLE user_roles(
 	user_roles_id bigint NOT NULL AUTO_INCREMENT,
	user_id bigint NOT NULL,
	role_id bigint NOT NULL,
	PRIMARY KEY (user_roles_id),
	FOREIGN KEY (role_id) references roles(role_id),
	FOREIGN KEY (user_id) references users(user_id)
);

CREATE TABLE post(
	post_id bigint NOT NULL AUTO_INCREMENT,
	image varchar(MAX) NOT NULL,
	title varchar(100) NOT NULL,
	contentOne varchar(MAX) NOT NULL,
	contentTwo varchar(MAX) NOT NULL,
	category varchar(100) NOT NULL,
	author varchar(100) NOT NULL,
	date_created DATETIME,
	last_updated DATETIME,
	PRIMARY KEY (post_id)
);


INSERT INTO roles (name, created_at, updated_at) VALUES ('ROLE_ADMIN', NOW(), NOW());
INSERT INTO roles (name, created_at, updated_at) VALUES ('ROLE_USER', NOW(), NOW());


