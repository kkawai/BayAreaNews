package com.kk.android.bayareanews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kk.android.bayareanews.presentation.NewsNavHost
import com.kk.android.bayareanews.presentation.ui.RssListScreen
import com.kk.android.bayareanews.ui.theme.BayAreaNewsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BayAreaNewsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
                    Scaffold(
                        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                        /*
                        floatingActionButton = {
                            FloatingActionButton(onClick = {}) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        },
                         */

                        topBar = {
                            TopAppBar(
                                scrollBehavior = scrollBehavior,
                                title = {
                                    Text(text = stringResource(id = R.string.app_name))
                                },
                                colors = topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    ) { values ->
                        NewsNavHost(contentPadding = values)
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BayAreaNewsTheme {
        Greeting("Android")
    }
}