package textEntity;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import events.Event;
import textEntities.*;

public class ListPostsTest {

	ListPosts lp;

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
		lp = new ListPosts();

		p1 = new Post(new Long(1), "Michel", new Long(1), LocalDateTime.of(2018, 5, 18, 10, 22));
		p2 = new Post(new Long(2), "Paul", new Long(2), LocalDateTime.of(2018, 5, 18, 10, 22));
		p3 = new Post(new Long(3), "Michel", new Long(1), LocalDateTime.of(2018, 5, 22, 10, 22));
		p4 = new Post(new Long(4), "Albert", new Long(3), LocalDateTime.of(2018, 5, 22, 10, 23));
		p5 = new Post(new Long(5), "Michel", new Long(1), LocalDateTime.of(2018, 5, 23, 10, 22));

		c1 = new Comment(new Long(1), new Long(1), new Long(1), new Long(-1), LocalDateTime.of(2018, 5, 18, 10, 25));
		c2 = new Comment(new Long(2), new Long(1), new Long(2), new Long(-1), LocalDateTime.of(2018, 5, 21, 10, 25));
		c3 = new Comment(new Long(3), new Long(2), new Long(-1), new Long(2), LocalDateTime.of(2018, 5, 31, 10, 22));
		c4 = new Comment(new Long(4), new Long(1), new Long(3), new Long(-1), LocalDateTime.of(2018, 5, 27, 10, 22));
		c5 = new Comment(new Long(5), new Long(2), new Long(1), new Long(-1), LocalDateTime.of(2018, 5, 26, 10, 33));
		c6 = new Comment(new Long(6), new Long(3), new Long(-1), new Long(3), LocalDateTime.of(2018, 5, 27, 10, 26));
	}

	@Test
	public final void testUpdate() {
		// Event ep1;
		// Event ep2;
		// Event ec1;
		// Event ec5;
		//
		// ep1 = lp.addTextEntity(p1);
		// ep2 = lp.addTextEntity(p2);
		// ec1 = lp.addTextEntity(c1);
		//
		// assertEquals(10, (int) lp.get(new Long(2)).getTotalScore());
		// for (int i = 0; i < 9; i++) {
		// assertTrue(lp.update(ep2));
		// assertEquals(9 - i, (int) lp.get(new Long(2)).getTotalScore());
		// }
		// assertFalse(lp.update(ep2));
		//
		// assertEquals(20, (int) lp.get(new Long(1)).getTotalScore());
		// for (int i = 0; i < 9; i++) {
		// assertTrue(lp.update(ep1));
		// assertEquals(19 - i, (int) lp.get(new Long(1)).getTotalScore());
		// }
		// assertFalse(lp.update(ep1));
		// for (int i = 0; i < 9; i++) {
		// assertTrue(lp.update(ec1));
		// assertEquals(9 - i, (int) lp.get(new Long(1)).getTotalScore());
		// }
		// assertFalse(lp.update(ec1));
		//
		// ec5 = lp.addTextEntity(c5);
		// assertEquals(null, ec5);

		// TODO Not implemented. The code above tests updatePost and update of Event but
		// not update of StackOfEvent. Given update of Event is now private, the code
		// above is obsolete

	}

	@Test
	public final void testAddTextEntity() {
		assertEquals(0, lp.size());

		lp.addTextEntity(p1);
		assertEquals(1, lp.size());
		assertEquals(p1, lp.get(new Long(1)));

		lp.addComment(c1);
		assertEquals(1, lp.get(new Long(1)).getComments().size());

		lp.addTextEntity(p2);
		assertEquals(2, lp.size());
		assertEquals(p1, lp.get(new Long(1)));
		assertEquals(p2, lp.get(new Long(2)));

		lp.addComment(c2);
		assertEquals(1, lp.get(new Long(1)).getComments().size());
		assertEquals(1, lp.get(new Long(2)).getComments().size());

		lp.addTextEntity(p3);
		assertEquals(3, lp.size());
		assertEquals(p1, lp.get(new Long(1)));
		assertEquals(p2, lp.get(new Long(2)));
		assertEquals(p3, lp.get(new Long(3)));

		lp.addComment(c3);
		assertEquals(1, lp.get(new Long(1)).getComments().size());
		assertEquals(2, lp.get(new Long(2)).getComments().size());
		assertEquals(0, lp.get(new Long(3)).getComments().size());

		lp.addTextEntity(p4);
		assertEquals(4, lp.size());
		assertEquals(p1, lp.get(new Long(1)));
		assertEquals(p2, lp.get(new Long(2)));
		assertEquals(p3, lp.get(new Long(3)));
		assertEquals(p4, lp.get(new Long(4)));

		lp.addComment(c4);
		assertEquals(1, lp.get(new Long(1)).getComments().size());
		assertEquals(2, lp.get(new Long(2)).getComments().size());
		assertEquals(1, lp.get(new Long(3)).getComments().size());
		assertEquals(0, lp.get(new Long(4)).getComments().size());

		lp.addTextEntity(p5);
		assertEquals(5, lp.size());
		assertEquals(p1, lp.get(new Long(1)));
		assertEquals(p2, lp.get(new Long(2)));
		assertEquals(p3, lp.get(new Long(3)));
		assertEquals(p4, lp.get(new Long(4)));
		assertEquals(p5, lp.get(new Long(5)));

		lp.addComment(c5);
		assertEquals(2, lp.get(new Long(1)).getComments().size());
		assertEquals(2, lp.get(new Long(2)).getComments().size());
		assertEquals(1, lp.get(new Long(3)).getComments().size());
		assertEquals(0, lp.get(new Long(4)).getComments().size());
		assertEquals(0, lp.get(new Long(5)).getComments().size());

		lp.addComment(c6);
		assertEquals(2, lp.get(new Long(1)).getComments().size());
		assertEquals(3, lp.get(new Long(2)).getComments().size());
		assertEquals(1, lp.get(new Long(3)).getComments().size());
		assertEquals(0, lp.get(new Long(4)).getComments().size());
		assertEquals(0, lp.get(new Long(5)).getComments().size());

		// TODO : check if the events returned by the addTextEntity function are correct
	}

	@Test
	public final void testAddPost() {
		assertEquals(0, lp.size());

		lp.addPost(p1);
		assertEquals(1, lp.size());
		assertEquals(p1, lp.get(new Long(1)));

		lp.addPost(p2);
		assertEquals(2, lp.size());
		assertEquals(p1, lp.get(new Long(1)));
		assertEquals(p2, lp.get(new Long(2)));

		lp.addPost(p3);
		assertEquals(3, lp.size());
		assertEquals(p1, lp.get(new Long(1)));
		assertEquals(p2, lp.get(new Long(2)));
		assertEquals(p3, lp.get(new Long(3)));

		lp.addPost(p4);
		assertEquals(4, lp.size());
		assertEquals(p1, lp.get(new Long(1)));
		assertEquals(p2, lp.get(new Long(2)));
		assertEquals(p3, lp.get(new Long(3)));
		assertEquals(p4, lp.get(new Long(4)));

		lp.addPost(p5);
		assertEquals(5, lp.size());
		assertEquals(p1, lp.get(new Long(1)));
		assertEquals(p2, lp.get(new Long(2)));
		assertEquals(p3, lp.get(new Long(3)));
		assertEquals(p4, lp.get(new Long(4)));
		assertEquals(p5, lp.get(new Long(5)));

		// TODO : check if the events returned by the addPost function are correct
	}

	@Test
	public final void testAddComment() {
		lp.addPost(p1);
		lp.addPost(p2);
		lp.addPost(p3);
		lp.addPost(p4);
		lp.addPost(p5);

		lp.addComment(c1);
		assertEquals(1, lp.get(new Long(1)).getComments().size());
		assertEquals(0, lp.get(new Long(2)).getComments().size());
		assertEquals(0, lp.get(new Long(3)).getComments().size());
		assertEquals(0, lp.get(new Long(4)).getComments().size());
		assertEquals(0, lp.get(new Long(5)).getComments().size());

		lp.addComment(c2);
		assertEquals(1, lp.get(new Long(1)).getComments().size());
		assertEquals(1, lp.get(new Long(2)).getComments().size());
		assertEquals(0, lp.get(new Long(3)).getComments().size());
		assertEquals(0, lp.get(new Long(4)).getComments().size());
		assertEquals(0, lp.get(new Long(5)).getComments().size());

		lp.addComment(c3);
		assertEquals(1, lp.get(new Long(1)).getComments().size());
		assertEquals(2, lp.get(new Long(2)).getComments().size());
		assertEquals(0, lp.get(new Long(3)).getComments().size());
		assertEquals(0, lp.get(new Long(4)).getComments().size());
		assertEquals(0, lp.get(new Long(5)).getComments().size());

		lp.addComment(c4);
		assertEquals(1, lp.get(new Long(1)).getComments().size());
		assertEquals(2, lp.get(new Long(2)).getComments().size());
		assertEquals(1, lp.get(new Long(3)).getComments().size());
		assertEquals(0, lp.get(new Long(4)).getComments().size());
		assertEquals(0, lp.get(new Long(5)).getComments().size());

		lp.addComment(c5);
		assertEquals(2, lp.get(new Long(1)).getComments().size());
		assertEquals(2, lp.get(new Long(2)).getComments().size());
		assertEquals(1, lp.get(new Long(3)).getComments().size());
		assertEquals(0, lp.get(new Long(4)).getComments().size());
		assertEquals(0, lp.get(new Long(5)).getComments().size());

		lp.addComment(c6);
		assertEquals(2, lp.get(new Long(1)).getComments().size());
		assertEquals(3, lp.get(new Long(2)).getComments().size());
		assertEquals(1, lp.get(new Long(3)).getComments().size());
		assertEquals(0, lp.get(new Long(4)).getComments().size());
		assertEquals(0, lp.get(new Long(5)).getComments().size());

		// TODO : check if the events returned by the addComment function are correct
	}

	@Test
	public final void testGetTop3() {
		List<Post> expected;
		Event ep1;
		Event ep2;
		Event ec1;

		expected = new ArrayList<Post>();
		assertEquals(expected, lp.getTop3());

		ep1 = lp.addTextEntity(p1);
		expected.add(p1);
		assertEquals(expected, lp.getTop3());

		// lp.update(ep1);
		// assertEquals(expected, lp.getTop3());

		ep2 = lp.addTextEntity(p2);
		expected.add(0, p2);
		assertEquals(expected, lp.getTop3());

		lp.addTextEntity(p3);
		expected.add(0, p3);
		assertEquals(expected, lp.getTop3());

		// lp.update(ep2);
		// expected.remove(p2);
		// expected.add(p2);
		// assertEquals(expected, lp.getTop3());

		// lp.update(ep2);
		// expected.remove(p1);
		// expected.add(1, p1);
		// assertEquals(expected, lp.getTop3());

		lp.addTextEntity(p4);
		expected.remove(2);
		expected.add(0, p4);
		assertEquals(expected, lp.getTop3());

		ec1 = lp.addTextEntity(c1);
		expected.remove(2);
		expected.add(0, p1);
		assertEquals(expected, lp.getTop3());

		// lp.update(ec1);
		// assertEquals(expected, lp.getTop3());

		// TODO implement a test of getTop3 using update of StackOfEvent instead of
		// using update of Event
	}

	@Test
	public final void testSize() {

		assertEquals(0, lp.size());

		lp.addTextEntity(p1);
		assertEquals(1, lp.size());

		lp.addTextEntity(p2);
		assertEquals(2, lp.size());

		lp.addTextEntity(c1);
		assertEquals(2, lp.size());

		lp.addTextEntity(p3);
		assertEquals(3, lp.size());
	}

}
