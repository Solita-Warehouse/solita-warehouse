package model

class User (var userName: String, var id: Int, var partnerId: Int, var eMail: String) {
    override fun toString(): String {
        return "username = $userName, id = $id, partnerId = $partnerId, email = $eMail"
    }
}