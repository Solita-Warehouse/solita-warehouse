package model

class User (var userName: String, var id: Int, var partnerId: Int, var eMail: String) {
    // Custom constructor to create a User instance from Array<*>
    companion object {
        fun createUserFromData(userInformation: Array<*>): User {
            var extractedId = 0
            var extractedUserName = ""
            var extractedPartnerId = 0
            var extractedEmail = ""

            for (user in userInformation) {
                for (item in user as Map<*, *>) {
                    if (item.key == "id") {
                        extractedId = item.value as Int
                    }
                    if (item.key == "login") {
                        extractedUserName = item.value.toString()
                    }
                    if (item.key == "partner_id") {
                        if (item.value is Array<*>) {
                            for (subItem in item.value as Array<*>) {
                                if (subItem is Int) {
                                    extractedPartnerId = subItem
                                }
                            }
                        }
                    }
                }
            }

            return User(extractedUserName, extractedId, extractedPartnerId, extractedEmail)
        }
    }
    override fun toString(): String {
        return "username = $userName, id = $id, partnerId = $partnerId, email = $eMail"
    }
}