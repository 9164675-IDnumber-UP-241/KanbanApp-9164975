package ua.opu.kanbanapp.data.model

// ===== КРОК 2: Data class для однієї карточки на дошці =====
// Це модель даних, вона містить всю інформацію про одне завдання
data class TaskCard(
    // Унікальний ідентифікатор для識別ції карточки
    // Використовується: видалення, переміщення, ListItem key
    val id: String,

    // Назва завдання, яку бачить користувач
    // Приклад: "Сніданок", "Купити хліб", "Написати лист"
    val title: String,

    // Поточний статус завдання (яка колонка)
    // Змінюється під час роботи користувача
    val status: TaskStatus
)

/*
Чому data class?
────────────────
1. Автоматично генерує: equals(), hashCode(), toString(), copy()
2. copy() дозволяє створювати копію з змінами: card.copy(status = DONE)
3. Імутабельність (immutable) — безпечна для реактивного UI
4. val (readonly) — після створення не можна змінити поля

Приклад використання:
─────────────────────
val card1 = TaskCard("1", "Сніданок", TaskStatus.TODO)
val card2 = card1.copy(status = TaskStatus.IN_PROGRESS)  // Нова копія

// card1.status = IN_PROGRESS  ❌ ПОМИЛКА — неможливо змінити!
// card2.status = IN_PROGRESS  ✅ ПРАВИЛЬНО
*/
