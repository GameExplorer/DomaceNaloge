package DN11;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class DN11_1 {
    public static void main(String[] args) {
        args = new String[]{"2", "src/DN11/kraji.txt", "src/DN11/povezave.txt"};
        Locale.setDefault(Locale.ENGLISH);
        if (args.length < 3) {
            System.out.println("NAPAKA: podaj argumente programa!");
            System.out.println("UPORABA: <naloga> <datoteka_kraji> <datoteka_povezave> [prestopanja] [kraj]");
            System.exit(0);
        }
        EuroRail1 omrezje = new EuroRail1();
        if (!omrezje.preberiKraje(args[1])) {
            System.out.println("NAPAKA: branje podatkov o krajih.");
            System.exit(0);
        }
        boolean prebraniVlaki = false;
        if (args[0].charAt(0) == '5')
            prebraniVlaki = omrezje.preberiPovezaveBin(args[2]);
        else
            prebraniVlaki = omrezje.preberiPovezave(args[2]);
        if (!prebraniVlaki) {
            System.out.println("NAPAKA: branje podatkov o povezavah.");
            System.exit(0);
        }
        switch (args[0].charAt(0)) {
            case '1':
                // branje podatkov o krajih in o vlakih ter izpis
                omrezje.izpisiKraje();
                System.out.println();
                omrezje.izpisiPovezave();
                break;
            case '2':
                // izpis vseh podatkov o krajih (tudi odhodi vlakov)
                omrezje.izpisiKrajeVsePodatke();
                break;
            case '3':
                // izpis krajev, urejen po abecedi
                omrezje.izpisiKrajePoAbecedi();
                break;
            case '4':
                // izpis krajev, do katerih se lahko pripeljemo iz podanega kraja z največ p prestopanji
                if (args.length < 5) {
                    System.out.println("NAPAKA: premalo podanih argumentov.");
                    System.out.println("UPORABA: 4 datoteka_kraji datoteka_povezave max_prestopanj ime_kraja");
                    System.exit(0);
                }
                int p = Integer.parseInt(args[3]); //prestopanja
                String ime = args[4]; //ime kraja
                int ind = 5;
                while (ind < args.length)
                    ime += " " + args[ind++];
                Kraj1 kraj = omrezje.vrniKraj(ime);
                if (kraj == null) {
                    System.out.printf("NAPAKA: podanega kraja (%s) ni na seznamu krajev.%n", ime);
                    System.exit(0);
                }

                Set<Kraj1> destinacije = kraj.destinacije(p);
                if (destinacije.isEmpty()) {
                    System.out.printf("Iz kraja %s ni nobenih povezav.%n", kraj);
                } else {
                    System.out.printf("Iz kraja %s lahko z max %d prestopanji pridemo do naslednjih krajev:%n", kraj, p);
                    for (Kraj1 k : destinacije)
                        System.out.println(k.toString());
                }
                break;
            case '5':
                // branje vlakov iz binarne datoteke in izpis
                omrezje.izpisiPovezave();
                break;
            default:
                System.out.println("UPORABA: <naloga 1 - 5> <datoteka_kraji> <datoteka_povezave> [prestopanja] [kraj]");
        }
    }
}

/**
 * Kraji hrani podatke o kraju. Imamo še ID spremenljivko ki nam pride prav pri branju
 * binarnih datotek
 */
class Kraj1 implements Comparable<Kraj1> {
    private static int naslednjiID = 1;
    private int id;
    private String ime;
    private String drzava;
    private List<Vlak1> odhodi;

    /**
     * Konstruktor
     */
    public Kraj1(String ime, String drzava) {
        this.id = naslednjiID;
        naslednjiID++;
        this.ime = ime;
        this.drzava = drzava;
        odhodi = new ArrayList<>(); //na začetku je prazen
    }

    public int getId() {
        return id;
    }

    public String getIme() {
        return ime;
    }

    public List<Vlak1> getOdhodi() {
        return odhodi;
    }

    /**
     * podan vlak doda na seznam odhodov iz tega kraja
     * @return - true, če je vlak uspešno dodan
     */
    public boolean dodajOdhod(Vlak1 vlak) {
        if (odhodi.contains(vlak))
            return false;
        odhodi.add(vlak);
        return true;
    }


    /**
     * Izpis podatkov o kraju
     * @return - vrne izpis
     */
    @Override
    public String toString() {
        return String.format("%s (%s)", this.ime, this.drzava);
    }


    public String vsiPodatki(boolean sort) {
        StringBuilder tmp = new StringBuilder(this.toString());
        if (sort)
            Collections.sort(odhodi);
        tmp.append("\nodhodi vlakov (").append(odhodi.size()).append("):\n");
        for (Vlak1 v : odhodi)
            tmp.append(" - ").append(v.toString()).append("\n");
        return tmp.toString();
    }

