package model

class User (val userName: String, val department : String, val eMail : String) {
    override fun toString(): String {
        return "$userName $department $eMail"
    }
}