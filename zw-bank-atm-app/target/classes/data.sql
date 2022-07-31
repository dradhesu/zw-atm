
INSERT INTO Account (accountNumber, openingBalance,overdraft) VALUES ('123456789', 800,200);
INSERT INTO Account (accountNumber, openingBalance,overdraft) VALUES ('987654321', 1230,150);
INSERT INTO Account (accountNumber, openingBalance,overdraft) VALUES ('987654322', 1830,250);
INSERT INTO Account (accountNumber, openingBalance,overdraft) VALUES ('987654323', 240,50);
INSERT INTO Account (accountNumber, openingBalance,overdraft) VALUES ('987654324', 120,10);

INSERT INTO User (accountNumber, pin) VALUES ('123456789', 1234);
INSERT INTO User (accountNumber, pin) VALUES ('987654321', 4321);
INSERT INTO User (accountNumber, pin) VALUES ('987654322', 1831);
INSERT INTO User (accountNumber, pin) VALUES ('987654323', 2401);
INSERT INTO User (accountNumber, pin) VALUES ('987654324', 1201);

commit;