package com.itsbusinessposter.idebrandvideo.api;

import androidx.lifecycle.LiveData;

import com.itsbusinessposter.idebrandvideo.items.AppInfo;
import com.itsbusinessposter.idebrandvideo.items.BusinessCategoryItem;
import com.itsbusinessposter.idebrandvideo.items.BusinessItem;
import com.itsbusinessposter.idebrandvideo.items.CategoryItem;
import com.itsbusinessposter.idebrandvideo.items.CouponItem;
import com.itsbusinessposter.idebrandvideo.items.CustomCategory;
import com.itsbusinessposter.idebrandvideo.items.CustomModel;
import com.itsbusinessposter.idebrandvideo.items.FestivalItem;
import com.itsbusinessposter.idebrandvideo.items.HomeItem;
import com.itsbusinessposter.idebrandvideo.items.ItemVcard;
import com.itsbusinessposter.idebrandvideo.items.LanguageItem;
import com.itsbusinessposter.idebrandvideo.items.MainStrModel;
import com.itsbusinessposter.idebrandvideo.items.NewsItem;
import com.itsbusinessposter.idebrandvideo.items.PostItem;
import com.itsbusinessposter.idebrandvideo.items.StickerItem;
import com.itsbusinessposter.idebrandvideo.items.StoryItem;
import com.itsbusinessposter.idebrandvideo.items.SubjectItem;
import com.itsbusinessposter.idebrandvideo.items.SubsPlanItem;
import com.itsbusinessposter.idebrandvideo.items.UploadItem;
import com.itsbusinessposter.idebrandvideo.items.UserFrame;
import com.itsbusinessposter.idebrandvideo.items.UserItem;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    //********* User Login ********
    @FormUrlEncoded
    @POST("{API_KEY}/login")
    LiveData<ApiResponse<UserItem>> postUserLogin(@Path("API_KEY") String apiKey,
                                                  @Field("email") String userEmail,
                                                  @Field("password") String userPassword);

    //********* User for Register **********
    @FormUrlEncoded
    @POST("{API_KEY}/registration")
    Call<UserItem> postUser(@Path("API_KEY") String apiKey,
                            @Field("name") String userName,
                            @Field("email") String userEmail,
                            @Field("password") String userPassword,
                            @Field("mobile_no") String userPhone);

    //******** Recent Code ********
    @FormUrlEncoded
    @POST("{API_KEY}/resend-verify-code")
    Call<ApiStatus> resentCodeAgain(
            @Path("API_KEY") String API_KEY,
            @Field("userId") String userId
    );

    //******** Verify Email ********
    @FormUrlEncoded
    @POST("{API_KEY}/verify-account")
    LiveData<ApiResponse<ApiStatus>> verifyEmail(
            @Path("API_KEY") String API_KEY,
            @Field("userId") String userId,
            @Field("code") String code);

    //******** Password Update *******
    @FormUrlEncoded
    @POST("{API_KEY}/change-password")
    LiveData<ApiResponse<ApiStatus>> postPasswordUpdate(@Path("API_KEY") String apiKey,
                                                        @Field("userId") String loginUserId,
                                                        @Field("newPassword") String password);

    //******* POST Forgot Password *******
    @FormUrlEncoded
    @POST("{API_KEY}/forgot-password")
    LiveData<ApiResponse<ApiStatus>> postForgotPassword(@Path("API_KEY") String apiKey, @Field("email") String userEmail);


    //********* Get User Data ********
    @GET("{API_KEY}/user?")
    LiveData<ApiResponse<UserItem>> getUserById(@Path("API_KEY") String apiKey, @Query("id") String user_id);

    //********* Login with google ********
    @FormUrlEncoded
    @POST("{API_KEY}/google-registration")
    Call<UserItem> postGoogleUser(
            @Path("API_KEY") String API_KEY,
            @Field("name") String userName,
            @Field("email") String userEmail,
            @Field("image") String profilePhotoUrl);

    //********* Login with Mobile ********
    @FormUrlEncoded
    @POST("{API_KEY}/phone-login")
    Call<UserItem> postMobileUser(
            @Path("API_KEY") String API_KEY,
            @Field("name") String userName,
            @Field("email") String userEmail,
            @Field("phoneNumber") String mobile);

    //********* POST Upload Image *********
    @Multipart
    @POST("{API_KEY}/profile-update")
    LiveData<ApiResponse<UserItem>> doUploadImage(@Path("API_KEY") String apiKey,
                                                  @Part("id") RequestBody userId,
                                                  @Part("image") RequestBody name,
                                                  @Part MultipartBody.Part file,
                                                  @Part("name") RequestBody userName,
                                                  @Part("email") RequestBody userEmail,
                                                  @Part("mobile_no") RequestBody phone);

    //********** Get Category ********
    @GET("{API_KEY}/category")
    LiveData<ApiResponse<List<CategoryItem>>> getCategory(@Path("API_KEY") String apiKey, @Query("page") String page);

    //********** Get News ********
    @GET("{API_KEY}/news")
    LiveData<ApiResponse<List<NewsItem>>> getNews(@Path("API_KEY") String apiKey, @Query("page") String page);

    //********** Get Story ********
    @GET("{API_KEY}/story")
    LiveData<ApiResponse<List<StoryItem>>> getStory(@Path("API_KEY") String apiKey);

    //********** Get Festival ********
    @GET("{API_KEY}/festival")
    LiveData<ApiResponse<List<FestivalItem>>> getFestival(@Path("API_KEY") String apiKey, @Query("page") String page);

    //********* Get Business ******
    @GET("{API_KEY}/business")
    LiveData<ApiResponse<List<BusinessItem>>> getBusiness(@Path("API_KEY") String apiKey, @Query("userId") String userId);

    //******** Add Business *******
    @Multipart
    @POST("{API_KEY}/add-business")
    LiveData<ApiResponse<List<BusinessItem>>> addBusiness(@Path("API_KEY") String apiKey,
                                                          @Part("userId") RequestBody userId,
                                                          @Part("bussinessImage") RequestBody logo,
                                                          @Part MultipartBody.Part file,
                                                          @Part("bussinessName") RequestBody name,
                                                          @Part("bussinessEmail") RequestBody email,
                                                          @Part("bussinessNumber") RequestBody phone,
                                                          @Part("bussinessWebsite") RequestBody website,
                                                          @Part("bussinessAddress") RequestBody address);

    //******** Update Business *******
    @Multipart
    @POST("{API_KEY}/update-business")
    LiveData<ApiResponse<List<BusinessItem>>> updateBusiness(@Path("API_KEY") String apiKey,
                                                             @Part("bussinessId") RequestBody userId,
                                                             @Part("bussinessImage") RequestBody logo,
                                                             @Part MultipartBody.Part file,
                                                             @Part("bussinessName") RequestBody name,
                                                             @Part("bussinessEmail") RequestBody email,
                                                             @Part("bussinessNumber") RequestBody phone,
                                                             @Part("bussinessWebsite") RequestBody website,
                                                             @Part("bussinessAddress") RequestBody address);

    //******* Delete Business *********
    @FormUrlEncoded
    @POST("{API_KEY}/delete-business")
    Call<ApiStatus> deleteBusiness(@Path("API_KEY") String apiKey, @Field("bussinessId") String bussinessId);

    //******* Set Default Business *********
    @FormUrlEncoded
    @POST("{API_KEY}/set-default-business")
    Call<ApiStatus> setDefault(@Path("API_KEY") String apiKey, @Field("userId") String userId, @Field("bussinessId") String bussinessId);

    //********** Get Languages ********
    @GET("{API_KEY}/language")
    LiveData<ApiResponse<List<LanguageItem>>> getLanguages(@Path("API_KEY") String apiKey);

    //********** Get Post ********
    @GET("{API_KEY}/get-post")
    LiveData<ApiResponse<List<PostItem>>> getPost(@Path("API_KEY") String apiKey,
                                                  @Query("type") String type,
                                                  @Query("id") String id);

    //********** Get Plans ********
    @GET("{API_KEY}/subscription-plan")
    LiveData<ApiResponse<List<SubsPlanItem>>> getPlanData(@Path("API_KEY") String apiKey);

    //********** Get Contact Subject ********
    @GET("{API_KEY}/contact-subject")
    LiveData<ApiResponse<List<SubjectItem>>> getSubjectItems(@Path("API_KEY") String apiKey);

    //******** Send Contact *********
    @FormUrlEncoded
    @POST("{API_KEY}/contact-massage")
    Call<ApiStatus> sendContact(@Path("API_KEY")String apiKey,
                                    @Field("name") String name,
                                    @Field("email") String email,
                                    @Field("mobileNo") String number,
                                    @Field("message") String massage,
                                    @Field("subjectId") String subjectId);

    //******** Send Payment *********
    @FormUrlEncoded
    @POST("{API_KEY}/create-payment")
    Call<ApiStatus> loadPayment(@Path("API_KEY") String apiKey,
                                @Field("userId") String userId,
                                @Field("planId") String planId,
                                @Field("paymentId") String paymentId,
                                @Field("paymentAmount") String planPrice,
                                @Field("code") String couponCode);

    //******* Get App Info *********
    @GET("{API_KEY}/app-about")
    LiveData<ApiResponse<AppInfo>> getAppInfo(@Path("API_KEY") String apiKey);

    //******* Get Custom Category *********
    @GET("{API_KEY}/custom-category")
    LiveData<ApiResponse<List<CustomCategory>>> getCustomCategory(@Path("API_KEY") String apiKey, @Query("page") String page);

    //******* Get Custom Post *********
    @GET("{API_KEY}/custom-frame")
    LiveData<ApiResponse<List<PostItem>>> getCustomPost(@Path("API_KEY") String apiKey, @Query("id") String festId);

    //******** Get Custom All Data ******
    @GET("{API_KEY}/custom-post")
    LiveData<ApiResponse<CustomModel>>getAllCustom(@Path("API_KEY") String apiKey);

    //******** Get Home Data *****
    @GET("{API_KEY}/get-home-data")
    LiveData<ApiResponse<HomeItem>> getHomeData(@Path("API_KEY") String apiKey);

    //******* Get Business Category *********
    @GET("{API_KEY}/business-category")
    LiveData<ApiResponse<List<BusinessCategoryItem>>> getBusinessCategory(@Path("API_KEY") String apiKey);

    //******* Get Business Category Post *********
    @GET("{API_KEY}/business-frame")
    LiveData<ApiResponse<List<PostItem>>> getBusinessPost(@Path("API_KEY")String apiKey, @Query("id")String festId);

    //******* Get User Frame *********
    @GET("{API_KEY}/user-custom-frame")
    LiveData<ApiResponse<List<UserFrame>>> getUserFrame(@Path("API_KEY")String apiKey, @Query("userId")String userId);

    //******* Get Video ******
    @GET("{API_KEY}/get-video")
    LiveData<ApiResponse<List<PostItem>>>getVideosById(@Path("API_KEY") String apiKey, @Query("id") String id, @Query("type") String type);

    //******** Check Coupon *********
    @FormUrlEncoded
    @POST("{API_KEY}/coupon-code-validation")
    Call<CouponItem> checkCoupon(@Path("API_KEY")String apiKey, @Field("userId")String userId, @Field("code")String couponCode);


    /** TODO: PDF */

    //*********** Upload Image *******
    @Multipart
    @POST("{API_KEY}/profile-card-image-upload")
    Call<UploadItem> upLoadImage(@Path("API_KEY") String apiKey,
                                 @Part("profile_image") RequestBody name,
                                 @Part MultipartBody.Part file);

    @GET("{API_KEY}/profile-card")
    Call<ResponseBody> getPDFData(@Path("API_KEY") String apiKey);


    //******** Create Vcard *********
    @FormUrlEncoded
    @POST("{API_KEY}/profile-card")
    Call<UploadItem> createVcard(@Path("API_KEY")String apiKey,
                                 @Field("comapany_name")String businessName,
                                 @Field("name")String yourName,
                                 @Field("designation")String designation,
                                 @Field("phone")String mobile,
                                 @Field("whatsapp")String whatsapp,
                                 @Field("email")String email,
                                 @Field("website")String website,
                                 @Field("address")String location,
                                 @Field("facebook")String facebook,
                                 @Field("instagram")String insta,
                                 @Field("youtube")String youtube,
                                 @Field("twitter")String twitter,
                                 @Field("linkedin")String linkedin,
                                 @Field("about_us")String about,
                                 @Field("image")String imageUrl,
                                 @Field("template")String tempID);

    //******** Get Vcard *********
    @GET("{API_KEY}/business-card-list")
    LiveData<ApiResponse<List<ItemVcard>>> getVCards(@Path("API_KEY")String apiKey);

    //******** Get Stickers *********
    @GET("{API_KEY}/get-sticker")
    LiveData<ApiResponse<MainStrModel>> getStickers(@Path("API_KEY")String apiKey);

    //******** Search Stickers *********
    @FormUrlEncoded
    @POST("{API_KEY}/search-sticker")
    LiveData<ApiResponse<List<StickerItem>>> getStickersByKeyword(@Path("API_KEY")String key,
                                                                  @Field("keyword")String keyword);
}
