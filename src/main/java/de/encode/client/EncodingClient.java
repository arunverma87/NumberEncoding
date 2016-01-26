/**
 *
 */
package de.encode.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.encode.dictionary.Dictionary;
import de.encode.dictionary.AlphabetDictionary;
import de.encode.encoder.Encoder;
import de.encode.encoder.NumberToWordEncoder;

/**
 * A client to test the number to word encoding
 *
 * @author arunv
 *
 */
public class EncodingClient implements IOConstants {

	public static final Logger log = LoggerFactory.getLogger(EncodingClient.class);

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {

		// Load Dictionary
		log.info("Load directory");
		Dictionary dictionary = new AlphabetDictionary();
		try {
			dictionary.readDictionary(DICTIONARY_PATH);

			// Set Pattern and dictionary in Encoding algorithm
			log.info("Load Encoder");
			Encoder<AlphabetDictionary> enc = new NumberToWordEncoder();
			enc.loadPattern();
			enc.loadDictionary((AlphabetDictionary) dictionary);

			// Generate encoded file
			log.info("Start Encoding");

			enc.encodeAndGenerateFile(INPUTFILE_PATH, OUTPUTFILE_PATH);

			log.info("End Encoding. File written at {}", OUTPUTFILE_PATH);

		} catch (Exception e) {
			log.info("Dictionay is not loaded");
		}

		// runs on object if pending finalization.
		System.runFinalization();
	}

}
