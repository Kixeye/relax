package com.kixeye.relax.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the {@link UrlUtils}
 * 
 * @author ebahtijaragic
 */
public class UrlUtilsTest {
	@Test
	public void testNotTempletazied() throws Exception {
		final String template = "/blah";
		
		Assert.assertEquals("/blah", UrlUtils.expand(template));
	}

	@Test
	public void testOnce() throws Exception {
		final String template = "/blah/{}";
		
		Assert.assertEquals("/blah/once", UrlUtils.expand(template, "once"));
	}

	@Test
	public void testOnceWithOtherEnd() throws Exception {
		final String template = "/blah/{}/otherEnd";
		
		Assert.assertEquals("/blah/once/otherEnd", UrlUtils.expand(template, "once"));
	}

	@Test
	public void testTwice() throws Exception {
		final String template = "/blah/{}/{}";
		
		Assert.assertEquals("/blah/once/twice", UrlUtils.expand(template, "once", "twice"));
	}

	@Test
	public void testTwiceWithMid() throws Exception {
		final String template = "/blah/{}/mid/{}";
		
		Assert.assertEquals("/blah/once/mid/twice", UrlUtils.expand(template, "once", "twice"));
	}

	@Test
	public void testTwiceWithMidWithOtherEnd() throws Exception {
		final String template = "/blah/{}/mid/{}/otherEnd";
		
		Assert.assertEquals("/blah/once/mid/twice/otherEnd", UrlUtils.expand(template, "once", "twice"));
	}

	@Test
	public void testTwiceWithMidWithOtherEndWithParameters() throws Exception {
		final String template = "/blah/{}/mid/{}/otherEnd?param={}";
		
		Assert.assertEquals("/blah/once/mid/twice/otherEnd?param=parameters", UrlUtils.expand(template, "once", "twice", "parameters"));
	}

	@Test
	public void testWithNotEnoughtTokens() throws Exception {
		final String template = "/blah/{}/mid/{}";
		
		Assert.assertEquals("/blah/once/mid/{}", UrlUtils.expand(template, "once"));
	}
}
