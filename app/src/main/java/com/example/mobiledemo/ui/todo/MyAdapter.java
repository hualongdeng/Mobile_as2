package com.example.mobiledemo.ui.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mobiledemo.R;
import com.example.mobiledemo.ui.notifications.TodoEntity;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TodoEntity> mDatas;
    private LayoutInflater mInflater;
    private OnItemClickListener onItemClickListener;
    private final int NO_DATA = 0, TEXT_VIEW = 2;

    public MyAdapter(List<TodoEntity> mDatas) {
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
                mHolder = new TextViewHolder(mInflater.inflate(R.layout.todo_cardview_edit, parent, false));
                break;
            case TEXT_VIEW:
                mHolder = new TextViewHolder(mInflater.inflate(R.layout.todo_cardview_edit, parent, false));
                break;
        }
        return mHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(mDatas.size() <= 0){ //无数据的情况
            return;
        }
        TextViewHolder textViewHolder = (TextViewHolder) holder;
        textViewHolder.startTime.setText(mDatas.get(position).getStart_time());
        textViewHolder.endTime.setText(mDatas.get(position).getEnd_time());
        textViewHolder.title.setText(mDatas.get(position).getTitle());
        textViewHolder.startTime.setOnClickListener(new View.OnClickListener() {
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

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 文字item的holder
     */
    class TextViewHolder extends RecyclerView.ViewHolder{

        private TextView startTime, endTime, title;
        public TextViewHolder(View itemView) {
            super(itemView);
            startTime = (TextView) itemView.findViewById(R.id.todo_start_time);
            endTime = (TextView) itemView.findViewById(R.id.todo_end_time);
            title = (TextView) itemView.findViewById(R.id.todo_title);
        }
    }

    /**
     * 适配器的点击事件接口
     */
    public interface OnItemClickListener{
        void OnItemClick(View v, int position);
    }
}
