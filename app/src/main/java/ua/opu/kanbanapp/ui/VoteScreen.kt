package ua.opu.kanbanapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ua.opu.kanbanapp.viewmodel.VoteViewModel

@Composable
fun VoteScreen(
    viewModel: VoteViewModel = viewModel()
) {
    val options by viewModel.options.collectAsState()
    val leader by viewModel.leader.collectAsState()

    var newOption by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = if (leader == null) {
                "Голосування ще не розпочато"
            } else {
                "Лідер: ${leader!!.title} (${leader!!.votes} голосів)"
            },
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = newOption,
            onValueChange = { newOption = it },
            label = { Text("Новий варіант") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.addOption(newOption)
                newOption = ""
            },
            enabled = newOption.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Додати варіант")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = options,
                key = { option -> option.id }
            ) { option ->
                VoteCard(
                    option = option,
                    onVote = { viewModel.vote(option.id) }
                )
            }
        }
    }
}