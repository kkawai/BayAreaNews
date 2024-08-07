package com.kk.android.bayareanews.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.BaseColumns;

import com.kk.android.bayareanews.common.MLog;
import com.kk.android.bayareanews.common.StringUtil;
import com.kk.android.bayareanews.domain.model.Rss;

import java.util.ArrayList;
import java.util.List;

import static com.kk.android.bayareanews.data.local.RssLocalDbHelper.RssColumns.COL_AUTHOR;
import static com.kk.android.bayareanews.data.local.RssLocalDbHelper.RssColumns.COL_PUBLISHER;
import static com.kk.android.bayareanews.data.local.RssLocalDbHelper.RssColumns.COL_TITLE;

/**
 * @author kkawai
 * Migrated to Room database.
 * @see RoomDbKt
 *
 * After some time, this can be deleted, but for now, keep for migrating favorites from the
 * old non-room database to the new.
 */
@Deprecated
public final class RssLocalDbHelper {

    private static final String TAG = "RssDb";
    private static final int DB_VERSION = 9;
    private static RssLocalDbHelper instance;
    private static final String TABLE_RSS = "rss";
    private static final String TABLE_RSS_FAVORITES = "rss_favorites";

    private final DbOpenHelper sqlHelper;

    private RssLocalDbHelper(final Context context) {
        sqlHelper = new DbOpenHelper(context, null, null, DB_VERSION);
    }

    public synchronized static RssLocalDbHelper getInstance(Context context) {
        if (instance == null)
            instance = new RssLocalDbHelper(context);
        return instance;
    }

    public synchronized static void closeDb() {
        if (instance != null && instance.sqlHelper != null) {
            instance.sqlHelper.close();
            instance = null;
            MLog.i(TAG, "database closed");
        }
    }

    public static final class RssColumns implements BaseColumns {

        // no instances please
        private RssColumns() {
        }

        public static final String COL_ID = "id";

        public static final String COL_ARTICLE_ID = "article_id"; //custom for favorites
        public static final String COL_AUTHOR = "author";
        public static final String COL_CATEGORY = "category";
        public static final String COL_ORIGINAL_CATEGORY = "original_category";
        public static final String COL_DESCR = "descr";
        public static final String COL_IMAGE_URL = "image_url";
        public static final String COL_LINK = "link";
        public static final String COL_PUB_DATE = "pub_date";
        public static final String COL_PUBLISHER = "publisher";
        public static final String COL_TITLE = "title";
        public static final String COL_VIDEO_URL = "video_url";

        /**
         * The default sort order for this table
         */
        private static final String DEFAULT_SORT_ORDER = COL_PUB_DATE + " DESC";

    }

    public static String getDbName() {
        return "rss.db";
    }

    private class DbOpenHelper extends SQLiteOpenHelper {

        public DbOpenHelper(final Context context, final String name, final CursorFactory factory, final int version) {
            super(context, getDbName(), null, version);
        }

        @Override
        public void onCreate(final SQLiteDatabase db) {
            try {
                db.execSQL("CREATE TABLE " + TABLE_RSS + " (" + RssColumns.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + RssColumns.COL_AUTHOR + " TEXT, "
                        + RssColumns.COL_CATEGORY + " TEXT, "
                        + RssColumns.COL_ORIGINAL_CATEGORY + " TEXT, "
                        + RssColumns.COL_DESCR + " TEXT, " + RssColumns.COL_IMAGE_URL + " TEXT, "
                        + RssColumns.COL_LINK + " TEXT, "
                        + RssColumns.COL_PUB_DATE + " INTEGER DEFAULT 0, "
                        + COL_TITLE + " TEXT, "
                        + RssColumns.COL_VIDEO_URL + " TEXT);");
            } catch (final Throwable t) {
                MLog.e(TAG, "Error creating database.  Very bad: ", t);
            }
            updateOrCreateTables(db, 0, 0 );
        }

