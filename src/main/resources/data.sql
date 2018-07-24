/* admin */
/* insert ru.loginov.security.entity.UserData*/
insert
into
	public."security_entity_UserData"
	("username","firstName","surname","password","encodedPassword","banned")
values
	('admin','admin','admin','123','$2a$10$y4CySFqyXjtMz4ZMqZMDcOU8ST85pBDQJM2ZbZ8X9GbT5huh1Jh1C',false);

/* insert collection row ru.loginov.security.entity.UserData.authorities */
insert
into
	public."security_entity_UserData_authorities"
	("security_entity_UserData_username", "role")
values
	('admin', 0);

/* insert collection row ru.loginov.security.entity.UserData.options */
insert
into
	public."security_entity_UserData_options"
	("security_entity_UserData_username", "options_KEY", "optionBoolean", "optionInteger", "optionString")
values
	('admin', 'theme', null, null, 'omega');

insert
into
	public."security_entity_UserData_options"
	("security_entity_UserData_username", "options_KEY", "optionBoolean", "optionInteger", "optionString")
values
	('admin', 'language', null, null, 'ru');

/* John */
/* insert ru.loginov.security.entity.UserData*/
insert
into
	public."security_entity_UserData"
	("username","firstName","surname","password","encodedPassword","banned")
values
	('John','John','Lennon','321','$2a$10$E.L8YE484sXL6Mim9sYtHerYHWuvG3/J3BgQrbbqWOt.AxFWtRoqC',false);

/* insert collection row ru.loginov.security.entity.UserData.authorities */
insert
into
	public."security_entity_UserData_authorities"
	("security_entity_UserData_username", "role")
values
	('John', 1);

/* insert collection row ru.loginov.security.entity.UserData.options */
insert
into
	public."security_entity_UserData_options"
	("security_entity_UserData_username", "options_KEY", "optionBoolean", "optionInteger", "optionString")
values
	('John', 'theme', null, null, 'omega');

insert
into
	public."security_entity_UserData_options"
	("security_entity_UserData_username", "options_KEY", "optionBoolean", "optionInteger", "optionString")
values
	('John', 'language', null, null, 'ru');


/* Paul */
/* insert ru.loginov.security.entity.UserData*/
insert
into
	public."security_entity_UserData"
	("username","firstName","surname","password","encodedPassword","banned")
values
	('Paul','Paul','McCartney','1','$2a$10$foM5Rih3.X4xx2lVl0jTA.3/C4Y4sP155DfG0Djt5uMWaQvIZsvrC',false);

/* insert collection row ru.loginov.security.entity.UserData.authorities */
insert
into
	public."security_entity_UserData_authorities"
	("security_entity_UserData_username", "role")
values
	('Paul', 2);

/* insert collection row ru.loginov.security.entity.UserData.options */
insert
into
	public."security_entity_UserData_options"
	("security_entity_UserData_username", "options_KEY", "optionBoolean", "optionInteger", "optionString")
values
	('Paul', 'theme', null, null, 'omega');

insert
into
	public."security_entity_UserData_options"
	("security_entity_UserData_username", "options_KEY", "optionBoolean", "optionInteger", "optionString")
values
	('Paul', 'language', null, null, 'ru');


/* George */
/* insert ru.loginov.security.entity.UserData*/
insert
into
	public."security_entity_UserData"
	("username","firstName","surname","password","encodedPassword","banned")
values
	('George','George','Harrison','2','$2a$10$DugxXKulrjXkjxII.UeneuScSIAH8QiFmoRhrFxfbvmX5b4ELUtVC',false);

/* insert collection row ru.loginov.security.entity.UserData.authorities */
insert
into
	public."security_entity_UserData_authorities"
	("security_entity_UserData_username", "role")
values
	('George', 2);

/* insert collection row ru.loginov.security.entity.UserData.options */
insert
into
	public."security_entity_UserData_options"
	("security_entity_UserData_username", "options_KEY", "optionBoolean", "optionInteger", "optionString")
values
	('George', 'theme', null, null, 'omega');

insert
into
	public."security_entity_UserData_options"
	("security_entity_UserData_username", "options_KEY", "optionBoolean", "optionInteger", "optionString")
values
	('George', 'language', null, null, 'ru');


/* Ringo */
/* insert ru.loginov.security.entity.UserData*/
insert
into
	public."security_entity_UserData"
	("username","firstName","surname","password","encodedPassword","banned")
values
	('Ringo','Ringo','Starr','3','$2a$10$mLsq0RZNNnEOKviZDynI9uLm7KGb6dGeBoTTAcIlt2ULNvEOJaoe6',false);

/* insert collection row ru.loginov.security.entity.UserData.authorities */
insert
into
	public."security_entity_UserData_authorities"
	("security_entity_UserData_username", "role")
values
	('Ringo', 2);

/* insert collection row ru.loginov.security.entity.UserData.options */
insert
into
	public."security_entity_UserData_options"
	("security_entity_UserData_username", "options_KEY", "optionBoolean", "optionInteger", "optionString")
values
	('Ringo', 'theme', null, null, 'omega');

insert
into
	public."security_entity_UserData_options"
	("security_entity_UserData_username", "options_KEY", "optionBoolean", "optionInteger", "optionString")
values
	('Ringo', 'language', null, null, 'ru');