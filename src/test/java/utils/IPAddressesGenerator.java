package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


/**
 * Just a random txt file generator to ease the testing.
 * Is able to generate a file with valid IP addresses.
 */

public class IPAddressesGenerator {

	/**
	 * Just a random txt file generator to ease the testing.
	 * Is able to generate a file with valid IP addresses.
	 *
	 * @param filePath the output file path
	 * @param totalIPs the number of IPs in the file
	 * @param duplicatedIPs the number of duplicates in the file
	 */
	public static void generateIPs(String filePath, int totalIPs, int duplicatedIPs) throws IOException {
		var random = new Random();
		Set<String> generatedIPSet = new HashSet<>();
		int uniqueIPs = totalIPs - duplicatedIPs;
		int duplicationsCounter = 0;
		int uniqueCounter = 0;

		try (var writer = new BufferedWriter(new FileWriter(filePath))) {

			// add random IPs to file while the number is not enough
			while (uniqueCounter < uniqueIPs) {
				var ipAddress = random.nextInt(256) + "." +
						random.nextInt(256) + "." +
						random.nextInt(256) + "." +
						random.nextInt(256);
				var isAddressAdded = generatedIPSet.add(ipAddress);

				if (isAddressAdded) {
					// Write IP to file
					writer.write(ipAddress);
					writer.newLine();

					// add duplication for each IP while the number is not enough
					if (duplicationsCounter < duplicatedIPs) {
						writer.write(ipAddress);
						writer.newLine();
						duplicationsCounter++;
					}

					uniqueCounter++;
				}
			}
		}
	}
}
