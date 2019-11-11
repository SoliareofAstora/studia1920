public abstract class HistogramBase {
    protected int[][] neighbours;
    protected boolean[][] occupancy;

    /**
     * Metoda ustawia tablice sąsiedztw. Tablica może zawierać wyłącznie liczby
     * całkowite niemniejsze od 0.
     *
     * @param neighbours tablica sąsiedztw. Wskazuje, które położenia traktowane są
     *                   jako sąsiednie oraz jaki jest rząd tego sąsiedztwa.
     */
    public void setNeighboursTable(int[][] neighbours) {
        this.neighbours = neighbours;
    }

    /**
     * Metoda ustawia tablice informującą o zajętych/wolnych położeniach.
     *
     * @param occupancy tablica zawierająca informację o zajętych pozycjach. false -
     *                  położenie wolne, true - pozycja zajęta.
     */
    public void setOccupancyTable(boolean[][] occupancy) {
        this.occupancy = occupancy;
    }

    /**
     * Metoda wyznacza i zwraca histogram ilości par zajętych pozycji w tablicy
     * occupancy, które są dla siebie n-tymi sąsiadami wg. tablicy neighbours.
     *
     *
     * @return histogram liczba par sąsiadów danego rządu
     */
    public abstract int[] getHistogram();

    /**
     * Metoda wyznacza i zwraca liczbę tych zajętych pozycji w tablicy occupancy,
     * które nie mają żadnego sąsiada dowolnego rzędu (nie występują w żadnej parze
     * wliczanej do histogramu).
     *
     * @return liczba pozycji tablicy occupancy, które nie posiadają żadnego
     * sąsiada wg. tablicy neighbours.
     */
    public abstract int noNeighbours();

}