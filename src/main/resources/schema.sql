create table accounts (
    id                  bigint       not null constraint accounts_pkey primary key,

    name                varchar(16)  not null constraint account_key_name unique,
    email               varchar(64)  not null constraint accounts_key_email unique,
    password            varchar(128) not null,
    state               integer      not null,

    created_date        timestamp,
    last_modified_date  timestamp,
    created_by_id       bigint constraint accounts_fk_created_accounts references accounts,
    last_modified_by_id bigint constraint accounts_fk_modified_accounts references accounts
);

create table rest_meta (

    id                  bigint       not null constraint rest_meta_pkey primary key,

    servlet_path        varchar(128) not null constraint rest_meta_key_servlet_path unique,

    created_date        timestamp,
    last_modified_date  timestamp,
    created_by_id       bigint constraint rest_meta_fk_created_accounts references accounts,
    last_modified_by_id bigint constraint rest_meta_fk_modified_accounts references accounts
);

create table authorities (
    id                  bigint       not null constraint authorities_pkey primary key,

    http_method         varchar(255) not null,
    id_rest_meta        bigint       not null constraint fkgw9ntbsc2eveup10l57rpxisg references rest_meta,

    created_date        timestamp,
    last_modified_date  timestamp,
    created_by_id       bigint constraint fkkrxn7jkh570ctw4ob4g07qykg references accounts,
    last_modified_by_id bigint constraint fkl7aes9ebxovc146pd72xs54la references accounts
);

create table role (
    id                  bigint       not null constraint role_pkey primary key,
    name                varchar(255) not null constraint uk_8sewwnpamngi6b1dwaa88askk unique,

    created_date        timestamp,
    last_modified_date  timestamp,
    created_by_id       bigint constraint fktl9xotj81tpjote5ibu0i83hn references accounts,
    last_modified_by_id bigint constraint fkrtmm39ng5narwdg8hpjrwv0pk references accounts
);

create table accounts_roles (
    id_account bigint not null constraint fk7of2xgg8h5b3t2pe6622ma8gn references accounts,
    id_role    bigint not null constraint fkr2kqi8wjxqlyg1fror6ilyiny references role
);

create table accounts_authorities (
    id_account   bigint not null constraint fkackmwrm319efrbnp2880wiyki references accounts,
    id_authority bigint not null constraint fk5xhblpwtnjqd8nctdttga5a2a references authorities,
    constraint accounts_authorities_pkey primary key (id_account, id_authority)
);

create table roles_authorities (
    id_role      bigint not null constraint fktb1i3csboav32kko7qeq3g0kn references role,
    id_authority bigint not null constraint fk8cycjq49jqouasn93csv8ta7t references authorities,
    constraint roles_authorities_pkey primary key (id_role, id_authority)
);

alter table accounts owner to k9;
alter table accounts_authorities owner to k9;
alter table accounts_roles owner to k9;
alter table authorities owner to k9;
alter table rest_meta owner to k9;
alter table role owner to k9;
alter table roles_authorities owner to k9;