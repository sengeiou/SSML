package zhonghuass.ssml.mvp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.adapter.BaseViewHolder;
import com.github.library.layoutView.CircleImageView;
import com.jess.arms.utils.ArmsUtils;

import java.util.List;

import zhonghuass.ssml.R;
import zhonghuass.ssml.mvp.model.appbean.DanynimicBean;
import zhonghuass.ssml.mvp.ui.activity.GraphicDetailsActivity;
import zhonghuass.ssml.mvp.ui.activity.VideoDetailActivity;

public class DanymicAdapter extends BaseQuickAdapter<DanynimicBean, RecyclerView.ViewHolder> {
    Context mContext;
    public DanymicAdapter(Context activity, List<DanynimicBean> mlist) {
        super(activity,mlist);
        mContext = activity;
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.recommend_item;
    }

    @Override
    protected void convert(BaseViewHolder holder, DanynimicBean item) {
        holder.setText(R.id.company_name, item.getMember_name())
                .setText(R.id.company_title, item.getContent_title());
        //设置theme
        TextView flag = holder.getView(R.id.tv_flag);
        String theme_title = item.getTheme_title();
        if (theme_title != null && !theme_title.equals("")) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(theme_title);
            spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#ffd800")), 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            flag.setVisibility(View.VISIBLE);
            flag.setText(spannableStringBuilder);
        } else {
            flag.setVisibility(View.GONE);
        }
        //设置红心
        ImageView likeImg = holder.getView(R.id.iflike);
        if(item.isPraise_tag()){
            likeImg.setBackgroundResource(R.mipmap.home_icon_4);
        }else {
            likeImg.setBackgroundResource(R.mipmap.home_icon_3);
        }

        final String cover_width = item.getCover_width();
        final String cover_height = item.getCover_height();
        int screenWidth = ArmsUtils.getScreenWidth(mContext);
        int imgWidth = (screenWidth) / 2;
        double resize = (double)(Integer.parseInt(cover_width)/3 / imgWidth);

        if(resize==0){
            resize=1;
        }
        int imghight = (int)(Integer.parseInt(cover_height)/3 / resize);

        ImageView iv = (ImageView) holder.getView(R.id.recommend_img);
        ImageView ivVideo = (ImageView) holder.getView(R.id.iv_video);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.getView(R.id.recommend_img).getLayoutParams();
        layoutParams.width = imgWidth;
        layoutParams.height = imghight;
        iv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.shape_iv_bg));
        iv.setLayoutParams(layoutParams);


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.shape_iv_bg);
            Glide.with(mContext)
                    .load(item.getContent_cover())
//                .apply(requestOptions)
                    .into(iv);
        if (item.getContent_type().equals("0")){
            ivVideo.setVisibility(View.GONE);
        }else {
            ivVideo.setVisibility(View.VISIBLE);
        }

        Glide.with(mContext)
                .load(item.getMember_image())
                //.apply(requestOptions)
                .into((CircleImageView) holder.getView(R.id.company_icon));
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getContent_type().equals("0")){
                    Intent intent = new Intent(mContext, GraphicDetailsActivity.class);
                    intent.putExtra("content_id",item.getContent_id());
                    intent.putExtra("member_id",item.getMember_id());
                    intent.putExtra("member_type",item.getMember_type());
                    mContext.startActivity(intent);
                }else {
                    Intent intent = new Intent(mContext, VideoDetailActivity.class);
                    intent.putExtra("content_id",item.getContent_id());
                    intent.putExtra("member_id",item.getMember_id());
                    intent.putExtra("member_type",item.getMember_type());
                    mContext.startActivity(intent);
                }


            }
        });
    }





}
