package se.newton.letsconnectviewer.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseUser
import se.newton.letsconnectviewer.R

class UserProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        // Read the user object passed as a parameter to the Intent...
        val user = intent.extras["user"] as FirebaseUser
        // ...and show their display name in the greeting.
        val profileImageView: ImageView = findViewById<ImageView>(R.id.imageViewProfile)
        val textViewName: TextView = findViewById<TextView>(R.id.textViewDisplayName)
        val textViewEmail: TextView = findViewById<TextView>(R.id.textViewEmailAddress)

        Glide.with(profileImageView)
                .load(user.photoUrl)
                .apply(
                        RequestOptions()
                                .placeholder(R.drawable.ic_profile_image_placeholder_circular)
                                .circleCrop()
                )
                .into(profileImageView)

        textViewName.text = user.displayName
        textViewEmail.text = user.email
    }


    companion object {
        private val INTENT_USER = "user"

        fun createIntent(context: Context, user: FirebaseUser?): Intent {
            if (user == null)
                throw IllegalArgumentException("Must supply a User object")
            val intent = Intent(context, UserProfileActivity::class.java)
            intent.putExtra(INTENT_USER, user)
            return intent
        }
    }

}
