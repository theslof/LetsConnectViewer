package se.newton.letsconnectviewer.service

import se.newton.letsconnectviewer.model.User

class UserManager {
    companion object {
        private val users: HashMap<String, User> = HashMap()

        fun getUser(uid: String): User{
            return users[uid] ?: createUser(uid)
        }

        private fun createUser(uid: String): User{
            val user = User(uid)
            users[uid] = user
            Database.getUser(user.uid, {u ->
                if(u != null) user.update(u)
            })
            return user
        }
    }
}