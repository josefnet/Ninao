package com.costostudio.ninao.presentation.drawer.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.costostudio.ninao.presentation.drawer.Screen
import com.costostudio.ninao.presentation.drawer.drawerScreens

@Composable
fun DrawerContent(
    currentScreen: String,
    onScreenSelected: (Screen) -> Unit,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.width(280.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header avec Logo
            DrawerHeader()

            Spacer(modifier = Modifier.height(24.dp))

            // Items de navigation
            drawerScreens.forEach { screen ->
                DrawerItem(
                    screen = screen,
                    isSelected = currentScreen == screen.route,
                    onClick = {
                        onScreenSelected(screen)
                        onCloseDrawer()
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun DrawerHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo (remplacez par votre logo)
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "LOGO",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Mon Application",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun DrawerItem(
    screen: Screen,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        Color.Transparent
    }

    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = screen.icon,
            contentDescription = screen.title,
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = screen.title,
            style = MaterialTheme.typography.bodyLarge,
            color = contentColor,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}