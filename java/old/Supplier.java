import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.lang.Thread.sleep;

class Start {
    public static void main(String[] args) {
        LinkedList<HashMap<Integer, Integer>>requests = new LinkedList<>();
        HashMap<Integer, Integer> request = new HashMap<>();

        request.put(10,5);

        requests.add(request);

        var supp = new Supplier();

        Thread client = new Thread(new Runnable() {
            @Override
            public void run() {
                supp.request(requests.get(0));
            }
        });
        Thread client1 = new Thread(new Runnable() {
            @Override
            public void run() {
                supp.request(requests.get(0));
            }
        });
        Thread client3 = new Thread(new Runnable() {
            @Override
            public void run() {
                supp.request(requests.get(0));
            }
        });
        Thread client4 = new Thread(new Runnable() {
            @Override
            public void run() {
                supp.request(requests.get(0));
            }
        });

        Thread c = new Thread(client);

        client.start();
        client1.start();
        c.start();
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        supp.add(10,10);
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        supp.add(10,10);
        client3.start();
        client4.start();

        System.out.println("Main finished");
    }
}

public class Supplier implements SupplierInterface {

    class Client{
        final Thread thread;
        HashMap<Integer, Integer> request;
        Client(Thread t, HashMap r){
            thread = t;
            request = r;
        }
    }

    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    HashSet<Client> waiting_bench = new HashSet<>();
    HashMap<Integer, Integer> storage = new HashMap<>();

    void update() {
        LinkedList<Client> todelete = new LinkedList<>();
        for (var client : waiting_bench) {
            var order = client.request;
            if (tryToGetItems(order)) {
                for (var key : order.keySet()) {
                    storage.put(key, storage.get(key) - order.get(key));
                }
                todelete.add(client);
            }
        }
        for (var leaving : todelete) {
            synchronized (leaving.thread) {
                leaving.thread.notify();
            }
            waiting_bench.remove(leaving);
        }
    }

    boolean tryToGetItems(Map<Integer, Integer> order){
        for (var key:order.keySet()) {
            if (storage.containsKey(key)) {
                if (storage.get(key) < order.get(key)) {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        return true;
    }

     /**
     * Metoda dodaje do systemu quantity obiektów typu objectID. Jeśli istnieją
     * watki wstrzymane z powodu braku obiektów, a wykonanie metody add brakujące
     * obiekty dodaje, to wątki te powinny zostać obudzone.
     *
     * @param objectID typ dodawanego obiektu
     * @param quantity liczba dodawanych obiektów
     */
    @Override
    public void add(int objectID, int quantity) {
        lock.writeLock().lock();
        int count = 0;
        if (storage.containsKey(objectID))
            count = storage.get(objectID);
        storage.put(objectID, count + quantity);
        System.out.println("Added "+objectID+" quantity "+quantity+". Storage is now "+storage);
//        update();
        lock.writeLock().unlock();
        synchronized (storage){
            storage.notifyAll();
        }
    }

    /**
     * Metoda służąca do zgłoszenia zamówienia na określoną liczbę obiektów
     * określonych typów. Zamówienie składane jest w postaci mapy. Kluczem jest typ
     * obiektu a wartością ilość obiektów, które są potrzebne. Metoda wstrzymuje
     * wątek zamawiającego do czasu, gdy zamówienie może być zrealizowane. Jeśli
     * obiekty są dostępne przed wywołaniem metody request, metoda powinna zakończyć
     * się natychmiast. Jedynym poprawnym sposobem wstrzymania wątku jest użycie
     * metody wait() i wprowadzenie wątek w stan WAITING. Realizacja zamówienia
     * zmniejsza liczbę obiektów będących w posiadaniu systemu o te, które zostały
     * wydane w zrealizowanym zamówieniu.
     *
     * @param order mapa zawierająca informacje o zamówieniu.
     */
    @Override
    public void request(Map<Integer, Integer> order) {
        lock.writeLock().lock();
        if (!tryToGetItems(order)) {
            lock.writeLock().unlock();
            while(true) {
                System.out.println("No items in storage, waiting " + Thread.currentThread());
//                waiting_bench.add(new Client(Thread.currentThread(), (HashMap) order));
//                lock.writeLock().unlock();
                synchronized (storage) {
                    try {
                        storage.wait();
                    } catch (InterruptedException e) {
                    }
                    if (tryToGetItems(order)) {
                        for (var key : order.keySet()) {
                            storage.put(key, storage.get(key) - order.get(key));
                        }
                        System.out.println("Got my items, going home " + Thread.currentThread());
                        return;
                    }
                }

            }
        }
        System.out.println(Thread.currentThread()+" items are in storage, getting "+order);
        for (var key : order.keySet()) {
            storage.put(key, storage.get(key) - order.get(key));
        }
        lock.writeLock().unlock();
    }

    /**
     * Metoda zwraca aktualny stan magazynu tj. mapę zawierającą informację o znajdujących
     * się w systemie obiektach. W przypadku braku obiektów metoda zwraca pustą mapę.
     * Kluczem mapy ma być typ obiektu a wartością liczba takich obiektów w magazynie.
     *
     * @return stan magazynu
     */
    @Override
    public Map<Integer, Integer> getInventory() {
        return storage;
    }
}
