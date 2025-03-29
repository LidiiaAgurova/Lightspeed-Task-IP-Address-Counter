package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class IPGenerator {

	public static void generateRandomIPs(String filePath, int totalIPs, int duplicateCount) throws IOException {
		Random random = new Random();
		Set<String> generatedIPs = new HashSet<>();
		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

		int uniqueIPs = totalIPs - duplicateCount;

		// First, generate unique IP addresses
		while (generatedIPs.size() < uniqueIPs) {
			String ip = generateRandomIP(random);
			generatedIPs.add(ip);
		}

		// Write unique IPs to file
		for (String ip : generatedIPs) {
			writer.write(ip);
			writer.newLine();
		}

		// Now, add duplicates
		for (int i = 0; i < duplicateCount; i++) {
			String duplicateIP = getRandomItemFromSet(generatedIPs, random);
			writer.write(duplicateIP);
			writer.newLine();
		}

		writer.close();
	}

	// Method to generate a random valid IP address
	private static String generateRandomIP(Random random) {
		int part1 = random.nextInt(256); // First octet (0-255)
		int part2 = random.nextInt(256); // Second octet (0-255)
		int part3 = random.nextInt(256); // Third octet (0-255)
		int part4 = random.nextInt(256); // Fourth octet (0-255)

		return part1 + "." + part2 + "." + part3 + "." + part4;
	}

	// Helper method to get a random item from the set (for introducing duplicates)
	private static String getRandomItemFromSet(Set<String> set, Random random) {
		int index = random.nextInt(set.size());
		int currentIndex = 0;
		for (String item : set) {
			if (currentIndex == index) {
				return item;
			}
			currentIndex++;
		}
		return null;
	}
}
