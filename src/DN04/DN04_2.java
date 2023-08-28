package DN04;

public class DN04_2 {
    public static void main(String[] args) {
        args = new String[]{"0111011011110100"};
        if(args.length > 0) {
            for(int i = 0; i < args[0].length(); i+= 8) {
                System.out.println((char) Integer.parseInt(args[0].substring(i, 8 + i), 2));
            }
        }
    }
}
