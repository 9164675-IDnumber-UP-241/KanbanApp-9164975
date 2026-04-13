package ua.opu.kanbanapp.ui.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.opu.kanbanapp.data.model.TaskCard
import ua.opu.kanbanapp.data.model.TaskStatus

// ===== ГОЛОВНА ФУНКЦІЯ: Одна колонка на дошці =====
@Composable
fun TaskColumnView(
    status: TaskStatus,                    // Статус колонки (TODO, IN_PROGRESS, REVIEW, DONE)
    cards: List<TaskCard>,                 // Всі карти на дошці
    onMoveForward: (String) -> Unit,       // Callback для переміщення
    onDelete: (String) -> Unit,            // Callback для видалення
) {
    // ===== КРОК 1: Визначаємо назву та іконку колонки залежно від статусу =====
    val columnTitle = when (status) {
        TaskStatus.TODO -> "📝 Невпроцесовано"
        TaskStatus.IN_PROGRESS -> "⚙️ В прогресі"
        TaskStatus.REVIEW -> "👀 На перевірці"
        TaskStatus.DONE -> "✅ Готово"
    }

    // ===== КРОК 2: Фільтруємо карти — залишаємо тільки цього статусу =====
    // Наприклад, для TODO колонки покажемо тільки карти де status == TODO
    val filteredCards = cards.filter { it.status == status }

    // ===== КРОК 3: Основна колонка (контейнер) =====
    Column(
        modifier = Modifier
            .fillMaxHeight() // Займає всю висоту Row
            .background(Color.White)  // Світло-сірий фон
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        // ===== КРОК 4: Заголовок колонки з рахунком карт =====
        Text(
            text = "$columnTitle (${filteredCards.size})",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        // ===== КРОК 5: Лінія-розділювач =====
        Divider(color = Color.Gray, thickness = 1.dp)

        // ===== КРОК 6: LazyColumn для вивлення карточок колонки =====
        // Рендеримо тільки карти цього статусу
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .weight(1f)         // Займає залишкове місце
        ) {
            // ===== Ітеруємо по картах цього статусу =====
            items(
                items = filteredCards,
                key = { it.id }     // Унікальний ключ для кожної карти
            ) { card ->
                // ===== Рендеримо карточку =====
                TaskCardView(
                    card = card,
                    onMoveForward = onMoveForward,
                    onDelete = onDelete
                )
            }
        }
    }
}

/*
ПОЯСНЕННЯ:
──────────

1. when(status):
   ✅ Визначаємо назву та іконку для кожної колонки
   ✅ DONE показує інший заголовок для наочності

2. filter { it.status == status }:
   ✅ Фільтруємо карти — тільки цього статусу
   ✅ Приклад: для TODO показуємо тільки карти де status == TODO

3. weight(1f):
   ✅ Кожна колонка займає 1 частину з 4
   ✅ Як <td width="25%"> в HTML таблиці

4. fillMaxHeight():
   ✅ Колонка займає всю висоту Row
   ✅ Якщо в одній колонці 10 карт, інші також розтягуються

5. LazyColumn:
   ✅ Ефективна вертикальна прокрутка (як RecyclerView)
   ✅ Рендеримо тільки видимі карти

6. key = { it.id }:
   ✅ Compose розуміє, які карти змінилися
   ✅ Уникаємо помилок при переміщенні/видаленні

РЕЗУЛЬТАТ:
──────────
┌──────────┐┌──────────┐┌──────────┐┌──────────┐
│   TODO   ││IN_PROGRESS││ REVIEW   ││  DONE    │
│   (4)    ││    (2)   ││  (1)     ││  (1)     │
├──────────┤├──────────┤├──────────┤├──────────┤
│ Card 1   ││ Card 2   ││ Card 3   ││ Card 4   │
│ Card 5   ││ Card 6   ││          ││          │
│ Card 7   ││          ││          ││          │
│ Card 8   ││          ││          ││          │
└──────────┘└──────────┘└──────────┘└──────────┘
*/