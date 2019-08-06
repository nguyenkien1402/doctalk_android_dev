package com.mobile.docktalk.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.R;
import com.mobile.docktalk.models.Professional;

import java.util.ArrayList;
import java.util.List;

public class ProfessionalAdapter extends RecyclerView.Adapter<ProfessionalAdapter.MyViewHolder> {

    private Context context;
    private List<Professional> list;
    private List<Integer> selectedIds = new ArrayList<>();

    public ProfessionalAdapter(Context context,List<Professional> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.listview_professional_choice_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        int id = list.get(position).getId();

        if (selectedIds.contains(id)){
            //if item is selected then,set foreground color of FrameLayout.
            holder.rootView.setForeground(new ColorDrawable(ContextCompat.getColor(context,R.color.colorControlActivated)));
        }
        else {
            //else remove selected item color.
            holder.rootView.setForeground(new ColorDrawable(ContextCompat.getColor(context,android.R.color.transparent)));
        }
    }

    public List<Professional> getList() {
        return list;
    }

    @Override
    public int getItemCount() {
        return getList().size();
    }
    public Professional getItem(int position){
        return list.get(position);
    }

    public void setSelectedIds(List<Integer> selectedIds) {
        this.selectedIds = selectedIds;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        FrameLayout rootView;
        MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.professional_title);
            rootView = itemView.findViewById(R.id.root_view);
        }
    }

//    private Context context;
//    private LayoutInflater inflater;
//    private List<Professional> professionals;
//    private SparseBooleanArray mSelectedItemsIds;
//
//    public ProfessionalAdapter(Context context, int resourceId, List<Professional> professionals){
//        super(context, resourceId, professionals);
//        this.context = context;
//        this.professionals = professionals;
//        mSelectedItemsIds = new SparseBooleanArray();
//        inflater = LayoutInflater.from(context);
//    }
//
//    private class ViewHolder{
//        TextView title;
//    }
//
//    public View getView(int position, View view, ViewGroup parent){
//        final ViewHolder holder;
//        if(view == null){
//            holder = new ViewHolder();
//            view = inflater.inflate(R.layout.listview_professional_choice_item, null);
//            holder.title = (TextView) view.findViewById(R.id.professional_title);
//            view.setTag(holder);
//        }else{
//            holder = (ViewHolder) view.getTag();
//        }
//        holder.title.setText(professionals.get(position).getTitle());
//        return view;
//    }
//
//    public List<Professional> getProfessionals() {
//        return professionals;
//    }
//
//    public void toggleSelection(int position) {
//        selectView(position, !mSelectedItemsIds.get(position));
//    }
//    public void removeSelection() {
//        mSelectedItemsIds = new SparseBooleanArray();
//        notifyDataSetChanged();
//    }
//
//    public void selectView(int position, boolean value) {
//        if (value)
//            mSelectedItemsIds.put(position, value);
//        else
//            mSelectedItemsIds.delete(position);
//        notifyDataSetChanged();
//    }
//
//    public int getSelectedCount() {
//        return mSelectedItemsIds.size();
//    }
//
//    public SparseBooleanArray getSelectedIds() {
//        return mSelectedItemsIds;
//    }

}
