package com.costostudio.ninao.presentation.drawer


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*

class NavigationDrawerViewModel : ViewModel() {

    private val _selectedItemIndex = MutableStateFlow(0)
    val selectedItemIndex: StateFlow<Int> = _selectedItemIndex.asStateFlow()

    val drawerItems: List<NavigationItem> = listOf(
        NavigationItem("Home", Icons.Filled.Home, Icons.Outlined.Home),
        NavigationItem("Info", Icons.Filled.Info, Icons.Outlined.Info),
        NavigationItem("Edit", Icons.Filled.Edit, Icons.Outlined.Edit, badgeCount = 105),
        NavigationItem("Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
    )

    fun selectItem(index: Int) {
        _selectedItemIndex.value = index
    }
}