--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.5
-- Dumped by pg_dump version 9.6.6

-- Started on 2018-01-17 07:25:58 CET

SET statement_timeout = 0;
-- SET lock_timeout = 0;
-- SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
-- SET row_security = off;

--
-- TOC entry 1 (class 3079 OID 12390)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2170 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- TOC entry 185 (class 1259 OID 16386)
-- Name: app_app_id_seq; Type: SEQUENCE; Schema: public; Owner: linuxstore
--

CREATE SEQUENCE app_app_id_seq
    START WITH 3052
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE app_app_id_seq OWNER TO linuxstore;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 188 (class 1259 OID 16399)
-- Name: app; Type: TABLE; Schema: public; Owner: linuxstore
--

CREATE TABLE app (
    app_id integer DEFAULT nextval('app_app_id_seq'::regclass) NOT NULL,
    name character varying(128),
    summary character varying(1024),
    description character varying(4096),
    project_license character varying(1024),
    homepage_url character varying(2048),
    bugtracker_url character varying(2048),
    current_release_version character varying,
    flatpak_repo_id integer,
    flatpak_app_id character varying(128),
    first_release_date timestamp with time zone,
    first_release_version character varying(1024),
    current_release_date timestamp with time zone,
    rating double precision,
    rating_votes integer
);


ALTER TABLE app OWNER TO linuxstore;

--
-- TOC entry 189 (class 1259 OID 40970)
-- Name: app_category; Type: TABLE; Schema: public; Owner: linuxstore
--

CREATE TABLE app_category (
    app_id integer NOT NULL,
    category_id integer NOT NULL
);


ALTER TABLE app_category OWNER TO linuxstore;

--
-- TOC entry 191 (class 1259 OID 41004)
-- Name: category; Type: TABLE; Schema: public; Owner: linuxstore
--

CREATE TABLE category (
    category_id integer NOT NULL,
    name character varying(256) NOT NULL
);


ALTER TABLE category OWNER TO linuxstore;

--
-- TOC entry 190 (class 1259 OID 41002)
-- Name: category_category_id_seq; Type: SEQUENCE; Schema: public; Owner: linuxstore
--

CREATE SEQUENCE category_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE category_category_id_seq OWNER TO linuxstore;

--
-- TOC entry 2171 (class 0 OID 0)
-- Dependencies: 190
-- Name: category_category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: linuxstore
--

ALTER SEQUENCE category_category_id_seq OWNED BY category.category_id;


--
-- TOC entry 186 (class 1259 OID 16388)
-- Name: flatpak_repo_flatpak_repo_id_seq; Type: SEQUENCE; Schema: public; Owner: linuxstore
--

CREATE SEQUENCE flatpak_repo_flatpak_repo_id_seq
    START WITH 32
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE flatpak_repo_flatpak_repo_id_seq OWNER TO linuxstore;

--
-- TOC entry 187 (class 1259 OID 16390)
-- Name: flatpak_repo; Type: TABLE; Schema: public; Owner: linuxstore
--

CREATE TABLE flatpak_repo (
    flatpak_repo_id integer DEFAULT nextval('flatpak_repo_flatpak_repo_id_seq'::regclass) NOT NULL,
    name character varying(128),
    description character varying(1024),
    url character varying(2048),
    homepage_url character varying(2048),
    default_branch character varying(128),
    gpgkey character varying(16384),
    download_flatpakrepo_url character varying(2048)
);


ALTER TABLE flatpak_repo OWNER TO linuxstore;

--
-- TOC entry 193 (class 1259 OID 49156)
-- Name: screenshot; Type: TABLE; Schema: public; Owner: linuxstore
--

CREATE TABLE screenshot (
    screenshot_id integer NOT NULL,
    thumb_url character varying(2048),
    img_mobile_url character varying(2048),
    img_desktop_url character varying(2048),
    app_id integer NOT NULL
);


ALTER TABLE screenshot OWNER TO linuxstore;

--
-- TOC entry 192 (class 1259 OID 49154)
-- Name: screenshot_screenshot_id_seq; Type: SEQUENCE; Schema: public; Owner: linuxstore
--

