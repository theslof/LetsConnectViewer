package se.newton.letsconnectviewer.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.Toolbar
import se.newton.letsconnectviewer.R
import se.newton.letsconnectviewer.model.Game
import se.newton.letsconnectviewer.model.Move
import se.newton.letsconnectviewer.service.Database
import android.util.TypedValue
import android.util.DisplayMetrics
import android.view.Gravity
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import se.newton.letsconnectviewer.model.User
import se.newton.letsconnectviewer.service.UserManager


class GameBoardActivity : AppCompatActivity() {
    private val GRID_HEIGHT: Int = 6
    private val GRID_WIDTH: Int = 7
    private val COIN_RED: Int = R.drawable.ic_coin_red
    private val COIN_YELLOW: Int = R.drawable.ic_coin_yellow
    private val COIN_NONE: Int = R.drawable.ic_coin_none
    private val COIN_START: Int = COIN_NONE
    private val MARGIN: Int = 4
    private var gid: String = ""
    private var game: Game = Game()
    private var moves: List<Move> = ArrayList()
    private var move: Int = 0
    private val grid: ArrayList<ImageView> = ArrayList()
    private lateinit var playfield: GridLayout
    private lateinit var seekBar: SeekBar
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_board)

        playfield = findViewById(R.id.layoutPlayfield)
        seekBar = findViewById(R.id.seekBar)
        seekBar.isEnabled = false

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener { finish() }

        // Take the Game data from the Intent
        gid = intent.getStringExtra(INTENT_GAME)

        val marginPX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, MARGIN.toFloat(), DisplayMetrics()).toInt()
        val marginDP = 8

        // Setup layout
        playfield.alignmentMode = GridLayout.ALIGN_BOUNDS
        playfield.columnCount = GRID_WIDTH
        playfield.rowCount = GRID_HEIGHT
        playfield.setPadding(marginPX, marginPX, marginPX, marginPX)

        val dispMet = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dispMet)
        val coinSize: Int = Math.min(dispMet.heightPixels / GRID_HEIGHT, dispMet.widthPixels / GRID_WIDTH) - marginPX * 2

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
                params.setMargins(marginPX, marginPX, marginPX, marginPX)

                view.layoutParams = params
            }
        }

        Database.getMoves(gid, { moves: List<Move>? ->
            if (moves != null) {
                this.moves = moves
                seekBar.isEnabled = true
                seekBar.max = moves.size
                seekBar.progress = moves.size
                redrawPlayfield(moves.size)
            }
        })

        Database.getGame(gid, { game: Game? ->
            if (game != null) {
                this.game = game

                val profile1 = findViewById<ImageView>(R.id.imageViewProfile1)
                val profile2 = findViewById<ImageView>(R.id.imageViewProfile2)

                val p1: User = UserManager.getUser(game.player1)
                Glide.with(this)
                        .load(User.getProfileImage(p1.profileImage))
                        .apply(
                                RequestOptions()
                                        .placeholder(R.drawable.ic_profile_image_placeholder_circular)
                        )
                        .into(profile1)
                profile1.setOnClickListener {
                    startActivity(UserProfileActivity.createIntent(this, p1.uid))
                }

                val p2: String = if (game.type == "local") game.player2
                else {
                    val u = UserManager.getUser(game.player2)
                    Glide.with(this)
                            .load(User.getProfileImage(u.profileImage))
                            .apply(
                                    RequestOptions()
                                            .placeholder(R.drawable.ic_profile_image_placeholder_circular)
                            )
                            .into(profile2)
                    profile2.setOnClickListener {
                        startActivity(UserProfileActivity.createIntent(this, u.uid))
                    }
                    u.displayName
                }

                supportActionBar?.title = "${p1.displayName} vs. $p2"
            }
        })

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, userEvent: Boolean) {
                if (userEvent)
                    redrawPlayfield(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

        findViewById<AppCompatImageButton>(R.id.buttonPrevious).setOnClickListener { redrawPlayfield(move - 1) }
        findViewById<AppCompatImageButton>(R.id.buttonNext).setOnClickListener { redrawPlayfield(move + 1) }
    }

    private fun redrawPlayfield(progress: Int) {
        if (progress < 0 || progress > moves.size)
            return
        val forward = progress > move
        var state = move
        while (progress != state) {
            if (!forward) {
                state--
                grid[moves[state].y * GRID_WIDTH + moves[state].x].setImageResource(COIN_NONE)
            } else {
                grid[moves[state].y * GRID_WIDTH + moves[state].x]
                        .setImageResource(if (moves[state].player == 0) COIN_RED else COIN_YELLOW)
                state++
            }
        }
        move = progress
        seekBar.progress = progress
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
