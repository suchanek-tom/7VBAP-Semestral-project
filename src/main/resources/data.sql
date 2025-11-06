INSERT INTO book (id, title, author, content, publication_year, isbn, available)
VALUES (1, '1984', 'George Orwell', 'Dystopian', 1949, '9780451524935', true);

INSERT INTO book (id, title, author, content, publication_year, isbn, available)
VALUES (2, 'Brave New World', 'Aldous Huxley', 'Science fiction', 1932, '9780060850524', true);

-- Users
INSERT INTO library_user (id, name, surname, email, address, city, password)
VALUES (1, 'Alice', 'Novak', 'alice@example.com', '1 Main St', 'Prague', 'password123');

INSERT INTO library_user (id, name, surname, email, address, city, password)
VALUES (2, 'Bob', 'Svoboda', 'bob@example.com', '2 Second St', 'Brno', 'secret');

-- Loans
-- Loan 1: Alice borrowed '1984' (book id 1)
INSERT INTO loan (id, user_id, book_id, loan_date, return_date, status)
VALUES (1, 1, 1, '2025-11-01', NULL, 'ACTIVE');

-- Loan 2: Bob borrowed and returned 'Brave New World' (book id 2)
INSERT INTO loan (id, user_id, book_id, loan_date, return_date, status)
VALUES (2, 2, 2, '2025-10-01', '2025-10-10', 'RETURNED');

-- Reflect availability consistent with loans above: book 1 should be unavailable, book 2 available
UPDATE book SET available = false WHERE id = 1;
UPDATE book SET available = true WHERE id = 2;
