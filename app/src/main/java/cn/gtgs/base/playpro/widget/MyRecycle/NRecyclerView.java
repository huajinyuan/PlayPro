package cn.gtgs.base.playpro.widget.MyRecycle;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.lang.reflect.Constructor;

import cn.gtgs.base.OTO.utils.F;
import cn.gtgs.base.OTO.widget.MyRecycle.base.BaseLayout;
import cn.gtgs.base.OTO.widget.MyRecycle.base.BaseLoaderView;
import cn.gtgs.base.OTO.widget.MyRecycle.base.BaseRefreshView;
import cn.gtgs.base.OTO.widget.MyRecycle.base.HeaderStateInterface;
import cn.gtgs.base.OTO.widget.MyRecycle.base.LoaderStateInterface;
import cn.gtgs.base.OTO.widget.MyRecycle.impl.LoaderView;
import cn.gtgs.base.OTO.widget.MyRecycle.impl.RefreshView;
import cn.gtgs.base.OTO.widget.MyRecycle.util.Logger;

/**
 * 描述：Controller recyclerView load behavior.
 * 时间: 2016-08-01 16:42
 */
public class NRecyclerView extends BaseLayout {

    private RecyclerView.LayoutManager layoutManager;
    private InnerAdapter adapter;
    private final RecyclerView.AdapterDataObserver mDataObserver = new DataObserver();

    public NRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private ViewGroup AdtureView;
    private ViewGroup BottomView;

    /**
     * The contentView scroll state.
     */
    private int scrollState = RecyclerView.SCROLL_STATE_IDLE;

    /**
     * Set adventure view , The view is belong to contentview.
     * You must set AdventureView before setAdapter.
     *
     * @param view
     */
    public void setAdtureView(ViewGroup view) {
        AdtureView = view;
    }

    /**
     * Set bottom view that last position at contentview.
     *
     * @param view
     */
    public void setBottomView(ViewGroup view) {
        BottomView = view;
    }

    /**
     * Set headerView that contain refreshView.
     *
     * @param headerView
     */
    public void setHeaderView(ViewGroup headerView) {
        this.headerView = headerView;
    }


    private View errorView;


    /**
     * Add load error view that contain Adventure view.
     *
     * @param view   The error view.
     * @param isWrap The error view is wrap parent view content or match parent.
     */
    public void setErrorView(View view, boolean isWrap) {
        if (AdtureView == null)
            throw new IllegalStateException("You should setAdtureView at first");

        if (view == null) return;

        errorView = view;
        LinearLayout.LayoutParams lp = null;
        if (isWrap)
            lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        else {
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, rect.bottom - rect.top - AdtureView.getHeight());
        }

        if (footerView != null)
            footerView.setVisibility(View.INVISIBLE);

