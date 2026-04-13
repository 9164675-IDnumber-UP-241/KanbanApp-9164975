package ua.opu.kanbanapp.ui.viewmodel

import ua.opu.kanbanapp.data.model.TaskCard

// ===== КРОК 1: Data class для UI стану =====
// Це єдине джерело правди (single source of truth)
// Весь UI будується на основі цього стану
data class KanbanState(
    // ===== КРОК 2: Список всіх карточок на дошці =====
    // Можуть бути в будь-якому статусі (TODO, IN_PROGRESS, REVIEW, DONE)
    // Порожньо за замовчуванням (пізніше завантажиться з ViewModel)
    val cards: List<TaskCard> = emptyList(),

    // ===== КРОК 3: Флаг завантаження =====
    // true = показуємо спіннер (дані завантажуються)
    // false = показуємо карточки (дані готові)
    val isLoading: Boolean = false,

    // ===== КРОК 4: Текст помилки =====
    // null = помилки немає
    // String = показуємо помилку користувачу
    val error: String? = null
)

/*
ПОЯСНЕННЯ:
──────────

1. Data class дозволяє:
   ✅ Автоматична генерація: equals(), hashCode(), toString(), copy()
   ✅ copy() — створювати нові версії стану
      state.copy(isLoading = true)  ← нова копія зі змінами
   ✅ Іммутабельність (val) — безпечно для реактивного UI

2. Приклад використання:
   val initialState = KanbanState()  ← порожний стан
   val loadingState = initialState.copy(isLoading = true)  ← копія зі змінами
   val readyState = loadingState.copy(
       isLoading = false,
       cards = listOf(card1, card2, card3)
   )

3. Чому окремий State клас?
   ✅ Розділення обов'язків (State = дані, ViewModel = логіка)
   ✅ Легко тестувати (передаємо State в Composable)
   ✅ Легко додавати нові поля (просто добавляємо параметр)
   ✅ UI читає з одного місця (не розшукує дані по ViewModel)
*/
