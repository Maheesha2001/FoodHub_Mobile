package com.example.foodhubmobile.utils

import android.content.Context

class SessionManager(context: Context) {

    private val prefs =
        context.getSharedPreferences("foodhub_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("auth_token", token).apply()
    }

    fun getToken(): String? =
        prefs.getString("auth_token", null)

    fun saveDeliveryPersonId(id: String) {
        prefs.edit().putString("delivery_person_id", id).apply()
    }

    fun getDeliveryPersonId(): String? =
        prefs.getString("delivery_person_id", null)

    fun saveAttendanceMarked(date: String) {
        prefs.edit()
            .putBoolean("attendance_marked", true)
            .putString("attendance_date", date)
            .apply()
    }

    fun isAttendanceMarkedToday(today: String): Boolean {
        val savedDate = prefs.getString("attendance_date", null)
        return prefs.getBoolean("attendance_marked", false)
                && savedDate == today
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}

//package com.example.foodhubmobile.utils
//
//import android.content.Context
//import android.content.SharedPreferences
////
////class SessionManager(context: Context) {
////
////    private val prefs: SharedPreferences = context.getSharedPreferences("foodhub_prefs", Context.MODE_PRIVATE)
////
////    fun saveToken(token: String) {
////        prefs.edit().putString("auth_token", token).apply()
////    }
////
////    fun getToken(): String? {
////        return prefs.getString("auth_token", null)
////    }
////
////    fun clearToken() {
////        prefs.edit().remove("auth_token").apply()
////    }
////}
//class SessionManager(context: Context) {
//
//    private val prefs = context.getSharedPreferences("foodhub_prefs", Context.MODE_PRIVATE)
//
//    fun saveToken(token: String) {
//        prefs.edit().putString("auth_token", token).apply()
//    }
//
//    fun getToken(): String? = prefs.getString("auth_token", null)
//
//    fun saveAttendanceMarked(date: String) {
//        prefs.edit()
//            .putBoolean("attendance_marked", true)
//            .putString("attendance_date", date)
//            .apply()
//    }
//
//    fun isAttendanceMarkedToday(today: String): Boolean {
//        val savedDate = prefs.getString("attendance_date", null)
//        return prefs.getBoolean("attendance_marked", false) && savedDate == today
//    }
//
//    fun clearSession() {
//        prefs.edit().clear().apply()
//    }
//}
//
//fun saveAttendanceMarked() {
//    prefs.edit().putBoolean("attendance_marked", true).apply()
//}
//
//fun isAttendanceMarkedToday(): Boolean {
//    return prefs.getBoolean("attendance_marked", false)
//}
//
//fun clearAttendance() {
//    prefs.edit().remove("attendance_marked").apply()
//}
