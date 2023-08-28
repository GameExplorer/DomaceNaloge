package DN05;

/**
 * Program iz podanih argumentov sestavi niz ter prešteje kolikokrat se v tem nizu pojavi posamezna števka
 * Ob koncu naj program izpiše najbolj pogosto uporabljeno(e) števko(e) ter frekvenco uporabe
 * Pri izpisu naj se upošteva tudi primer, ko v nizu ni nobene števke
 */
public class DN05_1 {
    public static void main(String[] args) {
        args = new String[]{"asda5 1414149 as1241rt", "0qwasd", "212"};

        String niz = "";
        for(int i = 0; i < args.length; i++) {
            niz += args[i] + " ";
        }

        niz = niz.trim(); // odrežemo presledke na koncu niza

        int stStevk = 0; //koliko stevk se je pojavilo v nizu

        //tabela stevke, ki bo stela pojavitve posamezne števke. stevke[0] steje 0,...
        int[] stevke = new int[10]; //na začetku so samo 0 notri

        //sprehodim se po nizu in zabeležim pojavitve števk v tabeli stevke
        for(int i = 0; i < niz.length(); i++) {
            if(niz.charAt(i) >= '0' && niz.charAt(i) <= '9') {
                stStevk++;
                stevke[niz.charAt(i) - '0']++;
            }
        }

        if(stStevk > 0) { //pojavila se je vsaj ena števka
             int max = 0;

             //poiščem maksimalno število pojavitev katere od števk
            for(int i = 0; i < stevke.length; i++) {
                if(stevke[i] > max) max = stevke[i];
            }

            //izpišem vse števke, ki se pojavijo max-krat
            System.out.printf("'%s' -> ", niz);
            for(int i = 0; i < stevke.length; i++) {
                if(stevke[i] == max) {
                    System.out.printf("%c ", (char)(i+'0'));
                }
            }

            System.out.printf("(%d)%n", max);
        }
        else { //če ni bilo števk
            System.out.printf("V nizu %s ni števk %n", niz);
        }
    }
}
