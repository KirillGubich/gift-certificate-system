INSERT INTO gift_certificates (name, description, price, duration, create_date, last_update_date)
VALUES ('test1', 'Test description 1', 1, 1, '2021-06-01 23:24:38.851', '2021-05-01 23:24:38.851');
INSERT INTO gift_certificates (name, description, price, duration, create_date, last_update_date)
VALUES ('test2', 'Test description 2', 2, 2, '2021-06-01 23:24:58.419', '2021-05-01 23:24:58.419');
INSERT INTO tags (name) VALUES ('firstTag');
INSERT INTO tags (name) VALUES ('secondTag');
INSERT INTO certificate_tag (certificate_id, tag_id) VALUES (1, 1);
INSERT INTO certificate_tag (certificate_id, tag_id) VALUES (1, 2);
INSERT INTO certificate_tag (certificate_id, tag_id) VALUES (2, 1);