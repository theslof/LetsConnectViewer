package se.newton.letsconnectviewer.model

class Game() {
    var gid: String = ""
    var type: String = "local"
    var player1: String = ""
    var player2: String = ""
    var state: String = "init"
    var activePlayer: Int = 0

    constructor(gid: String) : this() {
        this.gid = gid
    }
}