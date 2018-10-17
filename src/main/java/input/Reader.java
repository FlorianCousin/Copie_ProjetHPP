package input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import textEntities.TextEntity;

public class Reader implements Runnable {

	String filePath;
	Queue<TextEntity> queue;
	Class<?> clazz;

	/**
	 * @param filePath
	 * @param buffer
	 * @param queue
	 * @param events
	 */
	public Reader(String filePath, Queue<TextEntity> queue, Class<?> clazz) {
		this.filePath = filePath;
		this.queue = queue;
		this.clazz = clazz;
	}

	@Override
	public void run() {
		// Opening of the file
		File file = new File(this.filePath);
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		BufferedReader buffer;
		buffer = new BufferedReader(fileReader);

		String line;
		TextEntity newTextEntity;

		// Reading and fulfilling the queue
		try {
			line = buffer.readLine();

			while (line != null) {
				newTextEntity = TextEntity.stringToTextEntity(line, clazz);
				synchronized (queue) {
					((ArrayBlockingQueue<TextEntity>) queue).put(newTextEntity);
					queue.notify();
				}
				line = buffer.readLine();
			}

			newTextEntity = TextEntity.getPoisonPill(clazz);
			synchronized (queue) {
				((ArrayBlockingQueue<TextEntity>) queue).put(newTextEntity);
				queue.notify();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			// We close everything at the end
			try {
				buffer.close();
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
