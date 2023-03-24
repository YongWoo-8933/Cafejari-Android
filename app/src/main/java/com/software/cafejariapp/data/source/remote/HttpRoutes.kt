package com.software.cafejariapp.data.source.remote

import com.naver.maps.geometry.LatLng

object HttpRoutes {
    const val BASE_SERVER_URL = "https://cafejari.software"
//    private const val BASE_SERVER_URL = "https://cafejaritest.shop"
    const val BASE_IMAGE_SERVER_URL = "https://cafejari.shop"

    // 웹페이지
    const val BLOG = "https://blog.naver.com/cafejari22/"
    const val INSTAGRAM = "https://www.instagram.com/cafejari_official/"
    const val PLAY_STORE = "https://play.google.com/store/apps/details?id=com.software.cafejariapp"
    const val MASTER_GUIDE = "https://blog.naver.com/cafejari22/222879424057"
    const val CROWDED_GUIDE = "https://blog.naver.com/cafejari22/222883642619"
    const val POINT_GUIDE = "https://blog.naver.com/cafejari22/222883663747"
    const val CAFE_REGISTRATION_GUIDE = "https://blog.naver.com/cafejari22/222883686897"

    // 약관, 처리방침
    const val TOS = "$BASE_SERVER_URL/information/tos/"
    const val PRIVACY_POLICY = "$BASE_SERVER_URL/information/privacy_policy/"
    const val PRIVACY_POLICY_AGREEMENT = "$BASE_SERVER_URL/information/privacy_policy_agreement/"

    // 로그인 관련
    private const val BASE_USER_URL = "$BASE_SERVER_URL/user"
    const val LOGIN = "$BASE_USER_URL/login/"
    const val GOOGLE_LOGIN = "$BASE_USER_URL/google/login/"
    const val GOOGLE_LOGIN_FINISH = "$BASE_USER_URL/google/login/finish/"
    const val KAKAO_LOGIN = "$BASE_USER_URL/kakao/login/"
    const val KAKAO_LOGIN_FINISH = "$BASE_USER_URL/kakao/login/finish/"
    const val LOGOUT = "$BASE_USER_URL/logout/"
    const val GET_USER = "$BASE_USER_URL/user/"
    const val MAKE_NEW_PROFILE = "$BASE_USER_URL/profile/"
    const val PRE_REGISTRATION = "$BASE_USER_URL/pre_registration/"
    const val REGISTRATION = "$BASE_USER_URL/registration/"
    const val SMS_SEND = "$BASE_USER_URL/naver/sms/send/"
    const val RESET_SMS_SEND = "$BASE_USER_URL/password/reset/send/"
    const val RESET_SMS_AUTH = "$BASE_USER_URL/password/reset/auth/"
    const val RESET_PASSWORD = "$BASE_USER_URL/password/reset/done/"
    const val SMS_AUTH = "$BASE_USER_URL/naver/sms/authentication/"
    const val SOCIAL_USER_CHECK = "$BASE_USER_URL/social_user_check/"
    const val PRE_RECOMMENDATION = "$BASE_USER_URL/pre_recommend/"
    const val RECOMMENDATION = "$BASE_USER_URL/recommend/"
    const val PRE_AUTHORIZATION = "$BASE_USER_URL/pre_authorization/"
    fun authorization(id: Int): String{ return "$BASE_USER_URL/profile/$id/authorization/" }
    fun updateProfile(id: Int): String { return "$BASE_USER_URL/profile/$id/" }

    // 각종 정보
    private const val BASE_INFORMATION_URL = "$BASE_SERVER_URL/information"
    const val ANDROID_VERSION = "$BASE_INFORMATION_URL/android_app_version/"
    const val EVENTS = "$BASE_INFORMATION_URL/event/"
    const val EVENT_POINT_HISTORIES = "$BASE_INFORMATION_URL/event_point_history/"
    const val FAQ = "$BASE_INFORMATION_URL/faq/"
    const val INQUIRY_ETC = "$BASE_INFORMATION_URL/inquiry_etc/"
    const val INQUIRY_CAFE = "$BASE_INFORMATION_URL/inquiry_cafe/"
    const val INQUIRY_CAFE_ADDITIONAL_INFO = "$BASE_INFORMATION_URL/inquiry_cafe_additional_info/"
    const val POP_UP_NOTIFICATION_WITH_CAFE_INFO = "$BASE_INFORMATION_URL/pop_up_notification_with_cafe_info/"
    const val ON_SALE_CAFE = "$BASE_INFORMATION_URL/on_sale_cafe/"
    const val WEEK_LEADERS = "$BASE_USER_URL/week_leader/"
    const val MONTH_LEADERS = "$BASE_USER_URL/month_leader/"
    fun deleteInquiryEtc(id: Int): String {
        return "$BASE_INFORMATION_URL/inquiry_etc/$id/" }
    fun deleteInquiryCafe(id: Int): String {
        return "$BASE_INFORMATION_URL/inquiry_cafe/$id/" }

    // 맵
    private const val BASE_CAFE_URL = "$BASE_SERVER_URL/cafe"
    const val MASTER_REGISTRATION = "$BASE_CAFE_URL/cafe_master/registration/"
    const val CROWDED_UPDATE = "$BASE_CAFE_URL/cafe_master/crowded/"
    const val MASTER_EXPIRATION = "$BASE_CAFE_URL/cafe_master/expiration/"
    const val AUTO_EXPIRED_CAFE_LOG = "$BASE_CAFE_URL/auto_expired_log/"
    const val AD_POINT = "$BASE_CAFE_URL/cafe_master/ad_point/"
    const val GET_MY_CAFE_LOGS = "$BASE_CAFE_URL/cafe_log/"
    const val THUMBS_UP = "$BASE_CAFE_URL/thumbs_up/"
    fun search(query: String): String { return "$BASE_CAFE_URL/search/?query=${query}" }
    fun nearbyCafeInfos(latLng: LatLng, zoomLevel: Int): String {
        return "$BASE_CAFE_URL/cafe_info/nearby_cafe_info/?latitude=${latLng.latitude}&longitude=${latLng.longitude}&zoom_level=${zoomLevel}" }
    fun getMyUnExpiredCafeLog(expired: Boolean): String {
        return "$BASE_CAFE_URL/cafe_log/?expired=${if(expired) "True" else "False"}" }
    fun deleteCafeDetailLog(id: Int): String {
        return "$BASE_CAFE_URL/cafe_detail_log/$id/" }
    fun deleteAutoExpiredCafeLog(id: Int): String {
        return "$AUTO_EXPIRED_CAFE_LOG$id/" }

    // 상점
    private const val BASE_SHOP_URL = "$BASE_SERVER_URL/shop"
    const val ITEMS = "$BASE_SHOP_URL/item/"
    const val PURCHASE_REQUEST = "$BASE_SHOP_URL/purchase_request/"
    fun deletePurchaseRequest(id: Int): String { return "$PURCHASE_REQUEST$id/" }

    // 토큰
    private const val BASE_TOKEN_URL = "$BASE_USER_URL/token"
    const val VERIFY = "$BASE_TOKEN_URL/verify/"
    const val REFRESH = "$BASE_TOKEN_URL/refresh/"
}