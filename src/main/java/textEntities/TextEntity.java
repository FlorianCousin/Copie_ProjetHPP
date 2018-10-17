package textEntities;

import java.time.LocalDateTime;

import events.StackOfEvent;

public abstract class TextEntity {

	public abstract Long getEntity_id();

	public abstract LocalDateTime getTimestamp();

	/**
	 * Transforms a string representing a TextEntity (as described in the subject)
	 * to the corresponding TextEntity
	 * 
	 * @param line
	 *            the String to consider
	 * @param clazz
	 *            the class of the object described by line
	 * @return the object described by line
	 */
	public static TextEntity stringToTextEntity(String line, Class<?> clazz) {
		if (clazz == Post.class) {
			return Post.stringToPost(line);
		} else {
			return Comment.stringToComment(line);
		}
	}

	/**
	 * Return the poison pill depending on the class we want it.
	 * 
	 * @param clazz
	 *            the class to consider
	 * @return the corresponding poison pill
	 */
	public static TextEntity getPoisonPill(Class<?> clazz) {
		if (clazz == Post.class) {
			return Post.getPoisonPill();
		} else {
			return Comment.getPoisonPill();
		}
	}

	/**
	 * Return whether this is a poison pill or not
	 * 
	 * @return true if this is a poison pill and false otherwise
	 */
	public boolean isPoisonPill() {
		return this.getEntity_id() == -1;
	}

	/**
	 * Compare two text entities. Actually it compares the timstamp of the events.
	 * Be careful because this comparator returns 0 if the timestamps are equals,
	 * the objects are not necessarily equals.
	 */
	public int compareTsTo(Object o) {
		if (o == null) {
			throw new IllegalArgumentException("The object compared must not be null");
		}
		if (!(o instanceof TextEntity || o instanceof StackOfEvent)) {
			throw new IllegalArgumentException("Only TextEnity and Event are comparable to TextEntity");
		}
		if (o instanceof TextEntity) {
			return this.getTimestamp().compareTo(((TextEntity) o).getTimestamp());
		}
		if (o instanceof StackOfEvent) {
			return this.getTimestamp().compareTo(((StackOfEvent) o).getTimeToChange());
		}
		// The function is not supposed to get there
		return -2;
	}

}
