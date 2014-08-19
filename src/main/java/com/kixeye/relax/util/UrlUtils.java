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

import java.io.IOException;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * A simple URL template parser.
 * 
 * @author ebahtijaragic
 */
public final class UrlUtils {
	private UrlUtils() {}
	
	/**
	 * Expands the template.
	 * 
	 * @param objects
	 * @return
	 * @throws IOException 
	 */
	public static String expand(final String template, Object... objects) throws IOException {
		StringWriter writer = new StringWriter();

		int currentIndex = 0;
		int currentCount = 0;
		int lastIndex = 0;
		
		while ((currentIndex = template.indexOf("{", lastIndex)) != -1 && currentCount < objects.length) {
			writer.write(template.substring(lastIndex, currentIndex));
			
			if ((currentIndex = template.indexOf("}", currentIndex)) != -1) {
				writer.write(URLEncoder.encode("" + objects[currentCount], StandardCharsets.UTF_8.name()));
				currentCount++;
				lastIndex = currentIndex + 1;
			} else {
				break;
			}
		}
		
		if (lastIndex < template.length()) {
			writer.write(template.substring(lastIndex));
		}
		
		return writer.toString();
	}
}
