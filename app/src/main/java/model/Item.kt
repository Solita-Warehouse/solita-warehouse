package model

class Item(val name: String, val id: Int) {
    override fun toString(): String {
        return "Item name: $name - Item id: $id"
    }
}