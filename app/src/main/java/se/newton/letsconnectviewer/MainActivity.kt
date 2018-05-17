package se.newton.letsconnectviewer

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = intent

        var user = intent.extras["user"] as FirebaseUser
        findViewById<TextView>(R.id.textViewHello).text = "Hello ${user.displayName}!"

    }

    companion object {
        private val INTENT_USER = "user"

        fun createIntent(context:Context, user: FirebaseUser?): Intent {
            if(user == null)
                throw IllegalArgumentException("User is not logged in")
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(INTENT_USER, user)
            return intent
        }
    }
}
