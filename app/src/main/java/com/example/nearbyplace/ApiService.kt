package com.example.nearbyplace

import com.example.nearbyplace.model.directions.Direction
import com.example.nearbyplace.model.nearby.NearByPlace
import com.example.nearbyplace.model.nearby.Photo
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NearbyApi {
    //place/nearbysearch/json?location=16.068264,108.214405&radius=10000&types=restaurant&key=AIzaSyDtxS6znDp9TzYPYdV8XwptR-ARnFHKRCs
    @GET("place/nearbysearch/json?")
    fun getDetails(
        @Query("location") loc: String?,
        @Query("radius") radius: String?,
        @Query("types") type: String,
        @Query("key") key: String?
    ): Call<NearByPlace>

//    place/photo?photoreference=CmRaAAAAd5d7SdYV_G1PWnuTWIhI4gfnvjhGoVlu229ld6Ss9_qGLouiaJpLF06ARhSrdmDjCggPwUDiYAuwe2_50eu6YABgGfrGi7-ktubccpH9gFk39F5Zma-MrLsrli_tudffEhDI5K6D8FrmRKvHkgJIaKQuGhTqOWtokckdCap8cNlLHvElsBeZlg&sensor=false&maxheight=2009&maxwidth=1340&key=AIzaSyDtxS6znDp9TzYPYdV8XwptR-ARnFHKRCs
    @GET("/place/photo?")
    fun getDetailsPhoto(
        @Query("photoreference") photoreference: String?,
        @Query("sensor") sensor: String?,
        @Query("maxheight") maxheight: Int,
        @Query("maxwidth") maxwidth: Int,
        @Query("key") key: String?
    ): Call<ResponseBody>
//directions/json?origin=36 Bạch Đằng, Thạch Thang, Q. Hải Châu, Đà Nẵng 551291, Việt Nam&destination=Số 02 Đ. 2 Tháng 9, Bình Hiên, Hải Châu, Đà Nẵng 550000, Việt Nam&waypoints=via:16.077329,108.22367|via:16.060475,108.223349&key=AIzaSyDtxS6znDp9TzYPYdV8XwptR-ARnFHKRCs
    @GET("directions/json")
    fun getDirectionWithWayPoints(
        @Query("origin") origin: String?,
        @Query("destination") destination: String?,
        @Query("waypoints") wayPoints: String?,
        @Query("key") key: String?
    ): Call<Direction>
}
