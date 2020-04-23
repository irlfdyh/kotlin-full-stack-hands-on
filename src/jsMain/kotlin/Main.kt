import react.child
import react.dom.render
import react.functionalComponent
import kotlin.browser.document

fun main() {
//    document.getElementById("root")?.innerHTML = "Hello, Kotlin/JS!"
    render(document.getElementById("root")) {
        child(functionalComponent = App)
    }
}
