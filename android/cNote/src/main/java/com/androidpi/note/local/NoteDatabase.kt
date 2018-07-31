package com.androidpi.note.local

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.migration.Migration
import com.androidpi.base.data.model.DateConverter
import com.androidpi.base.data.model.StringArrayConverter
import com.androidpi.note.entity.TextNote
import com.androidpi.note.entity.Todo
import com.androidpi.note.local.dao.TextNoteDao
import com.androidpi.note.local.dao.TodoDao

/**
 * Created by jastrelax on 2017/11/2.
 */

@Database(entities = arrayOf(Todo::class, TextNote::class), version = 4)
@TypeConverters(DateConverter::class, StringArrayConverter::class, TodoStatusConverter::class)
abstract class NoteDatabase : RoomDatabase() {

    companion object {
        val DATABASE_NAME = "note.db"
    }

    abstract fun todoDao(): TodoDao

    abstract fun textNoteDao(): TextNoteDao
}

class TodoStatusConverter {

    @TypeConverter
    fun fromStatus(status: Todo.Status?): String? {
        return status?.name
    }

    @TypeConverter
    @Throws(IllegalArgumentException::class)
    fun fromString(statusStr: String?): Todo.Status? {
        return if (statusStr == null) null else Todo.Status.valueOf(statusStr)
    }
}

object NOTE_MIGRATION_1_2 : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE todo ADD COLUMN update_time INTEGER;")
        database.execSQL("ALTER TABLE todo ADD COLUMN tags TEXT;")
        database.execSQL("ALTER TABLE todo ADD COLUMN status TEXT;")
        database.execSQL("""UPDATE todo SET status =
            CASE
                WHEN date(start_time/1000, 'unixepoch') > date('now') then 'NEW'
                WHEN date(start_time/1000, 'unixepoch') <= date('now') and date('now') <= date(deadline/1000, 'unixepoch') THEN 'START'
            END
            ;
            """)
    }
}

object NOTE_MIGRATION_2_3 : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE todo ADD COLUMN priority INTEGER;")
    }
}


object NOTE_MIGRATION_3_4 : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        val tableTextNotes = "text_notes"
        database.execSQL("CREATE TABLE IF NOT EXISTS `$tableTextNotes` (`text` TEXT, `id` INTEGER, `created_time` INTEGER, `update_time` INTEGER, `tags` TEXT, `category` TEXT, PRIMARY KEY(`id`))")
    }
}
