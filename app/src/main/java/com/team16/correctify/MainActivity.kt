package com.team16.correctify

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.aallam.openai.client.OpenAI
import com.team16.correctify.databinding.ActivityMainBinding
import io.ktor.client.engine.android.*
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.*
import java.net.Proxy
import java.net.InetSocketAddress

/* To use your own OpenAI API key here, create a file called `gradle.properties` in your
* GRADLE_USER_HOME directory (this will usually be `$HOME/.gradle/` in MacOS/Linux and
* `$USER_HOME/.gradle/` in Windows), and add the following line:
*
*   OPENAI_API_KEY="<put_your_own_OpenAI_API_key_here>"
*/
const val OPENAI_KEY = BuildConfig.OPENAI_API_KEY

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    val client = HttpClient(OkHttp) {
        engine {
            // this: OkHttpConfig
            config {
                // this: OkHttpClient.Builder
                followRedirects(true)
                // ...
            }
        }
    }
    private val openAI = OpenAI(OPENAI_KEY)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}