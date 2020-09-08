CREATE TABLE IF NOT EXISTS "customer"."customer"(
    "id"         UUID NOT NULL CONSTRAINT "pk_customer" PRIMARY KEY,
    "first_name" VARCHAR(250) NOT NULL,
    "last_name"  VARCHAR(250) NOT NULL,
    "email"      VARCHAR(250) NOT NULL CONSTRAINT "un_customer_email" UNIQUE,
    "phone"      VARCHAR(250) NOT NULL
);