drop SEQUENCE user_details_seq;
drop SEQUENCE bank_details_seq;

drop SEQUENCE buyer_details_seq;
drop SEQUENCE transprt_details_seq;

DROP TABLE user_details CASCADE;
DROP TABLE bank_details CASCADE;
DROP TABLE buyer_details CASCADE;
DROP TABLE transport_details CASCADE;


CREATE SEQUENCE user_details_seq START 1 INCREMENT 1;

CREATE TABLE user_details (
    user_id BIGINT DEFAULT nextval('user_details_seq') PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address TEXT NOT NULL,
    gst_no VARCHAR(15) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    pan VARCHAR(10) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(100) NOT NULL,
    updated_at TIMESTAMP,
    updated_by VARCHAR(100),
    bank_id BIGINT REFERENCES user_details(user_id) ON DELETE CASCADE
);


CREATE SEQUENCE bank_details_seq START 1 INCREMENT 1;
CREATE TABLE bank_details (
    bank_id BIGINT DEFAULT nextval('bank_details_seq') PRIMARY KEY,
    bank_name VARCHAR(50) NOT NULL,
    account_no VARCHAR(20) UNIQUE NOT NULL,
    branch VARCHAR(50) NOT NULL,
    ifsc_code VARCHAR(11) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(100) NOT NULL,
    updated_at TIMESTAMP,
    updated_by VARCHAR(100)
);

CREATE SEQUENCE buyer_details_seq START 1 INCREMENT 1;
CREATE TABLE buyer_details (
    buyer_id BIGINT DEFAULT nextval('buyer_details_seq') PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address TEXT NOT NULL,
    gst_no VARCHAR(15) ,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(100) NOT NULL,
    updated_at TIMESTAMP,
    updated_by VARCHAR(100)
);
CREATE SEQUENCE transport_details_seq START 140 INCREMENT 1;
CREATE TABLE IF NOT EXISTS public.transport_details
(
    invoice_no bigint NOT NULL DEFAULT nextval('transport_details_seq'::regclass),
    date date NOT NULL,
    client_name character varying(255) COLLATE pg_catalog."default",
    origin character varying(255) COLLATE pg_catalog."default" NOT NULL,
    destination character varying(255) COLLATE pg_catalog."default" NOT NULL,
    no_of_days integer,
    vehicle_no character varying(255) COLLATE pg_catalog."default",
    vehicle_type character varying(255) COLLATE pg_catalog."default",
    driver_name character varying(255) COLLATE pg_catalog."default",
    total_hrs numeric(10,2),
    actual_hrs numeric(10,2),
    extra_hrs numeric(10,2),
    basic_km numeric(10,2),
    base_fare numeric(10,2),
    actual_km numeric(10,2),
    extra_km numeric(10,2),
    per_km_rate numeric(10,2),
    per_hr_rate numeric(10,2),
    extra_hr_rate numeric(10,2),
    extra_km_rate numeric(10,2),
    toll_and_parking numeric(10,2),
    net_amount numeric(10,2),
    advance numeric(10,2),
    balance numeric(10,2),
    buyer_id bigint,
    user_id bigint,
    created_at timestamp without time zone NOT NULL,
    created_by character varying(255) COLLATE pg_catalog."default" NOT NULL,
    updated_at timestamp without time zone,
    updated_by character varying(255) COLLATE pg_catalog."default",
    rate numeric(10,2) DEFAULT 2.5,
    cgst numeric(10,2),
    sgst numeric(10,2),
    tax_amount numeric(10,2),
    grand_net_amount numeric(10,2),
    grand_net_amount_inwords character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT transport_details_pkey PRIMARY KEY (invoice_no),
    CONSTRAINT fk_buyer_id FOREIGN KEY (buyer_id)
        REFERENCES public.buyer_details (buyer_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id)
        REFERENCES public.user_details (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.transport_details
    OWNER to postgres;


INSERT INTO user_details (name, address, gst_no, email, pan,created_by)
VALUES (
    'JEESHA ENTERPRISES',
    'Teerth Avila, C1/606, Susgaon, Pune, Maharashtra 411021',
    '27FMOPS6754N2ZS',
    'jeeshaenterprise2017@gmail.com',
    'FMOPS6754N',1
);
 INSERT INTO bank_details ( bank_name, account_no, branch, ifsc_code,created_by)
VALUES (

    'YES BANK',
    '072852000005482',
    'WAKAD',
    'YESB0000728',1
);


INSERT INTO buyer_details (name, address, gst_no,created_by)
VALUES (
    'Flame University',
    'GAT NO 1270, Lavale, Taluka- Mulshi, Dist – Pune, Maharashtra 412115',
    '27AAATF2122L1ZW',1
);
