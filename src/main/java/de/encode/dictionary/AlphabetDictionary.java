/**
 *
 */
package de.encode.dictionary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * To perform functions on tree structure.
 *
 * @author arunv
 */
public class AlphabetDictionary implements Dictionary<AlphabetNode> {

	private static final Logger log = LoggerFactory.getLogger(AlphabetDictionary.class);

	// object of Node interface
	Node obj = null;

	public AlphabetDictionary() {
		obj = new AlphabetNode('^'); // root node
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.encode.dictionary.IWord#readDictionary(java.lang.String)
	 */
	@Override
	public void readDictionary(String filePath) throws Exception {
		try(Stream<String> fileStream = Files.lines(Paths.get(filePath));) {
				// Read line by line
				//getting stream of file
				fileStream.forEach((word) -> {
					if (word.trim().length() > 0) {
						insertNode(obj, word.trim());
					}
				});
		} catch (IOException exio) {
			log.info("Can not open dictionary file. Error : {}", exio.toString());
			throw exio;
		} catch (Exception ex) {
			log.info(ex.toString());
			throw ex;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.encode.dictionary.Dictionary#insertNode(de.encode.dictionary.Node,
	 * java.lang.String)
	 */
	@Override
	public void insertNode(Node node, String input) {
		try {
			node.setHasChild(true);
			// check children of object for key (first character of word).
			if (node.getChildren().get(input.charAt(0)) == null) {
				// if not make new children of that character

				node.addChild(input.charAt(0), new AlphabetNode(input.charAt(0)));
				node.getChildren().get(input.charAt(0)).setParent(node);
				// set parent of that character
			}
			if (input.length() > 1) {
				insertNode(node.getChildren().get(input.charAt(0)), input.substring(1));
				// recursively finish whole word
			} else {
				// at last character set haschild = false and isWholeWord =
				// true;
				node.getChildren().get(input.charAt(0)).setHasChild(false);
				node.getChildren().get(input.charAt(0)).setIsWord(true);
			}
		} catch (Exception e) {
			log.error(e.toString());
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.encode.dictionary.Dictionary#getDictionaryObject()
	 */
	@Override
	public AlphabetNode getDictionaryObject() {
		return (AlphabetNode) obj;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.encode.dictionary.Dictionary#readWholeWords(de.encode.dictionary.Node)
	 */
	@Override
	public List<String> readWholeWords(Node wordChar) {
		List<String> wholeWords = new ArrayList<String>();

		if (wordChar.getIsWord())
			// if true then return add that whole word by doing ToString()
			wholeWords.add(wordChar.toString());
		if (wordChar.getHasChild()) {
			// if character node has child then recursively perform same
			// function.
			Set<Character> set = wordChar.getChildren().keySet();
			// for (Iterator<Character> iterator = set.iterator();
			// iterator.hasNext();)
			for (char ch : set)
				wholeWords.addAll(readWholeWords((Node) wordChar.getChildren().get(ch)));
		}
		return wholeWords;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.encode.dictionary.Dictionary#isWholeWordExist(de.encode.dictionary.
	 * Node, java.lang.String)
	 */
	@Override
	public boolean isWholeWordExist(Node wordNode, String word) {

		// Check first letter
		if (wordNode.getChildren().containsKey(word.charAt(0))) {
			if (word.length() > 1) {
				Node temp = (Node) wordNode.getChildren().get(word.charAt(0));
				// check recursively
				return isWholeWordExist(temp, word.substring(1));
			} else {// On Last character return that node
				return (wordNode.getChildren().get(word.charAt(0)) != null
						&& wordNode.getChildren().get(word.charAt(0)).getIsWord());
			}
		} else // if not exist return false
			return false;
	}

}
