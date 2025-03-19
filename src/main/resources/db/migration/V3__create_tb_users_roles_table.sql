CREATE TABLE tb_users_roles (
    user_id uuid not null,
    role_id int8 not null,
    constraint fk_user_id foreign key (user_id) references tb_users,
    constraint fk_role_id foreign key (role_id) references tb_roles
);
