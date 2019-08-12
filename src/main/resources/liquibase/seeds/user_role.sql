INSERT INTO user_role (user_id, role_id)
VALUES
((SELECT id FROM `user` WHERE username = 'hedza06'), (SELECT id FROM `role` WHERE `name` = 'ROLE_ADMIN')),
((SELECT id FROM `user` WHERE username = 'john'), (SELECT id FROM `role` WHERE `name` = 'ROLE_DEVELOPER')),
((SELECT id FROM `user` WHERE username = 'jasmine'), (SELECT id FROM `role` WHERE `name` = 'ROLE_MANAGER'));
