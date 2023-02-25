package DN02;

public class DN02 {
    public static void main(String[] args) {
        //args = new String[] {"jesen zima pomlad poletje"};

        if(args.length == 0) System.out.println("Napaka pri uporabi programa!");

        else {
            //izpis obrobe v prvi vrstici
            for (int i = 0; i < args.length; i++) {
                for(int j = 0; j < args[i].length()+2; j++) {
                    System.out.print("*");
                }
            }

            System.out.println();

            //* besede * izpis besedila
            System.out.print("*");
            for(int i = 0; i < args.length; i++) {
                System.out.print(" " + args[i]);
            }
            System.out.print(" *");

            System.out.println();

            //izpis obrobe v zadnje vrstice
            for (int i = 0; i < args.length; i++) {
                for(int j = 0; j < args[i].length()+2; j++) System.out.print("*");
            }
        }
    }

}
