package org.zywx.wbpalmstar.widgetone.uex11597450.http;

import org.zywx.wbpalmstar.widgetone.uex11597450.data.ActionData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.HeaderData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.HighScoreData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.HighScoreDetailData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ListenData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.RemarkData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.TestInfomationData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.TopicDiscussionData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.VersionInfo;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.download.DownloadData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.download.DownloadDetailData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat.NewHot;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat.PublicLessonData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.CommunityData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.KnowData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.KnowDetailInfo;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.RemarkBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ReplyData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ReportData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.SimuRecordData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserInfo;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserRecord;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat.HotResultData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat.TeacherData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.localdb.LocalQuestionData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.localdb.UpdateLocalDbData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.simulation.SimulatSimpleData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.simulation.SimulationData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.simulation.SimulationTopicData;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.CommitData;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by fire on 2017/5/3 15:18.
 */
public interface RestApi {

    /**
     * 获取电话的验证码
     */
    @FormUrlEncoded
    @POST("cn/app-api/phone-code")
    Observable<ResultBean> numGetAuthCode(@Field("phoneNum") String phone, @Field("type") String type);

    /**
     * 获取邮箱的验证码
     */
    @FormUrlEncoded
    @POST("cn/app-api/send-mail")
    Observable<ResultBean> emailGetAuthCode(@Field("email") String email, @Field("type") String type);

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("cn/app-api/register")
    Observable<ResultBean> register(@Field("type") String type, @Field("registerStr") String registerStr,
                                    @Field("pass") String pass, @Field("code") String code,
                                    @Field("userName") String userName, @Field("source") String source, @Field("belong") String belong);

    /**
     * 三方登录
     */
    @FormUrlEncoded
    @POST("index.php?web/webapi/qqLoginForAppTest")
    Observable<UserInfo> thrLogin(@Field("openid") String openId, @Field("nickname") String nickname,
                                  @Field("figureurl_qq_2") String figureurl_qq_2, @Field("source") String source);

    /**
     * 正常登录
     */
    @FormUrlEncoded
    @POST("cn/app-api/check-login")
    Observable<UserInfo> login(@Field("userName") String userName, @Field("userPass") String userPass);

    /**
     * 重置密码
     */
    @FormUrlEncoded
    @POST("cn/app-api/find-pass")
    Observable<ResultBean> retrievePwd(@Field("type") String type, @Field("registerStr") String registerStr,
                                       @Field("pass") String pass, @Field("code") String code);

    //============================================================
    /*
    * 登录后重置session
    *   "http://www.toeflonline.cn/cn/api/unify-login",
	    "http://smartapply.gmatonline.cn/cn/api/unify-login",
	    "http://www.gmatonline.cn/index.php?web/webapi/unifyLogin",
	    "http://gossip.gmatonline.cn/cn/app-api/unify-login"
    */

    //    @GET("cn/api/unify-login")
    @GET("cn/app-api/unify-login")
    Observable<Response<Void>> toefl(@QueryMap Map<String, String> info);

    //    @GET("cn/api/unify-login")
    @GET("cn/app-api/unify-login")
    Observable<Response<Void>> smartapply(@QueryMap Map<String, String> info);

    //    @FormUrlEncoded
//    @GET("index.php?web/webapi/unifyLogin")
    @GET("index.php?web/appapi/unifyLogin")
    Observable<Response<Void>> gmatl(@QueryMap Map<String, String> info);

    //    @FormUrlEncoded
    @GET("cn/app-api/unify-login")
    Observable<Response<Void>> gossip(@QueryMap Map<String, String> info);

//======================================================================================

    @POST("index.php?web/webapi/personalCenter")
    Observable<ResultBean<UserData>> getPersonalCenter();

    /**
     * 获取用户做题记录的最后一题
     */
    @POST("index.php?web/webapi/userRecord")
    Observable<ResultBean<UserRecord>> getUserRecord();

//    @POST("index.php?web/webapi/knowledgeBase")
    @POST("index.php?web/appapi/knowledgeBase")
    Observable<ResultBean<List<KnowData>>> getKnowBase();

