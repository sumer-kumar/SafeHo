package com.sumer.safeho.sendNotificationPack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization: key=AAAA01a6RTo:APA91bEY5YvIIGTVDEi9YKY9q_7I4_RmS8jcE1eRI7OZtxQ_ZGC6HGkn8VtcRCr1Fyy7GO8jaKL2li1ul9QmoMfwEFZTpx7EI4U7k9rn1ZoZZMU-Rz816-GzPZrafNSOP0gSe_AxVuHV"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}