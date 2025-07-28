CREATE TABLE restaurants (
    uuid UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    image_url VARCHAR(255),
    is_open BOOLEAN NOT NULL
);