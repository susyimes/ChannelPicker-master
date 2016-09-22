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

import android.graphics.Bitmap;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import java.util.ArrayList;
import java.util.List;

public class DraggableGridFragment extends Fragment {
    private RecyclerView mRecyclerView,mRecyclerViewB;
    private GridLayoutManager mLayoutManager,mLayoutManagerb;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;
    private List<ChannelBean> list;
    private List<ChannelUserBean> list2;
    private List<ChannelBean> listopen;
    private List<String> setting;
    private BAdapter badapter;
    private TextView tv_edit,tv_notice;
    private Boolean clickAble=false;


    boolean isMove = false;

    public DraggableGridFragment() {
        super();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_list_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_edit= (TextView) view.findViewById(R.id.tv_edit);
        tv_notice= (TextView) view.findViewById(R.id.tv_notice);



        list=new ArrayList<>();
        list2=new ArrayList<>();
        listopen=new ArrayList<>();
        setting=new ArrayList<>();

        if (MyApplication.sDb.query(ChannelBean.class).size()>0) {
            list.addAll(MyApplication.sDb.query(ChannelBean.class));
            //MyApplication.sDb.deleteAll(ChannelBean.class);

        }



        Log.i("listopen",list.toString());
        Log.i("listopen",MyApplication.sDb.query(ChannelBean.class).size()+"");

        for (int i=0;i<4;i++){
            ChannelUserBean channelBean=new ChannelUserBean();
            channelBean.setChusername("xxx"+i);
            list2.add(channelBean);
        }
        if (list.size()==0){
        for (int i=0;i<5;i++){
            ChannelBean channelBean=new ChannelBean();
            channelBean.setChname("fun"+i);
            list.add(channelBean);
        }}
        listopen=list;
        //noinspection ConstantConditions

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);

        mRecyclerViewB = (RecyclerView) getView().findViewById(R.id.recycler_viewb);


         mLayoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
         mLayoutManagerb = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);


        // drag & drop manager
        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
       /* mRecyclerViewDragDropManager.setDraggingItemShadowDrawable(
                (NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z3));*/
        // Start dragging after long press
        mRecyclerViewDragDropManager.setInitiateOnLongPress(true);
        mRecyclerViewDragDropManager.setInitiateOnMove(false);
        mRecyclerViewDragDropManager.setLongPressTimeout(550);


        //to define span size by susyimes
     /*   mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

        if (position<list.size()){
                if (list.get(position).equals("xxx")){
                    return 4;
                }}
                return 1;
            }
        });*/

        //adapter

        final DraggableGridAdapter myItemAdapter = new DraggableGridAdapter(list,setting);
        badapter=new BAdapter(getContext(),list2);
        badapter.SusClickListener(OnSusClick());

        mAdapter = myItemAdapter;

        mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(myItemAdapter);      // wrap for dragging
        myItemAdapter.SusTopClickListener(OnSusTopClick());
        myItemAdapter.SusTopLongClickListener(OnSusLongClick());

        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();
       // mWrappedAdapter.notifyDataSetChanged();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        mRecyclerView.setItemAnimator(animator);
        mRecyclerViewB.setLayoutManager(mLayoutManagerb);
        mRecyclerViewB.setAdapter(badapter);

        // additional decorations
        //noinspection StatementWithEmptyBody
        if (supportsViewElevation()) {
            // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
        } else {
            mRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z1)));
        }

        mRecyclerViewDragDropManager.attachRecyclerView(mRecyclerView);

        // for debugging
