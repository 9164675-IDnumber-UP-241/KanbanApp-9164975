package ua.opu.kanbanapp.ui.screen.components

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.opu.kanbanapp.data.model.TaskCard
import ua.opu.kanbanapp.data.model.TaskStatus


// ===== ГОЛОВНА ФУНКЦІЯ: Карточка на дошці =====
@Composable
fun TaskCardView(
    card: TaskCard,
    onMoveForward: (String) -> Unit,
    onDelete: (String) -> Unit,
) {
    // ===== КРОК 1: Стан для діалогу видалення =====
    var showDeleteConfirm by remember { mutableStateOf(false) }

    // ===== КРОК 2: Card компонент (контейнер карточки) =====
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()

            .combinedClickable(
                onClick = { /* Нічого */ },
                onLongClick = {
                    // ===== Довгий клік тільки для DONE карточок =====
                    if (card.status == TaskStatus.DONE) {
                        showDeleteConfirm = true
                    }
                }
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        // ===== КРОК 3: Внутрішній вміст карточки =====
        TaskCardContent(
            card = card,
            onMoveForward = onMoveForward,
            onDelete = onDelete
        )
    }

    // ===== КРОК 4: Dialog для видалення =====
    if (showDeleteConfirm) {
        DeleteConfirmationDialog(
            cardTitle = card.title,
            onConfirmDelete = {
                onDelete(card.id)
                showDeleteConfirm = false
            },
            onDismiss = {
                showDeleteConfirm = false
            }
        )
    }
}

// ===== ПІДФУНКЦІЯ 1: Вміст карточки (Title + Button/Text) =====
@Composable
private fun TaskCardContent(
    card: TaskCard,
    onMoveForward: (String) -> Unit,
    onDelete: (String) -> Unit,
) {

    // ===== Кнопки: вирівняні вправо в горизонтальному ряду =====
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {

        CardTitle(
            card = card,
            Modifier
                .weight(1f)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.width(8.dp))

        if (card.status != TaskStatus.DONE) {
            // ===== Для НЕ-DONE карточок: показуємо обидві кнопки =====
            MoveButton(
                cardId = card.id,
                onMoveForward = onMoveForward
            )

            Spacer(modifier = Modifier.width(8.dp))

            DeleteButton(
                cardId = card.id,
                onDelete = onDelete
            )
        } else {
            // ===== Для DONE карточок: показуємо тільки кнопку видалення =====
            DeleteButton(
                cardId = card.id,
                onDelete = onDelete
            )
        }
    }

}

// ===== ПІДФУНКЦІЯ 2: Заголовок карточки (з умовним перекресленням) =====
@Composable
private fun CardTitle(card: TaskCard, modifier: Modifier = Modifier) {
    // ===== Прапор: чи карточка завершена =====
    val isCompleted = card.status == TaskStatus.DONE

    Text(
        text = card.title,
        fontSize = 16.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontWeight = FontWeight.Medium,
        // ===== УМОВНА ЛОГІКА: Стиль залежно від статусу =====
        textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None,
        color = if (isCompleted) Color.LightGray else Color.Black,
        modifier = modifier
    )

}

// ===== ПІДФУНКЦІЯ 3: Кнопка переміщення =====
@Composable
private fun MoveButton(
    cardId: String,
    onMoveForward: (String) -> Unit,
) {
    // ===== Компактна кнопка зі стрілкою для переміщення карти =====
    Button(
        onClick = { onMoveForward(cardId) },
        modifier = Modifier
            .height(28.dp)
            .width(36.dp),  // Квадратна форма
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF6200EA)  // Purple
        ),
        contentPadding = PaddingValues(0.dp),  // Без padding
        shape = RoundedCornerShape(4.dp)       // Менш скруглена
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ===== Стрілка для переміщення =====
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Next",
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

// ===== ПІДФУНКЦІЯ 4: Кнопка видалення =====
@Composable
private fun DeleteButton(
    cardId: String,
    onDelete: (String) -> Unit,
) {
    // ===== Компактна кнопка з "×" для видалення карти =====
    Button(
        onClick = { onDelete(cardId) },
        modifier = Modifier
            .height(28.dp)
            .width(36.dp),  // Квадратна форма
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE0E0E0)  // Light gray
        ),
        contentPadding = PaddingValues(0.dp),  // Без паддингу
        shape = RoundedCornerShape(4.dp)       // Менш скруглена
    ) {
        // ===== Хрестик для видалення =====
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Next",
            modifier = Modifier.size(14.dp)
        )
    }
}

