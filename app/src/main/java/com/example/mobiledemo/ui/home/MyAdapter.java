package com.example.mobiledemo.ui.home;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
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
        if(mDatas.size() <= 0){
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
                mHolder = new TextViewHolder(mInflater.inflate(R.layout.todo_cardview, parent, false));
                break;
            case TEXT_VIEW:
                mHolder = new TextViewHolder(mInflater.inflate(R.layout.todo_cardview, parent, false));
                break;
        }
        return mHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(mDatas.size() <= 0){
            TextViewHolder textViewHolder = (TextViewHolder) holder;
            textViewHolder.endTime.setText("No todo thing!");
            return;
        }
        TextViewHolder textViewHolder = (TextViewHolder) holder;
        textViewHolder.startTime.setText(mDatas.get(position).getStart_time());
//        Log.d("TAG", mDatas.get(position).getStart_time());
        textViewHolder.endTime.setText(mDatas.get(position).getEnd_time());
        textViewHolder.title.setText(mDatas.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mDatas.size()>0 ? mDatas.size() : 1;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class TextViewHolder extends RecyclerView.ViewHolder{
        private TextView startTime, endTime, title;
        public TextViewHolder(View itemView) {
            super(itemView);
            startTime = (TextView) itemView.findViewById(R.id.todo_start_time);
            endTime = (TextView) itemView.findViewById(R.id.todo_end_time);
            title = (TextView) itemView.findViewById(R.id.todo_title);
        }
    }

    class NoTextViewHolder extends RecyclerView.ViewHolder{
        private TextView startTime, endTime, title;
        public NoTextViewHolder(View itemView) {
            super(itemView);
            startTime = (TextView) itemView.findViewById(R.id.todo_start_time);
            endTime = (TextView) itemView.findViewById(R.id.todo_end_time);
            title = (TextView) itemView.findViewById(R.id.todo_title);
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(View v, int position);
    }
}
