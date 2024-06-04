WITH EgzemplioriuSkaicius(isbn, Skaicius)
	AS (SELECT isbn, COUNT(ISBN)
         FROM Stud.Egzempliorius 
         GROUP BY isbn),
     Daugiausiai(Daugiausiai)
     AS (SELECT Max(Skaicius) FROM EgzemplioriuSkaicius)
SELECT Pavadinimas as "Knygos pavadinimas"
FROM  EgzemplioriuSkaicius B, Daugiausiai, Stud.Knyga A
WHERE A.isbn = B.isbn AND Skaicius = Daugiausiai
ORDER BY Pavadinimas;