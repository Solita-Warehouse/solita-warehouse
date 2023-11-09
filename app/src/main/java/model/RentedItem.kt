package model

class RentedItem(var partnerId: String,  var id: Int, var renter: String, var name: String, var state: String) {
    override fun toString(): String {
        return "name: $name - id: $id - partner_id: $partnerId - renter: $renter - state: $state"
    }
}