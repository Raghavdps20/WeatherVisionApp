package com.example.weathervisionapp.ui.screen.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.example.weathervisionapp.R
import com.example.weathervisionapp.data.local.Constants.CITY_INDEX
import com.example.weathervisionapp.data.local.Constants.CITY_SCREEN
import com.example.weathervisionapp.data.local.Constants.DARK_THEME
import com.example.weathervisionapp.data.local.Constants.TEMP_UNIT
import com.example.weathervisionapp.data.viewmodel.SettingsViewModel
import com.example.weathervisionapp.ui.theme.LARGE_MARGIN
import com.example.weathervisionapp.ui.theme.MEDIUM_MARGIN
import com.example.weathervisionapp.ui.theme.SMALL_MARGIN
import com.example.weathervisionapp.ui.theme.Shapes
import com.example.weathervisionapp.ui.theme.VERY_SMALL_MARGIN
import com.example.weathervisionapp.ui.theme.*
import com.example.weathervisionapp.util.Line
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: SettingsViewModel,
    appTheme: MutableState<Boolean>
) {

    val scope = rememberCoroutineScope()
    val sheetState =
        rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    var tempUnitState by remember { mutableStateOf(2) }
    var cityIndexState by remember { mutableStateOf(-1) }


    LaunchedEffect(key1 = true) {
        viewModel.retrieveInt(TEMP_UNIT).collectLatest {
            tempUnitState = it
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.retrieveInt(CITY_INDEX).collectLatest {
            cityIndexState = it
        }
    }


    BottomSheetScaffold(
        sheetShape = Shapes.small,
        scaffoldState = scaffoldState,
        sheetContent = {
            BottomSheetContent(
                sheetState,
                viewModel,
                tempUnitState
            )
        },
        sheetBackgroundColor = MaterialTheme.colors.surface,
        sheetPeekHeight = 0.dp
    ) {

        // This is the main content of settings screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
        ) {


            Image(
                painter = painterResource(id = R.drawable.settings),
                contentDescription = "",
                modifier = Modifier
                    .width(32.dp)
                    .padding(top = 8.dp)
                    .align(
                        Alignment.CenterHorizontally
                    )
            )
            Text(
                text = stringResource(R.string.settings),
                fontSize = 25.sp,
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .align(
                        Alignment.CenterHorizontally
                    )
            )


            Image(
                painter = painterResource(id = R.drawable.temperature),
                contentDescription = "",
                modifier = Modifier.padding(32.dp)
                    .width(64.dp)
                    .align(
                        Alignment.Start
                    )
            )
            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = MEDIUM_MARGIN, end = MEDIUM_MARGIN),
                title = stringResource(R.string.temp_unit),
                selected(index = tempUnitState),
                onButtonClicked = {
                    scope.launch {
                        if (sheetState.isCollapsed) sheetState.expand()
                    }
                },
                viewModel,
                appTheme
            )

            Line(
                modifier = Modifier
                    .padding(start = LARGE_MARGIN, end = LARGE_MARGIN)
            )


            Image(
                painter = painterResource(id = R.drawable.cityscape),
                contentDescription = "",
                modifier = Modifier.padding(32.dp)
                    .width(64.dp)
                    .align(
                        Alignment.Start
                    )
            )
            CustomButton(
                modifier = Modifier
                    .padding(start = MEDIUM_MARGIN, end = MEDIUM_MARGIN)
                    .fillMaxWidth(),
                title = stringResource(R.string.city),
                viewModel.selectedCity(cityIndexState),
                onButtonClicked = {
                    navController.navigate(CITY_SCREEN)
                },
                viewModel,
                appTheme
            )

            Line(
                modifier = Modifier
                    .padding(start = LARGE_MARGIN, end = LARGE_MARGIN)
            )

            Image(
                painter = painterResource(id = R.drawable.themes),
                contentDescription = "",
                modifier = Modifier.padding(32.dp)
                    .width(64.dp)
                    .align(
                        Alignment.Start
                    )
            )
            CustomButton(
                modifier = Modifier
                    .padding(start = MEDIUM_MARGIN, end = MEDIUM_MARGIN)
                    .fillMaxWidth(),
                title = stringResource(R.string.dark_theme),
                "",
                onButtonClicked = {
                    appTheme.value = !appTheme.value
                }, viewModel,
                appTheme
            )

            Line(
                modifier = Modifier
                    .padding(start = LARGE_MARGIN, end = LARGE_MARGIN)
            )
        }
    }
}


