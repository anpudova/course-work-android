@file:OptIn(ExperimentalMaterial3Api::class)

package com.feature.materials.impl.ui

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.feature.materials.api.model.Material
import com.itis.core.utils.PreferencesManager
import org.json.JSONObject
import kotlin.random.Random
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Divider
import androidx.compose.ui.graphics.Color

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Composable
fun MaterialScreen(
    navController: NavController,
    viewModel: MaterialViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    MaterialContent(
        viewState = state.value,
        eventHandler = viewModel::event
    )

    MaterialActions(
        navController = navController,
        viewAction = action
    )
}

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Composable
fun MaterialContent(
    viewState: MaterialViewState,
    eventHandler: (MaterialEvent) -> Unit,
) {

    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)

    val materials = readDataFromJsonFile(context)
    var listMaterials = remember {
        mutableStateListOf(Material(0, "", "", ""))
    }
    if (listMaterials.size <= 1) {
        listMaterials.removeAt(0)
        if (materials != null) {
            for (i in materials.indices)
                listMaterials.add(Material(
                    Random.nextLong(1, 1000000),
                    materials[i].getString("topic"),
                    materials[i].getString("definition"),
                    materials[i].getString("explanation")))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // Toolbar
        TopAppBar(
            title = {
                Text(
                    text = "Материалы",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(bottom = 8.dp)
        )

        // List
        if (listMaterials.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(items = listMaterials, key = { it.id }) {
                    MaterialItem(material = it, eventHandler = eventHandler, preferencesManager = preferencesManager)
                    Divider(color = Color(0xFFC2C2C2), thickness = 1.dp)
                }
            }
        }
    }
}

@Composable
private fun MaterialActions(
    navController: NavController,
    viewAction: MaterialAction?
) {
    LaunchedEffect(viewAction) {
        when (viewAction) {
            null -> Unit
            MaterialAction.NavigateDetails -> {
                navController.navigate("material")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Composable
fun MaterialItem(
    material: Material,
    eventHandler: (MaterialEvent) -> Unit,
    preferencesManager: PreferencesManager) {

    Row(
        modifier = Modifier
            .clickable(
                enabled = true,
                onClickLabel = null,
                role = null,
                onClick = {
                    eventHandler.invoke(MaterialEvent.OnMaterialClick)
                    preferencesManager.saveDataLong("id-material", material.id)
                    preferencesManager.saveDataString("name-material", material.name)
                    preferencesManager.saveDataString("def-material", material.definition)
                    preferencesManager.saveDataString("exp-material", material.explanation)
                }
            )
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(5.dp)
    ) {

        Text(
            text = material.name,
            modifier = Modifier
                .padding(5.dp)
        )
    }
}
/*
fun isOnline(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = cm.activeNetworkInfo
    return netInfo != null && netInfo.isConnectedOrConnecting
}
*/
fun readDataFromJsonFile(context: Context): List<JSONObject>? {
    var json: String? = null
    try {
        val inputStream = context.assets.open("dataMaterials.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        json = String(buffer, charset("UTF-8"))
    } catch (ex: Exception) {
        Log.i("TAG", ex.message.toString())
        return null
    }

    val jsonArray = JSONObject(json).getJSONArray("finance_topics")
    val selected = mutableListOf<JSONObject>()

    for (index in 0..<jsonArray.length()) {
        selected.add(jsonArray.getJSONObject(index))
    }
    return selected
}
@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Preview(showBackground = true)
@Composable
fun MaterialPreview() {
    MaterialContent(
        viewState = MaterialViewState(
            materialDataState = null
        ), {}
    )
}