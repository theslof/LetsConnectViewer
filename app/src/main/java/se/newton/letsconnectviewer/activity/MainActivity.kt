package se.newton.letsconnectviewer.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import se.newton.letsconnectviewer.R
import se.newton.letsconnectviewer.adapter.FirebaseGameAdapter
import se.newton.letsconnectviewer.model.User
import se.newton.letsconnectviewer.service.Database
import se.newton.letsconnectviewer.service.UserManager

class MainActivity : AppCompatActivity() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var onAuthChangeListener: FirebaseAuth.AuthStateListener
    private val user: User = UserManager.getUser(auth.currentUser?.uid ?: "")
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Create a listener method that triggers if the user login state changes.
        // This is attached to an object so that we can register and unregister the listener.
        // If it was attached directly as a lambda we wouldn't be able to register and unregister
        // it during state changes.
        onAuthChangeListener = FirebaseAuth.AuthStateListener { auth ->
            if (auth.currentUser == null) {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        // Make the buttons do something.
        findViewById<Button>(R.id.buttonSignOut).setOnClickListener {
            auth.signOut()
        }

        findViewById<Button>(R.id.buttonProfile).setOnClickListener {
            startActivity(UserProfileActivity.createIntent(this, user.uid))
        }
        val recyclerView: RecyclerView = findViewById(R.id.gameList)
        recyclerView.setHasFixedSize(true)

        Database.getGames(user.uid, { games ->
            recyclerView.adapter = FirebaseGameAdapter(games)
        })


    }

    override fun onStart() {
        super.onStart()

        auth.addAuthStateListener(onAuthChangeListener)
        findViewById<TextView>(R.id.textViewDisplayName).text = user.displayName
    }

    override fun onStop() {
        super.onStop()

        auth.removeAuthStateListener(onAuthChangeListener)
    }

    // This is similar to statics in Java. It can be addressed from any other class
    // through MainActivity.createIntent(context, user)
    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
