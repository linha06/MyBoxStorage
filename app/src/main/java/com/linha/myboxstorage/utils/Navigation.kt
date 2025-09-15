package com.linha.myboxstorage.utils

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItems(val title: String, val icon: ImageVector, val screen: Screen)
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ListUser : Screen("list_user")
    object Create : Screen("create")
    object Transaction : Screen("transaction")
    object Location : Screen("location")
    object DetailPedagang : Screen("detailPedagang/{pedagangId}") {
        fun createRoute(pedagangId: Long): String {
            return "detailPedagang/$pedagangId"
        }
    }
    object DetailTransaksi : Screen("detailTransaksi/{transaksiId}") {
        fun createRoute(transaksiId: Long): String {
            return "detailTransaksi/$transaksiId"
        }
    }
    object CreateGerobak : Screen("createGerobak/{pedagangId}") {
        fun createRoute(pedagangId: Long): String {
            return "createGerobak/$pedagangId"
        }
    }
}