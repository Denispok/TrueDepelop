package com.example.truedevelop;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;

import com.example.truedevelop.adapter.TabsFragmentAdapter;
import com.example.truedevelop.dto.RemindDTO;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static int LAYOUT = R.layout.activity_main;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;

    private TabsFragmentAdapter adapter;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        initToolbar();
        initNavView();
        initDB();
        initTabs();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.textColor));
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return false;
            }
        });

        toolbar.inflateMenu(R.menu.menu);
    }

    private void initNavView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.view_navigation_open, R.string.view_navigation_close);
        drawerLayout.setDrawerListener(toogle);
        toogle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                switch (item.getItemId()){
                    case R.id.actionNotificationItem:
                        showNotificationTab();
                }
                return true;
            }
        });
    }

    private void initDB() {
        dbHelper = new DBHelper(this);
    }

    private void initTabs() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new TabsFragmentAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        new UpdateRemindTask().execute();
        //new RemindMeTask().execute();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void showNotificationTab() {
        viewPager.setCurrentItem(Constants.TAB_ONE_N);
    }

    private class RemindMeTask extends AsyncTask<Void, Void, List<RemindDTO>> {
        @Override
        protected List<RemindDTO> doInBackground(Void... params) {
            SQLiteDatabase database = dbHelper.getReadableDatabase();
            Cursor cursor = database.query(DBHelper.TABLE_NEWS, null, null, null, null, null, null);

            if (cursor.moveToFirst()){
                List<RemindDTO> reminders = new ArrayList<>();

                int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
                int dateIndex = cursor.getColumnIndex(DBHelper.KEY_DATE);

                do {
                    RemindDTO remindDTO = new RemindDTO();
                    remindDTO.setId(cursor.getLong(idIndex));
                    remindDTO.setTitle(cursor.getString(titleIndex));
                    Date d = new Date(cursor.getString(dateIndex));
                    remindDTO.setRemindDate(d);
                    reminders.add(remindDTO);
                } while (cursor.moveToNext());

                cursor.close();
                return reminders;
            }
            else {
                cursor.close();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<RemindDTO> reminders) {
            if(reminders != null) adapter.setData(reminders);
        }
    }

    private class UpdateRemindTask extends AsyncTask<Void, Void, List<RemindDTO>> {
        @Override
        protected List<RemindDTO> doInBackground(Void... params) {
            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            try{
                RemindDTO[] reminders = template.getForObject(Constants.URL.GET_REMINDERS, RemindDTO[].class);
                List<RemindDTO> remindersList = Arrays.asList(reminders);
                if (remindersList != null) return remindersList;
                else return null;
            } catch (Exception e) {return null;}
        }

        @Override
        protected void onPostExecute(List<RemindDTO> remindDTO) {
            if(remindDTO != null) {
                dbHelper.addRemindDTOList(remindDTO, dbHelper);
            }
            new RemindMeTask().execute();
        }
    }

    public void updateDB(View view){
        new UpdateRemindTask().execute();
    }
}