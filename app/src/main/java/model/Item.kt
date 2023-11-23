package model

class Item(val name: String, val id: Int, var quantity: Double) {
    companion object {
        fun createFromApiData(apiData: Map<*, *>): Item? {
            val name = apiData["name"]?.toString() ?: return null
            val id = apiData["id"] as? Int ?: return null
            val rental = apiData["rental"] as? Boolean ?: return null
            val quantity = (apiData["virtual_available"] as? Double) ?: 0.0

            if (rental) {
                return Item(name, id, quantity)
            }

            return null
        }
    }
    override fun toString(): String {
        return "Item name: $name - Item id: $id  - Item quantity: $quantity"
    }
}