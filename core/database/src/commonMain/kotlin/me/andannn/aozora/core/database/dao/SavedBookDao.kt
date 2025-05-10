package me.andannn.aozora.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.andannn.aozora.core.database.Tables
import me.andannn.aozora.core.database.entity.SavedBookColumns
import me.andannn.aozora.core.database.entity.SavedBookEntity

/**
 * The DAO for [SavedBookEntity]
 */
@Dao
interface SavedBookDao {
    /**
     * Get all saved books
     */
    @Query("SELECT * FROM ${Tables.SAVED_BOOK_TABLE}")
    fun getSavedBooks(): Flow<List<SavedBookEntity>>

    /**
     * Get a saved book by id
     */
    @Query("SELECT * FROM ${Tables.SAVED_BOOK_TABLE} WHERE ${SavedBookColumns.BOOK_ID} = :bookId")
    fun getSavedBookById(bookId: String): Flow<SavedBookEntity?>

    /**
     * Insert a saved book
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedBook(savedBookEntities: List<SavedBookEntity>)

    /**
     * Delete a saved book
     */
    @Query("DELETE FROM ${Tables.SAVED_BOOK_TABLE} WHERE ${SavedBookColumns.BOOK_ID} = :bookId")
    suspend fun deleteSavedBook(bookId: String)
}
