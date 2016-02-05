package dsmm.mobilemanager.showJobs;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import dsmm.mobilemanager.PopupAdapter;
import dsmm.mobilemanager.R;
import dsmm.mobilemanager.SAListFragment;
import dsmm.mobilemanager.SAsAsyncTask;
import dsmm.mobilemanager.users.LoginActivity;

public class JobsActivity extends AppCompatActivity {
    private FragmentManager fragman;
    private ShowJobsFragment fragment;
    private ArrayList<String> jobsArray;
    private JobsAdapter jobsAdapter;
    private String hash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);

        setTitle("Active Jobs");

        Intent intent = getIntent();
        hash = intent.getExtras().getString("hash");

        fragman = this.getFragmentManager();
        fragment = (ShowJobsFragment) fragman.findFragmentById(R.id.show_jobs_fragment);
        jobsArray = new ArrayList<>();
        jobsAdapter = new JobsAdapter(jobsArray,this,fragment);
        fragment.setListAdapter(jobsAdapter);

        JobsAsyncTask jobsAsyncTask = new JobsAsyncTask(hash,JobsActivity.this);
        jobsAsyncTask.execute();

    }

    public void JobsListUpdate(ArrayList<String> new_hasharray)
    {
        jobsAdapter.clear();
        jobsAdapter.addAll(new_hasharray);
        jobsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getBaseContext(),LoginActivity.class);
        startActivity(i);
        finish();
    }
}
