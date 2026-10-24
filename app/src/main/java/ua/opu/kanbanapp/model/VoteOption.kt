package ua.opu.kanbanapp.model

data class VoteOption(
    val id: String,
    val title: String,
    val votes: Int = 0
)