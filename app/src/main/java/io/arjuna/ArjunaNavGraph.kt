package io.arjuna

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.arjuna.blockedwebsites.BlockedWebsite
import io.arjuna.blockedwebsites.BlockedWebsitesViewModel

@Composable
fun ArjunaNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ArjunaDestinations.HOME,
    blockedWebsitesViewModel: BlockedWebsitesViewModel
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(ArjunaDestinations.HOME) {
            val blockedWebsites by blockedWebsitesViewModel.blockedWebsites
                .collectAsState(initial = emptySet())
            HomeComposable(blockedWebsites,
                onWebsiteRemove = { websiteToRemove: BlockedWebsite ->
                    blockedWebsitesViewModel.removeWebsiteToBlock(websiteToRemove)
                },
                onWebsiteAdd = { websiteToBlock: String? ->
                    if (websiteToBlock != null) {
                        blockedWebsitesViewModel.addWebsiteToBlock(
                            BlockedWebsite(mainDomain = websiteToBlock)
                        )
                    }
                }
            )
        }
    }
}

object ArjunaScreens {
    const val HOME = "home"
}

object ArjunaDestinations {
    const val HOME = "/${ArjunaScreens.HOME}"
}
