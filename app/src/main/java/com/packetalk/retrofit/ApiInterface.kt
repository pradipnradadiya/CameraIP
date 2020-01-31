package com.packetalk.retrofit

import com.google.gson.JsonObject
import com.packetalk.Trailer.fragment.model.trailer.TrailerGaugeItem
import com.packetalk.Trailer.fragment.model.trailer.TrailerSubGaugeItem
import com.packetalk.chart.model.ChartItem
import com.packetalk.home.model.group_camera_model.GroupCameraItem
import com.packetalk.home.model.group_camera_model.strored_video.StoredVideoItem
import com.packetalk.map.fragment.model.MapItem
import com.packetalk.setting.model.add_camera.CamToGroupRequest
import com.packetalk.setting.model.add_camera.CameraUrlItem
import com.packetalk.setting.model.add_camera.DefaultCameraItem
import com.packetalk.setting.model.modem_setting.ModemItem
import com.packetalk.setting.model.server_setting.ServerItem
import com.packetalk.setting.model.trailer.TrailerItem
import com.packetalk.setting.request.trailor.TrailerRequest
import com.packetalk.setting.request.trailor.group.AssignUserToGroupRequest
import com.packetalk.setting.request.trailor.my_camera_setting.MyCameraSettingDeleteRequest
import com.packetalk.setting.request.trailor.my_camera_setting.MyCameraSettingPriorityRequest
import com.packetalk.user.model.CommonItem
import com.packetalk.user.model.assign_camera_user.AssignCameraItem
import com.packetalk.user.model.assign_camera_user.PostAssignCamera
import com.packetalk.user.model.log.LogItem
import com.packetalk.user.model.users.UserListItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {
    //using for login
    @Headers("Content-Type: application/json")
    @POST("Login.aspx/SendRequest")
    fun checkLogin(@Body body: Map<String, String>): Call<JsonObject>

    //using for get group list
    @Headers("Content-Type: application/json")
    @POST("api/Home/GetGroupList")
    fun getGroupList(@Body body: Map<String, String>): Call<GroupCameraItem>

    //************************************CREATE USER **************************************
    //    User List
    @GET("api/User/GetUsers")
    fun getUserList(): Call<UserListItem>

    //Getting cameras which already assigned to users
    @Headers("Content-Type: application/json")
    @POST("api/User/GetCamerasForMember")
    fun getCamerasForMember(@Body body: Map<String, String>): Call<AssignCameraItem>

    //Getting cameras which already assigned to users
    @Headers("Content-Type: application/json")
    @POST("api/User/DeleteMycameraUser")
    fun deleteAssignCamera(@Body body: Map<String, String>): Call<CommonItem>

    //    Log List
    @GET("api/User/GetUsersLog")
    fun getLogList(): Call<LogItem>

    //    Clear Log
    @GET("api/User/ClearUsersLog")
    fun clearLog(): Call<JsonObject>

    //    Update Users
//    @Headers("Content-Type: application/json")
    @GET("api/User/UpdateRelayUsers")
    fun updateUsers(): Call<CommonItem>

    //    assign camera
    @Headers("Content-Type: application/json")
    @POST("api/User/SaveNewAddedCamera")
    fun assignCameraSave(@Body data: PostAssignCamera): Call<JsonObject>

    // Add Group
    @Headers("Content-Type: application/json")
    @POST("api/SettingAdmin/AddGroup")
    fun addGroup(@Body body: Map<String, String>): Call<JsonObject>

    //Update group
    @Headers("Content-Type: application/json")
    @POST("api/SettingAdmin/UpdateGroup")
    fun updateGroup(@Body body: Map<String, String>): Call<JsonObject>

    //Delete group
    @Headers("Content-Type: application/json")
    @POST("api/SettingAdmin/RemoveGroup")
    fun deleteGroup(@Body body: Map<String, String>): Call<JsonObject>

    //Modem list
    @Headers("Content-Type: application/json")
    @GET("api/SettingAdmin/GetModems")
    fun modemList(): Call<ModemItem>

    //modem switch change action
    @Headers("Content-Type: application/json")
    @POST("api/SettingAdmin/DeactiveRouter")
    fun disableModemRouter(@Body body: Map<String, String>): Call<JsonObject>

    //Server setting list
    @Headers("Content-Type: application/json")
    @GET("api/Common/GetServerURL")
    fun getServerUrl(): Call<ServerItem>

    //server switch change action
    @Headers("Content-Type: application/json")
    @POST("api/SettingAdmin/UpdateGroup")
    fun disableServerRouter(@Body body: Map<String, String>): Call<JsonObject>

    //delete group camera
    @Headers("Content-Type: application/json")
    @POST("api/SettingAdmin/DeleteGroupCamera")
    fun deleteGroupCamera(@Body body: Map<String, String>): Call<JsonObject>

    //Save Camera Priority
    @Headers("Content-Type: application/json")
    @POST("api/SettingAdmin/SavePriority")
    fun saveCameraPriority(@Body body: MyCameraSettingPriorityRequest): Call<JsonObject>

    //**************************Trailer************************************
    //Trailer List
    @GET("api/SettingAdmin/GetTrailerList")
    fun getTrailerList(): Call<TrailerItem>

    //Save trailer
    @Headers("Content-Type: application/json")
    @POST("api/SettingAdmin/AddTrailer")
    fun addTrailer(@Body data: TrailerRequest): Call<JsonObject>

    //update trailer
    @Headers("Content-Type: application/json")
    @POST("api/SettingAdmin/UpdateTrailer")
    fun updateTrailer(@Body data: TrailerRequest): Call<JsonObject>

    //delete trailer
    @Headers("Content-Type: application/json")
    @POST("api/SettingAdmin/RemoveTraier")
    fun deleteTrailer(@Body body: Map<String, String>): Call<JsonObject>

    //*********************************My Camera Setting*******************************************
    //delete multiple camera
    @Headers("Content-Type: application/json")
    @POST("api/SettingAdmin/DeleteCameraList")
    fun deleteMultipleCamera(@Body body: MyCameraSettingDeleteRequest): Call<JsonObject>

    //********************************Add Camera**************************************************
    @Headers("Content-Type: application/json")
    @POST("api/SettingAdmin/AddCamera")
    fun addUpdateCamera(@Body body: Map<String, String>): Call<JsonObject>

    @GET("api/SettingAdmin/DefaultCamera")
    fun getDefaultGroupList(): Call<DefaultCameraItem>

    //Move Camera To default Group
    @Headers("Content-Type: application/json")
    @POST("api/SettingAdmin/DeleteMycamera")
    fun moveCameraToDefaultGroup(@Body body: Map<String, Int>): Call<JsonObject>

    //save cam
    @Headers("Content-Type: application/json")
    @POST("api/SettingAdmin/savecam")
    fun saveCam(@Body body: Map<String, Int>): Call<JsonObject>


    //Get camera url
    @GET("api/SettingAdmin/GetCamerasUrl")
    fun getCameraURL(): Call<CameraUrlItem>

    //Assign camera to group
    @Headers("Content-Type: application/json")
    @POST("api/SettingAdmin/AssignCamToGroup")
    fun assignCameraToGroup(@Body body: CamToGroupRequest): Call<JsonObject>

    //Remove Camera Server
    @Headers("Content-Type: application/json")
    @POST("api/SettingAdmin/RemoveAllServerCam")
    fun removeCameraServer(@Body body: Map<String, String>): Call<JsonObject>



    //************************************ MAP *******************************************
    @Headers("Content-Type: application/json")
    @POST("api/Common/GetMapPins")
    fun getMapData(@Body body: Map<String, String>): Call<MapItem>


    //**********Group***************
    @Headers("Content-Type: application/json")
    @POST("api/SettingAdmin/GetGroupAssignedUsers")
    fun getGroupAssignedUsers(@Body body: Map<String, String>): Call<UserListItem>


    @Headers("Content-Type: application/json")
    @POST("api/SettingAdmin/DeleteGroupCamera")
    fun deleteGroupAssignedUsers(@Body body: Map<String, String>): Call<JsonObject>

    @Headers("Content-Type: application/json")
    @POST("api/SettingAdmin/SaveAssignGroupUser")
    fun saveAssignUserToGroup(@Body body: AssignUserToGroupRequest): Call<JsonObject>

    @Headers("Content-Type: application/json")
    @POST("api/VideoDetail/StoredVideos")
    fun storedVideo(@Body body: Map<String, String>): Call<StoredVideoItem>

    /************************************Trailer****************************************/
    //Trailer List
    @GET("api/Trailers/GetTrailerList")
    fun getTrailerGaugeList(): Call<TrailerGaugeItem>


    //Hybrid Trailer List
    @Headers("Content-Type: application/json")
    @POST("api/Trailers/GetHybridVitalsList")
    fun getHybridTrailerGaugeList(@Body body: Map<String, String>): Call<TrailerGaugeItem>


    //Sub Trailer List
    @Headers("Content-Type: application/json")
    @POST("api/Trailers/GetVitalsDetails")
    fun getSubTrailerGaugeList(@Body body: Map<String, String>): Call<TrailerSubGaugeItem>

    //Hybrid trailer Trailer List
    @Headers("Content-Type: application/json")
    @POST("api/Trailers/GetVitalsDetails")
    fun getHybridVitalGaugeList(@Body body: Map<String, String>): Call<TrailerGaugeItem>

    // On/Off trailer
    @Headers("Content-Type: application/json")
    @POST("api/Trailers/ChangeEngineStatus")
    fun onOffTrailer(@Body body: Map<String, String>): Call<JsonObject>

    // Start Diesel
    @Headers("Content-Type: application/json")
    @POST("api/Trailers/StartDiesel")
    fun startDiesel(@Body body: Map<String, String>): Call<JsonObject>

    // Stop Diesel
    @Headers("Content-Type: application/json")
    @POST("api/Trailers/StopDiesel")
    fun stopDiesel(@Body body: Map<String, String>): Call<JsonObject>

    // Reset Diesel
    @Headers("Content-Type: application/json")
    @POST("api/Trailers/ResetDiesel")
    fun resetDiesel(@Body body: Map<String, String>): Call<JsonObject>


    /************************************CHART*****************************************
     *
     */

    //get chart data
    @Headers("Content-Type: application/json")
    @POST("api/Common/GetChartData")
    fun getChartData(@Body body: Map<String, String>): Call<ChartItem>

}
