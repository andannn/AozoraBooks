/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import me.andannn.aozora.core.database.dao.SavedBookDao
import me.andannn.aozora.core.database.entity.BookEntity
import me.andannn.aozora.core.database.entity.BookProgressColumns
import me.andannn.aozora.core.database.entity.BookProgressEntity
import me.andannn.aozora.core.database.entity.SavedBookEntity

internal object Tables {
    const val BOOK_TABLE = "book_table"
    const val SAVED_BOOK_TABLE = "saved_book_table"
    const val BOOK_PROGRESS_TABLE = "book_progress_table"
}

@Database(
    entities = [
        BookEntity::class,
        SavedBookEntity::class,
        BookProgressEntity::class,
    ],
    version = 5,
)
@ConstructedBy(MelodifyDataBaseConstructor::class)
abstract class AozoraDataBase : RoomDatabase() {
    abstract fun savedBookDao(): SavedBookDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
internal expect object MelodifyDataBaseConstructor : RoomDatabaseConstructor<AozoraDataBase> {
    override fun initialize(): AozoraDataBase
}

internal fun <T : RoomDatabase> RoomDatabase.Builder<T>.setUpDatabase() =
    apply {
        setQueryCoroutineContext(Dispatchers.IO)
        addMigrations(
            MIGRATION_1_2,
            MIGRATION_2_3,
            MIGRATION_3_4,
            MIGRATION_4_5,
        )
    }

internal val MIGRATION_1_2 =
    object : Migration(1, 2) {
        override fun migrate(connection: SQLiteConnection) {
            connection.execSQL(
                """
                CREATE TABLE book_table_new (
                    book_id TEXT NOT NULL PRIMARY KEY,
                    group_id TEXT NOT NULL,
                    title TEXT NOT NULL,
                    title_kana TEXT NOT NULL,
                    author TEXT,
                    author_url TEXT,
                    zip_url TEXT,
                    html_url TEXT,
                    saved_date INTEGER NOT NULL
                )
                """.trimIndent(),
            )

            connection.execSQL(
                """
                INSERT INTO book_table_new (
                    book_id, group_id, title, title_kana, author, author_url, zip_url, html_url, saved_date
                )
                SELECT 
                    book_id, group_id, title, title_kana, author, author_url, zip_url, html_url, saved_date
                FROM book_table
                """.trimIndent(),
            )

            connection.execSQL("DROP TABLE book_table")
            connection.execSQL("ALTER TABLE book_table_new RENAME TO book_table")
        }
    }

internal val MIGRATION_2_3 =
    object : Migration(2, 3) {
        override fun migrate(connection: SQLiteConnection) {
            connection.execSQL(
                "ALTER TABLE ${Tables.BOOK_PROGRESS_TABLE} ADD COLUMN ${BookProgressColumns.TOTAL_BLOCK_COUNT} INTEGER",
            )
        }
    }

internal val MIGRATION_3_4 =
    object : Migration(3, 4) {
        override fun migrate(connection: SQLiteConnection) {
            connection.execSQL(
                "ALTER TABLE ${Tables.BOOK_PROGRESS_TABLE} ADD COLUMN ${BookProgressColumns.MARK_COMPLETED} INTEGER",
            )
        }
    }

internal val MIGRATION_4_5 =
    object : Migration(4, 5) {
        override fun migrate(connection: SQLiteConnection) {
            connection.execSQL("DROP TABLE IF EXISTS book_table")

            connection.execSQL(
                """
                CREATE TABLE book_table (
                    book_id TEXT PRIMARY KEY NOT NULL,
                    title TEXT NOT NULL,
                    title_kana TEXT NOT NULL,
                    title_sort_kana TEXT,
                    subtitle TEXT,
                    subtitle_kana TEXT,
                    original_title TEXT,
                    first_appearance TEXT,
                    category_no TEXT,
                    orthography TEXT,
                    work_copyright_flag TEXT,
                    publish_date TEXT,
                    last_update_date TEXT,
                    card_url TEXT NOT NULL,
                    author_id TEXT NOT NULL,
                    author_last_name TEXT NOT NULL,
                    author_first_name TEXT NOT NULL,
                    author_last_name_kana TEXT,
                    author_first_name_kana TEXT,
                    author_last_name_sort_kana TEXT,
                    author_first_name_sort_kana TEXT,
                    author_last_name_romaji TEXT,
                    author_first_name_romaji TEXT,
                    author_role_flag TEXT,
                    author_birth TEXT,
                    author_death TEXT,
                    author_copyright_flag TEXT,
                    source_book1 TEXT,
                    source_publisher1 TEXT,
                    source_pub_year1 TEXT,
                    input_edition1 TEXT,
                    proof_edition1 TEXT,
                    parent_source_book1 TEXT,
                    parent_source_publisher1 TEXT,
                    parent_source_pub_year1 TEXT,
                    source_book2 TEXT,
                    source_publisher2 TEXT,
                    source_pub_year2 TEXT,
                    input_edition2 TEXT,
                    proof_edition2 TEXT,
                    parent_source_book2 TEXT,
                    parent_source_publisher2 TEXT,
                    parent_source_pub_year2 TEXT,
                    input_by TEXT,
                    proof_by TEXT,
                    text_file_url TEXT,
                    text_file_last_update TEXT,
                    text_file_encoding TEXT,
                    text_file_charset TEXT,
                    text_file_revision TEXT,
                    html_file_url TEXT,
                    html_file_last_update TEXT,
                    html_file_encoding TEXT,
                    html_file_charset TEXT,
                    html_file_revision TEXT
                )
                """.trimIndent(),
            )
        }
    }
