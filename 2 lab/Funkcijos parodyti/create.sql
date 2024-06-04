CREATE TABLE name.Byla (
    ID            INTEGER       NOT NULL,
    Pavadinimas   VARCHAR(254)  DEFAULT 'Nesukurtas',
    Statusas      VARCHAR(12)   NOT NULL CHECK(Statusas IN ('Nepradeta', 'Pradeta', 'Sustabdyta', 'Nutraukta', 'Pabaigta')),
    Pradzia       DATE,
    PRIMARY KEY (ID)
);

CREATE TABLE name.Sale (
    ID            Serial  NOT NULL,
    Numeris       CHAR(11) NOT NULL,
    Adresas       VARCHAR(254),
    PRIMARY KEY (ID)
);

CREATE TABLE name.Posedis (
    ID           Serial     NOT NULL,
    Sale         INTEGER    NOT NULL,
    Laikas       DATE DEFAULT CURRENT_DATE,
    Bylos_kodas  INTEGER NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT Byla FOREIGN KEY (Bylos_kodas) REFERENCES name.Byla ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT Sale FOREIGN KEY (Sale) REFERENCES name.Sale ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE INDEX idx_PosedzioLaikas ON name.Posedis (Laikas);

CREATE TABLE name.Advokatas (
    Advokato_Kodas  BIGINT NOT NULL,
    Vardas          VARCHAR(32) NOT NULL,
    Pavarde         VARCHAR(32) NOT NULL,
    Tel_Nr          VARCHAR(32) CONSTRAINT Val_Tel_numeris CHECK(Tel_Nr LIKE '+370%'),
    El_Pastas       CHAR(32) CONSTRAINT ValidusElPastas CHECK (El_Pastas LIKE '%_@__%.__%'),
    PRIMARY KEY (Advokato_Kodas)
);

CREATE TABLE name.Teisiamasis (
    Asmens_Kodas   BIGINT NOT NULL,
    Vardas         VARCHAR(32) NOT NULL,
    Pavarde        VARCHAR(32) NOT NULL,
    Tel_Nr         VARCHAR(32) CONSTRAINT Val_Tel_numeris CHECK(Tel_Nr LIKE '+370%'),
    El_Pastas      CHAR(32) CONSTRAINT ValidusElPastas CHECK (El_Pastas LIKE '%_@__%.__%'),
    PRIMARY KEY (Asmens_Kodas)
);

CREATE UNIQUE INDEX idx_TeisiamojoTel_Nr ON name.Teisiamasis (Tel_Nr);
CREATE UNIQUE INDEX idx_AdvokatoTel_Nr ON name.Advokatas (Tel_Nr);

CREATE TABLE name.Advokatai (
    Bylos_ID          INTEGER,
    Advokato_Kodas    BIGINT    NOT NULL,
    PRIMARY KEY (Bylos_ID, Advokato_Kodas),
    CONSTRAINT Advokatas FOREIGN KEY (Advokato_Kodas) REFERENCES name.Advokatas ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT Byla FOREIGN KEY (Bylos_ID) REFERENCES name.Byla ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE name.Teisiamieji (
    Bylos_ID          INTEGER,
    Teisiamojo_Kodas  BIGINT   NOT NULL,
    PRIMARY KEY (Bylos_ID, Teisiamojo_Kodas),
    CONSTRAINT Teisiamasis FOREIGN KEY (Teisiamojo_Kodas) REFERENCES name.Teisiamasis ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT Byla FOREIGN KEY (Bylos_ID) REFERENCES name.Byla ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE FUNCTION BylosPradziosData()
RETURNS TRIGGER AS $$
BEGIN
  IF (NEW.Pradzia < CURRENT_DATE - INTERVAL '1 MONTH') THEN
    RAISE EXCEPTION 'Data neatitinka reikalavimu: ivedimas galimas vieno menesio laikotarpyje';
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER BylosPradziosData
BEFORE INSERT OR UPDATE ON name.Byla
FOR EACH ROW
EXECUTE PROCEDURE BylosPradziosData();

CREATE FUNCTION PosedzioData()
RETURNS TRIGGER AS $$
BEGIN
  IF (NEW.Laikas < CURRENT_DATE - INTERVAL '1 MONTH') THEN
    RAISE EXCEPTION 'Data neatitinka reikalavimu: ivedimas galimas vieno menesio laikotarpyje';
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER PosedzioData
BEFORE INSERT OR UPDATE ON name.Posedis
FOR EACH ROW
EXECUTE PROCEDURE PosedzioData();

CREATE OR REPLACE FUNCTION AdvokatuSkaiciusBylai()
RETURNS TRIGGER AS $$
DECLARE
    advokatu_skaicius INTEGER;
BEGIN
    SELECT COUNT(*) INTO advokatu_skaicius
    FROM name.Advokatai
    WHERE Bylos_ID = NEW.Bylos_ID;

    IF advokatu_skaicius >= 5 THEN
        RAISE EXCEPTION 'Negalima priskirti daugiau nei penkiu advokatu vienai bylai';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER AdvokatuSkaiciusBylai
BEFORE INSERT OR UPDATE
ON name.Advokatai
FOR EACH ROW
EXECUTE FUNCTION AdvokatuSkaiciusBylai();

CREATE VIEW name.View_Teisiamuju_Su_Byla AS
SELECT t.Asmens_Kodas, t.Vardas, t.Pavarde, t.Tel_Nr, t.El_Pastas, b.ID AS Bylos_ID, b.Pavadinimas AS Bylos_Pavadinimas
FROM name.Teisiamasis t
JOIN name.Teisiamieji tb ON t.Asmens_Kodas = tb.Teisiamojo_Kodas
JOIN name.Byla b ON tb.Bylos_ID = b.ID;

CREATE VIEW name.View_Advokatu_Su_Byla AS
SELECT a.Advokato_Kodas, a.Vardas, a.Pavarde, a.Tel_Nr, a.El_Pastas, ba.Bylos_ID, b.Pavadinimas AS Bylos_Pavadinimas
FROM name.Advokatas a
JOIN name.Advokatai ba ON a.Advokato_Kodas = ba.Advokato_Kodas
JOIN name.Byla b ON ba.Bylos_ID = b.ID;

CREATE MATERIALIZED VIEW name.MatView_Teisiamuju_Advokatu_Statistika AS
SELECT
    t.Asmens_Kodas AS Teisiamojo_Kodas,
    t.Vardas AS Teisiamojo_Vardas,
    t.Pavarde AS Teisiamojo_Pavarde,
    COUNT(DISTINCT tb.Bylos_ID) AS Bendras_Bylu_Skaicius,
    COUNT(DISTINCT ba.Advokato_Kodas) AS Bendras_Advokatu_Skaicius
FROM
    name.Teisiamasis t
LEFT JOIN
    name.Teisiamieji tb ON t.Asmens_Kodas = tb.Teisiamojo_Kodas
LEFT JOIN
    name.Advokatai ba ON tb.Bylos_ID = ba.Bylos_ID
GROUP BY
    t.Asmens_Kodas, t.Vardas, t.Pavarde;

REFRESH MATERIALIZED VIEW name.MatView_Teisiamuju_Advokatu_Statistika;