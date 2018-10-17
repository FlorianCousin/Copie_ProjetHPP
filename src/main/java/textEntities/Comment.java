package textEntities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class Comment extends TextEntity {

	Integer score;
	Long comment_id;
	// The id of the user who posted the comment
	Long user_id;
	// The post id associated with the comment
	Long post_id;
	// The comment replied id
	Long commentReplied_id;
	LocalDateTime timestamp;

	/**
	 * The constructor
	 * 
	 * @param comment_id
	 *            Long
	 * @param user_id
	 *            Long
	 * @param post_id
	 *            Long
	 * @param commentReplied_id
	 *            Long
	 * @param timestamp
	 *            LocalDateTime
	 */
	public Comment(Long comment_id, Long user_id, Long post_id, Long commentReplied_id, LocalDateTime timestamp) {
		this.score = 10;
		this.comment_id = comment_id;
		this.user_id = user_id;
		this.post_id = post_id;
		this.commentReplied_id = commentReplied_id;
		this.timestamp = timestamp;
	}

	/**
	 * The cloner
	 * 
	 * @param comment
	 *            the comment to clone
	 */
	public Comment(Comment comment) {
		this.score = new Integer(comment.getScore());
		this.comment_id = new Long(comment.getComment_id());
		this.user_id = new Long(comment.getUser_id());
		this.post_id = new Long(comment.getPost_id());
		this.commentReplied_id = new Long(-1);
		this.timestamp = comment.getTimestamp();
	}

	/* Getters */

	/**
	 * @return an Integer which is the score
	 */
	public Integer getScore() {
		return score;
	}

	/**
	 * @return an Integer wich is the comment_id
	 */
	public Long getComment_id() {
		return comment_id;
	}

	@Override
	public Long getEntity_id() {
		return comment_id;
	}

	/**
	 * @return an Integer which is the user_id
	 */
	public Long getUser_id() {
		return user_id;
	}

	/**
	 * @return an Integer which is the post_id
	 */
	public Long getPost_id() {
		return post_id;
	}

	/**
	 * @return the commentReplied_id
	 */
	public Long getCommentReplied_id() {
		return commentReplied_id;
	}

	/**
	 * @return a LocalDateTime which is the timestamp
	 */
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	/* Setters */

	/**
	 * Set the Integer post_id to a value
	 * 
	 * @param value
	 *            int to set in the post_id
	 */
	public void setPost_id(Long value) {
		this.post_id = value;
	}

	/**
	 * @param commentReplied_id
	 *            the commentReplied_id to set
	 */
	public void setCommentReplied_id(Long commentReplied_id) {
		this.commentReplied_id = commentReplied_id;
	}

	/* Other */

	/**
	 * Decrements the Integer representing the score by one
	 * 
	 * @return true if another event has to be set
	 */
	public boolean decrementScore() {
		if (this.score > 0) {
			return --this.score != 0;
		}
		return false;
	}

	/**
	 * Transform a string representing a Comment (as described in the subject) to a
	 * Comment
	 * 
	 * @param line
	 *            the string representing a Comment
	 * @return the Comment represented by the string line
	 */
	public static Comment stringToComment(String line) {
		String[] stringAttributes = line.split(Pattern.quote("|"));

		String data = stringAttributes[0];
		data = data.substring(0, data.length() - 5);
		LocalDateTime timestamp = LocalDateTime.parse(data, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));

		Long comment_id = Long.valueOf(stringAttributes[1]);

		Long user_id = Long.valueOf(stringAttributes[2]);

		Long commentReplied_id;

		Long post_id;

		if (stringAttributes[5].isEmpty()) {
			commentReplied_id = new Long(-1);
		} else {
			commentReplied_id = Long.valueOf(stringAttributes[5]);
		}

		if (stringAttributes.length == 6 || stringAttributes[6].isEmpty()) {
			post_id = new Long(-1);
		} else {
			post_id = Long.valueOf(stringAttributes[6]);
		}

		return new Comment(comment_id, user_id, post_id, commentReplied_id, timestamp);
	}

	/**
	 * Returns the poison pill
	 * 
	 * @return the poison pill
	 */
	public static Comment getPoisonPill() {
		return new Comment(new Long(-1), new Long(-1), new Long(-1), new Long(-1), LocalDateTime.MAX);
	}

}
