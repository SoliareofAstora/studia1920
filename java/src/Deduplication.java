import java.util.Map;

class Start {
    public static void main(String[] args) {
        var dd = new Deduplication();
        System.out.print("dziaka");
    }
}


public class Deduplication implements DeduplicationInterface {

    @Override
    public Map<Integer, String> getDictionary() {
        return null;
    }

    @Override
    public String decode(Map<Integer, String> dictionary, String toDecode) {
        return null;
    }

    @Override
    public int addString(String newString) {
        return 0;
    }

    @Override
    public String getString(int id) {
        return null;
    }
}
