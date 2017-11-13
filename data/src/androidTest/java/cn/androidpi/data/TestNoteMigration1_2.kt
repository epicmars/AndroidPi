package cn.androidpi.data

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory
import android.arch.persistence.room.testing.MigrationTestHelper
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import cn.androidpi.common.datetime.DateUtils
import cn.androidpi.data.local.NOTE_MIGRATION_1_2
import cn.androidpi.data.local.NoteDatabase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Created by jastrelax on 2017/11/12.
 */
@RunWith(AndroidJUnit4::class)
class TestNoteMigration1_2 {

    companion object {
        val TEST_DB = "note_migration_1_2.db"
    }

    @Rule
    @JvmField
    var helper = MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
            NoteDatabase::class.java.canonicalName,
            FrameworkSQLiteOpenHelperFactory())

    @Test
    fun testMigration1To2() {
        var db = helper.createDatabase(TEST_DB, 1)

        // db has schema version 1. insert some data using SQL queries.
        // You cannot use DAO classes because they expect the latest schema.
        insertTodoV1(db)

        // Prepare for the next version.
        db.close()

        // Re-open the database with version 2 and provide
        // NOTE_MIGRATION_1_2 as the migration process.
        db = helper.runMigrationsAndValidate(TEST_DB, 2, true, NOTE_MIGRATION_1_2)

        // MigrationTestHelper automatically verifies the schema changes,
        // but you need to validate that the data was migrated properly.

        val cursor = db.query("select * from todo;")
        val now = Date()
        while (cursor.moveToNext()) {
            val startTime = cursor.getLong(cursor.getColumnIndex("start_time"))
            val deadline = cursor.getLong(cursor.getColumnIndex("deadline"))
            val status = cursor.getString(cursor.getColumnIndex("status"))
            if (DateUtils.formatDate(Date(startTime)) > DateUtils.formatDate(now)) {
                assertEquals("NEW", status)
            } else if (DateUtils.formatDate(now) in DateUtils.formatDate(Date(startTime))..DateUtils.formatDate(Date(deadline))) {
                assertEquals("START", status)
            } else {
                assertNull(status)
            }
        }
        cursor.close()
    }

    fun insertTodoV1(db: SupportSQLiteDatabase) {
        val calendar = Calendar.getInstance()
        val content = ContentValues()

        // status START
        content.put("created_time", calendar.timeInMillis)
        content.put("start_time", calendar.timeInMillis)
        content.put("deadline", calendar.timeInMillis + 72 * 3600 * 1000)
        content.put("content", "a todo text.")
        db.insert("todo", SQLiteDatabase.CONFLICT_ABORT, content)

        // status null
        content.put("start_time", calendar.timeInMillis)
        content.put("deadline", calendar.timeInMillis)
        db.insert("todo", SQLiteDatabase.CONFLICT_ABORT, content)

        // status NEW
        calendar.add(Calendar.DAY_OF_WEEK, 1)
        content.put("start_time", calendar.timeInMillis)
        content.put("deadline", calendar.timeInMillis)
        db.insert("todo", SQLiteDatabase.CONFLICT_ABORT, content)
    }
}