package com.harunuyar.studentassistant.ÖsymHelper;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.harunuyar.studentassistant.Constants;
import com.harunuyar.studentassistant.R;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Harun on 24.01.2017.
 */

public class ÖsymAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Exam> al;

    public ÖsymAdapter(Context context, ArrayList<Exam> al) {
        this.context = context;
        this.al = al;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item, null);

            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.cb = (CheckBox) convertView.findViewById(R.id.checkBox);
            holder.first = (TextView) convertView.findViewById(R.id.first);
            holder.last = (TextView) convertView.findViewById(R.id.last);
            holder.result = (TextView) convertView.findViewById(R.id.result);
            holder.delete = (ImageButton) convertView.findViewById(R.id.deleteButton);
            holder.change = (ImageButton) convertView.findViewById(R.id.changeButton);
            holder.position = position;
            convertView.setTag(holder);

            holder.cb.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    Exam exam = (Exam) cb.getTag();
                    exam.setSelected(cb.isChecked());
                    saveSelectedExams();
                    saveUserCreatedExams();
                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.delete_exam_dialog);

                    Button button2 = (Button) dialog.findViewById(R.id.button2);
                    Button button3 = (Button) dialog.findViewById(R.id.button3);
                    TextView title = (TextView) dialog.findViewById(R.id.alertTitle);
                    TextView message = (TextView) dialog.findViewById(R.id.textViewMessage);

                    message.setText("" + al.get(holder.position).getAd() + "\n\nBu sınavın silinmesini onaylıyor musunuz?");
                    button2.setText("EVET");
                    button3.setText("HAYIR");
                    title.setText("Sınavı Sil");
                    button2.setVisibility(View.VISIBLE);
                    button3.setVisibility(View.VISIBLE);
                    title.setVisibility(View.VISIBLE);

                    button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            al.remove(holder.position);
                            ÖsymAdapter.this.notifyDataSetChanged();
                            saveUserCreatedExams();

                            dialog.dismiss();
                        }
                    });

                    button3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });

            holder.change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.exam_dialog);
                    final Exam exam = al.get(holder.position);

                    Button button2 = (Button) dialog.findViewById(R.id.button2);
                    Button button3 = (Button) dialog.findViewById(R.id.button3);
                    TextView title = (TextView) dialog.findViewById(R.id.alertTitle);
                    final EditText ad = (EditText) dialog.findViewById(R.id.nameEditText);
                    final EditText first = (EditText) dialog.findViewById(R.id.FirstEditText);
                    final EditText last = (EditText) dialog.findViewById(R.id.LastEditText);
                    final EditText tarih = (EditText) dialog.findViewById(R.id.sınavEditText);
                    final EditText sonuç = (EditText) dialog.findViewById(R.id.sonuçEditText);

                    ad.setText(exam.getAd());
                    first.setText(exam.getBaşvuruTarihiFirst());
                    last.setText(exam.getBaşvuruTarihiLast());
                    tarih.setText(exam.getSınavTarihi());
                    sonuç.setText(exam.getSonuçTarihi());
                    button2.setText("KAYDET");
                    button3.setText("İPTAL");
                    title.setText("Sınavı Güncelle");
                    button2.setVisibility(View.VISIBLE);
                    button3.setVisibility(View.VISIBLE);
                    title.setVisibility(View.VISIBLE);

                    button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!ad.getText().toString().equals("")) {
                                exam.setAd(ad.getText().toString());
                                exam.setBaşvuruTarihiFirst(first.getText().toString());
                                exam.setBaşvuruTarihiLast(last.getText().toString());
                                exam.setSınavTarihi(tarih.getText().toString());
                                exam.setSonuçTarihi(sonuç.getText().toString());
                                ÖsymAdapter.this.notifyDataSetChanged();

                                saveUserCreatedExams();

                                dialog.dismiss();
                            }
                            else{
                                Toast.makeText(context, "Sınav adı boş bırakılamaz.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    button3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Exam exam = al.get(position);
        holder.position = position;
        holder.name.setText(exam.getAd());
        holder.date.setText(exam.getSınavTarihi());
        holder.first.setText(exam.getBaşvuruTarihiFirst());
        holder.last.setText(exam.getBaşvuruTarihiLast());
        holder.result.setText(exam.getSonuçTarihi());
        holder.cb.setChecked(exam.isSelected());
        holder.cb.setTag(exam);
        if (exam.isUserCreated()) {
            holder.delete.setVisibility(View.VISIBLE);
            holder.change.setVisibility(View.VISIBLE);
        }
        else{
            holder.delete.setVisibility(View.GONE);
            holder.change.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView name;
        TextView date;
        TextView first;
        TextView last;
        TextView result;
        CheckBox cb;
        ImageButton delete;
        ImageButton change;
        int position;
    }

    public ArrayList<Exam> getArrayList(){
        return al;
    }

    public void saveUserCreatedExams(){
        try {
            Constants.saveUserCreatedExams(context, al);
        } catch (IOException e) { }
    }

    public void saveSelectedExams(){
        try {
            Constants.saveSelectedExams(context, al);
        } catch (IOException e) { }
    }
}

