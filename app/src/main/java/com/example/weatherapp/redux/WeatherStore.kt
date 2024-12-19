import com.example.weatherapp.redux.WeatherAction
import com.example.weatherapp.redux.WeatherState
import com.example.weatherapp.redux.weatherReducer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.reduxkotlin.Store
import org.reduxkotlin.applyMiddleware
import org.reduxkotlin.createThreadSafeStore
import org.reduxkotlin.middleware

object WeatherStore {
    private val middleware = middleware<WeatherState> { store, next, action ->
        println("Dispatching action: $action")
        val result = next(action)
        println("New state: ${store.state}")
        result
    }

    private val _store: Store<WeatherState> = createThreadSafeStore(
        reducer = weatherReducer,
        preloadedState = WeatherState(),
        enhancer = applyMiddleware(middleware)
    )

    private val _stateFlow = MutableStateFlow(_store.state)

    init {
        _store.subscribe {
            _stateFlow.value = _store.state
        }
    }

    val stateFlow: StateFlow<WeatherState> = _stateFlow

    fun dispatch(action: WeatherAction) {
        _store.dispatch(action)
    }
}
