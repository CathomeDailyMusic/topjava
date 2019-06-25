DELETE FROM meals;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (user_id, datetime, description, calories)
VALUES (100000, '2019-06-03T10:00', 'Завтрак пользователь', 500),
       (100000, '2019-06-03T14:00', 'Обед пользователь', 1000),
       (100000, '2019-06-03T19:00', 'Ужин пользователь', 500),
       (100000, '2019-06-04T10:00', 'Завтрак пользователь', 500),
       (100000, '2019-06-04T14:00', 'Обед пользователь', 1000),
       (100000, '2019-06-04T19:00', 'Ужин пользователь', 700),
       (100001, '2019-06-01T10:00', 'Завтрак админ', 200),
       (100001, '2019-06-01T14:00', 'Обед админ', 1200),
       (100001, '2019-06-01T19:00', 'Ужин админ', 500),
       (100001, '2019-06-02T10:00', 'Завтрак админ', 300),
       (100001, '2019-06-02T14:00', 'Обед админ', 1100),
       (100001, '2019-06-02T19:00', 'Ужин админ', 700);