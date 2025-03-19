CREATE TABLE tb_contacts (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(255),
    life_cycle_stage VARCHAR(255),
    hs_lead_status VARCHAR(255),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by uuid not null,
    constraint fk_created_by foreign key (created_by) references tb_users
);
