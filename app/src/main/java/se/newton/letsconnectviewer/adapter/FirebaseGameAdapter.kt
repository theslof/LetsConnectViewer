package se.newton.letsconnectviewer.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import se.newton.letsconnectviewer.R
import se.newton.letsconnectviewer.activity.GameBoardActivity
import se.newton.letsconnectviewer.model.Game
import se.newton.letsconnectviewer.service.Database

class FirebaseGameAdapter(private val dataset: List<Game>) : RecyclerView.Adapter<FirebaseGameAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.game_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindGame(dataset[position])
    }


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val coin1: ImageView
        val coin2: ImageView
        val player1: TextView
        val player2: TextView
        var gid: String = ""

        init {
            view.setOnClickListener(this)
            coin1 = view.findViewById(R.id.imageViewCoinPlayer1)
            coin2 = view.findViewById(R.id.imageViewCoinPlayer2)
            player1 = view.findViewById(R.id.textViewPlayer1)
            player2 = view.findViewById(R.id.textViewPlayer2)
        }

        override fun onClick(p0: View?) {
            p0?.context?.startActivity(GameBoardActivity.createIntent(p0.context, gid))
        }

        fun bindGame(game: Game) {
            gid = game.gid
            val uid: String = FirebaseAuth.getInstance().uid ?: ""

            if (game.activePlayer == 0)
                Glide.with(view)
                        .load(R.drawable.ic_coin_yellow)
                        .into(coin1)
            else
                Glide.with(view)
                        .load(R.drawable.ic_coin_red)
                        .into(coin2)

            if (game.player1 != uid)
                Database.getUser(game.player1, { user -> player1.text = user?.displayName ?: "Player 1" })
            else {
                player1.text = "You"
                player1.setTextColor(view.resources.getColor(R.color.colorPrimary, view.context.theme))
            }

            if (game.type == "local")
                player2.text = game.player2
            else
                if (game.player2 == uid) {
                    player2.setTextColor(view.resources.getColor(R.color.colorPrimary, view.context.theme))
                    Database.getUser(game.player2, { user -> player2.text = user?.displayName ?: "Player 2" })
                }
        }
    }
}