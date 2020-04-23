import react.*
import react.dom.*
import kotlinext.js.*
import kotlinx.html.js.*
import kotlinx.coroutines.*

val scope = MainScope()

val App = functionalComponent<RProps> { _ ->
    val (shoppingList, setShoppingList) = useState(emptyList<ShoppingListItem>())

    useEffect(dependencies = listOf()) {
        scope.launch {
            setShoppingList(getShoppingList())
        }
    }

    h1 {
        +"Full Stack Shopping List"
    }
    ul {
        shoppingList.sortedByDescending(ShoppingListItem::priority).forEach { item ->

            // to delete data by clicking an item
            attrs.onClickFunction = {
                scope.launch {
                    deleteShoppingListItem(item)
                    setShoppingList(getShoppingList())
                }
            }

            li {
                key = item.toString()
                +"[${item.priority}] ${item.desc}"
            }
        }
    }

    child(
        functionalComponent = InputComponent,
        props = jsObject {
            onSubmit = { input ->
                val cartItem = ShoppingListItem(input.replace("!", ""), input.count { it == '!' })
                scope.launch {
                    addShoppingListItem(cartItem)
                    setShoppingList(getShoppingList())
                }
            }
        }
    )
}