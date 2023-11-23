package model

import android.util.Log

class RentedItem(var partnerId: Int,  var id: Int, var orderId: Int, var productId: Int, var orderName: String, var renter: String, var name: String, var state: String, var endDate: String) {
    companion object {
        fun createFromApiData(apiData: Map<*, *>): RentedItem? {
            val id = apiData["id"] as? Int ?: return null
            val name = apiData["name"]?.toString() ?: ""
            val state = apiData["state"]?.toString() ?: ""
            val endDate = apiData["end_date"]?.toString() ?: ""
            val partnerId = extractPartnerId(apiData["order_partner_id"])
            val renter = extractRenter(apiData["order_partner_id"])
            val orderId = extractOrderId(apiData["order_id"])
            val productId = extractProductId(apiData["product_id"])


            val rentedItem = RentedItem(partnerId, id, orderId, productId, name, renter, name, state, endDate)
            Log.i("odoo-rented", "$rentedItem")
            return rentedItem
        }

        private fun extractPartnerId(partnerIdData: Any?): Int {
            return when (partnerIdData) {
                is Int -> partnerIdData
                is Array<*> -> partnerIdData[0] as? Int ?: 0
                else -> 0
            }
        }

        private fun extractRenter(partnerIdData: Any?): String {
            return when (partnerIdData) {
                is Array<*> -> {
                    val renterData = partnerIdData.getOrNull(1)
                    renterData?.toString() ?: ""
                }
                else -> ""
            }
        }

        private fun extractOrderId(orderIdData: Any?): Int {
            return when (orderIdData) {
                is Array<*> -> orderIdData[0] as? Int ?: 0
                else -> 0
            }
        }

        private fun extractProductId(productIdData: Any?): Int {
            return when (productIdData) {
                is Array<*> -> productIdData[0] as? Int ?: 0
                else -> 0
            }
        }
    }
    override fun toString(): String {
        return "name: $name - id: $id - partner_id: $partnerId - order_id: $orderId - order_name: $orderName - product_id: $productId - renter: $renter - state: $state"
    }
}