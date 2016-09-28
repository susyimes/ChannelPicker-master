/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.susyimes.administrator.channelpicker;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

import java.util.List;

class DraggableGridAdapter
        extends RecyclerView.Adapter<DraggableGridAdapter.MyViewHolder>
        implements DraggableItemAdapter<DraggableGridAdapter.MyViewHolder> {
    private static final String TAG = "MyDraggableItemAdapter";
    private List<ChannelBean> sampledata;
    private SusTopClickListener susClickListener;
    private SusTopLongClickListener  susLongClickListener;
    private List<String> setting;
    // NOTE: Make accessible with short name
    private interface Draggable extends DraggableItemConstants {
    }



    public static class MyViewHolder extends AbstractDraggableItemViewHolder {
        public FrameLayout mContainer;
        public View mDragHandle;
        private ImageView imagedel;

        public TextView mTextView;

        public MyViewHolder(View v) {
            super(v);
            mContainer = (FrameLayout) v.findViewById(R.id.container);
            mDragHandle = v.findViewById(R.id.drag_handle);
            mTextView = (TextView) v.findViewById(android.R.id.text1);
            imagedel= (ImageView) v.findViewById(R.id.iv_del);
        }
    }

    public  void SusTopClickListener(SusTopClickListener susClickListener) {
        this.susClickListener = susClickListener;
    }
    public  void SusTopLongClickListener(SusTopLongClickListener susLongClickListener) {
        this.susLongClickListener = susLongClickListener;
    }

    public DraggableGridAdapter(List<ChannelBean> listsampledata,List<String> setting) {

        this.sampledata= listsampledata;
        this.setting=setting;

        // DraggableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return sampledata.get(position).getId();
      //  mProvider.getItem(position).getId()
    }

/*
    @Override
    public int getItemViewType(int position) {
        return mProvider.getItem(position).getViewType();
    }*/

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_grid_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {



        // set text
        if (setting.size()==0||position==0){
            holder.imagedel.setVisibility(View.INVISIBLE);
        }else {
            holder.imagedel.setVisibility(View.VISIBLE);
        }
        holder.mTextView.setText(sampledata.get(position).getCname());
        ChannelOtherBean channelOtherBean =new ChannelOtherBean();
        channelOtherBean.setCname(sampledata.get(position).getCname());
        channelOtherBean.setCid(sampledata.get(position).getCid());
        channelOtherBean.setIsChoose("0");
        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                susClickListener.onClick(view,holder.mContainer, channelOtherBean,position);
            }
        });
        holder.mContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                susLongClickListener.onClick(view,holder.mContainer, channelOtherBean,position);
                return false;
            }
        });



        // set background resource (target view ID: container)
     /*   final int dragState = holder.getDragStateFlags();

        if (((dragState & Draggable.STATE_FLAG_IS_UPDATED) != 0)) {
            int bgResId;

            if ((dragState & Draggable.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_item_dragging_active_state;

                // need to clear drawable state here to get correct appearance of the dragging item.
                DrawableUtils.clearState(holder.mContainer.getForeground());
            } else if ((dragState & Draggable.STATE_FLAG_DRAGGING) != 0) {
                bgResId = R.drawable.bg_item_dragging_state;
            } else {
                bgResId = R.drawable.bg_item_normal_state;
            }

            holder.mContainer.setBackgroundResource(bgResId);
        }*/

    }

    @Override
    public int getItemCount() {
        return sampledata.size();
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        Log.d(TAG, "onMoveItem(fromPosition = " + fromPosition + ", toPosition = " + toPosition + ")");

        if (fromPosition == toPosition) {
            return;
        }
        if (fromPosition==0||toPosition==0){
            return;

        }
        final ChannelBean item = sampledata.remove(fromPosition);
        //sampledata.moveItem(fromPosition, toPosition);

        sampledata.add(toPosition, item);


        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public boolean onCheckCanStartDrag(MyViewHolder holder, int position, int x, int y) {
        if (position==0){
            return false;
        }
        return true;
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(MyViewHolder holder, int position) {
        // no drag-sortable range specified
        return null;
    }

    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        if (dropPosition==0){
            return false;
        }
        return true;
    }
}
