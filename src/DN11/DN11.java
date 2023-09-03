package DN11;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Kraj razred
 * Predstavlja eno mesto v železniški ponudbi. Vsebuje String ime kraja, String kratico (dvočrkovna kratica) ter seznam
 * odhodov vlakov v sosednje kraje
 */
class Kraj {
    private final String ime;
    private final String kratica;
    private final List<Vlak> odhodi;

    public Kraj(String ime, String kratica) {
        this.ime = ime;
        this.kratica = kratica;
        this.odhodi = new ArrayList<>();
    }

    public String getIme() {
        return ime;
    }

    public String getKratica() {
        return kratica;
    }

    public List<Vlak> getOdhodi() {
        return odhodi;
    }

    public boolean dodajOdhod(Vlak vlak) {
        if (odhodi.contains(vlak)) {
            return false;
        } else {
            odhodi.add(vlak);
            return true;
        }
    }
    public void dodajVlak(Vlak vlak) {
        odhodi.add(vlak);
    }

    public String toString() {
        return String.format("%s (%s)", ime, kratica);
    }
}

/**
 * Razred Vlak
 * Predstavlja vlake iz poletne ponudbe, ki povezujejo dva kraja. Vsebuje oznako vlaka(idVlak), zacetniKraj, koncniKraj
 * in trajanjeVoznje.
 * Vsebuje abstraktni metodi String opis() in double cenaVoznje(). Opis vrne niz ali gre za regionalni ali za ekspresni
 * vlak, cenaVoznje pa izračuna in vrne končno ceno vozovnice.
 */
abstract class Vlak {
    private final String idVlak;
    private final Kraj zacetniKraj;
    private final Kraj koncniKraj;
    private final double trajanjeVoznje;


    public Vlak(String idVlak, Kraj zacetniKraj, Kraj koncniKraj, double trajanjeVoznje) {
        this.idVlak = idVlak;
        this.zacetniKraj = zacetniKraj;
        this.koncniKraj = koncniKraj;
        this.trajanjeVoznje = trajanjeVoznje;
        zacetniKraj.dodajVlak(this);
    }


    public String getIdVlak() {
        return idVlak;
    }

    public Kraj getZacetniKraj() {
        return zacetniKraj;
    }

    public Kraj getKoncniKraj() {
        return koncniKraj;
    }

    public double getTrajanjeVoznje() {
        return trajanjeVoznje;
    }

    public abstract String opis();

    public abstract double cenaVoznje();

    public String trajanjeVoznje() {
        if(getTrajanjeVoznje() > 60) {
            int ure = (int) (getTrajanjeVoznje() / 60);
            int minute = (int) (getTrajanjeVoznje() % 60);

            if(9 < minute) return String.format("%d.%dh", ure, minute);
            else return String.format("%d.0%dh", ure,minute);
        }
        else return Math.round(getTrajanjeVoznje()) + "min";
    }

    public String toString() {
        /*return String.format(
                "Vlak " + idVlak + " (" + opis() + ") " + zacetniKraj + " -- " + koncniKraj + " (" + trajanjeVoznje() + ", %.2f EUR)",
                cenaVoznje());*/
        return String.format(
                "Vlak %s (%s) %s -- %s (%s, %.2f EUR)",
                idVlak, opis(), zacetniKraj, koncniKraj, trajanjeVoznje(), cenaVoznje()
        );
    }
}

/**
 * Razred RegionalniVlak
 * Regionalni vlaki vozijo s povp hitrostjo 50km/h.
 * Cena na km je 0.068
 * metoda cenaVoznje() vrne izračunano ceno vozovnice
 */
class RegionalniVlak extends Vlak {
    private static final double HITROST = 50;
    private static final double CenaNaKm = 0.068;

    public RegionalniVlak(String oznaka, Kraj zacetek, Kraj konec, double trajanje) {
        super(oznaka, zacetek, konec, (int) trajanje);
    }

    @Override
    public String opis() {
        return "regionalni";
    }

    @Override
    public double cenaVoznje() {
        double cena = (HITROST * super.getTrajanjeVoznje()) / 60;
        return cena * CenaNaKm;
    }
}

