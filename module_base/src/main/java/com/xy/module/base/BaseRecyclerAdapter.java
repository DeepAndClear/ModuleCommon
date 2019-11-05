package com.xy.module.base;

import android.content.Context;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding2.view.RxView;
import com.xy.commonbase.helper.LogHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int NORMAL_TYPE = 0;

    public static final int HEAD_TYPE = 1;

    public static final int FOOT_TYPE = 4;

    public static final int NORMAL_TYPE_ONE = 2;

    public static final int NORMAL_TYPE_TWO = 3;

    private static final int DATA_SHOW_STATE = 10;

    private static final int DATA_CHANGE_STATE = 11;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({DATA_SHOW_STATE,DATA_CHANGE_STATE})
    public  @interface DataAppendState{
        int SHOW = DATA_SHOW_STATE;
        int CHANGE = DATA_CHANGE_STATE;
    }

    protected CompositeDisposable disposable;

    protected List<T> mList;

    protected Context mContext;

    protected Object headData;

    protected Object footData;

    protected OnItemClickListener<T> onItemClickListener;

    protected OnItemLongClickListener<T> onItemLongClickListener;

    protected int listOffset = 0;

    protected int pageRange;

    protected SparseArray<T> mChecks;

    private boolean mCheckStatus = false;

    public void addListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void addListener(OnItemClickListener<T> onItemClickListener
            , OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
    }


    public BaseRecyclerAdapter(Context mContext, CompositeDisposable disposable) {
        this.mContext = mContext;
        this.disposable = disposable;
        mChecks = new SparseArray<>();
    }

    public synchronized void setData(List<T> list) {
        if (list == null)
            return;
        if (mList == null || mList != list) {
            mList = list;
        }
        pageRange = list.size();
        notifyDataSetChanged();
    }

    public void setPageData(int position ,int page,List<T> list){
        if (list == null){
            return;
        }
        int pageOffset = (page -1) * pageRange;
        LogHelper.e(pageOffset+"              "+page);
        if (mList!= null){
            if (mList.size() == pageOffset + list.size()) {
                for (int i = 0; i < list.size(); i++) {
                    mList.set(i + pageOffset, list.get(i));
                }
                if (position == -1) {
                    notifyItemRangeChanged(pageOffset, getItemCount());
                }else {
                    notifyItemChanged(position);
                }
            }else if (mList.size() < pageOffset + list.size()){
                for (int i = 0; i < list.size(); i++) {
                    if (i + pageOffset >= mList.size()){
                        mList.add(i + pageOffset,list.get(i));
                    }else {
                        mList.set(i + pageOffset, list.get(i));
                    }
                }
                if (position == -1) {
                    notifyItemRangeChanged(pageOffset, getItemCount());
                }else {
                    notifyItemRangeChanged(pageOffset, getItemCount());
                }

            }else {
                int j = mList.size() - pageOffset - list.size();
                for (int i = 0; i < j; i++) {
                    mList.remove(position+i);
                }
                if (position == -1) {
                    notifyItemRangeChanged(pageOffset, getItemCount());
                }else {
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());
                }
            }
        }
    }

    public void appendData(List<T> list) {
        if (list == null)
            return;
        int position = 0;
        if (mList == null) {
            mList = list;
        } else if (mList != list) {
            position = mList.size()+listOffset;
            mList.addAll(list);
        }
        notifyItemRangeInserted(position, list.size());
    }

    public synchronized <H> void setHeadData(H data) {
        if (this.headData != null && this.headData.equals(data))
            return;
        if (this.headData != null) {
            this.headData = data;
            notifyItemChanged(0);
            return;
        }
        this.headData = data;
        if (mList == null){
            notifyDataSetChanged();
        }else {
            notifyItemInserted(0);
            notifyItemRangeChanged(0,mList.size() + listOffset);
        }
    }

    public synchronized <F> void setFootData(F data) {
        if (this.footData != null && this.footData.equals(data))
            return;
        if (this.footData != null) {
            this.footData = data;
            notifyItemChanged(getItemCount() - 1);
            return;
        }
        this.footData = data;
        if (mList == null){
            notifyDataSetChanged();
        }else {
            notifyItemInserted(getItemCount() - 1);
        }
    }

    public int getPageByPosition(int position){
        if (pageRange!= 0 && position >= listOffset && (position - listOffset) < mList.size()){
            LogHelper.e(position+"        "+listOffset+"        "+pageRange+"      "+(((position - listOffset) / pageRange) + 1));
            return ((position - listOffset) / pageRange) + 1;
        }
        return 0;
    }

    public List<T> getDataOrigin(){
        return mList;
    }

    public void modifyItemData(int position,T t){
        mList.set(position,t);
        notifyItemChanged(position);
    }

    public void modifyItemRangeData(int position,List<T> t){
        for (int i = 0; i < t.size(); i++) {
            mList.set(position+i,t.get(i));
        }
        notifyItemRangeChanged(position,t.size());
    }

    public void setCheckState(boolean status) {
        this.mCheckStatus = status;
        if (status) {
            if (mChecks.size() > 0)
                mChecks.clear();
        } else {
            mChecks.clear();
        }
        notifyDataSetChanged();
    }

    public void checkAll() {
        if (mList != null) {
            for (int i = listOffset > 1 ? 1 : listOffset; i < getItemCount(); i++) {
                if (listOffset > 0 && i == 0) {
                    mChecks.append(0, null);
                } else {
                    mChecks.append(i, mList.get(i));
                }
            }
        }
        notifyItemRangeChanged(listOffset > 1 ? 1 : listOffset, getItemCount() - 1);
    }

    public void removeAll() {
        if (mCheckStatus) {
            mChecks.clear();
            mCheckStatus = false;
        }
        mList.clear();
        notifyItemRangeRemoved(listOffset > 1 ? 1 : listOffset,getItemCount() -1);
    }

    public void removeSelect() {
        mChecks.clear();
        notifyDataSetChanged();
    }

    public void removeDataByPosition(int position) {
        if (position >= 0 && position < getItemCount()) {
            if (listOffset > 0 && position == 0) {
                headData = null;
                notifyItemRangeChanged(position, getItemCount());
                return;
            }
            if (listOffset > 1 && position >= getItemCount() - 1) {
                footData = null;
                notifyItemRangeChanged(position, getItemCount());
                return;
            }
            mList.remove(position-listOffset);
            if (mCheckStatus) {
                mChecks.delete(position-listOffset);
            }
            notifyItemRemoved(position-listOffset);
            notifyItemRangeChanged(position-listOffset, getItemCount());
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEAD_TYPE) {
            View view = LayoutInflater.from(mContext)
                    .inflate(getHeadView(), parent, false);
            return createHeadViewHolder(view);
        } else if (viewType == FOOT_TYPE) {
            View view = LayoutInflater.from(mContext)
                    .inflate(getFootView(), parent, false);
            return createFootViewHolder(view);
        } else if (viewType == NORMAL_TYPE){
            View view = LayoutInflater.from(mContext)
                    .inflate(getItemView(), parent, false);
            return createViewHolder(view);
        } else if (viewType == NORMAL_TYPE_ONE) {
            View view = LayoutInflater.from(mContext)
                    .inflate(getOneItemView(), parent, false);
            return createOneViewHolder(view);
        } else if (viewType == NORMAL_TYPE_TWO) {
            View view = LayoutInflater.from(mContext)
                    .inflate(getTwoItemView(), parent, false);
            return createTwoViewHolder(view);
        } else {
            return createExtraViewHolder(parent,viewType);
        }
    }

    protected RecyclerView.ViewHolder createExtraViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(getItemView(), parent, false);
        return createViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        disposable.add(Observable.merge(RxView.clicks(holder.itemView)
                        .filter(o -> onItemClickListener != null)
                        .map(o -> 0),
                RxView.longClicks(holder.itemView)
                        .filter(o -> onItemLongClickListener != null)
                        .map(o -> 1))
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    switch (o) {
                        case 0:
                            if (holder.getAdapterPosition() == -1)
                                return;
                            if (headData != null && position == 0) {
                                actionForHead(headData);
                            } else if (footData != null && position == getItemCount() - 1) {
                                actionForFoot(footData);
                            } else {
                                onItemClickListener.onItemClick(holder.getAdapterPosition()
                                        ,holder.getAdapterPosition() - listOffset >= mList.size()
                                                ?null:mList.get(holder.getAdapterPosition() - listOffset));
                            }
                            break;
                        case 1:
                            if (headData != null && position == 0) {
                                actionLongForHead(headData);
                            } else if (footData != null && position == getItemCount() - 1) {
                                actionLongForFoot(footData);
                            } else {
                                onItemClickListener.onItemClick(holder.getAdapterPosition()
                                        , holder.getAdapterPosition() - listOffset >= mList.size()
                                                ?null:mList.get(holder.getAdapterPosition() - listOffset));
                            }
                            break;
                    }
                }));
    }

    protected void actionLongForFoot(Object data) {

    }

    protected void actionLongForHead(Object data) {

    }

    protected void actionForFoot(Object data) {

    }

    protected void actionForHead(Object data) {

    }

    protected int getHeadView() {
        return 0;
    }

    protected int getFootView() {
        return 0;
    }

    protected int getOneItemView() {
        return getItemView();
    }

    protected int getTwoItemView() {
        return getItemView();
    }

    protected RecyclerView.ViewHolder createHeadViewHolder(View view) {
        return null;
    }

    protected RecyclerView.ViewHolder createFootViewHolder(View view) {
        return null;
    }

    protected RecyclerView.ViewHolder createOneViewHolder(View view) {
        return createViewHolder(view);
    }

    protected RecyclerView.ViewHolder createTwoViewHolder(View view) {
        return createViewHolder(view);
    }

    protected abstract int getItemView();

    protected abstract VH createViewHolder(View view);

    public void clearData() {
        if (headData!= null)
            headData = null;
        if (footData!= null)
            footData = null;
        if (mList == null || mList.size() == 0)
            return;
        else {
            mList.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        listOffset = 0;
        if (headData != null) {
            listOffset++;
        }
        return listOffset + (mList == null ? 0 : mList.size())+ (footData!=null?1:0);
    }

    @Override
    public int getItemViewType(int position) {
        if (footData != null && position == getItemCount() - 1)
            return FOOT_TYPE;
        if (headData != null && position == 0) {
            return HEAD_TYPE;
        }
        return configItemViewType(position - listOffset);
    }

    protected int configItemViewType(int position){
        return super.getItemViewType(position);
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position, T t);
    }

    public interface OnItemLongClickListener<T> {
        void onItemLongClick(int position, T o);
    }
}
