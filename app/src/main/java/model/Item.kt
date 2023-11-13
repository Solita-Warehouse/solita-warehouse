package model

class Item(val name: String, val id: Int, var available: Boolean) {
    override fun toString(): String {
        return "Item name: $name - Item id: $id - Available : $available"
    }
    // Setter for the 'available' property
    fun setAvailableStatus(newStatus: Boolean) {
        available = newStatus
    }
}