-- A coller dans h2
-- Utilisateur1: johnjoe ; mot de passe : password123 
-- Utilisateur2: ee		 ; mot de passe : password123

SET @A1A = RANDOM_UUID();
SET @A1B = RANDOM_UUID();
SET @A1C = 'aea971b8-5591-485c-a31a-4cba1f9fff0b';
SET @A1E = '10eaae6c-e0a6-4e93-8f82-0988dff636c0';
SET @A1F = RANDOM_UUID();
SET @A1G = RANDOM_UUID();
SET @A1I = RANDOM_UUID();
SET @A1K = RANDOM_UUID();
SET @A1M = RANDOM_UUID();
SET @A2A = '695a576a-ab26-4241-b8c3-27f31d1a5b07';
SET @A2C = RANDOM_UUID();
SET @A2D = RANDOM_UUID();
SET @A2F = RANDOM_UUID();
SET @A2H = RANDOM_UUID();
SET @A2I = RANDOM_UUID();
SET @A2J = RANDOM_UUID();
SET @A2L = RANDOM_UUID();


INSERT INTO TREE (ID, OWNER_ID, NAME) 
VALUES (2, null, 'Arbre de Doe');

INSERT INTO TREE (ID, OWNER_ID, NAME) 
VALUES (1, null, 'Arbre de Dupond');

INSERT INTO USER_TABLE 
(IS_ADMIN,IS_MALE, IS_VALIDATED,ID, TREE_ID, VALIDATION_CODE, EMAIL, FIRSTNAME, LASTNAME, PASSWORD, USERNAME) 
VALUES 
(TRUE, TRUE, TRUE, 1, 2, null, 'email@example.com', 'John', 'Doe', 'ff56ea7e29e926d1f953ec5448452450daa8c041579e299e07d0e8f7b641ea528abee497216dcb913d84b309ebe5bbea63e06fcb032a03a27ac5b6fec24b88f9', 'johndoe');

INSERT INTO USER_TABLE 
(IS_ADMIN,IS_MALE, IS_VALIDATED,ID, TREE_ID, VALIDATION_CODE, EMAIL, FIRSTNAME, LASTNAME, PASSWORD, USERNAME) 
VALUES 
(TRUE, TRUE, TRUE, 2, 1, null, 'e.e@example.com', 'E', 'E', 'ff56ea7e29e926d1f953ec5448452450daa8c041579e299e07d0e8f7b641ea528abee497216dcb913d84b309ebe5bbea63e06fcb032a03a27ac5b6fec24b88f9', 'ee');

INSERT INTO NODE 
(MALE, VISIBILITY, TREE_ID, USER_ACCOUNT_ID, FATHER_ID, MOTHER_ID, ID, BIRTH_DATE, FIRST_NAME, LAST_NAME)
VALUES 
(TRUE, 0, 2, 1, null, null, '695a576a-ab26-4241-b8c3-27f31d1a5b07', '12/08/1900', 'John', 'Doe');

INSERT INTO NODE 
(MALE, VISIBILITY, TREE_ID, USER_ACCOUNT_ID, FATHER_ID, MOTHER_ID, ID, BIRTH_DATE, FIRST_NAME, LAST_NAME)
VALUES 
(TRUE, 0, 1, 2, null, null, '10eaae6c-e0a6-4e93-8f82-0988dff636c0', '12/08/1930', 'E', 'E');

INSERT INTO NODE (MALE, VISIBILITY, TREE_ID, USER_ACCOUNT_ID, FATHER_ID, MOTHER_ID, ID, BIRTH_DATE, FIRST_NAME, LAST_NAME)
VALUES (TRUE, 0, 1, null, null, null, @A1A, '12/08/1900', 'John', 'Doe'),
       (FALSE, 0, 1, null, null, null, @A1B, '12/08/1900', 'B', 'B'),
       (TRUE, 0, 1, null, @A1A, @A1B, @A1C, '12/08/1915', 'C', 'C'),
       (TRUE, 0, 1, null, @A1C, null, @A1F, '12/08/1930', 'F', 'F'),
       (FALSE, 0, 1, null, null, null, @A1G, '12/08/1930', 'G', 'G'),
       (FALSE, 0, 1, null, @A1F, @A1G, @A1I, '12/08/1945', 'I', 'I'),
       (FALSE, 0, 1, null, @A1F, @A1G, @A1K, '12/08/1945', 'K', 'K'),
       (TRUE, 0, 1, null, null, @A1I, @A1M, '12/08/1970', 'M', 'M'),
       (TRUE, 0, 2, null, @A2A, null, @A2C, '12/08/1915', 'C', 'C'),
       (FALSE, 0, 2, null, null, null, @A2D, '12/08/1915', 'D', 'D'),
       (TRUE, 0, 2, null, @A2C, @A2D, @A2F, '12/08/1930', 'F', 'F'),
       (TRUE, 0, 2, null, null, null, @A2H, '12/08/1945', 'H', 'H'),
       (FALSE, 0, 2, null, @A2F, null, @A2I, '12/08/1945', 'I', 'I'),
       (TRUE, 0, 2, null, @A2F, null, @A2J, '12/08/1945', 'J', 'J'),
       (TRUE, 0, 2, null, @A2H, @A2I, @A2L, '12/08/1970', 'L', 'L');

       
UPDATE NODE
SET FATHER_ID=@A1C
WHERE FIRST_NAME='E' and TREE_ID=1;       
