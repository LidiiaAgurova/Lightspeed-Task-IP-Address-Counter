import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.BitSet;


/**
	An algorithm to count the number of unique IP addresses in a txt file.

	The algorithm reads the bytes from a file. For each IP address it reads all the 4 parts of it.
	After that all 4 parts are stored in a BitSet[][][] - a 4-dimensional structure to understand which IP addresses are unique.
	Each time a new value is added to the set, the counter is incremented.

  	The algorithm works for the files with valid IP addresses. Validation can be added later.

	Main points:

	1. MappedByteBuffer do not store the whole file in memory, but instead it stores its map. It saves us time and memory.

	2. BitSet is a convenient structure to effectively store bit data. The easy way was to create a BitSet with 2^32 capacity - the possible number of IP addresses.
	But BitSet can be created only with int capacity, so 2^32 value is too big. That is why we create a BitSet with possible range for each part of the IP (0-255).
	In case such value is present - we set the value in BitSet to 'true'.
	For 2nd, 3rd and 4th parts we create a set only if such value is present. If we were able to use single BitSet, 2^32 size would be anyway occupied.
	And such 4-dimensional structure saves us the memory.
 */
public class CountUniqueIPService {
	private static final int BYTE_RANGE = 256;
	private final BitSet[][][] IP_ADDRESSES_SET = new BitSet[BYTE_RANGE][][];

	public long countUniqueIPs(String filePath) throws IOException {

		// We are going to use MappedByteBuffer to store a file map in a memory.
		// But for huge files (e.g. 120GB) we still need to use chunks otherwise we can fill up the memory
		long positionInFile = 0;
		int chunkSize = 256 * 1024 * 1024; // 256MB chunks, can be moved to properties file later

		try (var channel = FileChannel.open(Paths.get(filePath), StandardOpenOption.READ)) {
			var channelSize = channel.size();
			long uniqueCount = 0;

			while (positionInFile < channelSize) {
				long remainingSize = channelSize - positionInFile;
				int needToReadSize = (int) Math.min(chunkSize, remainingSize);

				// Create a mapped buffer from current position in file to chunk size (or less if it's the end of file)
				MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, positionInFile, needToReadSize);
				positionInFile += needToReadSize;

				// Every IP address is represented in 4 parts
				int part1 = 0, part2 = 0, part3 = 0, part4 = 0;
				int partIndex = 0;

				while (buffer.hasRemaining()) {
					byte byteValue = buffer.get();
					if (byteValue >= '0' && byteValue <= '9') {        // If current byte is a number
						switch (partIndex) {
							case 0:
								// Move the previous number to next decimal
								// Transform byteValue to int by subtracting '0' char int value
								part1 = part1 * 10 + (byteValue - '0');
								break;
							case 1:
								part2 = part2 * 10 + (byteValue - '0');
								break;
							case 2:
								part3 = part3 * 10 + (byteValue - '0');
								break;
							case 3:
								part4 = part4 * 10 + (byteValue - '0');
								break;
						}
					} else if (byteValue == '.') {        // The current part of the IP is finished
						partIndex++;
					} else if (byteValue == '\n' || !buffer.hasRemaining()) {     // End of the line or the buffer

						// If any dimension is null - there was no such IP address value
						// Create all 4 dimensions for each IP address part
						if (IP_ADDRESSES_SET[part1] == null) {
							IP_ADDRESSES_SET[part1] = new BitSet[BYTE_RANGE][];
						}
						if (IP_ADDRESSES_SET[part1][part2] == null) {
							IP_ADDRESSES_SET[part1][part2] = new BitSet[BYTE_RANGE];
						}
						if (IP_ADDRESSES_SET[part1][part2][part3] == null) {
							IP_ADDRESSES_SET[part1][part2][part3] = new BitSet(BYTE_RANGE);
						}

						// All 4 dimensions are present - set a unique value and increment
						if (!IP_ADDRESSES_SET[part1][part2][part3].get(part4)) {
							IP_ADDRESSES_SET[part1][part2][part3].set(part4);
							uniqueCount++;
						}

						// Reset all values to 0 for the next IP processing
						part1 = part2 = part3 = part4 = 0;
						partIndex = 0;
					}
				}
			}
			return uniqueCount;
		}
	}
}
