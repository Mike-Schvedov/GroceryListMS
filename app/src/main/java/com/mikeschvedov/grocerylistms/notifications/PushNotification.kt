package com.mikeschvedov.grocerylistms.notifications

data class PushNotification(
    val data: NotificationData,
    val to: String
)