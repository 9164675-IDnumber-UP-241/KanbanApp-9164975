package ua.opu.kanbanapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ua.opu.kanbanapp.data.model.TaskCard
import ua.opu.kanbanapp.data.model.TaskStatus

// ===== КРОК 1: ViewModel для управління Kanban дошкою =====
// Дотримується паттерну MVI: Model-View-Intent
// ViewModel: не втрачає дані при rotation, відповідає за логіку
class KanbanViewModel : ViewModel() {

    // ===== КРОК 2: StateFlow — реактивний контейнер для стану =====

    // private _uiState — ВНУТРІШНЯ (приватна)
    //   ├─ MutableStateFlow — можемо писати нові значення
    //   ├─ Тільки ViewModel змінює цей об'єкт
    //   └─ UI не має доступу (захист від випадкових змін)
    private val _uiState = MutableStateFlow(KanbanState())

    // public uiState — ЗОВНІШНЯ (публічна)
    //   ├─ StateFlow — читаємо тільки (immutable для UI)
    //   ├─ asStateFlow() — перетворює MutableStateFlow в безпечний StateFlow
    //   └─ UI слухає зміни через collectAsState()
    val uiState: StateFlow<KanbanState> = _uiState.asStateFlow()

    /*
    Аналогія з Java/Kotlin:
    ──────────────────────
    private List<Card> _cards = new ArrayList<>();  // MutableStateFlow
    public List<Card> getCards() { return _cards; }  // StateFlow через getter

    Але StateFlow краще, бо:
    ✅ Автоматично повідомляє UI про зміни
    ✅ UI не може змінювати дані напряму
    ✅ One-way data flow (only ViewModel can change)
    */

    // ===== КРОК 3: На старті завантажуємо тестові дані =====
    init {
        loadInitialCards()
    }

    // ===== КРОК 4: Приватний метод для завантаження даних =====
    private fun loadInitialCards() {
        // ===== Створюємо 4 тестові карточки (по одній в кожній колонці) =====
        val testCards = listOf(
            TaskCard("1", "Написати звіт", TaskStatus.TODO),              // Колонка 1
            TaskCard("2", "Зателефонувати клієнту", TaskStatus.IN_PROGRESS),  // Колонка 2
            TaskCard("3", "Оновити резюме", TaskStatus.REVIEW),           // Колонка 3
            TaskCard("4", "Здати лабораторну", TaskStatus.DONE),          // Колонка 4
        )

        // ===== Оновлюємо StateFlow новим станом =====
        // copy(cards = testCards) — створюємо новий KanbanState з картами
        _uiState.value = _uiState.value.copy(cards = testCards)

        /*
        Процес:
        ───────
        1. Створюється KanbanViewModel()
        2. Виклик init {} блоку
        3. loadInitialCards() додає 4 тестові карточки
        4. _uiState.value = новий KanbanState з картами
        5. StateFlow сповіщує UI: "дані змінилися!"
        6. collectAsState() в Composable перехопить сигнал
        7. Composable перерендериться з новими даними

        Результат:
        ──────────
        UI автоматично показує 4 карточки при старті! ✅
        */
    }
}

/*
ЗАГАЛЬНИЙ ПОТІК ДАНИХ:
──────────────────────

1. Користувач запускає додаток
   ↓
2. MainActivity створює KanbanViewModel()
   ↓
3. init {} блок KanbanViewModel запускає loadInitialCards()
   ↓
4. loadInitialCards() оновлює _uiState новими картами
   ↓
5. StateFlow автоматично випускає новий стан
   ↓
6. UI (KanbanScreen) слухає через collectAsState()
   ↓
7. Composable перерендериться з картами
   ↓
8. Користувач бачить 4 карточки на дошці! 🎉

АРХІТЕКТУРА:
────────────

┌─────────────────────┐
│ MainActivity        │
│ (Entry Point)       │
└──────────┬──────────┘
           │
           ├─ setContent {
           │
           └─ KanbanViewModel()
              (управління даними)
              │
              ├─ _uiState (приватна)
              │  └─ MutableStateFlow
              │
              ├─ uiState (публічна)
              │  └─ StateFlow
              │
              └─ init { loadInitialCards() }
                 (ініціалізація на старті)
*/