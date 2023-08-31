package DN07;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DN07_1 {

    public static void izpisiDatotekeZDetajli(File f){
        File[] imena = f.listFiles();

        for (int i = 0; i < imena.length; i++) {
            String mapa = imena[i].isDirectory() ? "Mapa" : "Datoteka";
            System.out.printf("%20s%20s%10.3f\n", imena[i].getName(),mapa,imena[i].length()/1000.0);
        }

    }

    public static void izpisiNajvecjo(File f){
        File[] imena = f.listFiles();
        long najvecjaVelikost = 0;
        long najmanjsaVelikost = 999999999;
        File najvecjaDatoteka = null;
        File najmanjsaDatoteka = null;
        for (int i = 0; i < imena.length; i++) {
            if (!imena[i].isDirectory()){
                if(imena[i].length() > najvecjaVelikost){
                    najvecjaVelikost = imena[i].length();
                    najvecjaDatoteka=imena[i];
                }
                if(imena[i].length() < najmanjsaVelikost){
                    najmanjsaVelikost = imena[i].length();
                    najmanjsaDatoteka = imena[i];
                }
            }
        }
        System.out.println(najvecjaDatoteka.getName() + " " + najvecjaVelikost/1000.0);
        System.out.println(najmanjsaDatoteka.getName() + " " + najmanjsaVelikost/1000.0);
    }

    public static void izpisiVsebineDatotek(File f, int dolzina) throws FileNotFoundException {
        File[] imena = f.listFiles();

        for (int i = 0; i < imena.length; i++) {
            if (!imena[i].isDirectory()){ //če ni direktorij
                if(imena[i].getName().endsWith(".txt")){ //če se konča na .txt
                    System.out.println(imena[i].getName()); //izpiši ime
                    Scanner sc = new Scanner(imena[i]);
                    for(int j = 0; j < dolzina; j++){ //sprehod po vsebini in izpis njene vsebine
                        if (sc.hasNextLine()) {
                            System.out.print("    ");
                            System.out.println(sc.nextLine());
                        }
                    }
                }
                else {
                    System.out.println(imena[i].getName() + " (ni tekstovna datoteka)");
                }
            }
        }
    }

    public static void kopirajDatoteko(String vhodnaDatoteka, String izhodnaDatoteka) throws IOException {
        File vhod = new File(vhodnaDatoteka);
        Scanner sc = new Scanner(vhod);
        File izhod = new File(izhodnaDatoteka);
        if (izhod.length() > 0){
            System.out.println("Napaka pri kopiranju, datoteka že vsebuje besedilo");
            return;
        }
        FileWriter fw = new FileWriter(izhod);
        while (sc.hasNext()){
            String vrstica = sc.nextLine();
            fw.write(vrstica);
            fw.write("\n");
        }
        fw.close();
        sc.close();
    }


    public static void zdruziDatoteke(File direktorij, String outFilename) throws IOException {
        String text = "";
        File[] files = direktorij.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory()){
                continue;
            }
            if (!f.getName().endsWith(".txt")){
                continue;
            }
            Scanner sc = new Scanner(f);
            while(sc.hasNextLine()){
                text += sc.nextLine() + "\n";
            }
            sc.close();
        }
        System.out.println(text);
        File f = new File(outFilename);
        FileWriter fw = new FileWriter(f);
        fw.write(text);
        fw.close();
    }



    public static String findInFile(File f, String toFind) throws FileNotFoundException {
        Scanner sc = new Scanner(f);
        int lineIndex = 1;
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            if(line.contains(toFind)){
                System.out.println(f.getName() + " " + lineIndex + ": " + line);
                lineIndex++;
            }
        }
        return null;
    }
    public static void grep(String location, String toFind) throws FileNotFoundException {
        String[] results;
        File f = new File(location);
        File[] imena = f.listFiles();
        int num_results = 0;
        results = new String[imena.length];
        for (int i = 0; i < imena.length; i++) {
            if (!imena[i].isDirectory()) {
                findInFile(imena[i], toFind);
            }
        }
    }

    public static void izpisiRekurzivno(File f, int zamik){
        File[] imena = f.listFiles();
        for (int j = 0; j < zamik; j++) {
            System.out.print(" ");
        }
        System.out.println("/"+f.getName());
        for (int i = 0; i < imena.length; i++) {
            if (imena[i].isDirectory()) {
                izpisiRekurzivno(imena[i], zamik + 4);
            } else {
                for (int j = 0; j < zamik+4; j++) {
                    System.out.print(" ");
                }
                System.out.println(imena[i].getName());
            }
        }
    }
    public static File[] izpisiNajdaljse(File f, int n){
        File[] imena = f.listFiles();

        long[] najdaljseVelikosti = new long[n];
        File[] najdaljseDatoteke = new File[n];
        for (int i = 0; i < imena.length; i++) {
            if (imena[i].isDirectory()){
                File[] rezultat_poddirektorija = izpisiNajdaljse(imena[i], n);
                for(int subdir_i = 0; subdir_i< rezultat_poddirektorija.length;subdir_i++) {
                    if(rezultat_poddirektorija[subdir_i] == null){
                        continue;
                    }
                    for (int j = n - 1; j >= 0; j--) {
                        if (rezultat_poddirektorija[subdir_i].length() > najdaljseVelikosti[j]) {
                            for (int k = 1; k <= j; k++) {
                                najdaljseDatoteke[k - 1] = najdaljseDatoteke[k];
                                najdaljseVelikosti[k - 1] = najdaljseVelikosti[k];
                            }
                            najdaljseDatoteke[j] = rezultat_poddirektorija[subdir_i];
                            najdaljseVelikosti[j] = rezultat_poddirektorija[subdir_i].length();
                            break;

                        }
                    }
                }
            } else {
                for (int j = n - 1; j >= 0; j--) {
                    if (imena[i].length() > najdaljseVelikosti[j]) {
                        // premakni tastare navzdol
                        for (int k = 1; k <= j; k++) {
                            najdaljseDatoteke[k - 1] = najdaljseDatoteke[k];
                            najdaljseVelikosti[k - 1] = najdaljseVelikosti[k];
                        }
                        najdaljseDatoteke[j] = imena[i];
                        najdaljseVelikosti[j] = imena[i].length();
                        // System.out.println("Added2 " + imena[i]);
                        break;

                    }
                }
            }
        }

        return najdaljseDatoteke;
    }


    public static int resiIzraz(String s){
        int number = 0;
        if (!Character.isDigit(s.charAt(0))){
            return Integer.MIN_VALUE;
        } else {
            number = Character.getNumericValue(s.charAt(0));
        }
        boolean needOperator = true;
        for(int i = 1; i<s.length(); i++){
            if (needOperator){
                if (!(s.charAt(i) == '+' || s.charAt(i) == '-')){
                    return Integer.MIN_VALUE;
                } else {
                    needOperator = false;
                }
            } else {
                if(!Character.isDigit(s.charAt(i))){
                    return Integer.MIN_VALUE;
                }
                char operator = s.charAt(i-1);
                if(operator == '+'){
                    number += Character.getNumericValue(s.charAt(i));
                    needOperator = true;
                } else if (operator == '-') {
                    number -= Character.getNumericValue(s.charAt(i));
                    needOperator = true;
                } else {
                    return Integer.MIN_VALUE;
                }
            }
        }
        return number;
    }

    public static void izpisiMatIzraze(File f) throws FileNotFoundException {
        File imena[] = f.listFiles();
        for (int i = 0; i < imena.length; i++) {
            if (!imena[i].isDirectory()) {
                System.out.println(imena[i].getName());
                Scanner sc = new Scanner(imena[i]);
                while(sc.hasNextLine()){
                    String line = sc.nextLine();
                    if(line.length() > 0){
                        int vsota = resiIzraz(line);
                        if (vsota != Integer.MIN_VALUE){
                            System.out.println("  "+ line + "=" + vsota);
                        }

                    }
                }
            }
        }
    }

    //Vrni veljavne in neveljavne programe
    public static void main(String args[]) throws IOException {
        args = new String[]{"1", "src/DN07", "3"};
        int naloga = 8;
        String filename = args[1];
        File f = new File(filename);

        if (naloga == 1){
            izpisiDatotekeZDetajli(f);
        }
        if(naloga == 2){
            izpisiNajvecjo(f);
        }
        if(naloga == 3){
            izpisiVsebineDatotek(f, Integer.parseInt(args[2]));
        }
        if (naloga == 4){
            kopirajDatoteko(args[2], args[3]);
        }
        if(naloga == 5){
            zdruziDatoteke(f, args[2]);
        }
        if(naloga == 6){
            grep(f.getAbsolutePath(), args[2]);
        }
        if(naloga == 7){
            izpisiRekurzivno(f, 0);
        }
        if(naloga==8){
            izpisiMatIzraze(f);
        }
        if(naloga==9){
            int n = Integer.parseInt(args[2]);
            File[] najdaljseDatoteke = izpisiNajdaljse(f, n);
            for (int i = n-1; i >= 0; i--) {
                if(najdaljseDatoteke[i] != null) {
                    System.out.println(najdaljseDatoteke[i].getName() + " " + najdaljseDatoteke[i].length());
                }
            }
        }

    }
}