// ===== ПІДФУНКЦІЯ 5: Dialog для видалення =====
@Composable
private fun DeleteConfirmationDialog(
    cardTitle: String,
    onConfirmDelete: () -> Unit,
    onDismiss: () -> Unit,
) {
    // ===== Dialog з підтвердженням видалення карточки =====
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Видалити завдання?") },
        text = { Text("\"$cardTitle\" буде видалено") },
        confirmButton = {
            // ===== Кнопка підтвердження (червона) =====
            Button(
                onClick = onConfirmDelete,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                )
            ) {
                Text("Видалити", color = Color.White)
            }
        },
        dismissButton = {
            // ===== Кнопка скасування (сіра) =====
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray
                )
            ) {
                Text("Скасувати", color = Color.White)
            }
        }
    )
}

/*
СТРУКТУРА КОМПОНЕНТА:
──────────────────────

TaskCardView() — ГОЛОВНА
    │
    ├─ Card(
    │   ├─ alpha = getCardAlpha()
    │   ├─ combinedClickable()
    │   └─ TaskCardContent()
    │       ├─ CardTitle()
    │       └─ if (DONE) nothing else MoveButton()
    │
    └─ if (showDeleteConfirm) DeleteConfirmationDialog()

ПЕРЕВАГИ РОЗБИТТЯ:
──────────────────
✅ Кожна функція має одну відповідальність
✅ Легко знайти та змінити конкретну частину
✅ Перевикористання (DoneCardHint можна викликати з інших місць)
✅ Тестування (легше тестувати окремі Composable'и)
✅ Читаність (код більш структурований та зрозумілий)

ЛОГІКА ПОТОКУ:
──────────────
1. TaskCardView() — управління станом (showDeleteConfirm)
2. TaskCardContent() — рендерить вміст (Title + Button/Hint)
3. CardTitle() — заголовок з умовним стилем
4. MoveButton()/DoneCardHint() — залежно від статусу
5. DeleteConfirmationDialog() — окремий dialog

CALLBACKS:
──────────
onMoveForward(cardId) — батько обробляє натиск "→"
onDelete(cardId) — батько обробляє видалення

БАТЬКОВІ ПОТРІБНО:
──────────────────
<TaskCardView
    card = card,
    onMoveForward = { viewModel.moveCardForward(it) },
    onDelete = { viewModel.deleteCard(it) }
/>
*/

// ===== PREVIEW: Карточка в статусі TODO (з кнопкою) =====
@Preview(showBackground = true)
@Composable
private fun TaskCardViewTodoPreview() {
    TaskCardView(
        card = TaskCard(
            id = "1",
            title = "СніданокСніданокСніданокСніданокСніданокСніданок",
            status = TaskStatus.TODO
        ),
        onMoveForward = { /* Preview */ },
        onDelete = { /* Preview */ }
    )
}

// ===== PREVIEW: Карточка в статусі DONE (без кнопки, перекреслена) =====
@Preview(showBackground = true)
@Composable
private fun TaskCardViewDonePreview() {
    TaskCardView(
        card = TaskCard(
            id = "4",
            title = "СніданокСніданокСніданокСніданокСніданокСніданок",
            status = TaskStatus.DONE
        ),
        onMoveForward = { /* Preview */ },
        onDelete = { /* Preview */ }
    )
}

/*
ПОЯСНЕННЯ:
──────────

@Preview — декоратор для показу компонента в Android Studio
showBackground = true — додає білий фон (легше бачити)

Preview 1 (TODO):
- Показує карточку з кнопкою "→"
- Текст нормальний (чорний, не перекреслений)
- Alpha = 1.0 (повна прозорість)

Preview 2 (DONE):
- Показує карточку без кнопки
- Текст перекреслений
- Текст сірий
- Alpha = 0.6 (напівпрозорість)
- Показується підказка "Довгий клік для видалення"

ЯК КОРИСТУВАТИСЯ:
──────────────────
1. Відкрити файл TaskCardView.kt в Android Studio
2. Праворуч буде виведено 2 Preview
3. Натиснути на Preview, щоб побачити результат
4. Натиснути "Build & Refresh" при змінах
*/