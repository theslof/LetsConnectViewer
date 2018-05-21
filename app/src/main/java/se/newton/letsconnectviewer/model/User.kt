package se.newton.letsconnectviewer.model

import se.newton.letsconnectviewer.R

class User() {
    var uid: String = ""
    var userName: String = ""
    var email: String = ""
    var profileImage: String = ""

    constructor(uid: String): this() {
        this.uid = uid
    }

    fun update(user: User){
        this.userName = user.userName
        this.email = user.email
        this.profileImage = user.profileImage
    }

    companion object {
        fun getProfileImage(imageName: String) : Int {
            return when(imageName){
                "avatar_cat" -> R.drawable.avatar_cat
                "avatar_chicken" -> R.drawable.avatar_chicken
                "avatar_cow" -> R.drawable.avatar_cow
                "avatar_deer" -> R.drawable.avatar_deer
                "avatar_dog" -> R.drawable.avatar_dog
                "avatar_fox" -> R.drawable.avatar_fox
                "avatar_monkey" -> R.drawable.avatar_monkey
                "avatar_panda" -> R.drawable.avatar_panda
                "avatar_pig" -> R.drawable.avatar_pig
                else -> R.drawable.ic_profile_image_placeholder_circular
            }
        }
    }
}