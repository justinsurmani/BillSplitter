package edu.ucsb.cs.cs184.group9.billsplitter.ui.nav

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import edu.ucsb.cs.cs184.group9.billsplitter.R

// Routes
const val NAV_HOME = "home"
const val NAV_PROFILE = "profile"
const val NAV_BILL = "bill/{billId}"

// NavItems
sealed class NavItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val navRoute: String
) {
    object Home: NavItem(R.string.home, R.drawable.ic_home, NAV_HOME)
    object Profile: NavItem(R.string.profile, R.drawable.ic_profile, NAV_PROFILE)
}


@Composable
fun BottomBar(navController: NavController) {

    val navItems : List<NavItem> = listOf(NavItem.Home, NavItem.Profile)

    BottomNavigation {
        navItems.forEach { item ->
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = stringResource(id = item.title)
                    )
                },
                label = { Text(text = stringResource(id = item.title)) },
                selected = currentRoute == item.navRoute,
                onClick = { navController.navigate(item.navRoute) }
            )
        }
    }
}
