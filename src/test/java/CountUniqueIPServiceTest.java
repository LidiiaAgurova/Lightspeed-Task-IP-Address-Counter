import org.junit.jupiter.api.Test;
import utils.FileConstants;
import utils.IPGenerator;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CountUniqueIPServiceTest {

	@Test
	void shouldCountUniqueAddresses() throws IOException {
		IPGenerator.generateRandomIPs(FileConstants.FILE_NAME, FileConstants.TOTAL_IP_NUMBER, FileConstants.DUPLICATED_IP_NUMBER);
		long result = CountUniqueIPService.countUniqueIPs(FileConstants.FILE_NAME);

		assertEquals( 900, result);
	}
}
