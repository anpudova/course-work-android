@file:OptIn(ExperimentalMaterial3Api::class)

package com.feature.home.impl.ui.game

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.itis.core.utils.PreferencesManager
import org.json.JSONObject
import kotlin.random.Random
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import com.feature.home.api.model.Credit
import kotlin.math.pow


@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Composable
fun GameScreen(
    navController: NavController,
    viewModel: GameViewModel = hiltViewModel()
) {
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    GameContent(
        eventHandler = viewModel::event
    )

    GameActions(
        navController = navController,
        viewAction = action
    )
}

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Composable
fun GameContent(
    eventHandler: (GameEvent) -> Unit,
) {

    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)
    val step = remember { mutableStateOf(1) }

    var sumMinLiveExpenses = remember { mutableStateOf(300000) }
    var sumCourses = remember { mutableStateOf(160000) }
    var sumMgstr = remember { mutableStateOf(220000) }
    var sumCasco = remember { mutableStateOf(21800) }
    var sumDms = remember { mutableStateOf(11000) }
    var sumApartment = remember { mutableStateOf(32000) }

    var btnMinLiveExpenses = remember { mutableStateOf(true) }
    var btnCourses = remember { mutableStateOf(true) }
    var btnMgstr = remember { mutableStateOf(true) }
    var btnCasco = remember { mutableStateOf(true) }
    var btnDms = remember { mutableStateOf(true) }
    var btnApartment = remember { mutableStateOf(true) }

    var btnNext = remember { mutableStateOf(false) }

    var paymentMinLiveExpenses = remember { mutableStateOf(false) }
    var paymentCourses = remember { mutableStateOf(false) }
    var paymentMgstr = remember { mutableStateOf(false) }
    var paymentCasco = remember { mutableStateOf(false) }
    var paymentDms = remember { mutableStateOf(false) }
    var paymentApartment = remember { mutableStateOf(false) }

    var paymentExpenses = remember { mutableStateOf(false) }

    val money = remember { mutableStateOf(600000.0) }
    val salary = remember { mutableStateOf(600000) }
    val joy = remember { mutableStateOf(70) }
    var inflation = remember { mutableStateOf(0) }

    val credits = remember { mutableStateListOf(Credit(0, 0, 0, 0, 0F, 0)) }
    var sumCredit by remember { mutableStateOf("") }
    val isError = sumCredit.isNotEmpty() && !sumCredit.all { it.isDigit() }
    var yearsCredit by remember { mutableStateOf(1F) }
    var paymentPerYear = remember { mutableStateOf(0F) }
    var percentCredit = remember { mutableStateOf(0.12) }

    var randomPercentPlus = 0.01
    var itemsRemoveCredit = arrayListOf<Int>()

    val nameOffer = remember { mutableStateOf("") }
    val descriptionOffer = remember { mutableStateOf("") }
    val priceOffer = remember { mutableStateOf(0) }
    val yesOffer = remember { mutableStateOf(0) }
    val noOffer = remember { mutableStateOf(0) }

    val count = remember { mutableStateOf(1) }
    var selectedOffers = remember { mutableStateListOf(JSONObject()) }

    var selectedAccidents = readDataFromJsonFileAccidents(context)
    val isInsurance = remember { mutableStateOf(false) }
    val isAccident = remember { mutableStateOf(0) }

    val gameOver = remember { mutableStateOf(false) }
    val loss = remember { mutableStateOf(false) }

    var isRecord = false
    val stepCount = 10

    val keyboardController = LocalSoftwareKeyboardController.current

    if (paymentMinLiveExpenses.value && paymentExpenses.value) {
        btnNext.value = true
    }

    if (money.value < 0) {
        loss.value = true
        gameOver.value = true
    }

    val openDialogNext = remember { mutableStateOf(false) }
    if (openDialogNext.value) {
        Dialog(onDismissRequest = {}) {
            Card(
                //shape = MaterialTheme.shapes.medium,
                shape = RoundedCornerShape(10.dp),
                // modifier = modifier.size(280.dp, 240.dp)
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    Modifier
                        .background(Color.White)
                        .padding(10.dp)
                ) {

                    Text(
                        text = "Итоги хода",
                        modifier = Modifier.padding(8.dp),
                        fontSize = 20.sp
                    )
                    Text(
                        text = "Деньги: ${money.value.toInt()} ₽",
                        modifier = Modifier.padding(8.dp),
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Зарплата: + ${salary.value} ₽",
                        modifier = Modifier.padding(8.dp),
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Радость: ${joy.value}",
                        modifier = Modifier.padding(8.dp),
                        fontSize = 16.sp
                    )
                    if (isAccident.value == 0) {
                        Text(
                            text = "Событие: ${selectedAccidents?.getString("name")}",
                            modifier = Modifier.padding(8.dp),
                            fontSize = 16.sp
                        )
                        if (isInsurance.value) {
                            Text(
                                text = "Страховка покрыла траты",
                                modifier = Modifier.padding(8.dp, 3.dp, 8.dp, 8.dp),
                                fontSize = 16.sp
                            )
                        } else {
                            Text(
                                text = "Вы потеряли ${selectedAccidents?.getString("recovery_cost")} ₽ и ${selectedAccidents?.getString("happiness_loss")} очков радости",
                                modifier = Modifier.padding(8.dp, 3.dp, 8.dp, 8.dp),
                                fontSize = 16.sp
                            )
                        }
                    }
                    OutlinedButton(
                        border = BorderStroke(width = 1.dp, color = Color(0xFF3d3f66)),
                        onClick = {
                            paymentCasco.value = false
                            paymentApartment.value = false
                            paymentDms.value = false
                            paymentExpenses.value = false
                            paymentMinLiveExpenses.value = false
                            isInsurance.value = false
                            selectedAccidents = readDataFromJsonFileAccidents(context)
                            inflation.value = Random.nextInt(4, 8)
                            sumMinLiveExpenses.value = sumMinLiveExpenses.value * (100 + inflation.value) / 100
                            step.value++
                            salary.value = (salary.value * 1.04).toInt()
                            btnNext.value = false
                            money.value += salary.value * 0.87
                            sumCourses.value = sumCourses.value * (100 + inflation.value) / 100
                            sumApartment.value = sumApartment.value * (100 + inflation.value) / 100
                            sumCasco.value = sumCasco.value * (100 + inflation.value) / 100
                            sumDms.value = sumDms.value * (100 + inflation.value) / 100
                            sumMgstr.value = sumMgstr.value * (100 + inflation.value) / 100
                            sumMinLiveExpenses.value = sumMinLiveExpenses.value * (100 + inflation.value) / 100
                            btnMinLiveExpenses.value = money.value >= sumMinLiveExpenses.value
                            btnCourses.value = money.value >= sumCourses.value
                            btnMgstr.value = money.value >= sumMgstr.value
                            btnCasco.value = money.value >= sumCasco.value
                            btnDms.value = money.value >= sumDms.value
                            btnApartment.value = money.value >= sumApartment.value
                            randomPercentPlus = Random.nextDouble(0.0, 0.02)
                            percentCredit.value += randomPercentPlus

                            if (credits.size > 1) {
                                for (item in 1..<credits.size) {
                                    if (credits[item].years - (step.value - credits[item].step) > 0) {
                                        money.value -= credits[item].paymentPerYear
                                    } else if (credits[item].years - (step.value - credits[item].step) == 0) {
                                        money.value -= credits[item].paymentPerYear
                                        itemsRemoveCredit.add(item)
                                    }
                                }
                            }
                            //readDataFromJsonFileOffers(context)?.let { selectedOffers.addAll(it) }

                            for (item in 0..<itemsRemoveCredit.size) {
                                credits.removeAt(item)
                            }
                            count.value = 1

                            if (step.value > stepCount) {
                                eventHandler.invoke(GameEvent.GetRecord(preferencesManager.getDataString("username", "")))
                                if (joy.value >= 80) {
                                    if (preferencesManager.getDataLong("record", 0L) < money.value.toLong()) {
                                        eventHandler.invoke(GameEvent.SetRecord(preferencesManager.getDataString("username", ""), money.value.toInt()))
                                        isRecord = true
                                    }
                                }
                                gameOver.value = true

                            }
                            openDialogNext.value = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(text = "Продолжить")
                    }
                }
            }
        }
    }
    val openDialogMenu = remember { mutableStateOf(false) }
    if (openDialogMenu.value) {
        Dialog(onDismissRequest = {}) {
            Card(
                //shape = MaterialTheme.shapes.medium,
                shape = RoundedCornerShape(10.dp),
                // modifier = modifier.size(280.dp, 240.dp)
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    Modifier
                        .background(Color.White)
                        .padding(10.dp)
                ) {
                    OutlinedButton(
                        border = BorderStroke(width = 1.dp, color = Color(0xFF3d3f66)),
                        onClick = {
                            eventHandler.invoke(GameEvent.OnNavigateHome)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(text = "Выйти из игры")
                    }
                    OutlinedButton(
                        border = BorderStroke(width = 1.dp, color = Color(0xFF3d3f66)),
                        onClick = {
                            openDialogMenu.value = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(text = "Закрыть")
                    }
                }
            }
        }
    }
    if (gameOver.value) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(5.dp)
                    .align(Alignment.Center)
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(10.dp),
                        ambientColor = Color(0xF3E0E0E0)
                    ),

                ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Игра окончена!",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(15.dp)
                    )
                    if (!loss.value) {
                        Text(
                            "Тебе удалось накопить ",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                        Text(
                            "${money.value.toInt()} ₽",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(10.dp)
                        )
                        if (joy.value >= 80) {
                            Text(
                                "Новый рекорд!",
                                fontSize = 20.sp,
                                modifier = Modifier.padding(10.dp)
                            )
                            Text(
                                "Твой уровень радости - ${joy.value}",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(5.dp)
                            )
                        } else {
                            Column (Modifier.padding(5.dp)) {
                                Box{
                                    Text(
                                        "Однако деньги не принесли тебе необходимый минимум радости. Результат не будет засчитан",
                                        fontSize = 16.sp,
                                        modifier = Modifier
                                            .padding(3.dp)
                                            .align(Alignment.Center)
                                    )
                                }
                                Box {
                                    Text(
                                        "Твой уровень радости - ${joy.value}",
                                        fontSize = 16.sp,
                                        modifier = Modifier
                                            .padding(3.dp)
                                            .align(Alignment.Center)
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            "Ты стал банкротом и не смог закрыть свои долги. Попробуй еще раз!",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                    OutlinedButton(
                        enabled = true,
                        modifier = Modifier.padding(20.dp),
                        border = BorderStroke(width = 1.dp, color = Color(0xFF3d3f66)),
                        onClick = {
                            eventHandler.invoke(GameEvent.OnNavigateHome)
                        }
                    ) {
                        Text("Перейти на главную")
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            // Toolbar
            TopAppBar(
                title = {
                    Text(
                        text = "Ход ${step.value} из $stepCount",
                        fontSize = 16.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        openDialogMenu.value = true
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = ""
                        )
                    }
                },
                actions = {
                    IconButton(
                        enabled = btnNext.value,
                        onClick = {
                            isAccident.value = Random.nextInt(0, 2)
                            if (isAccident.value == 0) {
                                if (selectedAccidents?.getString("insurance") == "ДМС") {
                                    if (paymentDms.value) {
                                        isInsurance.value = true
                                    } else {
                                        money.value -= selectedAccidents!!.getString("recovery_cost").toInt()
                                        joy.value -= selectedAccidents!!.getString("happiness_loss").toInt()
                                    }
                                } else if (selectedAccidents?.getString("insurance") == "КАСКО") {
                                    if (paymentCasco.value) {
                                        isInsurance.value = true
                                    } else {
                                        money.value -= selectedAccidents!!.getString("recovery_cost").toInt()
                                        joy.value -= selectedAccidents!!.getString("happiness_loss").toInt()
                                    }
                                } else if (selectedAccidents?.getString("insurance") == "Квартира") {
                                    if (paymentApartment.value) {
                                        isInsurance.value = true
                                    } else {
                                        money.value -= selectedAccidents!!.getString("recovery_cost").toInt()
                                        joy.value -= selectedAccidents!!.getString("happiness_loss").toInt()
                                    }
                                }
                            }
                            openDialogNext.value = true
                        }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = ""
                        )
                    }
                }
            )

            // money and joy
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp),

                ) {
                // Block with three horizontally aligned components
                Card(
                    //shape = MaterialTheme.shapes.medium,
                    shape = RoundedCornerShape(10.dp),
                    // modifier = modifier.size(280.dp, 240.dp)
                    modifier = Modifier
                        .padding(5.dp)
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(10.dp),
                            ambientColor = Color(0xF3E0E0E0)
                        )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Деньги\n${money.value.toInt()} ₽",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(10.dp)
                        )
                        Text(
                            "Радость\n${joy.value}/100",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                // Block with vertically centered elements
                if (!paymentMinLiveExpenses.value) {
                    Card(
                        //shape = MaterialTheme.shapes.medium,
                        shape = RoundedCornerShape(10.dp),
                        // modifier = modifier.size(280.dp, 240.dp)
                        modifier = Modifier
                            .padding(5.dp)
                            .shadow(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(10.dp),
                                ambientColor = Color(0xF3E0E0E0)
                            ),

                        ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Минимальные расходы на жизнь",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(20.dp)
                            )
                            Text(
                                "Их нужно оплачивать каждый ход",
                                fontSize = 10.sp,
                                modifier = Modifier.padding(2.dp)
                            )
                            Text(
                                "Включают в себя ежегодные расходы на еду, транспорт, жилье и прочее",
                                fontSize = 10.sp,
                                modifier = Modifier.padding(2.dp)
                            )
                            Text(
                                "${sumMinLiveExpenses.value} ₽",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(5.dp)
                            )
                            OutlinedButton(
                                enabled = btnMinLiveExpenses.value,
                                modifier = Modifier.padding(10.dp),
                                border = BorderStroke(width = 1.dp, color = Color(0xFF3d3f66)),
                                onClick = {
                                    btnMinLiveExpenses.value = false
                                    money.value -= sumMinLiveExpenses.value
                                    paymentMinLiveExpenses.value = true
                                    btnMinLiveExpenses.value =
                                        money.value >= sumMinLiveExpenses.value
                                    btnCourses.value = money.value >= sumCourses.value
                                    btnMgstr.value = money.value >= sumMgstr.value
                                    btnCasco.value = money.value >= sumCasco.value
                                    btnDms.value = money.value >= sumDms.value
                                    btnApartment.value = money.value >= sumApartment.value
                                    if (selectedOffers.size > 1) {
                                        selectedOffers.removeRange(1, selectedOffers.size - 1)
                                    }
                                    readDataFromJsonFileOffers(context)?.let { selectedOffers.addAll(it) }
                                }
                            ) {
                                Text("Оплатить")
                            }
                        }
                    }
                } else {
                    if (!paymentExpenses.value) {
                        Card(
                            //shape = MaterialTheme.shapes.medium,
                            shape = RoundedCornerShape(10.dp),
                            // modifier = modifier.size(280.dp, 240.dp)
                            modifier = Modifier
                                .padding(5.dp)
                                .shadow(
                                    elevation = 2.dp,
                                    shape = RoundedCornerShape(10.dp),
                                    ambientColor = Color(0xF3E0E0E0)
                                ),

                            ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (selectedOffers.size > 1) {
                                    nameOffer.value =
                                        selectedOffers[count.value].getString("name")
                                    descriptionOffer.value =
                                        selectedOffers[count.value].getString("description")
                                    priceOffer.value = selectedOffers[count.value].getInt("price")
                                    yesOffer.value = selectedOffers[count.value].getInt("agree")
                                    noOffer.value = selectedOffers[count.value].getInt("disagree")
                                }
                                Text(
                                    nameOffer.value,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(20.dp, 10.dp)
                                )
                                Text(
                                    descriptionOffer.value,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(20.dp, 10.dp)
                                )
                                Text(
                                    "${priceOffer.value} ₽",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(5.dp)
                                )

                                Row {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(3.dp)
                                            .weight(1F),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    )
                                    {
                                        if (noOffer.value == 0) {
                                            Text(text = "")
                                        } else {
                                            Text(text = "- ${noOffer.value}")
                                        }
                                        OutlinedButton(
                                            border = BorderStroke(
                                                width = 1.dp,
                                                color = Color(0xFF3d3f66)
                                            ),
                                            onClick = {
                                                count.value++
                                                joy.value -= noOffer.value
                                                if (count.value == 5) {
                                                    paymentExpenses.value = true
                                                }
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(3.dp)

                                        ) {
                                            Text(text = "Отклонить")
                                        }
                                    }
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(3.dp)
                                            .weight(1F),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    )
                                    {
                                        if (yesOffer.value == 0) {
                                            Text(text = "")
                                        } else {
                                            Text(text = "+ ${yesOffer.value}")
                                        }
                                        Button(
                                            onClick = {
                                                count.value++
                                                money.value -= priceOffer.value
                                                joy.value += yesOffer.value
                                                if (count.value == 5) {
                                                    paymentExpenses.value = true
                                                }
                                            },
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(3.dp)
                                        ) {
                                            Text(text = "Оплатить")
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Card(
                            //shape = MaterialTheme.shapes.medium,
                            shape = RoundedCornerShape(10.dp),
                            // modifier = modifier.size(280.dp, 240.dp)
                            modifier = Modifier
                                .padding(5.dp, 10.dp)
                                .shadow(
                                    elevation = 2.dp,
                                    shape = RoundedCornerShape(10.dp),
                                    ambientColor = Color(0xF3E0E0E0)
                                )

                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Основные решения приняты!",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(20.dp, 10.dp)
                                )
                                Text(
                                    text = "Можете закончить ход",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(20.dp, 10.dp)
                                )
                            }
                        }
                    }
                }
                Text(
                    "Образование",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(5.dp)
                )
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    Card(
                        //shape = MaterialTheme.shapes.medium,
                        shape = RoundedCornerShape(10.dp),
                        // modifier = modifier.size(280.dp, 240.dp)
                        modifier = Modifier
                            .padding(5.dp)
                            .shadow(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(10.dp),
                                ambientColor = Color(0xF3E0E0E0)
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(5.dp)
                                .width(170.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Курсы повышения квалификации",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(3.dp)
                                    .height(45.dp)
                            )
                            if (!paymentCourses.value) {
                                Text(
                                    "${sumCourses.value} ₽",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(3.dp)
                                )
                                Text(
                                    "+ 5% к зарплате",
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(3.dp)
                                )
                                OutlinedButton(
                                    enabled = btnCourses.value,
                                    border = BorderStroke(width = 1.dp, color = Color(0xFF3d3f66)),
                                    onClick = {
                                        btnCourses.value = false
                                        money.value -= sumCourses.value
                                        paymentCourses.value = true
                                        btnMinLiveExpenses.value =
                                            money.value >= sumMinLiveExpenses.value
                                        btnCourses.value = money.value >= sumCourses.value
                                        btnMgstr.value = money.value >= sumMgstr.value
                                        btnCasco.value = money.value >= sumCasco.value
                                        btnDms.value = money.value >= sumDms.value
                                        btnApartment.value = money.value >= sumApartment.value
                                        salary.value = (salary.value * 1.05).toInt()
                                    }) {
                                    Text(
                                        "Оплатить",
                                        fontSize = 12.sp
                                    )
                                }
                            } else {
                                Text(
                                    text = "Оплачено",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(20.dp)
                                )
                            }
                        }
                    }
                    Card(
                        //shape = MaterialTheme.shapes.medium,
                        shape = RoundedCornerShape(10.dp),
                        // modifier = modifier.size(280.dp, 240.dp)
                        modifier = Modifier
                            .padding(5.dp)
                            .shadow(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(10.dp),
                                ambientColor = Color(0xF3E0E0E0)
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(5.dp)
                                .width(170.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Магистратура",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(3.dp)
                                    .height(45.dp)
                            )
                            if (!paymentMgstr.value) {
                                Text(
                                    "${sumMgstr.value} ₽",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(3.dp)
                                )
                                Text(
                                    "+ 8% к зарплате",
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(3.dp)
                                )
                                OutlinedButton(
                                    enabled = btnMgstr.value,
                                    border = BorderStroke(width = 1.dp, color = Color(0xFF3d3f66)),
                                    onClick = {
                                        btnMgstr.value = false
                                        money.value -= sumMgstr.value
                                        paymentMgstr.value = true
                                        btnMinLiveExpenses.value =
                                            money.value >= sumMinLiveExpenses.value
                                        btnCourses.value = money.value >= sumCourses.value
                                        btnMgstr.value = money.value >= sumMgstr.value
                                        btnCasco.value = money.value >= sumCasco.value
                                        btnDms.value = money.value >= sumDms.value
                                        btnApartment.value = money.value >= sumApartment.value
                                        salary.value = (salary.value * 1.08).toInt()
                                    }) {
                                    Text(
                                        "Оплатить",
                                        fontSize = 12.sp
                                    )
                                }
                            } else {
                                Text(
                                    text = "Оплачено",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                    }
                }

                Text(
                    "Страхование",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(5.dp)
                )
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    Card(
                        //shape = MaterialTheme.shapes.medium,
                        shape = RoundedCornerShape(10.dp),
                        // modifier = modifier.size(280.dp, 240.dp)
                        modifier = Modifier
                            .padding(5.dp)
                            .shadow(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(10.dp),
                                ambientColor = Color(0xF3E0E0E0)
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(5.dp)
                                .width(170.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "КАСКО",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(3.dp)
                                    .height(45.dp)
                            )
                            Text(
                                "${sumCasco.value} ₽",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(3.dp)
                            )
                            Text(
                                "за год",
                                fontSize = 10.sp,
                                modifier = Modifier.padding(3.dp)
                            )
                            if (!paymentCasco.value) {
                                OutlinedButton(
                                    enabled = btnCasco.value,
                                    border = BorderStroke(width = 1.dp, color = Color(0xFF3d3f66)),
                                    onClick = {
                                        btnCasco.value = false
                                        money.value -= sumCasco.value
                                        paymentCasco.value = true
                                        btnMinLiveExpenses.value =
                                            money.value >= sumMinLiveExpenses.value
                                        btnCourses.value = money.value >= sumCourses.value
                                        btnMgstr.value = money.value >= sumMgstr.value
                                        btnCasco.value = money.value >= sumCasco.value
                                        btnDms.value = money.value >= sumDms.value
                                        btnApartment.value = money.value >= sumApartment.value
                                    }) {
                                    Text(
                                        "Оплатить",
                                        fontSize = 12.sp
                                    )
                                }
                            } else {
                                Text(
                                    text = "Оплачено",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                    }
                    Card(
                        //shape = MaterialTheme.shapes.medium,
                        shape = RoundedCornerShape(10.dp),
                        // modifier = modifier.size(280.dp, 240.dp)
                        modifier = Modifier
                            .padding(5.dp)
                            .shadow(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(10.dp),
                                ambientColor = Color(0xF3E0E0E0)
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(5.dp)
                                .width(170.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "ДМС",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(3.dp)
                                    .height(45.dp)
                            )
                            Text(
                                "${sumDms.value} ₽",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(3.dp)
                            )
                            Text(
                                "за год",
                                fontSize = 10.sp,
                                modifier = Modifier.padding(3.dp)
                            )
                            if (!paymentDms.value) {
                                OutlinedButton(
                                    enabled = btnDms.value,
                                    border = BorderStroke(width = 1.dp, color = Color(0xFF3d3f66)),
                                    onClick = {
                                        btnDms.value = false
                                        money.value -= sumDms.value
                                        paymentDms.value = true
                                        btnMinLiveExpenses.value =
                                            money.value >= sumMinLiveExpenses.value
                                        btnCourses.value = money.value >= sumCourses.value
                                        btnMgstr.value = money.value >= sumMgstr.value
                                        btnCasco.value = money.value >= sumCasco.value
                                        btnDms.value = money.value >= sumDms.value
                                        btnApartment.value = money.value >= sumApartment.value
                                    }) {
                                    Text(
                                        "Оплатить",
                                        fontSize = 12.sp
                                    )
                                }
                            } else {
                                Text(
                                    text = "Оплачено",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                    }
                    Card(
                        //shape = MaterialTheme.shapes.medium,
                        shape = RoundedCornerShape(10.dp),
                        // modifier = modifier.size(280.dp, 240.dp)
                        modifier = Modifier
                            .padding(5.dp)
                            .shadow(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(10.dp),
                                ambientColor = Color(0xF3E0E0E0)
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(5.dp)
                                .width(170.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Квартира",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(3.dp)
                                    .height(45.dp)
                            )
                            Text(
                                "${sumApartment.value} ₽",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(3.dp)
                            )
                            Text(
                                "за год",
                                fontSize = 10.sp,
                                modifier = Modifier.padding(3.dp)
                            )
                            if (!paymentApartment.value) {
                                OutlinedButton(
                                    enabled = btnApartment.value,
                                    border = BorderStroke(width = 1.dp, color = Color(0xFF3d3f66)),
                                    onClick = {
                                        btnApartment.value = false
                                        money.value -= sumApartment.value
                                        paymentApartment.value = true
                                        btnMinLiveExpenses.value =
                                            money.value >= sumMinLiveExpenses.value
                                        btnCourses.value = money.value >= sumCourses.value
                                        btnMgstr.value = money.value >= sumMgstr.value
                                        btnCasco.value = money.value >= sumCasco.value
                                        btnDms.value = money.value >= sumDms.value
                                        btnApartment.value = money.value >= sumApartment.value
                                    }) {
                                    Text(
                                        "Оплатить",
                                        fontSize = 12.sp
                                    )
                                }
                            } else {
                                Text(
                                    text = "Оплачено",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                    }
                }
                Text(
                    "Кредит",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(5.dp)
                )
                Card(
                    //shape = MaterialTheme.shapes.medium,
                    shape = RoundedCornerShape(10.dp),
                    // modifier = modifier.size(280.dp, 240.dp)
                    modifier = Modifier
                        .padding(5.dp)
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(10.dp),
                            ambientColor = Color(0xF3E0E0E0)
                        ),

                    ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Банк",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(12.dp, 3.dp)
                        )
                        Text("${(percentCredit.value * 100).toInt()}% годовых", fontSize = 10.sp, modifier = Modifier.padding(2.dp))
                        OutlinedTextField(
                            value = sumCredit,
                            onValueChange = { input ->
                                if (input.all { it.isDigit() }) {
                                    sumCredit = input
                                }
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            isError = isError,
                            modifier = Modifier.padding(8.dp),
                            label = { Text("Сумма кредита") },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                unfocusedBorderColor = Color(0xC83D3F66),
                                unfocusedTextColor = Color(0xFFD8D8D8)
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                        Text(
                            text = "Количество лет - ${yearsCredit.toInt()}",
                            Modifier.padding(0.dp, 5.dp, 0.dp, 0.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(3F)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "1",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier
                                        .padding(3.dp)
                                        .align(Alignment.CenterEnd)
                                )
                            }

                            Spacer(
                                modifier = Modifier
                                    .width(1.dp)
                                    .weight(1F)
                            )
                            Slider(
                                value = yearsCredit,
                                onValueChange = { yearsCredit = it },
                                valueRange = 1F..(stepCount - step.value + 1).toFloat(),
                                steps = if (stepCount - step.value - 1 <= 0) {
                                    0
                                } else {
                                    stepCount - step.value - 1
                                },
                                modifier = Modifier
                                    .padding(10.dp, 5.dp)
                                    .weight(11F)
                                    .fillMaxWidth()
                            )

                            Spacer(
                                modifier = Modifier
                                    .width(1.dp)
                                    .weight(1F)
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(3F)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "${stepCount - step.value + 1}",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier
                                        .width(3.dp)
                                        .align(Alignment.CenterStart)
                                )
                            }
                        }
                        if (sumCredit != "") {
                            paymentPerYear.value = (sumCredit.toInt() * (percentCredit.value / 12)
                                    / (1 - (1 + percentCredit.value / 12).pow((-yearsCredit * 12).toInt()))).toFloat()
                            Text(
                                "Платеж в год: %.2f ₽".format(paymentPerYear.value),
                                fontSize = 16.sp, modifier = Modifier.padding(5.dp)
                            )
                        }

                        OutlinedButton(
                            enabled = (sumCredit != "" && yearsCredit != 0F),
                            modifier = Modifier.padding(10.dp),
                            border = BorderStroke(width = 1.dp, color = Color(0xFF3d3f66)),
                            onClick = {
                                money.value += sumCredit.toInt()
                                credits.add(
                                    Credit(
                                        Random.nextLong(1, 1000000),
                                        sumCredit.toInt(),
                                        (percentCredit.value * 100).toInt(),
                                        yearsCredit.toInt(),
                                        (sumCredit.toInt() * (percentCredit.value / 12)
                                                / (1 - (1 + percentCredit.value / 12).pow((-yearsCredit * 12).toInt()))).toFloat(),
                                        step.value
                                    )
                                )
                                btnMinLiveExpenses.value = money.value >= sumMinLiveExpenses.value
                                btnCourses.value = money.value >= sumCourses.value
                                btnMgstr.value = money.value >= sumMgstr.value
                                btnCasco.value = money.value >= sumCasco.value
                                btnDms.value = money.value >= sumDms.value
                                btnApartment.value = money.value >= sumApartment.value
                                yearsCredit = 1F
                                sumCredit = ""
                                keyboardController?.hide()
                            }
                        ) {
                            Text("Взять кредит")
                        }
                        if (credits.size > 1) {
                            Text(text = ("Мои кредиты"), Modifier.padding(2.dp))
                            for (item in 1..<credits.size) {
                                Row(Modifier.padding(5.dp)) {
                                    Text(
                                        text = item.toString(),
                                        Modifier
                                            .padding(2.dp)
                                            .weight(1F),
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "${credits[item].sum}",
                                        Modifier
                                            .padding(2.dp)
                                            .weight(4F),
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "Платеж в год - %.2f ₽".format(credits[item].paymentPerYear),
                                        Modifier
                                            .padding(2.dp)
                                            .weight(5F),
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "Осталось лет - ${(credits[item].years - (step.value - credits[item].step))}",
                                        Modifier
                                            .padding(2.dp)
                                            .weight(4F),
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GameActions(
    navController: NavController,
    viewAction: GameAction?
) {
    LaunchedEffect(viewAction) {
        when (viewAction) {
            null -> Unit
            GameAction.NavigateHome -> {
                navController.navigate("home")
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

fun readDataFromJsonFileOffers(context: Context): ArrayList<JSONObject>? {
    var json: String? = null
    try {
        val inputStream = context.assets.open("dataOffers.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        json = String(buffer, charset("UTF-8"))
    } catch (ex: Exception) {
        Log.i("TAG", ex.message.toString())
        return null
    }

    val jsonArray = JSONObject(json).getJSONArray("offers")
    val indexes = mutableListOf<Int>()
    val selectedOffers = arrayListOf<JSONObject>()
    repeat(4) {
        indexes.add(Random.nextInt(jsonArray.length()))
    }
    for (index in indexes) {
        selectedOffers.add(jsonArray.getJSONObject(index))
    }
    return selectedOffers
}

fun readDataFromJsonFileAccidents(context: Context): JSONObject? {
    var json: String? = null
    try {
        val inputStream = context.assets.open("dataAccidents.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        json = String(buffer, charset("UTF-8"))
    } catch (ex: Exception) {
        Log.i("TAG", ex.message.toString())
        return null
    }

    val jsonArray = JSONObject(json).getJSONArray("accidents")
    val index = Random.nextInt(jsonArray.length())
    return jsonArray.getJSONObject(index)
}

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Preview(showBackground = true)
@Composable
fun GamePreview() {
    GameContent(
        {}
    )
}