package se.newton.letsconnectviewer.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.view.View
import android.widget.ImageView
import se.newton.letsconnectviewer.R
import se.newton.letsconnectviewer.model.Game
import se.newton.letsconnectviewer.model.Move
import se.newton.letsconnectviewer.service.Database
import android.util.TypedValue
import android.util.DisplayMetrics
import android.view.Gravity
import android.widget.GridLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class GameBoardActivity : AppCompatActivity() {
    private val GRID_HEIGHT: Int = 6
    private val GRID_WIDTH: Int = 7
    private val COIN_RED: Int = R.drawable.ic_coin_red
    private val COIN_YELLOW: Int = R.drawable.ic_coin_yellow
    private val COIN_NONE: Int = R.drawable.ic_coin_none
    private val COIN_START: Int = COIN_RED
    private val MARGIN: Int = 8
    var gid: String = ""
    var game: Game = Game()
    var moves: List<Move> = ArrayList()
    val grid: ArrayList<ImageView> = ArrayList()
    private lateinit var playfield: GridLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_board)
        playfield = findViewById(R.id.layoutPlayfield)

        // Take the Game data from the Intent
        gid = intent.getStringExtra(INTENT_GAME)

        // Setup layout
        playfield.alignmentMode = GridLayout.ALIGN_BOUNDS
        playfield.columnCount = GRID_WIDTH
        playfield.rowCount = GRID_HEIGHT

        val marginPX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, MARGIN.toFloat(), DisplayMetrics()).toInt()
        val marginDP = 8
        val dispMet = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dispMet)
        val coinSize: Int = Math.min(dispMet.heightPixels / GRID_HEIGHT, dispMet.widthPixels / GRID_WIDTH) - marginPX

        for (y in 0 until GRID_HEIGHT) {
            for (x in 0 until GRID_WIDTH) {
                val view = ImageView(this)
                view.setImageResource(COIN_START)
                grid.add(view)
                playfield.addView(view)

                val params: GridLayout.LayoutParams = GridLayout.LayoutParams()
                params.setGravity(Gravity.CENTER)
                params.columnSpec = GridLayout.spec(x)
                params.rowSpec = GridLayout.spec(y)
                params.height = coinSize
                params.width = coinSize
                params.rightMargin = marginPX
                params.topMargin = marginPX
                params.bottomMargin = marginPX
                params.rightMargin = marginPX

                view.layoutParams = params
            }
        }

        Database.getMoves(gid, { moves: List<Move>? ->
            if (moves != null) {
                this.moves = moves
                for(move in moves){
                    grid[move.y * GRID_WIDTH + move.x].setImageResource(if (move.player == 0) COIN_NONE else COIN_YELLOW)
                }
            }
        })

        Database.getGame(gid, { game: Game? ->
            if (game != null) {
                this.game = game
            }
        })
    }

    companion object {
        private val INTENT_GAME = "game_id"

        fun createIntent(context: Context, gid: String): Intent {
            val intent = Intent(context, GameBoardActivity::class.java)
            intent.putExtra(INTENT_GAME, gid)
            return intent
        }

    }
}
