package performance_analysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import utils.Main;

public class Throughput {

	public static void main(String[] args) {
		test(13, 20, 100);
//		test(14, 0, 1);
	}

	/**
	 * This function tests the throughput of our program with a warm up at the
	 * beginnig. It prints the result in the console log.
	 * 
	 * @param i
	 *          the number of the file to consider
	 */
	public static void test(int i, int nbWarmUps, int nbIterations) {
		// Warm up
		for (int k = 0; k < nbWarmUps; k++) {
			Main.runTest(i);
		}

		// Iterations
		long somme = 0;
		long startTime = 0, endTime = 0;
		for (int k = 0; k < nbIterations; k++) {
			startTime = System.nanoTime();
			Main.runTest(i);
			endTime = System.nanoTime();
			somme += endTime - startTime;
		}
		long moyenne = somme / nbIterations;

		int lines = nbPostsAndComments(i);

		System.out.println("Nombre d'entités de texte par test : " + lines);
		System.out.println("Moyenne de temps par iteration : " + (int) (moyenne * Math.pow(10, -6)) + "ms");
		System.out.println("Moyenne debit : " + (int) (lines / (moyenne * Math.pow(10, -9))) + " textEntity/s");
	}

	/**
	 * This function is supposed to count the number of lines in the files
	 * input_posts%i.txt and input_comments%i.txt stored in src/main/resources and
	 * where %i is the parameter of the function. The number of lines in these files
	 * corresponds to the number of posts and comments considered to calculate the
	 * throughput.
	 * 
	 * @param i
	 *          the number of the file to consider
	 * @return the number of posts and comments considered to calculate the
	 *         throughput
	 */
	public static int nbPostsAndComments(int i) {
		int lines = 0;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("./src/main/resources/input_posts" + i + ".txt"));
			while (reader.readLine() != null)
				lines++;
			reader.close();
			reader = new BufferedReader(new FileReader("./src/main/resources/input_comments" + i + ".txt"));
			while (reader.readLine() != null)
				lines++;
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

}
