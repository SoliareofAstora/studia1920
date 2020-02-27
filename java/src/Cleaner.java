import javax.accessibility.AccessibleKeyBinding;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class Start{
    static public void main(String[] args){
        Cleaner cl = new Cleaner();
        cl.removeMain("/home/SoliareofAstora/studia1920/java/old/Histogram.java");
    }
}
public class Cleaner implements CleanerInterface{

    /**
     * Metoda usuwa z pliku źródłowego java o nazwie filename metodę main. Metoda używa wyłącznie
     * pliku o podanej nazwie. Jest on najpierw odczytywany a następnie zapisywany. Metoda
     * nie tworzy (nawet tymczasowo) innych plików.
     *
     * @param filename plik do przetworzenia.
     */

    boolean checkPrevious(String[] arr, int i){
        int[] check = new int[10];

        for (int j = i; j > i-7; j--) {
            if (arr[j].equals("public")){
                check[0]++;
            }
            if (arr[j].equals("static")){
                check[1]++;
            }
            if (arr[j].equals("void")){
                check[2]++;
            }
            if (arr[j].equals("final")){
                check[3]++;
            }
            if (arr[j].equals("synchronized")){
                check[4]++;
            }
            if (arr[j].equals("staticfp")){
                check[5]++;
            }
        }
        for (var c: check
             ) {
            if (c>1)return false;
        }
        if (check[0]+check[1]+check[2]==3)
            return true;
        return false;
    }
    private enum main_type {
        normal,
        dots,
        square,
        finals
    }
    @Override
    public void removeMain(String filename){
        try {
            var txt = new String(Files.readAllBytes(Path.of(filename))).split(" ");

            for (int i = 0; i < 20; i++) {
                System.out.println(txt[i]);
                main_type mt = main_type.normal;
                int main_position = -1;
                if (txt[i].equals("main(int") | txt[i].equals("main(String")) {
                    System.out.println("Found normal main");
                    main_position = i;
                    mt = main_type.normal;
                }
                if (txt[i].equals("main(int...args){") | txt[i].equals("main(String...args){")) {
                    System.out.println("Found ... main");
                    main_position = i;
                    mt = main_type.dots;
                }
                if (txt[i].equals("main(int[]") | txt[i].equals("main(String[]")) {
                    System.out.println("Found [] main");
                    main_position = i;
                    mt = main_type.square;
                }
                if (txt[i].equals("main(final")) {
                    System.out.println("Found final main");
                    main_position = i;
                    mt = main_type.finals;
                }
                if (main_position > 3) {
                    if (checkPrevious(txt, main_position)) {
                        System.out.println("We have proper main here");
                        switch (mt) {

                            case normal:
                                if (txt[i+1].equals("[]args){"))
                                break;
                            case dots:
                                break;
                            case square:
                                break;
                            case finals:
                                break;
                        }
                    }
                }
            }

        }catch (Exception e){
            System.out.println(e);
        }
    }
}
/*    final
    main(int
    main(String
                 main(final String

    public static void main(String[] args)

    static public void main(String[] args)

    public static void main(String []args)

    public static void main(String args[])

    public static void main(String...args)

    public static void main(final String[] args)

    public final static void main(String[] args)

    public synchronized static void main(String[] args)

    public strictfp static void main(String[] args)

    final static synchronized strictfp static void main(String[] args)

    public static void main(int[] args)


*/





























