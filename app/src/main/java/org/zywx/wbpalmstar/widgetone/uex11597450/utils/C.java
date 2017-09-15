package org.zywx.wbpalmstar.widgetone.uex11597450.utils;

public interface C {

    int APP_TYPE_DEV = 1;
    int APP_TYPE_PRO = APP_TYPE_DEV + 2;

    String DEFALUT_PWD = "123456";

    String REPLY = "1";
    int CONTROLLER_DB_UPDATE_VERSION = 2;

    String UPDATE_LOCAL_DB = "update_local_db";

    String FONT_SIEZ_CHANGE = "font_siez_change";

    String REMARK_REFRESH_ACTION = "remark_refresh_action";
    String EXIT_LOGIN_ACTION = "exit_login_action";
    String REFRESH_MAKE_RECORD = "refresh_make_record";
    String LOGIN_SUCCESS = "login_success";
    String THR_LOGIN_ACTION = "thr_login_action";
    String POST_COMMUNITY_REMARK = "post_community_remark";
    String REFRESH_SIMULATION_INFO = "refresh_simulation_info";
    String TOPIC_ERROR_TAG = "topic_error_tag";
    String UPLOAD_DB_TOPIC_SIZE = "upload_db_topic_size";
    String CENTER_CHANGE = "center_change";
    String SIMULATION_RECORD = "simulation_record";
    String SIMULATION_LIST_REFRESH = "simulation_list_refresh";

    int HEADER_CHANGE = 500;
    int NICKNAME_CHANGE = HEADER_CHANGE + 1;

    int TRIAL_COURSE = 1;
    int INTRO_COURSE = TRIAL_COURSE + 1;
    int PUBLIC_COURSE = INTRO_COURSE + 1;
    int HOT_COURSE = PUBLIC_COURSE + 1;
    int TEACHER_COURSE = HOT_COURSE + 1;
    int MAKE_TEST_PERSON = TEACHER_COURSE + 1;
    int KNOW_POINT_TYPE = MAKE_TEST_PERSON + 1;
    int KNOW_POINT_TYPE_ITEM = KNOW_POINT_TYPE + 1;
    int REQUST_CODE_UPDATE = KNOW_POINT_TYPE_ITEM + 1;

//    //首页试听链接,与试听列表
//    String TRIAL_SC = "http://bjsy.gensee.com/training/site/v/16558960?nickname=GMAT";
//    String TRIAL_RC = "http://bjsy.gensee.com/training/site/v/96034817?nickname=RC";
//    String TRIAL_CR = "http://bjsy.gensee.com/training/site/v/26829265?nickname=GMAT";
//    String TRIAL_MATH = "http://bjsy.gensee.com/training/site/v/19608245?nickname=GMAT";

    //随机存储数标识
    String FREE_SC = "free_sc";
    String FREE_RC = "free_rc";
    String FREE_CR = "free_cr";
    String FREE_MATH = "free_math";
    //随机存储数标识
    String INTRO_SC = "intro_sc";
    String INTRO_RC = "intro_rc";
    String INTRO_CR = "intro_cr";
    String INTRO_MATH = "intro_math";
    //随机存储数标识
    String PUBLIC_ = "public_";
    String HOT_ = "hot_";
    String TEACHER_ = "teacher_";
    //做题备考随机
    String MAKE_TEST_ = "nake_test_";
    //知识点 五种类别里面的随机数
    String KNOW_POINT_ = "know_point_";
    //进入知识点后的类别条目随机数
    String KNOW_POINT_TYPE_ITEM_ = "know_point_type_item_";


    //    http://p.qiao.baidu.com/im/index?siteid=7905926&ucid=18329536&cp=&cr=&cw=
    //    要把 header 里面的 Referer 设置为 http://www.gmatonline.cn/
    int DEAL_ADD_HEADER = 1;
    int DEAL_GO_MAIN = DEAL_ADD_HEADER + 1;
    int FREE_INTERCEPT_BUSY = DEAL_GO_MAIN + 1;//试听满了，弹框

    String DEAL_TYPE = "deal_type";//

    //题目状态
    int NO_START = 0;//未开始
    int START = NO_START + 1;//已经开始
    int END = START + 1;//结束

    //做过的题目
    int MAKE_TOPIC = 1;
    //重新做题
    int AGAIN_START = MAKE_TOPIC + 1;

    int NEED_UPLOAD = 1;
    int OLD_DATA = 2;

    int REQUEST_RESULT_SUCCESS = 1;
    //获取验证码
    String REGISTER_TYPE = "1";
    String RETRIEVE_TYPE = "2";
    String MODIFY_INFO_TYPE = "3";
    //     ["SC", "CR", "RC", "PS", "DS"]
//        sectionidArray
//        ["6","8","7","4","5"]
    int PS = 4;
    int DS = PS + 1;
    int SC = DS + 1;
    int RC = SC + 1;
    int CR = RC + 1;

    int LOGIN_REQUEST_CODE = 100;
    int COM_REQUEST_CODE = LOGIN_REQUEST_CODE + 1;
    int REMARK_RELEASE_REQUEST_CODE = COM_REQUEST_CODE + 1;
    int MAKE_REQUEST_CODE = REMARK_RELEASE_REQUEST_CODE + 1;
    int SETTING_RESET_TXT_CODE = MAKE_REQUEST_CODE + 1;
    int SET_NICK_REQUEST_CODE = SETTING_RESET_TXT_CODE + 1;
    int ASK_QUESTION_CODE = SET_NICK_REQUEST_CODE + 1;

    int USER_EIXT = 555;

    //做题结果
    int CORRECT = 1;
    int ERROR = CORRECT + 1;

    int LANGUAGE = 1;
    int MATH = LANGUAGE + 1;
    int ALL = MATH + 1;

    String VERBAL = "verbal";
    String QUANT = "quant";
    String TYPE_ALL = "all";

    String SYNC_SERVICE_DB_URL_KEY = "sync_service_db_url_key";

    //模考状态  2表示模考结束  1表示继续模考
    String MARK_QUESTION = "2";
    String GOON_MARK_QUESTION = "1";

    int ALL_TOPIC = 1;
    int ERROR_TOPIC = ALL_TOPIC + 1;
}
