package com.bhirschberger.a20210909_bretthirschberger_nycschools.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bhirschberger.a20210909_bretthirschberger_nycschools.ListAdapter.SchoolListAdapter;
import com.bhirschberger.a20210909_bretthirschberger_nycschools.R;
import com.bhirschberger.a20210909_bretthirschberger_nycschools.models.SchoolData;
import com.bhirschberger.a20210909_bretthirschberger_nycschools.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SchoolListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // enables view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        adapter = new SchoolListAdapter(this, SchoolData.getInstance().getSchoolsList());
        binding.schoolsList.setLayoutManager(new LinearLayoutManager(this));
        binding.schoolsList.setNestedScrollingEnabled(false);
        binding.schoolsList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText); // filters items on list by input in search bar
                return false;
            }
        });
        return true;
    }
}