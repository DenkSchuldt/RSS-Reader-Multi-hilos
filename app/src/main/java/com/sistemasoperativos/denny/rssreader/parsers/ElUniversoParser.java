package com.sistemasoperativos.denny.rssreader.parsers;

import com.sistemasoperativos.denny.rssreader.models.Feed;
import com.sistemasoperativos.denny.rssreader.utils.Constants;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by denny on 05/07/15.
 */
public class ElUniversoParser {

  private static final String ITEM = "item";
  private static final String TITLE = "title";
  private static final String LINK = "link";
  private static final String DESCRIPTION = "description";
  private static final String ENCLOSURE = "enclosure";
  private static final String PUBDATE = "pubDate";

  public ArrayList<Feed> parse(InputStream inputStream) {
    ArrayList<Feed> feeds = new ArrayList<>();
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(new InputSource(inputStream));
      doc.getDocumentElement().normalize();
      NodeList items = doc.getElementsByTagName(ITEM);
      for (int i = 0; i < items.getLength(); i++) {
        Feed feed = new Feed();
        NodeList childNodes = items.item(i).getChildNodes();
        for (int j=0; j<childNodes.getLength(); j++) {
          String content = childNodes.item(j).getTextContent();
          switch (childNodes.item(j).getNodeName()) {
            case TITLE:
              feed.setTitle(content);
              break;
            case LINK:
              feed.setUrl(content);
              break;
            case DESCRIPTION:
              feed.setDescription(content);
              break;
            case ENCLOSURE:
              String url = childNodes.item(j).getAttributes().getNamedItem("url").getNodeValue();
              feed.setImgurl(url);
              break;
            case PUBDATE:
              feed.setPubDate(content);
              break;
          }
        }
        feed.setSource(Constants.ELUNIVERSO);
        feeds.add(feed);
      }
    } catch (Exception e) {
      System.out.println(e);
    }
    return feeds;
  }
}
