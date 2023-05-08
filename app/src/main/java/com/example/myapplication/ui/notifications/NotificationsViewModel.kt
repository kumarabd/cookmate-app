package com.example.myapplication.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel : ViewModel() {

    private val _notificationList = MutableLiveData<List<NotificationItem>>()
    val notificationsList: LiveData<List<NotificationItem>> = _notificationList

    init {
        // Simulate loading notification list from a data source
        val notifications = listOf(
            NotificationItem("Title 1", "Content 1"),
            NotificationItem("Title 2", "Content 2"),
            NotificationItem("Title 3", "Content 3"),
            NotificationItem("Title 4", "Content 4")
        )
        _notificationList.value = notifications
    }
    fun addNotifications(notifications: List<NotificationItem>) {
        val currentList = _notificationList.value?.toMutableList() ?: mutableListOf()
        currentList.addAll(notifications)
        _notificationList.value = currentList
    }
    fun setNotifications(notifications: List<NotificationItem>) {
        _notificationList.value = notifications
    }
}