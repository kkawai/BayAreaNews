package com.kk.android.bayareanews.presentation.ui.search_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kk.android.bayareanews.R
import com.kk.android.bayareanews.ui.theme.BayAreaNewsTheme

@Composable
@Preview
fun NoResultsScreenPreview() {
    val title = remember {
        mutableStateOf("my title")
    }
    BayAreaNewsTheme {
        Surface {
            Column {
                NoResultsScreen(title, {})
            }
        }
    }
}

@Composable
fun NoResultsScreen(searchTerm: MutableState<String>, onGoBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(
                id = R.string.search_no_results, searchTerm.value
            ),
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
        Button(onClick = onGoBack) {
            Text(stringResource(id = R.string.go_back))
        }
    }
}