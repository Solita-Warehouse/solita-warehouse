package model

class Item(val name: String, val returnDate: String) {
    override fun toString(): String {
        return "$name $returnDate"
    }
}