/**
 * Razred EkspresniVlak
 * Regionalni vlaki vozijo s povp hitrostjo 50km/h.
 * Cena na km je 0.068
 * metoda cenaVoznje() vrne izračunano ceno vozovnice
 */
class EkspresniVlak extends Vlak {
    private static final int HITROST = 110;
    private static final double CenaNaKm = 0.154;
    private final double dopl;

    public EkspresniVlak(String oznaka, Kraj zacetek, Kraj konec, double trajanje, double dopl) {
        super(oznaka, zacetek, konec, (int) trajanje);
        this.dopl = dopl;
    }

    public double getDopl() {
        return dopl;
    }

    @Override
    public String opis() {
        return "ekspresni";
    }

    @Override
    public double cenaVoznje() {
        return HITROST * (getTrajanjeVoznje() / 60) * CenaNaKm + dopl;
    }
}

/**
 * Razred EuroRail
 * Predstavlja celotno železniško omrežje. Vsebuje zbirko vseh krajev in zbirko vseh vlakov.
 * metoda preberiKraje(String ime datoteke) prebere podatke o krajih ter jih zapiše v zbirko
 * metoda preberiPovezave(String ime datoteke) prebere podatke o vlakih, ter jih zapiše v zbirko vlakov
 * metoda izpisiKraje() izpiše vse prebrane kraje
 * metoda izpisiPovezave() izpiše vse prebrane vlake in podatke o trajanju vožnje in o ceni
 */
class EuroRail {
    private final List<Kraj1> kraji;
    private final List<Vlak1> vlaki;

    public EuroRail() {
        kraji = new ArrayList<>();
        vlaki = new ArrayList<>();
    }

    public void dodajKraj(Kraj1 kraj) {
        kraji.add(kraj);
    }

    public void dodajVlak(Vlak1 vlak) {
        vlaki.add(vlak);
    }

    public List<Kraj1> getKraji() {
        return kraji;
    }

    public List<Vlak1> getVlaki() {
        return vlaki;
    }

    public boolean preberiKraje(String imeDatoteke) {
        try (BufferedReader br = new BufferedReader(new FileReader(imeDatoteke))) {
            String vrstica;
            while ((vrstica = br.readLine()) != null) {
                String[] podatki = vrstica.split(";");

                if (podatki.length == 2) {
                    String imeKraja = podatki[0].trim();
                    String kodaDrzave = podatki[1].trim();

                    boolean krajObstaja = false;

                    for(Kraj1 kraj : kraji) {
                        if(kraj.getIme().equalsIgnoreCase(imeKraja)) {
                            krajObstaja = true;
                            break;
                        }
                    }
                    if(!krajObstaja) {
                        Kraj1 kraj = new Kraj1(imeKraja, kodaDrzave);
                        kraji.add(kraj);
                    }
                }
            }

            br.close();
            return true; //Uspešno branje
        } catch (IOException e) {
            e.printStackTrace();
            return false; //Napaka pri branju
        }
    }

    public void izpisiKraje() {
        System.out.println("Kraji, povezani z vlaki:");
        for (Kraj1 kraj : kraji) {
            System.out.println(kraj);
        }
    }

