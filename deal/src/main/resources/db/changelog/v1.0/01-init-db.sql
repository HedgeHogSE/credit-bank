-- "Enum" гендера
create table gender (
    gender varchar(20) primary key
);

-- "Enum" семейного положения
create table marital_status (
    marital_status varchar(50) primary key
);

-- "Enum" статуса заявки
create table application_status (
    application_status varchar(50) primary key
);

-- "Enum" статуса кредита
create table credit_status (
    credit_status varchar(20) primary key
);
------

create table client (
                        client_id UUID primary key default gen_random_uuid(),
                        last_name varchar(50) not null,
                        first_name varchar(50) not null,
                        middle_name varchar(50) not null,
                        birth_date date not null,
                        email varchar(200) not null,
                        gender varchar(20) references gender(gender),
                        marital_status varchar(50) references marital_status(marital_status),
                        dependent_amount int,
                        passport jsonb,
                        employment jsonb,
                        account_number varchar(200)
);

CREATE TABLE credit (
                        credit_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        amount DECIMAL(19, 2) not null,
                        term INT not null,
                        monthly_payment DECIMAL(19, 2) not null,
                        rate DECIMAL(5, 2) not null,
                        psk DECIMAL(5, 2) not null,
                        payment_schedule JSONB not null,
                        insurance_enabled BOOLEAN DEFAULT FALSE not null,
                        salary_client BOOLEAN DEFAULT FALSE not null,
                        credit_status VARCHAR(50) REFERENCES credit_status(credit_status) not null
);

CREATE TABLE statement (
                           statement_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           client_id UUID REFERENCES client(client_id) not null,
                           credit_id UUID REFERENCES credit(credit_id),
                           status VARCHAR(50) not null REFERENCES application_status(application_status),
                           creation_date TIMESTAMP not null default current_timestamp,
                           applied_offer JSONB,
                           sign_date TIMESTAMP,
                           ses_code VARCHAR(200),
                           status_history JSONB not null
);

-- Гендер
INSERT INTO gender (gender) VALUES ('MALE'),
                                   ('FEMALE');

-- Семейное положение
INSERT INTO marital_status (marital_status) VALUES ('MARRIED'),
                                                   ('DIVORCED'),
                                                   ('SINGLE'),
                                                   ('WIDOWED');

-- Статус заявки
INSERT INTO application_status (application_status) VALUES ('PREAPPROVAL'),
                                                           ('APPROVED'),
                                                           ('CC_DENIED'),
                                                           ('CC_APPROVED'),
                                                           ('PREPARE_DOCUMENTS'),
                                                           ('DOCUMENT_SIGNED'),
                                                           ('CLIENT_DENIED');

-- Статус кредита
INSERT INTO credit_status (credit_status) VALUES ('CALCULATED'),
                                                 ('ISSUED');
