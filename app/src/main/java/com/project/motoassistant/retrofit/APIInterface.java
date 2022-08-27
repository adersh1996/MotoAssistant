package com.project.motoassistant.retrofit;

import com.project.motoassistant.models.Root;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("user_login.php")
    Call<Root> LOGINROOTCALL(@Query("phone") String phone,
                             @Query("password") String password,
                             @Query("device_token") String deviceToken);

    @Multipart
    @POST("user_registration.php")
    Call<Root> REGROOTCALL(@Part("name") RequestBody name,
                           @Part("email") RequestBody email,
                           @Part("phone") RequestBody phone,
                           @Part("password") RequestBody password,
                           @Part("address") RequestBody address,
                           @Part MultipartBody.Part image);

    @GET("view_workshop_latlong.php")
    Call<Root> WORKSHOPLISTCALL(@Query("lattitude") String lat,
                                @Query("longitude") String longitude,
                                @Query("service_id") String serviceId);

    @GET("view_services.php")
    Call<Root> SERVICELISTCALL();

    @GET("order_history.php")
    Call<Root> ORDER_HISTORY(@Query("user_id") String userId);

    @GET("order_details.php")
    Call<Root> ORDER_DETAILS(@Query("user_id") String userId, @Query("request_id") String requestId);

    @GET("payment.php")
    Call<Root> PAYMENT(@Query("user_id") String userId, @Query("request_id") String requestId);

    @GET("add_rating.php")
    Call<Root> ADD_REVIEW(@Query("user_id") String userId,
                          @Query("workshop_id") String workshopId,
                          @Query("ratcount") String rateCount,
                          @Query("service_id") String serviceId,
                          @Query("request_id") String requestId,
                          @Query("review") String review);

    @GET("view_workshop.php")
    Call<Root> WORKSHOP_DETAILS(@Query("id") String workshopId);

    @GET("view_allreview.php")
    Call<Root> VIEW_ALL_REVIEW(@Query("workshop_id") String workshopId);

    @GET("request_workshop.php")
    Call<Root> REQUEST_SERVICE(@Query("workshop_id") String workshopId,
                               @Query("userid") String userId,
                               @Query("service_name") String ServiceName,
                               @Query("service_id") String ServiceId,
                               @Query("description") String description);

    @GET("view_user.php")
    Call<Root> USER_DETAILS(@Query("userid") String userId);

    @Multipart
    @POST("edit_user.php")
    Call<Root> UPDATE_USER_DETAILS(@Part("name") RequestBody name,
                                   @Part("email") RequestBody email,
                                   @Part("phone") RequestBody phone,
                                   @Part("address") RequestBody address,
                                   @Part MultipartBody.Part image,
                                   @Part("userid") RequestBody userId);
}
