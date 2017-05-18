create table if not exists LANGUAGES (
	LANGUAGE_CODE varchar(5) primary key,
	LANGUAGE_NAME varchar(100)
);
create table if not exists TOPICS (
	TOPIC_CODE varchar (5)  primary key,
	TOPIC_NAME varchar(200)
);

create table if not exists SONGS (
	SONG_CODE varchar(10)  primary key,
	SONG_NAME varchar(300),
	KARAOKE tinyint,
	link text
);