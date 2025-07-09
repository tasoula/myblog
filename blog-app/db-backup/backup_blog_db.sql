--
-- PostgreSQL database dump
--

-- Dumped from database version 14.0
-- Dumped by pg_dump version 14.0

-- Started on 2025-07-09 06:47:31

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 210 (class 1259 OID 82000)
-- Name: t_comments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_comments (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    post_id uuid NOT NULL,
    content text NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.t_comments OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 81990)
-- Name: t_posts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_posts (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    title character varying(1024) NOT NULL,
    content text,
    image_url character varying(512),
    like_count integer DEFAULT 0 NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.t_posts OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 82022)
-- Name: t_sv_post_tag; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_sv_post_tag (
    post_id uuid NOT NULL,
    tag_id uuid NOT NULL,
    id uuid NOT NULL
);


ALTER TABLE public.t_sv_post_tag OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 82014)
-- Name: t_tags; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_tags (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    name character varying(256) NOT NULL
);


ALTER TABLE public.t_tags OWNER TO postgres;


--
-- TOC entry 3335 (class 0 OID 81990)
-- Dependencies: 209
-- Data for Name: t_posts; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.t_posts (id, title, content, image_url, like_count, created_at) VALUES ('ea5789e4-09f7-4f6d-8444-5a199eeefa10', 'Баухаус ', 'Кажется, баухаус — просто минимализм, но постарше и построже. На самом деле — это бунтарь, который меняет подход к дизайну, мебели и даже светильникам.

Вчера мы писали о Дитере Рамсе — одном из главных продолжателей идей школы Баухаус. А теперь разберемся, что это за стиль.', '53c1b13b-2fc7-4510-b76b-a657c4716782_photo_5397593110529308659_y.jpg', 14, '2025-07-07 10:53:47.674939+03');
INSERT INTO public.t_posts (id, title, content, image_url, like_count, created_at) VALUES ('5f023b74-7d7a-4723-a8a9-0b34cc145494', 'Укладка пола', 'Тема напольных покрытий откликнулась — было много вопросов и обсуждений. Двигаемся дальше.

Напомним, что в прошлых постах мы уже разобрали:

— как выбрать напольное покрытие,
— влагостойкость и водостойкость,
— экологичные покрытия.

Сегодня поговорим об укладке 

Что важно предусмотреть заранее, чем отличается монтаж ламината, паркета и плитки — и как не запутаться в подложках и клее.

 Все в карточках.

Пиши вопросы в комментарии, разберем.', 'd9373402-896a-4139-801f-9491d479bcbb_photo_5355004987916284891_y.jpg', 10, '2025-07-07 11:03:45.686427+03');
INSERT INTO public.t_posts (id, title, content, image_url, like_count, created_at) VALUES ('94b85ea5-a9a4-4c49-b54b-b57317db0787', 'Что бы вы добавили в этот интерьер?', '', '5f3e1ab2-b576-4a5d-b704-1870fcc844c3_photo_5373188479288210449_y.jpg', 6, '2025-07-07 11:01:20.596079+03');
INSERT INTO public.t_posts (id, title, content, image_url, like_count, created_at) VALUES ('a501935e-fc40-431b-a075-7cd8398368c1', 'Mid-centyry modern', 'Интерьер выглядит так, будто время в нем замедлилось — но не от скуки, а от спокойствия и уюта. Чистые линии, теплое дерево и мебель на тонких ножках… Это все — стиль мид-сенчури модерн.

Он не кричит о себе, не стремится произвести вау-эффект прямо с порога. Зато он остается в интерьере надолго.

В карточках: как появился стиль, почему до сих пор в моде и как использовать его в современных интерьерах (например, если у тебя квартира в «сталинке»).
', '6ceb9796-08eb-4c47-ab9f-e32d23be6890_photo_5355004987916284757_y.jpg', 18, '2025-07-07 11:06:15.218874+03');
INSERT INTO public.t_posts (id, title, content, image_url, like_count, created_at) VALUES ('4e1b5789-1432-4039-aae8-2d6e9ffe13d6', 'Карл Лагерфельд', 'Вилла Жако. Дом с видом на Эльбу, который Лагерфельд купил в память о важном для него человеке. Здесь уже не металл, а мрамор, золото и библиотека под стеклянным куполом.

В этом доме он жил почти десять лет, создавал интерьеры сам — и снял рекламу знаменитого аромата Lagerfeld Jaco.
', 'aef4ce9d-ba49-4229-a280-0fe67c3303ee_photo_5352584576276491375_y.jpg', 2, '2025-07-07 11:11:46.339187+03');
INSERT INTO public.t_posts (id, title, content, image_url, like_count, created_at) VALUES ('ac6a67ed-f18c-4faf-b094-dc4c932e1c43', 'Валентино', 'Валентино — не только про красные платья. У кутюрье, помимо всего прочего, есть вилла, шале, яхта и квартира в Лондоне. А любимая резиденция — замок под Парижем.

Звучит как завязка к фильму, а не реальная жизнь. Но Замок де Видвиль — не выдумка. Его строили еще при Людовике XIII, одной из жительниц была фаворитка «короля-солнце».

Сейчас в замке живут мопсы, лошади, китайский фарфор и эстетика, доведенная до совершенства. 

 Интересно было бы заглянуть и в другие дома кутюрье?', 'c61035fc-52cc-4458-879f-14f6560bb275_photo_5206586075106111470_y.jpg', 3, '2025-07-07 11:19:32.14989+03');
INSERT INTO public.t_posts (id, title, content, image_url, like_count, created_at) VALUES ('7b7f78d2-d406-4086-b5f3-7b0b8ae2e053', 'Мемфис', 'Цвета как из поп-арта, формы будто из конструктора — этот стиль вдохновлял Боуи и Лагерфельда, а сегодня его снова цитируют модные бренды. Самые знаковые вещи Мемфиса уходят с молотка за тысячи долларов.

Недавно мы писали про Соттсасса — основателя движения.

А сейчас — листай карусель. Покажем, кто собирал Мемфис, какие вещи они выбирали и чем этот странный, дерзкий стиль цепляет до сих пор.

Поставили бы у себя в доме что-то из Мемфис?
', 'a02654ae-dfd0-4cf1-b111-4b6eb1c86dae_photo_5334740550529710260_y.jpg', 11, '2025-07-07 11:08:18.677741+03');
INSERT INTO public.t_posts (id, title, content, image_url, like_count, created_at) VALUES ('a7846460-fb23-4cfc-b67e-c3b4f4f9d8e0', 'Ар-деко', 'Добро пожаловать в мир элегантности и изысканных форм. Золотые акценты, хрустальные люстры и четкая геометрия — черты богатого и величественного ар-деко 

Заходи в карточки и узнай, как этот стиль делает интерьер запоминающимся.

 Тебе нравится ар-деко? Какой элемент хотелось бы добавить в свой интерьер? Поделись в комментариях.', 'a8a1a670-debe-420a-956e-91ef908918b3_photo_5229147903384218134_y.jpg', 7, '2025-07-07 11:17:06.339322+03');
INSERT INTO public.t_posts (id, title, content, image_url, like_count, created_at) VALUES ('7a8ffad4-99c8-4d48-86ad-49688daeda2b', 'Цвет настроения: терракотовый 🧡', 'Как тебе этот цвет в интерьере?

❤️ — классно
🤔 — не мое', '5ee15335-493e-4278-abc0-77859bbcd2ec_photo_5332488750716023330_y.jpg', 5, '2025-07-07 11:13:23.979073+03');
INSERT INTO public.t_posts (id, title, content, image_url, like_count, created_at) VALUES ('42b95bae-2eb8-4036-b5e4-eea6045ca2af', 'Келли Уэстлер', 'Королева экстравагантности, дизайнер интерьеров для Гвен Стефани и Кэмерон Диас, человек, который может поставить бюст Цезаря на розовый бархатный ковер и сделать это стильно. 

Сегодня говорим о Келли Уэстлер — пожалуй, самой яркой фигуре в современном дизайне.

Листай карточки и знакомься с автором голливудского шика.

Какой из ее проектов тебе нравится больше всего? Пиши в комментариях.', '2ee7a37e-f0fc-4432-8d1f-104859e27ed5_Без имени.jpg', 0, '2025-07-07 11:25:43.933448+03');
INSERT INTO public.t_posts (id, title, content, image_url, like_count, created_at) VALUES ('10ec2c6a-48eb-41ef-9f55-1eea2e13d1c8', 'Дитер Рамс', 'Чистый, минималистичный, но с характером — стиль Apple во многом обязан одному немецкому дизайнеру.

Сегодня в рубрике #легендарные_дизайнеры — Дитер Рамс. Он создавал продукты для Braun, сформулировал «10 принципов хорошего дизайна» и вдохновил целое поколение дизайнеров.
', '4e8f5aec-abd0-4f09-a111-a1202d5ee186_photo_5395341310715621396_y (1).jpg', 18, '2025-07-07 11:27:45.383751+03');
--
-- TOC entry 3336 (class 0 OID 82000)
-- Dependencies: 210
-- Data for Name: t_comments; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('392ca9e3-9e15-4dce-829a-75e436746749', 'ea5789e4-09f7-4f6d-8444-5a199eeefa10', 'Не люблю этот стиль, какой-то бардачный и не уютный, для меня', '2025-07-07 10:54:51.755928+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('ff5990a0-7629-464d-bccd-14383d5fbd8f', 'ea5789e4-09f7-4f6d-8444-5a199eeefa10', 'Думаю, что сейчас этот стиль уместен для оформления жилья в аренду. И ярко и необычно. И не надоест.', '2025-07-07 10:55:04.729999+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('ce48c646-26c8-47cb-ae1c-c16f43ea37a0', 'ea5789e4-09f7-4f6d-8444-5a199eeefa10', 'Супер🔥 MinimalArt схожи с Баухаузом - оба стиля ценят простоту, функциональность и лаконичность.', '2025-07-07 10:55:15.610771+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('0725a7c5-aa40-42fb-b7ab-06aee966a363', 'ea5789e4-09f7-4f6d-8444-5a199eeefa10', 'Очень интересно! ЛюблюMinimalArt в интерьере!', '2025-07-07 10:55:24.796711+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('e231049f-b794-4b2a-a9d5-c15260acd9dd', '94b85ea5-a9a4-4c49-b54b-b57317db0787', 'большое зеленое растение', '2025-07-07 11:01:43.392498+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('3a2bc1b7-001c-4c05-acb6-9c2d095a316c', '94b85ea5-a9a4-4c49-b54b-b57317db0787', 'торшер', '2025-07-07 11:01:47.535482+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('22828e74-ab51-4842-8e38-41324a565ac2', '94b85ea5-a9a4-4c49-b54b-b57317db0787', 'плед', '2025-07-07 11:01:52.6574+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('873c0295-9963-402d-ab8c-e2be0ea2a207', '94b85ea5-a9a4-4c49-b54b-b57317db0787', 'По моему всё отлично и так
', '2025-07-07 11:01:57.96077+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('fa093af3-c8f1-4738-8c1d-6ffb3645026a', '94b85ea5-a9a4-4c49-b54b-b57317db0787', 'Стена пустая справа от дивана, здесь акцент добавила бы', '2025-07-07 11:02:07.8822+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('a37d5234-dd0e-4980-9ab5-27f24f8d3edb', '94b85ea5-a9a4-4c49-b54b-b57317db0787', 'С освещением бы поиграла.', '2025-07-07 11:02:17.377752+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('69a128c9-f04d-45e4-85e3-6a57e8733374', '5f023b74-7d7a-4723-a8a9-0b34cc145494', 'Здравствуйте! Спасибо , что подняли эту тему! У меня вопрос.У подруги была ситуация, когда для пола они выбрали длинную плитку, похожую на ламинат. И оказалось, что она вся неровная -один угол ниже , другой по диагонали выше и ее невозможно укладывать. Используете ли вы в дизайне полов такую плитку или действительно с ней часто случаются косяки и много брака ? Какие ещё напольные материалы не очень хорошо ведут себя при установке и лучше их не брать ? Из дорогих материалов и из эконом сегмента?', '2025-07-07 11:04:09.226166+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('3578ac6d-6e0b-43bb-bd90-98c3d6e4f41f', '5f023b74-7d7a-4723-a8a9-0b34cc145494', 'Очень жду! Укладка кажется такой интересной', '2025-07-07 11:04:18.453418+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('c9681f7e-a67c-4ee6-b434-8073680de813', '5f023b74-7d7a-4723-a8a9-0b34cc145494', 'У меня квартира старая, полы и потолки -дерево, но высота -3''250 мм. ;) Вопрос: если я хочу, в будущем, класть инженерную доску, и ставить Инд. Отопление -мне полы -срывать и переделывать, под инженерную доску? Ещё вопрос: есть ли инженерная доска с верхним слоем из клёна, и какова, максимально допустимая толщина декоративного слоя в инженерной доске?', '2025-07-07 11:04:27.238906+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('ff75b9b4-4319-4841-8864-373c557f97eb', '5f023b74-7d7a-4723-a8a9-0b34cc145494', 'Здравствуйте 👋 
Очень хороший вопрос и важная тема - действительно, длинная плитка иногда может иметь дефекты, особенно если производство или доставка прошли неидеально. Лучше выбирать проверенных поставщиков и контролировать ровность еще на этапе приёмки. Что касается других материалов - ламинат и инженерная доска обычно более стабильны, а массив может реагировать на влажность. В эконом-сегменте часто встречаются проблемы с ровностью и качеством, поэтому выбор стоит делать осмотрительно', '2025-07-07 11:04:38.060998+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('d10e9094-09e3-438a-afda-9c8be89eb869', 'a501935e-fc40-431b-a075-7cd8398368c1', 'Что за фанерные панели? На фото так и не нашла)
', '2025-07-07 11:06:42.015004+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('7dcff4b1-e87e-4eed-9c49-99b05dd926bb', '7b7f78d2-d406-4086-b5f3-7b0b8ae2e053', 'Крик детской души и взрослой прагматичности😁', '2025-07-07 11:08:32.588287+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('85fa95d0-bb33-4ae3-8cc3-68965d947b4f', '7b7f78d2-d406-4086-b5f3-7b0b8ae2e053', 'Четкий стиль, узнаваемый, оригинальный. Вполне эмоциональный и самодостаточный. Огромный потенциал. Конечно самый простой способ использовать отдельные предметы в связке с минимализмом', '2025-07-07 11:08:40.369246+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('6e935d60-24a4-4427-bd62-7ab39754b30f', '7b7f78d2-d406-4086-b5f3-7b0b8ae2e053', 'Какое точное наблюдение 😊', '2025-07-07 11:08:45.761072+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('35b24542-b082-409d-84b5-f219e7d77bc8', '7b7f78d2-d406-4086-b5f3-7b0b8ae2e053', 'Светильник- машинку в детскую однозначно бы поставила🥰', '2025-07-07 11:08:52.247994+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('5d546929-3826-4b47-8bea-fac31173f66e', '4e1b5789-1432-4039-aae8-2d6e9ffe13d6', 'На вилле Жако. Почему-то фото Парижской квартиры мне напоминает кабинет врача, возможно немного неудачно сделано фото', '2025-07-07 11:12:11.172033+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('9cbfe697-5fe2-446d-9486-8d258f7cae64', '7a8ffad4-99c8-4d48-86ad-49688daeda2b', 'Мне нравится, но для себя бы не выбрала. Если только терракотовый и серый мне тоскливо, осень 🍂, а вот с зеленью уже поживее. На 3й и 4й фотографии - люстры 😍', '2025-07-07 11:13:39.19077+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('5d86f7bc-1912-45db-8603-f5550e42b27e', '7a8ffad4-99c8-4d48-86ad-49688daeda2b', 'Благодарим за мнение 🫶
Да, у каждого свои вкусы и предпочтения, и здорово, что цвета воспринимаются по-разному👍Терракотовый и серый действительно могут быть спокойными и уютными, а зелёный - более живым и освежать🌿  Люстры действительно великолепны, они придают интерьеру особый шарм💫', '2025-07-07 11:13:46.630818+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('a9cb95a6-b262-42ed-8683-439b52b51f50', '7a8ffad4-99c8-4d48-86ad-49688daeda2b', 'Обожаю терракотовые акценты', '2025-07-07 11:13:53.23386+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('d0e06d88-cc0e-4555-8eef-0d04e223208c', '7a8ffad4-99c8-4d48-86ad-49688daeda2b', 'Шикарный цвет!!!  Он не надоедает вообще 😍', '2025-07-07 11:13:58.510095+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('4065fd56-a01d-4d97-9bc7-8414c138d754', 'a7846460-fb23-4cfc-b67e-c3b4f4f9d8e0', 'Нет, слишком вычурно. Мне нравится американский и эко😊', '2025-07-07 11:17:24.109294+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('f08a79fb-e2d8-41f7-a0c1-14865ff31981', 'a7846460-fb23-4cfc-b67e-c3b4f4f9d8e0', 'Благодарим,  что поделились 😉', '2025-07-07 11:17:29.350972+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('71a4eff1-4241-4168-bff7-ee3198d5a325', 'a7846460-fb23-4cfc-b67e-c3b4f4f9d8e0', 'нравится 👍 Роскошь и геометрия, металл и бархат - такие вот сочетания ', '2025-07-07 11:17:34.937964+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('1bb505f9-4b3e-4b83-9ef3-18809053826e', 'a7846460-fb23-4cfc-b67e-c3b4f4f9d8e0', 'Люблю такой стиль ! Выглядит необычно 🔥', '2025-07-07 11:17:45.726296+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('418f275b-3b6e-4a92-867e-1d934c9e161e', 'a7846460-fb23-4cfc-b67e-c3b4f4f9d8e0', '❤️это шик, роскошь!', '2025-07-07 11:17:53.560194+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('21df14fa-833c-4c7e-9e24-266ca7027c7d', 'a7846460-fb23-4cfc-b67e-c3b4f4f9d8e0', 'Мне тоже нравится ар-деко, смотрится элегантно. В таком интерьере хочется надеть пушистые тапочки, шелковый халат и бокал мартини в руки 😁 
Недавно попался один проект что-то похожее на ар-деко, с отсылками к русским народным мотивам, смотрится просто потрясающе!🤩', '2025-07-07 11:17:59.498783+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('9b056a5a-e867-40a8-a547-84c196e17b9c', 'ac6a67ed-f18c-4faf-b094-dc4c932e1c43', 'Очень интересно!
', '2025-07-07 11:19:43.019034+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('5de54652-113f-42ac-9a04-db6efe027a34', 'ac6a67ed-f18c-4faf-b094-dc4c932e1c43', 'А почему не посмотреть?можно.. конечно
', '2025-07-07 11:19:52.896615+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('630b3835-609b-44ae-9c30-ba5b971ecf37', '42b95bae-2eb8-4036-b5e4-eea6045ca2af', 'Все очень красиво и необычно! Нравится все !', '2025-07-07 11:25:54.356795+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('a304d222-8e3a-41e3-9ad0-06f21da08cbc', '42b95bae-2eb8-4036-b5e4-eea6045ca2af', 'Смелые и необычные интерьеры Келли Уэстлер, где Эклектика - с большой буквы,  больше всех поразили и запомнились в модуле по истории дизайна школы. Обязательно буду изучать её творчество! 👌', '2025-07-07 11:26:02.569932+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('3f59654d-6280-47a8-9b62-5ae4d19560b4', '42b95bae-2eb8-4036-b5e4-eea6045ca2af', 'Мне очень нравится
', '2025-07-07 11:26:07.705292+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('b055ccd7-9055-4667-8b3a-b416b2f77ab2', '42b95bae-2eb8-4036-b5e4-eea6045ca2af', 'И нам очень 😉', '2025-07-07 11:26:14.397295+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('ecd3e417-9d0c-4ae8-9d93-e2dc3e24a62a', '10ec2c6a-48eb-41ef-9f55-1eea2e13d1c8', 'Восхищаюсь 10 принципами! 👍', '2025-07-07 11:28:12.831966+03');
INSERT INTO public.t_comments (id, post_id, content, created_at) VALUES ('f84f6e06-ad9a-4725-a1ce-0ec692c5e65e', '10ec2c6a-48eb-41ef-9f55-1eea2e13d1c8', 'Рады,  что вам понравились 😉', '2025-07-07 11:28:19.676818+03');



--
-- TOC entry 3337 (class 0 OID 82014)
-- Dependencies: 211
-- Data for Name: t_tags; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.t_tags (id, name) VALUES ('ec29bd7b-744f-4d25-96d3-29d638ee4dd2', 'баухаус');
INSERT INTO public.t_tags (id, name) VALUES ('2cf31370-69c6-4c63-8f30-e66e38e31793', 'стили');
INSERT INTO public.t_tags (id, name) VALUES ('bc89b10f-e6fc-4fa1-9846-d22aaf4067ac', 'легендарные_дизайнеры');
INSERT INTO public.t_tags (id, name) VALUES ('902d0652-228f-444f-a195-85eb9c6edbc0', 'планировка');
INSERT INTO public.t_tags (id, name) VALUES ('4b595bac-23bd-493c-be60-99693d627ff9', 'интерьер');
INSERT INTO public.t_tags (id, name) VALUES ('0376f22c-00cd-45eb-8b11-bc96be7c50ec', 'полы');
INSERT INTO public.t_tags (id, name) VALUES ('5a030fdf-89e9-4e1f-82c9-59394c62ba72', 'отделка');
INSERT INTO public.t_tags (id, name) VALUES ('bffc68c6-d4ff-456a-adbc-93bdbed92b86', 'мид-сенчури');
INSERT INTO public.t_tags (id, name) VALUES ('b3013a2a-6060-4e65-8ac7-d6a4c4b6ffcf', 'mid-centyry');
INSERT INTO public.t_tags (id, name) VALUES ('998b2421-1969-4180-879c-7094ad731d94', 'мемфис');
INSERT INTO public.t_tags (id, name) VALUES ('b0d8e732-e7d1-4199-99ee-c7762dd541fc', 'виллы');
INSERT INTO public.t_tags (id, name) VALUES ('520e04e5-cb65-46bb-afd1-fc770e646b38', 'цвет');
INSERT INTO public.t_tags (id, name) VALUES ('e24051ee-bf51-4248-b169-1b925f0060fe', 'ад-деко');


--
-- TOC entry 3338 (class 0 OID 82022)
-- Dependencies: 212
-- Data for Name: t_sv_post_tag; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.t_sv_post_tag (post_id, tag_id, id) VALUES ('ea5789e4-09f7-4f6d-8444-5a199eeefa10', 'ec29bd7b-744f-4d25-96d3-29d638ee4dd2', 'be4de2ed-d198-4ddc-883d-d3221c118814');
INSERT INTO public.t_sv_post_tag (post_id, tag_id, id) VALUES ('ea5789e4-09f7-4f6d-8444-5a199eeefa10', '2cf31370-69c6-4c63-8f30-e66e38e31793', '728f26df-c3e8-4483-8f1b-9e8233966469');
INSERT INTO public.t_sv_post_tag (post_id, tag_id, id) VALUES ('5f023b74-7d7a-4723-a8a9-0b34cc145494', '0376f22c-00cd-45eb-8b11-bc96be7c50ec', 'ba49241a-b30f-4bca-ae55-2a54e5efe91b');
INSERT INTO public.t_sv_post_tag (post_id, tag_id, id) VALUES ('5f023b74-7d7a-4723-a8a9-0b34cc145494', '5a030fdf-89e9-4e1f-82c9-59394c62ba72', '0e015c69-68c0-4795-aab2-c6e2e1078664');
INSERT INTO public.t_sv_post_tag (post_id, tag_id, id) VALUES ('a501935e-fc40-431b-a075-7cd8398368c1', '2cf31370-69c6-4c63-8f30-e66e38e31793', 'ea735464-77bb-40f5-8d59-0c9ad1c1ef3d');
INSERT INTO public.t_sv_post_tag (post_id, tag_id, id) VALUES ('a501935e-fc40-431b-a075-7cd8398368c1', 'bffc68c6-d4ff-456a-adbc-93bdbed92b86', 'd4ac3a1e-a69d-467a-91c4-987d38e3f10e');
INSERT INTO public.t_sv_post_tag (post_id, tag_id, id) VALUES ('a501935e-fc40-431b-a075-7cd8398368c1', 'b3013a2a-6060-4e65-8ac7-d6a4c4b6ffcf', 'ace89f00-6ad2-4a89-9c56-e613b26cd773');
INSERT INTO public.t_sv_post_tag (post_id, tag_id, id) VALUES ('7b7f78d2-d406-4086-b5f3-7b0b8ae2e053', '2cf31370-69c6-4c63-8f30-e66e38e31793', '02e629e0-9f34-47fa-ba9b-baf2ceebe00e');
INSERT INTO public.t_sv_post_tag (post_id, tag_id, id) VALUES ('7b7f78d2-d406-4086-b5f3-7b0b8ae2e053', '998b2421-1969-4180-879c-7094ad731d94', 'ff8b9194-6b49-48c2-9855-36ed64dbb21c');
INSERT INTO public.t_sv_post_tag (post_id, tag_id, id) VALUES ('4e1b5789-1432-4039-aae8-2d6e9ffe13d6', 'bc89b10f-e6fc-4fa1-9846-d22aaf4067ac', 'b7586d2d-ece4-4c5b-ba3c-8b31ce909023');
INSERT INTO public.t_sv_post_tag (post_id, tag_id, id) VALUES ('4e1b5789-1432-4039-aae8-2d6e9ffe13d6', 'b0d8e732-e7d1-4199-99ee-c7762dd541fc', 'd4b01a74-2174-4b5f-a8cd-3cf742c919af');
INSERT INTO public.t_sv_post_tag (post_id, tag_id, id) VALUES ('7a8ffad4-99c8-4d48-86ad-49688daeda2b', '4b595bac-23bd-493c-be60-99693d627ff9', 'af66107b-4daf-4a80-9522-d12eaf70d0e5');
INSERT INTO public.t_sv_post_tag (post_id, tag_id, id) VALUES ('7a8ffad4-99c8-4d48-86ad-49688daeda2b', '520e04e5-cb65-46bb-afd1-fc770e646b38', '8c82eeee-5b22-4b2b-924f-59fef99c4019');
INSERT INTO public.t_sv_post_tag (post_id, tag_id, id) VALUES ('a7846460-fb23-4cfc-b67e-c3b4f4f9d8e0', '2cf31370-69c6-4c63-8f30-e66e38e31793', '23a4e94a-3183-4e3c-af37-59358bfb695c');
INSERT INTO public.t_sv_post_tag (post_id, tag_id, id) VALUES ('a7846460-fb23-4cfc-b67e-c3b4f4f9d8e0', 'e24051ee-bf51-4248-b169-1b925f0060fe', 'ddee5c97-7a19-4d28-a3ad-c0bd1d787c11');
INSERT INTO public.t_sv_post_tag (post_id, tag_id, id) VALUES ('ac6a67ed-f18c-4faf-b094-dc4c932e1c43', 'bc89b10f-e6fc-4fa1-9846-d22aaf4067ac', 'b21db20f-ad67-4eb1-abe9-59534a795eb2');
INSERT INTO public.t_sv_post_tag (post_id, tag_id, id) VALUES ('10ec2c6a-48eb-41ef-9f55-1eea2e13d1c8', 'bc89b10f-e6fc-4fa1-9846-d22aaf4067ac', '422ad960-c04f-41a9-9751-cc40381bf0f4');
INSERT INTO public.t_sv_post_tag (post_id, tag_id, id) VALUES ('42b95bae-2eb8-4036-b5e4-eea6045ca2af', 'bc89b10f-e6fc-4fa1-9846-d22aaf4067ac', 'f60ed8cc-ae92-4e3d-bc92-d5abc14e683c');


--
-- TOC entry 3184 (class 2606 OID 82008)
-- Name: t_comments pk_comment; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_comments
    ADD CONSTRAINT pk_comment PRIMARY KEY (id);


--
-- TOC entry 3182 (class 2606 OID 81999)
-- Name: t_posts pk_post; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_posts
    ADD CONSTRAINT pk_post PRIMARY KEY (id);


--
-- TOC entry 3186 (class 2606 OID 82019)
-- Name: t_tags pk_tag; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_tags
    ADD CONSTRAINT pk_tag PRIMARY KEY (id);


--
-- TOC entry 3190 (class 2606 OID 82026)
-- Name: t_sv_post_tag t_sv_post_tag_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_sv_post_tag
    ADD CONSTRAINT t_sv_post_tag_pkey PRIMARY KEY (id);


--
-- TOC entry 3188 (class 2606 OID 82021)
-- Name: t_tags unq_name; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_tags
    ADD CONSTRAINT unq_name UNIQUE (name);


--
-- TOC entry 3192 (class 2606 OID 82028)
-- Name: t_sv_post_tag unq_sv_post_tag; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_sv_post_tag
    ADD CONSTRAINT unq_sv_post_tag UNIQUE (post_id, tag_id);


--
-- TOC entry 3193 (class 2606 OID 82009)
-- Name: t_comments fk_post; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_comments
    ADD CONSTRAINT fk_post FOREIGN KEY (post_id) REFERENCES public.t_posts(id) ON DELETE CASCADE;


--
-- TOC entry 3194 (class 2606 OID 82029)
-- Name: t_sv_post_tag fk_post; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_sv_post_tag
    ADD CONSTRAINT fk_post FOREIGN KEY (post_id) REFERENCES public.t_posts(id) ON DELETE CASCADE;


--
-- TOC entry 3195 (class 2606 OID 82034)
-- Name: t_sv_post_tag fk_tag; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_sv_post_tag
    ADD CONSTRAINT fk_tag FOREIGN KEY (tag_id) REFERENCES public.t_tags(id) ON DELETE CASCADE;


-- Completed on 2025-07-09 06:47:32

--
-- PostgreSQL database dump complete
--

