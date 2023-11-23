package model

class Item(val name: String, val id: Int, var availability: String, var quantity: Double) {
    override fun toString(): String {
        return "Item name: $name - Item id: $id - Available : $availability - Item quantity: $quantity"
    }
    // Setter for the 'available' property
    fun setAvailableStatus(newStatus: String) {
        availability = newStatus
    }
}