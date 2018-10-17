package performance_analysis;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import input.Reader;
import output.Writer;
import process.Processor;
import textEntities.Comment;
import textEntities.Post;
import textEntities.TextEntity;
import textEntities.Top3;
import utils.Main;

public class Latency {

	public static void main(String[] args) {
		testPost(10, 20, 100);
		testComment(9, 20, 100);
	}

	/**
	 * This function tests the latency of a post in our program with a warm up at
	 * the beginnig. It prints the result in the console log.
	 * 
	 * @param i
	 *            the number of the file to consider
	 */
	public static void testPost(int i, int nbWarmUps, int nbIterations) {
		// Warm up
		for (int k = 0; k < nbWarmUps; k++) {
			Main.runTest(i);
		}

		// Iterations
		long somme = 0;
		long startTime = 0, endTime = 0;
		for (int k = 0; k < nbIterations; k++) {
			startTime = System.nanoTime();
			Main.runTest(13);
			endTime = System.nanoTime();
			somme += endTime - startTime;
		}
		long moyenne = somme / nbIterations;

		System.out.println("Moyenne latence post : " + (int) (moyenne * Math.pow(10, -3)) + "us");
	}

	/**
	 * This function tests the latency of a comment in our program with a warm up at
	 * the beginnig. It prints the result in the console log.
	 * 
	 * @param i
	 *            the number of the file to consider
	 */
	public static void testComment(int i, int nbWarmUps, int nbIterations) {
		// Warm up
		for (int k = 0; k < nbWarmUps; k++) {
			runTestComment(i);
		}

		// Iterations
		long somme = 0;
		for (int k = 0; k < nbIterations; k++) {
			somme += runTestComment(i);
		}
		long moyenne = somme / nbIterations;

		System.out.println("Moyenne latence comment : " + (int) (moyenne * Math.pow(10, -3)) + "us");
	}

	/**
	 * This function reads all the posts, and the it reads all the comments while
	 * measuring the time spent
	 * 
	 * @param i
	 *            the number of the file test to consider
	 * @return the time spent to process the comments
	 */
	public static long runTestComment(int i) {
		// Queues are declared
		Queue<TextEntity> postQueue = new ArrayBlockingQueue<TextEntity>(10000);
		Queue<TextEntity> commentQueue = new ArrayBlockingQueue<TextEntity>(10000);
		Queue<Top3> top3Queue = new ArrayBlockingQueue<Top3>(1000);

		// paths are declared and initialized
		String commentPath = "./src/main/resources/input_comments" + i + ".txt";
		String postPath = "./src/main/resources/input_posts" + i + ".txt";
		String outputPath = "./src/main/resources/output.txt";

		// Threads are declared and initialized
		Thread readerPostThread = new Thread(new Reader(postPath, postQueue, Post.class));
		Thread readerCommentThread = new Thread(new Reader(commentPath, commentQueue, Comment.class));
		Thread processorThread = new Thread(new Processor(postQueue, commentQueue, top3Queue));
		Thread writerThread = new Thread(new Writer(outputPath, top3Queue));

		// Threads are run
		readerPostThread.start();
		processorThread.start();
		writerThread.start();

		try {
			readerPostThread.join();
		} catch (InterruptedException e) {
			System.out.println("Unable to join");
			e.printStackTrace();
		}

		long startTime, endTime;
		startTime = System.nanoTime();
		readerCommentThread.start();
		try {
			writerThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		endTime = System.nanoTime();

		return endTime - startTime;
	}

}
