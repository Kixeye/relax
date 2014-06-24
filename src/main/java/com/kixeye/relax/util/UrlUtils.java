package com.kixeye.relax.util;

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
		
		int currentCount = 0;
		int lastIndex = 0;
		int currentIndex = 0;
		
		while ((currentIndex = template.indexOf("{}", lastIndex)) != -1) {
			writer.write(template.substring(lastIndex, currentIndex));
			
			if (currentCount < objects.length) {
				writer.write(URLEncoder.encode("" + objects[currentCount], StandardCharsets.UTF_8.name()));
				currentCount++;
			} else {
				writer.write("{}");
			}
			
			lastIndex = currentIndex + 2;
		}
		
		if (lastIndex < template.length()) {
			writer.write(template.substring(lastIndex));
		}
		
		return writer.toString();
	}
}
