@file:OptIn(ExperimentalMaterial3Api::class)

package com.feature.home.impl.ui.game

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.itis.core.utils.PreferencesManager

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Composable
fun ResultScreen(
    navController: NavController,
    viewModel: ResultViewModel = hiltViewModel()
) {
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    ResultContent(
        eventHandler = viewModel::event
    )

    ResultActions(
        navController = navController,
        viewAction = action
    )
}

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Composable
fun ResultContent(
    eventHandler: (ResultEvent) -> Unit,
) {

    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Toolbar
        TopAppBar(
            title = {
                Text(
                    text = "Результаты",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(bottom = 8.dp)
        )
    }
}

@Composable
private fun ResultActions(
    navController: NavController,
    viewAction: ResultAction?
) {
}

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Preview(showBackground = true)
@Composable
fun ResultPreview() {
    ResultContent(
        {}
    )
}