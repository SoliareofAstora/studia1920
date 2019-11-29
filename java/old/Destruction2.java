import java.util.*;

class Start {

    public static void main(String[] args) {

        Destruction2 dstr = new Destruction2();
        Set<String> vertices = new HashSet<String>();
        vertices.addAll(Arrays.asList(new String[]{"A", "B", "C", "D", "E", "F", "G"}));
        dstr.allObjects(vertices);

        try {
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

            System.out.println(dstr.allDestructionPath("B", "F"));
            System.out.println(dstr.shortestDestructionPath("B", "F"));
//            System.out.println(dstr.allDestructionPath("E","B"));
        }
        catch (Exception e){
            System.out.println("Not working");
        }



/*
        Map<String, Integer> whatever = dstr.sourceStatistics();
        for (String key: whatever.keySet()
             ) {
            System.out.println(key+ whatever.get(key));
        }
        whatever = dstr.destructionStatistics();
        for (String key: whatever.keySet()
        ) {
            System.out.println(key+ whatever.get(key));
        }*/
    }
}

public class Destruction2 implements DestructionInterface2 {

    Map<String,Set<String>> graph = new HashMap<String, Set<String>>();
    @Override
    public void allObjects(Set<String> objects) {
        for (String a: objects) {
            graph.put(a, new HashSet<String>());
        }
    }

    @Override
    public void addDependence(String source, Set<String> dependentSet) throws ObjectUnknownException, LoopException{
        if (!graph.containsKey(source))
            throw new ObjectUnknownException();
        for (String a: dependentSet) {
            if (!graph.containsKey(a))
                throw new ObjectUnknownException();
            graph.get(source).add(a);
        }
        if(checkLoop(graph, source, source))
            throw new LoopException();
    }

    private boolean checkLoop(Map<String,Set<String>> map, String source, String current){
        for(String a: graph.get(current)){
            if (a==source){
                return true;
            }
            if(checkLoop(map, source, a))
                return true;
        }
        return false;
    }

    @Override
    public Set<List<String>> allDestructionPath(String source, String destroyedObject) throws NoPathException, ObjectUnknownException {

        if (!graph.containsKey(source) || !graph.containsKey(destroyedObject))
            throw new ObjectUnknownException();
        Set<List<String>> result =pathFinder(source,destroyedObject);
        if (result==null){
            throw new NoPathException();
        }
        return result;
    }

    public Set<List<String>> pathFinder(String current, String target){
        Set<List<String>> result = new HashSet<List<String>>();
        if (current == target){
            List<String> tmp = new ArrayList<String>();
            tmp.add(current);
            result.add(tmp);
            return result;
        }
        for(String a: graph.get(current)){
            Set<List<String>> temp = pathFinder(a,target);
            if (temp!=null){
                for (List<String> b: temp) {
                    b.add(0,current);
                }
                result.addAll(temp);
            }
        }
        if (result.size()>0)
            return result;
        return null;
    }
    @Override
    public List<String> shortestDestructionPath(String source, String destroyedObject) throws NoPathException, ObjectUnknownException, AmbiguousSolutionsException {
        Set<List<String>> paths = allDestructionPath(source, destroyedObject);
        List<String> result = null;
        int shortest = 999999999;
        for (List<String> path:paths) {
            if (path.size()<shortest)
            {
                shortest = path.size();
                result = path;
            }
        }
        int check = 0;
        for (List<String> path:paths) {
            if (path.size()==shortest)
            {
                check += 1;
            }
        }
        if(check>1) throw new AmbiguousSolutionsException();

        return result;
    }
}