    @FormUrlEncoded
    @POST("index.php?web/webapi/knowledgeDetails")
    Observable<ResultBean<KnowDetailInfo>> getKnowInfo(@Field("contentid") String contentid);

    @FormUrlEncoded
    @POST("cn/app-api/gossip-list")
    Observable<ResultBean<RemarkBean>> getRemarkList(@Field("page") String page, @Field("pageSize") String pageSize, @Field("belong") String belong);

    @FormUrlEncoded
    @POST("cn/app-api/gossip-details")
    Observable<RemarkData> getRemarkDetail(@Field("gossipId") String id);

    @FormUrlEncoded
    @POST("cn/app-api/add-like")
    Observable<ResultBean> praiseOrCancel(@Field("gossipId") String id, @Field("belong") String belong);

    @FormUrlEncoded
    @POST("cn/app-api/reply")
    Observable<ResultBean> reply(@Field("content") String content, @Field("type") String type, @Field("id") String id,
                                 @Field("gossipUser") String gossipUser, @Field("uName") String uName,
                                 @Field("userImage") String userImage, @Field("replyUser") String replyUser,
                                 @Field("replyUserName") String replyUserName, @Field("belong") String belong);

    @FormUrlEncoded
    @POST("cn/app-api/add-gossip")
    Observable<ResultBean> releaseStatus(@Field("title") String title, @Field("content") String content, @Field("image[]") List<String> list,
                                         @Field("video") List<String> video, @Field("audio") List<String> audio, @Field("icon") String icon,
                                         @Field("publisher") String publisher, @Field("belong") String belong);

    @FormUrlEncoded
    @POST("index.php?web/webapi/getMokaoList")
    Observable<ResultBean<List<SimulationData>>> getTestList(@Field("type") String type, @Field("typeClass") String typeClass);

    @FormUrlEncoded
    @POST("index.php?web/webapi/mkTishi")
    Observable<Object> mkTishi(@Field("id") String id);

    @FormUrlEncoded
    @POST("index.php?web/webapi/AffirmRedoModel")
    Observable<Object> affirmRedoModel(@Field("id") String id, @Field("step") String step);


    //======================================继续模考========================================
    //用来告诉服务器即将进行模考的mkid
    @FormUrlEncoded
    @POST("index.php?web/appapi/mokao")
    Observable<ResultBean> simulationTest(@Field("id") String id);

    // 用来告诉服务器即将进入模考开始的那个页面
    @POST("index.php?web/appapi/tishi")
    Observable<ResultBean> simulationHint();

    // 用来获取单个题目
    @POST
    Observable<SimulationTopicData> simulationPostSign(@Url String url);

    @FormUrlEncoded  // 用来提交答案
    @POST("index.php?web/appapi/checkmkanswer")
    Observable<ResultBean> commitSimulationAnswer(@Field("questionid") int questionId, @Field("answer") String answer, @Field("duration") int duration);

    @POST("index.php?web/appapi/mokaobreak")
    Observable<ResultBean> simulationReset();  // 用来告诉服务器即将进行休息 8 分钟

    @POST("index.php?web/appapi/levelbreak")
    Observable<ResultBean> simulationResetEnd();  // 用来告诉服务器即将结束休息, 继续做题

    @FormUrlEncoded
    @POST("index.php?web/appapi/result")
    Observable<SimulatSimpleData> simulationResult(@Field("mkid") String id, @Field("mkscoreid") String mkscoreid);   // 用来获取模考的结果

    @FormUrlEncoded
    @POST("index.php?web/appapi/mokaoredo")
    Observable<ResultBean> simulationReset(@Field("mkid") String id);// 用来重置模考进度

    //======================================继续模考========================================

    @FormUrlEncoded
    @POST("index.php?web/webapi/dryDatiExam")
    Observable<SimulationTopicData> getSimulationFirstTopic(@Field("step") String step, @Field("mkid") String id, @Field("mark") String mark, @Field("starttime") String starttime);

    @FormUrlEncoded
    @POST("index.php?web/webapi/dryDatiExam")
    Observable<SimulationTopicData> getSimulationNextTopic(@Field("next") String next, @Field("mark") String mark, @Field("allmark") String allmark, @Field("submark") String subject);

