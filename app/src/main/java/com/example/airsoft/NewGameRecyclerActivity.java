package com.example.airsoft;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewGameRecyclerActivity extends AppCompatActivity {
    TextView currentDateTime;
    Calendar dateAndTime = Calendar.getInstance();
    String[] teamsstring = {"t1", "t2", "t3", "Волгоград", "Саратов", "Воронеж"};
    String[] mapsstring = {"map1", "map2", "Вологда", "Волгоград", "Саратов", "Воронеж"};
    private List<MemberTeamClass> member_team_List = new ArrayList<>();
    private RecyclerView recyclerView;
    private MemberTeamAdapter mtAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game_recycler);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_new_game);

        mtAdapter = new MemberTeamAdapter(member_team_List);
        RecyclerView.LayoutManager mtLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mtLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mtAdapter);

        add_to_table();


        addListenerOnButton();

        currentDateTime = (TextView) findViewById(R.id.currentDateTime);
        setInitialDateTime();

        Spinner spinnerTeams = (Spinner) findViewById(R.id.teams);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapterTeams = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teamsstring);
        // Определяем разметку для использования при выборе элемента
        adapterTeams.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinnerTeams.setAdapter(adapterTeams);

        Spinner spinnerMaps = (Spinner) findViewById(R.id.maps);
        ArrayAdapter<String> adapterMaps = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mapsstring);
        adapterMaps.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaps.setAdapter(adapterMaps);
    }

    /*Spinner spinner = findViewById(R.id.spinner);
    String selected = spinner.getSelectedItem().toString();
Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT).show();*/

    public void add_to_db() {
        final String gameDateTime = ((TextView) findViewById(R.id.currentDateTime)).getText().toString();
        final String winnerTeam = ((Spinner) findViewById(R.id.teams)).getSelectedItem().toString();
        final String gameMap = ((Spinner) findViewById(R.id.maps)).getSelectedItem().toString();
        //final String personArsenal = ((EditText)findViewById(R.id.arsenal)).getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference db_gameDateTime;
        DatabaseReference db_winnerTeam;
        DatabaseReference db_gameMap;
        //DatabaseReference db_personArsenal;

        String key = database.getReference("quiz").push().getKey();

        db_gameDateTime = database.getReference("Games/game_id/" + key + "/DateTime");
        db_winnerTeam = database.getReference("Games/game_id/" + key + "/WinnerTeam");
        db_gameMap = database.getReference("Games/game_id/" + key + "/Map");
        //db_personArsenal = database.getReference("Games/games_id/"+key+"/DateTime");
        db_gameDateTime.setValue(gameDateTime);
        db_winnerTeam.setValue(winnerTeam);
        db_gameMap.setValue(gameMap);
        //db_personArsenal.setValue(personArsenal);
    }

    public void addListenerOnButton() {
        Button button_cancel = findViewById(R.id.game_cancel);
        Button button_save = findViewById(R.id.game_save);
        button_cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                }

        );
        button_save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        add_to_db();
                    }
                }

        );
    }

    // отображаем диалоговое окно для выбора даты
    public void setDate(View v) {
        new DatePickerDialog(NewGameRecyclerActivity.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // отображаем диалоговое окно для выбора времени
    public void setTime(View v) {
        new TimePickerDialog(NewGameRecyclerActivity.this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }

    // установка начальных даты и времени
    private void setInitialDateTime() {

        currentDateTime.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME));
    }

    // установка обработчика выбора времени
    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    public void add_to_table() {
        final List<String> membersIdList = new ArrayList<>();
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Members");
        databaseRef.child("members_id").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null) return;
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    membersIdList.add(postSnapShot.getKey());
                }
                SetData(databaseRef, membersIdList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error
                Log.d("Error", "databaseError");
            }
        });
    }

    private void SetData(DatabaseReference databaseRef, List<String> userIdList) {
        for (final String id : userIdList) {
            databaseRef.child("members_id").child(id).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot2) {

                    if (dataSnapshot2 == null) return;
                    String fio = (String) dataSnapshot2.child("FIO").getValue();

                    addRow(fio);
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Error
                    Log.d("Error", "databaseError");
                }
            });
        }
    }

    private void addRow(String fio_from_base) {
        MemberTeamClass member = new MemberTeamClass(fio_from_base);
        member_team_List.add(member);


        mtAdapter.notifyDataSetChanged();
    }
}