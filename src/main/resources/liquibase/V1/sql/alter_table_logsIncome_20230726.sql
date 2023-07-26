ALTER TABLE logsIncome DROP COLUMN dateIncome;
ALTER TABLE logsIncome DROP COLUMN dateOutcome;

ALTER TABLE logsIncome ADD transactionDate timestamp;