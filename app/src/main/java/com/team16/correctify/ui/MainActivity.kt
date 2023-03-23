package com.team16.correctify.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.team16.correctify.BuildConfig
import com.team16.correctify.R
import com.team16.correctify.databinding.ActivityMainBinding
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener {
            saveText()
        }

        supportActionBar?.title = getString(R.string.app_name)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return when (item.itemId) {
            R.id.action_settings -> {
                navController.navigate(R.id.SettingsFragment)
                true
            }
            R.id.action_share -> {
                shareResponseText()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * This method launches the Android Sharesheet to allow the user to share information about
     * this forecast period.
     */
    private fun shareResponseText() {
        // val promptText = view?.findViewById<TextInputLayout>(R.id.input_text_layout)?.editText?.text.toString()
        val resultText = this.findViewById<TextView>(R.id.result_text)?.text.toString()

        if (resultText == "") {
            showSnackbar(
                "Sorry, you have to submit a prompt first!",
                this.findViewById<View>(R.id.main_constraint_layout)
            )
            return
        }

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, resultText)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, null))
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun showSnackbar(text: String, view: View) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()
    }

    fun saveText() {
        // https://gist.github.com/codinginflow/6c13bd0d08416115798f17d45b5d8056

        // Saves the output of the output as "output.txt"
        // Data stored on: "/data/data/com.team16.correctify/files/output.txt"
        val FILE_NAME = "output.txt"

        val mEditText = findViewById<TextView>(R.id.result_text)

        val text: String = mEditText.text.toString()
        var fos: FileOutputStream? = null

        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE)
            fos.write(text.toByteArray())
            // mEditText.getText().clear()
            Toast.makeText(
                this, "Saved to $filesDir/$FILE_NAME",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}