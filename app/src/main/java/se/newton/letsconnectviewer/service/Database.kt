package se.newton.letsconnectviewer.service

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import se.newton.letsconnectviewer.model.User

class Database {
    companion object {
        private const val TAG = "Database"


        fun createUser(uid: String, onCompleteCallback: (User?) -> Unit) {
            Log.d(TAG, "Fetching user $uid")
            FirebaseFirestore.getInstance().collection("Users").document(uid).get()
                    .addOnCompleteListener { task ->
                        Log.d(TAG, "Fetching completed")
                        if (task.isSuccessful) {
                            Log.d(TAG, "Fetching successful")
                            val doc = task.result
                            if (doc.exists()) {
                                Log.d(TAG, "User '$uid' already exists")
                                onCompleteCallback(doc.toObject(User::class.java))
                            } else {
                                Log.d(TAG, "Creating user '$uid'")
                                val user = User(uid)
                                doc.reference.set(user).addOnCompleteListener { res ->
                                    if (res.isSuccessful) {
                                        onCompleteCallback(user)
                                    } else {
                                        onCompleteCallback(null)
                                    }
                                }
                            }
                        } else {
                            Log.d(TAG, task.exception.toString())
                            onCompleteCallback(null)
                        }
                    }
        }

        fun getUser(uid: String, onCompleteCallback: (User?) -> Unit) {
            val db: FirebaseFirestore = FirebaseFirestore.getInstance()
            Log.d(TAG, "Fetching user $uid")

            db.collection("Users").document(uid).get().addOnCompleteListener { task ->
                Log.d(TAG, "Fetching completed")
                if (task.isSuccessful) {
                    Log.d(TAG, "Fetching successful")
                    val doc: DocumentSnapshot = task.result

                    if (doc.exists()) {
                        Log.d(TAG, "User document exists")
                        onCompleteCallback(doc.toObject(User::class.java))
                    } else {
                        Log.d(TAG, "User does not exist in collection")
                        onCompleteCallback(null)
                    }
                } else {
                    Log.d(TAG, task.exception.toString())
                    onCompleteCallback(null)
                }
            }
        }

        fun updateUser(user: User, onCompleteCallback: (User?) -> Unit) {
            val db: FirebaseFirestore = FirebaseFirestore.getInstance()

            db.collection("Users").document(user.uid).set(user, SetOptions.merge())
                    .addOnCompleteListener({ task ->
                        if (task.isSuccessful) {
                            onCompleteCallback(user)
                        } else {
                            Log.d(TAG, task.exception.toString())
                            onCompleteCallback(null)
                        }
                    })

        }
    }
}