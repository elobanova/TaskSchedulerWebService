package com.elobanova.websiteanalyzer.parser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * A utility class to for networking operations.
 * 
 * @author Ekaterina Lobanova
 *
 */
public class NetworkUtils {

	/**
	 * A method to check if the link is valid. A link is considered invalid if
	 * the connection times out, redirects, requires authorization or is not of
	 * HTTP type (e. g. a "mailto" will be treated as inaccessible link).
	 * 
	 * @param path
	 *            a link to check
	 * @return an instance of a Connection with details about its status
	 */
	public static Connection checkURL(String path) {
		Connection info = new Connection();
		HttpURLConnection connection = null;
		try {
			HttpURLConnection.setFollowRedirects(false);
			URL linkURL = new URL(path);
			URLConnection urlConnection = linkURL.openConnection();
			if (urlConnection instanceof HttpURLConnection) {
				connection = (HttpURLConnection) urlConnection;
				connection.setRequestMethod("HEAD");
				int responseCode = connection.getResponseCode();
				info.setStatusCode(responseCode);
				info.setValid(responseCode == HttpURLConnection.HTTP_OK);
			}
		} catch (IOException e) {
			info.setValid(false);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return info;
	}
}
