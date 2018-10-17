package utils;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

public class MainTest {

	@Test
	public final void testRunTest() {

		String expected = new String();
		String actual = new String();
		for (int i = 1; i < 14; i++) {
			Main.runTest(i);
			try {
				actual = new String(Files.readAllBytes(Paths.get("./src/main/resources/output.txt")));
				expected = new String(
						Files.readAllBytes(Paths.get("./src/main/resources/expected_output" + i + ".txt")));
			} catch (IOException e) {
				System.out.println("Error in iteration " + i);
				e.printStackTrace();
			}
			System.out.println(i);
			assertEquals(expected, actual);
		}
		for (int i = 14; i < 14; i++) {
			Main.runTest(i);
			System.out.println(i);
		}
	}

}
