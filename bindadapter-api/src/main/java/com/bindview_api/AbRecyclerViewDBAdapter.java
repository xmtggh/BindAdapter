package com.bindview_api;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 关于recyclerview 的databinding daapter
 *
 * @param <T>
 * @author ggh
 * @email 626393661@qq.com
 */
public abstract class AbRecyclerViewDBAdapter<T> extends RecyclerView.Adapter<BindingViewHolder> {
    protected List<T> datas;
    protected Context context;
    protected LayoutInflater inflater;

    protected OnItemClickListener onItemClickListener;

    protected int dataBindingId;
    public AbRecyclerViewDBAdapter(Context context, List<T> list , int id) {
        this.datas = list;
        this.context = context;
        this.dataBindingId = id;
        inflater = LayoutInflater.from(context);
    }

    /**
     * @desc 清空数据并刷新adapter
     */
    public void clearData() {
        if (this.datas != null) {
            clear();
        }
    }

    public List<T> getList() {
        return datas;
    }

    public void clear() {
        this.datas.clear();
        notifyDataSetChanged();
    }

    /**
     * 设置数据
     *
     * @param
     */
    public void setDataList(List<T> list) {
        if (list != null) {
            this.datas = list;
            notifyDataSetChanged();
        }
    }

    /**
     * 添加数据
     *
     * @param
     */
    public void addDataList(List<T> list) {
        if (list != null) {
            this.datas.addAll(list);
            notifyDataSetChanged();
        }
    }

    public T getItem(int position) {
        try {
            return datas.get(position);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 添加数据
     *
     * @param
     */
    public void addDataList(List<T> list, int location) {
        if (list != null) {
            this.datas.addAll(location, list);
            notifyDataSetChanged();
        }
    }

    /**
     * 添加数据
     *
     * @param item
     */
    public void addItem(T item) {
        if (item != null) {
            datas.add(item);
            notifyDataSetChanged();
        }
    }

    public void addItem(T item, int location) {
        if (item != null) {
            datas.add(location, item);
            notifyDataSetChanged();
        }
    }

    public void addItenAnim(T item, int location) {
        if (item != null) {
            datas.add(location, item);
            notifyItemInserted(location);
        }
    }

    @Override
    public int getItemCount() {
        if (datas != null) {
            return datas.size();
        }
        return 0;
    }

    public void deletePos(int pos) {
        if (datas != null && datas.size() > 0) {
            datas.remove(pos);
            notifyItemRemoved(pos);

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected View inflate(int resId) {
        return inflater.inflate(resId, null);
    }

    protected ViewDataBinding inflate(int resId, ViewGroup parent) {
        return DataBindingUtil.inflate(inflater, resId, parent, false);
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        //刷新界面重新绑定
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClicked(View itemView, int position);
    }




}
