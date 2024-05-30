@file:OptIn(ExperimentalMaterial3Api::class)

package com.feature.profile.impl.ui.auth.signup

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.itis.core.utils.PreferencesManager
import kotlin.math.roundToLong
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    SignUpContent(
        viewState = state.value,
        eventHandler = viewModel::event
    )

    SignUpActions(
        navController = navController,
        viewAction = action
    )
}

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Composable
fun SignUpContent(
    viewState: SignUpViewState,
    eventHandler: (SignUpEvent) -> Unit,
) {
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var rpassword by remember { mutableStateOf(TextFieldValue("")) }

    val preferencesManager = PreferencesManager(LocalContext.current)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Регистрация",
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(text = "Имя пользователя") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Пароль") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        TextField(
            value = rpassword,
            onValueChange = { rpassword = it },
            label = { Text(text = "Повторите пароль") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Text(
            text = viewState.messageState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            color = Color.Red
        )

        Button(
            onClick = {
                val id = (Math.random() * 10000000 + 1).roundToLong()
                eventHandler.invoke(
                    SignUpEvent.OnSignUpClick(
                        id,
                        username.text,
                        password.text,
                        rpassword.text
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Зарегистрироваться")
        }
    }
}

@Composable
private fun SignUpActions(
    navController: NavController,
    viewAction: SignUpAction?
) {
    LaunchedEffect(viewAction) {
        when (viewAction) {
            null -> Unit
            SignUpAction.NavigateSignIn -> {
                navController.navigate("signin")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Preview(showBackground = true)
@Composable
fun SignUpPreview() {
    SignUpContent(
        viewState = SignUpViewState(
            messageState = ""
        ), {}
    )
}