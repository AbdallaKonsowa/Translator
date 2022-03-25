package com.example.api.Database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.api.Database.DataClasses.DictionaryDB;
import com.example.api.Database.DataClasses.En_Ar_Table;
import com.example.api.R;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);

        List<En_Ar_Table> selectAll = DictionaryDB.getInstance(this).DictionaryDAO().SelectAll();

        AllMovieAdapter adapter = new AllMovieAdapter(this, selectAll);
        list.setAdapter(adapter);


    }
    public void text_to_Speech(View view) {
        Toast.makeText(this, view.getId()+"", Toast.LENGTH_SHORT).show();


    }
    class AllMovieAdapter extends ArrayAdapter<En_Ar_Table> {

        public AllMovieAdapter(@NonNull Context context, List<En_Ar_Table> allMovies) {
            super(context, 0, allMovies);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            // 1 inflation
            ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.custom_db_view, parent, false);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();

            }
            // 2 fill data
            holder.current_lan.setText(getItem(position).local_Lan);
            holder.current_text.setText(getItem(position).local_text);
            holder.Target_Lan.setText(getItem(position).target_Lan);
            holder.target_text.setText(getItem(position).target_text);

            return convertView;

        }

        class ViewHolder {
            TextView current_lan, current_text,Target_Lan,target_text;
            public ViewHolder(View convertView) {
                current_lan = convertView.findViewById(R.id.current_lan);
                current_text = convertView.findViewById(R.id.current_text);
                Target_Lan = convertView.findViewById(R.id.Target_Lan);
                target_text=convertView.findViewById(R.id.target_text);
            }
        }
    }


}