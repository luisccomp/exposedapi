CREATE TABLE IF NOT EXISTS "contact"."contact"(
    "id"          BIGINT NOT NULL CONSTRAINT "pk_contact" PRIMARY KEY,
    "name"        VARCHAR(250) NOT NULL,
    "email"       VARCHAR(250) NOT NULL,
    "phone"       VARCHAR(250) NOT NULL,
    "customer_id" UUID NOT NULL,
    CONSTRAINT "fk_contact_customer" FOREIGN KEY ("customer_id") REFERENCES "customer"."customer"("id")
);