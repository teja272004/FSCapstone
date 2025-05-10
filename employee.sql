CREATE DATABASE employees;

USE employees;

CREATE TABLE IF NOT EXISTS users (
    id INT unique not null,
    username VARCHAR(50) primary key,  -- Username field
    password VARCHAR(255) NOT NULL,         -- Password field (hashed)
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL UNIQUE,
    job VARCHAR(100) NOT NULL,
    salary DECIMAL(10, 2) NOT NULL          -- Salary field
);
drop table users;
-- Insert an example user (password should ideally be hashed)
INSERT INTO users  VALUES
(1,'admin', 'password123', 'Alice Johnson', 'alice.johnson@example.com', '1234567890', 'Software Engineer', 80000.00),
(2,'user1', 'password456', 'Bob Smith', 'bob.smith@example.com', '0987654321', 'Project Manager', 95000.00),
(3,'user2', 'password789', 'Charlie Brown', 'charlie.brown@example.com', '5551234567', 'Data Analyst', 70000.00);
SELECT * FROM users WHERE username = 'admin';
SELECT * FROM users WHERE username = 'your_test_username';
select * from users;
CREATE TABLE attendance (
    user_id INT NOT NULL,
    login_time TIMESTAMP NOT NULL,
    date DATE NOT NULL,
    logout_time TIMESTAMP, -- Optional: if you want to track logout times
    status VARCHAR(20),
    attendance_count INT DEFAULT 0, -- New column to track attendance count
    PRIMARY KEY (user_id, date), -- Composite primary key on user_id and date
    FOREIGN KEY (user_id) REFERENCES users(id)
);

drop table attendance;
select * from attendance;


