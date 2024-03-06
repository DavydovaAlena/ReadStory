package ru.adavydova.booksmart.presentation.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppBottomBar(
    items: List<BottomNavigationItem>,
    select: Int,
    onItemClick: (String) -> Unit
) {

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.fillMaxWidth()
    ) {

        items.forEachIndexed { index, bottomNavigationItem ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                ),
                selected = select == index,
                onClick = { onItemClick(bottomNavigationItem.route) },
                icon = {
                    Icon(
                        contentDescription = null,
                        imageVector =
                        if (select == index)
                            bottomNavigationItem.selectedIcon
                        else bottomNavigationItem.unselectedIcon,
                    )
                },
                label = {
                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        text = bottomNavigationItem.name
                    )
                })
        }

    }
}


@Composable
fun BottomBarTabs(
    tabs: List<BottomNavigationItem>,
    selectedTab: Int,
    onTabSelected: (BottomNavigationItem) -> Unit
) {


    val animatedSelectedTabIndex by animateFloatAsState(
        targetValue = selectedTab.toFloat(),
        label = "animatedSelectedTabIndex",
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioLowBouncy
        )
    )

    val animationColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.primaryContainer,
        label = "animationColor",
        animationSpec = spring(
            stiffness = Spring.StiffnessLow
        )
    )


    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .blur(50.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded),
    ){
        val tabWidth = size.width/tabs.size
        drawCircle(
            color = animationColor.copy(alpha = .6f),
            radius = size.height/2,
            center = Offset(
                (tabWidth * animatedSelectedTabIndex) + tabWidth /2,
                size.height /2
            )
        )
    }

    CompositionLocalProvider(
        LocalTextStyle provides LocalTextStyle.current.copy(
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        ),
        LocalContentColor provides Color.White,
    ) {

        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            for (tab in tabs) {
                val alpha by animateFloatAsState(
                    targetValue = if (selectedTab == tabs.indexOf(tab)) 1f else .35f,
                    label = "alpha"
                )
                val scale by animateFloatAsState(
                    targetValue = if (selectedTab == tabs.indexOf(tab)) 1f else .98f,
                    visibilityThreshold = .000001f,
                    animationSpec = spring(
                        stiffness = Spring.StiffnessLow,
                        dampingRatio = Spring.DampingRatioMediumBouncy
                    ),
                    label = "scale"
                )

                Column(
                    modifier = Modifier
                        .scale(scale)
                        .alpha(alpha)
                        .weight(1f)
                        .pointerInput(Unit) {
                            detectTapGestures {
                                onTabSelected(tab)
                            }
                        }
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Icon(
                        imageVector = tab.selectedIcon,
                        contentDescription = null
                    )
                    Text(text = tab.name)
                }

            }

        }
    }
}

