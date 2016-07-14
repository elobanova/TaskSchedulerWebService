package com.elobanova.websiteanalyzer.parser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
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

public class JsoupParser {
	private static final String LINK_ATTRIBUTE_NAME = "href";
	private final String INTERNAL_LINK_QUERY = "a[" + LINK_ATTRIBUTE_NAME + "]";
	private final String EXTERNAL_LINK_QUERY = "link[" + LINK_ATTRIBUTE_NAME + "]";
	private final String PUBLIC_ID_KEY = "publicid";

	private final Document document;

	public JsoupParser(String url) throws IOException {
		this.document = Jsoup.connect(url).get();
	}

	public DocumentInfo parseDocument() {
		DocumentInfoBuilder documentInfoBuilder = new DocumentInfoBuilder();
		documentInfoBuilder.setTitle(parseTitle()).setHTMLVersion(parseHTMLVersion())
				.setNumberOfInternalLinks(parseNumberOfInternalLinks())
				.setNumberOfExternalLinks(parseNumberOfExternalLinks())
				.setNumberOfNotAccessibleLinks(parseNumberOfNotAccessableLinks());
		return documentInfoBuilder.build();
	}

	public String parseTitle() {
		return document.title();
	}

	public int parseNumberOfInternalLinks() {
		Elements internalLinks = document.select(INTERNAL_LINK_QUERY);
		return internalLinks.size();
	}

	public int parseNumberOfExternalLinks() {
		Elements externalLinks = document.select(EXTERNAL_LINK_QUERY);
		return externalLinks.size();
	}

	public String parseHTMLVersion() {
		List<Node> childNodes = document.childNodes();
		Optional<Node> documentTypeNode = childNodes.stream().filter(node -> node instanceof DocumentType).findFirst();
		if (documentTypeNode.isPresent()) {
			DocumentType documentType = (DocumentType) documentTypeNode.get();
			return documentType.attr(PUBLIC_ID_KEY);
		}

		return null;
	}

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
