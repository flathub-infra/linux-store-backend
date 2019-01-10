CREATE SEQUENCE apprelease_apprelease_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;

ALTER TABLE apprelease_apprelease_id_seq OWNER TO linuxstore;

CREATE TABLE app_release (
	apprelease_id integer DEFAULT nextval('apprelease_apprelease_id_seq'::regclass) NOT NULL,
	appdata_release_version varchar(1024) NULL,
	appdata_release_version_updated bool NOT NULL,
	arch varchar(32) NULL,
	downloads int4 NOT NULL,
	installs int4 NOT NULL,
	ostree_commit_date timestamp NULL,
	ostree_commit_hash varchar(512) NULL,
	ostree_commit_hash_parent varchar(512) NULL,
	ostree_commit_subject varchar(2048) NULL,
	ostree_commit_date_next timestamp NULL,
	updates int4 NOT NULL,
	app_id int4 NULL,
	download_size varchar(512) NULL,
	installed_size varchar(512) NULL,
	runtime varchar(512) NULL,
	sdk varchar(512) NULL,
	is_end_of_life bool NOT NULL,
	end_of_life_info varchar(512) NULL,
	end_of_life_rebase varchar(512) NULL,
	metadata varchar(8192) NULL,
	CONSTRAINT app_release_pkey PRIMARY KEY (apprelease_id),
	CONSTRAINT app_release FOREIGN KEY (app_id) REFERENCES app(app_id)
);


ALTER TABLE app_release OWNER TO linuxstore;