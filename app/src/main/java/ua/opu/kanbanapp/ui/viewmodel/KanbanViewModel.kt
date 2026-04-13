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
    private val _uiState = MutableStateFlow(KanbanState())
    val uiState: StateFlow<KanbanState> = _uiState.asStateFlow()


    // ===== КРОК 5: Переміщення карти в наступну колонку =====
    fun moveCardForward(cardId: String) {
        // ===== Беремо поточний список карт =====
        val currentCards = _uiState.value.cards

        // ===== Трансформуємо список: змінюємо статус потрібної карти =====
        val updatedCards = currentCards.map { card ->
            if (card.id == cardId) {
                // ===== Знаходимо наступний статус =====
                val nextStatus = when (card.status) {
                    TaskStatus.TODO -> TaskStatus.IN_PROGRESS
                    TaskStatus.IN_PROGRESS -> TaskStatus.REVIEW
                    TaskStatus.REVIEW -> TaskStatus.DONE
                    TaskStatus.DONE -> card.status  // DONE залишається DONE (не рухаємо далі!)
                }
                // ===== Створюємо КОПІЮ карти з новим статусом (імутабельність) =====
                card.copy(status = nextStatus)
            } else {
                // ===== Решта карт залишаються без змін =====
                card
            }
        }

        // ===== Оновлюємо StateFlow новим списком =====
        // Всі Composable'и, що слухають uiState, перерендеруються автоматично
        _uiState.value = _uiState.value.copy(cards = updatedCards)
    }

    // ===== КРОК 6: Видалення карти =====
    fun deleteCard(cardId: String) {
        // ===== Беремо поточний список карт =====
        val currentCards = _uiState.value.cards

        // ===== Фільтруємо список: залишаємо всі карти крім видаленої =====
        val updatedCards = currentCards.filter { it.id != cardId }

        // ===== Оновлюємо StateFlow новим списком =====
        _uiState.value = _uiState.value.copy(cards = updatedCards)
    }

    // ===== КРОК 7: Додавання нової карти =====
    fun addNewCard(title: String) {
        // ===== Валідація: не додаємо порожні карти =====
        if (title.isBlank()) return

        // ===== Беремо поточний список карт =====
        val currentCards = _uiState.value.cards

        // ===== Генеруємо унікальний ID: max(існуючих) + 1 =====
        // Приклад: якщо є ID "1", "2", "4", то новий ID буде "5"
        val newId = (currentCards.maxOfOrNull { it.id.toLongOrNull() ?: 0 } ?: 0) + 1

        // ===== Створюємо нову карту (завжди стартує в TODO) =====
        val newCard = TaskCard(
            id = newId.toString(),
            title = title.trim(),  // Видаляємо пробіли на початку/кінці
            status = TaskStatus.TODO
        )

        // ===== Додаємо нову карту в список (+ оператор) =====
        val updatedCards = currentCards + newCard

        // ===== Оновлюємо StateFlow новим списком =====
        _uiState.value = _uiState.value.copy(cards = updatedCards)
    }
}

/*
РЕЗЮМЕ ОПЕРАЦІЙ:
────────────────

1. moveCardForward(cardId):
   • TODO → IN_PROGRESS → REVIEW → DONE (стоп)
   • map() для трансформації списку
   • copy(status = nextStatus) для імутабельності

2. deleteCard(cardId):
   • filter { it.id != cardId } видаляє карту
   • Решта карт залишаються без змін

3. addNewCard(title):
   • Валідація (isBlank())
   • Генерація ID (max + 1)
   • + оператор для додавання
   • Завжди стартує в TODO

ІММУТАБЕЛЬНІСТЬ:
────────────────
✅ Не змінюємо карти напряму (card.status = ...)
✅ Створюємо НОВІ версії через copy()
✅ Не змінюємо список напряму (cards.add(...))
✅ Створюємо НОВИЙ список через map/filter/+

ПОТІК ДАНИХ:
────────────
UI → callback → ViewModel.method() → StateFlow.update → UI rerender
*/