    @FormUrlEncoded
    @POST("index.php?web/webapi/ModelAnswer")
    Observable<ResultBean> commitAnswer(@Field("questionid") String questionid, @Field("useranswer") String useranswer, @Field("checke") String checke);

    @FormUrlEncoded
    @POST("index.php?web/webapi/ModelResult")
    Observable<ResultBean> getSimulationRestul(@Field("mkid") String mkId, @Field("mkscoreid") String mkscoreid);

    @FormUrlEncoded
    @POST("index.php?web/webapi/ModelResult")
    Observable<SimulatSimpleData> getSimulationRestulShow(@Field("mkid") String mkId, @Field("mkscoreid") String mkscoreid);

    @POST("index.php?web/webapi/hotClass1")
    Observable<NewHot> getHostGmat();

    @POST("index.php?web/webapi/proFormaData")
    Observable<ResultBean<ReportData>> getReportData();

    //    @POST("index.php?web/webapi/gmatLiveLesson")
    @POST("index.php?web/webapi/newLiveLesson1")
    Observable<ResultBean<List<HotResultData>>> getLiveLesson();

    //    @POST("index.php?web/webapi/gmatVideoLessons")
    @POST("index.php?web/webapi/newVideoLessons1")
    Observable<ResultBean<List<HotResultData>>> getVideoLesson();

    @POST("index.php?web/webapi/teacherDetailsList")
    Observable<ResultBean<List<TeacherData>>> getTeacherList();

    @POST("cn/api/gmat-open-class")
    Observable<ResultBean<List<PublicLessonData>>> getOpenLesson();

    @FormUrlEncoded
    @POST("cn/api/add-content")
    Observable<ResultBean> addContent(@Field("catId") int catId, @Field("name") String name, @Field("extend[]") String... extend);

