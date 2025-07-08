CREATE TABLE IF NOT EXISTS public.t_tags
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    name character varying(256) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT pk_tag PRIMARY KEY (id),
    CONSTRAINT unq_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS public.t_sv_post_tag
(
    post_id uuid NOT NULL,
    tag_id uuid NOT NULL,
    id uuid NOT NULL,
    CONSTRAINT t_sv_post_tag_pkey PRIMARY KEY (id),
    CONSTRAINT unq_sv_post_tag UNIQUE (post_id, tag_id),
    CONSTRAINT fk_post FOREIGN KEY (post_id)
        REFERENCES public.t_posts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
        NOT VALID,
    CONSTRAINT fk_tag FOREIGN KEY (tag_id)
        REFERENCES public.t_tags (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
        NOT VALID
);