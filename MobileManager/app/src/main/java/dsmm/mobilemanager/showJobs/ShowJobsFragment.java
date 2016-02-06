package dsmm.mobilemanager.showJobs;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import dsmm.mobilemanager.R;

/**
 * Created by apostolis on 3/2/2016.
 */
public class ShowJobsFragment extends ListFragment implements View.OnClickListener {
    private JobsAdapter jobsAdapter;

    @Override
    //link my custom list layout to the fragment instead of the standard listview
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.show_jobs_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        jobsAdapter = (JobsAdapter) getListAdapter();

    }

    @Override
    public void onClick(final View view) {

        view.post(new Runnable() {
            @Override
            public void run() {
                showPopupMenu(view);
            }
        });
    }

    private void showPopupMenu(View view) {

        // Retrieve the clicked item from view's tag
        final String item = ((String) view.getTag());

        // Create a PopupMenu, giving it the clicked view for an anchor
        PopupMenu popup = new PopupMenu(getActivity(), view);

        // Inflate our menu resource into the PopupMenu's Menu
        popup.getMenuInflater().inflate(R.menu.popup2_menu, popup.getMenu());

        // Set a listener so we are notified if a menu item is clicked
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String[] temp = item.split(",");
                int id = Integer.parseInt(temp[0]);
                switch (menuItem.getItemId()) {
                    case R.id.menu_delete:
                        if (temp[2].equals("false")) {
                            Toast toast = Toast.makeText(getActivity(), "You can't delete a non-Periodic Job", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            DeleteJobAsyncTask deleteJobAsyncTask = new DeleteJobAsyncTask(id, getActivity());
                            deleteJobAsyncTask.execute();
                            jobsAdapter.remove(item);
                        }

                        return true;
                    case R.id.menu_run_again:
                        RerunAsyncTask rerunAsyncTask = new RerunAsyncTask(item, getActivity());
                        rerunAsyncTask.execute();

                        return true;
                }
                return false;
            }
        });

        //show the PopupMenu
        popup.show();
    }
}
