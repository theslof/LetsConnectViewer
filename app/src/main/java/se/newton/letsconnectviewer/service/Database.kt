package se.newton.letsconnectviewer.service

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import se.newton.letsconnectviewer.model.Game
import se.newton.letsconnectviewer.model.Move
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
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onCompleteCallback(user)
                        } else {
                            Log.d(TAG, task.exception.toString())
                            onCompleteCallback(null)
                        }
                    }

        }

        fun createGame(type: String, player1: User, player2: User, onCompleteCallback: (Game?) -> Unit) {
            val game = Game()
            game.type = type
            game.player1 = player1.uid
            game.player2 = player2.uid
            FirebaseFirestore.getInstance().collection("Games").add(game)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onCompleteCallback(game)
                        } else {
                            Log.d(TAG, task.exception.toString())
                            onCompleteCallback(null)
                        }
                    }
        }

        fun getGame(gid: String, onCompleteCallback: (Game?) -> Unit) {
            val db: FirebaseFirestore = FirebaseFirestore.getInstance()
            Log.d(TAG, "Fetching game $gid")

            db.collection("Games").document(gid).get().addOnCompleteListener { task ->
                Log.d(TAG, "Fetching completed")
                if (task.isSuccessful) {
                    Log.d(TAG, "Fetching successful")
                    val doc: DocumentSnapshot = task.result

                    if (doc.exists()) {
                        Log.d(TAG, "Game document exists")
                        onCompleteCallback(doc.toObject(Game::class.java))
                    } else {
                        Log.d(TAG, "Game does not exist in collection")
                        onCompleteCallback(null)
                    }
                } else {
                    Log.d(TAG, task.exception.toString())
                    onCompleteCallback(null)
                }
            }
        }

        fun getMoves(gid: String, onCompleteCallback: (List<Move>?) -> Unit) {
            val db: FirebaseFirestore = FirebaseFirestore.getInstance()
            Log.d(TAG, "Fetching moves for game $gid")

            db.collection("Games").document(gid).collection("Moves").orderBy("move").get().addOnCompleteListener { task ->
                Log.d(TAG, "Fetching completed")
                if (task.isSuccessful) {
                    Log.d(TAG, "Fetching successful")
                    val query: QuerySnapshot = task.result
                    onCompleteCallback(query.toObjects(Move::class.java))
                } else {
                    Log.d(TAG, task.exception.toString())
                    onCompleteCallback(null)
                }
            }
        }
    }
}