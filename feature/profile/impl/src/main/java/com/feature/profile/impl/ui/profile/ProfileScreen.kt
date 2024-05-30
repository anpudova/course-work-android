@file:OptIn(ExperimentalMaterial3Api::class)

package com.feature.profile.impl.ui.profile

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.itis.core.utils.PreferencesManager

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    ProfileContent(
        viewState = state.value,
        eventHandler = viewModel::event
    )

    ProfileActions(
        navController = navController,
        viewAction = action
    )
}

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Composable
fun ProfileContent(
    viewState: ProfileViewState,
    eventHandler: (ProfileEvent) -> Unit,
) {
    val preferencesManager = PreferencesManager(LocalContext.current)
    val username = preferencesManager.getDataString("username", "")
    eventHandler.invoke(ProfileEvent.IsSignIn(username))
    eventHandler.invoke(ProfileEvent.GetRecord(username))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Профиль",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        if (viewState.dataState) {
            Text(
                    text = "Имя пользователя: $username",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp, start = 16.dp)
            )
            Text(
                text = "Рекорд: ${viewState.recordState}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp, start = 16.dp)
            )
            Button(
                onClick = {
                    preferencesManager.deleteData()
                    eventHandler.invoke(ProfileEvent.OnProfileClick)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp, 32.dp, 8.dp, 8.dp)
            ) {
                Text(text = "Выход")
            }

            Button(
                onClick = {
                    preferencesManager.deleteData()
                    eventHandler.invoke(ProfileEvent.OnDeleteAccClick(username))
                    eventHandler.invoke(ProfileEvent.OnProfileClick)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = "Удалить аккаунт")
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
                        eventHandler.invoke(ProfileEvent.OnSignInClick)
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

@Composable
private fun ProfileActions(
    navController: NavController,
    viewAction: ProfileAction?
) {
    LaunchedEffect(viewAction) {
        when (viewAction) {
            null -> Unit
            ProfileAction.NavigateSignIn -> {
                navController.navigate("signin")
            }
            ProfileAction.NavigateProfile -> {
                navController.navigate("profile")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    ProfileContent(
        viewState = ProfileViewState(
            dataState = false
        ), {}
    )
}