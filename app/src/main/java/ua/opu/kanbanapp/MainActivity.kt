package ua.opu.kanbanapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ua.opu.kanbanapp.ui.screen.KanbanScreen
import ua.opu.kanbanapp.ui.viewmodel.KanbanViewModel
import android.graphics.Color as AndroidColor


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = AndroidColor.parseColor("#FFFFFF"),  // Фон status bar
                darkScrim = AndroidColor.parseColor("#FFFFFF")  // ← Додай цей параметр
            ),
            navigationBarStyle = SystemBarStyle.light(
                scrim = AndroidColor.parseColor("#FFFFFF"),
                darkScrim = AndroidColor.parseColor("#FFFFFF")
            )
        )
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White  // Фон всього додатку
            ) {
                // ===== КРОК 1: Створюємо ViewModel =====
                val viewModel: KanbanViewModel by viewModels()

                // ===== КРОК 2: Читаємо стан з ViewModel =====
                val uiState by viewModel.uiState.collectAsState()

                // ===== КРОК 3: Scaffold для управління system bars =====
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    // ===== КРОК 4: KanbanScreen — весь UI дошки =====
                    androidx.compose.foundation.layout.Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(Color.White)// Відступи від status/nav bar
                    ) {
                        KanbanScreen(
                            cards = uiState.cards,
                            // ===== КРОК 5: Підключення методів ViewModel =====
                            onMoveForward = { cardId ->
                                // ===== Переміщуємо карту в наступну колонку =====
                                viewModel.moveCardForward(cardId)
                            },
                            onDelete = { cardId ->
                                // ===== Видаляємо карту =====
                                viewModel.deleteCard(cardId)
                            },
                            onAddCard = { title ->
                                // ===== Додаємо нову карту в TODO =====
                                viewModel.addNewCard(title)
                            }
                        )
                    }
                }
            }
        }
    }
}
