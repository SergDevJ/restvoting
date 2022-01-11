DROP TABLE IF EXISTS votes;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS dishes;
DROP TABLE IF EXISTS restaurants;


DROP SEQUENCE IF EXISTS rest_seq;
DROP SEQUENCE IF EXISTS menu_seq;
DROP SEQUENCE IF EXISTS dish_seq;
DROP SEQUENCE IF EXISTS user_seq;
DROP SEQUENCE IF EXISTS votes_seq;


CREATE SEQUENCE rest_seq START WITH 1000;
CREATE SEQUENCE menu_seq START WITH 1000;
CREATE SEQUENCE dish_seq START WITH 1000;
CREATE SEQUENCE user_seq START WITH 1000;
CREATE SEQUENCE votes_seq START WITH 10000;

CREATE TABLE restaurants
(
    id INTEGER PRIMARY KEY DEFAULT nextval('rest_seq'),
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    email VARCHAR(70) NOT NULL,
    CONSTRAINT restaurants_address_idx UNIQUE (address),
    CONSTRAINT restaurants_email_idx UNIQUE (email)
);
CREATE UNIQUE INDEX restaurants_unique_name_idx ON restaurants (name);


CREATE TABLE dishes
(
    id INTEGER PRIMARY KEY DEFAULT nextval('dish_seq'),
    name VARCHAR(100) NOT NULL,
    weight INTEGER NOT NULL
);
CREATE UNIQUE INDEX dishes_unique_name_idx ON dishes (name);


CREATE TABLE menu
(
    id INTEGER PRIMARY KEY DEFAULT nextval('menu_seq'),
    restaurant_id INTEGER NOT NULL,
    menu_date DATE DEFAULT now() NOT NULL,
    dish_id INTEGER NOT NULL,
    price INTEGER NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON DELETE CASCADE,
    FOREIGN KEY (dish_id) REFERENCES dishes (id) ON DELETE RESTRICT
);
CREATE UNIQUE INDEX menu_unique_rest_date_dish_idx ON menu (restaurant_id, menu_date, dish_id);


CREATE TABLE users
(
    id INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
    name VARCHAR(100) NOT NULL,
    email VARCHAR(70) NOT NULL,
    password VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX users_unique_name_idx ON users (name);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);


CREATE TABLE user_roles
(
    user_id INTEGER NOT NULL,
    role VARCHAR(20) NOT NULL,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);


CREATE TABLE votes
(
    id INTEGER PRIMARY KEY DEFAULT nextval('votes_seq'),
    user_id INTEGER NOT NULL,
    vote_date DATE NOT NULL DEFAULT now(),
    restaurant_id INTEGER NOT NULL,
    CONSTRAINT user_vote_idx UNIQUE (user_id, vote_date),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON DELETE CASCADE
)
