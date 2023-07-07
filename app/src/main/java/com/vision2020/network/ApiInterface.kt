package com.vision2020.network

import BaseModel
import com.google.gson.JsonObject
import com.vision2020.data.request.LoginReq
import com.vision2020.data.response.*
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {
    @POST("/api/ljAppLogin")
    fun userLogin(@Body userModel: LoginReq): Call<User>

    @FormUrlEncoded
    @POST("/api/ljAppCalLogin")
    fun userDistressLogin(@Field("tokenId") email: String, @Field("password") password: String): Call<CalLoginDistressResponse>

    @POST("/api/ljAppSignUp")
    fun userSignUp(@Body file: RequestBody): Call<User>

    @FormUrlEncoded
    @POST("/api/ljAppForgotPwd")
    fun userForgot(@Field("email") email: String): Call<User>

    @FormUrlEncoded
    @POST("/api/ljAppCreateGroup")
    fun createGroup(@Field("authToken") token: String, @Field("name") name: String): Call<BaseModel>

    @FormUrlEncoded
    @POST("/api/ljAppGroupListing")
    fun getListOfGroup(
        @Field("authToken") token: String,
        @Field("pageNo") page: String
    ): Call<GroupListing>

    @FormUrlEncoded
    @POST("/api/ljAppStudentListing")
    fun getStudents(
        @Field("authToken") token: String,
        @Field("pageNo") page: String
    ): Call<StudentListing>

    @FormUrlEncoded
    @POST("/api/ljAppGroupMemberListing")
    fun getGroupMembers(
        @Field("authToken") token: String,
        @Field("group_id") groupId: String
    ): Call<GroupMembers>

    @FormUrlEncoded
    @POST("/api/ljAppAddtoGroup")
    fun addLeaderToGroup(
        @Field("authToken") token: String,
        @Field("group_id") groupId: String,
        @Field("user_ids") userId: String,
        @Field("leader_id") id: String
    ): Call<BaseModel>

    @FormUrlEncoded
    @POST("/api/ljAppGroupDelete")
    fun deleteGroup(
        @Field("authToken") token: String,
        @Field("del_ids") id: String
    ): Call<BaseModel>

    @FormUrlEncoded
    @POST("/api/ljAppGroupMemberDelete")
    fun deleteMembers(
        @Field("authToken") token: String, @Field("group_id") id: String,
        @Field("student_ids") stuId: String
    ): Call<BaseModel>

    @GET("/api/ljAppDrugList")
    fun getDrugList():Call<DrugList>
    @GET("/api/ljAppDrugDossesList/{path}")
    fun getDosagesList(@Path("path", encoded = true)id:String)

    @GET("/api/ljAppViewProfile/{path}")
    fun getUserData(@Path("path", encoded = true)token: String): Call<ProfileResponse>

    @GET("/api/ljAppdistressGroupList/{path}")
    fun getdistressGroupList(@Path("path", encoded = true)token: String): Call<DistressGroupDataListing>

    @FormUrlEncoded
    @POST("/api/ljAppdistressGroupList")
    fun getdistressGroupList(@Field("authToken") authToken:String,@Field("expId") calDate:String): Call<DistressGroupDataListing>

    @POST("/api/ljAppUpdateProfile")
    fun userEditProfile(@Body file: RequestBody): Call<UpdateProfileResponse>

  //  @POST("/api/ljAppdistressExperientSetup")
    //fun postRawJSON(@QueryMap params: Map<String, String>): Call<JsonArray>

    @Headers("Content-Type: application/json")
    @POST("/api/ljAppdistressExperientSetup")
    fun postRawJSON(@Body jsonArray: JsonObject): Call<DistressExpResponse>


    @FormUrlEncoded
    @POST("/api/ljAppfetchexperientResult")
    fun fetchExpResult(@Field("authToken") authToken:String,@Field("calDate") calDate:String): Call<ExpResultResponse>


    @FormUrlEncoded
    @POST("/api/ljAppcalculateExpResultByDate")
    fun fetchExpResultbyDate(@Field("authToken") authToken:String,@Field("selectedDate") calDate:String,@Field("expId") expId:String,@Field("selectedSem") selectedSem:String): Call<ExpCalcResponse>


    @FormUrlEncoded
    @POST("/api/ljAppcalculateExpResultByWeek")
    fun fetchExpResultbyWeek(@Field("authToken") authToken:String,@Field("selectedDate", encoded = false) calDate:String ,@Field("expId") expId:String,@Field("selectedSem") selectedSem:String): Call<ExpCalWeekResponse>


    @Headers("Content-Type: application/json")
    @POST("/api/ljAppexperientResult")
    fun postExpRawJSON(@Body jsonArray: JsonObject): Call<ExpResultEditResponse>

    @Headers("Content-Type: application/json")
    @POST("/api/ljAppexperimentResultDayWise")
    fun postExpResultJSON(@Body jsonArray: JsonObject): Call<ExpResultEditResponse>

    @FormUrlEncoded
    @POST("/api/ljAppCheckDataExpDistress")
    fun verifyDSDataList(
        @Field("authToken") token: String,
        @Field("startDate") startDate: String,
        @Field("expDate") expDate: String): Call<FetchByDateListing>

    @FormUrlEncoded
    @POST("/api/ljAppdistressStudentGroupList")
    fun getGroupList(@Field("authToken") authToken:String,@Field("expDate") date:String):Call<GroupList>


    @FormUrlEncoded
    @POST("/api/ljAppdisAllStudentGroupList")
    fun getAllGroupList(@Field("authToken") authToken:String):Call<GroupList>


    @POST("/api/ljAppaddSemester")
    fun postSemSON(@Body jsonArray: JsonObject): Call<BaseModel>

    @POST("/api/ljAppaddTeacherSimulation")
    fun postSimulationSON(@Body jsonArray: JsonObject): Call<BaseModel>

    @POST("/api/ljAppgetSemester")
    fun getSemSON(@Body jsonArray: JsonObject): Call<SemList>


    @POST("/api/ljAppgetTeacherSimulation")
    fun getSimulationSON(@Body jsonArray: JsonObject): Call<SimulationList>
}