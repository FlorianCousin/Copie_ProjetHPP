package textEntity;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import textEntities.Comment;
import textEntities.Post;

public class PostTest {

	Post p1;
	Post p2;
	Post p3;
	Post p4;
	Post p5;

	Comment c1;
	Comment c2;
	Comment c3;
	Comment c4;
	Comment c5;
	Comment c6;

	@Before
	public void setUp() throws Exception {

		p1 = new Post(new Long(1), "Michel", new Long(1), LocalDateTime.now());
		p2 = new Post(new Long(2), "Paul", new Long(2), LocalDateTime.now());
		p3 = new Post(new Long(3), "Michel", new Long(1), LocalDateTime.now());
		p4 = new Post(new Long(4), "Albert", new Long(3), LocalDateTime.now());
		p5 = new Post(new Long(5), "Michel", new Long(1), LocalDateTime.now());

		c1 = new Comment(new Long(1), new Long(1), new Long(1), new Long(-1), LocalDateTime.now());
		c2 = new Comment(new Long(2), new Long(1), new Long(2), new Long(-1), LocalDateTime.now());
		c3 = new Comment(new Long(3), new Long(2), new Long(-1), new Long(2), LocalDateTime.now());
		c4 = new Comment(new Long(4), new Long(1), new Long(3), new Long(-1), LocalDateTime.now());
		c5 = new Comment(new Long(5), new Long(2), new Long(1), new Long(-1), LocalDateTime.now());
		c6 = new Comment(new Long(6), new Long(3), new Long(-1), new Long(3), LocalDateTime.now());

	}

	@Test
	public final void testGetCommenters() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetTotalScore() {

		assertEquals(10, (int) p1.getTotalScore());
		p1.addComment(c1);
		assertEquals(20, (int) p1.getTotalScore());
		p1.decrementScore();
		assertEquals(19, (int) p1.getTotalScore());
		p1.decrementScore();
		assertEquals(18, (int) p1.getTotalScore());
		p1.decrementCommentScore(0);
		assertEquals(17, (int) p1.getTotalScore());
		p1.addComment(c5);
		assertEquals(27, (int) p1.getTotalScore());
		p1.decrementCommentScore(0);
		assertEquals(26, (int) p1.getTotalScore());
		p1.decrementCommentScore(1);
		assertEquals(25, (int) p1.getTotalScore());
		p1.decrementScore();
		assertEquals(24, (int) p1.getTotalScore());
	}

	@Test
	public final void testIsOff() {

		assertFalse(p2.IsOff());
		for (int i = 0; i < 9; i++) {
			p2.decrementScore();
			assertFalse(p2.IsOff());
		}
		p2.decrementScore();
		assertTrue(p2.IsOff());

		assertFalse(p1.IsOff());
		for (int i = 0; i < 9; i++) {
			p1.decrementScore();
			assertFalse(p1.IsOff());
		}
		p1.addComment(c1);
		assertFalse(p1.IsOff());
		p1.decrementCommentScore(0);
		assertFalse(p1.IsOff());
		p1.decrementScore();
		assertEquals(0, (int) p1.getScore());
		assertFalse(p1.IsOff());
		for (int i = 0; i < 8; i++) {
			p1.decrementCommentScore(0);
			assertFalse(p1.IsOff());
		}
		p1.decrementCommentScore(0);
		assertTrue(p1.IsOff());

	}

	@Test
	public final void testDecrementScore() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testDecrementCommentScore() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testAddComment() {

		assertEquals(0, p1.getComments().size());
		assertEquals(0, p1.getCommenters().size());
		p1.addComment(c1);
		assertEquals(1, p1.getComments().size());
		assertEquals(0, p1.getCommenters().size());
		assertEquals(c1, p1.getComments().get(0));

		assertEquals(0, p2.getComments().size());
		assertEquals(0, p2.getCommenters().size());
		p2.addComment(c2);
		assertEquals(1, p2.getComments().size());
		assertEquals(1, p2.getCommenters().size());
		assertEquals(c2, p2.getComments().get(0));
		assertTrue(p2.getCommenters().contains(new Long(1)));

		assertEquals(0, p3.getComments().size());
		assertEquals(0, p3.getCommenters().size());
		p3.addComment(c4);
		assertEquals(1, p3.getComments().size());
		assertEquals(0, p3.getCommenters().size());
		assertEquals(c4, p3.getComments().get(0));

		assertEquals(1, p1.getComments().size());
		assertEquals(0, p1.getCommenters().size());
		p1.addComment(c5);
		assertEquals(2, p1.getComments().size());
		assertEquals(1, p1.getCommenters().size());
		assertEquals(c5, p1.getComments().get(1));
		assertTrue(p1.getCommenters().contains(new Long(2)));
	}

	@Test
	public final void testHasComment() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testToString() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testStringToPost() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetPoisonPill() {
		fail("Not yet implemented"); // TODO
	}

}
