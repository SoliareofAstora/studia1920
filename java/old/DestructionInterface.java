import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DestructionInterface {
	/**
	 * Metoda przekazuje zbiór wszystkich obiektów, które będą później używane w
	 * tracie pracy programu.
	 * 
	 * @param objects zbiór obiektów
	 */
	public void allObjects(Set<String> objects);

	/**
	 * Metoda dodaje jedną zależność pomiędzy obiektem-źródłem zniszczeń a
	 * obiektami, które niszczy bezpośrednio.
	 * 
	 * @param source       obiekt zniszczony.
	 * @param dependentSet zbiór obiektów zależnych od zniszczonego.
	 */
	public void addDependence(String source, Set<String> dependentSet);

	/**
	 * Metoda zwraca zbiór obiektów, które zostają zniszczone w wyniku zniszczenia
	 * obiektu source. Jeśli obiekt nie niszczy żadnego innego obiektu zwracany jest
	 * null.
	 * 
	 * @param source zniszczony obiekt
	 * @return zbiór wszystkich obiektów, które bezpośrednio i pośrednio są
	 *         niszczone w wyniku zniszczenia source.
	 */
	public Set<String> allObjectsDestroyedBy(String source);

	/**
	 * Metoda zwraca zbiór obiektów, które mogą spowodować zniszczenie obiektu
	 * object. Jeśli obiekt nie jest niszczony przez żaden inny obiekt zwracany jest
	 * null. Jeśli w trakcie analizy zniszczeń okaże się, że zniszczenie obiektu
	 * object prowadzi w dowolniej ścieżce do object to obiekt object także powinien
	 * zostać dodany do wyniku.
	 * 
	 * @param object analizowany obiekt
	 * @return zbiór obiektów, których zniszczenie doprowadzi bezpośrednio lub
	 *         pośrednio do zniszczenia object.
	 */
	public Set<String> allSourcesOfDestruction(String object);

	/**
	 * Metoda zwraca listę obiektów, która prowadzi od zniszczenia
	 * obiektu source do destroyedObject. Jeśli destroyedObject nie jest niszczony
	 * przez obiekt source zwracany jest null. Jeśli w trakcie analizy zniszczeń
	 * okaże się, że zniszczenie obiektu object prowadzi w dowolniej ścieżce do
	 * object to obiekt object także powinien zostać dodany do wyniku.
	 * 
	 * @param source          pierwszy obiekt na liście
	 * @param destroyedObject ostatni obiekt na liści
	 * @return lista (o ile istnieje) od source do destroyedObject. Jeśli
	 *         zniszczenie obiektu source nie prowadzi do zniszczenia
	 *         destroyedObject metoda zwraca null.
	 */
	public List<String> destructionPath(String source, String destroyedObject);

	/**
	 * Statystyka zniszczeń dla źródeł. Metoda zwraca mapę, której kluczem jest
	 * każdy z obiektów przekazanych za pomocą allObjects. Wartością zapisaną w
	 * mapie jest liczba obiektów, które bezpośrednio lub pośrednio są niszczone
	 * przez obiekt-klucz.
	 * 
	 * @return mapa ilości zniszczonych obiektów przez źródło.
	 */
	public Map<String, Integer> sourceStatistics();

	/**
	 * Statystyka zniszczeń dla obiektów niszczonych. Metoda zwraca mapę, której
	 * kluczem jest każdy z obiektów przekazanych za pomocą allObjects. Wartością
	 * jest liczba obiektów, których zniszczenie pośrednio lub bezpośrednio
	 * doprowadzi do zniszczenia obiektu-klucza.
	 * 
	 * @return mapa ilości obiektów, które mogą zniszczyć dany obiekt.
	 */
	public Map<String, Integer> destructionStatistics();
}