        private void updateOrCreateTables(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
            try {
                db.execSQL("ALTER TABLE " + TABLE_RSS + " ADD "
                        + RssColumns.COL_ORIGINAL_CATEGORY + " TEXT");
                MLog.i(TAG, TABLE_RSS + " table upgraded. onUpgrade() oldVersion=" +
                        oldVersion + " newVersion=" + newVersion);
            } catch (final Throwable t) {
                MLog.i(TAG, "updateOrCreateTables (a): " + t);
            }

            try {
                db.execSQL("ALTER TABLE " + TABLE_RSS + " ADD "
                        + RssColumns.COL_ARTICLE_ID + " TEXT");
                MLog.i(TAG, TABLE_RSS + " table upgraded. onUpgrade() oldVersion=" +
                        oldVersion + " newVersion=" + newVersion);
            } catch (final Throwable t) {
                MLog.i(TAG, "updateOrCreateTables (b): " + t);
            }

            //exact copy of TABLE_RSS.  needed since daily articles don't last.
            try {
                db.execSQL("CREATE TABLE " + TABLE_RSS_FAVORITES + " (" + RssColumns.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + RssColumns.COL_ARTICLE_ID + " TEXT, "
                        + RssColumns.COL_AUTHOR + " TEXT, "
                        + RssColumns.COL_CATEGORY + " TEXT, "
                        + RssColumns.COL_ORIGINAL_CATEGORY + " TEXT, "
                        + RssColumns.COL_DESCR + " TEXT, " + RssColumns.COL_IMAGE_URL + " TEXT, "
                        + RssColumns.COL_LINK + " TEXT, "
                        + RssColumns.COL_PUB_DATE + " INTEGER DEFAULT 0, "
                        + COL_TITLE + " TEXT, "
                        + RssColumns.COL_VIDEO_URL + " TEXT);");
            } catch (final Throwable t) {
                MLog.i(TAG, "updateOrCreateTables (c): " + t);
            }
            try {
                db.execSQL("ALTER TABLE " + TABLE_RSS + " ADD "
                        + RssColumns.COL_PUBLISHER + " TEXT");
                MLog.i(TAG, TABLE_RSS + " table upgraded. onUpgrade() oldVersion=" +
                        oldVersion + " newVersion=" + newVersion);
            } catch (final Throwable t) {
                MLog.i(TAG, "updateOrCreateTables (d): " + t);
            }

            try {
                db.execSQL("ALTER TABLE " + TABLE_RSS_FAVORITES + " ADD "
                        + RssColumns.COL_PUBLISHER + " TEXT");
                MLog.i(TAG, TABLE_RSS_FAVORITES + " table upgraded. onUpgrade() oldVersion=" +
                        oldVersion + " newVersion=" + newVersion);
            } catch (final Throwable t) {
                MLog.i(TAG, "updateOrCreateTables (e): " + t);
            }
        }

