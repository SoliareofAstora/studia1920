import java.util.*;

class Start {

    public static void main(String[] args) {

        Destruction dstr = new Destruction();
        Set<String> vertices = new HashSet<String>();
        vertices.addAll(Arrays.asList(new String[]{"A", "B", "C", "D", "E", "F", "G"}));
        dstr.allObjects(vertices);

        Set<String> egde = new HashSet<String>();
        egde.add("C");
        dstr.addDependence("A", egde);

        egde = new HashSet<String>();
        egde.add("B");
        egde.add("E");
        dstr.addDependence("C", egde);

        egde = new HashSet<String>();
        egde.add("F");
        egde.add("G");
        dstr.addDependence("E", egde);

        egde = new HashSet<String>();
        egde.add("D");
        egde.add("E");
        dstr.addDependence("B", egde);

        egde = new HashSet<String>();
        egde.add("E");
        dstr.addDependence("D", egde);

        for (String a:vertices) {
            System.out.println(dstr.allObjectsDestroyedBy(a));
        }

        System.out.println(dstr.allSourcesOfDestruction("E"));
        System.out.println(dstr.destructionPath("B", "F"));
        System.out.println(dstr.destructionPath("E","B"));

        Map<String, Integer> whatever = dstr.sourceStatistics();
        for (String key: whatever.keySet()
             ) {
            System.out.println(key+ whatever.get(key));
        }
        whatever = dstr.destructionStatistics();
        for (String key: whatever.keySet()
        ) {
            System.out.println(key+ whatever.get(key));
        }
    }
}

public class Destruction implements DestructionInterface {

    Map<String,Set<String>> graph = new HashMap<String, Set<String>>();
    Map<String,Set<String>> reversedGraph = new HashMap<String, Set<String>>();
    @Override
    public void allObjects(Set<String> objects) {
        for (String a: objects) {
            graph.put(a, new HashSet<String>());
            reversedGraph.put(a, new HashSet<String>());
        }
    }

    @Override
    public void addDependence(String source, Set<String> dependentSet) {
        for (String a: dependentSet) {
            graph.get(source).add(a);
            reversedGraph.get(a).add(source);
        }
    }

    @Override
    public Set<String> allObjectsDestroyedBy(String source) {
        Set<String> output = new HashSet<String>();
        if (graph.get(source).size()==0)
            return null;
        else
            for(String a: graph.get(source)){
                output.addAll(destructionLoop(graph,source,a));
            }
            return output;
    }

    private Set<String> destructionLoop(Map<String,Set<String>> map, String source, String current){
        Set<String> output = new HashSet<String>();
        if (source == current)
            return null;
        else
            output.add(current);
            if(map.get(source).size()!=0) {
                for (String a : map.get(current)) {
                    output.addAll(destructionLoop(map, current, a));
                }
            }
            return output;
    }

    @Override
    public Set<String> allSourcesOfDestruction(String object) {
        Set<String> output = new HashSet<String>();
        if (reversedGraph.get(object).size()==0)
            return null;
        else
            for(String a: reversedGraph.get(object)){
                output.addAll(destructionLoop(reversedGraph,object,a));
            }
        return output;
    }

    private List<String> pathFinding(Set<String> visited, String current, String destination){
        List<String> output = new LinkedList<String>();
        if (current == destination){
            output.add(destination);
            return output;
        }
        else
            if(graph.get(current).size()!=0) {
                for (String a : graph.get(current)) {
                    if (visited.contains(a)){
                        continue;
                    }
                    visited.add(a);
                    List<String> tmp = pathFinding(visited, a, destination);

                    if (tmp != null) {
                        output.add(a);
                        output.addAll(tmp);
                        return output;
                    }
                }
            }
            return null;
    }

    @Override
    public List<String> destructionPath(String source, String destroyedObject) {
        Set<String> visited = new HashSet<String>();
        visited.add(source);
        List<String> output = new LinkedList<String>();
        output.add(source);
        List<String> tmp = pathFinding(visited, source, destroyedObject);

        if (tmp != null) {
            output.addAll(tmp);
            output.remove(output.size()-1);
            return output;
        }
        return null;
    }

    @Override
    public Map<String, Integer> sourceStatistics() {
        Map<String, Integer> output = new HashMap<String, Integer>();
        for (String key: graph.keySet()
             ) {
            Set<String> tmp = allObjectsDestroyedBy(key);
            if (tmp == null)
                output.put(key, 0);
            else
                output.put(key, tmp.size());
        }
        return output;
    }

    @Override
    public Map<String, Integer> destructionStatistics() {
        Map<String, Integer> output = new HashMap<String, Integer>();
        for (String key: graph.keySet()
        ) {
            Set<String> tmp = allSourcesOfDestruction(key);
            if (tmp == null)
                output.put(key, 0);
            else
                output.put(key, tmp.size());
        }
        return output;
    }
}
