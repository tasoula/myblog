CREATE TABLE IF NOT EXISTS public.t_posts
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    title character varying(1024) COLLATE pg_catalog."default" NOT NULL,
    content text COLLATE pg_catalog."default",
    image_url character varying(512) COLLATE pg_catalog."default",
    like_count integer NOT NULL DEFAULT 0,
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT pk_post PRIMARY KEY (id)
);






