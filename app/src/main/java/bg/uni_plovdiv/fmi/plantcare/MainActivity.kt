package bg.uni_plovdiv.fmi.plantcare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bg.uni_plovdiv.fmi.plantcare.data.PlantRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var repository: PlantRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}