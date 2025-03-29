import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.IOException;
import java.util.BitSet;

public class CountUniqueIPService {
	private static final int BYTE_RANGE = 256;
	private static final BitSet[][][] storage = new BitSet[BYTE_RANGE][][];

	public static long countUniqueIPs(String fileName) throws IOException {
		// MappedByteBuffer stores a map in a memory. For huge files (e.g. 120GB) we still need to use chunks
		long position = 0;
		int chunkSize = 256 * 1024 * 1024; // 256MB chunks

		try (FileChannel channel = FileChannel.open(Paths.get(fileName), StandardOpenOption.READ)) {
			long uniqueCount = 0;

			while (position < channel.size()) {
				long remaining = channel.size() - position;
				int readSize = (int) Math.min(chunkSize, remaining);

				MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, position, readSize);
				position += readSize;

				int part1 = 0, part2 = 0, part3 = 0, part4 = 0;
				int partIndex = 0;

				while (buffer.hasRemaining()) {
					byte b = buffer.get();
					if (b >= '0' && b <= '9') {
						switch (partIndex) {
							case 0 -> part1 = part1 * 10 + (b - '0');
							case 1 -> part2 = part2 * 10 + (b - '0');
							case 2 -> part3 = part3 * 10 + (b - '0');
							case 3 -> part4 = part4 * 10 + (b - '0');
						}
					} else if (b == '.') {
						partIndex++;
					} else if (b == '\n' || !buffer.hasRemaining()) { // End of line or file
						if (storage[part1] == null) storage[part1] = new BitSet[BYTE_RANGE][];
						if (storage[part1][part2] == null) storage[part1][part2] = new BitSet[BYTE_RANGE];
						if (storage[part1][part2][part3] == null) storage[part1][part2][part3] = new BitSet(BYTE_RANGE);

						if (!storage[part1][part2][part3].get(part4)) {
							storage[part1][part2][part3].set(part4);
							uniqueCount++;
						}

						// Reset for next IP
						part1 = part2 = part3 = part4 = 0;
						partIndex = 0;
					}
				}
			}
			return uniqueCount;
		}
	}
}
