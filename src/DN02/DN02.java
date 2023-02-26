package DN02;

public class DN02 {
    public static void main(String[] args) {
        //args = new String[] {"jesen zima pomlad poletje"};
        String zvezda = "*";
        if(args.length == 0) System.out.println("Napaka pri uporabi programa!");

        else {
            //izpis obrobe v prvi vrstici
            for (int i = 0; i < args.length; i++) {

                    System.out.print(zvezda.repeat(args[i].length()+ 2));

            }

            System.out.println();

            System.out.print("* ");
            //* besede * izpis besedila
            for(int i = 0; i < args.length; i++) {
                    System.out.print(args[i]+ " ");
            }
            System.out.print("* ");

            System.out.println();

            //izpis obrobe v zadnje vrstice
            for (int i = 0; i < args.length; i++) {
                    System.out.print(zvezda.repeat(args[i].length()+ 2));

            }
        }
    }
}
