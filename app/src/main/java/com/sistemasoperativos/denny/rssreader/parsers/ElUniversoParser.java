package com.sistemasoperativos.denny.rssreader.parsers;

import com.sistemasoperativos.denny.rssreader.models.Entry;
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

  public ArrayList<Entry> parse(InputStream inputStream, String category) {
    ArrayList<Entry> entries = new ArrayList<>();
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(new InputSource(inputStream));
      doc.getDocumentElement().normalize();
      NodeList items = doc.getElementsByTagName(ITEM);
      for (int i = 0; i < items.getLength(); i++) {
        Entry entry = new Entry();
        NodeList childNodes = items.item(i).getChildNodes();
        for (int j=0; j<childNodes.getLength(); j++) {
          String content = childNodes.item(j).getTextContent();
          switch (childNodes.item(j).getNodeName()) {
            case TITLE:
              entry.setTitle(content);
              break;
            case LINK:
              entry.setUrl(content);
              break;
            case DESCRIPTION:
              entry.setDescription(content);
              break;
            case ENCLOSURE:
              String url = childNodes.item(j).getAttributes().getNamedItem("url").getNodeValue();
              entry.setImgurl(url);
              break;
            case PUBDATE:
              entry.setPubDate(content);
              break;
          }
        }
        entry.setSource(Constants.EL_UNIVERSO);
        entry.setCategory(category);
        entries.add(entry);
      }
    } catch (Exception e) {
      System.out.println(e);
    }
    return entries;
  }
}
