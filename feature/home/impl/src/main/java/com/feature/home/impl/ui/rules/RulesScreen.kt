@file:OptIn(ExperimentalMaterial3Api::class)

package com.feature.home.impl.ui.rules

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.feature.home.api.model.Rule
import com.itis.core.utils.PreferencesManager
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random


@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Composable
fun RulesScreen(
    navController: NavController,
    viewModel: RulesViewModel = hiltViewModel()
) {
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    RulesContent(
        eventHandler = viewModel::event
    )

    RulesActions(
        navController = navController,
        viewAction = action
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Composable
fun RulesContent(
    eventHandler: (RulesEvent) -> Unit,
) {

    val context = LocalContext.current
    val rules = readDataFromJsonFileRules(context)
    var listRules = remember {
        mutableStateListOf(Rule(0, "", ""))
    }
    if (listRules.size <= 1) {
        listRules.removeAt(0)
        if (rules != null) {
            for (i in rules.indices)
                listRules.add(Rule(
                    Random.nextLong(1, 1000000),
                    rules[i].getString("name"),
                    rules[i].getString("description")))
        }
    }

    val activity = context as Activity
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // Toolbar
        TopAppBar(
            title = {
                Text(
                    text = "Правила игры",
                    fontSize = 22.sp
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    eventHandler.invoke(RulesEvent.OnNavigateBack)
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = ""
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .wrapContentHeight(Alignment.CenterVertically)
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(items = listRules, key = { it.id }) {
                Column {
                    Text(text = it.name, modifier = Modifier
                        .padding(5.dp), fontSize = 20.sp)
                    Text(text = it.description, modifier = Modifier
                        .padding(5.dp), fontSize = 16.sp)
                }
                androidx.compose.material.Divider(
                    color = Color(0xFFC2C2C2),
                    thickness = 1.dp,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}

fun readDataFromJsonFileRules(context: Context): List<JSONObject>? {
    var json: String? = null
    try {
        val inputStream = context.assets.open("dataRules.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        json = String(buffer, charset("UTF-8"))
    } catch (ex: Exception) {
        Log.i("TAG", ex.message.toString())
        return null
    }

    val jsonArray = JSONObject(json).getJSONArray("rules")
    val selected = mutableListOf<JSONObject>()

    for (index in 0..<jsonArray.length()) {
        selected.add(jsonArray.getJSONObject(index))
    }
    return selected
}

@Composable
private fun RulesActions(
    navController: NavController,
    viewAction: RulesAction?
) {
    LaunchedEffect(viewAction) {
        when (viewAction) {
            null -> Unit
            RulesAction.NavigateBack -> {
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
@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Preview(showBackground = true)
@Composable
fun RulesPreview() {
    RulesContent(
        {}
    )
}