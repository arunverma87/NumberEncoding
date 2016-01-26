/**
 *
 */
package de.encode.encoder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.encode.custom.CustomArrayListMultiMap;
import de.encode.custom.CustomMultiMap;
import de.encode.dictionary.AlphabetDictionary;
import de.encode.dictionary.AlphabetNode;
import de.encode.dictionary.Dictionary;
import de.encode.dictionary.Node;

/**
 * A number to word encoder which encodes a given number to word looking up into
 * the dictionary
 *
 * @author arunv
 *
 */
public class NumberToWordEncoder implements Encoder<AlphabetDictionary> {

	private CustomMultiMap<Integer, Character> pattern;
	private Dictionary<AlphabetNode> dict;

	BufferedWriter wr = null;
	BufferedReader br = null;
	private static final Logger log = LoggerFactory.getLogger(NumberToWordEncoder.class);

	/**
	 * Constructor
	 */
	public NumberToWordEncoder() {
		pattern = new CustomArrayListMultiMap<>();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * arun.code.encoding.Encode#loadDictionary(arun.code.dictionary.Dictionary)
	 */
	public void loadDictionary(AlphabetDictionary dictionary) {
		this.dict = dictionary;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see arun.code.encoding.Encode#encodeAndGenerateFile(java.lang.String,
	 * arun.code.dictionary.DictionaryFunctions)
	 */
	@Override
	public void encodeAndGenerateFile(String inputFilePath, String outputFilePath) {
		try {
			wr = new BufferedWriter(new FileWriter(outputFilePath));
			File file = new File(inputFilePath);
			if (file.exists()) {
				br = new BufferedReader(new FileReader(file));
				// reading line by line
				String line;
				while ((line = br.readLine()) != null) {
					// trim line and check length
					if (line.trim().length() > 0) {
						// write in to file.
						for (String output : encodeNumbers(line.trim())) {
							try {
								// Write output in to file with Numbers.
								wr.write(line + ": " + output);
								wr.newLine();
								log.info("{}: {}", line, output);
							} catch (Exception e) {
								log.error(e.toString());
							}
						}
					}
				}
			} else
				throw new IOException("File not found on path " + inputFilePath);
		} catch (IOException exio) {
			log.error(exio.toString());
		} finally {
			try {
				if (br != null)
					br.close();
				wr.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}

	/**
	 * EncodeNumbers read by File.
	 *
	 * @param line
	 * @return List of String
	 */
	public List<String> encodeNumbers(String line) {
		List<String> fullyEncoded = new ArrayList<>();
		// remove special character
		String digitLine = removeSpecialChar(line);
		// get First possible words of input numbers
		List<String> partialEncoded = encodePartially(digitLine);

		if (partialEncoded != null)
			for (int i = 0; i < partialEncoded.size(); i++) {
				// Complete encoding by adding trailing words to partially
				// encoded number.
				List<String> subWord = encodeFully(partialEncoded.get(i), digitLine);
				if (subWord != null)
					fullyEncoded.addAll(subWord);
				try {
					subWord.clear();
					subWord = null;
				} catch (Exception e) {
					// ignore
				}
			}
		return fullyEncoded;
	}

	/**
	 * Encode Partially. Getting possible first words starting from first digit
	 * of input numbers.
	 *
	 * @param numbers
	 * @return List
	 */
	private List<String> encodePartially(String numbers) {

		List<String> words = new ArrayList<>();
		if (numbers == null || numbers.length() == 0)
			// if numbers is null then just send new arraylist.
			return words;
		else if (numbers.length() == 1) {
			// if it's last digit then just add it in word
			words.add(numbers);
			return words;
		}
		// get Dictionary root object
		Node obj = dict.getDictionaryObject();
		int chInt = numbers.charAt(0) - 48;
		// get list of characters of first number
		for (char ch : getListofChar(chInt)) {
			// get children of one character of that digit
			List<Node> childNodes = obj.getChildNodes(ch);
			if (childNodes != null) {
				// add all words of that Children nodes
				for (Node node : childNodes) {
					words.addAll(getWords(numbers.substring(1), node));
				}
			}
		}

		if (words.size() == 0) {
			words.add(numbers.subSequence(0, 1).toString());
			return words;
		}
		return words;
	}

	/**
	 * It concates all trailing words of first word of all possible outcomes
	 * from characters.
	 */
	private List<String> encodeFully(String lastword, String numbers) {
		List<String> finalList = new ArrayList<>();
		// removing spaces and quotes to check length (quotes are not considered
		// here as character it's a part of umlaut character.
		String textLine = removeSpacesAndQuote(lastword);
		if (textLine.length() == numbers.length()) {
			// if numbers length matches after removing spaces and quotes then
			// just add that word return the list
			finalList.add(lastword);
		} else {
			// remove numbers from number string which are already calculated.
			String remainingNumbers = numbers.substring(textLine.length());
			List<String> nextPartOfWord = null;
			// again start encoding for remaining numbers and take result in to
			// list.
			nextPartOfWord = encodePartially(remainingNumbers);
			if (nextPartOfWord != null) {
				for (String words : nextPartOfWord) {
					// Check for two consecutive digits, if true then don't add
					// those words it in to final list, else do
					if (!((textLine + words).matches("[0-9a-zA-Z]*[0-9]{2}[0-9a-zA-Z]*"))) {
						finalList.addAll(encodeFully(lastword + " " + words, numbers));
					}
				}
				try {
					// clear list
					nextPartOfWord.clear();
					nextPartOfWord = null;
				} catch (Exception e) {
					// ignore
				}
			}
		}
		return finalList;
	}

	/**
	 * should return list of all possible words start from first number up to it
	 * gets IsWord property true. It gets only possible first words list.
	 *
	 */
	private List<String> getWords(String remaining, Node node) {
		List<String> subWords = new ArrayList<>();
		if (remaining == null || remaining.length() == 0)
			// if remaining is null or length 0 then send new array list
			return subWords;
		// else start with first character of remaining letter
		int chInt = remaining.charAt(0) - 48;
		for (char ch : getListofChar(chInt)) {
			// Node childNodes = node.getChildNode(ch);
			List<Node> childObjects = node.getChildNodes(ch);
			if (childObjects == null || childObjects.size() == 0)
				continue;
			for (Node child : childObjects) {
				if (child.getIsWord()) {
					subWords.add(child.toString());
				} else if (child.getChildNode('"') != null && child.getChildNode('"').getIsWord()) {
					subWords.add(child.getChildNode('"').toString());
				}
				if (remaining.length() > 1)
					subWords.addAll(getWords(remaining.substring(1), child));
			}
		}

		return subWords;
	}

	// Get list of characters from Multimap on Key
	private List<Character> getListofChar(int key) {
		return pattern.get(key);
	}

	/**
	 * Removing special characters like \,-
	 *
	 * @param input
	 *            String
	 * @return Digits containing string
	 */
	private String removeSpecialChar(String input) {
		String regex = "[0-9]+";
		// check with regular expression
		if (!input.matches(regex)) {
			String output = "";
			try {
				for (int i = 0; i < input.length(); i++) {
					if (input.charAt(i) >= 48 && input.charAt(i) < 58)
						output += input.charAt(i);
				}
			} catch (Exception ex) {
				return null;
			}
			return output;
		} else
			return input;
	}

	private String removeSpacesAndQuote(String input) {
		if (input != null)
			return input.replace(" ", "").replace("\"", "");
		else
			return new String("");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see arun.code.encoding.Encode#loadPattern()
	 */
	@Override
	public void loadPattern() {

		pattern.put(0, 'e');
		pattern.put(0, 'E');

		pattern.put(1, 'j');
		pattern.put(1, 'n');
		pattern.put(1, 'q');
		pattern.put(1, 'J');
		pattern.put(1, 'N');
		pattern.put(1, 'Q');

		pattern.put(2, 'r');
		pattern.put(2, 'w');
		pattern.put(2, 'x');
		pattern.put(2, 'R');
		pattern.put(2, 'W');
		pattern.put(2, 'X');

		pattern.put(3, 'd');
		pattern.put(3, 's');
		pattern.put(3, 'y');
		pattern.put(3, 'D');
		pattern.put(3, 'S');
		pattern.put(3, 'Y');

		pattern.put(4, 'f');
		pattern.put(4, 't');
		pattern.put(4, 'F');
		pattern.put(4, 'T');

		pattern.put(5, 'a');
		pattern.put(5, 'm');
		pattern.put(5, 'A');
		pattern.put(5, 'M');

		pattern.put(6, 'c');
		pattern.put(6, 'i');
		pattern.put(6, 'v');
		pattern.put(6, 'C');
		pattern.put(6, 'I');
		pattern.put(6, 'V');

		pattern.put(7, 'b');
		pattern.put(7, 'k');
		pattern.put(7, 'u');
		pattern.put(7, 'B');
		pattern.put(7, 'K');
		pattern.put(7, 'U');

		pattern.put(8, 'l');
		pattern.put(8, 'o');
		pattern.put(8, 'p');
		pattern.put(8, 'L');
		pattern.put(8, 'O');
		pattern.put(8, 'P');

		pattern.put(9, 'g');
		pattern.put(9, 'h');
		pattern.put(9, 'z');
		pattern.put(9, 'G');
		pattern.put(9, 'H');
		pattern.put(9, 'Z');

	}

}
