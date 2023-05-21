package DN11;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * Kraj razred z imeno, kratico
 *
 */
class Kraj {
    private String ime;
    private String kratica;
    private List<Vlak> odhodi;

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

    public void dodajVlak(Vlak vlak) {
        odhodi.add(vlak);
    }

    public String toString() {
        return String.format("%s (%s)", ime, kratica);
    }
}

abstract class Vlak {
    private String idVlak;
    private Kraj zacetniKraj;
    private Kraj koncniKraj;
    private int trajanjeVoznje;

    public Vlak(String idVlak, Kraj zacetniKraj, Kraj koncniKraj, int trajanjeVoznje) {
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

    public int getTrajanjeVoznje() {
        return trajanjeVoznje;
    }

    public abstract String opis();

    public abstract double cenaVoznje();

    public String toString() {
        return String.format(
                "Vlak %s (%s) %s -- %s (%s) (%d min, %.2f EUR)",
                idVlak, opis(), zacetniKraj.getIme(), koncniKraj.getIme(), zacetniKraj.getKratica(),
                getTrajanjeVoznje(), cenaVoznje()
        );
    }
}

class RegionalniVlak extends Vlak {
    private int hitrost;
    private double cenaNaKm;

    public RegionalniVlak(String oznaka, Kraj zacetek, Kraj konec, int trajanje) {
        super(oznaka, zacetek, konec, trajanje);
        this.hitrost = 50;
        this.cenaNaKm = 0.068;
    }

    @Override
    public String opis() {
        return "regionalni";
    }

    @Override
    public double cenaVoznje() {
        double razdalja = (double) (hitrost * super.getTrajanjeVoznje()) / 60;
        double cena = razdalja * this.cenaNaKm;
        return cena;
    }
}

class EkspresniVlak extends Vlak {
    private int hitrost;
    private double cenaNaKm;
    private double dopl;

    public EkspresniVlak(String oznaka, Kraj zacetek, Kraj konec, int trajanje, double dopl) {
        super(oznaka, zacetek, konec, trajanje);
        this.hitrost = 110;
        this.cenaNaKm = 0.154;
        this.dopl = dopl;
    }

    @Override
    public String opis() {
        return "ekspresni";
    }

    @Override
    public double cenaVoznje() {
        double razdalja = (double) (hitrost * super.getTrajanjeVoznje()) / 60;
        double cena = razdalja * cenaNaKm + dopl;
        return cena;
    }
}

class EuroRail {
    private List<Kraj> kraji;
    private List<Vlak> vlaki;

    public EuroRail() {
        kraji = new ArrayList<>();
        vlaki = new ArrayList<>();
    }

    public void dodajKraj(Kraj kraj) {
        kraji.add(kraj);
    }

    public void dodajVlak(Vlak vlak) {
        vlaki.add(vlak);
    }

    public List<Kraj> getKraji() {
        return kraji;
    }

    public List<Vlak> getVlaki() {
        return vlaki;
    }

    public boolean preberiKraje(String imeDatoteke) {
        try (BufferedReader br = new BufferedReader(new FileReader(imeDatoteke))) {
            String vrstica;
            while ((vrstica = br.readLine()) != null) {
                String[] podatki = vrstica.split(";");

                if (podatki.length < 2) {
                    // Vrstica ne vsebuje imena kraja in kode države, preskočimo jo
                    continue;
                }

                String imeKraja = podatki[0];
                String kodaDrzave = podatki[1];

                Kraj kraj = new Kraj(imeKraja, kodaDrzave);
                kraji.add(kraj);
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void izpisiKraje() {
        System.out.println("Kraji, povezani z vlaki:");
        for (Kraj kraj : kraji) {
            System.out.println(kraj);
        }
    }

    public boolean preberiPovezave(String imeDatoteke) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(imeDatoteke));
            String vrstica;
            while ((vrstica = br.readLine()) != null) {
                String[] podatki = vrstica.split(";");
                if (podatki.length >= 4) {
                    String oznakaVlaka = podatki[0].trim();
                    String zacetniKraj = podatki[1].trim();
                    String koncniKraj = podatki[2].trim();
                    String trajanje = podatki[3].trim();
                    if (krajObstaja(zacetniKraj) && krajObstaja(koncniKraj) && !zacetniKraj.equals(koncniKraj)) {
                        int ure = 0;
                        int minute = 0;
                        if (trajanje.contains(".")) {
                            String[] cas = trajanje.split("\\.");
                            if (cas.length >= 2) {
                                ure = Integer.parseInt(cas[0]);
                                minute = Integer.parseInt(cas[1]);
                            }
                        } else {
                            minute = Integer.parseInt(trajanje);
                        }

                        if (podatki.length == 5) {
                            double doplacak = Double.parseDouble(podatki[4].trim());
                            EkspresniVlak vlak = new EkspresniVlak(oznakaVlaka, najdiKraj(zacetniKraj), najdiKraj(koncniKraj), ure, minute);
                            dodajVlak(vlak);
                        } else {
                            RegionalniVlak vlak = new RegionalniVlak(oznakaVlaka, najdiKraj(zacetniKraj), najdiKraj(koncniKraj), Integer.parseInt(trajanje));
                            dodajVlak(vlak);
                        }
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

    private boolean krajObstaja(String imeKraja) {
        for (Kraj kraj : kraji) {
            if (kraj.getIme().equals(imeKraja)) {
                return true;
            }
        }
        return false;
    }

    private Kraj najdiKraj(String imeKraja) {
        for (Kraj kraj : kraji) {
            if (kraj.getIme().equals(imeKraja)) {
                return kraj;
            }
        }
        return null;
    }

    public void izpisiPovezave() {
        System.out.println("Vlaki, ki povezujejo kraje:");
        for (Vlak vlak : vlaki) {
            System.out.println(vlak.toString());
        }
    }
}

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

        EuroRail euroRail = new EuroRail();

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
        } else {
            System.out.println("Napaka: Neveljavna naloga.");
        }
    }
}