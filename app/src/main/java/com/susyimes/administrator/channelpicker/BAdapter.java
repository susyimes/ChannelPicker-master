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
    private List<ChannelUserBean> listuserBean;
    private SusClickListener susClickListener;

    public BAdapter(Context context,List<ChannelUserBean> listuserBean) {
        this.context=context;
        this.listuserBean=listuserBean;

    }

    @Override
    public BViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_grid_itemb, parent, false);
        return new BViewHolder(v);



    }
    public void SusClickListener(SusClickListener susClickListener) {
        this.susClickListener = susClickListener;
    }
    @Override
    public void onBindViewHolder(final BViewHolder holder, final int position) {
        holder.mTextView.setText(listuserBean.get(position).getChusername());

        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChannelBean channelBean=new ChannelBean();
                channelBean.setChname(listuserBean.get(position).getChusername());
                susClickListener.onClick(view,holder.mTextView,channelBean,position);
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
