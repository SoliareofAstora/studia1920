import java.util.*;
import static java.lang.StrictMath.sqrt;

public class Main
{
    static List list = new ArrayList<Collection<Integer>>();
    static int testSize = 100000;
    //[ 0 - add 1 - remove 2 - contains ][elapsedTime]
    static double tab[][] = new double[3][testSize];
    static Random r = new Random();

    public static void main(String[] args)
    {
        list.add(new ArrayList<>());
        list.add(new HashSet<>());
        list.add(new LinkedList<>());
        list.add(new Stack<>());
        list.add(new Vector<>());
        list.add(new PriorityQueue<>());
        list.add(new TreeSet<>());

        System.out.println("Collection add\u00B1[ns] remove\u00B1[ns] contains\u00B1[ns]");
        for (int i = 0; i < list.size(); i++)
        {
            CalculateStuff((Collection<Integer>) list.get(i));
        }
    }

    static void CalculateStuff(Collection<Integer> T)
    {
        //Beginning of the test
        for (int i = 0; i < testSize; i++)
        {
            int rand = r.nextInt();
            double start = System.nanoTime();
            T.add(rand);
            double elapsedTime = System.nanoTime() - start;
            tab[0][i] = elapsedTime;
        }

        for (int i = 0; i < testSize; i++)
        {
            int rand = r.nextInt();
            double start = System.nanoTime();
            T.contains(rand);
            double elapsedTime = System.nanoTime() - start;
            tab[2][i] = elapsedTime;
        }
        for (int i = 0; i < testSize; i++)
        {
            int rand = r.nextInt();
            double start = System.nanoTime();
            T.remove(rand);
            double elapsedTime = System.nanoTime() - start;
            tab[1][i] = elapsedTime;
        }
        //End of a test


        //Average value & standard deviation calculation
        String output = T.getClass().getSimpleName();
        for (int x = 0; x < 3; x++)
        {
            double sum = 0;
            double sq_sum = 0;
            for (int i = 0; i < testSize; i++)
            {
                sum += tab[x][i];
                sq_sum += tab[x][i] * tab[x][i];
            }
            double mean = sum / testSize;
            double variance = sq_sum / testSize - mean * mean;
            output += "  " + (int)sum / testSize + "/\u00B1" + (int)sqrt(variance);
        }
        System.out.println(output);
    }
}