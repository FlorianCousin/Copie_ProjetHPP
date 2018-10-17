package textEntity;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import textEntities.*;

public class CommentTest {

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
		c3 = new Comment(new Long(3), new Long(2), new Long(2), new Long(-1), LocalDateTime.now());
		c4 = new Comment(new Long(4), new Long(1), new Long(3), new Long(-1), LocalDateTime.now());
		c5 = new Comment(new Long(5), new Long(2), new Long(1), new Long(-1), LocalDateTime.now());
		c6 = new Comment(new Long(6), new Long(3), new Long(-1), new Long(3), LocalDateTime.now());
	}

	@Test
	public final void testDecrementScore() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testStringToComment() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetPoisonPill() {
		fail("Not yet implemented"); // TODO
	}

}
