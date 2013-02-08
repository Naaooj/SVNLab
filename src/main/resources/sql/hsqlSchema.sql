create table svncommit (
	id IDENTITY not null,
	principal varchar(255),
	title varchar(255),
	description varchar(2000),
	revision BIGINT,
	
	createdBy varchar(30),
	updatedBy varchar(30),
	createdAt TIMESTAMP,
	updatedAt TIMESTAMP
);