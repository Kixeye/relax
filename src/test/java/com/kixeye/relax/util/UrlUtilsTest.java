package com.kixeye.relax.util;

/*
 * #%L
 * Relax
 * %%
 * Copyright (C) 2014 KIXEYE, Inc
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
	public void testWithNamedReplacements() throws Exception {
		final String template = "/blah/{something}/mid/{somethingElse}/otherEnd?param={yetAnotherThing}";
		
		Assert.assertEquals("/blah/once/mid/twice/otherEnd?param=parameters", UrlUtils.expand(template, "once", "twice", "parameters"));
	}

	@Test
	public void testWithNotEnoughtTokens() throws Exception {
		final String template = "/blah/{}/mid/{}";
		
		Assert.assertEquals("/blah/once/mid/{}", UrlUtils.expand(template, "once"));
	}
}
