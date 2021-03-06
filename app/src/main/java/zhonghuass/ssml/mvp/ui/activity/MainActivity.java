package zhonghuass.ssml.mvp.ui.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.github.library.layoutView.BottomNavigationViewEx;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import zhonghuass.ssml.R;
import zhonghuass.ssml.di.component.DaggerMainActivityComponent;
import zhonghuass.ssml.di.module.MainActivityModule;
import zhonghuass.ssml.mvp.EventMsg;
import zhonghuass.ssml.mvp.contract.MainActivityContract;
import zhonghuass.ssml.mvp.model.appbean.UserInfoBean;
import zhonghuass.ssml.mvp.presenter.MainActivityPresenter;
import zhonghuass.ssml.mvp.ui.fragment.CompanyFragment;
import zhonghuass.ssml.mvp.ui.fragment.DialyFragment;
import zhonghuass.ssml.mvp.ui.fragment.HomeFragment;
import zhonghuass.ssml.mvp.ui.fragment.MycenterFragment;
import zhonghuass.ssml.utils.*;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static zhonghuass.ssml.utils.EventBusTags.ACTIVITY_FRAGMENT_REPLACE;


public class MainActivity extends BaseActivity<MainActivityPresenter> implements MainActivityContract.View {

    @BindView(R.id.toolbar_back)
    RelativeLayout toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_frame)
    FrameLayout mainFrame;
    @BindView(R.id.bottomMenu)
    BottomNavigationViewEx bottomMenu;
    @BindView(R.id.navigation)
    LinearLayout mNavigationView;
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.tv_menu1)
    TextView tvmenu1;
    @BindView(R.id.tv_menu2)
    TextView tvmenu2;
    @BindView(R.id.tv_menu3)
    TextView tvmenu3;
    @BindView(R.id.tv_menu4)
    TextView tvmenu4;
    @BindView(R.id.tv_menu5)
    TextView tvmenu5;
    @BindView(R.id.tv_menu6)
    TextView tvmenu6;
    private List<Integer> mTitles;
    private List<Fragment> mFragments;
    private List<Integer> mNavIds;
    private int mReplace = 0;
    private HomeFragment homeFragment;
    private CompanyFragment companyFragment;
    private DialyFragment dialyFragment;
    private MycenterFragment mycenterFragment;
    private WebSocketClient mSocketClient;
    private UserInfoBean mUserInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        EventBusUtils.register(this);
