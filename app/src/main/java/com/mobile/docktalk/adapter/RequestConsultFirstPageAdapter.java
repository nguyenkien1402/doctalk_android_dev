package com.mobile.docktalk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.docktalk.R;

public class RequestConsultFirstPageAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private String[] title;
    private int[] images;
    public RequestConsultFirstPageAdapter(Context context, String[] title, int[] images){
        this.context = context;
        this.title = title;
        this.images = images;
    }
    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public Object getItem(int position) {
        return title[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_request_consult_first_page, null, false);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.rq1_title);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.rq1_image);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.textView.setText(title[position]);
        viewHolder.imageView.setImageResource(images[position]);
        return convertView;
    }

    private static class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
