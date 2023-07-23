CREATE DATABASE cms_template;

CREATE USER cms_template_admin@localhost IDENTIFIED BY 'cms_template_admin';
GRANT ALL PRIVILEGES ON cms_template.* TO cms_template_admin@localhost;
FLUSH PRIVILEGES;

CREATE USER cms_template_admin@'%' IDENTIFIED BY 'cms_template_admin';
GRANT ALL PRIVILEGES ON cms_template.* TO cms_template_admin@'%';
FLUSH PRIVILEGES;

UPDATE mysql.user SET Super_Priv = 'Y' WHERE `user` = 'cms_template_admin';
FLUSH PRIVILEGES;
