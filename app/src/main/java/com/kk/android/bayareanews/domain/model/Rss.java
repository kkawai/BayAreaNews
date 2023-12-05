package com.kk.android.bayareanews.domain.model;

import android.content.ContentValues;
import android.database.DatabaseUtils;

import com.kk.android.bayareanews.common.StringUtil;
import com.kk.android.bayareanews.common.TimeUtil;
import com.kk.android.bayareanews.data.local.RssLocalDbHelper;

import java.util.Date;
import java.util.Random;

import androidx.annotation.Nullable;

/**
 * Created by kevin on 1/10/2016.
 */
public class Rss {

    private int id;
    private String title, link, descr, imageUrl, videoUrl, category, author, originalCategory, articleId;
    private long pubDate;//Sat, 07 Sep 2002 00:00:01 GMT

    private int minRead = new Random().nextInt(5) + 2;

    public static final String KEY_CATEGORY = "cat";
    public static final String KEY_ORIGINAL_CATEGORY = "o_cat";
    public static final String KEY_TITLE = "title";
    public static final String KEY_IMAGE = "image";

    public Rss() {
    }

    public Rss(final ContentValues contentValues) {
        final Integer id = contentValues.getAsInteger(RssLocalDbHelper.RssColumns.COL_ID);
        if (id != null) {
            setId(id);
        }
        setTitle(StringUtil.unescapeQuotes(contentValues.getAsString(RssLocalDbHelper.RssColumns.COL_TITLE)));
        setArticleId(contentValues.getAsString(RssLocalDbHelper.RssColumns.COL_ARTICLE_ID));
        setLink(contentValues.getAsString(RssLocalDbHelper.RssColumns.COL_LINK));
        setDescr(StringUtil.unescapeQuotes(contentValues.getAsString(RssLocalDbHelper.RssColumns.COL_DESCR)));
        setImageUrl(contentValues.getAsString(RssLocalDbHelper.RssColumns.COL_IMAGE_URL));
        setVideoUrl(contentValues.getAsString(RssLocalDbHelper.RssColumns.COL_VIDEO_URL));
        setCategory(StringUtil.unescapeQuotes(contentValues.getAsString(RssLocalDbHelper.RssColumns.COL_CATEGORY)));
        setOriginalCategory(StringUtil.unescapeQuotes(contentValues.getAsString(RssLocalDbHelper.RssColumns.COL_ORIGINAL_CATEGORY)));
        setAuthor(StringUtil.unescapeQuotes(contentValues.getAsString(RssLocalDbHelper.RssColumns.COL_AUTHOR)));
        setPubDate(contentValues.getAsLong(RssLocalDbHelper.RssColumns.COL_PUB_DATE));
    }

    public ContentValues getContentValues() {
        final ContentValues values = new ContentValues();
        if (id != 0) {
            values.put(RssLocalDbHelper.RssColumns.COL_ID, id);
        }
        values.put(RssLocalDbHelper.RssColumns.COL_TITLE, DatabaseUtils.sqlEscapeString(getTitle()));
        values.put(RssLocalDbHelper.RssColumns.COL_ARTICLE_ID, getArticleId());
        values.put(RssLocalDbHelper.RssColumns.COL_LINK, getLink());
        values.put(RssLocalDbHelper.RssColumns.COL_DESCR, DatabaseUtils.sqlEscapeString(getDescr()));
        values.put(RssLocalDbHelper.RssColumns.COL_IMAGE_URL, getImageUrl());
        values.put(RssLocalDbHelper.RssColumns.COL_VIDEO_URL, getVideoUrl());
        values.put(RssLocalDbHelper.RssColumns.COL_CATEGORY, DatabaseUtils.sqlEscapeString(getCategory()));
        values.put(RssLocalDbHelper.RssColumns.COL_ORIGINAL_CATEGORY, DatabaseUtils.sqlEscapeString(getOriginalCategory()));
        values.put(RssLocalDbHelper.RssColumns.COL_AUTHOR, DatabaseUtils.sqlEscapeString(getAuthor()));
        values.put(RssLocalDbHelper.RssColumns.COL_PUB_DATE, getPubDate());
        return values;
    }


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescr() {
        return descr;
    }

    private static String IMG_TAG = "<img src=";
    private static String P_TAG = "<p>";

    public void setDescr(final String descr) {
        if (descr.contains(IMG_TAG)) {
            int i = descr.indexOf(IMG_TAG);
            String x = descr.substring(i + IMG_TAG.length() + 1, descr.length());
            imageUrl = x.substring(0, x.indexOf('"'));
        }
        if (descr.contains(P_TAG)) {
            int i = descr.indexOf(P_TAG);
            String x = descr.substring(i + P_TAG.length(), descr.length());
            this.descr = x.substring(0, x.indexOf("</p>"));
        } else {
            this.descr = descr;
        }
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getPubDate() {
        return pubDate;
    }

    public void setPubDate(long pubDate) {
        this.pubDate = pubDate;
    }

    public String getTitle() {

        return title;
    }

    public String getAuthor() {
        return author != null ? author : "";
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeAgo() {
        if (pubDate == 0) return "now";
        return TimeUtil.getTimeAgo(new Date(pubDate));
    }

    @Override
    public String toString() {
        return "RSS. time ago: [" + getTimeAgo() + "] author: [" + author + "] title: [" + title + "] link: [" + link + "] descr: [" + descr + "] imageUrl: [" + imageUrl + "] videoUrl: [" + videoUrl + "] category: [" + category + "] pubDate: [" + pubDate + "]";
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getOriginalCategory() {
        return originalCategory;
    }

    public void setOriginalCategory(String originalCategory) {
        this.originalCategory = originalCategory;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        try {
            return ((Rss) obj).articleId.equals(articleId);
        }catch (Exception e) {
            return false;
        }
    }

    public String getMonthDayString() {
        if (pubDate != 0) {
            return StringUtil.DATE_AND_HOUR_FORMAT_3.format(new Date(pubDate));
        }
        return StringUtil.DATE_AND_HOUR_FORMAT_3.format(new Date());
    }

    public int getMinRead() {
        return minRead;
    }


    @Override
    public int hashCode() {
        return articleId != null ? articleId.hashCode() : 0;
    }
}