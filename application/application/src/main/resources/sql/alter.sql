ALTER TABLE activity 
ALTER COLUMN jarak TYPE FLOAT;
ALTER TABLE activity 
ALTER COLUMN elevasi TYPE FLOAT;
ALTER TABLE raceparticipants
ADD id_training INT;

ALTER TABLE Activity 
ALTER COLUMN createdAt TYPE TIMESTAMP
USING createdAt::timestamp,
ALTER COLUMN createdAt SET DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE Activity 
ALTER COLUMN durasi TYPE VARCHAR(16);