        AdtureView.addView(view, AdtureView.getChildCount(), lp);
    }

    /**
     * Remove error view.
     */
    public void removeErrorView() {
        if (AdtureView != null && errorView != null) {
            if (footerView != null)
                footerView.setVisibility(View.VISIBLE);
            AdtureView.removeView(errorView);
            errorView = null;
        }
    }


    /**
     * Create the refreshView.
     *
     * @param context
     * @return
     */
    @Override
    protected ViewGroup CreateRefreshView(Context context) {
        LinearLayout headerView = new LinearLayout(context);
        headerView.setOrientation(VERTICAL);
        refreshView = new RefreshView(context);
        refreshView.setState(HeaderStateInterface.IDLE);
        return headerView;
    }


    /**
     * The entry view that you can use recyclerview or custom by yourself.
     *
     * @param context
     * @param attrs
     * @param innerView
     * @return
     */
    @Override
    protected ViewGroup CreateEntryView(final Context context, AttributeSet attrs, String innerView) {

        if (TextUtils.equals(innerView, "NONE"))
            contentView = new RecyclerView(context, attrs);
        else {
            //TODO create your view.
            try {
                Class c = Class.forName(innerView);
                Class[] paramtersTypes = {Context.class, AttributeSet.class};
                Constructor constructor = c.getConstructor(paramtersTypes);
                Object[] paramters = {context, attrs};
                contentView = (ViewGroup) constructor.newInstance(paramters);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Don't has found " + innerView);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("Don't has found " + innerView + " constructor method");
            } catch (Exception e) {
                throw new IllegalStateException("Create " + innerView + " error");
            }
        }

        ((RecyclerView) contentView).addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                F.e("============================================================" + dy);
                if (dy < 0)
                    orientation = CONTENT_VIEW_SCROLL_ORIENTATION.UP;
                else {
                    orientation = (dy > 0) ? CONTENT_VIEW_SCROLL_ORIENTATION.DOWN :
                            CONTENT_VIEW_SCROLL_ORIENTATION.IDLE;
                }

                F.d("orientation = " + orientation);
                IsFirstItem = getFirstVisibleItem() == 0 ? true : false;
                IsLastItem = (getLastVisibleItem() + 1 == adapter.getItemCount()) ? true : false;

                //Solve the isNestConfilct led to a small bug.
                isNestConfilct = false;

                View firstView = contentView.getChildAt(0);
                if (firstView != null) {
                    if (IsFirstItem && getLocalRectPosition(firstView).top == 0) {

                        if (!isRefreshing)
                            pullOverScroll();
                        else {
                            if (scrollState != RecyclerView.SCROLL_STATE_DRAGGING)
                                pullEventWhileLoadData(-1);
                        }
                    }
                }

                View lastView = contentView.getChildAt(contentView.getChildCount() - 1);
                if (lastView != null) {
                    if (IsLastItem && getLocalRectPosition(
                            lastView).bottom == lastView.getHeight()) {
                        if (isLoadingMore) {
//                            scrollDy += dy;
                            if (scrollState != RecyclerView.SCROLL_STATE_DRAGGING)
                                pullEventWhileLoadData(1);
                        }
                    }
                }
            }

            //TODO We should load more when recyclerView scroll to bottom.
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //Record recyclerView scrolll state.
                scrollState = newState;

                if ((newState == RecyclerView.SCROLL_STATE_IDLE) && IsLastItem && !isNestLoad) {

                    //TODO 已经滑动到最底端
                    if (!isLoadingMore && isPullLoadEnable && !isRefreshing) {
                        View lastView = contentView.getChildAt(contentView.getChildCount() - 1);
                        Rect rect = getLocalRectPosition(lastView);
                        if (lastView.getHeight() == rect.bottom) {
                            pullMoreEvent();
                        }
                    }
                }

            }
        });

        return contentView;
    }

    private int getLastVisibleItem() {
        int lastVisiblePos = 0;
        if (layoutManager != null) {
            if (layoutManager instanceof LinearLayoutManager)
                lastVisiblePos = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            else if (layoutManager instanceof GridLayoutManager)
                ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] lastVisiblePositions = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()]);
                lastVisiblePos = getMaxElem(lastVisiblePositions);
            }
        }
        return lastVisiblePos;
    }

    private int getFirstVisibleItem() {
        int firstVisblePos = -1;
        if (layoutManager != null) {
            if (layoutManager instanceof LinearLayoutManager)
                firstVisblePos = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            else if (layoutManager instanceof GridLayoutManager)
                firstVisblePos = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
            else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] firstVisblePosSpan = ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()]);
                firstVisblePos = firstVisblePosSpan[0];
            }
        }
        return firstVisblePos;
    }

    private int getMaxElem(int[] arr) {
        int size = arr.length;
        int maxVal = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            if (arr[i] > maxVal)
                maxVal = arr[i];
        }
        return maxVal;
    }

    /**
     * Set contentView
     *
     * @param viewGroup
     */
    public void setEntryView(ViewGroup viewGroup) {
        try {
            removeViewInLayout(contentView);
            standView = viewGroup;
            addView(standView, 2);
            if (footerView != null)
                footerView.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set debug enable.
     *
     * @param debug
     */
    public void setLogDebug(boolean debug) {
        Logger.setDebug(debug);
    }

    /**
     * Get contentView.
     *
     * @return
     */
    public ViewGroup getEntryView() {
        return contentView;
    }

    /**
     * Reset contentView
     */
    public void resetEntryView() {
        try {
            if (standView != null) {
                removeViewInLayout(standView);
                addView(contentView, 2);
                ((RecyclerView) contentView).scrollToPosition(0);
                standView = null;
                if (footerView != null)
                    footerView.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param decor
     * @param size  decor height
     */
    public void addItemDecoration(RecyclerView.ItemDecoration decor, int size) {
        if (contentView != null) {
            ((RecyclerView) contentView).addItemDecoration(decor);
            ITEM_DIVIDE_SIZE = size;
        } else
            throw new IllegalStateException("You hasn't add contentView in baseLayout");
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor, int index, int size) {
        if (contentView != null) {
            ((RecyclerView) contentView).addItemDecoration(decor, index);
            ITEM_DIVIDE_SIZE = size;
        } else
            throw new IllegalStateException("You hasn't add contentView in baseLayout");
    }

    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        if (contentView != null)
            ((RecyclerView) contentView).setItemAnimator(animator);
        else
            throw new IllegalStateException("You hasn't add contentView in baseLayout");
    }

    @Override
    protected ViewGroup CreateLoadView(Context context) {
        LinearLayout footerView = new LinearLayout(context);
        footerView.setOrientation(VERTICAL);
        loaderView = new LoaderView(context);
        loaderView.setState(LoaderStateInterface.LOADING_MORE);
        return footerView;
    }

    /**
     * Scroll to first position.
     */
    @Override
    protected void scrollToFirstItemPosition() {
        ((RecyclerView) contentView).scrollToPosition(0);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        this.layoutManager = layout;
        ((RecyclerView) contentView).setLayoutManager(layout);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = new InnerAdapter(adapter);
        ((RecyclerView) contentView).setAdapter(this.adapter);
        adapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
    }

    public void setNotifyChange() {
        ((RecyclerView) contentView).getAdapter().notifyDataSetChanged();
    }

    public void setRefreshView(BaseRefreshView view) {
        if (headerView != null) {
            if (refreshView != null)
                headerView.removeViewInLayout(refreshView);
            refreshView = view;
            headerView.addView(refreshView, 0);
            ViewGroup.LayoutParams lp = refreshView.getLayoutParams();
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            refreshView.setLayoutParams(lp);
            requestLayout();
        }
    }

    public void setLoaderView(BaseLoaderView view) {
        if (footerView != null) {
            if (loaderView != null)
                footerView.removeViewInLayout(loaderView);
            loaderView = view;
            footerView.addView(loaderView, 0);
            ViewGroup.LayoutParams lp = loaderView.getLayoutParams();
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            loaderView.setLayoutParams(lp);
            requestLayout();
        }
    }


    /**
     * If you can scroll when loading data (including the refresh and load more)
     *
     * @param enable
     */
    public void setLoadDataScrollable(boolean enable) {
        LoadDataScrollEnable = enable;
    }

    public boolean getLoadDataScrollable() {
        return LoadDataScrollEnable;
    }

    public void setTotalPages(int total) {
        this.totalPages = total;
    }

    private class DataObserver extends RecyclerView.AdapterDataObserver {

        @Override
        public void onChanged() {
            if (adapter != null) {
                adapter.notifyDataSetChanged();
                IsFirstItem = getFirstVisibleItem() == 0 ? true : false;
                IsLastItem = (getLastVisibleItem() + 1 == adapter.getItemCount()) ? true : false;
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (adapter != null)
                adapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            if (adapter != null)
                adapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if (adapter != null)
                adapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (adapter != null)
                adapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            if (adapter != null)
                adapter.notifyItemMoved(fromPosition, toPosition);
        }
    }

    ;

    private class InnerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private RecyclerView.Adapter adapter;

        private static final int TYPE_ADVENTRUE = -1;
        private static final int TYPE_NORMAL = 15;
        private static final int TYPE_BOTTOM = -2;

        public InnerAdapter(RecyclerView.Adapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null) {
                if (AdtureView != null) {
                    if (lp instanceof StaggeredGridLayoutManager.LayoutParams && holder.getLayoutPosition() == 0) {
                        StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                        p.setFullSpan(true);
                    }
                }
                if (BottomView != null && currentPages == totalPages && !isLoadingMore) {
                    if (lp instanceof StaggeredGridLayoutManager.LayoutParams && holder.getLayoutPosition() == getItemCount() - 1) {
                        StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                        p.setFullSpan(true);
                    }
                }
            }

            if (layoutManager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager)
                        layoutManager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        boolean SpanResult = false;
                        if (AdtureView != null && BottomView != null)
                            if (currentPages == totalPages && !isLoadingMore) {
                                SpanResult = (position == 0 || position == getItemCount() - 1);
                            } else {
                                SpanResult = position == 0;
                            }
                        else if (AdtureView != null)
                            SpanResult = (position == 0);
                        else if (BottomView != null && currentPages == totalPages && !isLoadingMore)
                            SpanResult = (position == getItemCount() - 1);
                        return SpanResult
                                ? gridManager.getSpanCount() : 1;
                    }
                });
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType == TYPE_ADVENTRUE)
                return new InnerViewHolder(AdtureView);
            if (viewType == TYPE_BOTTOM && currentPages == totalPages && !isLoadingMore) {
                return new InnerViewHolder(BottomView);
            }
            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(0) == TYPE_ADVENTRUE) {
                if (position == 0)
                    return;
//
                int adjPoi = --position;
                if (adapter != null) {
                    if (adjPoi < adapter.getItemCount()) {
                        adapter.onBindViewHolder(holder, adjPoi);
                    }
                }
                return;
            }
            adapter.onBindViewHolder(holder, position);
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return AdtureView == null ? position : TYPE_ADVENTRUE;
            } else if (position == getItemCount() - 1 && currentPages == totalPages && !isLoadingMore)
                return BottomView == null ? position : TYPE_BOTTOM;
            else
                return position;
        }

        @Override
        public int getItemCount() {
            int count = adapter.getItemCount();
            if (AdtureView != null)
                count++;
            if (BottomView != null && currentPages == totalPages && !isLoadingMore) {
                count++;
            }
            return count;
        }

        private class InnerViewHolder extends RecyclerView.ViewHolder {
            public InnerViewHolder(View itemView) {
                super(itemView);
            }
        }
    }


}