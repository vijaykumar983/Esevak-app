package in.jploft.esevak.network;

import java.util.HashMap;

import in.jploft.esevak.pojo.AddToCartData;
import in.jploft.esevak.pojo.AllCategoryData;
import in.jploft.esevak.pojo.ApplyCouponData;
import in.jploft.esevak.pojo.AllCityData;
import in.jploft.esevak.pojo.DashboardData;
import in.jploft.esevak.pojo.FeedbackData;
import in.jploft.esevak.pojo.ForgotPassData;
import in.jploft.esevak.pojo.GenerateChecksumData;
import in.jploft.esevak.pojo.LoginSignupData;
import in.jploft.esevak.pojo.MyCartData;
import in.jploft.esevak.pojo.MyOrdersData;
import in.jploft.esevak.pojo.PlaceOrderData;
import in.jploft.esevak.pojo.ProductDetailData;
import in.jploft.esevak.pojo.RemoveCartData;
import in.jploft.esevak.pojo.ResendOtpData;
import in.jploft.esevak.pojo.ResetPassData;
import in.jploft.esevak.pojo.ServiceData;
import in.jploft.esevak.pojo.SubCategoryData;
import in.jploft.esevak.pojo.TimeSlotData;
import in.jploft.esevak.pojo.TransactionHistoryData;
import in.jploft.esevak.pojo.UpdateCityData;
import in.jploft.esevak.pojo.UserData;
import in.jploft.esevak.pojo.WalletData;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface RestApi {
    @FormUrlEncoded
    @POST("/esevak/Webservice/login")
    Observable<LoginSignupData> loginSignup(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/esevak/Webservice/otpverify")
    Observable<UserData> verifyOtp(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/esevak/Webservice/resendotp")
    Observable<ResendOtpData> resendOtp(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/esevak/Webservice/forgetpassword")
    Observable<ForgotPassData> forgotPass(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/esevak/Webservice/changepassword")
    Observable<ResetPassData> resetPass(@FieldMap HashMap<String, String> reqData);

    @Multipart
    @POST("/esevak/Webservice/UpdateProfile")
    Observable<UserData> updateProfile(@Part MultipartBody.Part id,
                                       @Part MultipartBody.Part name,
                                       @Part MultipartBody.Part email,
                                       @Part MultipartBody.Part phone,
                                       @Part MultipartBody.Part address,
                                       @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("/esevak/Webservice/homePage")
    Observable<DashboardData> dashboard(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/esevak/Webservice/datBYCategory")
    Observable<ServiceData> service(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/esevak/Webservice/categoryList")
    Observable<AllCategoryData> allCategory(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/esevak/Webservice/productDetails")
    Observable<ProductDetailData> productDetail(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/esevak/Webservice/addTocart")
    Observable<AddToCartData> addToCart(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/esevak/Webservice/mycart")
    Observable<MyCartData> myCart(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/esevak/Webservice/removeCart")
    Observable<RemoveCartData> removeCart(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/esevak/Webservice/applypromocode")
    Observable<ApplyCouponData> applyCoupon(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/esevak/Webservice/placeOrder")
    Observable<PlaceOrderData> placeOrder(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/esevak/Webservice/myOrders")
    Observable<MyOrdersData> myOrders(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/esevak/Webservice/mywallet")
    Observable<WalletData> wallet(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/esevak/Webservice/transactionsHistory")
    Observable<TransactionHistoryData> transactionHistory(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/esevak/Webservice/myProfile")
    Observable<UserData> myProfile(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("payment/payment_paytm/generateChecksum.php")
    Call<GenerateChecksumData> generateChecksum(@FieldMap HashMap<String, String> reqData);


    @GET
    Observable<PlaceOrderData> placeOrders(@Url String url);

    @FormUrlEncoded
    @POST("/esevak/Webservice/feedback")
    Observable<FeedbackData> feedback(@FieldMap HashMap<String, String> reqData);

    @POST("/esevak/Webservice/timeslot")
    Observable<TimeSlotData> timeSlot();

    @FormUrlEncoded
    @POST("/esevak/Webservice/subCategoryList")
    Observable<SubCategoryData> subCategory(@FieldMap HashMap<String, String> reqData);


    @POST("/esevak/Webservice/cityList")
    Observable<AllCityData> allCity();

    @FormUrlEncoded
    @POST("/esevak/Webservice/updateCity")
    Observable<UpdateCityData> updateCity(@FieldMap HashMap<String, String> reqData);
}
