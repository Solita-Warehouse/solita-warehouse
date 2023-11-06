package model
import io.github.cdimascio.dotenv.dotenv

object EnvVariableLoader {
    private val dotenv = dotenv {
        directory = "/assets"
        filename = "env"
    }

    val URL: String? by lazy { dotenv["URL"] }
    val DB: String? by lazy { dotenv["DB"] }
    val PASSWORD: String? by lazy { dotenv["PASSWORD"] }
    val USERNAME: String? by lazy { dotenv["USERNAME"] }
    val PASSWORD_LOCAL: String? by lazy { dotenv["PASSWORD_LOCAL"] }
    val URL_LOCAL: String? by lazy { dotenv["URL_LOCAL"] }
}