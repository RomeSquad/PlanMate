package presentation.navigation

import org.example.presentation.navigation.Route

class NavigationController(
    private val startDestination: Route = Route.LoginScreen
) {
    private var navigationBackStack: MutableList<Route> = mutableListOf()
    private lateinit var navigationCallBack: NavigationCallBack
    private var currentRoute: Route = startDestination

    fun registerNavigationCallBack(navigationCallBack: NavigationCallBack) {
        this.navigationCallBack = navigationCallBack
        navigateTo(startDestination,false)
    }
    fun popBackStack() {
        if (navigationBackStack.isNotEmpty()) {
            val previousRoute = navigationBackStack.removeLast()
            navigateTo(previousRoute,false)
        } else {
            navigationCallBack.onFinish()
        }
    }

    fun navigateTo(destination: Route, addToBackStack: Boolean = true) {
        if (addToBackStack) {
            navigationBackStack.add(currentRoute)
        }
        println("back stack  $navigationBackStack")
        navigationCallBack.onNavigate(destination)
    }
}