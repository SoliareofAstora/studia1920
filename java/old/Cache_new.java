import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
// https://zti.if.uj.edu.pl/Piotr.Oramus/dydaktyka/Java/WebTester/report.php?hash=f03901f7aa3adc08e8ce33246381d806
public class Cache implements CacheInterface {

    int counter = 0;
    ReadWriteLock rwlock = new ReentrantReadWriteLock();
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
                for (var index : todelete) {
                    rwlock.writeLock().lock();
                    try {
                        base_timeout.remove(index);
                        objects.remove(index);
                        current_timeout.remove(index);
                    } finally {
                        rwlock.writeLock().unlock();
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
        int this_id = counter;
        rwlock.writeLock().lock();
        try {
            base_timeout.put(counter, timeout);
            objects.put(counter, o);
            reset_timeout(counter);
            counter++;
        } finally {
            rwlock.writeLock().unlock();
        }
        return this_id;
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
                rwlock.writeLock().lock();
                try {
                    base_timeout.remove(id);
                    objects.remove(id);
                    current_timeout.remove(id);
                } finally {
                    rwlock.writeLock().unlock();
                }
                return null;
            }

            rwlock.writeLock().lock();
            try {
                reset_timeout(id);
                output = objects.get(id);
            } finally {
                rwlock.writeLock().unlock();
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
        rwlock.readLock().lock();
        try {
            output = current_timeout.get(id) - System.currentTimeMillis();
        } finally {
            rwlock.readLock().unlock();
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
        rwlock.writeLock().lock();
        try {
            base_timeout.remove(id);
            objects.remove(id);
            current_timeout.remove(id);
        } finally {
            rwlock.writeLock().unlock();
        }
    }

    /**
     * Metoda wydłuża timeout wszystkich przechowywanych w systemie Cache obiektow.
     *
     * @param time dodatkowy czas przetrzymania.
     */
    @Override
    public void incrementTimeout(long time) {
        rwlock.writeLock().lock();
        try {
            for (var index : objects.keySet()) {
                var current = current_timeout.get(index);
                current_timeout.put(index, current + time);
            }
        } finally {
            rwlock.writeLock().unlock();
        }
    }
}
