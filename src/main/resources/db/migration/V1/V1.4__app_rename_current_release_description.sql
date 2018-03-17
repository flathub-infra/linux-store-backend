ALTER TABLE public.app
    RENAME first_release_version TO current_release_description;

ALTER TABLE public.app
    ALTER COLUMN current_release_description TYPE character varying(4096);