    public boolean preberiPovezave(String imeDatoteke) {
        try (BufferedReader br = new BufferedReader(new FileReader(imeDatoteke)))
        {
            String vrstica;
            while ((vrstica = br.readLine()) != null) {
                String[] temptab;
                int temp;
                String[] podatki = vrstica.split(";");
                if(podatki.length >= 4) {
                    String oznakaVlaka = podatki[0].trim();
                    String zacetniKraj = podatki[1].trim();
                    String koncniKraj = podatki[2].trim();

                    if(podatki[3].contains(".")) {
                        temptab  = podatki[3].split("\\.");
                        temp = Integer.parseInt(temptab[0]) * 60 + Integer.parseInt(temptab[1]);
                    }
                    else temp = Integer.parseInt(podatki[3]);

                    Kraj1 zacetniKrajIme = null;
                    Kraj1 koncniKrajIme = null;

                    for(Kraj1 kraj : kraji) {
                        if(kraj.getIme().equalsIgnoreCase(zacetniKraj)) zacetniKrajIme = kraj;
                        else if(kraj.getIme().equalsIgnoreCase(koncniKraj)) koncniKrajIme = kraj;
                        if(zacetniKrajIme != null && koncniKrajIme != null) break;
                    }

                    if(zacetniKrajIme != null && koncniKrajIme != null) {
                        int trajanjeVoznje = temp;

                        Vlak1 vlak;

                        if(podatki.length == 5) {
                            double doplacanZnesek = Double.parseDouble(podatki[4].trim());
                            vlak = new EkspresniVlak1(oznakaVlaka, zacetniKrajIme, koncniKrajIme, trajanjeVoznje, doplacanZnesek);
                        }
                        else {
                            vlak = new RegionalniVlak1(oznakaVlaka, zacetniKrajIme, koncniKrajIme, trajanjeVoznje);
                        }
                        vlaki.add(vlak);

                    }
                }

            }
            br.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void izpisiPovezave() {
        System.out.println();
        System.out.println("Vlaki, ki povezujejo kraje:");
        for (Vlak1 vlak : vlaki) {
            System.out.println(vlak.toString());
        }
    }


}

/**
 * Metoda main izvede ustrezen del programa. V primeru da pokličemo nalogo, ki ne obstaja izpiše sporočilo o napaki
 */
public class DN11 {
    public static void main(String[] args) {
        //EuroRail euroRail;

        //        Kraj ljubljana = new Kraj("Ljubljana", "SI");
        //        Kraj maribor = new Kraj("Maribor", "SI");
        //        Kraj celje = new Kraj("Celje", "SI");
        //
        //        euroRail.dodajKraj(ljubljana);
        //        euroRail.dodajKraj(maribor);
        //        euroRail.dodajKraj(celje);
        //
        //        Vlak vlak1 = new RegionalniVlak("RG123", ljubljana, maribor, 120);
        //        Vlak vlak2 = new EkspresniVlak("EK456", ljubljana, celje, 90, 5.0);
        //
        //        euroRail.dodajVlak(vlak1);
        //        euroRail.dodajVlak(vlak2);

        // Preverjanje podatkov

        //        for (Kraj kraj : euroRail.getKraji()) {
        //            System.out.println(kraj);
        //        }
        //
        //        System.out.println();
        //
        //        System.out.println("Seznam vlakov:");
        //        for (Vlak vlak : euroRail.getVlaki()) {
        //            System.out.println(vlak);
        //        }

        //        boolean uspesnoBranje = euroRail.preberiKraje("src/DN11/kraji.txt");
        //        if (uspesnoBranje) {
        //            System.out.println("Kraji so bili uspešno prebrani.");
        //        } else {
        //            System.out.println("Prišlo je do napake pri branju krajev.");
        //        }

        //        euroRail = new EuroRail();
        //        boolean uspesnoBranjeKrajev = euroRail.preberiKraje("src/DN11/kraji.txt");
        //        boolean uspesnoBranjePovezav = euroRail.preberiPovezave("src/DN11/povezave.txt");
        //        if (uspesnoBranjeKrajev && uspesnoBranjePovezav) {
        //            euroRail.izpisiKraje();
        //            euroRail.izpisiPovezave();
        //        } else {
        //            System.out.println("Prišlo je do napake pri branju krajev in/ali povezav.");
        //        }

        int naloga = Integer.parseInt(args[0]);
        String krajiDatoteka = args[1];
        String povezaveDatoteka = args[2];

        EuroRail1 euroRail = new EuroRail1();

        boolean uspesnoBranjeKrajev;
        boolean uspesnoBranjePovezav;

        if (naloga == 1) {
            uspesnoBranjeKrajev = euroRail.preberiKraje(krajiDatoteka);
            uspesnoBranjePovezav = euroRail.preberiPovezave(povezaveDatoteka);

            if (uspesnoBranjeKrajev && uspesnoBranjePovezav) {
                euroRail.izpisiKraje();
                euroRail.izpisiPovezave();
            } else {
                System.out.println("Prišlo je do napake pri branju krajev in/ali povezav.");
            }
        }
        else {
            System.out.println("Napaka: Neveljavna naloga.");
        }
    }
}