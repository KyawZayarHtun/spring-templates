INSERT INTO role (created_at, updated_at, name)
VALUES ('2024-09-18 23:03:00.000000', '2024-09-18 23:03:02.000000', 'Admin');

INSERT user (activated, dob, locked, created_at, updated_at, user_role_id, phone_number, email,
             name, password, profile_image_path, gender)
VALUES ('T', '2024-09-18', 'F', '2024-09-18 23:03:41.000000', '2024-09-18 23:03:45.000000', 1, '09965122877',
        'admin@gmail.com', 'Admin', '$2a$12$wGcHoeBduDBUHTmV6lqKTueWKdTeHapMgocOpshaRc7ecjvxGWNUW', null, 'Male');

INSERT INTO role_access (created_at, name, url, request_method, crud_operation)
VALUES ('2024-09-18 23:07:47.000000', 'Home Page', '/', 'GET', 'READ'),
       ('2024-09-18 23:07:47.000000', 'Login Page', '/login', 'GET', 'READ'),
       ('2024-09-18 23:07:47.000000', 'User List Page', '/user/user-list', 'GET', 'READ'),
       ('2024-09-18 23:07:47.000000', 'User List Rest Method', '/user/user-list', 'POST', 'READ'),
       ('2024-09-18 23:07:47.000000', 'User List Excel Export Rest Method', '/user/export-excel', 'POST', 'READ'),
       ('2024-09-18 23:07:47.000000', 'Edit User Profile Page', '/user/edit-user-profile', 'GET', 'READ'),
       ('2024-09-18 23:07:47.000000', 'Edit User Profile Method', '/user/edit-user-profile', 'POST', 'UPDATE'),
       ('2024-09-18 23:07:47.000000', 'Role Page', '/role/manage-role', 'GET', 'READ'),
       ('2024-09-18 23:07:47.000000', 'Role Manage Method', '/role/manage-role', 'POST', 'UPDATE'),
       ('2024-09-18 23:07:47.000000', 'Role List Method', '/role/role-list', 'POST', 'READ'),
       ('2024-09-18 23:07:47.000000', 'Role Detail Page', '/role/role-detail', 'GET', 'READ'),
       ('2024-09-18 23:07:47.000000', 'Update Role Detail', '/role/role-detail', 'POST', 'UPDATE'),
       ('2024-09-18 23:07:47.000000', 'Role Access Page', '/role-access/manage-role-access', 'GET', 'READ'),
       ('2024-09-18 23:07:47.000000', 'Manage Role Access', '/role-access/manage-role-access', 'POST', 'UPDATE'),
       ('2024-09-18 23:07:47.000000', 'Role Access List Method', '/role-access/role-access-list', 'POST', 'READ'),
       ('2024-09-18 23:07:47.000000', 'Delete Role Access Method', '/role-access/delete-role-access', 'POST', 'DELETE');

INSERT INTO role_role_access (role_access_id, role_id)
VALUES
    (1, 1),
    (2, 1),
    (3, 1),
    (4, 1),
    (5, 1),
    (6, 1),
    (7, 1),
    (8, 1),
    (9, 1),
    (10, 1),
    (11, 1),
    (12, 1),
    (13, 1),
    (14, 1),
    (15, 1),
    (16, 1);

