package textEntities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class Post extends TextEntity implements Comparable<Object> {

	Integer score;
	Long post_id;
	String user;
	Long user_id;
	List<Comment> comments;
	// The list of the commentors except the user of the post
	Set<Long> commenters;
	LocalDateTime timestamp;
	Integer totalScore;

	/**
	 * The constructor
	 * 
	 * @param post_id
	 *            Integer
	 * @param user
	 *            String
	 * @param timestamp
	 *            LocalDateTime
	 */
	public Post(Long post_id, String user, Long user_id, LocalDateTime timestamp) {
		this.score = 10;
		this.post_id = post_id;
		this.user = user;
		this.user_id = user_id;
		this.comments = new ArrayList<Comment>();
		this.commenters = new TreeSet<Long>();
		this.timestamp = timestamp;
		this.totalScore = this.score;
	}

	/**
	 * The cloner
	 */
	public Post(Post post) {
		this.score = new Integer(post.getScore());
		this.post_id = new Long(post.getPost_id());
		this.user = post.getUser();
		this.user_id = new Long(post.getUser_id());

		List<Comment> comments = post.getComments();
		this.comments = new ArrayList<Comment>(comments.size());
		comments.forEach(comment -> this.comments.add(new Comment(comment)));

		Set<Long> commenters = post.getCommenters();
		this.commenters = new TreeSet<Long>();
		commenters.forEach(commenter -> this.commenters.add(commenter));

		this.timestamp = post.getTimestamp();
		this.totalScore = new Integer(post.getTotalScore());
	}

	/* Getters */

	/**
	 * @return the score
	 */
	public Integer getScore() {
		return score;
	}

	/**
	 * @return the post_id
	 */
	public Long getPost_id() {
		return post_id;
	}

	@Override
	public Long getEntity_id() {
		return post_id;
	}

	/**
	 * @return the user name in a String
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @return the user_id of the post
	 */
	public Long getUser_id() {
		return user_id;
	}

	/**
	 * @return the comments of the post
	 */
	public List<Comment> getComments() {
		return comments;
	}

	/**
	 * @return the commentors
	 */
	public Set<Long> getCommenters() {
		return commenters;
	}

	/**
	 * @return the timestamp
	 */
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	/**
	 * To get the total score (i.e. considering the comments scores)
	 * 
	 * @return an Integer which is the total score of the post
	 */
	public Integer getTotalScore() {
		return this.totalScore;
	}

	/**
	 * This function returns the LocalDateTime of the latest comment in this post.
	 * If there is non comment in this post, then the function returns the minimum
	 * LocalDateTime.
	 * 
	 * @return the LocalDateTime of the latest comment
	 */
	public LocalDateTime getMaxTsComment() {
		LocalDateTime ans = LocalDateTime.MIN;

		for (Comment comment : comments) {
			if (comment.getTimestamp().isAfter(ans)) {
				ans = comment.getTimestamp();
			}
		}

		return ans;
	}

	/* Other */

	/**
	 * Return whether the post is off or not. A post is off if it has 0 as total
	 * score.
	 * 
	 * @return true if the post is off and false otherwise
	 */
	public boolean IsOff() {
		if (this.score > 0)
			return false;

		// Only the last comment (if it exists) is checked because the comments are
		// added in the list in timestamp order so the last comment is always the
		// comment with the higher score
		if (!comments.isEmpty() && comments.get(comments.size() - 1).getScore() > 0)
			return false;

		return true;
	}

	/**
	 * Decrements the Integer representing the score by one
	 * 
	 * @return true if another event has to be set
	 */
	public boolean decrementScore() {
		if (this.score > 0) {
			this.totalScore--;
			return --this.score != 0;
		}
		return false;
	}

	/**
	 * Decrements the Integer representing the score by one in the ith comment of
	 * the post
	 * 
	 * @param i
	 *            location of the comment
	 * @return true if another event has to be set
	 */
	public boolean decrementCommentScore(int i) {
		if (comments.get(i).getScore() > 0) {
			this.totalScore--;
		}
		return comments.get(i).decrementScore();
	}

	/**
	 * Add a comment to the post
	 * 
	 * @param comment
	 *            the comment to add
	 * @return the location of the comment in the comments List of the post
	 */
	public int addComment(Comment comment) {
		comments.add(comment);

		// Update the totalScore
		this.totalScore += comment.getScore();

		// We add the commenter if it is not the post owner
		if (!comment.getUser_id().equals(this.user_id)) {
			commenters.add(comment.getUser_id());
		}

		// The comment is at the location size - 1 in the comments List.
		return comments.size() - 1;
	}

	/**
	 * Test whether the post contains a comment with the comment_id comment_id
	 * 
	 * @param comment_id
	 *            the comment_id to consider
	 * @return true if the researched comment is in the post
	 */
	public boolean hasComment(Long commentReplied_id) {
		for (Comment comment : comments) {
			if (comment.getComment_id().equals(commentReplied_id)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return the string description of the post like described in the output
	 * specification.
	 * 
	 * @return a String like "post_id,user,score,post_commenters" where
	 *         post_commenters is the number of commenters.
	 */
	public String toString() {
		StringBuilder ans = new StringBuilder();

		// @formatter:off
		ans.append(post_id)
			.append(',')
			.append(user)
			.append(',')
			.append(totalScore)
			.append(',')
			.append(commenters.size());
		// @formatter:on

		return ans.toString();
	}

	/**
	 * Transforms a string representing a Post (as described in the subject) to a
	 * Post
	 * 
	 * @param line
	 *            the string representing a Post
	 * @return the Post represented by the string line
	 */
	public static Post stringToPost(String line) {
		String[] stringAttributes = line.split(Pattern.quote("|"));

		String data = stringAttributes[0];
		data = data.substring(0, data.length() - 5);
		LocalDateTime timestamp = LocalDateTime.parse(data, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));

		Long post_id = Long.valueOf(stringAttributes[1]);

		Long user_id = Long.valueOf(stringAttributes[2]);

		String user = stringAttributes[4];

		return new Post(post_id, user, user_id, timestamp);
	}

	/**
	 * Returns the poison pill
	 * 
	 * @return the poinson pill
	 */
	public static Post getPoisonPill() {
		return new Post(new Long(-1), "", new Long(-1), LocalDateTime.MAX);
	}

	/**
	 * A comparator of Post useful to get the top3 of the posts. The comparator is
	 * implemented as described in the project subject.
	 * 
	 * @return the value 0 if this Post is equal to the argument Post, the value -1
	 *         if this Post is less than the argument Post and the value 1 if this
	 *         Post is greater than the argument Post (signed comparison).
	 */
	@Override
	public int compareTo(Object o) {
		if (o == null) {
			throw new IllegalArgumentException("The object compared must not be null");
		}
		if (!(o instanceof Post)) {
			throw new IllegalArgumentException("Only TextEnity and Event are comparable to TextEntity");
		}

		Post other = (Post) o;
		short comparison;

		// We compare the total score
		comparison = (short) this.totalScore.compareTo(other.totalScore);
		if (comparison != 0)
			return comparison;

		// We compare the timetamps
		comparison = (short) this.getTimestamp().compareTo(other.getTimestamp());
		if (comparison != 0)
			return comparison;

		// We compare the timestamps of the latest comment
		comparison = (short) this.getMaxTsComment().compareTo(other.getMaxTsComment());
		if (comparison != 0)
			return comparison;

		// We eventually compare the post_id. It is not in the rules but it seems to
		// help
		comparison = (short) this.getPost_id().compareTo(other.getPost_id());

		return comparison;
	}

}
