package ua.opu.kanbanapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.opu.kanbanapp.ui.screen.components.TaskCardView
import ua.opu.kanbanapp.ui.viewmodel.KanbanViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // ===== КРОК 1: Створюємо ViewModel (viewModels delegation) =====
            // viewModels() автоматично створює ViewModel і управляє її життєвим циклом
            val viewModel: KanbanViewModel by viewModels()

            // ===== КРОК 2: Читаємо стан з ViewModel через StateFlow =====
            // collectAsState() підписує UI на зміни StateFlow
            // При змінах — автоматичний перерендер
            val uiState by viewModel.uiState.collectAsState()

            // ===== КРОК 3: Scaffold — основна структура з EdgeToEdge =====
            Scaffold(
                modifier = Modifier.fillMaxSize()
            ) { innerPadding ->
                // ===== КРОК 4: Головна колонка зі всім контентом =====
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)  // ← Відступи від status/nav bar
                        .padding(16.dp)         // ← Додатковий padding для вмісту
                ) {
                    // ===== КРОК 5: Заголовок екрана =====
                    Text(
                        text = "📋 Дошка задач",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // ===== КРОК 6: LazyColumn для виявлення карточок =====
                    // LazyColumn — ефективна вертикальна прокрутка (як RecyclerView)
                    // Рендеримо тільки видимі елементи (економія пам'яті)
                    LazyColumn {
                        // ===== Ітеруємо по всіх картах зі стану =====
                        items(
                            items = uiState.cards,
                            key = { it.id }  // ← Унікальний ключ для кожної карточки
                        ) { card ->
                            // ===== Рендеримо карточку =====
                            TaskCardView(
                                card = card,
                                onMoveForward = { cardId ->
                                    println("TODO: Переміщуємо карточку: $cardId")
                                    // Потім тут буде: viewModel.moveCardForward(cardId)
                                },
                                onDelete = { cardId ->
                                    println("TODO: Видаляємо карточку: $cardId")
                                    // Потім тут буде: viewModel.deleteCard(cardId)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
