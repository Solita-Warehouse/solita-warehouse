package model

class RentedItem(var partnerId: Int,  var id: Int, var orderId: Int, var productId: Int, var orderName: String, var renter: String, var name: String, var state: String, var endDate: String) {
    override fun toString(): String {
        return "name: $name - id: $id - partner_id: $partnerId - order_id: $orderId - order_name: $orderName - product_id: $productId - renter: $renter - state: $state"
    }
}