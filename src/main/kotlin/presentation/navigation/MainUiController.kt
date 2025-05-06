package presentation.navigation

import org.example.logic.usecase.auth.InsertUserUseCase
import org.example.logic.usecase.auth.LoginUseCase
import org.example.presentation.action.InsertUserMenuAction
import org.example.presentation.action.LoginMenuAction
import org.example.presentation.action.MateMenuAction
import org.example.presentation.navigation.Route
import presentation.io.InputReader
import presentation.io.UiDisplayer
import kotlin.system.exitProcess

class MainUiController(
    private val navigationController: NavigationController,
    private val viewer: UiDisplayer,
    private val reader: InputReader,
    private val loginUseCase: LoginUseCase,
    private val insertUserUseCase: InsertUserUseCase
) : NavigationCallBack {
    init {
        navigationController.registerNavigationCallBack(this)
    }

    override fun onNavigate(route: Route) {
        when (route) {
            Route.LoginScreen -> LoginMenuAction(
                loginUseCase = loginUseCase,
                ui = viewer,
                inputReader = reader,
                navigateToMateScreen = { navigationController.navigateTo(Route.MateScreen) },
                navigateToAdminScreen = { navigationController.navigateTo(Route.AdminScreen) },
                retryAgain = { navigationController.navigateTo(Route.LoginScreen,false) },
                navigateBack = { navigationController.popBackStack() }
            ).invoke()
            Route.AdminScreen -> TODO()
            Route.EditProjectScreen -> TODO()
            Route.InsertProjectScreen -> TODO()
            Route.InsertUserScreen -> InsertUserMenuAction(
                insertUserUseCase = insertUserUseCase,
                ui = viewer,
                inputReader = reader,
                navigateBack = { navigationController.popBackStack() }
            ).invoke()
            Route.MateScreen -> MateMenuAction(
                ui = viewer,
                inputReader = reader,
                retryAgain = { navigationController.navigateTo(Route.MateScreen,false) },
                navigateBack = { navigationController.popBackStack() },
                exit = { onFinish() }
            ).invoke()
            Route.RegisterScreen -> TODO()
        }
    }

    override fun onFinish() {
        viewer.displayMessage("Exiting...")
        exitProcess(0)
    }
}