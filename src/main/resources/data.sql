INSERT INTO user
    (first_name, last_name, email, password, role, team_id)
VALUES
    ('Test', 'User', 'test@mail.com', 'password', 'USER', NULL),
    ('Team', 'Owner', 'owner@mail.com', 'password', 'USER', NULL);

INSERT INTO team
    (name, invite_code, owner_id)
VALUES
    ('Seeded Owners', 'OWNER001', (SELECT id FROM user WHERE email = 'owner@mail.com'));

UPDATE user
SET team_id = (SELECT id FROM team WHERE invite_code = 'OWNER001')
WHERE email = 'owner@mail.com';

INSERT INTO location
    (city, stadium_name, country)
VALUES
    ('Atlanta', 'Mercedes-Benz Stadium', 'USA'),
    ('Boston', 'Gillette Stadium', 'USA'),
    ('Dallas', 'AT&T Stadium', 'USA'),
    ('Houston', 'NRG Stadium', 'USA'),
    ('Kansas City', 'Arrowhead Stadium', 'USA'),
    ('Los Angeles', 'SoFi Stadium', 'USA'),
    ('Miami', 'Hard Rock Stadium', 'USA'),
    ('New York/New Jersey', 'MetLife Stadium', 'USA'),
    ('Philadelphia', 'Lincoln Financial Field', 'USA'),
    ('San Francisco Bay Area', 'Levi''s Stadium', 'USA'),
    ('Seattle', 'Lumen Field', 'USA'),
    ('Toronto', 'BMO Field', 'CANADA'),
    ('Vancouver', 'BC Place', 'CANADA'),
    ('Guadalajara', 'Estadio Akron', 'MEXICO'),
    ('Mexico City', 'Estadio Azteca', 'MEXICO'),
    ('Monterrey', 'Estadio BBVA', 'MEXICO');

INSERT INTO matches
    (home_country, away_country, match_date_time, location_id, stadium_code, checksum, final_home_score, final_away_score, match_stage)
