package se.newton.letsconnectviewer.activity

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import se.newton.letsconnectviewer.R
import se.newton.letsconnectviewer.model.Game
import se.newton.letsconnectviewer.model.Move
import se.newton.letsconnectviewer.service.Database

class GameBoardActivity : AppCompatActivity() {
    var gid: String = ""
    var game: Game = Game()
    var moves: List<Move> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_board)

        gid = intent.getStringExtra(INTENT_GAME)
        Database.getMoves(gid, { moves:List<Move>? ->
            if(moves != null){
                this.moves = moves
            }
        })

        Database.getGame(gid, { game:Game? ->
            if(game != null){
                this.game = game
            }
        })
    }

    companion object {
        private val INTENT_GAME = "game_id"

        fun createIntent(context: Context, gid: String): Intent {
            val intent = Intent(context, UserProfileActivity::class.java)
            intent.putExtra(INTENT_GAME, gid)
            return intent
        }

    }
}
