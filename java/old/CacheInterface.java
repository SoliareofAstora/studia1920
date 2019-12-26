
public interface CacheInterface {
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
	public int add(Object o, long timeout);

	/**
	 * Metoda odbiera z systemu obiekt o identyfikatorze id lub null jeśli obiekt
	 * został już usunięty z systemu Cache.
	 * 
	 * @param id identyfikator obiektu
	 * @return obiekt przetrzymywany w systemie lub null jeśli okres przetrzymania
	 *         obiektu już upłynął.
	 */
	public Object get(int id);

	/**
	 * Metoda zwraca timeout dla obiektu id.
	 * 
	 * @param id identyfikator obiektu
	 * @return czas przetrzymania obiektu o podanym id.
	 */
	public long getTimeout(int id);

	/**
	 * Metoda kasuje obiekt o identyfikatorze id.
	 * 
	 * @param id identyfikator obiektu do skasowania
	 */
	public void delete(int id);

	/**
	 * Metoda wydłuża timeout wszystkich przechowywanych w systemie Cache obiektow.
	 * 
	 * @param time dodatkowy czas przetrzymania.
	 */
	public void incrementTimeout(long time);
}
