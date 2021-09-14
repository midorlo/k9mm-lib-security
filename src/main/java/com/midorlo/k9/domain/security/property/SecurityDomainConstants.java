package com.midorlo.k9.domain.security.property;

public class SecurityDomainConstants {

    public static final String SCHEMA = "security";

    public static final String ACCOUNTS_TABLE  = "accounts";
    public static final String ACCOUNTS_ENTITY = "account";

    public static final String ROLES = "roles";
    public static final String ROLE  = "role";

    public static final String ID   = "id";
    public static final String NAME = "name";

    public static final String ACCOUNTS_ID            = "id";
    public static final String ACCOUNTS_DISPLAY_NAME  = "display_name";
    public static final String ACCOUNTS_LOGIN         = "login";
    public static final String ACCOUNTS_PASSWORD_HASH = "password_hash";
    public static final String ACCOUNTS_STATE         = "state";

    public static final String REL_ACCOUNTS_ROLES         = "accounts_roles";
    public static final String REL_ACCOUNTS_ROLES_ACCOUNT = "id_account";
    public static final String REL_ACCOUNTS_ROLES_ROLE    = "id_role";

    public static final String REL_ACCOUNTS_CLEARANCES           = "accounts_clearances";
    public static final String REL_ACCOUNTS_CLEARANCES_ACCOUNT   = "id_account";
    public static final String REL_ACCOUNTS_CLEARANCES_CLEARANCE = "id_clearance";

    public static final String REL_ROLES_CLEARANCES           = "roles_clearances";
    public static final String REL_ROLES_CLEARANCES_ROLE      = "id_role";
    public static final String REL_ROLES_CLEARANCES_CLEARANCE = "id_clearance";

    public static final String CLEARANCES_TABLE  = "clearances";
    public static final String CLEARANCES_ENTITY = "clearance";

    public static final String CLEARANCES_METHOD               = "method";
    public static final String COL_NAME_ID_SERVLET_DESCRIPTION = "id_servlet_descriptions";
    public static final String ID_OWNER                        = "ID_OWNER";
}
