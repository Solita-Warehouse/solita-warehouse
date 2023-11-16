package model

class RentedItem(var partnerId: Int,  var id: Int, var renter: String, var name: String, var state: String, var endDate: String) {
    override fun toString(): String {
        return "name: $name - id: $id - partner_id: $partnerId - renter: $renter - state: $state"
    }
}