package com.elobanova.websiteanalyzer.parser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.elobanova.websiteanalyzer.model.DocumentInfo;
import com.elobanova.websiteanalyzer.model.DocumentInfo.DocumentInfoBuilder;
import com.elobanova.websiteanalyzer.model.HeadingInfo;

/**
 * A parser class which delegates finding out all the needed information about
 * an HTML document to Jsoup implementation.
 * 
 * The following details can be retrieved:
 * What HTML version the document has.
 * What the page title is.
 * How many headings of what level there are in the document.
 * How many internal and external links there are in the document. 
 * How many inaccessible links there are in the document.
 * Whether the page contains a login-form.
 * 
 * @author Ekaterina Lobanova
 */
public class JsoupParser {
	public static final int HEADING_LEVELS_NUMBER_IN_HTML = 6;
	private static final String INPUT_TYPE_PASSWORD_QUERY = "input[type$=password]";
	private static final String FORM_TAG = "form";
	private static final String LINK_ATTRIBUTE_NAME = "href";
	private static final String HEADING_MARK = "h";
	private final String INTERNAL_LINK_QUERY = "a[" + LINK_ATTRIBUTE_NAME + "]";
	private final String EXTERNAL_LINK_QUERY = "link[" + LINK_ATTRIBUTE_NAME + "]";
	private final String PUBLIC_ID_KEY = "publicid";

	private final Document document;

	public JsoupParser(String url) throws IOException {
		this.document = Jsoup.connect(url).get();
	}

	/**
	 * A parsing procedure for an HTML document. The method builds an instance
	 * of a DocumentInfo and configures it with the parsing results.
	 * 
	 * @return an instance of DocumentInfo with all the properties set with
	 *         parsing results.
	 */
	public DocumentInfo parseDocument() {
		DocumentInfoBuilder documentInfoBuilder = new DocumentInfoBuilder();
		documentInfoBuilder.setTitle(parseTitle()).setHTMLVersion(parseHTMLVersion())
				.setNumberOfInternalLinks(parseNumberOfInternalLinks())
				.setNumberOfExternalLinks(parseNumberOfExternalLinks())
				.setNumberOfNotAccessibleLinks(parseNumberOfNotAccessableLinks())
				.setLoginFormPresent(parseLoginFormIsPresent()).setHeadings(parseHeadings());
		return documentInfoBuilder.build();
	}

	/**
	 * Constructs a collection with headings of all levels in the document.
	 * 
	 * @return a list of all possible headers with their frequency
	 */
	public List<HeadingInfo> parseHeadings() {
		List<HeadingInfo> headings = new ArrayList<>();

		StringBuilder fullQuery = new StringBuilder();
		for (int i = 0; i < HEADING_LEVELS_NUMBER_IN_HTML; i++) {
			int level = i + 1;

			fullQuery.append(HEADING_MARK + level);
			if (level != HEADING_LEVELS_NUMBER_IN_HTML) {
				fullQuery.append(", ");
			}
		}

		Elements hTags = document.select(fullQuery.toString());
		for (int i = 0; i < HEADING_LEVELS_NUMBER_IN_HTML; i++) {
			headings.add(getHeadingInfo(i + 1, hTags));
		}

		return headings;
	}

	private HeadingInfo getHeadingInfo(int level, Elements container) {
		String query = HEADING_MARK + level;
		Elements headingTags = container.select(query);
		HeadingInfo headingInfo = new HeadingInfo(query, headingTags.size());
		return headingInfo;
	}

	/**
	 * Detects if the HTML document has a login form. Assumption is that a login
	 * form is present if there is a "form" element with the "input" of type
	 * "password".
	 * 
	 * @return true if the document has a login form
	 */
	public boolean parseLoginFormIsPresent() {
		Elements forms = document.select(FORM_TAG);
		List<Element> formsWithPassword = forms.stream().filter(form -> isLoginForm(form)).collect(Collectors.toList());
		return formsWithPassword != null && formsWithPassword.size() != 0;
	}

	private boolean isLoginForm(Element form) {
		Elements passwordElements = form.select(INPUT_TYPE_PASSWORD_QUERY);
		return passwordElements.size() != 0;
	}

	/**
	 * Extracts a title of an HTML document.
	 * 
	 * @return a title of a document
	 */
	public String parseTitle() {
		return document.title();
	}

	/**
	 * Detects the internal links assuming that an internal link is given as
	 * <a>.
	 * 
	 * @return the number of internal links in the document
	 */
	public int parseNumberOfInternalLinks() {
		Elements internalLinks = document.select(INTERNAL_LINK_QUERY);
		return internalLinks.size();
	}

	/**
	 * Detects the external links assuming that an external link is given as
	 * <link>.
	 * 
	 * @return the number of external links in the document
	 */
	public int parseNumberOfExternalLinks() {
		Elements externalLinks = document.select(EXTERNAL_LINK_QUERY);
		return externalLinks.size();
	}

	/**
	 * Retrieves the information of HTML version as a standard of W3C.
	 * 
	 * @return an HTML version of the document
	 */
	public String parseHTMLVersion() {
		List<Node> childNodes = document.childNodes();
		Optional<Node> documentTypeNode = childNodes.stream().filter(node -> node instanceof DocumentType).findFirst();
		if (documentTypeNode.isPresent()) {
			DocumentType documentType = (DocumentType) documentTypeNode.get();
			return documentType.attr(PUBLIC_ID_KEY);
		}

		return null;
	}

	/**
	 * Retrieves a number of all inaccessible links in the document. A link is
	 * considered inaccessible if the connection times out, redirects, requires
	 * authorization or is not of HTTP type (e. g. a "mailto" will be treated as
	 * inaccessible link).
	 * 
	 * @return a number of all inaccessible links in the document
	 */
	public int parseNumberOfNotAccessableLinks() {
		Elements externalLinks = document.select(EXTERNAL_LINK_QUERY);
		Elements internalLinks = document.select(INTERNAL_LINK_QUERY);
		Elements allLinks = new Elements(externalLinks);
		allLinks.addAll(internalLinks);

		List<Element> result = allLinks.stream().filter(elementLink -> !isAccessable(elementLink))
				.collect(Collectors.toList());
		return result == null ? 0 : result.size();
	}

	private boolean isAccessable(Element elementLink) {
		String path = parseElementLinkPath(elementLink);
		try {
			HttpURLConnection.setFollowRedirects(false);
			URL linkURL = new URL(path);
			URLConnection urlConnection = linkURL.openConnection();
			if (urlConnection instanceof HttpURLConnection) {
				HttpURLConnection connection = (HttpURLConnection) urlConnection;
				connection.setRequestMethod("HEAD");
				int responseCode = connection.getResponseCode();
				return responseCode == HttpURLConnection.HTTP_OK;
			}

			return false;
		} catch (IOException e) {
			// log
			return false;
		}
	}

	private String parseElementLinkPath(Element elementLink) {
		if (elementLink != null) {
			String href = elementLink.attr(LINK_ATTRIBUTE_NAME);
			if (href != null && !href.isEmpty()) {
				return href;
			}
		}
		return null;
	}
}
