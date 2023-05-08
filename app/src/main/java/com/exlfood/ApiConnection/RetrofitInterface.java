package com.exlfood.ApiConnection;

import com.exlfood.DataSets.AddressListApi;
import com.exlfood.DataSets.ApiResponseCheck;
import com.exlfood.DataSets.CheckoutCartListApi;
import com.exlfood.DataSets.ConfirmOrderApi;
import com.exlfood.DataSets.CountryCodeApi;
import com.exlfood.DataSets.CouponApplyApi;
import com.exlfood.DataSets.CouponListApi;
import com.exlfood.DataSets.CuisinesApi;
import com.exlfood.DataSets.FilterListApi;
import com.exlfood.DataSets.GPApiResponseCheck;
import com.exlfood.DataSets.GPLApiResponseCheck;
import com.exlfood.DataSets.GroceryInfoApi;
import com.exlfood.DataSets.GroceryProductApi;
import com.exlfood.DataSets.GroceryProductInfo;
import com.exlfood.DataSets.GroceryProducts;
import com.exlfood.DataSets.GroceryStoresListApi;
import com.exlfood.DataSets.GroceryStoresSearchApi;
import com.exlfood.DataSets.HomeModulesApi;
import com.exlfood.DataSets.LoginApi;
import com.exlfood.DataSets.MyOrderInfoApi;
import com.exlfood.DataSets.MyOrderListApi;
import com.exlfood.DataSets.PagesApi;
import com.exlfood.DataSets.PaymentMethodsApi;
import com.exlfood.DataSets.ProfilePictureApi;
import com.exlfood.DataSets.RegisterApi;
import com.exlfood.DataSets.SubCategoryDataSet;
import com.exlfood.DataSets.TrackOrderApi;
import com.exlfood.DataSets.VendorDataSet;
import com.exlfood.DataSets.VendorFilterApi;
import com.exlfood.DataSets.VendorListingApi;
import com.exlfood.DataSets.VendorListingApiPagination;
import com.exlfood.DataSets.VendorSearchApi;
import com.exlfood.DataSets.VendorZoneApi;
import com.exlfood.DataSets.Vendor_Info;
import com.exlfood.DataSets.CartListApi;
import com.exlfood.DataSets.ReviewListApi;
import com.exlfood.DataSets.WishListDataSet;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("api/ordering/add-cart")
    Call<String> add_to_cart(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/cart-product-count")
    Call<String> cart_product_count(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/cart-delete")
    Call<String> clear_cart(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/vendor-info")
    Call<Vendor_Info> vendorInfo(@Body RequestBody jsonObject);

    @POST("api/ordering/modules")
    Call<HomeModulesApi> homeModules(@Body RequestBody jsonObject);

    @POST("api/ordering/modules")
    Call<ApiResponseCheck> homeModulesForDeliveryAvailableCheck(@Body RequestBody jsonObject);

    @POST("api/ordering/vendor-listing")
    Call<VendorListingApi> AllRestaurantsList(@Body RequestBody jsonObject);

    @POST("api/ordering/vendor-listing")
    Call<VendorListingApiPagination> AllRestaurantsListPagination(@Body RequestBody jsonObject);

    @POST("api/ordering/cuisine-list")
    Call<CuisinesApi> CuisinesList(@Body RequestBody jsonObject);

    @POST("api/ordering/vendor-search")
    Call<VendorSearchApi> searchRestaurantsList(@Body RequestBody jsonObject);

    @POST("api/ordering/country-list")
    Call<CountryCodeApi> countryCodeList(@Body RequestBody jsonObject);

    @POST("api/ordering/login")
    Call<LoginApi> loginApi(@Body RequestBody jsonObject);

    @POST("api/ordering/profile-info")
    Call<LoginApi> profile_info(@Header("Customer-Authorization") String authorization);

    @POST("api/ordering/check-customer")
    Call<ApiResponseCheck> checkCustomerApi(@Body RequestBody jsonObject);

    @POST("api/ordering/forget-password")
    Call<ApiResponseCheck> forgotPwdApi(@Body RequestBody jsonObject);

    @POST("api/ordering/register")
    Call<RegisterApi> registerApi(@Body RequestBody jsonObject);

    @POST("api/ordering/cart-product")
    Call<CartListApi> cartListApi(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/cart-product")
    Call<CheckoutCartListApi> checkoutCartListApi(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/option-count-change")
    Call<GPApiResponseCheck> groceryCartItemIncrementOrDecrementApi(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/option-count-change")
    Call<GPLApiResponseCheck> groceryPLCartItemIncrementOrDecrementApi(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/option-count-change")
    Call<ApiResponseCheck> cartItemIncrementOrDecrementApi(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/cart-delete")
    Call<GPApiResponseCheck> groceryCartItemDeleteApi(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/cart-delete")
    Call<GPLApiResponseCheck> groceryPLCartItemDeleteApi(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/cart-delete")
    Call<ApiResponseCheck> cartItemDeleteApi(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/coupon-list")
    Call<CouponListApi> couponListApi(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/payment-list")
    Call<PaymentMethodsApi> paymentMethodsApi(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/apply-coupon")
    Call<CouponApplyApi> applyCouponApi(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/confirm-order")
    Call<ConfirmOrderApi> confirmOrderApi(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/place-order")
    Call<ApiResponseCheck> placeOrderApi(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/address-list")
    Call<AddressListApi> addressListsApi(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/is-delivery")
    Call<ApiResponseCheck> isDeliveryApi(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/delete-address")
    Call<ApiResponseCheck> addressDeleteApi(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/add-address")
    Call<ApiResponseCheck> addAddressApi(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/edit-address")
    Call<ApiResponseCheck> editAddressApi(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/pages")
    Call<PagesApi> webViewPagesApi(/*@Header("Customer-Authorization") String authorization,*/ @Body RequestBody jsonObject);

    @POST("api/ordering/contactUs")
    Call<ApiResponseCheck> contactUsApi(@Body RequestBody jsonObject);

    @POST("api/ordering/change-password")
    Call<ApiResponseCheck> changePasswordApi(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/order-list")
    Call<MyOrderListApi> ordersListsApi(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/order-info")
    Call<MyOrderInfoApi> orderInfoApi(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/edit-profile")
    Call<ApiResponseCheck> editProfileApi(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/profile-picture")
    Call<ProfilePictureApi> changeProfilePictureApi(@Header("Customer-Authorization") String Token, @Body RequestBody jsonObject);

    @POST("api/ordering/Vendor-zone")
    Call<VendorZoneApi> vendorZoneApi(@Body RequestBody jsonObject);

    @POST("api/ordering/vendor-listing")
    Call<GroceryStoresListApi> groceryStoresList(@Body RequestBody jsonObject);

    @POST("api/ordering/vendor-info")
    Call<GroceryInfoApi> groceryInfo(@Body RequestBody jsonObject);

    @POST("api/ordering/grocery/product")
    Call<GroceryProductApi> groceryProductList(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/grocery/subcategory")
    Call<SubCategoryDataSet> subcategory(@Body RequestBody jsonObject);

    @POST("api/ordering/grocery/product")
    Call<GroceryProducts> product(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/grocery/product-info")
    Call<GroceryProductInfo> product_info(@Body RequestBody jsonObject);

    @POST("api/ordering/vendor/filter")
    Call<VendorFilterApi> vendorFiltersApi(@Body RequestBody jsonObject);

    @POST("api/ordering/guest-address")
    Call<ApiResponseCheck> guestAddEditAddressApi(@Body RequestBody jsonObject);

    @POST("api/ordering/review-list")
    Call<ReviewListApi> reviewListApi(@Body RequestBody jsonObject);

    @POST("api/ordering/add-review")
    Call<ApiResponseCheck> addReviewApi(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/grocery/grocery-search")
    Call<GroceryStoresSearchApi> grocerySearchApi(@Body RequestBody jsonObject);

    @POST("api/ordering/filter-list")
    Call<FilterListApi> filterListApi(@Body RequestBody jsonObject);

    @POST("api/ordering/track-order")
    Call<TrackOrderApi> trackOrderApi(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/wish-list")
    Call<WishListDataSet> wishlist(@Body RequestBody jsonObject);

    @POST("api/ordering/delete-account")
    Call<ApiResponseCheck> customer_account_deletion(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/order-cancel")
    Call<String> cancel_order(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);

    @POST("api/ordering/logout")
    Call<String> log_out(@Header("Customer-Authorization") String authorization, @Body RequestBody jsonObject);


}
