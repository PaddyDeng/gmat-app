package org.zywx.wbpalmstar.widgetone.uex11597450.http;

import org.zywx.wbpalmstar.widgetone.uex11597450.data.ActionData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.CommunityData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.HeaderData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.HighScoreData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.HighScoreDetailData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.KnowData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.KnowDetailInfo;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ListenData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.RemarkBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.RemarkData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ReplyData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ReportData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.SimuRecordData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.TestInfomationData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.TopicDiscussionData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserInfo;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserRecord;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.VersionInfo;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.download.DownloadData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.download.DownloadDetailData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat.HotResultData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat.NewHot;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat.PublicLessonData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat.TeacherData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.localdb.UpdateLocalDbData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.simulation.SimulatSimpleData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.simulation.SimulationData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.simulation.SimulationTopicData;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.CommitData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;


public class HttpUtil {
    private HttpUtil() {
    }

    private static RestApi getRestApi(@HostType.HostTypeChecker int hostType) {
        return RetrofitProvider.getInstance(hostType).create(RestApi.class);
    }

    public static Observable<ResultBean> numGetAuthCode(String num, String type) {
        return getRestApi(HostType.LOGIN_REGIST_HOST).numGetAuthCode(num, type)
                .compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> emailGetAuthCode(String email, String type) {
        return getRestApi(HostType.LOGIN_REGIST_HOST).emailGetAuthCode(email, type).
                compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> retrievePwd(String type, String registerStr, String pwd, String code) {
        return getRestApi(HostType.LOGIN_REGIST_HOST).retrievePwd(type, registerStr, pwd, code).
                compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> register(String type, String registerStr, String pass, String code,
                                                  String userName, String source) {
        return getRestApi(HostType.LOGIN_REGIST_HOST).register(type, registerStr, pass, code, userName, source, "2").
                compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<UserInfo> thrLogin(String id, String name, String iconUrle) {
        return getRestApi(HostType.BASE_URL_HOST).thrLogin(id, name, iconUrle, String.valueOf(1)).
                compose(new SchedulerTransformer<UserInfo>());
    }

    public static Observable<UserInfo> login(String name, String pwd) {
        return getRestApi(HostType.LOGIN_REGIST_HOST).login(name, pwd)
                .compose(new SchedulerTransformer<UserInfo>());
    }

    //重置session
    //================================================================
    public static Observable<Response<Void>> toefl(Map<String, String> userInfo) {
//        return getRestApi(HostType.TOEFL_URL_HOST).toefl(uid, userName, password, email, phone).compose(new SchedulerTransformer<>());
        return getRestApi(HostType.TOEFL_URL_HOST).toefl(userInfo).compose(new SchedulerTransformer<Response<Void>>());
    }

    public static Observable<Response<Void>> smartapply(Map<String, String> userInfoe) {
        return getRestApi(HostType.SMARTAPPLY_URL_HOST).smartapply(userInfoe).compose(new SchedulerTransformer<Response<Void>>());
    }

    public static Observable<Response<Void>> gmatl(Map<String, String> userInfoee) {
        return getRestApi(HostType.BASE_URL_HOST).gmatl(userInfoee).compose(new SchedulerTransformer<Response<Void>>());
    }

    public static Observable<Response<Void>> gossip(Map<String, String> userInfoe) {
        return getRestApi(HostType.GOSSIP_URL_HOST).gossip(userInfoe).compose(new SchedulerTransformer<Response<Void>>());
    }
    //================================================================

    public static Observable<ResultBean<UserData>> getUserCenterDetailInfo() {
        return getRestApi(HostType.BASE_URL_HOST).getPersonalCenter().compose(new SchedulerTransformer<ResultBean<UserData>>());
    }

    public static Observable<ResultBean<UserRecord>> getUserRecord() {
        return getRestApi(HostType.BASE_URL_HOST).getUserRecord().compose(new SchedulerTransformer<ResultBean<UserRecord>>());
    }


    public static Observable<ResultBean<List<KnowData>>> getKnowBase() {
        return getRestApi(HostType.BASE_URL_HOST).getKnowBase().compose(new SchedulerTransformer<ResultBean<List<KnowData>>>());
    }

    public static Observable<ResultBean<KnowDetailInfo>> getKnowInfo(String contentId) {
        return getRestApi(HostType.BASE_URL_HOST).getKnowInfo(contentId).compose(new SchedulerTransformer<ResultBean<KnowDetailInfo>>());
    }

    public static Observable<ResultBean<RemarkBean>> getRemarkList(String page) {
        return getRestApi(HostType.GOSSIP_URL_HOST).getRemarkList(page, "10", "1").compose(new SchedulerTransformer<ResultBean<RemarkBean>>());
    }

    public static Observable<RemarkData> getRemarkDetail(String id) {
        return getRestApi(HostType.GOSSIP_URL_HOST).getRemarkDetail(id).compose(new SchedulerTransformer<RemarkData>());
    }

    public static Observable<ResultBean> praiseOrCancel(String id) {
        return getRestApi(HostType.GOSSIP_URL_HOST).praiseOrCancel(id, "1").compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> reply(String content, String id, String gossipUser, String uName, String image) {
        return getRestApi(HostType.GOSSIP_URL_HOST).reply(content, "1", id, gossipUser, uName, image, "0", "", "1").compose(new SchedulerTransformer<ResultBean>());
    }


    public static Observable<ResultBean> replyFloor(String content, String id, String uid, String uName, String photo, String replyUid, String replayUName) {
        return getRestApi(HostType.GOSSIP_URL_HOST).reply(content, "2", id, uid, uName, photo, replyUid, replayUName, "1").compose(new SchedulerTransformer<ResultBean>());
    }


//    public static Observable<ResultBean> upload(MultipartBody.Part file) {
//        return getRestApi(HostType.GOSSIP_URL_HOST).upload(file);
//    }

    public static Observable<ResultBean> releaseStatus(String title, String content, List<String> images, String icon, String publisher) {
        return getRestApi(HostType.GOSSIP_URL_HOST).releaseStatus(title, content, images, new ArrayList<String>(), new ArrayList<String>(), icon, publisher, "1").compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean<List<SimulationData>>> getTestList(String type, String typeClass) {
        return getRestApi(HostType.BASE_URL_HOST).getTestList(type, typeClass).compose(new SchedulerTransformer<ResultBean<List<SimulationData>>>());
    }

    public static Observable<Object> mkTishi(String id) {
        return getRestApi(HostType.BASE_URL_HOST).mkTishi(id);
    }

    public static Observable<Object> affirmRedoModel(String id) {
        return getRestApi(HostType.BASE_URL_HOST).affirmRedoModel(id, "redo");
    }

//    ===================================继续模考======================================================

    public static Observable<ResultBean> simulationTest(String id) {
        return getRestApi(HostType.BASE_URL_HOST).simulationTest(id);
    }

    public static Observable<ResultBean> simulationHint() {
        return getRestApi(HostType.BASE_URL_HOST).simulationHint();
    }

    public static Observable<SimulationTopicData> simulationPostSign(String url) {
        return getRestApi(HostType.BASE_URL_HOST).simulationPostSign(url);
    }

    public static Observable<ResultBean> commitSimulationAnswer(int questionId, String answer, int makeTime) {
        return getRestApi(HostType.BASE_URL_HOST).commitSimulationAnswer(questionId, answer, makeTime);
    }

    public static Observable<ResultBean> simulationReset() {
        return getRestApi(HostType.BASE_URL_HOST).simulationReset().compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> simulationResetEnd() {
        return getRestApi(HostType.BASE_URL_HOST).simulationResetEnd();
    }

    public static Observable<SimulatSimpleData> simulationResult(String mkid, String mkscoreid) {
        return getRestApi(HostType.BASE_URL_HOST).simulationResult(mkid, mkscoreid).compose(new SchedulerTransformer<SimulatSimpleData>());
    }

    public static Observable<ResultBean> simulationReset(String id) {
        return getRestApi(HostType.BASE_URL_HOST).simulationReset(id).compose(new SchedulerTransformer<ResultBean>());
    }

//    ===================================继续模考======================================================

    /**
     * 获取模考第一道题目
     * mark 语文, 数学, 全套, 分别对应 type = "verbal" | "quant" | "all"
     */
    public static Observable<SimulationTopicData> getSimulationFirstTopic(String id, String mark) {
        return getRestApi(HostType.BASE_URL_HOST).getSimulationFirstTopic("redo", id, mark, "starttime").compose(new SchedulerTransformer<SimulationTopicData>());
    }

    /**
     * 获取模考其他题目
     * mark 语文, 数学, 全套, 分别对应 type = "verbal" | "quant" | "all"
     * allmark 如果是全套模考的休息过后请求第一题, 那么 allmark = "" 否则都为 "allmark"
     */
    public static Observable<SimulationTopicData> getSimulationNextTopic(String mark, String allmark) {
        return getRestApi(HostType.BASE_URL_HOST).getSimulationNextTopic("1", mark, allmark, "subject").compose(new SchedulerTransformer<SimulationTopicData>());
    }

    public static Observable<ResultBean> commitAnswer(String questionid, String useranswer) {
        return getRestApi(HostType.BASE_URL_HOST).commitAnswer(questionid, useranswer, "checke").compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> getSimulationRestul(String mkId, String mkscoreid) {
        return getRestApi(HostType.BASE_URL_HOST).getSimulationRestul(mkId, mkscoreid).compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<SimulatSimpleData> getSimulationRestulShow(String mkId, String mkscoreid) {
        return getRestApi(HostType.BASE_URL_HOST).getSimulationRestulShow(mkId, mkscoreid).compose(new SchedulerTransformer<SimulatSimpleData>());
    }

    public static Observable<NewHot> getHostGmat() {
        return getRestApi(HostType.BASE_URL_HOST).getHostGmat();
    }

    public static Observable<ResultBean<ReportData>> getReportData() {
        return getRestApi(HostType.BASE_URL_HOST).getReportData().compose(new SchedulerTransformer<ResultBean<ReportData>>());
    }

    public static Observable<ResultBean<List<HotResultData>>> getLiveLesson() {
        return getRestApi(HostType.BASE_URL_HOST).getLiveLesson();
    }

    public static Observable<ResultBean<List<HotResultData>>> getVideoLesson() {
        return getRestApi(HostType.BASE_URL_HOST).getVideoLesson().compose(new SchedulerTransformer<ResultBean<List<HotResultData>>>());
    }

    public static Observable<ResultBean<List<TeacherData>>> getTeacherList() {
        return getRestApi(HostType.BASE_URL_HOST).getTeacherList();
    }

    public static Observable<ResultBean<List<PublicLessonData>>> getOpenLesson() {
        return getRestApi(HostType.VIPLGW_URL_HOST).getOpenLesson();
    }

    public static Observable<ResultBean> addContent(String name, String... extend) {
        return getRestApi(HostType.SMARTAPPLY_URL_HOST).addContent(236, name, extend).compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> addContentToMap(Map<String, Object> map) {
        return getRestApi(HostType.SMARTAPPLY_URL_HOST).addContentToMap(map).compose(new SchedulerTransformer<ResultBean>());
    }

    /**
     * 当前页   一页获取多少条数据
     */
    public static Observable<ResultBean<List<SimuRecordData>>> getSimuRecord(String type, String pageNum, String pageSize) {
        return getRestApi(HostType.BASE_URL_HOST).getSimuRecord(type, pageNum, pageSize).compose(new SchedulerTransformer<ResultBean<List<SimuRecordData>>>());
    }

    public static Observable<ResultBean> fileRecord(MultipartBody.Part file) {
        return getRestApi(HostType.BASE_URL_HOST).fileRecord(file)/*.compose(new SchedulerTransformer<ResultBean>())*/;
    }

    public static Observable<ResultBean> upload(MultipartBody.Part file) {
        return getRestApi(HostType.GOSSIP_URL_HOST).upload(file);
    }

    public static Observable<List<CommitData>> download(String url) {
        return getRestApi(HostType.BASE_URL_HOST).download(url);
    }

    public static Observable<ResultBean> modifyName(String nickName) {
        return getRestApi(HostType.BASE_URL_HOST).modifyName(nickName).compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> newModifyName(String nickName) {
        return getRestApi(HostType.LOGIN_REGIST_HOST).newModifyName(nickName).compose(new SchedulerTransformer<ResultBean>());
    }

    /**
     * 修改用户电话, 重新登录生效
     *
     * @param uid   用户 uid
     * @param phone 新电话
     * @param code  电话验证码
     */
    public static Observable<ResultBean> modifyPhone(String uid, String phone, String code) {
        return getRestApi(HostType.LOGIN_REGIST_HOST).modifyPhone(uid, phone, code).compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> modifyEmail(String uid, String email, String code) {
        return getRestApi(HostType.LOGIN_REGIST_HOST).modifyEmail(uid, email, code).compose(new SchedulerTransformer<ResultBean>());

    }

    public static Observable<ResultBean> modifyPwd(String uid, String oldPass, String newPass) {
        return getRestApi(HostType.LOGIN_REGIST_HOST).modifyPwd(uid, oldPass, newPass).compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> getMsg(String pageNumber) {
        return getRestApi(HostType.BASE_URL_HOST).getMsg(pageNumber, "15").compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<List<ReplyData>> replyList(Map<String, String> map) {
        return getRestApi(HostType.GOSSIP_URL_HOST).replyList(map).compose(new SchedulerTransformer<List<ReplyData>>());
    }

    public static Observable<ResultBean<HeaderData>> replaceHeader(MultipartBody.Part file) {
        return getRestApi(HostType.BASE_URL_HOST).replaceHeader(file).compose(new SchedulerTransformer<ResultBean<HeaderData>>());
    }

    public static Observable<ResultBean> getSubjectTotal() {
        return getRestApi(HostType.BASE_URL_HOST).getSubjectTotal().compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> getAdvertising() {
        return getRestApi(HostType.BASE_URL_HOST).getAdvertising().compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> getBuyCourse(String catid, String pageNumber) {
        return getRestApi(HostType.BASE_URL_HOST).getBuyCourse(catid, "15", pageNumber).compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean<List<CommunityData>>> getPostList(String selectId, String page) {
        return getRestApi(HostType.GOSSIP_URL_HOST).getPostList(selectId, page, "15").compose(new SchedulerTransformer<ResultBean<List<CommunityData>>>());
    }

    public static Observable<ResultBean<List<CommunityData>>> getOldPostList(String page) {
        return getRestApi(HostType.GOSSIP_URL_HOST).getOldPostList("1", page, "15").compose(new SchedulerTransformer<ResultBean<List<CommunityData>>>());
    }

    public static Observable<List<TestInfomationData>> getTestInfo(String page) {
        return getRestApi(HostType.BASE_URL_HOST).getTestInfo(page, "15").compose(new SchedulerTransformer<List<TestInfomationData>>());
    }

    public static Observable<TestInfomationData> getTestInfomationDetail(String id) {
        return getRestApi(HostType.BASE_URL_HOST).getTestInfomationDetail(id).compose(new SchedulerTransformer<TestInfomationData>());
    }

    public static Observable<ResultBean<List<DownloadData>>> getPostTestList(String selectId, String page) {
        return getRestApi(HostType.GOSSIP_URL_HOST).getPostTestList(selectId, page, "15").compose(new SchedulerTransformer<ResultBean<List<DownloadData>>>());
    }

    public static Observable<CommunityData> getPostDeail(String postId) {
        return getRestApi(HostType.GOSSIP_URL_HOST).getPostDeail(postId).compose(new SchedulerTransformer<CommunityData>());
    }

    public static Observable<DownloadDetailData> getDownloadDeail(String postId) {
        return getRestApi(HostType.GOSSIP_URL_HOST).getDownloadDeail(postId).compose(new SchedulerTransformer<DownloadDetailData>());
    }

    public static Observable<ResultBean> postReply(String postId, String content) {
        return getRestApi(HostType.GOSSIP_URL_HOST).postReply(postId, content).compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> addPost(String title, String content) {
        return getRestApi(HostType.GOSSIP_URL_HOST).addPost("1", title, content).compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> commitFeedBack(String phone, String content) {
        return getRestApi(HostType.BASE_URL_HOST).commitFeedBack(phone, content).compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> updateDb(int lastTime) {
        return getRestApi(HostType.BASE_URL_HOST).updateDb(lastTime).compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<UpdateLocalDbData> updateLocalData(String url) {
        return getRestApi(HostType.BASE_URL_HOST).updateLocalData(url).compose(new SchedulerTransformer<UpdateLocalDbData>());
    }

    public static Observable<VersionInfo> getUpdate() {
        return getRestApi(HostType.BASE_URL_HOST).getUpdate().compose(new SchedulerTransformer<VersionInfo>());
    }

    public static Observable<TopicDiscussionData> discussion(int questionId, int page) {
        return getRestApi(HostType.BASE_URL_HOST).discussion(questionId, page).compose(new SchedulerTransformer<TopicDiscussionData>());
    }

    public static Observable<ResultBean> commitTopicBug(int questionId, String type, String des) {
        return getRestApi(HostType.BASE_URL_HOST).commitTopicBug(questionId, type, des).compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ResultBean> addComment(String content, int questionId, String commentId) {
        return getRestApi(HostType.BASE_URL_HOST).addComment(content, questionId, commentId).compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<HighScoreData> highScoreList(int page) {
        return getRestApi(HostType.BASE_URL_HOST).highScoreList(page, "15").compose(new SchedulerTransformer<HighScoreData>());
    }

    public static Observable<HighScoreDetailData> highScoreDetail(String contentId) {
        return getRestApi(HostType.BASE_URL_HOST).highScoreDetail(contentId).compose(new SchedulerTransformer<HighScoreDetailData>());
    }

    public static Observable<ActionData> action() {
        return getRestApi(HostType.BASE_URL_HOST).action().compose(new SchedulerTransformer<ActionData>());
    }

    public static Observable<List<ListenData>> gmatFreeInfo() {
        return getRestApi(HostType.BASE_URL_HOST).gmatFreeInfo().compose(new SchedulerTransformer<List<ListenData>>());
    }

    public static Observable<List<ListenData>> gmatIntroInfo() {
        return getRestApi(HostType.BASE_URL_HOST).gmatIntroInfo().compose(new SchedulerTransformer<List<ListenData>>());
    }

}
