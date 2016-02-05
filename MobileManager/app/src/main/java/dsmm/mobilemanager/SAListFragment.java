package dsmm.mobilemanager;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.DialogFragment;
import android.support.v7.widget.PopupMenu;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import dsmm.mobilemanager.insertJob.AssignJobDialogFragment;
import dsmm.mobilemanager.showJobs.JobsActivity;
import dsmm.mobilemanager.showResults.SaResultsAsyncTask;
import dsmm.mobilemanager.terminateSA.TerminateSaAsyncTask;

public class SAListFragment  extends ListFragment implements View.OnClickListener
{
    private PopupAdapter myPopupAdapter;
    public static final String KEY_SAHASH = "hash_key";


    @Override
    //link my custom list layout to the fragment instead of the standard listview
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sa_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myPopupAdapter = (PopupAdapter) getListAdapter();

    }



    @Override
    public void onListItemClick(ListView listView, View v, int position, long id) {
        String item = (String) listView.getItemAtPosition(position);

        // Show a toast if the user clicks on an item
        Toast.makeText(getActivity(), "Item Clicked: " + item, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(final View view) {
        // We need to post a Runnable to show the popup to make sure that the PopupMenu is
        // correctly positioned. The reason being that the view may change position before the
        // PopupMenu is shown.
        view.post(new Runnable() {
            @Override
            public void run() {
                showPopupMenu(view);
            }
        });
    }


    //Handles the popup menu creation
    private void showPopupMenu(View view) {

        // Retrieve the clicked item from view's tag
        final String item = ((String) view.getTag());
        final String asynctask_item;
        if(item.length() == 65){
            asynctask_item = item.replace(" ","");
        }
        else{
            asynctask_item = item;
        }
        // Create a PopupMenu, giving it the clicked view for an anchor
        PopupMenu popup = new PopupMenu(getActivity(), view);

        // Inflate our menu resource into the PopupMenu's Menu
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());


        // Set a listener so we are notified if a menu item is clicked
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_terminate:
                        // Remove the item from the adapter
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Terminate SA");
                        builder.setMessage("Are you sure ?");

                        final TerminateSaAsyncTask tersa =  new TerminateSaAsyncTask(getActivity(),asynctask_item);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                tersa.execute();
                                myPopupAdapter.remove(item);
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        return true;

                    case R.id.menu_add_nmap:
                        //Create a DialogFragment to get new Job info
                        DialogFragment newFragment = new AssignJobDialogFragment();

                        //Pass the selected SA's hash to the DialogFragment
                        Bundle fragArgs = new Bundle();
                        fragArgs.putString(KEY_SAHASH, asynctask_item);

                        newFragment.setArguments(fragArgs);

                        //Show the DialogFragment to the user
                        newFragment.show(getFragmentManager(), "assignjob");

                        return true;
                    case R.id.menu_show_nmap:
                        Intent i=new Intent(getActivity(),JobsActivity.class);
                        i.putExtra("hash",asynctask_item);
                        startActivity(i);

                        return true;
                    case R.id.menu_show_results:

                        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setTitle("SA Results");
                        dialog.setMessage("How many last results do you want to see?");
                        final EditText input=new EditText(getActivity());
                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        input.setRawInputType(Configuration.KEYBOARD_12KEY);
                        dialog.setView(input);
                        final SaResultsAsyncTask rs = new SaResultsAsyncTask(dialog,getActivity(),input,asynctask_item);
                        rs.execute();

                        return true;
                }
                return false;
            }
        });

        //show the PopupMenu
        popup.show();
    }


}

