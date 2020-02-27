import java.util.Map;

public interface SupplierInterface {
	/**
	 * Metoda dodaje do systemu quantity obiektów typu objectID. Jeśli istnieją
	 * watki wstrzymane z powodu braku obiektów, a wykonanie metody add brakujące
	 * obiekty dodaje, to wątki te powinny zostać obudzone.
	 * 
	 * @param objectID typ dodawanego obiektu
	 * @param quantity liczba dodawanych obiektów
	 */
	public void add(int objectID, int quantity);

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
	public void request(Map<Integer, Integer> order);
	
	/**
	 * Metoda zwraca aktualny stan magazynu tj. mapę zawierającą informację o znajdujących
	 * się w systemie obiektach. W przypadku braku obiektów metoda zwraca pustą mapę.
	 * Kluczem mapy ma być typ obiektu a wartością liczba takich obiektów w magazynie.
	 * 
	 * @return stan magazynu
	 */
	public Map<Integer, Integer> getInventory();
}
