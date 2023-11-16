package model

class Item(val name: String, val id: Int, var availability: String) {
    override fun toString(): String {
        return "Item name: $name - Item id: $id - Available : $availability"
    }
    // Setter for the 'available' property
    fun setAvailableStatus(newStatus: String) {
        availability = newStatus
    }
}