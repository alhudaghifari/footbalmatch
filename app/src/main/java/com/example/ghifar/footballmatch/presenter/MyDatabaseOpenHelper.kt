package com.example.ghifar.footballmatch.presenter

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.ghifar.footballmatch.model.database.EventDb
import org.jetbrains.anko.db.*


/**
 * Created by Alhudaghifari on 12:30 26/09/18
 *
 */
class MyDatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "FavoriteTeam.db", null, 1) {
    companion object {
        private var instance: MyDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MyDatabaseOpenHelper {
            if (instance == null) {
                instance = MyDatabaseOpenHelper(ctx.applicationContext)
            }
            return instance as MyDatabaseOpenHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db.createTable(EventDb.TABLE_FAVORITE_MATCH, true,
                EventDb.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                EventDb.EVENT_ID to TEXT + UNIQUE,
                EventDb.ID_HOME_TEAM to TEXT,
                EventDb.ID_AWAY_TEAM to TEXT,
                EventDb.STR_HOME_TEAM to TEXT,
                EventDb.STR_AWAY_TEAM to TEXT,
                EventDb.INT_HOME_SCORE to TEXT,
                EventDb.INT_AWAY_SCORE to TEXT,
                EventDb.DATE_EVENT to TEXT,
                EventDb.STR_HOME_BADGE to TEXT,
                EventDb.STR_AWAY_BADGE to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(EventDb.TABLE_FAVORITE_MATCH, true)
    }
}

// Access property for Context
val Context.database: MyDatabaseOpenHelper
    get() = MyDatabaseOpenHelper.getInstance(applicationContext)