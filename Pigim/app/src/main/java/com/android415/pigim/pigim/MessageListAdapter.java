package com.android415.pigim.pigim;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder>
{
    private final LinkedList<String> mMessageList;
    private LayoutInflater mInflater;

    public MessageListAdapter(Context context, LinkedList<String> messageList)
    {
        mInflater = LayoutInflater.from(context);
        this.mMessageList = messageList;
    }

    @NonNull
    @Override
    public MessageListAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View mItem = mInflater.inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(mItem, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageListAdapter.MessageViewHolder holder, int position)
    {
        String mCurrent = mMessageList.get(position);
        holder.messageItemView.setText(mCurrent + "\n");
    }

    @Override
    public int getItemCount()
    {
        return mMessageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public final TextView messageItemView;
        final MessageListAdapter mAdapter;

        public MessageViewHolder(View itemView, MessageListAdapter adapter)
        {
            super(itemView);
            messageItemView = itemView.findViewById(R.id.message);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            //TODO: select and give options? including delete?
        }
    }
}
