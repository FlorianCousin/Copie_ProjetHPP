package textEntities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import events.Event;
import events.StackOfEvent;
import utils.SortedHashMap;

/**
 * ArrayList where posts are stored at post_id - 1.
 * 
 * @author Florian
 *
 */
public class ListPosts extends SortedHashMap<Long, Post> {

	private static final long serialVersionUID = 6665750070899850139L;

	/**
	 * The constructor without parameter
	 */
	public ListPosts() {
		super();
	}

	/**
	 * A constructor
	 * 
	 * @param initialCapacity
	 *            the initial length of the data
	 */
	public ListPosts(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * Update the posts depending on the events
	 * 
	 * @param soe
	 *            the stack of events to consider
	 * @return another StackOfEvent to consider later, or null if there is no events
	 *         to consider next
	 */
	public StackOfEvent update(StackOfEvent soe) {
		List<Event> soeCopy = new ArrayList<Event>(soe.size());
		Set<Long> keysToSort = new HashSet<Long>(soe.size());
		Map<Long, Short> outputUpdate = new HashMap<>(soe.size());

		for (Event event : soe) {
			outputUpdate.put(event.getPost_id(), update(event));
			if (outputUpdate.get(event.getPost_id()) == 0)
				soeCopy.add(event);
		}

		// The posts are sorted
		outputUpdate.forEach((key, value) -> {
			if (value != 2)
				keysToSort.add(key);
		});
		sort(keysToSort);

		if (soeCopy.isEmpty()) {
			return null;
		}

		return new StackOfEvent(soe.getTimeToChange().plusDays(1), soeCopy);
	}

	/**
	 * Update the posts depending on the event
	 * 
	 * @param event
	 *            the event to consider
	 * @return 0 if another event has to be set, 1 if no other event has to be set
	 *         but the post is not off and 2 if no other event has to be set and the
	 *         post is off
	 */
	private short update(Event event) {
		// If commentLocation is -1, then the event is associated with the post
		if (event.getCommentLocation() == -1) {
			boolean anotherEvent = this.get(event.getPost_id()).decrementScore();
			// If the post is dead, then we destroy it
			if (!anotherEvent && this.get(event.getPost_id()).IsOff()) {
				this.remove(event.getPost_id());
				return 2;
			}
			return (short) (anotherEvent ? 0 : 1);
		}
		// If commentLocation is not -1, then the event is associated with the comment
		else {
			Post postHasComment = this.get(event.getPost_id());
			int commentLocation = event.getCommentLocation();
			boolean anotherEvent = postHasComment.decrementCommentScore(commentLocation);
			// If the post is dead, then we destroy it
			if (!anotherEvent && this.get(event.getPost_id()).IsOff()) {
				this.remove(event.getPost_id());
				return 2;
			}
			return (short) (anotherEvent ? 0 : 1);
		}
	}

	/**
	 * Adds a textEntity to this
	 * 
	 * @param textEntity
	 *            the textEntity to add
	 * @return an event to add in the event List, and null if there is non event to
	 *         add (check the addComment documentation)
	 */
	public Event addTextEntity(TextEntity textEntity) {
		if (textEntity instanceof Post) {
			return addPost((Post) textEntity);
		} else {
			return addComment((Comment) textEntity);
		}
	}

	/**
	 * Adds a post to this
	 * 
	 * @param post
	 *            the post to add
	 * @return an event to add in the event List
	 */
	public Event addPost(Post post) {
		this.put(post.getPost_id(), post);
		sort(post.getPost_id());
		return new Event(post.getPost_id(), -1);
	}

	/**
	 * Add a comment in the correct post and returns an event to consider. If there
	 * is no event to consider, i.e. if the post is off, then returns null.
	 * 
	 * @param comment
	 *            the comment to consider
	 * @return an event to add in the event List, and null if there is no event to
	 *         add
	 * @throws IllegalArgumentException
	 */
	public Event addComment(Comment comment) throws IllegalArgumentException {

		// If the comment is a post reply, then we add it to the post
		if (comment.getCommentReplied_id() == -1) {
			Long post_id = comment.getPost_id();

			// If the post does not exists, we do not consider it
			if (!this.containsKey(post_id)) {
				return null;
			}

			int commentLocation = this.get(post_id).addComment(comment);
			if (!this.get(post_id).IsOff()) {
				sort(post_id);
				return new Event(post_id, commentLocation);
			} else {
				return null;
			}
		}
		// If the comment is a comment reply, then we have to find the post associated
		// and to add the comment to it
		else {
			for (Post post : this.values()) {
				if (post.hasComment(comment.getCommentReplied_id())) {
					comment.setPost_id(post.getPost_id());
					comment.setCommentReplied_id(new Long(-1));

					Long post_id = post.getPost_id();
					int commentLocation = this.get(post_id).addComment(comment);
					if (!post.IsOff()) {
						sort(post_id);
						return new Event(post_id, commentLocation);
					} else {
						return null;
					}
				}
			}
		}

		// If java comes here, it means the comment is a reply to another comment which
		// does not exist
		return null;
	}

	/**
	 * Return the top 3 posts of this considering any this
	 * 
	 * @return the top 3 posts
	 */
	public List<Post> getTop3() {
		List<Post> ans;

		// If there is no posts
		if (this.isEmpty()) {
			return new ArrayList<Post>();
		}

		// If there is only one post
		if (this.size() == 1) {
			ans = new ArrayList<Post>(1);
			ans.add((Post) this.getThValue(0));
			return ans;
		}

		// If there is two posts
		if (this.size() == 2) {
			ans = new ArrayList<Post>(2);
			ans.add(getThValue(0));
			ans.add(getThValue(1));
			return ans;
		}

		// If there is more than three posts
		ans = new ArrayList<Post>(3);
		ans.add(getThValue(0));
		ans.add(getThValue(1));
		ans.add(getThValue(2));
		return ans;
	}

}
