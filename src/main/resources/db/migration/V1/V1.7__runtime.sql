
SET search_path = public, pg_catalog;

CREATE SEQUENCE runtime_runtime_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE runtime_runtime_id_seq OWNER TO linuxstore;

SET default_tablespace = '';

SET default_with_oids = false;


CREATE TABLE runtime (
    runtime_id integer DEFAULT nextval('runtime_runtime_id_seq'::regclass) NOT NULL,
    flatpak_app_id character varying(128),
    flatpak_repo_id integer,
    name character varying(128),
    summary character varying(1024),
    publishing_status character varying(32),
    publishing_status_info character varying(4096),
    description character varying(4096),
    project_license character varying(1024),
    homepage_url character varying(2048),
    bugtracker_url character varying(2048),
    current_release_version character varying,
    first_release_date timestamp with time zone,
    first_release_version character varying(1024),
    current_release_date timestamp with time zone
);


ALTER TABLE runtime OWNER TO linuxstore;
