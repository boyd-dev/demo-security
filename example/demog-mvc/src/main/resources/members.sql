DROP TABLE IF EXISTS `members_roles`;
DROP TABLE IF EXISTS `members`;
DROP TABLE IF EXISTS `roles`;


CREATE TABLE `members` (
`user_id` VARCHAR(100) NOT NULL
,user_pass VARCHAR(88) NOT NULL
,enabled BOOLEAN NOT NULL
,PRIMARY KEY (user_id)
);

INSERT INTO `members` (user_id, user_pass, enabled) VALUES ('scott', '{noop}1234', TRUE);

CREATE TABLE `roles` (
`role_id` VARCHAR(100) NOT NULL
,role_name VARCHAR(100) NOT NULL
,role_desc VARCHAR(200)
,PRIMARY KEY (role_id)
);

INSERT INTO `roles` (role_id, role_name, role_desc) VALUES ('ROLE_USER', '일반사용자', NULL);
INSERT INTO `roles` (role_id, role_name, role_desc) VALUES ('ROLE_ADMIN', '관리자', NULL);
INSERT INTO `roles` (role_id, role_name, role_desc) VALUES ('ROLE_ANONYMOUS', '익명사용자', NULL);

CREATE TABLE `members_roles` (
`user_id` VARCHAR(100) NOT NULL
,role_id VARCHAR(100) NOT NULL
,UNIQUE uq_members_roles (user_id, role_id)
);

ALTER TABLE members_roles ADD CONSTRAINT fk_members FOREIGN KEY (user_id) REFERENCES members(user_id);
ALTER TABLE members_roles ADD CONSTRAINT fk_roles FOREIGN KEY (role_id) REFERENCES roles(role_id);

INSERT INTO `members_roles` (user_id, role_id) VALUES ('scott', 'ROLE_USER');
INSERT INTO `members_roles` (user_id, role_id) VALUES ('scott', 'ROLE_ADMIN');


