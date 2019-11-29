import java.util.Map;

public interface DeduplicationInterface {
	/**
	 * Metoda zwraca słownik używany przez system deduplikacji. Kluczem mapy jest
	 * identyfikator liczbowy a wartością słowo, które jest zastępowane przez dany
	 * klucz. Identyfikatory powinny być przydzielane od wartości 1. Kolejne słowa,
	 * które zapisywane są w słowniku powinny uzyskiwać kolejne numery
	 * identyfikacyjne.
	 * 
	 * @return mapa słów używanych przez system deduplikacji.
	 */
	public Map<Integer, String> getDictionary();

	/**
	 * Metoda dokonuje odtworzenia oryginalnego ciągu na podstawie mapy-słownika
	 * oraz ciągu do odkodowania. Dekodowaniu podlegają podciągi w postaci #numer.
	 * Podciąg #numer należy zastąpić słowem znajdującym się w przekazanym słowniku
	 * pod kluczem numer. Prawidłowy zapis sekwencji #numer to np. #1, #2, #3 czy
	 * #13 a nie np. #001, #002, #003 czy #013.
	 * 
	 * @param dictionary słownik użytych słów. Metoda nigdy nie zwraca null. Jeśli
	 * w przekazanych ciągach brak słów, które można wpisać do słownika, metoda
	 * zwraca pustą mapę. 
	 * @param toDecode   ciąg znaków do zdekodowania
	 * @return Odkodowany ciąg znaków
	 */
	public String decode(Map<Integer, String> dictionary, String toDecode);

	/**
	 * Metoda dodaje nowy ciąg znaków. Zostaje on zapisany i otrzymuje unikalny
	 * numer. Numer ten zostaje zwrócony użytkownikowi. Pierwszy ciąg powinien
	 * uzyskać numer 1.
	 * 
	 * @param newString dodawany ciąg znaków
	 * @return unikalny numer, pod którym ciąg został zapisany.
	 */
	public int addString(String newString);

	/**
	 * Metoda zwraca ciąg o podanym numerze. Zwracany ciąg powinien przybrać postać
	 * zależną od aktualnego stanu słownika. Słowa znajdujące się w słowniku powinny
	 * zostać zastąpione w ciągu podciągami o postaci #numer, gdzie numer to klucz w
	 * słowniku. Prawidłowy zapis podciągu #numer to np. #1, #2, #3 czy #13 a nie
	 * np. #001, #002, #003 czy #013.
	 * 
	 * @param id identyfikator zapisanego wcześniej w systemie ciągu.
	 * @return aktualna postać ciągu z uwzględnieniem procesu deduplikacji.
	 */
	public String getString(int id);
}
