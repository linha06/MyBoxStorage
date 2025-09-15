package com.linha.myboxstorage.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.linha.myboxstorage.repo.GerobakRepository
import com.linha.myboxstorage.repo.LaporanPembayaranRepository
import com.linha.myboxstorage.repo.LokasiPenitipanRepository
import com.linha.myboxstorage.repo.PedagangRepository
import com.linha.myboxstorage.repo.TransaksiPenitipanRepository
import com.linha.myboxstorage.ui.theme.minimalist.MinimalistAppTheme
import com.linha.myboxstorage.utils.NavigationItems
import com.linha.myboxstorage.utils.Screen
import com.linha.myboxstorage.utils.toRupiahFormat
import com.linha.myboxstorage.viewmodel.GerobakViewModel
import com.linha.myboxstorage.viewmodel.PedagangViewModel
import com.linha.myboxstorage.viewmodel.TransaksiPenitipanViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    pedagangRepo: PedagangRepository,
    gerobakRepo: GerobakRepository,
    lokasiPenitipanRepo: LokasiPenitipanRepository,
    transaksiPenitipanRepo: TransaksiPenitipanRepository,
    laporanPembayaranRepo: LaporanPembayaranRepository,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
    ) {
        composable(Screen.Home.route) {
            ScaffoldMain(navController, content = { innerPadding ->
                HomeScreen(
                    navController = navController,
                    pedagangRepo = pedagangRepo,
                    gerobakRepo = gerobakRepo,
                    transaksiRepo = transaksiPenitipanRepo,
                    modifier = Modifier.padding(innerPadding),
                    laporanPembayaranRepo = laporanPembayaranRepo
                )
            })
        }
        composable(Screen.ListUser.route) {
            ScaffoldMain(navController, content = { innerPadding ->
                ShowPedagangScreen(
                    pedagangRepo = pedagangRepo,
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
            })
        }
        composable(Screen.Create.route) {
            ScaffoldMain(navController, content = { innerPadding ->
                CreateFormScreen(
                    pedagangRepo = pedagangRepo,
                    gerobakRepo = gerobakRepo,
                    modifier = Modifier.padding(innerPadding),
                    lokasiRepo = lokasiPenitipanRepo,
                    transaksiRepo = transaksiPenitipanRepo,
                    laporanPembayaranRepo = laporanPembayaranRepo
                )
            })
        }
        composable(Screen.Transaction.route) {
            ScaffoldMain(navController, content = { innerPadding ->
                ShowTransaksiPenitipanScreen(
                    transaksiPenitipanRepo,
                    navController = navController,
                    modifier = Modifier.padding(innerPadding),
                    laporanPembayaranRepo = laporanPembayaranRepo
                )
            })
        }
        composable(Screen.Location.route) {
            ScaffoldMain(navController, content = { innerPadding ->
                ShowLokasiPenitipanScreen(
                    lokasiPenitipanRepo = lokasiPenitipanRepo,
                    modifier = Modifier.padding(innerPadding)
                )
            })
        }

        composable(
            route = Screen.DetailPedagang.route,
            arguments = listOf(navArgument("pedagangId") {
                type = NavType.LongType
            })
        ) { backStackEntry ->
            val pedagangId = backStackEntry.arguments?.getLong("pedagangId") ?: return@composable
            DetailPedagangScreen(
                id = pedagangId,
                pedagangRepo = pedagangRepo,
                gerobakRepo = gerobakRepo,
                navController = navController,
                lokasiPenitipanRepo = lokasiPenitipanRepo,
            )
        }
        
        composable(
            route = Screen.DetailTransaksi.route,
            arguments = listOf(navArgument("transaksiId") {
                type = NavType.LongType
            })
        ) { backStackEntry ->
            val transaksiId = backStackEntry.arguments?.getLong("transaksiId") ?: return@composable
            DetailTransaksiPenitipanScreen(
                id = transaksiId,
                transaksiPenitipanRepo = transaksiPenitipanRepo,
                laporanPembayaranRepo = laporanPembayaranRepo,
            )
        }

        composable(
            route = Screen.CreateGerobak.route,
            arguments = listOf(navArgument("pedagangId") {
                type = NavType.LongType
            })
        ) { backStackEntry ->
            val pedagangId = backStackEntry.arguments?.getLong("pedagangId") ?: return@composable
            CreateGerobakScreen(
                id = pedagangId,
                pedagangRepo = pedagangRepo,
                gerobakRepo = gerobakRepo,
                lokasiRepo = lokasiPenitipanRepo,
                transaksiRepo = transaksiPenitipanRepo,
                laporanPembayaranRepo = laporanPembayaranRepo,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldMain(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                ),
                title = { Text("MyBoxStorage") },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            BottomBar(navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.Create.route)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    pedagangRepo: PedagangRepository,
    gerobakRepo: GerobakRepository,
    transaksiRepo: TransaksiPenitipanRepository,
    laporanPembayaranRepo: LaporanPembayaranRepository,
) {
    val pedagangViewModel: PedagangViewModel =
        viewModel(factory = PedagangViewModel.PedagangViewModelFactory(pedagangRepo))
    val pedagang by pedagangViewModel.pedagang.observeAsState()

    val gerobakViewModel: GerobakViewModel =
        viewModel(factory = GerobakViewModel.GerobakViewModelFactory(gerobakRepo))
    val gerobak by gerobakViewModel.gerobakList.observeAsState()

    val transaksiViewModel: TransaksiPenitipanViewModel =
        viewModel(
            factory = TransaksiPenitipanViewModel.TransaksiPenitipanViewModelFactory(
                transaksiRepo, laporanPembayaranRepo
            )
        )
    val totalPendapatan by transaksiViewModel.totalPendapatan.observeAsState()
    val totalPendapatanFormat = totalPendapatan?.toRupiahFormat()
    val jatuhTempoCount by transaksiViewModel.getExpiredTransaksiCount().observeAsState(0)

    LaunchedEffect(gerobak) {
        transaksiViewModel.calculateTotalPendapatanBulanIni(gerobak)
    }


    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        CardDashboard(
            textTitle = "Pedagang Aktif",
            icon = Icons.Filled.Person,
            textMain = if (pedagang?.size != null) "${pedagang?.size}" else "0",
            textSub = " Anggota",
            onClickable = {
                navController.navigate(Screen.ListUser.route)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        CardDashboard(
            textTitle = "Jumlah Gerobak Aktif",
            icon = Icons.Filled.ShoppingCart,
            textMain = if (gerobak?.size != null) "${gerobak?.size}" else "0",
            textSub = " Total",
            onClickable = {
                navController.navigate(Screen.ListUser.route)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        CardDashboard(
            textTitle = "Total Pendapatan",
            icon = Icons.Filled.Star,
            textMain = "Rp. ",
            textSub = "$totalPendapatanFormat",
            onClickable = {
                navController.navigate(Screen.Transaction.route)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        CardDashboard(
            textTitle = "Pembayaran Jatuh Tempo",
            icon = Icons.Filled.DateRange,
            textMain = "$jatuhTempoCount",
            textSub = " Anggota",
            onClickable = {}
        )
    }
}

@Composable
fun BottomBar(navController: NavController, modifier: Modifier = Modifier) {
    NavigationBar(modifier = modifier) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val navigationItems = listOf(
            NavigationItems(
                title = "Home",
                icon = Icons.Filled.Home,
                screen = Screen.Home
            ),
            NavigationItems(
                title = "Pedagang",
                icon = Icons.Filled.Menu,
                screen = Screen.ListUser
            ),
            NavigationItems(
                title = "Create",
                icon = Icons.Filled.AddCircle,
                screen = Screen.Create
            ),
            NavigationItems(
                title = "Transaksi",
                icon = Icons.AutoMirrored.Filled.List,
                screen = Screen.Transaction
            ),
            NavigationItems(
                title = "Lokasi",
                icon = Icons.Filled.LocationOn,
                screen = Screen.Location
            ),
        )
        navigationItems.map { item ->
            NavigationBarItem(
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) }
            )
        }
    }
}

@Composable
fun CardDashboard(
    modifier: Modifier = Modifier,
    textTitle: String,
    icon: ImageVector,
    textMain: String,
    textSub: String,
    onClickable: () -> Unit
) {
    Box(
        modifier = modifier
            .background(Color(0xFF47A1AF), shape = RoundedCornerShape(12.dp))
            .border(1.dp, Color.White, shape = RoundedCornerShape(12.dp))
            .size(339.dp, 136.dp)
            .padding(20.dp)
            .clickable { onClickable() }
    ) {
        Text(
            text = textTitle,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.TopStart)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    text = textMain,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = textSub,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 4.dp)
                )
            }
            Icon(
                imageVector = icon,
                contentDescription = "Icon Pedagang",
                tint = Color.White,
                modifier = Modifier.size(65.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MinimalistAppTheme {
//        HomeScreen(navController = rememberNavController())
    }
}