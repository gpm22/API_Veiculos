INSERT INTO OWNERS (owner_name, owner_email, owner_cpf, owner_birthdate)
VALUES ('tester', 'tester@tester.com', '000.000.000-00', '+1995-12-12');

INSERT INTO VEHICLES( vehicle_brand, vehicle_model, vehicle_year, vehicle_type, vehicle_rotation_day, vehicle_is_rotation_active, vehicle_price)
VALUES ('AGRALE', '0000 LX 2p (diesel) (E5)', '2017', 'CAMINHOES', 5, false, 'R$ 157.078,00');
INSERT INTO VEHICLES( vehicle_brand, vehicle_model, vehicle_year, vehicle_type, vehicle_rotation_day, vehicle_is_rotation_active, vehicle_price)
VALUES ('FORD', 'F-4 MILHA CD 2.8 4x2 (diesel)(E5)', '2019', 'CAMINHOES', 6, false, 'R$ 183.203,00');
INSERT INTO VEHICLES( vehicle_brand, vehicle_model, vehicle_year, vehicle_type, vehicle_rotation_day, vehicle_is_rotation_active, vehicle_price)
VALUES ('Toyota', 'Paseo', '1995 Gasolina', 'CARROS', 4, false,'R$ 27.146,00' );
INSERT INTO VEHICLES( vehicle_brand, vehicle_model, vehicle_year, vehicle_type, vehicle_rotation_day, vehicle_is_rotation_active, vehicle_price)
VALUES ('Volvo', 'XC 40 Twin Ultimate (Elétrico)', '2023 Gasolina', 'CARROS', 3, false, 'R$ 298.549,00');
INSERT INTO VEHICLES( vehicle_brand, vehicle_model, vehicle_year, vehicle_type, vehicle_rotation_day, vehicle_is_rotation_active, vehicle_price)
VALUES ('ADLY', 'JAGUAR JT 50', '1999', 'MOTOS', 6, false, 'R$ 1.595,00');
INSERT INTO VEHICLES( vehicle_brand, vehicle_model, vehicle_year, vehicle_type, vehicle_rotation_day, vehicle_is_rotation_active, vehicle_price)
VALUES ('YAMAHA', 'YFZ 450/ YZ 450 F 449cc', '2024', 'MOTOS', 4, false, 'R$ 76.907,00');
INSERT INTO VEHICLES( vehicle_brand, vehicle_model, vehicle_year, vehicle_type, vehicle_rotation_day, vehicle_is_rotation_active, vehicle_price)
VALUES ('HARLEY-DAVIDSON', 'CVO BREAKOUT FXSBSE', '2014', 'MOTOS', 4, false,'R$ 89.690,00' );
INSERT INTO VEHICLES( vehicle_brand, vehicle_model, vehicle_year, vehicle_type, vehicle_rotation_day, vehicle_is_rotation_active, vehicle_price)
VALUES ('ZONTES','T 310', '2024', 'MOTOS', 4, false, 'R$ 29.061,00' );

INSERT INTO OWNERS_VEHICLES(owner_id, vehicle_id)
VALUES (1, 1);
INSERT INTO OWNERS_VEHICLES(owner_id, vehicle_id)
VALUES (1, 2);
INSERT INTO OWNERS_VEHICLES(owner_id, vehicle_id)
VALUES (1, 3);
INSERT INTO OWNERS_VEHICLES(owner_id, vehicle_id)
VALUES (1, 4);
INSERT INTO OWNERS_VEHICLES(owner_id, vehicle_id)
VALUES (1, 5);
INSERT INTO OWNERS_VEHICLES(owner_id, vehicle_id)
VALUES (1, 6);
INSERT INTO OWNERS_VEHICLES(owner_id, vehicle_id)
VALUES (1, 7);
INSERT INTO OWNERS_VEHICLES(owner_id, vehicle_id)
VALUES (1, 8);
