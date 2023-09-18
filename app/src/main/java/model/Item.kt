package model

class Item (private val name: String, private val returnDate: String) {
    override fun toString(): String {
        return "$name $returnDate"
    }
}