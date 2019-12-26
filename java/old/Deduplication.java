import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

//Słownik słów jest początkowo pusty {}
//
//Ala ma kota i psa -> Ciąg 1
//    Słownik: {}
//
//Ala ma słonia i Ala ma psa. -> Ciąg 2
//    Słownik {1->Ala}
//      Ciąg 1: #1 ma kota i psa.
//      Ciąg 2: #1 ma słonia i #1 ma psa.
//
//Ala bardzo lubi psa. -> Ciąg 3
//    Słownik {1->Ala, 2->psa}
//      Ciąg 1: #1 ma kota i #2.
//      Ciąg 2: #1 ma słonia i #1 ma #2.
//      Ciąg 3: #1 bardzo lubi #2.
class Start {
    public static void main(String[] args) {

        var dd = new Deduplication();
        dd.addString("Ala ma kota i psa");
        dd.addString("Ala ma słonia i Ala ma psa");
        System.out.println(dd.getDictionary());
        System.out.println(dd.getString(1));
        System.out.println(dd.getString(2));
        dd.addString("Ala @bardzo lubi psa");
        System.out.println(dd.getDictionary());
        System.out.println(dd.getString(1));
        System.out.println(dd.getString(2));
        System.out.println(dd.getString(3));

        dd.addString("Ala bardzo ^lubi ^lubi ^lubi ^bardzo &bardzo psa");
        System.out.println(dd.getDictionary());
        System.out.println(dd.getString(1));
        System.out.println(dd.getString(2));
        System.out.println(dd.getString(3));
        System.out.println(dd.getString(4));

        var dict = dd.getDictionary();
        var str = dd.getString(2);
        System.out.println(dd.decode(dict,str));
    }
}


public class Deduplication implements DeduplicationInterface {
    List<String> strings = new LinkedList<>();
    Map<String, Integer> counter = new HashMap<String, Integer>();
    Map<Integer, String> dictionary = new HashMap<>();

    private void updateDictionary(){
        for (String key: counter.keySet()) {
            if (counter.get(key) >= 3) {
                if (!dictionary.containsValue(key)){
                    System.out.println("Nowe słowo w słowniku "+key);
                    dictionary.put(dictionary.size()+1, key);
                    for (int i = 0; i < strings.size(); i++) {
                        strings.set(i,encodeString(i));
                    }
                }
            }
        }
    }
    public String encodeString(int index){
        String encodedString = strings.get(index);
        for (int key: dictionary.keySet()) {
            encodedString = encodedString.replaceAll(dictionary.get(key),"#"+key);
        }
        return encodedString;
    }
    public String encodeString(String encodedString){
        for (int key: dictionary.keySet()) {
            encodedString = encodedString.replaceAll(dictionary.get(key),"#"+key);
        }
        return encodedString;
    }

    @Override
    public Map<Integer, String> getDictionary() {
        return dictionary;
    }

    @Override
    public String decode(Map<Integer, String> dictionary, String toDecode) {
        for (int key: dictionary.keySet()) {
            toDecode = toDecode.replaceAll("#"+key,dictionary.get(key));
        }
        return toDecode;
    }

    @Override
    public int addString(String newString) {
        var words = newString.split(" ");
        for (var a:words ) {
            String word;
            Matcher p = Pattern.compile("[1-9]").matcher(a);
            if (p.find()){
                continue;
            }
            p = Pattern.compile("[a-zA-ZżźćńółęąśŻŹĆĄŚĘŁÓŃ%^]+").matcher(a);
            if(p.find())
            {
                word = p.group(0);
                if (word.length()>=3){
                    if (!p.find())
                    {
                        int count = counter.containsKey(word) ? counter.get(word) : 0;
                        counter.put(word, count+1);
                    }
                }
            }
        }

        updateDictionary();
        strings.add(encodeString(newString));
        return strings.size();
    }

    @Override
    public String getString(int id) {
        return strings.get(id-1);
    }
}
