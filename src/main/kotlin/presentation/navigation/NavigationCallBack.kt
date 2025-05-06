package presentation.navigation

import org.example.presentation.navigation.Route

interface NavigationCallBack {
    fun onNavigate(route: Route)
    fun onFinish()
}