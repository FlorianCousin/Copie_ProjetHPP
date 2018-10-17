package process;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import events.Event;
import events.SortedListEvents;
import events.StackOfEvent;
import textEntities.*;

public class Processor implements Runnable {

	Queue<TextEntity> postQueue;
	Queue<TextEntity> commentQueue;
	Queue<Top3> top3Queue;
	ListPosts posts;
	SortedListEvents events;
	Top3 currentTop3;

	/**
	 * The constructor
	 * 
	 * @param postQueue
	 * @param commentQueue
	 * @param top3Queue
	 */
	public Processor(Queue<TextEntity> postQueue, Queue<TextEntity> commentQueue, Queue<Top3> top3Queue) {
		this.postQueue = postQueue;
		this.commentQueue = commentQueue;
		this.top3Queue = top3Queue;
		this.posts = new ListPosts(1000000);
		this.events = new SortedListEvents();
		this.currentTop3 = new Top3();
	}

	@Override
	public void run() {
		TextEntity tmp;
		Event tmpEvent;
		tmp = pollEarlier();

		// While one of the postQueue or commentQueue is not finished
		while (!tmp.isPoisonPill()) {

			// While there are stacks of events to process before tmp, we process them and
			// we update top 3
			while (!events.isEmpty() && tmp.compareTsTo(events.get(0)) > 0) {
				processFirstEvent(true);
			}

			// While there are stacks of events to process at the same time as tmp, we
			// process them but we do not update top 3
			if (!events.isEmpty() && tmp.compareTsTo(events.get(0)) == 0) {
				processFirstEvent(false);
			}

			// We add the textEntity in the posts list
			tmpEvent = posts.addTextEntity(tmp);
			if (tmpEvent != null) {
				events.add(tmpEvent, tmp.getTimestamp().plusDays(1));
			}

			// We update the top 3 if it has changed
			updateTop3(tmp.getTimestamp());

			// We eventually take the next TextEntity
			tmp = pollEarlier();
		}

		// There is no more posts or comments but maybe there is events
		while (!events.isEmpty()) {
			processFirstEvent(true);
		}

		// We close the top3Queue
		Top3 poisonPill = Top3.getPoisonPill();
		synchronized (top3Queue) {
			top3Queue.add(poisonPill);
			top3Queue.notify();
		}

	}

	/**
	 * Process the first event of the event list. The boolean tsSameAsTextEntity is
	 * useful in case an event has the same timestamp as a textEntity. In that case,
	 * the top3 must not be updated because it will be done when we add the
	 * textEntity to the posts.
	 * 
	 * @param previousEvent
	 *            the previous event processed
	 * @param tsSameAsTextEntity
	 *            true if we may update currentTop3 and false otherwise
	 */
	private void processFirstEvent(boolean tsSameAsTextEntity) {
		// We remove the first event
		StackOfEvent currentSoe = events.remove(0);

		// We update the posts depending on the stack of events
		StackOfEvent tmpSoe = posts.update(currentSoe);

		// If there is another event to consider, i.e. if tmpSoe is not null, then we
		// add it in the events list
		if (tmpSoe != null) {
			events.add(tmpSoe);
		}

		// If we do not update top 3, then we have to stop here
		if (!tsSameAsTextEntity) {
			return;
		}

		// StackOfEvent has been created to avoid issues about top 3 updates whereas
		// another event at the same time will be processed just after. That is why we
		// may always check for update top3 here
		updateTop3(currentSoe.getTimeToChange());
	}

	/**
	 * This function checks whether the top 3 has changed. If so, it is added to the
	 * top3 queue
	 * 
	 * @param timestamp
	 *            the timestamp of the top 3, i.e. the timestamp of the last event
	 *            or addition of a textEntity
	 * @throws InterruptedException
	 *             if interrupted while waiting
	 */
	private void updateTop3(LocalDateTime timestamp) {
		List<Post> tmpTop3 = posts.getTop3();
		// We add a top3 in the queue only if the new top3 is not the same as before
		if (!tmpTop3.equals(currentTop3.getCurrentTop3())) {
			currentTop3 = new Top3(tmpTop3, timestamp);
			Top3 newTop3 = new Top3(currentTop3);
			synchronized (top3Queue) {
				try {
					((ArrayBlockingQueue<Top3>) top3Queue).put(newTop3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				top3Queue.notify();
			}
		}
	}

	/**
	 * This function works on the pipelines of posts and comments, takes the earlier
	 * textEntity and return it
	 * 
	 * @return the earlier textEntity in the pipelines we consume
	 */
	private TextEntity pollEarlier() {
		Post tmpPost;
		Comment tmpComment;
		TextEntity ans = null;

		try {

			// We wait for the postQueue not to be empty
			synchronized (postQueue) {
				tmpPost = (Post) postQueue.peek();
				while (tmpPost == null) {
					postQueue.wait();
					tmpPost = (Post) postQueue.peek();
				}
			}

			// We wait for the comment Queue not to be empty
			synchronized (commentQueue) {
				tmpComment = (Comment) commentQueue.peek();
				while (tmpComment == null) {
					commentQueue.wait();
					tmpComment = (Comment) commentQueue.peek();
				}
			}

			// We take the earlier textEntity between the post and the comment took just
			// before
			if (tmpPost.compareTsTo(tmpComment) <= 0) {
				synchronized (postQueue) {
					ans = postQueue.poll();
				}
			} else {
				synchronized (commentQueue) {
					ans = commentQueue.poll();
				}
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return ans;
	}

}
