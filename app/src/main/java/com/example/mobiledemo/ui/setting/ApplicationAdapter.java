package com.example.mobiledemo.ui.setting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mobiledemo.R;
import com.example.mobiledemo.ui.notifications.TodoEntity;
import com.example.mobiledemo.ui.todo.MyAdapter;

import java.util.List;

public class ApplicationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> mDatas;
    private LayoutInflater mInflater;
    private ApplicationAdapter.OnItemClickListener onItemClickListener;
    private final int NO_DATA = 0, TEXT_VIEW = 2;

    public ApplicationAdapter(List<String> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public int getItemViewType(int position) {
        if(mDatas.size() <= 0){ //无数据情况处理
            return NO_DATA;
        }
        return TEXT_VIEW;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder mHolder = null;
        switch (viewType){
            case NO_DATA:
                mHolder = new ApplicationAdapter.TextViewHolder(mInflater.inflate(R.layout.application_list_cardview, parent, false));
                break;
            case TEXT_VIEW:
                mHolder = new ApplicationAdapter.TextViewHolder(mInflater.inflate(R.layout.application_list_cardview, parent, false));
                break;
        }
        return mHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(mDatas.size() <= 0){ //无数据的情况
            return;
        }
        ApplicationAdapter.TextViewHolder textViewHolder = (ApplicationAdapter.TextViewHolder) holder;
        textViewHolder.application_name.setText(mDatas.get(position));
        textViewHolder.application_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.OnItemClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size()>0 ? mDatas.size() : 1; //这里在数据为空的情况下返回1，为了显示无数据的布局
    }

    public void setOnItemClickListener(ApplicationAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 文字item的holder
     */
    class TextViewHolder extends RecyclerView.ViewHolder{

        private TextView application_name;
        private Switch application_switch;
        public TextViewHolder(View itemView) {
            super(itemView);
            application_name = (TextView) itemView.findViewById(R.id.application_list_item);
            application_switch = itemView.findViewById(R.id.application_list_switch);
        }
    }

    /**
     * 适配器的点击事件接口
     */
    public interface OnItemClickListener{
        void OnItemClick(View v, int position);
    }
}