//        animator.setDebug(true);
//        animator.setMoveDuration(2000);


        //define bottom recyclerview
        initEditBtn();


    }



    private void initEditBtn() {
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_edit.getText().equals("Done")){
                    tv_edit.setText("Edit");
                    tv_notice.setVisibility(View.INVISIBLE);
                    clickAble=false;
                    if (setting.size()>0){
                        setting.remove(0);}
                    mAdapter.notifyDataSetChanged();

                }else {
                    tv_edit.setText("Done");
                    clickAble=true;
                    tv_notice.setVisibility(View.VISIBLE);
                    setting.add(0,"edit");
                    mAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    @Override
    public void onDestroy() {
        /*MyApplication.sDb.deleteAll(ChannelBean.class);


        for (int i=0;i<list.size();i++){
            MyApplication.sDb.insert(list.get(i));

            //MyApplication.sDb.update(list.get(i));
        }*/
       // MyApplication.sDb.query(ChannelBean.class).removeAll(listopen);
        MyApplication.sDb.deleteAll(ChannelBean.class);
       // MyApplication.sDb.delete(listopen);
        for (int i=0;i<list.size();i++){
            ChannelBean channelBean=new ChannelBean();
            channelBean.setChname(list.get(i).getChname());
            MyApplication.sDb.save(channelBean);

            //MyApplication.sDb.update(list.get(i));
        }
        //MyApplication.sDb.save(list);

        Log.i("list",list.toString());
        if (MyApplication.sDb.query(ChannelBean.class).size()!=0)
        Log.i("listbean", MyApplication.sDb.query(ChannelBean.class).get(0).toString());

        super.onDestroy();
    }
    private SusTopLongClickListener OnSusLongClick() {
        return new SusTopLongClickListener() {
            @Override
            public void onClick(View v, View card, ChannelUserBean holgaItem, int pos) {

                if (tv_edit.getText().equals("Edit")){
                tv_edit.setText("Done");
                clickAble=true;
                tv_notice.setVisibility(View.VISIBLE);
                setting.add(0,"edit");
                mAdapter.notifyDataSetChanged();}
            }
        };
    }

    private SusTopClickListener OnSusTopClick() {
        return new SusTopClickListener() {
            @Override
            public void onClick(View v, View card, ChannelUserBean holgaItem, int pos) {
                if (v != null&&v==card&&!isMove&&clickAble) {
                    isMove=true;
                    final ImageView moveImageView = getView(card);
                    TextView newTextView = (TextView) card.findViewById(android.R.id.text1);
                    final int[] startLocation = new int[2];
                    v.getLocationInWindow(startLocation);
                    //  final ChannelItem channel = ((DragAdapter) parent.getAdapter()).getItem(position);//获取点击的频道内容
                    // otherAdapter.setVisible(false);
                    //添加到最后一个
                    //otherAdapter.addItem(channel);
                    list.remove(pos);
                    mAdapter.notifyItemRemoved(pos);
                    new Handler().postDelayed(() ->   mAdapter.notifyDataSetChanged(),300);
                    list2.add(holgaItem);
                    badapter.notifyDataSetChanged();


                    new Handler().postDelayed(() -> mLayoutManagerb.getChildAt(mLayoutManagerb.findLastVisibleItemPosition()).setVisibility(View.INVISIBLE),10);
                    //mLayoutManagerb.getChildAt(mLayoutManagerb.findLastVisibleItemPosition()).setVisibility(View.INVISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            try {
                                int[] endLocation = new int[2];
                                //获取终点的坐标
                                //  otherGridView.getChildAt(otherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                // mRecyclerViewB.getChildAt()
                                mLayoutManagerb.getChildAt(mLayoutManagerb.findLastVisibleItemPosition()).getLocationInWindow(endLocation);
                                MoveAnim(moveImageView, startLocation , endLocation,null,holgaItem,2);
                               // mLayoutManagerb.getChildAt(mLayoutManagerb.findLastVisibleItemPosition()).setVisibility(View.VISIBLE);
                                //userAdapter.setRemove(position);
                                new Handler().postDelayed(() ->  mLayoutManagerb.getChildAt(mLayoutManagerb.findLastVisibleItemPosition()).setVisibility(View.VISIBLE),300-50L);


                            } catch (Exception localException) {
                            }
                        }
                    }, 50L);
                }





            }
        };

    }


    private SusBottomClickListener OnSusClick() {


        return new SusBottomClickListener() {
            @Override
            public void onClick(final View v, final View card, final ChannelBean holgaItem, final int pos) {
                if (v != null&&v==card&&!isMove){
                    if (v != null) {
                        isMove=true;
                        final ImageView moveImageView = getView(card);
                        TextView newTextView = (TextView) card.findViewById(android.R.id.text1);
                        final int[] startLocation = new int[2];
                        v.getLocationInWindow(startLocation);
                      //  final ChannelItem channel = ((DragAdapter) parent.getAdapter()).getItem(position);//获取点击的频道内容
                       // otherAdapter.setVisible(false);
                        //添加到最后一个
                        //otherAdapter.addItem(channel);
                        list2.remove(pos);
                        badapter.notifyItemRemoved(pos);
                        new Handler().postDelayed(() ->   badapter.notifyDataSetChanged(),300);
                        list.add(holgaItem);
                        mAdapter.notifyDataSetChanged();
                        new Handler().postDelayed(() -> mLayoutManager.getChildAt(mLayoutManager.findLastVisibleItemPosition()).setVisibility(View.INVISIBLE),10);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                try {
                                    int[] endLocation = new int[2];
                                    //获取终点的坐标
                                  //  otherGridView.getChildAt(otherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                   // mRecyclerViewB.getChildAt()
                                    mLayoutManager.getChildAt(mLayoutManager.findLastVisibleItemPosition()).getLocationInWindow(endLocation);
                                    MoveAnim(moveImageView, startLocation , endLocation, holgaItem,null,1);

                                   new Handler().postDelayed(() ->   mLayoutManager.getChildAt(mLayoutManager.findLastVisibleItemPosition()).setVisibility(View.VISIBLE),300-50L);
                                    //userAdapter.setRemove(position);



                                } catch (Exception localException) {
                                }
                            }
                        }, 50L);
                    }
                }
            }
        };
    }


    private void installBottomView() {






    }

    @Override
    public void onPause() {
        mRecyclerViewDragDropManager.cancelDrag();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (mRecyclerViewDragDropManager != null) {
            mRecyclerViewDragDropManager.release();
            mRecyclerViewDragDropManager = null;
        }

        if (mRecyclerView != null) {
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(null);
            mRecyclerView = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }
        mAdapter = null;
        mLayoutManager = null;

        super.onDestroyView();
    }

    private boolean supportsViewElevation() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }

   /* public AbstractDataProvider getDataProvider() {
       // return ( (MainActivity.this)getActivity()).getDataProvider();
        return null;
    }*/
   private ViewGroup getMoveViewGroup() {
       ViewGroup moveViewGroup = (ViewGroup) getActivity().getWindow().getDecorView();
       LinearLayout moveLinearLayout = new LinearLayout(getContext());
       moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
       moveViewGroup.addView(moveLinearLayout);
       return moveLinearLayout;
   }


   private void MoveAnim(View moveView, int[] startLocation,int[] endLocation, final ChannelBean moveChannel,final ChannelUserBean moveBean,
                         final int location) {
       int[] initLocation = new int[2];
       //获取传递过来的VIEW的坐标
       moveView.getLocationInWindow(initLocation);
       //得到要移动的VIEW,并放入对应的容器中
       final ViewGroup moveViewGroup = getMoveViewGroup();
       final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
       //创建移动动画
       TranslateAnimation moveAnimation = new TranslateAnimation(
               startLocation[0], endLocation[0], startLocation[1],
               endLocation[1]);
      /* TranslateAnimation moveAnimation = new TranslateAnimation(
               0, 150, 150,
              0);
     */
       moveAnimation.setDuration(300);//动画时间
       //动画配置
       AnimationSet moveAnimationSet = new AnimationSet(true);
       moveAnimationSet.setFillAfter(false);//动画效果执行完毕后，View对象不保留在终止的位置
       moveAnimationSet.addAnimation(moveAnimation);
       mMoveView.startAnimation(moveAnimationSet);
       moveAnimationSet.setAnimationListener(new Animation.AnimationListener() {

           @Override
           public void onAnimationStart(Animation animation) {
               isMove = true;
           }

           @Override
           public void onAnimationRepeat(Animation animation) {
           }

           @Override
           public void onAnimationEnd(Animation animation) {
               moveViewGroup.removeView(mMoveView);

/*if (location==1){

    mLayoutManager.getChildAt(mLayoutManager.findLastVisibleItemPosition()).setVisibility(View.VISIBLE);
             }else if(location==2){
    mLayoutManagerb.getChildAt(mLayoutManagerb.findLastVisibleItemPosition()).setVisibility(View.VISIBLE);
}*/
              /* badapter.notifyDataSetChanged();
               mWrappedAdapter.notifyDataSetChanged();*/


               // instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView

            /*   if (clickGridView instanceof RecyclerView) {
                   otherAdapter.setVisible(true);
                   otherAdapter.notifyDataSetChanged();
                   userAdapter.remove();

               }else{
                   userAdapter.setVisible(true);
                   userAdapter.notifyDataSetChanged();
                   otherAdapter.remove();
               }*/

               isMove = false;
           }
       });
   }

    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(getActivity());
        iv.setImageBitmap(cache);
        return iv;
    }

}
