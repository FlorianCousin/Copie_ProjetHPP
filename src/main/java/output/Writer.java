package output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Queue;

import textEntities.Top3;

public class Writer implements Runnable {

	String filePath;
	Queue<Top3> queue;

	/**
	 * @param queue
	 */
	public Writer(String filePath, Queue<Top3> queue) {
		this.filePath = filePath;
		this.queue = queue;
	}

	@Override
	public void run() {
		// Opening of the file
		File file = new File(this.filePath);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedWriter buffer;
		buffer = new BufferedWriter(fileWriter);

		Top3 tmp;
		boolean firstWrite = true; // true if it is the first write and false otherwise. This boolean is set to
									// manage the new lines in the output file.

		// Polling the queue and writing in the file
		try {
			tmp = waitAndPoll();

			// While tmp is not the poison pill
			while (!tmp.isPoisonPill()) {

				// If it is the first line, we do not add a new line at the beginning.
				if (firstWrite) {
					firstWrite = false;
				} else {
					buffer.newLine();
				}

				buffer.write(tmp.toString());

				tmp = waitAndPoll();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// We close everything at the end
			try {
				buffer.close();
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * This function is supposed to get the next Top3 in the Top3 queue. If the
	 * queue is empty, then this function wait for next Top3
	 * 
	 * @return the next Top3 in the queue.
	 */
	private Top3 waitAndPoll() {
		Top3 ans = null;

		try {
			synchronized (queue) {
				ans = queue.poll();
				while (ans == null) {
					queue.wait();
					ans = queue.poll();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return ans;
	}

}
