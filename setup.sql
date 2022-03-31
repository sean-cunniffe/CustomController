DROP SCHEMA IF EXISTS custom_controller_db;
CREATE SCHEMA custom_controller_db;

USE `custom_controller_db`;

DROP TABLE IF EXISTS user;
CREATE TABLE user
(
    user_id   int primary key AUTO_INCREMENT,
    email     VARCHAR(100) NOT NULL UNIQUE,
    firstName VARCHAR(50)  NOT NULL,
    lastName  VARCHAR(50)  NOT NULL,
    password  VARCHAR(150) NOT NULL,
    role      VARCHAR(50)  NOT NULL
);

DROP TABLE IF EXISTS shipping_details;
CREATE TABLE shipping_details
(
    shipping_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id     INT,
    deliver_to  VARCHAR(100) NOT NULL,
    street      VARCHAR(100) NOT NULL,
    street2      VARCHAR(100) NOT NULL,
    city        VARCHAR(100) NOT NULL,
    country     VARCHAR(100) NOT NULL,
    county     VARCHAR(100) NOT NULL,
    zip     VARCHAR(100) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (user_id)
);

DROP TABLE IF EXISTS payment_details;
CREATE TABLE payment_details
(
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id    INT NOT NULL,
    cardNumber VARCHAR(16),
    nameOnCard VARCHAR(100),
    cvv        VARCHAR(3),
    expiryDate varchar(10),
    FOREIGN KEY (user_id) REFERENCES user (user_id)
);

DROP TABLE IF EXISTS customer_order;
CREATE TABLE customer_order
(
    order_id    INT PRIMARY KEY AUTO_INCREMENT,
    user_id     int NOT NULL,
    status      VARCHAR(15),
    shipping_id INT NOT NULL,
    payment_id  INT NOT NULL,
    date_ordered Date NOT NULL,
    FOREIGN KEY (shipping_id) REFERENCES shipping_details (shipping_id),
    FOREIGN KEY (user_id) REFERENCES user (user_id),
    FOREIGN KEY (payment_id) REFERENCES payment_details (payment_id)
);

DROP TABLE IF EXISTS controller;
CREATE TABLE controller
(
    controller_id   int PRIMARY KEY NOT NULL AUTO_INCREMENT,
    controller_type varchar(100) NOT NULL,
    pattern         varchar(100),
    color           varchar(100) NOT NULL,
    img_url         varchar(200) NOT NULL,
    price           double NOT NULL
);

DROP TABLE IF EXISTS order_controllers;
CREATE TABLE order_controllers
(
    order_id      int NOT NULL,
    controller_id int NOT NULL,
    FOREIGN KEY (order_id) REFERENCES customer_order (order_id),
    FOREIGN KEY (controller_id) REFERENCES controller (controller_id)
);

#DATA
insert into user(email, firstName, lastName, password, role) values ('sean.cunniffe@gmail.com','Sean','Cunniffe',
                        '$2a$12$wZBbkcqBN1q5BdAaB/9k5OLCk3S19deOqSDVQp.HQJW31ZL8kr0qG',
                       'STAFF');
insert into user(email, firstName, lastName, password, role) values ('john.doe@gmail.com','John','Doe',
                                                                    '$2a$12$wZBbkcqBN1q5BdAaB/9k5OLCk3S19deOqSDVQp.HQJW31ZL8kr0qG',
                                                                    'CUSTOMER');
insert into shipping_details(user_id, deliver_to, street,street2, city, country, county, zip)
values (2, 'John Doe', 'Thornfort', 'Killimor', 'Ballinasloe', 'Galway','Ireland', 'ABCD123');

insert into payment_details (user_id, cardNumber, nameOnCard, cvv, expiryDate)
values (2, '1234567891234567', 'John Doe', '123', '01/2023');

insert into customer_order (user_id, status, shipping_id, payment_id, date_ordered)
values (2, 'SHIPPING',1,1,DATE('2022-02-15'));

