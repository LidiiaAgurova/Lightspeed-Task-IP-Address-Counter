import org.junit.jupiter.api.Test;
import utils.IPAddressesGenerator;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CountUniqueIPServiceTest {
	public final static String FILE_PATH = "src/main/resources/";

	@Test
	void shouldCountUniqueAddresses() throws IOException {
		IPAddressesGenerator.generateIPs(FILE_PATH + "addresses.txt", 1000, 100);

		//execute algorithm
		long result = CountUniqueIPService.countUniqueIPs(FILE_PATH + "addresses.txt");

		assertEquals(900, result);
	}

	@Test
	void shouldCountUniqueAddressesAndMeasureTime() throws IOException {
		IPAddressesGenerator.generateIPs(FILE_PATH + "addresses1.txt", 1000000, 10000);

		long start = System.currentTimeMillis();
		//execute algorithm
		long result = CountUniqueIPService.countUniqueIPs(FILE_PATH + "addresses1.txt");
		long delta = System.currentTimeMillis() - start;

		assertEquals(990000, result);
		System.out.println("time: " + delta + " ms");
	}
}
