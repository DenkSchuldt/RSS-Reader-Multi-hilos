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
 * Created by denny on 06/07/15.
 */
public class BBCParser {

  private static final String ENTRY = "entry";
  private static final String TITLE = "title";
  private static final String DCIDENTIFIER = "dc:identifier";
  private static final String SUMMARY = "summary";
  private static final String LINK = "link";
  private static final String PUBLISHED = "published";

  public ArrayList<Feed> parse(InputStream inputStream) {
    ArrayList<Feed> feeds = new ArrayList<>();
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(new InputSource(inputStream));
      doc.getDocumentElement().normalize();
      NodeList items = doc.getElementsByTagName(ENTRY);
      for (int i = 0; i < items.getLength(); i++) {
        Feed feed = new Feed();
        NodeList childNodes = items.item(i).getChildNodes();
        for (int j=0; j<childNodes.getLength(); j++) {
          String content = childNodes.item(j).getTextContent();
          switch (childNodes.item(j).getNodeName()) {
            case TITLE:
              feed.setTitle(content);
              break;
            case DCIDENTIFIER:
              content = "http://www.bbc.com" + content;
              feed.setUrl(content);
              break;
            case SUMMARY:
              feed.setDescription(content);
              break;
            case LINK:
              NodeList deph1 = childNodes.item(j).getChildNodes();
              for (int a=0; a<deph1.getLength(); a++) {
                switch (deph1.item(a).getNodeName()) {
                  case "media:content":
                    NodeList deph2 = deph1.item(a).getChildNodes();
                    for (int b=0; b<deph2.getLength(); b++) {
                      switch (deph2.item(b).getNodeName()) {
                        case "media:thumbnail":
                          NodeList deph3 = deph2.item(b).getChildNodes();
                          for (int c=0; c<deph3.getLength(); c++) {
                            switch (deph3.item(c).getNodeName()) {
                              case "img":
                                String img = deph3.item(c).getAttributes().getNamedItem("src").getNodeValue();
                                feed.setImgurl(img);
                                break;
                            }
                          }
                          break;
                      }
                    }
                    break;
                }
              }
              break;
            case PUBLISHED:
              feed.setPubDate(content);
              break;
          }
        }
        feed.setSource(Constants.BBC);
        feeds.add(feed);
      }
    } catch (Exception e) {
      System.out.println(e);
    }
    return feeds;
  }

}

