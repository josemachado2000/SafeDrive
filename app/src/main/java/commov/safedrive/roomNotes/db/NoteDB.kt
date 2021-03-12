package commov.safedrive.roomNotes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import commov.safedrive.roomNotes.dao.NoteDao
import commov.safedrive.roomNotes.entities.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Note class
@Database(entities = [Note::class], version = 1, exportSchema = false)
public abstract class NoteDB : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    private class NoteDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let {
                database -> scope.launch {
                var noteDao = database.noteDao()

                // Delete all content
//                noteDao.deleteAllNotes()

                // Add notes
//                var note = Note("teste")
//                noteDao.insertNote(note)
                }
            }
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: NoteDB? = null

        fun getDatabase(context: Context, scope: CoroutineScope): NoteDB {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDB::class.java,
                    "notes_database"
                )
                // Strategy of destruction
                // .fallbackToDestructiveMigration()
                .addCallback(NoteDatabaseCallback(scope))
                .build()

                INSTANCE = instance
                return instance
                // instance
            }
        }
    }
}