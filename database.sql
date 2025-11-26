USE librarydb;

CREATE TABLE library_user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ROLE_USER', 'ROLE_ADMIN') NOT NULL
);

CREATE TABLE Book (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(100) NOT NULL,
    content TEXT,
    publication_year INT NOT NULL CHECK (publication_year >= 1000),
    isbn VARCHAR(17) NOT NULL,
    available BOOLEAN DEFAULT TRUE
);

CREATE TABLE Loan (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    loanDate DATE,
    returnDate DATE,
    status VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES library_user(id),
    FOREIGN KEY (book_id) REFERENCES Book(id)
);