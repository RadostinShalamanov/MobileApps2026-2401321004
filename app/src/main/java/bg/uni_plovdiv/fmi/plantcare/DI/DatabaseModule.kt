package bg.uni_plovdiv.fmi.plantcare.DI

import android.content.Context
import androidx.room.Room
import bg.uni_plovdiv.fmi.plantcare.data.AppDatabase
import bg.uni_plovdiv.fmi.plantcare.data.PlantDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "plantcare.db").build()

    @Provides
    fun providePlantDao(db: AppDatabase): PlantDao = db.plantDao()
}