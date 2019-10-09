public class Main {

    public static void main(String[] args) {

        double a = Double.parseDouble(args[0]);
        double n = Double.parseDouble(args[1]);

        sqrt(a, n);
    }

    public static double sqrt(double a, double n) {

        double result = a;
        double prev_result;

        double tmp = pow(result, n - 1);

        do {
            prev_result = result;
            result = 1 / n * ((n - 1) * result + (a / tmp));
            tmp = pow(result, n - 1);
        } while (abs(prev_result - result) > 0.0);

        System.out.println(a + " do potęgi 1/" + (int) n + " = " + result);
        return result;
    }

    public static double abs(double a) {

        return a < 0 ? a * -1 : a;
    }

    public static double pow(double a, double n) {

        double result = 1;
        for (int i = 0; i < n; i++) {
            result *= a;
        }

        //System..println(a + " do potęgi " + n + " = " + result);
        return result;
    }
}
