ALTER TABLE statement DROP CONSTRAINT statement_client_id_fkey;
ALTER TABLE statement DROP CONSTRAINT statement_credit_id_fkey;

ALTER TABLE statement
    ADD CONSTRAINT statement_client_id_fkey
        FOREIGN KEY (client_id)
            REFERENCES client(client_id) ON DELETE CASCADE;

ALTER TABLE statement
    ADD CONSTRAINT statement_credit_id_fkey
        FOREIGN KEY (credit_id)
            REFERENCES credit(credit_id)
            ON DELETE CASCADE;