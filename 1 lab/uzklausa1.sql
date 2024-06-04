SELECT DISTINCT grazinti 
FROM Stud.Egzempliorius 
WHERE EXTRACT(MONTH FROM grazinti) = 10
AND grazinti is not null; 