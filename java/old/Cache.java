import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Cache implements CacheInterface {

    int counter = 0;
    Map<Integer, Long> base_timeout = new HashMap<>();
    Map<Integer, Object> objects = new HashMap<>();
    Map<Integer, Long> current_timeout = new HashMap<>();

    Thread master_thread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LinkedList<Integer> todelete = new LinkedList<Integer>();
                long current = System.currentTimeMillis();
                for (var index : current_timeout.keySet()) {
                    if (current > current_timeout.get(index)) {
                        todelete.add(index);
                    }
                }
                synchronized (objects) {
                    for (var index : todelete) {
                        base_timeout.remove(index);
                        objects.remove(index);
                        current_timeout.remove(index);
                    }
                }

            }
        }
    });

    Cache() {
        master_thread.start();
    }

    void reset_timeout(int index) {
        long current = System.currentTimeMillis();
        current_timeout.put(index, current + base_timeout.get(index));
    }

    /**
     * Metoda dodaje obiekt do systemu Cache. Obiekt otrzymuje <b>unikalny</b>
     * identyfikator. Wraz z obiektem przekazywana jest wartość timeout określająca
     * czas przetrzymania obiektu w systemie. Czas przetrzymania obiektu odliczany
     * jest od chwili wykonania metody add, która go dodała lub get, która go
     * pobrała.
     *
     * @param o       obiekt przekazywany do systemu
     * @param timeout czas jego przetrzymania.
     * @return unikalny identyfikator obiektu
     */
    @Override
    public int add(Object o, long timeout) {
        synchronized (objects) {
            int this_id = counter;
            base_timeout.put(counter, timeout);
            objects.put(counter, o);
            reset_timeout(counter);
            counter++;
            return this_id;
        }
    }

    /**
     * Metoda odbiera z systemu obiekt o identyfikatorze id lub null jeśli obiekt
     * został już usunięty z systemu Cache.
     *
     * @param id identyfikator obiektu
     * @return obiekt przetrzymywany w systemie lub null jeśli okres przetrzymania
     * obiektu już upłynął.
     */
    @Override
    public Object get(int id) {
        Object output = null;

        if (objects.containsKey(id)) {
            long current = System.currentTimeMillis();
            if (current > current_timeout.get(id)) {
                synchronized (objects) {
                    base_timeout.remove(id);
                    objects.remove(id);
                    current_timeout.remove(id);
                }
                return null;
            }

            synchronized (objects) {
                reset_timeout(id);
                output = objects.get(id);
            }
            return output;
        }
        return null;
    }

    /**
     * Metoda zwraca timeout dla obiektu id.
     *
     * @param id identyfikator obiektu
     * @return czas przetrzymania obiektu o podanym id.
     */
    @Override
    public long getTimeout(int id) {
        long output = 0;
        synchronized (objects) {
            output = current_timeout.get(id) - System.currentTimeMillis();
        }
        return output;
    }


    /**
     * Metoda kasuje obiekt o identyfikatorze id.
     *
     * @param id identyfikator obiektu do skasowania
     */
    @Override
    public void delete(int id) {
        synchronized (objects) {
            base_timeout.remove(id);
            objects.remove(id);
            current_timeout.remove(id);
        }
    }

    /**
     * Metoda wydłuża timeout wszystkich przechowywanych w systemie Cache obiektow.
     *
     * @param time dodatkowy czas przetrzymania.
     */
    @Override
    public void incrementTimeout(long time) {
        synchronized (objects) {
            for (var index : objects.keySet()) {
                var current = current_timeout.get(index);
                current_timeout.put(index, current + time);
            }
        }
    }
}

