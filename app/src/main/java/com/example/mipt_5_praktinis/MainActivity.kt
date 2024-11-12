package com.example.mipt_5_praktinis

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.mipt_5_praktinis.state.CurrencyViewModel
import com.example.mipt_5_praktinis.ui.components.CurrencyCard
import com.example.mipt_5_praktinis.ui.theme.CustomTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val currencyViewModel = CurrencyViewModel()
            CurrencyScreen(currencyViewModel, this)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyScreen(viewModel: CurrencyViewModel, context: Context) {
    val currencyList = viewModel.currencyList.collectAsState(initial = listOf())

    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()

    var open by remember { mutableStateOf(false) }
    var filter by remember { mutableStateOf("") }
    val filteredList = currencyList.value.filter {
        it.targetName.contains(filter) || it.targetCurrency.contains(filter)
    }
    CustomTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        AnimatedVisibility(!open) {
                            IconButton(
                                onClick = {
                                    open = true
                                },
                                content = { Icon(Icons.Rounded.Search, "") }
                            )
                        }
                        AnimatedVisibility(open) {
                            OutlinedTextField(
                                filter,
                                onValueChange = { filter = it },
                                textStyle = MaterialTheme.typography.bodyMedium,
                                leadingIcon = { Icon(Icons.Rounded.Search, "") },
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Search,
                                    showKeyboardOnFocus = true
                                ),
                                keyboardActions = KeyboardActions(
                                    onSearch = { focusManager.clearFocus(); open = false },
                                ),
                                modifier = Modifier
                                    .padding(8.dp)
                                    .focusRequester(focusRequester)
                                    .onPlaced { focusRequester.requestFocus() }
                            )
                        }
                    },
                    title = {},
                    actions = {
                        IconButton(
                            onClick = {
                                viewModel.refresh()
                                Toast.makeText(
                                    context,
                                    "Currencies fetching...",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            content = { Icon(Icons.Rounded.Refresh, "") }
                        )
                    }
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            focusManager.clearFocus()
                            open = false
                        }
                    )
                }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ) {
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp)
                    ) {
                        if (filteredList.isEmpty())
                            Text(
                                "Currencies (Empty)",
                                style = MaterialTheme.typography.titleLarge,
                            )
                        else
                            Text(
                                "Currencies (${filteredList.size})",
                                style = MaterialTheme.typography.titleLarge,
                            )
                        if (currencyList.value.isNotEmpty())
                            Text(
                                currencyList.value[0].pubDate,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                    }
                }
                itemsIndexed(
                    filteredList,
                    key = { _, item -> item.hashCode() }
                )
                { _, item -> CurrencyCard(item) }
            }
        }
    }
}

