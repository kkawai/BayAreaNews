package com.kk.android.bayareanews.data.local

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kk.android.bayareanews.MainApp
import com.kk.android.bayareanews.domain.model.Rss
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.LinkedList

@Entity(tableName = "rss")
data class RssEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "author") val author: String?,
    @ColumnInfo(name = "category") val category: String?,
    @ColumnInfo(name = "original_category") val originalCategory: String?,
    @ColumnInfo(name = "descr") val descr: String?,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    @ColumnInfo(name = "link") val link: String?,
    @ColumnInfo(name = "pub_date") val pubDate: Long? = 0,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "video_url") val videoUrl: String?,
    @ColumnInfo(name = "article_id") val articleId: String?,
    @ColumnInfo(name = "publisher") val publisher: String?,
)

@Entity(tableName = "rss_favorites")
data class RssFavoriteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "author") val author: String?,
    @ColumnInfo(name = "category") val category: String?,
    @ColumnInfo(name = "original_category") val originalCategory: String?,
    @ColumnInfo(name = "descr") val descr: String?,
    @ColumnInfo(name = "link") val link: String?,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    @ColumnInfo(name = "pub_date") val pubDate: Long? = 0,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "video_url") val videoUrl: String?,
    @ColumnInfo(name = "article_id") val articleId: String?,
    @ColumnInfo(name = "publisher") val publisher: String?,
)

@Dao
interface UserDao {

    @Insert
    fun insertRss(vararg rss: RssEntity)

    @Query("DELETE FROM rss")
    fun deleteAll()

    @Insert
    fun insertRssFavorite(vararg rssFavorite: RssFavoriteEntity)

    @Query("DELETE FROM rss_favorites where article_id = :articleId")
    fun deleteRssFavorite(vararg articleId: String)

    @Query("DELETE FROM rss where original_category = :originalCategory")
    fun deleteRss(vararg originalCategory: String)

    @Query("SELECT count(*) FROM rss_favorites WHERE article_id = :articleId")
    fun getRssFavoriteCountByArticleId(vararg articleId: String): Int

    @Query("SELECT * FROM rss_favorites WHERE article_id = :articleId")
    fun getRssFavoriteByArticleId(vararg articleId: String): RssFavoriteEntity

    @Query(
        "SELECT * FROM rss WHERE title LIKE :searchTerm or " +
        " author like :searchTerm or " +
        " publisher like :searchTerm " +
        " order by pub_date desc"
         )
    fun searchRss(searchTerm: String): List<RssEntity>

    @Query(
        "SELECT * FROM rss_favorites WHERE title LIKE :searchTerm or " +
                " author like :searchTerm or " +
                " publisher like :searchTerm " +
                " order by pub_date desc"
    )
    fun searchRssFavorites(searchTerm: String): List<RssFavoriteEntity>

    @Query("SELECT * FROM rss WHERE original_category = :originalCategory order by pub_date desc")
    fun getRss(originalCategory: String): List<RssEntity>

    @Query("SELECT * FROM rss_favorites order by pub_date desc")
    fun getFavoriteRss(): List<RssFavoriteEntity>
}

private val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            delay(3000)
            val oldRssFavorites = RssLocalDbHelper.getInstance(MainApp.app).rssFavorites
            for (oldRssFavorite in oldRssFavorites) {
                RssLocalDbHelper2.insertRssFavorite(MainApp.app, oldRssFavorite)
            }
            RssLocalDbHelper.getInstance(MainApp.app).deleteAllRssFavorites()
        }
    }
}

@Database(entities = [RssEntity::class,RssFavoriteEntity::class], version = 5)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

object RssLocalDbHelper2 {

    private lateinit var db : AppDatabase

