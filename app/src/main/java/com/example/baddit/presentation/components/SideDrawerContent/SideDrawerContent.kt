package com.example.baddit.presentation.components.SideDrawerContent

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.baddit.R
import com.example.baddit.presentation.utils.CommunityDetail
import com.example.baddit.ui.theme.CustomTheme.cardBackground
import com.example.baddit.ui.theme.CustomTheme.textPrimary
import com.example.baddit.ui.theme.CustomTheme.textSecondary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SideDrawerContent(
    onExploreClick: () -> Unit,
    navController: NavHostController,
    viewModel: SideDrawerContentViewModel = hiltViewModel(),
    drawerState: DrawerState
) {
    val isLoggedIn by viewModel.isLoggedIn
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val refreshboxState = rememberPullToRefreshState()
    if (isLoggedIn) {
        LaunchedEffect("") {
            viewModel.getJoinCommunity()
        }
    } else {
        LaunchedEffect("") {
            viewModel.joinedCommunities.clear()
        }
    }

    DismissibleDrawerSheet(
        modifier = Modifier
            .width(250.dp)
            .shadow(
                elevation = 2.dp,
            ),
        windowInsets = WindowInsets(left = 15.dp, right = 15.dp, top = 20.dp, bottom = 0.dp),
        drawerContainerColor = MaterialTheme.colorScheme.cardBackground,
        drawerContentColor = MaterialTheme.colorScheme.textPrimary,
    ) {
        PullToRefreshBox(
            isRefreshing = viewModel.isRefreshing.value,
            onRefresh = { viewModel.getJoinCommunity() },
            state = refreshboxState
        ) {
            LazyColumn (verticalArrangement = Arrangement.spacedBy(20.dp)) {
                item {
                    DrawerHeader(header = "Your Communities")
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onExploreClick()
                            },
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_explore_24),
                            contentDescription = null
                        )
                        Text(text = "Explore more", style = MaterialTheme.typography.bodyMedium)
                    }
                    if (viewModel.joinedCommunities.isEmpty() && !viewModel.isRefreshing.value) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = if (isLoggedIn) "Consider joining some communities!" else "Login to see your communities",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.textSecondary
                        )
                    }
                    if (viewModel.isRefreshing.value) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Loading...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.textSecondary
                        )
                    }
                }
                items(viewModel.joinedCommunities) {
                    DrawerItem(
                        communityName = it.community.name,
                        logoUrl = it.community.logoUrl,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                navController.navigate(CommunityDetail(name = it.community.name))
                            }
                        }
                    )
                }

            }
        }
    }
}

@Composable
private fun DrawerItem(communityName: String?, logoUrl: String?, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 1.dp)
            .clickable { onClick() }
    ) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(logoUrl)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .height(22.dp)
                .aspectRatio(1f)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Column {
            Text(
                text = "r/ $communityName",
                style = MaterialTheme.typography.bodyMedium
            )
        }

    }
}

@Composable
private fun DrawerHeader(header: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = header, style = MaterialTheme.typography.titleMedium)
    }
}
