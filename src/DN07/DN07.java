package DN07;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.Scanner;

public class DN07 {

    /**
     *
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        //File dat = new File(args[1]);

        File f_tmp = new File("src/DN07/");
        int tmp = 2;
        //int st = Integer.parseInt(args[0]);

        switch (tmp) {
            case 1:
                izpisi_datoteke(f_tmp);
                break;
            case 2:
                najvecja_datoteka(f_tmp);
                break;
            case 3:
                izpis_vsebine(f_tmp, 2);
                break;
            case 4:
                kopiraj_datoteko("DA", "DADA");
                break;
            case 5:
                zdruzi_datoteko(f_tmp, "dat");
                break;
            case 6:
                najdiVDatotekah(f_tmp, "N");
                break;
            case 7:
                drevo(f_tmp);
                break;
            case 8:
                resiMatematicneIzraze(f_tmp);
                break;
            case 9:
                nNajvecjih(f_tmp, 2);
                break;
        }
    }

    /**
     * Metoda izpiše vse datoteke v podanem direktoriju vključno z mapam. Izpiše pa ime datoteke/mape, tip datoteke/mape
     * ter velikost
     * @param f
     */
    public static void izpisi_datoteke(File f) {
        File[] datoteke = f.listFiles();

        for(File datoteka : datoteke) { //sprehod po daotekah
            //Če je datoteka potem izpiše podatke o datoteki, drugače pa podatke o mapi, ker sem iz Gorenjske se izogibam gnezdenju zank :)
            if(datoteka.isFile()) System.out.printf("%20s %19s %10.3f%n", datoteka.getName(),"Datoteka", datoteka.length() / 1000.0);
            else if(datoteka.isDirectory()) System.out.printf("%20s %19s %10.3f%n", datoteka.getName(), "Mapa", datoteka.length() / 1000.0);
        }
    }

    /**
     * Metoda v podanem direktoriju prebere vse datoteke in izpiše največjo in najmanjšo datoteko vred z njunima
     * velikostima.
     * Najprej sem deklariral spremenljivki za največjo in najmanjšo datoteko. Pri najmanjši datoteki sem nastavil velikost
     * na MAX vrednost int-a lahko bi uporabil tudi long in naredil podobno in mi ne bi bilo potrebno uporabiti cast-a.
     * Pa še dve spremenljivki tipa String ki si zapomnita ime največje oz. najmanjše datoteke, ki ju potem uporabimo pri
     * izpisu vsebine.
     * @param f
     */
    public static void najvecja_datoteka(File f) {
        File[] datoteke = f.listFiles();
        int najvecja_datoteka = 0;
        int najmanjsa_datoteka = 2147483647;

        String ime_NajvecjeDatoteke = "";
        String ime_NajmanjseDatoteke = "";

        for(File datoteka : datoteke) { //sprehod po datotekah
            if(datoteka.isFile()) { //if če je datoteka izvede naslednje ...
                if(datoteka.length() > najvecja_datoteka) { //pogleda če je večja od velikosti trenutne datoteke, če je
                    najvecja_datoteka = (int) datoteka.length(); // zapiše podatke o velikosti in imenu datoteke
                    ime_NajvecjeDatoteke = datoteka.getName(); // prva datoteka bo vedno večja od največje datoteke
                }
                if(datoteka.length() < najmanjsa_datoteka) { //na podoben princip kot iskanje največje datoteke
                    najmanjsa_datoteka = (int) datoteka.length(); //ker je vsako št. manjše od MAX int se potem prva datoteka
                    ime_NajmanjseDatoteke = datoteka.getName(); //shrani in potem to počne dokler datoteka.length() ni večja od
                                                                //najmanjše datoteke
                }
            }
        }
        System.out.printf("%s %1.3f%n", ime_NajvecjeDatoteke, (double) najvecja_datoteka / 1000.0); //izpis največje
        System.out.printf("%s %1.3f%n", ime_NajmanjseDatoteke, (double) najmanjsa_datoteka / 1000.0); //in najmanjše dat.
    }

    public static void izpis_vsebine(File f, int n) throws FileNotFoundException {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            if (files != null) {
                for (File file : files) {
                    izpis_vsebine(file, n);
                }
            }
        } else if (f.getName().toLowerCase().endsWith(".txt")) {
            System.out.println(f.getName());
            Scanner scanner = new Scanner(f);
            for (int i = 0; i < n && scanner.hasNextLine(); i++) {
                System.out.println("    " + scanner.nextLine());
            }
            scanner.close();
        } else {
            System.out.println(f.getName() + " (ni tekstovna datoteka)");

        }
    }

    public static void kopiraj_datoteko(String vhodnaDatoteka, String izhodnaDatoteka) {}
    public static void zdruzi_datoteko(File direktorij, String izhodnaDatoteka){}
    public static void najdiVDatotekah(File f, String iskanNiz) {

    }
    public static void drevo(File f){}
    public static void resiMatematicneIzraze(File f){}
    public static void nNajvecjih(File f, int n) {}
}