VALUES
    ('BELGIUM', 'BRAZIL', '2026-04-05 18:30:00', (SELECT id FROM location WHERE stadium_name = 'Hard Rock Stadium'), 73, 73, 4, 3, 'GROUP_G'),
    ('JAPAN', 'MOROCCO', '2026-04-06 20:00:00', (SELECT id FROM location WHERE stadium_name = 'BMO Field'), 74, 74, NULL, NULL, 'GROUP_F'),
    ('MEXICO', 'SOUTH_AFRICA', '2026-06-11 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Estadio Azteca'), 1, 1, NULL, NULL, 'GROUP_A'),
    ('KOREA_REPUBLIC', 'CZECHIA', '2026-06-11 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Estadio Akron'), 2, 2, NULL, NULL, 'GROUP_A'),
    ('CANADA', 'BOSNIA_AND_HERZEGOVINA', '2026-06-12 12:00:00', (SELECT id FROM location WHERE stadium_name = 'BMO Field'), 3, 3, NULL, NULL, 'GROUP_B'),
    ('USA', 'PARAGUAY', '2026-06-12 12:00:00', (SELECT id FROM location WHERE stadium_name = 'SoFi Stadium'), 4, 4, NULL, NULL, 'GROUP_D'),
    ('HAITI', 'SCOTLAND', '2026-06-13 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Gillette Stadium'), 5, 5, NULL, NULL, 'GROUP_C'),
    ('AUSTRALIA', 'TURKIYE', '2026-06-13 12:00:00', (SELECT id FROM location WHERE stadium_name = 'BC Place'), 6, 6, NULL, NULL, 'GROUP_D'),
    ('BRAZIL', 'MOROCCO', '2026-06-13 12:00:00', (SELECT id FROM location WHERE stadium_name = 'MetLife Stadium'), 7, 7, NULL, NULL, 'GROUP_C'),
    ('QATAR', 'SWITZERLAND', '2026-06-13 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Levi''s Stadium'), 8, 8, NULL, NULL, 'GROUP_B'),
    ('COTE_DIVOIRE', 'ECUADOR', '2026-06-14 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Lincoln Financial Field'), 9, 9, NULL, NULL, 'GROUP_E'),
    ('GERMANY', 'CURACAO', '2026-06-14 12:00:00', (SELECT id FROM location WHERE stadium_name = 'NRG Stadium'), 10, 10, NULL, NULL, 'GROUP_E'),
    ('NETHERLANDS', 'JAPAN', '2026-06-14 12:00:00', (SELECT id FROM location WHERE stadium_name = 'AT&T Stadium'), 11, 11, NULL, NULL, 'GROUP_F'),
    ('SWEDEN', 'TUNISIA', '2026-06-14 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Estadio BBVA'), 12, 12, NULL, NULL, 'GROUP_F'),
    ('SAUDI_ARABIA', 'URUGUAY', '2026-06-15 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Hard Rock Stadium'), 13, 13, NULL, NULL, 'GROUP_H'),
    ('SPAIN', 'CABO_VERDE', '2026-06-15 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Mercedes-Benz Stadium'), 14, 14, NULL, NULL, 'GROUP_H'),
    ('IR_IRAN', 'NEW_ZEALAND', '2026-06-15 12:00:00', (SELECT id FROM location WHERE stadium_name = 'SoFi Stadium'), 15, 15, NULL, NULL, 'GROUP_G'),
    ('BELGIUM', 'EGYPT', '2026-06-15 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Lumen Field'), 16, 16, NULL, NULL, 'GROUP_G'),
    ('FRANCE', 'SENEGAL', '2026-06-16 12:00:00', (SELECT id FROM location WHERE stadium_name = 'MetLife Stadium'), 17, 17, NULL, NULL, 'GROUP_I'),
    ('IRAQ', 'NORWAY', '2026-06-16 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Gillette Stadium'), 18, 18, NULL, NULL, 'GROUP_I'),
    ('ARGENTINA', 'ALGERIA', '2026-06-16 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Arrowhead Stadium'), 19, 19, NULL, NULL, 'GROUP_J'),
    ('AUSTRIA', 'JORDAN', '2026-06-16 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Levi''s Stadium'), 20, 20, NULL, NULL, 'GROUP_J'),
    ('GHANA', 'PANAMA', '2026-06-17 12:00:00', (SELECT id FROM location WHERE stadium_name = 'BMO Field'), 21, 21, NULL, NULL, 'GROUP_L'),
    ('ENGLAND', 'CROATIA', '2026-06-17 12:00:00', (SELECT id FROM location WHERE stadium_name = 'AT&T Stadium'), 22, 22, NULL, NULL, 'GROUP_L'),
    ('PORTUGAL', 'CONGO_DR', '2026-06-17 12:00:00', (SELECT id FROM location WHERE stadium_name = 'NRG Stadium'), 23, 23, NULL, NULL, 'GROUP_K'),
    ('UZBEKISTAN', 'COLOMBIA', '2026-06-17 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Estadio Azteca'), 24, 24, NULL, NULL, 'GROUP_K'),
    ('CZECHIA', 'SOUTH_AFRICA', '2026-06-18 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Mercedes-Benz Stadium'), 25, 25, NULL, NULL, 'GROUP_A'),
    ('SWITZERLAND', 'BOSNIA_AND_HERZEGOVINA', '2026-06-18 12:00:00', (SELECT id FROM location WHERE stadium_name = 'SoFi Stadium'), 26, 26, NULL, NULL, 'GROUP_B'),
    ('CANADA', 'QATAR', '2026-06-18 12:00:00', (SELECT id FROM location WHERE stadium_name = 'BC Place'), 27, 27, NULL, NULL, 'GROUP_B'),
    ('MEXICO', 'KOREA_REPUBLIC', '2026-06-18 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Estadio Akron'), 28, 28, NULL, NULL, 'GROUP_A'),
    ('BRAZIL', 'HAITI', '2026-06-19 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Lincoln Financial Field'), 29, 29, NULL, NULL, 'GROUP_C'),
    ('SCOTLAND', 'MOROCCO', '2026-06-19 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Gillette Stadium'), 30, 30, NULL, NULL, 'GROUP_C'),
    ('TURKIYE', 'PARAGUAY', '2026-06-19 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Levi''s Stadium'), 31, 31, NULL, NULL, 'GROUP_D'),
    ('USA', 'AUSTRALIA', '2026-06-19 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Lumen Field'), 32, 32, NULL, NULL, 'GROUP_D'),
    ('GERMANY', 'COTE_DIVOIRE', '2026-06-20 12:00:00', (SELECT id FROM location WHERE stadium_name = 'BMO Field'), 33, 33, NULL, NULL, 'GROUP_E'),
    ('ECUADOR', 'CURACAO', '2026-06-20 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Arrowhead Stadium'), 34, 34, NULL, NULL, 'GROUP_E'),
    ('NETHERLANDS', 'SWEDEN', '2026-06-20 12:00:00', (SELECT id FROM location WHERE stadium_name = 'NRG Stadium'), 35, 35, NULL, NULL, 'GROUP_F'),
    ('TUNISIA', 'JAPAN', '2026-06-20 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Estadio BBVA'), 36, 36, NULL, NULL, 'GROUP_F'),
    ('URUGUAY', 'CABO_VERDE', '2026-06-21 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Hard Rock Stadium'), 37, 37, NULL, NULL, 'GROUP_H'),
    ('SPAIN', 'SAUDI_ARABIA', '2026-06-21 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Mercedes-Benz Stadium'), 38, 38, NULL, NULL, 'GROUP_H'),
    ('BELGIUM', 'IR_IRAN', '2026-06-21 12:00:00', (SELECT id FROM location WHERE stadium_name = 'SoFi Stadium'), 39, 39, NULL, NULL, 'GROUP_G'),
    ('NEW_ZEALAND', 'EGYPT', '2026-06-21 12:00:00', (SELECT id FROM location WHERE stadium_name = 'BC Place'), 40, 40, NULL, NULL, 'GROUP_G'),
    ('NORWAY', 'SENEGAL', '2026-06-22 12:00:00', (SELECT id FROM location WHERE stadium_name = 'MetLife Stadium'), 41, 41, NULL, NULL, 'GROUP_I'),
    ('FRANCE', 'IRAQ', '2026-06-22 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Lincoln Financial Field'), 42, 42, NULL, NULL, 'GROUP_I'),
    ('ARGENTINA', 'AUSTRIA', '2026-06-22 12:00:00', (SELECT id FROM location WHERE stadium_name = 'AT&T Stadium'), 43, 43, NULL, NULL, 'GROUP_J'),
    ('JORDAN', 'ALGERIA', '2026-06-22 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Levi''s Stadium'), 44, 44, NULL, NULL, 'GROUP_J'),
    ('ENGLAND', 'GHANA', '2026-06-23 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Gillette Stadium'), 45, 45, NULL, NULL, 'GROUP_L'),
    ('PANAMA', 'CROATIA', '2026-06-23 12:00:00', (SELECT id FROM location WHERE stadium_name = 'BMO Field'), 46, 46, NULL, NULL, 'GROUP_L'),
    ('PORTUGAL', 'UZBEKISTAN', '2026-06-23 12:00:00', (SELECT id FROM location WHERE stadium_name = 'NRG Stadium'), 47, 47, NULL, NULL, 'GROUP_K'),
    ('COLOMBIA', 'CONGO_DR', '2026-06-23 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Estadio Akron'), 48, 48, NULL, NULL, 'GROUP_K'),
    ('SCOTLAND', 'BRAZIL', '2026-06-24 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Hard Rock Stadium'), 49, 49, NULL, NULL, 'GROUP_C'),
    ('MOROCCO', 'HAITI', '2026-06-24 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Mercedes-Benz Stadium'), 50, 50, NULL, NULL, 'GROUP_C'),
    ('SWITZERLAND', 'CANADA', '2026-06-24 12:00:00', (SELECT id FROM location WHERE stadium_name = 'BC Place'), 51, 51, NULL, NULL, 'GROUP_B'),
    ('BOSNIA_AND_HERZEGOVINA', 'QATAR', '2026-06-24 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Lumen Field'), 52, 52, NULL, NULL, 'GROUP_B'),
    ('CZECHIA', 'MEXICO', '2026-06-24 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Estadio Azteca'), 53, 53, NULL, NULL, 'GROUP_A'),
    ('SOUTH_AFRICA', 'KOREA_REPUBLIC', '2026-06-24 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Estadio BBVA'), 54, 54, NULL, NULL, 'GROUP_A'),
    ('CURACAO', 'COTE_DIVOIRE', '2026-06-25 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Lincoln Financial Field'), 55, 55, NULL, NULL, 'GROUP_E'),
    ('ECUADOR', 'GERMANY', '2026-06-25 12:00:00', (SELECT id FROM location WHERE stadium_name = 'MetLife Stadium'), 56, 56, NULL, NULL, 'GROUP_E'),
    ('JAPAN', 'SWEDEN', '2026-06-25 12:00:00', (SELECT id FROM location WHERE stadium_name = 'AT&T Stadium'), 57, 57, NULL, NULL, 'GROUP_F'),
    ('TUNISIA', 'NETHERLANDS', '2026-06-25 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Arrowhead Stadium'), 58, 58, NULL, NULL, 'GROUP_F'),
    ('TURKIYE', 'USA', '2026-06-25 12:00:00', (SELECT id FROM location WHERE stadium_name = 'SoFi Stadium'), 59, 59, NULL, NULL, 'GROUP_D'),
    ('PARAGUAY', 'AUSTRALIA', '2026-06-25 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Levi''s Stadium'), 60, 60, NULL, NULL, 'GROUP_D'),
    ('NORWAY', 'FRANCE', '2026-06-26 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Gillette Stadium'), 61, 61, NULL, NULL, 'GROUP_I'),
    ('SENEGAL', 'IRAQ', '2026-06-26 12:00:00', (SELECT id FROM location WHERE stadium_name = 'BMO Field'), 62, 62, NULL, NULL, 'GROUP_I'),
    ('EGYPT', 'IR_IRAN', '2026-06-26 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Lumen Field'), 63, 63, NULL, NULL, 'GROUP_G'),
    ('NEW_ZEALAND', 'BELGIUM', '2026-06-26 12:00:00', (SELECT id FROM location WHERE stadium_name = 'BC Place'), 64, 64, NULL, NULL, 'GROUP_G'),
    ('CABO_VERDE', 'SAUDI_ARABIA', '2026-06-26 12:00:00', (SELECT id FROM location WHERE stadium_name = 'NRG Stadium'), 65, 65, NULL, NULL, 'GROUP_H'),
    ('URUGUAY', 'SPAIN', '2026-06-26 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Estadio Akron'), 66, 66, NULL, NULL, 'GROUP_H'),
    ('PANAMA', 'ENGLAND', '2026-06-27 12:00:00', (SELECT id FROM location WHERE stadium_name = 'MetLife Stadium'), 67, 67, NULL, NULL, 'GROUP_L'),
    ('CROATIA', 'GHANA', '2026-06-27 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Lincoln Financial Field'), 68, 68, NULL, NULL, 'GROUP_L'),
    ('ALGERIA', 'AUSTRIA', '2026-06-27 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Arrowhead Stadium'), 69, 69, NULL, NULL, 'GROUP_J'),
    ('JORDAN', 'ARGENTINA', '2026-06-27 12:00:00', (SELECT id FROM location WHERE stadium_name = 'AT&T Stadium'), 70, 70, NULL, NULL, 'GROUP_J'),
    ('COLOMBIA', 'PORTUGAL', '2026-06-27 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Hard Rock Stadium'), 71, 71, NULL, NULL, 'GROUP_K'),
    ('CONGO_DR', 'UZBEKISTAN', '2026-06-27 12:00:00', (SELECT id FROM location WHERE stadium_name = 'Mercedes-Benz Stadium'), 72, 72, NULL, NULL, 'GROUP_K');
