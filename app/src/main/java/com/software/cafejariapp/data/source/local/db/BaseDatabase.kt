package com.software.cafejariapp.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.software.cafejariapp.data.source.local.dao.DisableDateDao
import com.software.cafejariapp.domain.entity.Login
import com.software.cafejariapp.domain.entity.RefreshToken
import com.software.cafejariapp.data.source.local.dao.TokenDao
import com.software.cafejariapp.data.source.local.dao.LoginDao
import com.software.cafejariapp.domain.entity.DisableDate


@Database(
    entities = [Login::class, RefreshToken::class, DisableDate::class],
    version = 3
)
abstract class BaseDatabase: RoomDatabase() {
    abstract val loginDao: LoginDao
    abstract val tokenDao: TokenDao
    abstract val disableDateDao: DisableDateDao

    companion object {
       const val DATABASE_NAME = "room_db"
    }
}

val migration_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE DisabledDate ('id' INTEGER NOT NULL, 'year' INTEGER NOT NULL, 'month' INTEGER NOT NULL, 'day' INTEGER NOT NULL, PRIMARY KEY('id'))"
        )
    }
}

val migration_2_3: Migration = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE DisabledDate RENAME TO DisableDate"
        )
    }
}