    @FormUrlEncoded
    @POST("cn/api/add-content")
    Observable<ResultBean> addContentToMap(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST("index.php?web/webapi/NewModelRecord")
    Observable<ResultBean<List<SimuRecordData>>> getSimuRecord(@Field("type") String type, @Field("pageNumber") String pageNum, @Field("pageSize") String pageSize);

    @Multipart
    @POST("cn/app-api/app-image")
    Observable<ResultBean> upload(@Part MultipartBody.Part file);

    @Multipart
    @POST("index.php?web/webapi/fileRecord")
    Observable<ResultBean> fileRecord(@Part MultipartBody.Part file);

    @POST()
    Observable<List<CommitData>> download(@Url String url);

    @FormUrlEncoded
    @POST("index.php?web/webapi/Setting")
    Observable<ResultBean> modifyName(@Field("nickname") String nickname);

    @FormUrlEncoded
    @POST("cn/app-api/change-nickname")
    Observable<ResultBean> newModifyName(@Field("nickname") String nickName);

    @FormUrlEncoded
    @POST("cn/app-api/update-user")
    Observable<ResultBean> modifyPhone(@Field("uid") String uid, @Field("phone") String phone, @Field("code") String code);

    @FormUrlEncoded
    @POST("cn/app-api/update-user")
    Observable<ResultBean> modifyEmail(@Field("uid") String uid, @Field("email") String phone, @Field("code") String code);

    @FormUrlEncoded
    @POST("cn/app-api/update-user")
    Observable<ResultBean> modifyPwd(@Field("uid") String uid, @Field("oldPass") String oldPass, @Field("newPass") String newPass);

    @FormUrlEncoded
    @POST("index.php?web/webapi/messages")
    Observable<ResultBean> getMsg(@Field("pageNumber") String pageNumber, @Field("pageSize") String pageSize);

    @GET("cn/app-api/reply-list")
    Observable<List<ReplyData>> replyList(@QueryMap Map<String, String> info);


    @Multipart
    @POST("index.php?web/webapi/fileimg")
    Observable<ResultBean<HeaderData>> replaceHeader(@Part MultipartBody.Part file);

    @POST("index.php?web/webapi/getSubjectTotal")
    Observable<ResultBean> getSubjectTotal();

    @POST("index.php?web/webapi/AppAd")
    Observable<ResultBean> getAdvertising();

    @FormUrlEncoded
    @POST("index.php?web/webapi/MyCourse")
    Observable<ResultBean> getBuyCourse(@Field("catid") String catid, @Field("pageSize") String pageSize, @Field("pageNumber") String pageNumber);

    @FormUrlEncoded
//    @POST("cn/app-api/post-list")
    @POST("cn/app-api/post-list")
    Observable<ResultBean<List<DownloadData>>> getPostTestList(@Field("selectId") String selectId, @Field("page") String page, @Field("pageSize") String pageSize);

    @FormUrlEncoded
//    @POST("cn/app-api/post-list")
    @POST("cn/app-api/post-list")
    Observable<ResultBean<List<CommunityData>>> getPostList(@Field("selectId") String selectId, @Field("page") String page, @Field("pageSize") String pageSize);

    @FormUrlEncoded
    @POST("cn/app-api/post-list")
    Observable<ResultBean<List<CommunityData>>> getOldPostList(@Field("selectId") String selectId, @Field("page") String page, @Field("pageSize") String pageSize);

    @FormUrlEncoded
    @POST("index.php?web/appapi/beiKaoInformation")
    Observable<List<TestInfomationData>> getTestInfo(@Field("page") String page, @Field("pageSize") String pageSize);

    @FormUrlEncoded
    @POST("index.php?web/appapi/beiKaoDetail")
    Observable<TestInfomationData> getTestInfomationDetail(@Field("id") String id);

    @FormUrlEncoded
    @POST("cn/app-api/post-details")
    Observable<CommunityData> getPostDeail(@Field("postId") String postId);

    @FormUrlEncoded
    @POST("cn/app-api/post-details")
    Observable<DownloadDetailData> getDownloadDeail(@Field("postId") String postId);

    @FormUrlEncoded
    @POST("cn/app-api/post-reply")
    Observable<ResultBean> postReply(@Field("postId") String postId, @Field("content") String content);

    @FormUrlEncoded
    @POST("cn/app-api/add-post")
    Observable<ResultBean> addPost(@Field("catId") String catId, @Field("title") String title, @Field("content") String content);

    @FormUrlEncoded
    @POST("index.php?web/webapi/opinion")
    Observable<ResultBean> commitFeedBack(@Field("phone") String phone, @Field("opinion") String content);

    @GET("index.php?web/appapi/apptikuupdate")
    Observable<ResultBean> updateDb(@Query("lasttime") int lastTime);

    @GET
    Observable<UpdateLocalDbData> updateLocalData(@Url String url);

    @GET("index.php?web/appapi/versions")
    Observable<VersionInfo> getUpdate();

    @FormUrlEncoded
    @POST("index.php?web/appapi/comment")
    Observable<TopicDiscussionData> discussion(@Field("questionId") int questionId, @Field("page") int page);

    @FormUrlEncoded
    @POST("index.php?web/appapi/problemBug")
    Observable<ResultBean> commitTopicBug(@Field("questionid") int questionId, @Field("type") String type, @Field("describe") String describe);

    @FormUrlEncoded
    @POST("index.php?web/appapi/addComment")
    Observable<ResultBean> addComment(@Field("content") String content, @Field("questionid") int questionid, @Field("commentid") String commentid);

    @FormUrlEncoded
    @POST("index.php?web/appapi/caselist")
    Observable<HighScoreData> highScoreList(@Field("pageNumber") int page, @Field("pageSize") String pageSize);


    @FormUrlEncoded
    @POST("index.php?web/appapi/casedetails")
    Observable<HighScoreDetailData> highScoreDetail(@Field("contentid") String contentid);

    @POST("index.php?web/appapi/activity")
    Observable<ActionData> action();

    @POST("index.php?web/appapi/freeAudition")
    Observable<List<ListenData>> gmatFreeInfo();

    @POST("index.php?web/appapi/introductionAudition")
    Observable<List<ListenData>> gmatIntroInfo();

}
