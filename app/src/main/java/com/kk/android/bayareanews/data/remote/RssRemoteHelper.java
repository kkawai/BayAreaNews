package com.kk.android.bayareanews.data.remote;

import com.kk.android.bayareanews.common.MLog;
import com.kk.android.bayareanews.common.StringUtil2;
import com.kk.android.bayareanews.common.TimeUtil;
import com.kk.android.bayareanews.domain.model.Rss;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 1/10/2016 ->  Updated 2023
 */
public class RssRemoteHelper {

   private static final String TAG = "RssReader";

   public List<Rss> getRss(final String urlToRssFeed) throws XmlPullParserException, MalformedURLException, IOException, Exception{
      final List<Rss> rssList = new ArrayList<>();
      InputStream is=null;

      try {
         final URL url = new URL(urlToRssFeed);

         XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
         factory.setNamespaceAware(false);
         XmlPullParser xpp = factory.newPullParser();

         // We will get the XML from an input stream
         xpp.setInput(is=getInputStream(url), "UTF_8");

        /* We will parse the XML content looking for the "<title>" tag which appears inside the "<item>" tag.
         * However, we should take in consideration that the rss feed name also is enclosed in a "<title>" tag.
         * As we know, every feed begins with these lines: "<channel><title>Feed_Name</title>...."
         * so we should skip the "<title>" tag which is a child of "<channel>" tag,
         * and take in consideration only "<title>" tag which is a child of "<item>"
         *
         * In order to achieve this, we will make use of a boolean variable.
         */
         boolean insideItem = false;
         Rss rss=null;
         // Returns the type of current event: START_TAG, END_TAG, etc..
         int eventType = xpp.getEventType();
         String publisher = "";
         while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {

               if (xpp.getName().equalsIgnoreCase("item")) {
                  insideItem = true;
                  rss = new Rss();
               } else if (xpp.getName().equalsIgnoreCase("title")) {
                  if (insideItem) {
                     String s = xpp.nextText();
                     //MLog.i(TAG, "title: " + s);
                     rss.setTitle(s);
                  }
               } else if (xpp.getName().equalsIgnoreCase("link")) {
                  if (insideItem) {
                     String s = xpp.nextText();
                     //MLog.i(TAG, "link: " + s);
                     rss.setLink(s);
                  } else {
                     if (publisher == null || publisher.isEmpty()) {
                        String link = xpp.nextText();
                        try {
                           publisher = StringUtil2.determinePublisher(link);
                        }catch (Exception e) {
                           MLog.e(TAG,"error parsing: "+link + " -> " +e);
                        }
                     }
                  }
               } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                  if (insideItem) {
                     String s = xpp.nextText();
                     //MLog.i(TAG, "pubDate: " + TimeUtil.RSS_DATE_FORMAT.parse(s) + " orig: "+s + " time ago: "+ TimeUtil.getTimeAgo(s));
                     try {
                        rss.setPubDate(TimeUtil.RSS_DATE_FORMAT.parse(s).getTime());
                     }catch (Exception e) {
                        MLog.e(TAG,"",e);
                     }
                  }
               } else if (xpp.getName().equalsIgnoreCase("dc:creator")) {
                  if (insideItem) {
                     String s = xpp.nextText();
                     //MLog.i(TAG, "creator: " + s);
                     rss.setAuthor(s);
                  }
               } else if (xpp.getName().equalsIgnoreCase("category")) {
                  if (insideItem) {
                     String s = xpp.nextText();
                     //MLog.i(TAG, "category: " + s);
                     if (rss.getCategory() == null)
                        rss.setCategory(s);
                  }
               } else if (xpp.getName().equalsIgnoreCase("media:category")) {
                  if (insideItem) {
                     String s = xpp.nextText();
                     //MLog.i(TAG, "category: " + s);
                     if (rss.getCategory() == null)
                        rss.setCategory(s);
                  }
               } else if (xpp.getName().equalsIgnoreCase("media:content")) {
                  if (insideItem) {
                     String contentUrl = xpp.getAttributeValue("", "url");
                     contentUrl = contentUrl != null ? contentUrl : "";
                     String medium = xpp.getAttributeValue("", "medium");
                     medium = medium != null ? medium : "";
                     if (medium.equalsIgnoreCase("image") && contentUrl.startsWith("http")) {
                        rss.setImageUrl(contentUrl);
                     } else if (medium.equalsIgnoreCase("video") && contentUrl.startsWith("http")) {
                        rss.setVideoUrl(contentUrl);
                     }
                  }
               } else if (xpp.getName().equalsIgnoreCase("description")) {
                  if (insideItem) {
                     String s = xpp.nextText();
                     //MLog.i(TAG, "description: " + s);
                     rss.setDescr(s);
                     /*
                     <div class="rss_thumbnail"><img src="http://www.wired.com/wp-content/uploads/2016/01/Internet-Explorer_1a-660x330.jpg" alt="The Sorry Legacy of Internet Explorer" /></div><p>Starting today, Microsoft will no longer support most versions of Internet Explorer, one of the most contentious pieces of software in history.</p> <p>The post <a rel="nofollow" href="http://www.wired.com/2016/01/the-sorry-legacy-of-microsoft-internet-explorer/">The Sorry Legacy of Internet Explorer</a> appeared first on <a rel="nofollow" href="http://www.wired.com">WIRED</a>.</p>
                      */
                  }
               } else if (xpp.getName().equalsIgnoreCase("media:thumbnail")) {
                  if (insideItem) {
//                     xpp.getAttributeValue("", "url");
//                     String s = xpp.nextText();
//                     int i = s.indexOf("url=\"");
//                     if (i != -1) {
//                        i = i + "url=\"".length();
//                        s = s.substring(i, s.indexOf("\"",i));
//                        rss.setImageUrl(s);
//                     }
                     String imageUrl = xpp.getAttributeValue(null, "url");
                     if (imageUrl != null && rss.getImageUrl() == null)
                        rss.setImageUrl(imageUrl);
                  }
               }

            } else if(eventType== XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")){
               insideItem=false;
               if (rss != null) {
                  rss.setArticleId(rss.getTitle().hashCode() + "-" + rss.getPubDate()); //generate article id
                  rss.setPublisher(publisher);
                  rssList.add(rss);
               }
               MLog.i(TAG,"rss: "+rss);
            }

            eventType = xpp.next(); //move to next element
         }

         MLog.i(TAG,"rss list size: "+rssList.size());

         return rssList;

      } finally {
         if (is != null) {
            try {
               is.close();
            }catch(final Exception e) {
               MLog.e(TAG,"",e);
            }
         }

      }
   }

   private InputStream getInputStream(final URL url) {
      try {
         return url.openConnection().getInputStream();
      } catch (IOException e) {
         MLog.e(TAG,"",e);
         return null;
      }
   }
}
