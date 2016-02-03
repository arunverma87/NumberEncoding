/**
 *
 */
package number.encode;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.encode.client.IOConstants;
import de.encode.dictionary.AlphabetDictionary;
import de.encode.dictionary.Dictionary;
import de.encode.encoder.NumberToWordEncoder;

/**
 *
 * Test number to word encoding for a given input number
 *
 * @author arunv
 *
 */
public class TestNumberEncoding {

	Dictionary<?> dictionary;

	NumberToWordEncoder enc;

	@Before
	public void createDictionary() throws Exception {

		dictionary = new AlphabetDictionary();

		// read Dictionary
		dictionary.readDictionary(IOConstants.DICTIONARY_PATH);

		// Create NumberToWordEncoder object
		enc = new NumberToWordEncoder();
		// Load pattern of encoding
		enc.loadPattern();
		// Load dictionary in Encoder
		enc.loadDictionary((AlphabetDictionary) dictionary);
	}

	@Test
	public void testNumberToWordEncoding() {

		String number = "04824";

		List<String> expectedOutput = Arrays.asList("0 Torf", "0 fort", "0 Tor 4");

		List<String> actualOutput = enc.encodeNumbers(number);

		Assert.assertEquals(expectedOutput.size(), actualOutput.size());

		for (String actual : actualOutput) {
			Assert.assertEquals(true, expectedOutput.contains(actual));
		}
	}

	@Test
	public void testOutputContainsEncodedString() {

		String wordToTest = "";
		String number = "";

		number = "7-3593-50";
		wordToTest = "USA 9 da 0";

		// number = "-885/63538";
		// wordToTest = "O\"l Midas 8";

		// number = "10/783--5";
		// wordToTest = "neu o\"d 5";

		List<String> actualOutput = enc.encodeNumbers(number);
		Assert.assertEquals(true, actualOutput.contains(wordToTest));
	}

	@Test
	public void testTotalOutputGenerate() {
		String number = "243815356/78989513-371084-5936659-0536281/-/25251";
		List<String> actualOutput = enc.encodeNumbers(number);
		Assert.assertEquals(15, actualOutput.size());
	}

	@Test
	public void testOutputNotContainsEncodedString() {
		String wordToTest = "";
		String number = "";
		number = "7-3593-50";
		wordToTest = "7 da 9 da 0";
		List<String> actualOutput = enc.encodeNumbers(number);
		Assert.assertNotEquals(true, actualOutput.contains(wordToTest));
	}

	@Test
	public void testNoOuputForNumbers() {
		String number = "902462013-98625";
		List<String> actualOutput = enc.encodeNumbers(number);
		Assert.assertEquals(0, actualOutput.size());
	}

	@Test
	public void testWordExist() {
		Assert.assertTrue(dictionary.isWholeWordExist(dictionary.getDictionaryObject(), "O\"l"));
	}

}
