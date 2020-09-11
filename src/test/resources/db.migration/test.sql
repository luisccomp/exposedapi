CREATE SCHEMA IF NOT EXISTS "customer";
CREATE SCHEMA IF NOT EXISTS "contact";

CREATE TABLE IF NOT EXISTS "customer"."customer"(
    "id"         UUID NOT NULL CONSTRAINT "pk_customer" PRIMARY KEY,
    "first_name" VARCHAR(250) NOT NULL,
    "last_name"  VARCHAR(250) NOT NULL,
    "email"      VARCHAR(250) NOT NULL CONSTRAINT "un_customer_email" UNIQUE,
    "phone"      VARCHAR(250) NOT NULL
);

CREATE TABLE IF NOT EXISTS "contact"."contact"(
    "id"          BIGSERIAL NOT NULL CONSTRAINT "pk_contact" PRIMARY KEY,
    "name"        VARCHAR(250) NOT NULL,
    "email"       VARCHAR(250) NOT NULL,
    "phone"       VARCHAR(250) NOT NULL,
    "customer_id" UUID NOT NULL,
    CONSTRAINT "fk_contact_customer" FOREIGN KEY ("customer_id") REFERENCES "customer"."customer"("id")
);