//        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMainActivityComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
    }


    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public boolean useEventBus() {
        return super.useEventBus();
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //关闭菜单切换动画特效
        bottomMenu.enableAnimation(false);
        bottomMenu.enableShiftingMode(false);
        bottomMenu.enableItemShiftingMode(false);

        toolbarBack.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
        //  mPresenter.requestPermissions();
        if (mTitles == null) {
            mTitles = new ArrayList<>();
            mTitles.add(R.string.tab_home);
            mTitles.add(R.string.tab_company);
            mTitles.add(R.string.tab_daily);
            mTitles.add(R.string.tab_mycenter);
        }
        if (mNavIds == null) {
            mNavIds = new ArrayList<>();
            mNavIds.add(R.id.tab_home);
            mNavIds.add(R.id.tab_company);
            mNavIds.add(R.id.tab_daily);
            mNavIds.add(R.id.tab_mycenter);
        }


        if (savedInstanceState == null) {
            homeFragment = HomeFragment.newInstance();
            companyFragment = CompanyFragment.newInstance();
            dialyFragment = DialyFragment.newInstance();
            mycenterFragment = MycenterFragment.newInstance();
        } else {
            mReplace = savedInstanceState.getInt(ACTIVITY_FRAGMENT_REPLACE);
            FragmentManager fm = getSupportFragmentManager();
            homeFragment = (HomeFragment) FragmentUtils.findFragment(fm, HomeFragment.class);
            companyFragment = (CompanyFragment) FragmentUtils.findFragment(fm, CompanyFragment.class);
            dialyFragment = (DialyFragment) FragmentUtils.findFragment(fm, DialyFragment.class);
            mycenterFragment = (MycenterFragment) FragmentUtils.findFragment(fm, MycenterFragment.class);
        }
        if (mFragments == null) {
            mFragments = new ArrayList<>();
            mFragments.add(homeFragment);
            mFragments.add(companyFragment);
            mFragments.add(dialyFragment);
            mFragments.add(mycenterFragment);
        }
        FragmentUtils.addFragments(getSupportFragmentManager(), mFragments, R.id.main_frame, 0);
        toolbarTitle.setText(mTitles.get(0));//设置默认显示第一个Fragment标题

        bottomMenu.setOnNavigationItemSelectedListener(menuSelect);

        //底部菜单栏图标字体点击颜色变化在这里修改
//        int[][] states = new int[][]{
//                new int[]{-android.R.attr.state_checked},
//                new int[]{android.R.attr.state_checked}
//        };
//        int[] colors = new int[]{getResources().getColor(R.color.corlor28),
//                getResources().getColor(R.color.colorcf1313)
//        };
//        ColorStateList csl = new ColorStateList(states, colors);
//        bottomMenu.setItemTextColor(csl);
//        bottomMenu.setItemIconTintList(csl);


        // 设置NavigationView宽度
        ViewGroup.LayoutParams params = mNavigationView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels * 2 / 3;
        mNavigationView.setLayoutParams(params);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener menuSelect = item -> {
        switch (item.getItemId()) {
            case R.id.bottom_menu1:
                mReplace = 0;
                changeFragment();
                return true; //不返回图标不变色
            case R.id.bottom_menu2:
                mReplace = 1;
                changeFragment();
                return true; //不返回图标不变色
            case R.id.bottom_menu3:
                mReplace = 2;
                changeFragment();
                return true; //不返回图标不变色
            case R.id.bottom_menu4:
                String member_id = PrefUtils.getString(MainActivity.this, Constants.USER_ID, "");
                String member_type = "1";
                if (member_id.equals("")) {
                    ArmsUtils.startActivity(LogInActivity.class);
                } else {
                    //请求查询我的数据统计接口
                    mPresenter.getMyStatistics(member_id, member_type);
                    mReplace = 3;
                    changeFragment();
                    mycenterFragment.setData(1);
                    return true; //不返回图标不变色
                }


        }
        return false;
    };

    private void changeFragment() {
        toolbar.setVisibility(View.GONE);
      /*  if (mReplace == 3 || mReplace == 0) {
            //隐藏标题栏
        } else {
            toolbar.setVisibility(View.VISIBLE);
        }
        toolbarTitle.setText(mTitles.get(mReplace));*/
        FragmentUtils.hideAllShowFragment(mFragments.get(mReplace));
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ACTIVITY_FRAGMENT_REPLACE, mReplace);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showNavigation(EventMsg eventMsg) {
        if (eventMsg != null && eventMsg.isShowNav) {
            mDrawerLayout.openDrawer(mNavigationView);
        }
    }

    private double firstTime = 0;

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            ArmsUtils.snackbarText("再按一次退出程序");
            firstTime = secondTime;
        } else {
            ArmsUtils.exitApp();
        }
    }


    @OnClick({R.id.civ_photo, R.id.tv_menu1, R.id.tv_menu2, R.id.tv_menu3, R.id.tv_menu4, R.id.tv_menu5, R.id.tv_menu6})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.civ_photo:
                ArmsUtils.startActivity(MyInfoActivity.class);
                break;
            case R.id.tv_menu1:
                ArmsUtils.startActivity(MyCollectionActivity.class);
                break;
            case R.id.tv_menu2:
                ArmsUtils.startActivity(MyConcernActivity.class);
                break;
            case R.id.tv_menu3:
                ArmsUtils.startActivity(MyFansActivity.class);
                break;
            case R.id.tv_menu4:
                ArmsUtils.startActivity(RealNameActivity.class);
                break;
            case R.id.tv_menu5:
                ArmsUtils.startActivity(HelpActivityActivity.class);
                break;
            case R.id.tv_menu6:
                ArmsUtils.startActivity(MySettingActivity.class);
                break;
        }
    }

    private void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //TODO 这里URL 别忘了切换到自己的IP
                    mSocketClient = new WebSocketClient(new URI("ws://video.zhonghuass.cn:10000")) {
                        @Override
                        public void onOpen(ServerHandshake handshakedata) {
                            Log.d("picher_log", "打开通道" + handshakedata.getHttpStatus());
                            if (mSocketClient != null) {
                                JSONObject parameters = new JSONObject();
                                parameters.put("message_type", "register");
                                parameters.put("member", "1_2");
                                parameters.put("user_agent", "android");
                                mSocketClient.send(parameters.toJSONString());
                            }
                        }

                        @Override
                        public void onMessage(String message) {
                            Log.d("picher_log", "接收消息" + message);
                        }

                        @Override
                        public void onClose(int code, String reason, boolean remote) {
                            Log.d("picher_log", "通道关闭");
                        }

                        @Override
                        public void onError(Exception ex) {
                            Log.d("picher_log", "链接错误");
                        }
                    };
                    mSocketClient.connect();

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    @Override
    public void getStatisticsSuccess(UserInfoBean userInfoBean) {
        if (userInfoBean != null) {
            mUserInfo = (UserInfoBean) ACache.get(this).getAsObject(Constants.USERINFO);
            if (mUserInfo == null) {
                mUserInfo = new UserInfoBean();
            }
            mUserInfo.member_id = userInfoBean.member_id;
            mUserInfo.member_type = userInfoBean.member_type;
            mUserInfo.member_image = userInfoBean.member_image;//" : "用户头像
            mUserInfo.amount_of_vermicelli = userInfoBean.amount_of_vermicelli;//
            mUserInfo.amount_of_concern = userInfoBean.amount_of_concern;//" :
            mUserInfo.amount_of_praise = userInfoBean.amount_of_praise;//" :
            mUserInfo.content_image_text_num = userInfoBean.content_image_text_num == null ? "0" : userInfoBean.content_image_text_num;//" :
            mUserInfo.content_video_num = userInfoBean.content_video_num == null ? "0" : userInfoBean.content_video_num;//" :
            ACache.get(this).put(Constants.USERINFO, mUserInfo);
            //通知我的页面刷新信息
            EventMsg msg = new EventMsg();
            msg.isShowInfo = true;
            EventBusUtils.post(msg);
        }


    }
}
