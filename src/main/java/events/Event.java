package events;

public class Event {

	Long post_id;
	// commentLocation is not the comment_id, it is the location of the comment in
	// the comments of the post. If commentLocation is to -1, then the event is
	// associated to the post.
	Integer commentLocation;

	/**
	 * @param post_id
	 * @param commentLocation
	 * @param timeToChange
	 */
	public Event(Long post_id, Integer commentLocation) {
		this.post_id = post_id;
		this.commentLocation = commentLocation;
	}

	/* Getters */

	/**
	 * @return the post_id
	 */
	public Long getPost_id() {
		return post_id;
	}

	/**
	 * @return the commentLocation
	 */
	public Integer getCommentLocation() {
		return commentLocation;
	}

	// TODO toString can be suppressed because it is only useful to tests
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(post_id).append(", ").append(commentLocation);
		return s.toString();
	}

}
