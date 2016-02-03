/**
 *
 */
package de.encode.dictionary;

import java.util.List;

/**
 * Interface to handle functions on Tree
 *
 * @author arunv
 *
 */
public interface Dictionary<T extends Node> {

	/**
	 * Read dictionary given in text format.
	 *
	 * @param filepath
	 *            a filepath to read a dictionary from
	 */
	void readDictionary(String filePath) throws Exception;

	/**
	 * inserts a node for each character of input string. Populates a given
	 * input into Dictionary
	 *
	 * @param node
	 * @param input
	 */
	void insertNode(Node node, String input);

	/**
	 * get object of class which implements Node interface
	 *
	 * @return {@link Node} object
	 */
	T getDictionaryObject();

	/**
	 * Read following words
	 *
	 * @param character
	 *            Letter
	 * @return List<String> List of Words
	 */
	List<String> readWholeWords(Node character);

	/**
	 * o check sequence of characters as a single word exist in a tree or not
	 * @param character
	 * @param word
	 * @return boolean
	 */
	boolean isWholeWordExist(Node character, String word);
}