    /**
     * Poišče množico vseh krajev, ki so dosegljivi za največ k prestopanj
     * @param prestopanja - št prestopanj
     * @return - vrne destinacijo
     */
    public Set<Kraj1> destinacije(int prestopanja) {
        Set<Kraj1> dest = new TreeSet<>();
        dest.add(this);
        while (prestopanja >= 0) {
            Set<Kraj1> tmp = new HashSet<>();
            for (Kraj1 k : dest)
                for (Vlak1 v : k.getOdhodi())
                    tmp.add(v.getCilj());
            dest.addAll(tmp);
            prestopanja--;
        }
        dest.remove(this);
        return dest;
    }

    /**
     * Primerjamo dva kraja
     */
    @Override
    public int compareTo(Kraj1 k) {
        if (this.drzava.equals(k.drzava))
            return this.ime.compareTo(k.ime);
        return this.drzava.compareTo(k.drzava);
    }
}

/**
 * Hrani vse informacije o vlakih
 */
abstract class Vlak1 implements Comparable<Vlak1> {
    private String oznaka;
    private Kraj1 start, cilj;
    private int casVoznje; // čas vožnje v minutah od <start> do <cilj>

    /**
     * Konstruktor
     */
    public Vlak1(String oznaka, Kraj1 start, Kraj1 cilj, int cas) {
        this.oznaka = oznaka;
        this.start = start;
        this.cilj = cilj;
        this.casVoznje = cas;
    }

    public int getCasVoznje() {
        return casVoznje;
    }

    public Kraj1 getCilj() {
        return cilj;
    }

    public abstract String opis();

    public abstract double cenaVoznje();

    /**
     * Metoda za izpis
     */
    @Override
    public String toString() {
        String cas = this.casVoznje < 60 ? String.format("%d min", this.casVoznje) : String.format("%d.%02dh", this.casVoznje / 60, this.casVoznje % 60);
        return String.format("Vlak %s (%s) %s -- %s (%s, %.2f EUR)", oznaka, opis(), start, cilj, cas, cenaVoznje());
    }

    /**
     * Primerjamo ceno voznje
     */
    @Override
    public int compareTo(Vlak1 v) {
        return Double.compare(v.cenaVoznje(), this.cenaVoznje());
    }
}

/**
 * Atributi razreda RegionalniVlak
 */
class RegionalniVlak1 extends Vlak1 {
    private static final int hitrost = 50; // povprečna hitrost regionalnih vlakov je 50 km/h
    private static final double cenaKm = 0.068; // cena v EUR za km razdalje

    public RegionalniVlak1(String oznaka, Kraj1 start, Kraj1 cilj, int cas) {
        super(oznaka, start, cilj, cas);
    }

    @Override
    public String opis() {
        return "regionalni";
    }

    @Override
    public double cenaVoznje() {
        return super.getCasVoznje() / 60.0 * hitrost * cenaKm;
    }
}

/**
 * Atributi razreda EkspresniVlak
 */
class EkspresniVlak1 extends Vlak1 {
    private static final int hitrost = 110; // povprečna hitrost ekspresnih vlakov je 110 km/h
    private static final double cenaKm = 0.154; // cena v EUR za km razdalje
    private double doplacilo;

    public EkspresniVlak1(String oznaka, Kraj1 start, Kraj1 cilj, int cas, double doplacilo) {
        super(oznaka, start, cilj, cas);
        this.doplacilo = doplacilo;
    }

    @Override
    public String opis() {
        return "ekspresni";
    }

    // Regionalni vlaki imajo doplacilo
    @Override
    public double cenaVoznje() {
        return super.getCasVoznje() / 60.0 * hitrost * cenaKm + this.doplacilo;
    }
}

class EuroRail1 {

    // Zbirka vseh krajev in povezav
    private List<Kraj1> kraji = new ArrayList<>();
    private List<Vlak1> povezave = new ArrayList<>();

    public Kraj1 vrniKraj(String ime) {
        for (Kraj1 k : kraji)
            if (ime.equals(k.getIme()))
                return k;
        return null;
    }

    /**
     * Vrne id kraja
     *
     * @param id
     * @return vrne null če kraj id ne obstaja
     */
    private Kraj1 vrniKrajID(int id) {
        for (Kraj1 k : kraji)
            if (k.getId() == id)
                return k;
        return null;
    }


    /**
     * Preberemo kraje iz datoteke kraji.txt. Kraji so ločeni s ; kraje zapiše zbirko krajev
     *
     * @param imeDatoteke - datoteka, ki jo preberemo
     * @return - vrne true če je uspešno prebral datoteko
     */