    private fun getDb(context: Context): AppDatabase {
        synchronized(this@RssLocalDbHelper2) {
            if (::db.isInitialized) {
                return db
            } else {
                db = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java, "rss4.db"
                ).addMigrations(MIGRATION_4_5)
                 .build()
                return db
            }
        }
    }

    private fun rssEntityFromRss(rss: Rss): RssEntity {
        return RssEntity(
            id = rss.id,
            author = rss.author,
            category = rss.category,
            originalCategory = rss.originalCategory,
            descr = rss.descr,
            link = rss.link,
            pubDate = rss.pubDate,
            title = rss.title,
            videoUrl = rss.videoUrl,
            articleId = rss.articleId,
            publisher = rss.publisher,
            imageUrl = rss.imageUrl,
        )
    }

    private fun rssFromRssEntity(rssEntity: RssEntity): Rss {
        val rss = Rss()
        rss.id = rssEntity.id
        rss.author = rssEntity.author
        rss.category = rssEntity.category
        rss.originalCategory = rssEntity.originalCategory
        rss.descr = rssEntity.descr
        rss.link = rssEntity.link
        rss.pubDate = rssEntity.pubDate!!
        rss.title = rssEntity.title
        rss.videoUrl = rssEntity.videoUrl
        rss.articleId = rssEntity.articleId
        rss.publisher = rssEntity.publisher
        return rss
    }

    private fun rssFromRssFavoriteEntity(rssFavoriteEntity: RssFavoriteEntity): Rss {
        val rss = Rss()
        rss.id = rssFavoriteEntity.id
        rss.author = rssFavoriteEntity.author
        rss.category = rssFavoriteEntity.category
        rss.originalCategory = rssFavoriteEntity.originalCategory
        rss.descr = rssFavoriteEntity.descr
        rss.link = rssFavoriteEntity.link
        rss.pubDate = rssFavoriteEntity.pubDate!!
        rss.title = rssFavoriteEntity.title
        rss.videoUrl = rssFavoriteEntity.videoUrl
        rss.articleId = rssFavoriteEntity.articleId
        rss.publisher = rssFavoriteEntity.publisher
        rss.imageUrl = rssFavoriteEntity.imageUrl
        return rss
    }

    private fun rssFavoriteEntityFromRss(rss: Rss): RssFavoriteEntity {
        return RssFavoriteEntity(
            id = rss.id,
            author = rss.author,
            category = rss.category,
            originalCategory = rss.originalCategory,
            descr = rss.descr,
            link = rss.link,
            pubDate = rss.pubDate,
            title = rss.title,
            videoUrl = rss.videoUrl,
            articleId = rss.articleId,
            publisher = rss.publisher,
            imageUrl = rss.imageUrl,
        )
    }

    fun insertRss(context: Context, originalCategory: String, rssList: List<Rss>) {
        val db = getDb(context)
        for (rss in rssList) {
            rss.originalCategory = originalCategory
            db.userDao().insertRss(rssEntityFromRss(rss))
        }
    }

    fun insertRssFavorite(context: Context, rss: Rss) {
        val db = getDb(context)
        if (db.userDao().getRssFavoriteCountByArticleId(rss.articleId) > 0) {
            return
        }
        db.userDao().insertRssFavorite(rssFavoriteEntityFromRss(rss))
    }

    fun deleteRssFavorite(context: Context, articleId: String): Int {
        val db = getDb(context)
        if (db.userDao().getRssFavoriteCountByArticleId(articleId) == 0) {
            return 0
        }
        db.userDao().deleteRssFavorite(articleId)
        return 1
    }

    fun getRssFavoriteByArticleId(context: Context, articleId: String): Rss {
        val db = getDb(context)
        if (db.userDao().getRssFavoriteCountByArticleId(articleId) == 0) {
            return Rss()
        }
        return rssFromRssFavoriteEntity(db.userDao().getRssFavoriteByArticleId(articleId))
    }

    fun searchRss(context: Context, searchTerm: String): List<Rss> {
        val db = getDb(context)
        val searchResults = LinkedList<Rss>()
        val rssEntities = db.userDao().searchRss(searchTerm)
        for (rssEntity in rssEntities) {
            searchResults.add(rssFromRssEntity(rssEntity))
        }
        return searchResults
    }

    fun searchRssFavorites(context: Context, searchTerm: String): List<Rss> {
        val db = getDb(context)
        val searchResults = LinkedList<Rss>()
        val rssFavoriteEntities = db.userDao().searchRssFavorites(searchTerm)
        for (rssFavoriteEntity in rssFavoriteEntities) {
            searchResults.add(rssFromRssFavoriteEntity(rssFavoriteEntity))
        }
        return searchResults
    }

    fun getRssFavorites(context: Context): List<Rss> {
        val db = getDb(context)
        val rssFavoriteEntities = db.userDao().getFavoriteRss()
        val rssFavorites = LinkedList<Rss>()
        for (rssFavoriteEntity in rssFavoriteEntities) {
            rssFavorites.add(rssFromRssFavoriteEntity(rssFavoriteEntity))
        }
        return rssFavorites
    }

    fun getRss(context: Context, originalCategory: String): List<Rss> {
        val db = getDb(context)
        val results = LinkedList<Rss>()
        val rssEntities = db.userDao().getRss(originalCategory)
        for (rssEntity in rssEntities) {
            results.add(rssFromRssEntity(rssEntity))
        }
        return results
    }

    fun deleteRss(context: Context, originalCategory: String): Int {
        val db = getDb(context)
        db.userDao().deleteRss(originalCategory)
        return 0 // irrelevant
    }
}
