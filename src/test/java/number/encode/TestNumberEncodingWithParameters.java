/**
 *
 */
package number.encode;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.encode.client.IOConstants;
import de.encode.dictionary.AlphabetDictionary;
import de.encode.dictionary.Dictionary;
import de.encode.encoder.NumberToWordEncoder;

/**
 * @author arunv
 *
 */
@RunWith(Parameterized.class)
public class TestNumberEncodingWithParameters {

	public static final Logger log = LoggerFactory.getLogger(TestNumberEncodingWithParameters.class);

	private String inputNumber;
	private int expectedTotalOutputGenerated;

	Dictionary<?> dict;
	NumberToWordEncoder enc;

	@Before
	public void initialize() throws Exception {
		dict = new AlphabetDictionary();
		dict.readDictionary(IOConstants.DICTIONARY_PATH);
		enc = new NumberToWordEncoder();
		enc.loadPattern();
		enc.loadDictionary((AlphabetDictionary) dict);
	}

	public TestNumberEncodingWithParameters(String inputNumber, int totalOutputGenerated) {
		this.inputNumber = inputNumber;
		this.expectedTotalOutputGenerated = totalOutputGenerated;
	}

	@SuppressWarnings("rawtypes")
	@Parameters
	public static Collection InputNumbers(){
		return Arrays.asList(new Object[][]{
			{"902462013-98625",0},
			{"243815356/78989513-371084-5936659-0536281/-/25251",15},
			{"810873502888/74-556227/1",18},
			{"6-81/",2},
			{"99014823625/",2},
			{"10/783--5",4},
			{"04824",3}
		});
	}

	@Test
	public void TestNumbersWithTotalOutput() {
		log.info("inputNumber: {}", inputNumber);
		List<String> actualOutput = enc.encodeNumbers(inputNumber);
		Assert.assertEquals(expectedTotalOutputGenerated, actualOutput.size());
	}

}
