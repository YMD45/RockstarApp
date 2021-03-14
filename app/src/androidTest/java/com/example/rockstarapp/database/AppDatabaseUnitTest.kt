import android.app.Instrumentation
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.rockstarapp.database.AppDatabase
import com.example.rockstarapp.database.dao.ProfilDao
import com.example.rockstarapp.database.dao.RockstarDao
import com.example.rockstarapp.model.Profil
import com.example.rockstarapp.model.Rockstar
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class AppDatabaseUnitTest {
    private lateinit var rockstarDao: RockstarDao
    private lateinit var profilDao: ProfilDao
    private lateinit var db: AppDatabase

    @Before
    fun initDatabase(){
        val context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(
            context,AppDatabase::class.java
        ).build()

        rockstarDao = db.RockstarDao()
        profilDao = db.ProfilDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeRockstarAndReadInList() {
        val testRockstar = Rockstar(1,"picture", "Lebron","James","En cours",true)
        rockstarDao.insert(testRockstar)
        val todoItem = rockstarDao.findByLastName(testRockstar.lastName)
        assertThat(todoItem, equalTo(testRockstar))
    }

    @Test
    @Throws(Exception::class)
    fun writeProfilAndReadInList() {
        val testProfil = Profil(1,"testUrl","MJ")
        profilDao.insert(testProfil)
        val todoItem = profilDao.findByName(testProfil.fullName)
        assertThat(todoItem, equalTo(testProfil))
    }

}