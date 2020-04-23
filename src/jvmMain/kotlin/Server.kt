
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.litote.kmongo.*
import org.litote.kmongo.async.*
import org.litote.kmongo.coroutine.*
import org.litote.kmongo.async.getCollection
import com.mongodb.ConnectionString

fun main() {
    embeddedServer(Netty, 9090) {

        // ContentNegotiation provides automatic content conversion of requests
        // based on Content-Type and Accept headers. Together with the json() setting
        install(ContentNegotiation){
            json()
        }

        // allow us later to make calls from arbitrary JavaScripts clients, and help
        // us prevent issues down the line.
        install(CORS) {
            method(HttpMethod.Get)
            method(HttpMethod.Post)
            method(HttpMethod.Delete)
            anyHost()
        }

        // Greatly reduces the amount of data that's needed to be sent th the client
        // by "gzip" ping outgoing content when applicable.
        install(Compression) {
            gzip()
        }

        routing {
            get("/hello") {
                call.respond("Hello...it's working")
            }
            route(ShoppingListItem.path) {
                get {
                    call.respond(shoppingList)
                }
                post {
                    shoppingList += call.receive<ShoppingListItem>()
                    call.respond(HttpStatusCode.OK)
                }
                delete("/{id}") {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid delete request")
                    shoppingList.removeIf { it.id == id }
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }.start(wait = true)
}

val shoppingList = mutableListOf(
    ShoppingListItem("Cucumbers 🥒", 1),
    ShoppingListItem("Tomatoes 🍅", 2),
    ShoppingListItem("Orange Juice 🍊", 3)
)