package se.newton.letsconnectviewer.model

import se.newton.letsconnectviewer.R

class User() {
    var uid: String = ""
    var displayName: String = ""
    var email: String = ""
    var profileImage: String = "placeholder"
    var highscore: Int = 0

    constructor(uid: String): this() {
        this.uid = uid
    }

    fun update(user: User){
        this.displayName = user.displayName
        this.email = user.email
        this.profileImage = user.profileImage
        this.highscore = user.highscore
    }

    companion object {
        fun getProfileImage(imageName: String) : Int {
            return when(imageName){
                "cat" -> R.drawable.avatar_cat
                "chicken" -> R.drawable.avatar_chicken
                "cow" -> R.drawable.avatar_cow
                "deer" -> R.drawable.avatar_deer
                "dog" -> R.drawable.avatar_dog
                "fox" -> R.drawable.avatar_fox
                "monkey" -> R.drawable.avatar_monkey
                "panda" -> R.drawable.avatar_panda
                "pig" -> R.drawable.avatar_pig
                else -> R.drawable.ic_profile_image_placeholder_circular
            }
        }
    }
}