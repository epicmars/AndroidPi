package cn.androidpi.data.local

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.migration.Migration

import cn.androidpi.data.local.dao.TodoDao
import cn.androidpi.note.entity.Todo

/**
 * Created by jastrelax on 2017/11/2.
 */

@Database(entities = arrayOf(Todo::class), version = 2)
@TypeConverters(DateConverter::class, StringArrayConverter::class, TodoStatusConverter::class)
abstract class NoteDatabase : RoomDatabase() {

    companion object {
        val DATABASE_NAME = "note.db"
    }

    abstract fun todoDao(): TodoDao
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
