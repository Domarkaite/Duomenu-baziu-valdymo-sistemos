import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Front {

    public int id;
    public String vardas;
    public String pavarde;
    public long kodas;
    public long kodas2;
    public String telNr;
    public String elPastas;
    public String pavadinimas;
    public String statusas;
    public Date data;


    private DataAccess db;
    BufferedReader bufRead = new BufferedReader(new InputStreamReader(System.in));
    private Scanner in;

    public Front() throws SQLException, ClassNotFoundException {
        db = new DataAccess();
        in = new Scanner(System.in);
    }

    public void start() {

        while (true) {
            try {
                printMenu();
                int input;
                while (true) {
                    try {
                        input = Integer.parseInt(bufRead.readLine());
                        break;
                    } catch (Exception e) {
                        System.out.println("Pasirinkimas privalo buti skaicius, iveskite dar karta:");
                    }
                }
                switch (input) {
                    case 0:
                        System.exit(0);
                        break;
                    case 1:
                        addByla();
                        break;
                    case 2:
                        deletePosedis();
                        break;
                    case 3:
                        showPosedziai();
                        break;
                    case 4:
                        addAdvokatas();
                        break;
                    case 5:
                        changeAdvokatasBylai();
                        break;
                    case 6:
                        deleteAndUpdateAdvokatasByloms();
                        break;
                    case 7:
                        showAdvokataiIrBylos();
                        break;
                    case 8:
                        addAdvokatasBylai();
                        break;
                    case 9:
                        findTeisiamiejiFromByla();
                        break;
                    default:
                        System.out.println("Blogas pasirinkimas");
                        break;
                }
            } catch (IOException e) {
                System.out.println("Klaida skaitant ivesti");
            } catch (NumberFormatException e) {
                System.out.println("Netinkamas ivesties formatas");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void printMenu(){
        System.out.println(" 0 -> Baigti programa");
        System.out.println(" 1 -> Ivesti nauja byla");                              // insert + trigeris
        System.out.println(" 2 -> Pasalinti posedi");                               // delete
        System.out.println(" 3 -> Rodyti posedzius");                               // select
        System.out.println(" 4 -> Prideti nauja advokata");                         // insert
        System.out.println(" 5 -> Pakeisti advokata byloje kitu advokatu");         // update
        System.out.println(" 6 -> Pasalinti advokata"); // transaction delete + update
        System.out.println(" 7 -> Rodyti advokatus ir bylas, kuriose jie dalyvauja");         // select + 2 tables
        System.out.println(" 8 -> Prideti advokata prie bylos");      //insert + trigeris
        System.out.println(" 9 -> Perziureti bylos teisiamuosius");   // find
    }

    public void addByla() {
        System.out.println("Naujos bylos duomenys");
        try {
            System.out.println("Iveskite naujos bylos ID");
            while (true) {
                try {
                    id = Integer.parseInt(bufRead.readLine());
                    break;
                } catch (Exception e) {
                    System.out.println("Bylos ID privalo buti skaicius, iveskite dar karta:");
                }
            }
            System.out.println("Iveskite naujos bylos pavadinima");
            pavadinimas = bufRead.readLine();
            System.out.println("Iveskite naujos bylos statusa: (Nepradeta, Pradeta, Sustabdyta, Nutraukta, Pabaigta)");
            while (true) {
                try {
                    statusas = bufRead.readLine();
                    String[] validStatuses = {"Nepradeta", "Pradeta", "Sustabdyta", "Nutraukta", "Pabaigta"};
                    boolean isValid = false;
                    for (String validStatus : validStatuses) {
                        if (validStatus.equalsIgnoreCase(statusas)) {
                            isValid = true;
                            break;
                        }
                    }
                    if (isValid) {
                        break;
                    } else {
                        System.out.println("Invalid status entered. Please try again.");
                    }
                } catch (Exception e) {
                    System.out.println("Statusas netinkamas");
                }
            }
            System.out.println("Iveskite naujos bylos pradzios data");
            while (true) {
                try {
                    data = Date.valueOf(bufRead.readLine());
                    break;
                } catch (Exception e) {
                    System.out.println("Bylos data privalo buti data, iveskite dar karta:");
                }
            }
            db.queryDb("INSERT INTO name.Byla VALUES('"+id+"','"+pavadinimas+"', '"+statusas+"', '"+data+"');");
            System.out.println("Byla sekmingai prideta");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void deletePosedis() throws Exception {
        List<List> result = new LinkedList<List>();
        System.out.println("Posedziai");
        result = db.queryDb("SELECT * FROM name.Posedis;");
        printPosedziaiTable(result);
        try {
            System.out.println("Iveskite norimo pasalinti posedzio ID");
            while (true) {
                try {
                    id = Integer.parseInt(bufRead.readLine());
                    break;
                } catch (Exception e) {
                    System.out.println("Bylos ID privalo buti skaicius, iveskite dar karta:");
                }
            }
            db.queryDb("DELETE FROM name.Posedis WHERE ID = " + id + ";");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void showPosedziai() {
        List<List> result = new LinkedList<List>();
        try {
            result = db.queryDb("SELECT * FROM name.Posedis;");
            printPosedziaiTable(result);
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    public void addAdvokatas() {
        System.out.println("Naujo advokato duomenys");
        try {
            System.out.println("Iveskite naujo advokato koda");
            while (true) {
                try {
                    kodas = Long.parseLong(bufRead.readLine());
                    break;
                } catch (Exception e) {
                    System.out.println("Advokato kodas privalo buti skaicius, iveskite dar karta:");
                }
            }
            System.out.println("Iveskite naujo advokato varda");
            vardas = bufRead.readLine();
            System.out.println("Iveskite naujo advokato pavarde");
            pavarde = bufRead.readLine();
            System.out.println("Iveskite naujo advokato telefono numeri");
            while (true) {
                try {
                    telNr = bufRead.readLine();
                    if (telNr.startsWith("+370")) {
                        break;
                    } else {
                        System.out.println("Invalid telephone number format. Please ensure it starts with +370.");
                    }
                } catch (Exception e) {
                    System.out.println("Telefono nr. netinkamas, iveskite dar karta:");
                }
            }
            System.out.println("Iveskite naujo advokato elektroninio pasto adresa");
            while (true) {
                try {
                    elPastas = bufRead.readLine();
                    String regex = "^(.+)@(.+)$";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(elPastas);
                    if (matcher.matches())
                    {
                        break;
                    }
                    else
                    {
                        System.out.println("Tai nera validus el pastas");
                    }
                } catch (Exception e) {
                    System.out.println("El pastas sudarytas is characteriu");
                }
            }

            db.queryDb("INSERT INTO name.Advokatas VALUES('"+kodas+"','"+vardas+"', '"+pavarde+"', '"+telNr+"', '"+elPastas+"');");
            System.out.println("Advokatas sekmingai pridetas");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void changeAdvokatasBylai() {
        List<List> result = new LinkedList<List>();
        try {
            result = db.queryDb("SELECT * FROM name.Byla;");
            printBylaTable(result);
            System.out.println("Kokios bylos advokata noretumete keisti?");
            while (true) {
                try {
                    id = Integer.parseInt(bufRead.readLine());
                    break;
                } catch (Exception e) {
                    System.out.println("Bylos ID privalo buti skaicius, iveskite dar karta:");
                }
            }
            System.out.println("Koki bylos advokata noretumete keisti?");
            System.out.println("Pavaizduoti jusu nurodytos bylos advokatai");
            result = db.queryDb("SELECT A.Advokato_Kodas, A.Vardas, A.Pavarde, A.Tel_Nr, A.El_Pastas FROM name.Advokatas A JOIN name.Advokatai AD ON A.Advokato_Kodas = AD.Advokato_Kodas WHERE AD.Bylos_ID = "+ id + ";");
            printAdvokatasTable(result);
            while (true) {
                try {
                    kodas = Long.parseLong(bufRead.readLine());
                    break;
                } catch (Exception e) {
                    System.out.println("Advokato kodas privalo buti skaicius, iveskite dar karta:");
                }
            }
            result = db.queryDb("SELECT * FROM name.Advokatas;");

            printAdvokatasTable(result);
            System.out.println("Iveskite naujo advokato koda");
            while (true) {
                try {
                    kodas2 = Long.parseLong(bufRead.readLine());
                    break;
                } catch (Exception e) {
                    System.out.println("Bylos ID privalo buti skaicius, iveskite dar karta:");
                }
            }

            db.queryDb("UPDATE name.Advokatai set Advokato_Kodas = " + kodas2 + "Where Bylos_ID = " + "'"+ id + "'" + "AND Advokato_Kodas = '" + kodas + "';");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void deleteAndUpdateAdvokatasByloms() {
        List<List> result = new LinkedList<List>();
        try {
            result = db.queryDb("SELECT * FROM name.Advokatas;");
            printAdvokatasTable(result);
            System.out.println("Iveskite salintino advokato koda");
            while (true) {
                try {
                    kodas = Long.parseLong(bufRead.readLine());
                    break;
                } catch (Exception e) {
                    System.out.println("Advokato kodas privalo buti skaicius, iveskite dar karta:");
                }
            }
            System.out.println("Iveskite pakeisianti advokato koda");
            while (true) {
                try {
                    kodas2 = Long.parseLong(bufRead.readLine());
                    break;
                } catch (Exception e) {
                    System.out.println("Advokato kodas privalo buti skaicius, iveskite dar karta:");
                }
            }
            db.deleteUpdate(kodas, kodas2);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void showAdvokataiIrBylos() {
        List<List> result = new LinkedList<List>();
        try {
            result = db.queryDb("SELECT A.Advokato_Kodas, A.Vardas, A.Pavarde, A.Tel_Nr, A.El_Pastas, B.ID AS Bylos_ID, B.Pavadinimas, B.Statusas, B.Pradzia FROM name.Advokatas A JOIN name.Advokatai AB ON A.Advokato_Kodas = AB.Advokato_Kodas JOIN name.Byla B ON AB.Bylos_ID = B.ID;");
            printAdvokataiBylosTable(result);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void printAdvokataiBylosTable(List<List> combinedList) {
        System.out.printf("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------%n");
        System.out.printf("| %-15s | %-15s | %-15s | %-15s | %-32s | %-10s | %-40s | %-12s | %-10s |%n",
                "Adv_Kodas", "Vardas", "Pavarde", "Tel_Nr", "El_Pastas", "ID", "Pavadinimas", "Statusas", "Pradzia");
        System.out.printf("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------%n");

        for (List row : combinedList) {
            System.out.printf("| %-15s | %-15s | %-15s | %-15s | %-32s | %-10s | %-40s | %-12s | %-10s |%n",
                    row.get(0), row.get(1), row.get(2),
                    row.get(3), row.get(4),
                    row.get(5), row.get(6), row.get(7), row.get(8));
        }
        System.out.printf("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------%n");
    }

    public void addAdvokatasBylai() {
        List<List> result = new LinkedList<List>();
        try {
            result = db.queryDb("SELECT * FROM name.Byla;");
            printBylaTable(result);
            System.out.println("Iveskite bylos ID i kuria norite prideti nauja advokata");
            while (true) {
                try {
                    id = Integer.parseInt(bufRead.readLine());
                    break;
                } catch (Exception e) {
                    System.out.println("Bylos ID privalo buti skaicius, iveskite dar karta:");
                }
            }
            result = db.queryDb("SELECT * FROM name.Advokatas;");
            printAdvokatasTable(result);
            System.out.println("Iveskite norimo prideti advokato koda");
            while (true) {
                try {
                    kodas = Long.parseLong(bufRead.readLine());
                    break;
                } catch (Exception e) {
                    System.out.println("Bylos ID privalo buti skaicius, iveskite dar karta:");
                }
            }
            db.queryDb("INSERT INTO name.Advokatai VALUES("+ id+ ", '"+ kodas + "');");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void findTeisiamiejiFromByla() {
        List<List> result = new LinkedList<List>();
        try {
            result = db.queryDb("SELECT * FROM name.Byla;");
            printBylaTable(result);
            System.out.println("Iveskite bylos ID");
            while (true) {
                try {
                    id = Integer.parseInt(bufRead.readLine());
                    break;
                } catch (Exception e) {
                    System.out.println("Bylos ID privalo buti skaicius, iveskite dar karta:");
                }
            }
            result = db.queryDb("SELECT T.Asmens_Kodas, T.Vardas, T.Pavarde, T.Tel_Nr, T.El_Pastas FROM name.Teisiamasis T JOIN name.Teisiamieji TM ON T.Asmens_Kodas = TM.Teisiamojo_Kodas WHERE TM.Bylos_ID = "+id+";");
            printTeisiamasisTable(result);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void printTeisiamasisTable(List<List> teisiamasisList) {
        System.out.printf("--------------------------------------------------------------------------------------------%n");
        System.out.printf("| %-15s | %-15s | %-15s | %-15s | %-32s |%n",
                "Asmens_Kodas", "Vardas", "Pavarde", "Tel_Nr", "El_Pastas");
        System.out.printf("--------------------------------------------------------------------------------------------%n");

        for (List row : teisiamasisList) {
            System.out.printf("| %-15s | %-15s | %-15s | %-15s | %-32s |%n",
                    row.get(0), row.get(1), row.get(2),
                    row.get(3), row.get(4));
        }
        System.out.printf("--------------------------------------------------------------------------------------------%n");
    }

    public void printPosedziaiTable(List<List> posedisList) {
        System.out.printf("-----------------------------------------------------------------%n");
        System.out.printf("| %-15s | %-15s | %-20s | %-15s | %n", "ID", "Sale", "Laikas", "Bylos_kodas");
        System.out.printf("-----------------------------------------------------------------%n");

        for (List row : posedisList) {
            System.out.printf("| %-15s | %-15s | %-20s | %-15s | %n",
                    row.get(0), row.get(1), row.get(2), row.get(3));
        }
        System.out.printf("-----------------------------------------------------------------%n");
    }

    public void printBylaTable(List<List> bylaList) {
        System.out.printf("--------------------------------------------------------------------%n");
        System.out.printf("| %-10s | %-40s | %-12s | %-10s |%n", "ID", "Pavadinimas", "Statusas", "Pradzia");
        System.out.printf("--------------------------------------------------------------------%n");

        for (List row : bylaList) {
            System.out.printf("| %-10s | %-40s | %-12s | %-10s |%n",
                    row.get(0), row.get(1), row.get(2), row.get(3));
        }
        System.out.printf("--------------------------------------------------------------------%n");
    }

    public void printAdvokatasTable(List<List> advokatasList) {
        System.out.printf("------------------------------------------------------------------------------------------------------------%n");
        System.out.printf("| %-15s | %-15s | %-15s | %-15s | %-32s |%n",
                "Advokato_Kodas", "Vardas", "Pavarde", "Tel_Nr", "El_Pastas");
        System.out.printf("------------------------------------------------------------------------------------------------------------%n");

        for (List row : advokatasList) {
            System.out.printf("| %-15s | %-15s | %-15s | %-15s | %-32s |%n",
                    row.get(0), row.get(1), row.get(2), row.get(3), row.get(4));
        }
        System.out.printf("------------------------------------------------------------------------------------------------------------%n");
    }

}