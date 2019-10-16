import java.util.Scanner;

class Start {

    public static void main(String[] args) {

        String input;
        Scanner in = new Scanner(System.in);

        System.out.println(" Co wpiszesz to sie pokaze w nastepnej linijce. Sam enter konczy zabawe");
        do {
            System.out.print( ">" );
            input = in.nextLine();
            System.out.println( "> >" + input ) ;
        } while ( ! input.equals ("") );
        in.close();


    }
}
