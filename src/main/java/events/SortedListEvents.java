package events;

import java.time.LocalDateTime;
import utils.UnremovedList;

public class SortedListEvents extends UnremovedList<StackOfEvent> {

	/**
	 * Adds a stack of events in this data structure
	 * 
	 * @param soe
	 *            the stack of events to add
	 * @return true (as specified by Collection.add)
	 */
	@Override
	public boolean add(StackOfEvent soe) {
		int comparison;

		for (int i = 0; i < this.size(); i++) {
			comparison = soe.compareTsTo(this.get(i));

			if (comparison < 0) {
				this.add(i, soe);
				return true;
			}

			if (comparison == 0) {
				this.get(i).addAll(soe);
				return true;
			}
		}

		return super.add(soe);
	}

	/**
	 * Adds an event in this data structure
	 * 
	 * @param e
	 *            the event to add
	 * @param timeToChange
	 *            the timestamp of the event to add
	 * @return true (as specified by Collection.add)
	 */
	public boolean add(Event e, LocalDateTime timeToChange) {
		int comparison;

		for (int i = 0; i < this.size(); i++) {
			comparison = timeToChange.compareTo(this.get(i).getTimeToChange());

			if (comparison < 0) {
				this.add(i, new StackOfEvent(timeToChange, e));
				return true;
			}

			if (comparison == 0) {
				this.get(i).add(e);
				return true;
			}
		}

		return super.add(new StackOfEvent(timeToChange, e));
	}

}
