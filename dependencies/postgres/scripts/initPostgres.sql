CREATE TABLE account(
   user_id serial PRIMARY KEY,
  username VARCHAR (50) UNIQUE NOT NULL,
  password VARCHAR (50) NOT NULL,
  email VARCHAR (355) UNIQUE NOT NULL

 );

INSERT INTO account (username, password, email)
 VALUES ('userTest', 'pass123', 'test@cit.com');
