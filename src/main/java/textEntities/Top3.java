package textEntities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Top3 {

	List<Post> currentTop3;
	LocalDateTime ts;

	/**
	 * @param currentTop3
	 * @param ts
	 */
	public Top3(List<Post> top3, LocalDateTime ts) {
		this.currentTop3 = top3;
		this.ts = ts;
	}

	/**
	 * @param ts
	 */
	public Top3(LocalDateTime ts) {
		this.currentTop3 = new ArrayList<Post>();
		this.ts = ts;
	}

	public Top3() {
		this.currentTop3 = new ArrayList<>();
		this.ts = LocalDateTime.MAX;
	}

	public Top3(Top3 top3) {
		this.currentTop3 = new ArrayList<Post>(top3.currentTop3.size());
		for (Post post : top3.currentTop3) {
			this.currentTop3.add(new Post(post));
		}
		this.ts = top3.ts;
	}

	/**
	 * @return the currentTop3
	 */
	public List<Post> getCurrentTop3() {
		return currentTop3;
	}

	/**
	 * @return the ts
	 */
	public LocalDateTime getTs() {
		return ts;
	}

	/**
	 * Return the poison pill
	 * 
	 * @return the poison pill
	 */
	public static Top3 getPoisonPill() {
		List<Post> poisonPills = new ArrayList<Post>(1);
		poisonPills.add(Post.getPoisonPill());
		return new Top3(poisonPills, LocalDateTime.MAX);
	}

	/**
	 * Return whether this is a poison pill or not
	 * 
	 * @return true if this is a poison pill and false otherwise
	 */
	public boolean isPoisonPill() {
		if (currentTop3.isEmpty()) {
			return false;
		}
		return currentTop3.get(0).isPoisonPill();
	}

	/**
	 * To get the output of the top 3 like described in the output specification:
	 * 
	 * <ts,top1_post_id,top1_post_user,top1_post_score,top1_post_commenters,
	 * top2_post_id,top2_post_user,top2_post_score,top2_post_commenters,
	 * top3_post_id,top3_post_user,top3_post_score,top3_post_commenters>
	 * 
	 * ts: the timestamp of the tuple event that triggers a change in the top-3
	 * scoring active posts appearing in the rest of the tuple topX_post_id: the
	 * unique id of the top-X post topX_post_user: the user author of top-X post
	 * topX_post_commenters: the number of commenters (excluding the post author)
	 * for the top-X post
	 * 
	 * @return the string description of the top 3
	 */
	@Override
	public String toString() {

		String ts = this.ts.toString();

		StringBuilder ans = new StringBuilder();

		ans.append(ts).append("+0000");

		// We add to ans the posts stored in top3sQueue
		for (Post p : this.currentTop3) {
			ans.append(',').append(p.toString());
		}

		// If there is less than 3 posts in top3sQueue, we fill ans by dashes
		for (int i = this.currentTop3.size(); i < 3; i++) {
			ans.append(",-,-,-,-");
		}

		return ans.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currentTop3 == null) ? 0 : currentTop3.hashCode());
		result = prime * result + ((ts == null) ? 0 : ts.hashCode());
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
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Top3)) {
			return false;
		}
		Top3 other = (Top3) obj;
		if (currentTop3 == null) {
			if (other.currentTop3 != null) {
				return false;
			}
		} else if (!currentTop3.equals(other.currentTop3)) {
			return false;
		}
		if (ts == null) {
			if (other.ts != null) {
				return false;
			}
		} else if (!ts.equals(other.ts)) {
			return false;
		}
		return true;
	}

}
