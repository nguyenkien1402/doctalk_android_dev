package com.mobile.cometchat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobile.R;
import com.mobile.cometchat.CustomView.CircleImageView;
import com.mobile.cometchat.Pojo.Option;

import java.util.List;

public class AutoCompleteAdapter extends ArrayAdapter {

    private int resource;
    private List<Option> optionList;
    private LayoutInflater layoutInflater;

    public AutoCompleteAdapter(Context context, int resource, List<Option> optionList) {
        super(context, resource, optionList);

        this.optionList = optionList;
        this.resource = resource;

        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public Object getItem(int position) {
        return optionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return optionList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rootView = layoutInflater.inflate(resource, parent, false);

        Option option = optionList.get(position);
        TextView tvName = rootView.findViewById(R.id.tvName);
        TextView tvId = rootView.findViewById(R.id.tvId);
        CircleImageView circleImageView = rootView.findViewById(R.id.ivAvatar);

        tvId.setText(option.getId());
        tvName.setText(option.getName());
        circleImageView.setImageDrawable(option.getDrawable());

        return rootView;
    }


}
