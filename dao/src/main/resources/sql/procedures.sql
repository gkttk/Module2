delimiter //

CREATE PROCEDURE searchByPartOfDescription(IN in_partOfDescription varchar(255))
BEGIN
    SELECT id, name, description, price, duration, create_date, last_update_date FROM gift_certificate
    WHERE description LIKE CONCAT('%', in_partOfDescription, '%');
END;
//

CREATE PROCEDURE searchByPartOfName(IN in_partOfName varchar(255))
BEGIN
    SELECT id, name, description, price, duration, create_date, last_update_date FROM gift_certificate
    WHERE name LIKE CONCAT('%', in_partOfName, '%');
END;
//

