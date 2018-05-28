package se.newton.letsconnectviewer.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import se.newton.letsconnectviewer.BuildConfig
import se.newton.letsconnectviewer.R
import se.newton.letsconnectviewer.model.User
import se.newton.letsconnectviewer.service.Database
import se.newton.letsconnectviewer.service.UserManager
import java.util.*

class LoginActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 123 //the request code could be any Integer
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val providers: List<AuthUI.IdpConfig> = Arrays.asList(
            // Add login providers here
            // AuthUI.IdpConfig.GoogleBuilder().build(),
            // AuthUI.IdpConfig.FacebookBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //If user is signed in we launch MainActivity
        if (auth.currentUser != null) launchMainActivity()
        // Otherwise we display AuthUI sign in form
        else showSignInPopup()

        findViewById<Button>(R.id.buttonSignIn).setOnClickListener { showSignInPopup() }
    }

    private fun showSignInPopup() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK)
                launchMainActivity()
        }
    }

    private fun launchMainActivity() {
        val user: FirebaseUser? = auth.currentUser
        if (user != null) {
            Database.createUser(user.uid, { res ->
                if (res != null) {
                    if (res.email.isBlank())
                        res.email = user.email ?: "anon@anon.anon"
                    if (res.displayName.isBlank())
                        res.displayName = user.displayName ?: "Anonymous"
                    Database.updateUser(res, {UserManager.getUser(user.uid)})
                }
            })
            startActivity(MainActivity.createIntent(this))
            finish()
        }
    }
}
