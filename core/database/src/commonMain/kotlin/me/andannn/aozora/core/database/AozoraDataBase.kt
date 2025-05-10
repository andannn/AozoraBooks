package me.andannn.aozora.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import me.andannn.aozora.core.database.dao.SavedBookDao
import me.andannn.aozora.core.database.entity.SavedBookEntity

internal object Tables {
    const val SAVED_BOOK_TABLE = "saved_book_table"
}

@Database(
    entities = [
        SavedBookEntity::class,
    ],
    version = 1,
)
@ConstructedBy(MelodifyDataBaseConstructor::class)
abstract class AozoraDataBase : RoomDatabase() {
    abstract fun savedBookDao(): SavedBookDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object MelodifyDataBaseConstructor : RoomDatabaseConstructor<AozoraDataBase> {
    override fun initialize(): AozoraDataBase
}

fun <T : RoomDatabase> RoomDatabase.Builder<T>.setUpDatabase() =
    apply {
        setQueryCoroutineContext(Dispatchers.IO)
    }
