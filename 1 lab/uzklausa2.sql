SELECT CONCAT(t1.vardas,' ', t1.pavarde) as "vardas, pavarde", 
             CONCAT(t2.vardas,' ', t2.pavarde) as "vardas, pavarde" 
FROM Stud.Skaitytojas t1, 
           Stud.Skaitytojas t2 
WHERE EXTRACT(MONTH FROM t1.gimimas) = EXTRACT(MONTH FROM t2.gimimas) 
AND (SUBSTRING(t1.pavarde, 2, 1) = SUBSTRING(t1.pavarde, length(t1.pavarde), 1)
OR (SUBSTRING(t2.pavarde, 2, 1)) = SUBSTRING(t2.pavarde, length(t2.pavarde), 1))
AND t1.Nr < t2.Nr;