CREATE TABLE IF NOT EXISTS public.t_comments
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    post_id uuid NOT NULL,
    content text COLLATE pg_catalog."default" NOT NULL,
  --  author character varying(256) COLLATE pg_catalog."default",
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT pk_comment PRIMARY KEY (id),
    CONSTRAINT fk_post FOREIGN KEY (post_id)
        REFERENCES public.t_posts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
        NOT VALID
);