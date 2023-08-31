package model

class User (private val userName: String, private val department : String, private val eMail : String) {
    override fun toString(): String {
        return "$userName $department $eMail"
    }
}