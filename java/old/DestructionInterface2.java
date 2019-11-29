import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Wyjątek zgłaszany w przypadku, gdy pomiędzy dwoma obiektami nie zachodzi
 * relacja "pierwszy doprowadza do zniszczenia drugiego".
 */
class NoPathException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7607980267154059456L;

}

/**
 * Wyjątek używany do poinformowania użytkownika o tym, że używa obiektu, który
 * wcześniej nie został zgłoszony za pomocą metody allObjects.
 *
 */
class ObjectUnknownException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1358441944798450371L;

}

/**
 * Wyjątek zgłaszany, gdy dojdzie do wykrycia niedozwolonej pętli prowadzącej od
 * zniszczenia obiektu X dowolną sciężką ponownie do X.
 */
class LoopException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7573622440014234879L;
}

/**
 * Wyjątek zgłaszany, gdy nie można podać jednoznaczego rozwiązania.
 */
class AmbiguousSolutionsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1166680220863024915L;

}

public interface DestructionInterface2 {
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
	 * 
	 * @throws ObjectUnknownException wyjątek zgłaszany gdy source lub zbiór
	 *                                dependentSet zawierają obiekt, który wcześniej
	 *                                nie został przekazany poprzez allObjects.
	 * 
	 * @throws LoopException          wyjątek zgłaszany, gdy dodanie zależności
	 *                                doprowadzi do pojawienia się pętli. Wywołanie
	 *                                metody prowadzące do pętli powoduje zgłoszenie
	 *                                wyjątku zaś przekazane dane są ignorowane
	 *                                (zależność nie jest dodawana).
	 */
	public void addDependence(String source, Set<String> dependentSet) throws ObjectUnknownException, LoopException;

	/**
	 * Metoda zwraca zbiór list obiektów. Każda lista to kompletna ścieżka
	 * prowadząca od zniszczenia obiektu source do zniszczenia destroyedObject.
	 * Zbiór powinien zawierać wszystkie możliwe ścieżki zniszczenia destroyedObject
	 * przez source. Jeśli destroyedObject nie jest niszczony przez obiekt source
	 * zwracany jest wyjątek.
	 * 
	 * @param source          pierwszy obiekt na liście
	 * @param destroyedObject ostatni obiekt na liści
	 * 
	 * @throws NoPathException        wyjątek zgłaszany, gdy source nie niszczy
	 *                                destroyedObject.
	 * @throws ObjectUnknownException source i/lub destroyedObject są nieznane (nie
	 *                                zostały wcześniej zgłoszone za pomocą
	 *                                allObjects
	 * 
	 * @return najkrótsza lista (o ile istnieje) od source do destroyedObject. Jeśli
	 *         zniszczenie obiektu source nie prowadzi do zniszczenia
	 *         destroyedObject metoda zwraca null.
	 */
	public Set<List<String>> allDestructionPath(String source, String destroyedObject)
			throws NoPathException, ObjectUnknownException;

	/**
	 * Metoda zwraca najkrótszą ścieżkę obiektów prowadzącą od zniszczenia obiektu
	 * source do zniszczenia destroyedObject. Jeśli destroyedObject nie jest
	 * niszczony przez obiekt source zwracany jest wyjątek. Jeśli istnieje więcej
	 * niż jedno rozwiązanie o takiej samej miminalnej długości ścieżki zgłaszany
	 * jest wyjątek.
	 * 
	 * @param source          pierwszy obiekt na liście
	 * @param destroyedObject ostatni obiekt na liści
	 * 
	 * @throws NoPathException             wyjątek zgłaszany, gdy source nie niszczy
	 *                                     destroyedObject.
	 * @throws ObjectUnknownException      source i/lub destroyedObject są nieznane
	 *                                     (nie zostały wcześniej zgłoszone za
	 *                                     pomocą allObjects
	 * @throws AmbiguousSolutionsException istnieje więcej niż jedno rozwiązanie o
	 *                                     minimalnej długości
	 * 
	 * @return najkrótsza lista (o ile istnieje) od source do destroyedObject. Jeśli
	 *         zniszczenie obiektu source nie prowadzi do zniszczenia
	 *         destroyedObject metoda zwraca null.
	 */
	public List<String> shortestDestructionPath(String source, String destroyedObject)
			throws NoPathException, ObjectUnknownException, AmbiguousSolutionsException;

}
