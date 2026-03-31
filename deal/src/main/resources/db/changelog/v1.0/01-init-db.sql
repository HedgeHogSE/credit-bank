-- "Enum" гендера
create table gender (
    gender varchar(20) primary key
);

-- "Enum" семейного положения
create table marital_status (
    marital_status varchar(50) primary key
);

-- "Enum" рабочего статуса
create table employment_status (
    employment_status varchar(50) primary key
);

-- "Enum" позиции на работе
create table employment_position (
    employment_position varchar(50) primary key
);

-- "Enum" статуса заявки
create table application_status (
    application_status varchar(50) primary key
);

-- "Enum" статуса кредита
create table credit_status (
    credit_status varchar(20) primary key
);

-- "Enum" изменения типа
create table change_type (
    change_type varchar(20) primary key
);

------

create table client (
                        client_id UUID primary key default gen_random_uuid(),
                        last_name varchar(50),
                        first_name varchar(50),
                        middle_name varchar(50),
                        birth_date date,
                        email varchar(200),
                        gender varchar(20) references gender(gender),
                        marital_status varchar(50) references marital_status(marital_status),
                        dependent_amount int,
                        passport jsonb,
                        employment jsonb,
                        account_number varchar(200)
);

CREATE TABLE credit (
                        credit_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        amount DECIMAL(19, 2),
                        term INT,
                        monthly_payment DECIMAL(19, 2),
                        rate DECIMAL(5, 2),
                        psk DECIMAL(5, 2),
                        payment_schedule JSONB,
                        insurance_enabled BOOLEAN DEFAULT FALSE,
                        salary_client BOOLEAN DEFAULT FALSE,
                        credit_status VARCHAR(50) REFERENCES credit_status(credit_status)
);

CREATE TABLE statement (
                           statement_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           client_id UUID REFERENCES client(client_id),
                           credit_id UUID REFERENCES credit(credit_id),
                           status VARCHAR(50) REFERENCES application_status(application_status),
                           creation_date TIMESTAMP,
                           applied_offer JSONB,
                           sign_date TIMESTAMP,
                           ses_code VARCHAR(200),
                           status_history JSONB
);

-- Гендер
INSERT INTO gender (gender) VALUES ('MALE'), ('FEMALE'); -- изменил

-- Семейное положение
INSERT INTO marital_status (marital_status) VALUES ('MARRIED'), ('DIVORCED'), ('SINGLE'), ('WIDOWED'); -- изменил

-- Статус занятости
INSERT INTO employment_status (employment_status) VALUES ('UNEMPLOYED'), ('SELF_EMPLOYED'), ('EMPLOYED'), ('BUSINESS_OWNER');

-- Должность
INSERT INTO employment_position (employment_position) VALUES ('WORKER'), ('MID_MANAGER'), ('TOP_MANAGER'); -- изменил

-- Статус заявки
INSERT INTO application_status (application_status) VALUES ('PREAPPROVAL'), ('APPROVED'), ('CC_DENIED'), ('CC_APPROVED'), ('PREPARE_DOCUMENTS'), ('DOCUMENT_SIGNED'), ('CLIENT_DENIED');

-- Статус кредита
INSERT INTO credit_status (credit_status) VALUES ('CALCULATED'), ('ISSUED');

-- Тип изменения (для истории статусов)
INSERT INTO change_type (change_type) VALUES ('AUTOMATIC'), ('MANUAL');