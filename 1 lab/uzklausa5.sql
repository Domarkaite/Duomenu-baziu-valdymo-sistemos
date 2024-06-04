SELECT information_schema.views.table_name
FROM information_schema.role_table_grants, information_schema.views, information_schema.table_privileges
WHERE role_table_grants.privilege_type = 'SELECT' 
AND table_privileges.grantee = 'PUBLIC' 
AND information_schema.role_table_grants.table_name = information_schema.views.table_name 
AND information_schema.table_privileges.table_name = information_schema.views.table_name 
AND information_schema.role_table_grants.table_name = information_schema.table_privileges.table_name;