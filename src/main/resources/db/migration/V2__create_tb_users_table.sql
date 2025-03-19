CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE tb_users (
     id uuid NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
     username VARCHAR(255) NOT NULL,
     password VARCHAR(255) NOT NULL,
     created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
     updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL
);