@ExperimentalMaterialApi
@Composable
fun BottomSheetContent(
    sheetState: BottomSheetState,
    viewModel: SettingsViewModel,
    savedIndex: Int
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(290.dp)
    ) {

        val scope = rememberCoroutineScope()
        val (txtHeader, btnFooter, rgTemperature, topHandler) = createRefs()
        val units = listOf(
            stringResource(R.string.celsius),
            stringResource(R.string.fahrenheit)
        )

        Line(
            modifier = Modifier
                .constrainAs(topHandler) {
                    top.linkTo(parent.top, VERY_SMALL_MARGIN)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .width(30.dp),
            thickness = 3.dp,
            color = MaterialTheme.colors.primaryVariant.copy(alpha = 0.4f)
        )

        Text(
            modifier = Modifier
                .constrainAs(txtHeader) {
                    top.linkTo(parent.top, MEDIUM_MARGIN)
                    start.linkTo(parent.start, MEDIUM_MARGIN)
                },
            text = stringResource(R.string.temperature),
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
        )

        // We did this "IF STATEMENT CHECK" to avoid index to be invalid value at first creation
        // in composable and to display the right radio button as a saved value.
        if (savedIndex != 2) {
            // Get the saved index from data store to display it in radio button.
            val (selectedOption, onOptionSelected) = remember {
                mutableStateOf(
                    units[savedIndex]
                )
            }

            Column(modifier = Modifier
                .constrainAs(rgTemperature) {
                    top.linkTo(txtHeader.bottom, MEDIUM_MARGIN)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ) {
                units.forEachIndexed { index, text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = {
                                    onOptionSelected(text)
                                    if (text != selectedOption) {
                                        viewModel.saveInt(TEMP_UNIT, index)
                                    }
                                    scope.launch { sheetState.collapse() }
                                }
                            )
                            .padding(horizontal = MEDIUM_MARGIN)
                    ) {

                        Text(
                            text = text,
                            color = MaterialTheme.colors.primaryVariant,
                            style = MaterialTheme.typography.body1.merge(),
                            modifier = Modifier
                                .padding(
                                    start = MEDIUM_MARGIN,
                                    top = MEDIUM_MARGIN
                                )
                                .weight(8f)
                        )


                        RadioButton(
                            modifier = Modifier.weight(2f),
                            selected = (text == selectedOption),
                            onClick = {
                                onOptionSelected(text)
                                if (text != selectedOption) {
                                    viewModel.saveInt(TEMP_UNIT, index)
                                }
                                scope.launch { sheetState.collapse() }
                            },
                            colors = RadioButtonDefaults.colors(
                                unselectedColor = MaterialTheme.colors.primaryVariant.copy(
                                    alpha = 0.3f
                                )
                            )
                        )
                    }

                    // Don't draw line after last item in the list
                    if (text != units.last()) {
                        Line(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = LARGE_MARGIN,
                                    end = LARGE_MARGIN,
                                    top = SMALL_MARGIN,
                                    bottom = SMALL_MARGIN
                                )
                        )
                    }
                }
            }
            Button(
                onClick = {
                    scope.launch {
                        sheetState.collapse()
                    }
                },
                modifier = Modifier
                    .constrainAs(btnFooter) {
                        top.linkTo(rgTemperature.bottom, SMALL_MARGIN)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                elevation = null
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    color = MaterialTheme.colors.primary
                )
            }
        }
    }
}


@Composable
fun CustomButton(
    modifier: Modifier,
    title: String,
    selected: String,
    onButtonClicked: () -> Unit,
    viewModel: SettingsViewModel,
    appTheme: MutableState<Boolean>
) {
    val context = LocalContext.current

    Button(
        elevation = null,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background),
        onClick = {
            if (title != context.getString(R.string.dark_theme)) {
                onButtonClicked()
            }
        }) {

        val color1 = MaterialTheme.colors.primary
        val color2 = MaterialTheme.colors.secondary

        Text(
            buildAnnotatedString {
                withStyle(style = ParagraphStyle(lineHeight = 18.sp)) {
                    withStyle(
                        style = SpanStyle(
                            color = color1,
                            fontSize = 17.sp
                        )
                    ) {
                        append("$title\n")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = color2,
                            fontSize = 12.sp
                        )
                    ) {
                        append(selected)
                    }
                }
            }, modifier = Modifier.weight(9f)
        )

        if (title == stringResource(R.string.dark_theme)) {
            val checkedState = remember { mutableStateOf(true) }

            Switch(
                modifier = Modifier.weight(1f),
                checked = appTheme.value,
                onCheckedChange = {
                    checkedState.value = it
                    onButtonClicked()
                    viewModel.saveBoolean(DARK_THEME, appTheme.value)
                },
                colors = SwitchDefaults.colors(MaterialTheme.colors.secondary)
            )
        } else if (title == stringResource(R.string.city)) {
            Image(
                modifier = Modifier.weight(1f),
                painter = painterResource(id = R.drawable.ic_keyboard_arrow_right),
                contentDescription = ""
            )
        }
    }
}

@Composable
fun selected(index: Int): String {
    return when (index) {
        0 -> stringResource(R.string.celsius)
        1 -> stringResource(R.string.fahrenheit)
        else -> stringResource(R.string.celsius)
    }
}