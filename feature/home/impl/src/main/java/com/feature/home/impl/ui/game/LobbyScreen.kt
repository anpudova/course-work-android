@file:OptIn(ExperimentalMaterial3Api::class)

package com.feature.home.impl.ui.game

import android.os.Build
import android.util.Log
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
fun LobbyScreen(
    navController: NavController,
    viewModel: LobbyViewModel = hiltViewModel()
) {
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    LobbyContent(
        eventHandler = viewModel::event
    )

    LobbyActions(
        navController = navController,
        viewAction = action
    )
}

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Composable
fun LobbyContent(
    eventHandler: (LobbyEvent) -> Unit,
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
                    text = "Lobby",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(bottom = 8.dp)
        )

        LazyColumn {
            Log.i("TAGID", preferencesManager.getDataJSONObj(0).toString())
        }
    }
}

@Composable
private fun LobbyActions(
    navController: NavController,
    viewAction: LobbyAction?
) {
}

/*
fun isOnline(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = cm.activeNetworkInfo
    return netInfo != null && netInfo.isConnectedOrConnecting
}
*/
@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Preview(showBackground = true)
@Composable
fun LobbyPreview() {
    LobbyContent(
        {}
    )
}