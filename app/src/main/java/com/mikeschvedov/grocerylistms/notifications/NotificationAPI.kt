package com.mikeschvedov.grocerylistms.notifications

import com.mikeschvedov.grocerylistms.utils.Constants.Companion.CONTENT_TYPE
import com.mikeschvedov.grocerylistms.utils.Constants.Companion.SERVER_KEY
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI {

    @Headers("Authorization: key=$SERVER_KEY", "Content-Type: $CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<Body>

}