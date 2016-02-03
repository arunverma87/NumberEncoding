/**
 *
 */
package de.encode.encoder;

/**
 * Basic encoder contract
 *
 * @author arunv
 *
 */
public interface Encoder<T> {

	/**
	 * Load Dictionary to encode
	 *
	 * @param dictionary
	 *            Dictionary object
	 */
	void loadDictionary(T dictionary);

	/**
	 * Load encoding pattern
	 */
	void loadPattern();

	/**
	 * perform encoding on numbers traverse line by line
	 *
	 * @param inputFilePath
	 *            (String) path of input numbers file.
	 * @param outputFilePath
	 *            (String) path of output file.
	 * @return true,if success, else false
	 */
	boolean encodeAndGenerateFile(String inputFilePath, String outputFilePath);
}
