package com.example.carchive.entities

data class ExpandableItem(
    val buttonText: String,
    val expandableFirstText: String,
    val expandableSecondText: String,
    var isExpanded: Boolean = false
) {
    fun toggleExpanded() {
        isExpanded = !isExpanded
    }
}
