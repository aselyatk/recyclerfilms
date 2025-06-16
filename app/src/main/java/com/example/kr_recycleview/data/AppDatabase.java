package com.example.kr_recycleview.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = { Movie.class }, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase db) {
            db.execSQL("ALTER TABLE movies ADD COLUMN qualityTag INTEGER");
        }
    };
    public abstract MovieDao movieDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room
                            .databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "movies.db"
                            )
                            .addMigrations(MIGRATION_2_3)

                            // при любом изменении схемы — уничтожаем старую БД и создаём новую
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
