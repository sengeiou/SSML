package zhonghuass.ssml.mvp.ui.adapter;

import android.content.Intent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.library.baseAdapter.BaseQuickAdapter;
import com.github.library.baseAdapter.BaseViewHolder;

import java.util.List;

import zhonghuass.ssml.R;
import zhonghuass.ssml.mvp.model.appbean.TradeBean;
import zhonghuass.ssml.mvp.ui.activity.TradeDetailActivity;

public class TradeAdapter extends BaseQuickAdapter<TradeBean, BaseViewHolder> {
    public TradeAdapter(int trade_item, List<TradeBean> data) {
        super(trade_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TradeBean item) {
        helper.setText(R.id.trade_name, item.getShortname())
                .setText(R.id.tv_type, item.getServicetype())
                .setText(R.id.tv_phone, item.getPhone())
                .setText(R.id.tv_address, item.getAddr());
        Glide.with(mContext)
                .load(item.getLogo())
                .into((ImageView) helper.getView(R.id.trade_log));
        helper.convertView.setOnClickListener((v) -> {
                    Intent intent = new Intent(mContext, TradeDetailActivity.class);
                    intent.putExtra("eid", item.getEid());
                    intent.putExtra("member_type", item.getServicetype());

                    mContext.startActivity(intent);
                }


        );
    }
}
