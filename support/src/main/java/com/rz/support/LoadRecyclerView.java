package com.rz.support;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Adapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Created by Zhou on 2016/6/27.

 *
 */
public class LoadRecyclerView extends RecyclerView {
    private static final int DISABLE_LOADMORE = 0;

    private static final int ENABLE_LOADMORE = 1;

    @State
    public int funState = DISABLE_LOADMORE;

    @IntDef({DISABLE_LOADMORE,ENABLE_LOADMORE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }
    public LoadRecyclerView(Context context) {
        super(context);
        init();
    }

    public LoadRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


//    private AdapterDataObserver mAdapterDataObserver = new AdapterDataObserver() {
//        @Override
//        public void onChanged() {
//            super.onChanged();
//        }
//    };

    public interface OnLoadMoreListener{
        void LoadMore();
    }
    protected OnLoadMoreListener mOnLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener){
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    private void init(){
        super.addOnScrollListener(new OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        int lastVisiblePosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();

                        if (lastVisiblePosition >= getAdapter().getItemCount() - 1) {
                            if(funState == ENABLE_LOADMORE){
//                                Log.w("---------debug--------"," LoadMore");
                                mOnLoadMoreListener.LoadMore();
                                funState = DISABLE_LOADMORE;
                            }
                        }
                    }
                }
            }
        });
    }


    @Override
    public void setAdapter(Adapter adapter) {
        LoadMoreAdapter loadMoreAdapter;
        if (adapter instanceof LoadMoreAdapter) {
            loadMoreAdapter = (LoadMoreAdapter) adapter;
//            loadMoreAdapter.registerAdapterDataObserver(mAdapterDataObserver);
            super.setAdapter(adapter);
        } else {
            loadMoreAdapter = new LoadMoreAdapter(adapter);
//            loadMoreAdapter.registerAdapterDataObserver(mAdapterDataObserver);
            super.setAdapter(loadMoreAdapter);
        }
    }



    public void setFootViewType(int footViewType) {
        this.footViewType = footViewType;
    }

    private int footViewType = LoadMoreAdapter.NULL_VIEW;


    public void setFirstInit(){
        setFootViewType(LoadMoreAdapter.NULL_VIEW);
        addFooterView = false;
        funState = DISABLE_LOADMORE;
        getAdapter().notifyDataSetChanged();

    }
    public void setFirstError(){
        setFootViewType(LoadMoreAdapter.NULL_VIEW);
        addFooterView = false;
        funState = DISABLE_LOADMORE;
        getAdapter().notifyDataSetChanged();

    }
    public void setSuccess(){
        setFootViewType(LoadMoreAdapter.LOADDING_VIEW);
        addFooterView = true;
        funState = ENABLE_LOADMORE;
        getAdapter().notifyDataSetChanged();

    }
    public void setFirstFail(){
        setFootViewType(LoadMoreAdapter.EMPTY_VIEW);
        addFooterView = true;
        funState = DISABLE_LOADMORE;
        getAdapter().notifyDataSetChanged();

    }
    public void setFail(){
        setFootViewType(LoadMoreAdapter.NO_MORE_DATA_VIEW);
        addFooterView = true;
        funState = DISABLE_LOADMORE;
        getAdapter().notifyDataSetChanged();

    }
    public void setError(){
        setFootViewType(LoadMoreAdapter.NULL_VIEW);
        addFooterView = false;
        funState = ENABLE_LOADMORE;
        getAdapter().notifyDataSetChanged();

    }
    public void setNull(){
        setFootViewType(LoadMoreAdapter.NULL_VIEW);
        addFooterView = false;
        getAdapter().notifyDataSetChanged();
    }
    public void setNoMoreData(){
        addFooterView = true;
        setFootViewType(LoadMoreAdapter.NO_MORE_DATA_VIEW);
        getAdapter().notifyDataSetChanged();

    }
    public void setLoadding(){
        addFooterView = true;
        setFootViewType(LoadMoreAdapter.LOADDING_VIEW);
        getAdapter().notifyDataSetChanged();

    }

    public void setEmpty(){
        addFooterView = true;
        setFootViewType(LoadMoreAdapter.EMPTY_VIEW);
        getAdapter().notifyDataSetChanged();
    }



    //TODO:预留
    public void setEmptyView(FootView emptyView) {
        EmptyView = emptyView;
    }

    public void setNoMoreDataView(FootView noMoreDataView) {
        NoMoreDataView = noMoreDataView;
    }

    public void setLoaddingView(FootView loaddingView) {
        LoaddingView = loaddingView;
    }

    private FootView LoaddingView;
    private FootView EmptyView;
    private FootView NoMoreDataView;

    private boolean addFooterView = false;

    public FootView getLoaddingView() {
        if (LoaddingView == null){
            return new FooterLoaddingView();
        }
        return LoaddingView;
    }

    public FootView getEmptyView() {
        if(EmptyView == null){
            return new FooterEmptyView();
        }
        return EmptyView;
    }

    public FootView getNoMoreDataView() {
        if(NoMoreDataView == null){
            return new FooterNoMoreDataView();
        }
        return NoMoreDataView;
    }

    public class LoadMoreAdapter extends Adapter<ViewHolder> {

        // 定义
        public Adapter mAdapter;
        public LoadMoreAdapter(Adapter adapter) {
            mAdapter = adapter;
        }
        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            super.registerAdapterDataObserver(observer);
            mAdapter.registerAdapterDataObserver(observer);
        }
        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            super.unregisterAdapterDataObserver(observer);
            mAdapter.unregisterAdapterDataObserver(observer);
        }


        private static final int LOADDING_VIEW = 1;
        private static final int NO_MORE_DATA_VIEW = 2;
        private static final int EMPTY_VIEW = 3;
        private static final int NULL_VIEW = 4;
        private static final int ITEM_VIEW = 5;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType){
                case LOADDING_VIEW:
                    return new LoaddingVH(parent);
                case NO_MORE_DATA_VIEW:
                    return new NoMoreDataVH(parent);
                case EMPTY_VIEW:
                    return new EmptyVH(parent);
            }
            return  mAdapter.onCreateViewHolder(parent,viewType);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if(getItemViewType(position) == ITEM_VIEW){
                mAdapter.onBindViewHolder(holder,position);
            }
        }


        @Override
        public int getItemViewType(int position) {
            super.getItemViewType(position);
            if(addFooterView){
                if (position + 1 == getItemCount()) {
                    return footViewType;
                }
            }
            return ITEM_VIEW;
        }

        @Override
        public int getItemCount() {
            int count;
            if(addFooterView){
                count = mAdapter.getItemCount()+1;
            }else{
                count = mAdapter.getItemCount();
            }
            return count;
        }


        private class EmptyVH extends ViewHolder {
            public EmptyVH(ViewGroup parent) {
                super(getEmptyView().onCreateView(parent));
            }
        }
        private class LoaddingVH extends ViewHolder {
            public LoaddingVH(ViewGroup parent) {
                super(getLoaddingView().onCreateView(parent));
            }
        }
        private class NoMoreDataVH extends ViewHolder {
            public NoMoreDataVH(ViewGroup parent) {
                super(getNoMoreDataView().onCreateView(parent));
            }
        }

    }
}
