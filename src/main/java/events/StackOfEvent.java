package events;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represtents some events at the same timestamp
 * 
 * @author Florian
 *
 */
public class StackOfEvent extends ArrayList<Event> {

	private static final long serialVersionUID = 1305115046009858169L;
	LocalDateTime timeToChange;

	public StackOfEvent(LocalDateTime timeToChange, List<Event> events) {
		super(events);
		this.timeToChange = timeToChange;
	}

	/**
	 * This constructor adds an event in this stack
	 * 
	 * @param timeToChange
	 * @param e
	 */
	public StackOfEvent(LocalDateTime timeToChange, Event e) {
		super(1);
		this.add(e);
		this.timeToChange = timeToChange;
	}

	/**
	 * @param timeToChange
	 * @param n
	 */
	public StackOfEvent(LocalDateTime timeToChange, Integer n) {
		super(n);
		this.timeToChange = timeToChange;
	}

	/**
	 * @param timeToChange
	 */
	public StackOfEvent(LocalDateTime timeToChange) {
		super();
		this.timeToChange = timeToChange;
	}

	/* Getters */

	/**
	 * @return the timeToChange
	 */
	public LocalDateTime getTimeToChange() {
		return timeToChange;
	}

	/* Other */

	/**
	 * This procedure adds 24 hours to the timestamp
	 */
	public void add24hours() {
		this.timeToChange.plusDays(1);
	}

	/**
	 * Compare two events. Actually it compares the timstamp of the events. Be
	 * careful because this comparator returns 0 if the timestamps are equals, the
	 * objects are not necessarily equals.
	 */
	public int compareTsTo(StackOfEvent o) {
		if (o == null) {
			throw new IllegalArgumentException("The object compared must not be null");
		}
		return this.timeToChange.compareTo(o.timeToChange);
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		this.forEach(event -> s.append(event.toString()).append("; "));
		s.append(timeToChange);
		return s.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((timeToChange == null) ? 0 : timeToChange.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof StackOfEvent)) {
			return false;
		}
		StackOfEvent other = (StackOfEvent) obj;
		if (timeToChange == null) {
			if (other.timeToChange != null) {
				return false;
			}
		} else if (!timeToChange.equals(other.timeToChange)) {
			return false;
		}
		return true;
	}

}
