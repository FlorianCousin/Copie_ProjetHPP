package utils;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import input.Reader;
import output.Writer;
import process.Processor;
import textEntities.*;

public class Main {

	public static void main(String[] args) {
		runTest(8);
	}

	public static void runTest(int i) {
		// Queues are declared
		Queue<TextEntity> postQueue = new ArrayBlockingQueue<TextEntity>(10000000);
		Queue<TextEntity> commentQueue = new ArrayBlockingQueue<TextEntity>(10000000);
		Queue<Top3> top3Queue = new ArrayBlockingQueue<Top3>(1000000);

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
		readerCommentThread.start();
		processorThread.start();
		writerThread.start();

		try {
			readerPostThread.join();
			readerCommentThread.join();
			processorThread.join();
			writerThread.join();
		} catch (InterruptedException e) {
			System.out.println("Unable to join");
			e.printStackTrace();
		}
	}

}
