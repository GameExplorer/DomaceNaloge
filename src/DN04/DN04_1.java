package DN04;

/**
 * Prvi del programa vsebuje metodi za kodiranje dvojiskiZapisZnaka() in
 * dvojiskiZapisSporocila() - te dve metodi sta za generiranje dvojiskih sporocil (vhod).
 * Drugi del programa vsebuje metodi odkodirajZnakI() in asciiSporocilo(), ki ju potrebujem za rešitev
 * domače naloge
 */
public class DN04_1 {

    /**** KODIRANJE ****/

    /**
     * Kodiranje enega znaka. A -> 01000001
     * @param znak
     * @return
     */
    public static String dvojskiZapisZnaka(char znak) {
        int koda = znak;

        //Za pretvorbo v binarno obliko uporabim metodo Integer.toString()
        String binarno = Integer.toString(koda, 2);

        //Če je niz krajši od 8 znakov, dodam ničle na začetek
        while(binarno.length() < 8) binarno = "0" + binarno;

        return binarno;
    }

    /**
     * Celotno sporocilo pretvorim v dvojiski zapis
     * za pretvorbo posameznega znaka uporabim metodo dvojiskiZapisZnaka()
     * @param niz
     * @return
     */
    public static String dvojiskiZapisSporocila(String niz) {
        String rezultat = "";
        for(int i = 0; i < niz.length(); i++) {
            rezultat += dvojskiZapisZnaka(niz.charAt(i));
        }

        return rezultat;
    }

    /***** DEKODIRANJE *****/

    /**
     * Pretvorba enega znaka iz dvojiskega v ASCII zapis 01000001 -> 'A'
     * @param crka
     * @return
     */
    public static char odKodirajZnak(String crka) {
        return (char) Integer.parseInt(crka, 2);
    }

    public static String asciiSporocilo(String dvojiskoSporocilo) {
        //prvotno sporocilo (odkirano)
        String ascii = "";

        //dokler dvojiskoSporocilo se vsebuje znake (potrebujem vsaj 8 ničel in enic da lahko dekodiram)
        while(dvojiskoSporocilo.length() > 7) {
            //V crko shranim prvih osem znakov dvojiskega sporocila
            String crka = dvojiskoSporocilo.substring(0, 8);
            //dvojiskemu sporocilu odrežem prvih 8 znakov
            dvojiskoSporocilo = dvojiskoSporocilo.substring(8);

            //iz znak crka pretvorim v ASCIII obliko
            ascii += odKodirajZnak(crka);
        }

        return ascii;
    }
    public static void main(String[] args) {
        args = new String[] {"01000001110110110101"};

        if(args.length < 1) {
            System.out.println("Napaka - program potrebuje vsaj en argument");
        }
        else {
            System.out.println(asciiSporocilo(args[0]));
        }
    }
}
