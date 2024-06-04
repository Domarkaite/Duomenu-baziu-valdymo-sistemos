SELECT k.isbn,
              k.pavadinimas,
              COUNT(DISTINCT a.vardas || ' ' || a.pavarde) AS "Autoriu skaicius",
	      COUNT(DISTINCT CASE WHEN paimta <= CURRENT_DATE 
	      AND grazinti >= CURRENT_DATE then e.nr end) AS "Egzemplioriu skaicius",
              k.leidykla
FROM Stud.Knyga k
LEFT JOIN Stud.Autorius a ON k.isbn = a.isbn
LEFT JOIN Stud.Egzempliorius e ON k.isbn = e.isbn
GROUP BY k.isbn, k.pavadinimas