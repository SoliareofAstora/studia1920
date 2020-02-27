import java.io.IOException;

public interface CleanerInterface {
	/**
	 * Metoda usuwa z pliku źródłowego java o nazwie filename metodę main. Metoda używa wyłącznie
	 * pliku o podanej nazwie. Jest on najpierw odczytywany a następnie zapisywany. Metoda
	 * nie tworzy (nawet tymczasowo) innych plików.
	 * 
	 * @param filename plik do przetworzenia.
	 */
	public void removeMain(String filename) throws IOException;
}
