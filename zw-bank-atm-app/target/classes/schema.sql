CREATE TABLE Account (
    accountNumber varchar(50),
    openingBalance int,
    overdraft int
);

CREATE TABLE User (
    accountNumber varchar(50),
    pin int
);

CREATE TABLE Note (
    totalNotes int,
    type varchar(50),
    id int
);
