package ua.opu.kanbanapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.opu.kanbanapp.data.model.TaskCard
import ua.opu.kanbanapp.data.model.TaskStatus
import ua.opu.kanbanapp.ui.screen.components.TaskColumnView

// ===== ГОЛОВНА ФУНКЦІЯ: Весь екран Kanban дошки =====
@Composable
fun KanbanScreen(
    cards: List<TaskCard>,
    onMoveForward: (String) -> Unit,
    onDelete: (String) -> Unit,
    onAddCard: (String) -> Unit,
) {
    // ===== КРОК 1: Локальний стан для input field =====
    var newCardTitle by remember { mutableStateOf("") }

    // ===== КРОК 2: Основна колонка екрану =====
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
    ) {
        // ===== КРОК 3: Заголовок екрану =====
        Text(
            text = "📋 Дошка задач",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // ===== КРОК 4: Рядок для введення нової карти =====
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ===== Input field для введення назви карти =====
            TextField(
                value = newCardTitle,
                onValueChange = { newCardTitle = it },
                placeholder = { Text("Введи назву завдання...") },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedIndicatorColor = Color(0xFF6200EA),
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            // ===== Кнопка додавання нової карти =====

            Button(
                onClick = {
                    onAddCard(newCardTitle)
                    newCardTitle = ""
                },
                modifier = Modifier.height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EA)
                ),
                contentPadding = PaddingValues(0.dp),  // Без padding
                shape = RoundedCornerShape(4.dp),       // Менш скруглена
                enabled = newCardTitle.isNotBlank()
            ) {
                Text("Додати", color = Color.White)
            }
        }

        // ===== КРОК 5: HorizontalPager з 4 колонками (SNAP scrolling) =====
        // ===== Створюємо pager state для управління сторінками =====
        val pagerState = rememberPagerState(pageCount = { 4 })

        // ===== HorizontalPager — автоматичний snap між колонками =====
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            // ===== КРОК 6: Визначаємо статус залежно від сторінки =====
            val status = when (page) {
                0 -> TaskStatus.TODO              // Сторінка 0: Невпроцесовано
                1 -> TaskStatus.IN_PROGRESS       // Сторінка 1: В прогресі
                2 -> TaskStatus.REVIEW            // Сторінка 2: На перевірці
                else -> TaskStatus.DONE           // Сторінка 3: Готово
            }

            // ===== КРОК 7: Рендеримо колонку на всю ширину екрану =====
            TaskColumnView(
                status = status,
                cards = cards,
                onMoveForward = onMoveForward,
                onDelete = onDelete
            )
        }
    }
}

/*
ПОЯСНЕННЯ:
──────────

1. rememberPagerState(pageCount = { 4 }):
   ✅ Створює стан для 4 сторінок (колонок)
   ✅ Відслідковує поточну сторінку (currentPage)

2. HorizontalPager:
   ✅ Автоматична гортання між сторінками (snap effect)
   ✅ Подібно до Instagram Stories — плавна гортання
   ✅ Кожна колонка займає весь екран

3. fillMaxSize():
   ✅ HorizontalPager займає все залишкове місце
   ✅ Кожна колонка на весь екран

4. when(page):
   ✅ Визначаємо статус залежно від номера сторінки
   ✅ page 0 = TODO, page 1 = IN_PROGRESS, і т.д.

5. TaskColumnView:
   ✅ Рендеримо тільки одну колонку за раз
   ✅ При свайпі переходимо на наступну колонку

КОРИСТУВАЦЬКИЙ ДОСВІД:
──────────────────────
1. Користувач вводить назву в input field
2. Натискає "➕ Додати" → нова карта в TODO
3. Видит TODO колонку (сторінка 0)
4. Свайпить вправо → видит IN_PROGRESS (сторінка 1)
5. Свайпить вправо → видит REVIEW (сторінка 2)
6. Свайпить вправо → видит DONE (сторінка 3)
7. Свайпить вліво → назад на REVIEW, і т.д.

SNAP ЕФЕКТ:
───────────
✅ Автоматичне вирівнювання до краю (snap)
✅ Гладкий перехід між колонками
✅ Можна також свайпити частково — сторінка "приклеїться"
✅ Як перелистування сторінок в книзі

ПЕРЕВАГИ ПЕРЕД Row + horizontalScroll:
──────────────────────────────────────
❌ Row + horizontalScroll: можна зупинитися посередині колонки
✅ HorizontalPager: завжди повністю видна одна колонка
✅ HorizontalPager: автоматичний snap (не потрібен код)
✅ HorizontalPager: приємніший UX
*/