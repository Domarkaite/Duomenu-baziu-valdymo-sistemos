INSERT INTO mido9017.Byla (ID, Pavadinimas, Statusas, Pradzia)
VALUES
  (1, 'Skyrybos', 'Nepradeta', '2023-12-08'),
  (2, 'Vagyste', 'Pradeta', '2023-12-19'),
  (3, 'Garsioji byla del valstybes isdavimo', 'Pradeta', '2023-12-06'),
  (4, 'Saligatviu nevalymas Vilniuje', 'Nutraukta', '2023-12-01');

INSERT INTO mido9017.Sale (Numeris, Adresas)
VALUES
  ('55', 'Vytauto g., 35'),
  ('109A', 'Sv. Jogailos g., 58A'),
  ('51', 'Napoleono g., 66');
  
INSERT INTO mido9017.Posedis (Sale, Laikas, Bylos_Kodas)
VALUES
  (1, '2023-12-11', 1),
  (2, '2023-12-20', 2),
  (3, '2023-12-25', 3);

INSERT INTO mido9017.Teisiamasis (Asmens_Kodas, Vardas, Pavarde, Tel_Nr, El_Pastas)
VALUES
  (31411229537, 'Marija', 'Jonaitiene', '+37064567890', 'marija.jonaitiene@gmail.com'),
  (30203139493, 'Jonas', 'Samuliauskas', '+37065678901', 'jonas.samuliauskas@gmail.com'),
  (60408029350, 'Edvinas', 'Lamulauskas', '+37066789042', 'edvinas.lamulauskas@gmail.com'),
  (42610288433, 'Leopoldas', 'Vainauskas', '+37066789032', 'leopoldas.vainauskas@gmail.com'),
  (38102258262, 'Livija', 'Sakalauskaite', '+37066789012', 'livija.sakalauskaite@gmail.com'),
  (48209279780, 'Marija', 'Elementauskaite', '+37066715012', 'marija.elementauskaite@gmail.com'),
  (38908039766, 'Darius', 'Bubliauskas', '+37069115012', 'darius.bubliauskas@gmail.com');

INSERT INTO mido9017.Advokatas (Advokato_Kodas, Vardas, Pavarde, Tel_Nr, El_Pastas)
VALUES
  (98765432109, 'Radvile', 'Mykolaitiene', '+37061234567', 'radvile.mykolaitiene@gmail.com'),
  (11223344556, 'Gabija', 'Maironyte', '+37062345678', 'gabija.maironyte@gmail.com'),
  (46787367712, 'Julius', 'Baranauskas', '+37063456189', 'julius.baranauskas@gmail.com'),
  (26282715159, 'Povilas', 'Nerijus', '+37063456729', 'povilas.nerijus@gmail.com'),
  (26105160606, 'Salomeja', 'Radilaite', '+37063456783', 'salomeja.radilaite@gmail.com'),
  (39006394336, 'Jolita', 'Vasiliauskiene', '+37063456749', 'jolita.vasiliauskiene@gmail.com'),
  (97055555022, 'Plinijus', 'Jaunesnysis', '+37063456785', 'plinijus.jaunesnysis@gmail.com'),
  (32126015080, 'Saulius', 'Dragunas', '+37063456789', 'saulius.dragunas@gmail.com');

INSERT INTO mido9017.Advokatai (Bylos_ID, Advokato_Kodas) VALUES
  (1, 98765432109), 
  (1, 11223344556),  
  (1, 46787367712),  
  (1, 26282715159), 
  (1, 39006394336), 
  (2, 46787367712),  
  (2, 26282715159),  
  (3, 26105160606),  
  (3, 39006394336),  
  (4, 97055555022),  
  (4, 32126015080);  

INSERT INTO mido9017.Teisiamieji (Bylos_ID, Teisiamojo_Kodas) VALUES
  (1, 31411229537),  
  (2, 60408029350),  
  (3, 60408029350),  
  (3, 38102258262),  
  (3, 42610288433),  
  (4, 48209279780),  
  (4, 38908039766);  

REFRESH MATERIALIZED VIEW mido9017.MatView_Teisiamuju_Advokatu_Statistika;