CREATE SEQUENCE screenshot_screenshot_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE screenshot_screenshot_id_seq OWNER TO linuxstore;

--
-- TOC entry 2029 (class 2604 OID 41007)
-- Name: category category_id; Type: DEFAULT; Schema: public; Owner: linuxstore
--

ALTER TABLE ONLY category ALTER COLUMN category_id SET DEFAULT nextval('category_category_id_seq'::regclass);


--
-- TOC entry 2033 (class 2606 OID 16407)
-- Name: app app_pkey; Type: CONSTRAINT; Schema: public; Owner: linuxstore
--

ALTER TABLE ONLY app
    ADD CONSTRAINT app_pkey PRIMARY KEY (app_id);


--
-- TOC entry 2037 (class 2606 OID 41017)
-- Name: category category_name_unique; Type: CONSTRAINT; Schema: public; Owner: linuxstore
--

ALTER TABLE ONLY category
    ADD CONSTRAINT category_name_unique UNIQUE (name);


--
-- TOC entry 2039 (class 2606 OID 41009)
-- Name: category category_pkey; Type: CONSTRAINT; Schema: public; Owner: linuxstore
--

ALTER TABLE ONLY category
    ADD CONSTRAINT category_pkey PRIMARY KEY (category_id);


--
-- TOC entry 2031 (class 2606 OID 16398)
-- Name: flatpak_repo flatpak_repo_pkey; Type: CONSTRAINT; Schema: public; Owner: linuxstore
--

ALTER TABLE ONLY flatpak_repo
    ADD CONSTRAINT flatpak_repo_pkey PRIMARY KEY (flatpak_repo_id);


--
-- TOC entry 2042 (class 2606 OID 49163)
-- Name: screenshot screenshot_pkey; Type: CONSTRAINT; Schema: public; Owner: linuxstore
--

ALTER TABLE ONLY screenshot
    ADD CONSTRAINT screenshot_pkey PRIMARY KEY (screenshot_id);


--
-- TOC entry 2034 (class 1259 OID 40984)
-- Name: fki_app_category_app_id_fkey; Type: INDEX; Schema: public; Owner: linuxstore
--

CREATE INDEX fki_app_category_app_id_fkey ON app_category USING btree (app_id);


--
-- TOC entry 2035 (class 1259 OID 41015)
-- Name: fki_app_category_category_id_fkey; Type: INDEX; Schema: public; Owner: linuxstore
--

CREATE INDEX fki_app_category_category_id_fkey ON app_category USING btree (category_id);


--
-- TOC entry 2040 (class 1259 OID 49169)
-- Name: fki_screenshot_app_id_fkey; Type: INDEX; Schema: public; Owner: linuxstore
--

CREATE INDEX fki_screenshot_app_id_fkey ON screenshot USING btree (app_id);


--
-- TOC entry 2044 (class 2606 OID 40985)
-- Name: app_category app_category_app_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: linuxstore
--

ALTER TABLE ONLY app_category
    ADD CONSTRAINT app_category_app_id_fkey FOREIGN KEY (app_id) REFERENCES app(app_id);


--
-- TOC entry 2045 (class 2606 OID 41010)
-- Name: app_category app_category_category_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: linuxstore
--

ALTER TABLE ONLY app_category
    ADD CONSTRAINT app_category_category_id_fkey FOREIGN KEY (category_id) REFERENCES category(category_id);


--
-- TOC entry 2043 (class 2606 OID 16408)
-- Name: app app_flatpak_repo_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: linuxstore
--

ALTER TABLE ONLY app
    ADD CONSTRAINT app_flatpak_repo_id_fkey FOREIGN KEY (flatpak_repo_id) REFERENCES flatpak_repo(flatpak_repo_id);


--
-- TOC entry 2046 (class 2606 OID 49164)
-- Name: screenshot screenshot_app_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: linuxstore
--

ALTER TABLE ONLY screenshot
    ADD CONSTRAINT screenshot_app_id_fkey FOREIGN KEY (app_id) REFERENCES app(app_id);


-- Completed on 2018-01-17 07:25:58 CET

--
-- PostgreSQL database dump complete
--
