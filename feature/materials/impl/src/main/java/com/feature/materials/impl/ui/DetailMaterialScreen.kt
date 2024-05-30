@file:OptIn(ExperimentalMaterial3Api::class)

package com.feature.materials.impl.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.feature.materials.api.model.Material
import com.itis.core.utils.PreferencesManager

@Composable
fun DetailMaterialScreen(
    navController: NavController,
    viewModel: DetailMaterialViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    DetailMaterialContent(
        viewState = state.value,
        eventHandler = viewModel::event
    )

    DetailMaterialActions(
        navController = navController,
        viewAction = action
    )
}

@Composable
fun DetailMaterialContent(
    viewState: DetailMaterialViewState,
    eventHandler: (DetailMaterialEvent) -> Unit,
) {
    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)

    val id = preferencesManager.getDataLong("id-material", -1)
    val name = preferencesManager.getDataString("name-material", "")
    val definition = preferencesManager.getDataString("def-material", "")
    val explanation = preferencesManager.getDataString("exp-material", "")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // Toolbar

        TopAppBar(
            title = {
                Text(
                    text = name,
                    fontSize = 22.sp
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    eventHandler.invoke(DetailMaterialEvent.OnNavigateBack)
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = ""
                    )
                }
            }
        )

        Text(
            text = definition,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            textAlign = TextAlign.Start,
            fontSize = 18.sp
        )

        Text(
            text = explanation,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            textAlign = TextAlign.Start,
            fontSize = 18.sp
        )
    }
}

@Composable
private fun DetailMaterialActions(
    navController: NavController,
    viewAction: DetailMaterialAction?
) {
    LaunchedEffect(viewAction) {
        when (viewAction) {
            null -> Unit
            DetailMaterialAction.NavigateBack -> {
                navController.navigateUp()
            }
        }
    }
}
/*
fun isOnline(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = cm.activeNetworkInfo
    return netInfo != null && netInfo.isConnectedOrConnecting
}
*/
@Preview(showBackground = true)
@Composable
fun DetailMaterialPreview() {
    DetailMaterialContent(
        viewState = DetailMaterialViewState(
            detailDataState = null,
        ), {}
    )
}