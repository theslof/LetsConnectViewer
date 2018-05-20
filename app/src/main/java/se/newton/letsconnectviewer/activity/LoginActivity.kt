package se.newton.letsconnectviewer.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import se.newton.letsconnectviewer.BuildConfig
import se.newton.letsconnectviewer.R
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

        if (auth.currentUser != null) {
            //If user is signed in we launch MainActivity
            launchMainActivity()
        } else {
            // Otherwise we display AuthUI sign in form
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN)
        }
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
        val user = auth.currentUser
        startActivity(MainActivity.createIntent(this, user))
        finish()
    }
}
