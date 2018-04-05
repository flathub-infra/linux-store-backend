ALTER TABLE public.app
    ADD COLUMN help_url character varying(2048);

ALTER TABLE public.app
    ADD COLUMN donation_url character varying(2048);

ALTER TABLE public.app
    ADD COLUMN translate_url character varying(2048);