    public boolean preberiKraje(String imeDatoteke) {
        // try v tem primeru ni nujem lahko bi metodi dodali throws
        try (Scanner sc = new Scanner(new File(imeDatoteke))) { //odpremo datoteko
            while (sc.hasNextLine()) {
                String[] elementi = sc.nextLine().split(";");
                if (elementi.length < 2) // nimamo zadosti podatkov
                    continue;
                if (vrniKraj(elementi[0]) != null) // kraj s tem imenom je že v seznamu
                    continue;
                kraji.add(new Kraj1(elementi[0], elementi[1]));
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Iz podane datoteke prebere podatke o vlakih in jih zapiše v zbirko vlakov. Tukaj moramo biti pozorni za
     * katero vrsto vlaka gre.
     *
     * @param imeDatoteke - datoteka ki jo preberemo
     * @return - vrne true, če je branje uspešno
     */
    public boolean preberiPovezave(String imeDatoteke) {
        try (Scanner sc = new Scanner(new File(imeDatoteke))) {
            while (sc.hasNextLine()) {
                String[] elementi = sc.nextLine().split(";");
                if (elementi.length < 4) //nimamo zadosti podatkov
                    continue;
                Kraj1 k1 = vrniKraj(elementi[1]); //drugi podatek je kraj odhoda
                Kraj1 k2 = vrniKraj(elementi[2]); //tretji podatek je kraj prihoda
                if (k1 == null || k2 == null || k1 == k2)
                    continue; // če enega kraja (ali obeh) ni v seznamu ali če gre za isti kraj, preskoči to vrstico
                int casMinute = 0;
                int ind = elementi[3].indexOf("."); //cena, ki je v double
                if (ind < 0)
                    casMinute = Integer.parseInt(elementi[3]);
                else
                    casMinute = Integer.parseInt(elementi[3].substring(0, ind)) * 60 + Integer.parseInt(elementi[3].substring(ind + 1));
                Vlak1 v;
                if (elementi.length > 4) //če je Ekspresni vlak
                    v = new EkspresniVlak1(elementi[0], k1, k2, casMinute, Double.parseDouble(elementi[4]));
                else //Regionalni vlak
                    v = new RegionalniVlak1(elementi[0], k1, k2, casMinute);
                if (k1.dodajOdhod(v))
                    povezave.add(v);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Metoda prebere binarno datoteko in izpiše informacije o krajih in o vlakih
     *
     * @param imeDatoteke
     * @return
     */
    public boolean preberiPovezaveBin(String imeDatoteke) {
        try (FileInputStream vhod = new FileInputStream(imeDatoteke)) {
            while (vhod.available() > 0) {
                // preberi podatke o oznaki vlaka
                byte[] bajti = new byte[6];
                vhod.read(bajti);
                StringBuilder tmp = new StringBuilder();
                for (int i = 0; i < 6; i++) {
                    tmp.append((char) bajti[i]);
                }
                String oznaka = tmp.toString().trim(); // odstrani presledke
                // preberi id prvega kraja in poišči kraj
                int id = vhod.read();
                Kraj1 k1 = vrniKrajID(id);
                // preberi id drugega kraja in poišči kraj
                id = vhod.read();
                Kraj1 k2 = vrniKrajID(id);
                // preberi čas vožnje (v minutah)
                int casMinute = vhod.read() * 256 + vhod.read();
                // preberi doplačilo (v centih)
                int doplacilo = vhod.read() * 256 + vhod.read();
                // ustvari vlak in ga dodaj na seznam
                if (k1 != null && k2 != null && k1 != k2) {
                    Vlak1 v;
                    if (doplacilo == 0) { // ni doplačila, torej je regionalni vlak
                        v = new RegionalniVlak1(oznaka, k1, k2, casMinute);
                    } else {
                        v = new EkspresniVlak1(oznaka, k1, k2, casMinute, doplacilo / 100.0);
                    }
                    if (k1.dodajOdhod(v))
                        povezave.add(v);
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Izpiše kraje
     */
    public void izpisiKraje() {
        System.out.println("Kraji, povezani z vlaki:");
        for (Kraj1 k : kraji)
            System.out.println(k);
    }

    /**
     * Izpiše vse kraje in vse podatke o odhodu vlakov
     */
    public void izpisiKrajeVsePodatke() {
        System.out.println("Kraji in odhodi vlakov:");
        for (Kraj1 k : kraji)
            System.out.println(k.vsiPodatki(false));
    }

    /**
     * Izpiše kraje po abecedi
     */
    public void izpisiKrajePoAbecedi() {
        System.out.println("Kraji in odhodi vlakov (po abecedi):");
        ArrayList<Kraj1> seznam = new ArrayList<>(kraji);
        Collections.sort(seznam);
        for (Kraj1 k : seznam)
            System.out.println(k.vsiPodatki(true));
    }

    /**
     * Izpis vseh povezav
     */
    public void izpisiPovezave() {
        System.out.println("Vlaki, ki povezujejo kraje:");
        for (Vlak1 v : povezave)
            System.out.println(v);
    }
}

