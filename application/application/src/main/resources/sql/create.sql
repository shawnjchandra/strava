DROP TABLE IF EXISTS Runners, Admins, Activity, RaceParticipants CASCADE;
DROP SEQUENCE IF EXISTS seq_id_user CASCADE;

CREATE SEQUENCE seq_id_user START 1;


CREATE TABLE Runners(
	id_users INT PRIMARY KEY DEFAULT nextval('seq_id_user'),
	id_runner INT UNIQUE NOT NULL GENERATED ALWAYS AS IDENTITY,
	username VARCHAR(255) NOT NULL UNIQUE,
	password VARCHAR(255) NOT NULL,
	nama varchar(255) NOT NULL,
	tanggal_lahir DATE NOT NULL,
	lokasi varchar(255) NOT NULL,
	gender VARCHAR(1) CHECK(gender IN ('p','l') )
	
);

CREATE TABLE Admins(
	id_users INT PRIMARY KEY DEFAULT nextval('seq_id_user'),
	id_admin INT UNIQUE NOT NULL GENERATED ALWAYS AS IDENTITY,
	username VARCHAR(255) NOT NULL UNIQUE,
	password VARCHAR(255) NOT NULL
);

CREATE TABLE Activity(
	id_activity INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	judul varchar(255) NOT NULL,
	durasi TIME NULL,
	jarak int null,
	createdAt DATE DEFAULT CURRENT_DATE,
	urlPath VARCHAR(255) NULL,
	id_training int UNIQUE null,
	id_runner int REFERENCES Runners(id_runner) null,
	id_race int UNIQUE null,
	kuota_max int null,
	id_admin int REFERENCES Admins(id_admin) null
	
);

CREATE TABLE RaceParticipants(
	id_runner int references Runners(id_runner) not null,
	id_race int references Activity(id_training) not null
	
);

