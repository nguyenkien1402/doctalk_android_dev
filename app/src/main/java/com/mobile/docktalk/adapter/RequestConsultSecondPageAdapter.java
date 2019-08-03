package com.mobile.docktalk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.docktalk.R;
import com.mobile.docktalk.models.Professional;

import java.util.List;

public class RequestConsultSecondPageAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Professional> title;

    public RequestConsultSecondPageAdapter(Context context, List<Professional> title){
        this.context = context;
        this.title = title;
    }
    @Override
    public int getCount() {
        return title.size();
    }

    @Override
    public Object getItem(int position) {
        return title.get(position);
    }

    @Override
    public long getItemId(int position) {
        return title.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_request_consult_second_page, null, false);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.rq2_title);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.rq2_image);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.textView.setText(title.get(position).getTitle());
        viewHolder.imageView.setImageResource(title.get(position).getIcon());
        return convertView;
    }

    private static class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
