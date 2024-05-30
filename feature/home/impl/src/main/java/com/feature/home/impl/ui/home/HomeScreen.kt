@file:OptIn(ExperimentalMaterial3Api::class)

package com.feature.home.impl.ui.home

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.itis.core.utils.PreferencesManager
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException


@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    HomeContent(
        viewState = state.value,
        eventHandler = viewModel::event
    )

    HomeActions(
        navController = navController,
        viewAction = action
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Composable
fun HomeContent(
    viewState: HomeViewState,
    eventHandler: (HomeEvent) -> Unit
) {

    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)
    val username = preferencesManager.getDataString("username", "")
    eventHandler.invoke(HomeEvent.IsSignIn(username))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // Toolbar
        TopAppBar(
            title = {
                Text(
                    text = "FinLiteracy",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(bottom = 8.dp)
        )

        val openDialogCreate = remember { mutableStateOf(false) }
        if (openDialogCreate.value) {
            CreateRoomDialogView(
                onDismiss = {
                    openDialogCreate.value = false
                },
                preferencesManager,
                eventHandler
            )
        }

        val openDialogGroup = remember { mutableStateOf(false) }
        if (openDialogGroup.value) {
            GroupGameDialogView(
                onDismiss = {
                    openDialogGroup.value = false
                },
                preferencesManager
            )
        }

        if (viewState.dataState) {
            LazyColumn(
                modifier = Modifier
                    .wrapContentHeight(Alignment.CenterVertically)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                item {
                    OutlinedButton(
                        shape = CircleShape,
                        border = BorderStroke(width = 1.dp, color = Color(0xFF3d3f66)),
                        onClick = {
                            eventHandler.invoke(HomeEvent.OnNavigateSingleGame)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)
                    ) {
                        Text(text = "Одиночная игра")
                    }
                    OutlinedButton(
                        enabled = false,
                        shape = CircleShape,
                        border = BorderStroke(width = 1.dp, color = Color(0xFF3d3f66)),
                        onClick = {
                            openDialogGroup.value = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)
                    ) {
                        Text(text = "Групповая игра")
                    }
                    OutlinedButton(
                        enabled = false,
                        shape = CircleShape,
                        border = BorderStroke(width = 1.dp, color = Color(0xFF3d3f66)),
                        onClick = {
                            openDialogCreate.value = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)
                    ) {
                        Text(text = "Создать комнату")
                    }
                    OutlinedButton(
                        shape = CircleShape,
                        border = BorderStroke(width = 1.dp, color = Color(0xFF3d3f66)),
                        onClick = {
                            eventHandler.invoke(HomeEvent.OnNavigateRules)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp, 40.dp)
                            .weight(1F)
                    ) {
                        Text(text = "Правила игры")
                    }
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Доступно только авторизованным пользователям",
                    modifier = Modifier
                        .padding(8.dp, 30.dp, 8.dp, 8.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        eventHandler.invoke(HomeEvent.OnSignInClick)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(text = "Вход")
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Composable
fun CreateRoomDialogView(onDismiss:() -> Unit,
                         preferencesManager: PreferencesManager,
                         eventHandler: (HomeEvent) -> Unit) {
    val context = LocalContext.current
    var mSocket: Socket? = null
    val activity = context as Activity
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            //shape = MaterialTheme.shapes.medium,
            shape = RoundedCornerShape(10.dp),
            // modifier = modifier.size(280.dp, 240.dp)
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                Modifier
                    .background(Color.White)
            ) {

                Text(
                    text = "Уверены что хотите создать комнату?",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 20.sp
                )

                Row {
                    OutlinedButton(
                        border = BorderStroke(width = 1.dp, color = Color(0xFF3d3f66)),
                        onClick = { onDismiss() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)
                    ) {
                        Text(text = "Закрыть")
                    }

                    Button(
                        onClick = {
                            try {
                                mSocket = IO.socket("")
                            } catch (e: URISyntaxException) {
                                Log.i("TAG", "CATCH SOCKET: $e")
                            }

                            mSocket?.on("connectionToRoom", Emitter.Listener { args ->
                                activity.runOnUiThread(Runnable {
                                    val data = args[0] as JSONObject
                                    Log.i("onConnectionToRoom", args.toString())
                                    preferencesManager.saveDataJSONObj(data)
                                })
                            })

                            mSocket?.connect()
                            eventHandler.invoke(HomeEvent.OnNavigateLobby)
                            onDismiss() },
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)
                    ) {
                        Text(text = "Создать")
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Composable
fun GroupGameDialogView(onDismiss:() -> Unit, preferencesManager: PreferencesManager) {
    val context = LocalContext.current
    var id by remember {
        mutableStateOf("")
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            //shape = MaterialTheme.shapes.medium,
            shape = RoundedCornerShape(8.dp),
            // modifier = modifier.size(280.dp, 240.dp)
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                Modifier
                    .background(Color.White)
            ) {

                Text(
                    text = "Введите ID комнаты",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 20.sp
                )

                OutlinedTextField(
                    value = id,
                    onValueChange = { id = it },
                    modifier = Modifier.padding(8.dp),
                    label = { Text("ID") }
                )

                Row {
                    OutlinedButton(
                        border = BorderStroke(width = 1.dp, color = Color(0xFF3d3f66)),
                        onClick = { onDismiss() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)

                    ) {
                        Text(text = "Закрыть")
                    }
                    Button(
                        onClick = {
                            preferencesManager.saveDataString("id-game", id)
                            onDismiss() },
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)
                    ) {
                        Text(text = "Войти")
                    }
                }
            }
        }
    }
}
@Composable
private fun HomeActions(
    navController: NavController,
    viewAction: HomeAction?
) {
    LaunchedEffect(viewAction) {
        when (viewAction) {
            null -> Unit
            HomeAction.NavigateRules -> {
                navController.navigate("rules")
            }
            HomeAction.NavigateSingleGame -> {
                navController.navigate("game")
            }
            HomeAction.NavigateGroupGame -> {
                navController.navigate("groupGame")
            }
            HomeAction.NavigateLobby -> {
                navController.navigate("lobby")
            }
            HomeAction.NavigateSignIn -> {
                navController.navigate("signin")
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
fun HomePreview() {
    HomeContent(
        viewState = HomeViewState(
            dataState = false
        ), {}
    )
}