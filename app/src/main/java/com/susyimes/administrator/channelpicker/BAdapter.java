package com.susyimes.administrator.channelpicker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Susyimes on 2016/8/24 0024.
 */
public class BAdapter extends RecyclerView.Adapter<BAdapter.BViewHolder>  {
    private Context context;
    private List<ChannelOtherBean> listuserBean;
    private SusBottomClickListener susClickListener;

    public BAdapter(Context context,List<ChannelOtherBean> listuserBean) {
        this.context=context;
        this.listuserBean=listuserBean;

    }

    @Override
    public BViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_grid_itemb, parent, false);
        return new BViewHolder(v);



    }
    public void SusClickListener(SusBottomClickListener susClickListener) {
        this.susClickListener = susClickListener;
    }
    @Override
    public void onBindViewHolder(final BViewHolder holder, final int position) {
        holder.mTextView.setText(listuserBean.get(position).getCname());

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ChannelBean channelBean=new ChannelBean();

                if (listuserBean.size()>position){
                channelBean.setCname(listuserBean.get(position).getCname());
                channelBean.setCid(listuserBean.get(position).getCid());
                channelBean.setIsChoose("1");
                susClickListener.onClick(view,holder.mContainer,channelBean,position);}
            }
        });

    }

    @Override
    public int getItemCount() {

        return listuserBean.size();
    }


    public static class BViewHolder extends RecyclerView.ViewHolder  {
        public FrameLayout mContainer;
        public View mDragHandle;
        public TextView mTextView;


        public BViewHolder(View v) {
            super(v);
            mContainer = (FrameLayout) v.findViewById(R.id.container);
            mDragHandle = v.findViewById(R.id.drag_handle);
            mTextView = (TextView) v.findViewById(android.R.id.text1);
        }


    }
}