        @Override
        public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
            updateOrCreateTables(db, oldVersion, newVersion);
        }
    }

    /**
     * @param rss
     */
    private synchronized void insertRss(final Rss rss) {

        try {
            final SQLiteDatabase db = sqlHelper.getWritableDatabase();
            final long rowId = db.replace(TABLE_RSS, null, rss.getContentValues());
            MLog.i(TAG, "rss: " + rss + " inserted at " + rowId + " [" + rss.getCategory() + "]");
        } catch (Throwable t) {
            MLog.e(TAG, "Error in storing rss: ", t);
        }
    }

    public synchronized void insertRss(final String originalCategory, final List<Rss> rssList) {
        for (int i = 0; i < rssList.size(); i++) {
            final Rss rss = rssList.get(i);
            rss.setOriginalCategory(originalCategory);
            insertRss(rss);
        }
    }

    public synchronized int deleteAllRssFavorites() {
        try {
            final SQLiteDatabase db = sqlHelper.getWritableDatabase();
            final int rowsDeleted = db.delete(TABLE_RSS_FAVORITES, "1", null);
            return rowsDeleted;
        } catch (final Throwable t) {
            MLog.e(TAG, "Error in deleting all rss favorites: ", t);
        }
        return 0;
    }

    /**
     * @param rss
     */
    public synchronized long insertRssFavorite(final Rss rss) {

        if (getRssFavoriteByArticleId(rss.getArticleId()) != null) {
            return 0;  //already exists
        }
        long id = -1; //if returned, indicates error while inserting
        try {
            final SQLiteDatabase db = sqlHelper.getWritableDatabase();
            rss.setId(0);
            id = db.replace(TABLE_RSS_FAVORITES, null, rss.getContentValues());
            MLog.i(TAG, "rss favorite: " + rss + " inserted at " + id + " [" + rss.getCategory() + "]");
        } catch (Throwable t) {
            MLog.e(TAG, "Error in storing favorite rss: ", t);
        }
        return id;
    }

    public synchronized int deleteRssFavorite(String articleId) {

        if (getRssFavoriteByArticleId(articleId) == null) {
            return 0;  //does not exist to begin with
        }
        try {
            final SQLiteDatabase db = sqlHelper.getWritableDatabase();
            return db.delete(TABLE_RSS_FAVORITES, RssColumns.COL_ARTICLE_ID + "='" + articleId + "'", null);
        } catch (final Throwable t) {
            MLog.e(TAG, "Error deleting rss: ", t);
        }
        return -1;
    }

    public synchronized Rss getRssFavoriteByArticleId(final String articleId) {

        Rss rss = null;
        try {
            final SQLiteDatabase db = sqlHelper.getReadableDatabase();
            final String sql = String.format("select * from " + TABLE_RSS_FAVORITES
                    + " where " + RssColumns.COL_ARTICLE_ID + " = '" + articleId + "'");
            final Cursor c = db.rawQuery(sql, null);
            final ContentValues contentValues = new ContentValues();
            if (c.moveToNext()) {
                DatabaseUtils.cursorRowToContentValues(c, contentValues);
                rss = new Rss(contentValues);
            }
            c.close();
        } catch (Throwable t) {
            MLog.e(TAG, "Error in getting favorite rss: ", t);
        }
        return rss;
    }

    public synchronized List<Rss> searchRss(String searchTerm) {

        final List<Rss> rssList = new ArrayList<>();
        try {
            final SQLiteDatabase db = sqlHelper.getReadableDatabase();
            final String sql = "select * from " + TABLE_RSS
                    + " where " + COL_TITLE + " like '%" + searchTerm + "%'"
                    + " or " + COL_AUTHOR + " like '%" + searchTerm + "%'"
                    + " or " + COL_PUBLISHER + " = '" + searchTerm + "'"
                    + " order by " + RssColumns.DEFAULT_SORT_ORDER;
            final Cursor c = db.rawQuery(sql, null);
            final ContentValues contentValues = new ContentValues();
            while (c.moveToNext()) {
                DatabaseUtils.cursorRowToContentValues(c, contentValues);
                rssList.add(new Rss(contentValues));
            }
            c.close();
        } catch (Throwable t) {
            MLog.e(TAG, "Error in getting rss favorites: ", t);
        }
        return rssList;
    }

    public synchronized List<Rss> searchFavoriteRss(String searchTerm) {

        final List<Rss> rssList = new ArrayList<>();
        try {
            final SQLiteDatabase db = sqlHelper.getReadableDatabase();
            final String sql = "select * from " + TABLE_RSS_FAVORITES
                    + " where " + COL_TITLE + " like '%" + searchTerm + "%'"
                    + " or " + COL_AUTHOR + " like '%" + searchTerm + "%'"
                    + " or " + COL_PUBLISHER + " = '" + searchTerm + "'"
                    + " order by " + RssColumns.DEFAULT_SORT_ORDER;
            final Cursor c = db.rawQuery(sql, null);
            final ContentValues contentValues = new ContentValues();
            while (c.moveToNext()) {
                DatabaseUtils.cursorRowToContentValues(c, contentValues);
                rssList.add(new Rss(contentValues));
            }
            c.close();
        } catch (Throwable t) {
            MLog.e(TAG, "Error in getting rss favorites: ", t);
        }
        return rssList;
    }

    public synchronized List<Rss> getRssFavorites() {

        final List<Rss> rssList = new ArrayList<>();
        try {
            final SQLiteDatabase db = sqlHelper.getReadableDatabase();
            final String sql = String.format("select * from " + TABLE_RSS_FAVORITES + " order by " + RssColumns.DEFAULT_SORT_ORDER);
            final Cursor c = db.rawQuery(sql, null);
            final ContentValues contentValues = new ContentValues();
            while (c.moveToNext()) {
                DatabaseUtils.cursorRowToContentValues(c, contentValues);
                rssList.add(new Rss(contentValues));
            }
            c.close();
        } catch (Throwable t) {
            MLog.e(TAG, "Error in getting rss favorites: ", t);
        }
        return rssList;
    }

    public synchronized List<Rss> getRss(final String originalCategory) {

        final List<Rss> rssList = new ArrayList<>();
        try {
            final SQLiteDatabase db = sqlHelper.getReadableDatabase();
            //final SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
            //qb.setTables(TABLE_NAME);
            final String sql = String.format("select * from " + TABLE_RSS + "  order by " + RssColumns.DEFAULT_SORT_ORDER);
            final Cursor c = db.rawQuery(sql, null);
            final ContentValues contentValues = new ContentValues();
            while (c.moveToNext()) {
                DatabaseUtils.cursorRowToContentValues(c, contentValues);
                final Rss rss = new Rss(contentValues);
                if (rss.getOriginalCategory().equals(originalCategory))
                    rssList.add(rss);
            }
            c.close();
        } catch (Throwable t) {
            MLog.e(TAG, "Error in getting rss: ", t);
        }
        return rssList;
    }

    public synchronized int deleteRss(final String originalCategory) {

        final List<Rss> rssList = getRss(originalCategory);
        int deleteCount = 0;
        try {
            final SQLiteDatabase db = sqlHelper.getWritableDatabase();
            for (int i = 0; i < rssList.size(); i++) {
                final Rss rss = rssList.get(i);
                deleteCount += db.delete(TABLE_RSS, RssColumns.COL_ID + "=" + rss.getId(), null);
            }
        } catch (final Throwable t) {
            MLog.e(TAG, "Error deleting rss: ", t);
        }
        return deleteCount;
    }

    public synchronized List<String> getOriginalCategories() {

        final List<String> categories = new ArrayList<>();
        try {
            final SQLiteDatabase db = sqlHelper.getReadableDatabase();
            final SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
            qb.setTables(TABLE_RSS);
            final String sql = String.format("select distinct(%s) from %s",
                    RssColumns.COL_ORIGINAL_CATEGORY, TABLE_RSS);
            final Cursor c = db.rawQuery(sql, null);
            while (c.moveToNext()) {
                categories.add(StringUtil.unescapeQuotes(c.getString(0)));
            }
            c.close();
        } catch (Throwable t) {
            MLog.e(TAG, "Error in getting rss categories: ", t);
        }
        return categories;
    }
}
