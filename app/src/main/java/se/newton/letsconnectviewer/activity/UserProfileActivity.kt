package se.newton.letsconnectviewer.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_user_profile.*
import se.newton.letsconnectviewer.R
import se.newton.letsconnectviewer.model.User
import se.newton.letsconnectviewer.service.Database

class UserProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val profileImageView: ImageView = findViewById<ImageView>(R.id.imageViewProfile)
        val textViewName: TextView = findViewById<TextView>(R.id.textViewDisplayName)
        val textViewEmail: TextView = findViewById<TextView>(R.id.textViewEmailAddress)
        val uid = intent.getStringExtra(INTENT_USER)

        Database.getUser(uid, { user ->
            Log.d("UserProfileActivity", "getUser()")
            if (user != null) {
                Glide.with(profileImageView)
                        .load(User.getProfileImage(user.profileImage))
                        .apply(
                                RequestOptions()
                                        .placeholder(R.drawable.ic_profile_image_placeholder_circular)
                        )
                        .into(profileImageView)

                textViewName.text = user.displayName
                textViewEmail.text = user.email
                textViewHighscore.text = user.highscore.toString()
            }
        })
    }


    companion object {
        private val INTENT_USER = "user"

        fun createIntent(context: Context, uid: String): Intent {
            val intent = Intent(context, UserProfileActivity::class.java)
            intent.putExtra(INTENT_USER, uid)
            return intent
        }
    }

}