insert into controller (controller_type, pattern, color, img_url, price)
values
       ('PlayStation4', 'Impact front', 'Purple', 'assets/images/ps4/scuf-custom-impact-front-energon.png', 55.99),
       ('PlayStation4', 'White flower', 'Purple', 'assets/images/ps4/flower-white.png', 39.99),
       ('PlayStation4', 'Star Storm', 'Multicolor', 'assets/images/ps4/star-storm.png', 39.99),
       ('PlayStation4', 'Dragon Skin', 'Red', 'assets/images/ps4/dragon-skin-red.png', 65.99),
       ('PlayStation4', 'Dragon Skin', 'Blue', 'assets/images/ps4/dragon-skin-blue.png', 65.99),
       ('PlayStation4', 'Adrenaline', 'Red', 'assets/images/ps4/Adrenaline.png', 44.99),
       ('PlayStation4', 'CyberStorm', 'Pink', 'assets/images/ps4/CyberStorm-pink.png', 44.99),
       ('PlayStation4', 'Energon', 'Blue', 'assets/images/ps4/Energon.png', 44.99),
       ('PlayStation4', 'BlindSide', 'Black', 'assets/images/ps4/blindside.png', 49.99),
       ('XboxOne', 'Energon', 'Blue', 'assets/images/xbox/Energon.png', 40.99),
       ('XboxOne', 'Nebula', 'Pink', 'assets/images/xbox/Nebula.png', 54.99),
       ('XboxOne', 'Prosperity', 'Red', 'assets/images/xbox/Prosperity.png', 56.99),
       ('XboxOne', 'StarStorm', 'Orange', 'assets/images/xbox/StarStorm.png', 40.99),
       ('XboxOne', 'Trance', 'Purple', 'assets/images/xbox/Trance.png', 47.99),
       ('XboxOne', 'Mystic', 'Multicolor', 'assets/images/xbox/Mystic.png', 44.99),
       ('XboxOne', 'Warhaul', 'Blue', 'assets/images/xbox/Warhaul.png', 55.99),
       ('XboxOne', 'Conjure', 'Blue', 'assets/images/xbox/Conjure.png', 51.99),
       ('XboxOne', 'Cherry Blossom', 'White', 'assets/images/xbox/CherryBlossom.png', 51.99),
       ('XboxOne', 'Celestrial', 'White', 'assets/images/xbox/Celestrial.png', 64.99),
       ('XboxOne', 'Magma', 'Orange', 'assets/images/xbox/Magma.png', 44.99),
       ('Switch', 'Green Apple', 'Green', 'assets/images/switch/green.webp', 78.99),
       ('Switch', 'Carbon', 'Grey', 'assets/images/switch/carbon.webp', 78.99),
       ('Switch', 'Black', 'Black', 'assets/images/switch/Black.webp', 68.99),
       ('Switch', 'Blue Flame', 'Blue', 'assets/images/switch/Blue-Flame.webp', 54.99),
       ('Switch', 'Game Boy Edition', 'Grey', 'assets/images/switch/GameBoy.webp', 64.99),
       ('Switch', 'Clear purple', 'Purple', 'assets/images/switch/Clear-Purple.webp', 44.99),
       ('Switch', 'IJen', 'Purple', 'assets/images/switch/Ijen.webp', 64.99),
       ('Switch', 'Joker', 'Multi-color', 'assets/images/switch/joker.webp', 64.99),
       ('Switch', 'Faded Blue', 'Multi-color', 'assets/images/switch/light-blue.webp', 64.99),
       ('Switch', 'Purple', 'Purple', 'assets/images/switch/purple.webp', 64.99),
       ('Switch', 'Stealth', 'Black', 'assets/images/switch/Stealth.webp', 64.99),
       ('PlayStation5', 'Black Void', 'Black', 'assets/images/ps5/black.png', 149.99),
       ('PlayStation5', 'White Blinding', 'White', 'assets/images/ps5/black.png', 174.99),
       ('PlayStation5', 'Scuf Reflex Pro', 'Grey', 'assets/images/ps5/reflex.png', 174.99),
       ('PlayStation5', 'Danger Orange', 'Orange', 'assets/images/ps5/orange.png', 174.99),
       ('PlayStation5', 'Blood Red', 'Red', 'assets/images/ps5/red.png', 174.99),
       ('PlayStation5', 'Pit Blue', 'Blue', 'assets/images/ps5/blue.png', 174.99);



insert into order_controllers (order_id, controller_id)
values (1, 1),
(1, 2);

