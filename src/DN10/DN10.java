package DN10;

import java.util.TreeSet;

/**
 * Poišče najdaljši podniz, v vseh argumentih programa
 */
public class DN10 {

    /**
     * Metoda vrne mnozico vseh podnizov danega niza
     * @param niz niz, ki ga prejme (argument)
     * @return - vrnemo množico podnizov
     */
    public static TreeSet<String> getVsiPodnizi(String niz) {
        TreeSet<String> rezultat = new TreeSet<>(); //prazen treeset za shranjevanje

        //Sprehod po nizu
        for(int i = 1; i <= niz.length(); i++) { //dolzina niza
            for(int j = 0; j < niz.length() - i + 1; j++) { //premik po vhodnem nizu
                rezultat.add(niz.substring(j, j+i)); //izrežemo ustrezen niz in ga dodamo rezultatu
            }
        }

        return rezultat;
    }

    public static void main(String[] args) {
        args = new String[]{"abc dasda", "abc"};

        if(args.length < 1) System.out.println("Dodaj vsaj en argument");

        TreeSet<String> besede = getVsiPodnizi(args[0]);
        //Sprehod po argumentu
        for(int i = 0; i < args.length; i++) {
            besede.retainAll(getVsiPodnizi(args[i])); //vse podnize dobimo
        }

        String najDaljsiNiz = "";
        for(String b : besede) {
            if(b.length() > najDaljsiNiz.length()) {
                najDaljsiNiz = b;
            }
        }

        if(!najDaljsiNiz.isEmpty()) System.out.println("Najdaljši niz: " + najDaljsiNiz);
        else System.out.println("Ni najdaljšega niza");
    }
}
