package com.harunuyar.studentassistant.ÖsymHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.harunuyar.studentassistant.R;

import java.util.ArrayList;

/**
 * Created by Harun on 24.01.2017.
 */

public class ÖsymAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Exam> al;
    private LayoutInflater inflater = null;

    public ÖsymAdapter(Context context, ArrayList<Exam> al) {
        this.context = context;
        this.al = al;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return al.size();
    }

    @Override
    public Object getItem(int position) {
        return al.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item, null);

            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.cb = (CheckBox) convertView.findViewById(R.id.checkBox);
            holder.first = (TextView) convertView.findViewById(R.id.first);
            holder.last = (TextView) convertView.findViewById(R.id.last);
            holder.result = (TextView) convertView.findViewById(R.id.result);
            convertView.setTag(holder);

            holder.cb.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    Exam exam = (Exam) cb.getTag();
                    exam.setSelected(cb.isChecked());
                }
            });
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Exam exam = al.get(position);
        holder.name.setText(exam.getAd());
        holder.date.setText(exam.getSınavTarihi());
        holder.first.setText(exam.getBaşvuruTarihiFirst());
        holder.last.setText(exam.getBaşvuruTarihiLast());
        holder.result.setText(exam.getSonuçTarihi());
        holder.cb.setChecked(exam.isSelected());
        holder.cb.setTag(exam);

        return convertView;
    }

    private class ViewHolder {
        TextView name;
        TextView date;
        TextView first;
        TextView last;
        TextView result;
        CheckBox cb;
    }

    public ArrayList<Exam> getArrayList(){
        return al;
    }
}

