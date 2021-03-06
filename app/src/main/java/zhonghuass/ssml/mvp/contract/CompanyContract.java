package zhonghuass.ssml.mvp.contract;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import zhonghuass.ssml.http.BaseResponse;
import zhonghuass.ssml.mvp.model.appbean.AreaBean;
import zhonghuass.ssml.mvp.model.appbean.TradeBean;
import zhonghuass.ssml.mvp.model.appbean.TradeItemBean;


public interface CompanyContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

        void showTradeData(List<TradeBean> data);

        void showAreaData(String datas);

        void showTradeItem(List<TradeItemBean> data);

        void notifystate();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

        Observable<BaseResponse<List<TradeBean>>> getTradeData(String area, String type, int currentPage, int pagesize);

        Observable<ResponseBody> getAreaData();

        Observable<BaseResponse<List<TradeItemBean>>> getTradeItem();
    }
}
