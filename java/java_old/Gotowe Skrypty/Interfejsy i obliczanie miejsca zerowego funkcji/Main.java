interface Fun {
    double  f(double x);
}

class Kwadratowa implements Fun{
    public double f(double x) {
        return (x * x )-9;
    }
}

class Liniowa implements Fun{
    public double f(double x) {
        return x/2 +2 ;
    }
}

class Sześcienna implements Fun {
    public double f(double x) {
        return (x * x * x) - 8;
    }
}

public class Main {

    public static double tolerance = 0.000001;

    public static double Bisection(Fun F, final double Left, final double Right)
    {
        double left =Left;
        double right=Right;
        double x=0;

        while (Math.abs(right - left) > tolerance) //odległość pomiędzy granicami przedziału jest mniejsza od tolerancji
        {
            x = (left + right) / 2;

            if (F.f(x) == 0) {
                System.out.println("\nDokładna odpowiedź! x = " + x);
                return 0;
            }
            if (F.f(left) * F.f(x) > 0) {
                //wartości funkcji są po tej samej stronie osi x
                left=x;
            } else {
                //wartości fukncji są po przeciwnych stronach osi x
                right = x;
            }

        }
        double dx = right - left;
        System.out.println("\nOszacowane miejsce zerowe: " + x);
        System.out.println("Szacowany błąd: " + dx);
        return  0;
    }

    public static void main(String[] args)
    {

        if (args.length !=3)
        {
            System.out.println("Zła ilość argumentów");
            return;
        }
        Fun Function;
        try
        {
            Function = (Fun) Class.forName(args[0]).newInstance();
        }
        catch (Exception e)
        {
            System.out.println("\n"+args[0]+"\nProblem ze znalezieniem nazwy funkcji. \nProszę wybrać spośród \n-Liniowa\n-Kwadratowa\n-Sześcienna ");
            return;
        }
        Bisection(Function, Double.parseDouble(args[1]), Double.parseDouble(args[2]));

    }
}