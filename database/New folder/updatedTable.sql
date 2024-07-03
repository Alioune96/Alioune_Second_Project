SELECT * FROM account;
SELECT * FROM tenmo_user;

SELECT * FROM transfer;

SELECT * FROM account;
SELECT transfer_type_id,t.username(SELECT account_from from transfer WHERE account_from = 2002)AS from,t.username(SELECT account_id from transfer WHERE account_to =)AS to, amount FROM transfer as t
JOIN tenmo_user as te ON t


SELECT * FROM transfer_type;
SELECT * FROM transfer_status;
SELECT * FROM 


START  TRANSACTION;
UPDATE account SET balance = balance - ? WHERE account_id = ?;
UPDATE account SET balance = balance + ? WHERE account_id = ?;
INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (2,2, ?,?,?) RETURNING ;
ROLLBACK;

SELECT * FROM tenmo_user;

SELECT * FROM account;

SELECT account_id FROM account WHERE user_id = 1002;
SELECT * FROM transfer_status;

SELECT transfer_status_desc FROM transfer_status WHERE transfer_status_id = 2;

SELECT * FROM transfer;

SELECT transfer_type_id, t.username, t.username, amount;

SELECT account_id FROM account WHERE user_id = 1002;

SELECT * FROM transfer;

