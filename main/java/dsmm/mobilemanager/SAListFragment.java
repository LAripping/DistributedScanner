package dsmm.mobilemanager;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SAListFragment  extends ListFragment implements View.OnClickListener
{
    private PopupAdapter myPopupAdapter;


    @Override
    //link my custom list layout to the fragment instead of the standard listview
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sa_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //set an adapter for the list data
        //ArrayAdapter<String> myadapter = new ArrayAdapter<String>(getActivity() ,R.layout.list_item , android.R.id.text1 , Items.myText);

        myPopupAdapter = (PopupAdapter) getListAdapter();
        //setListAdapter(myPopupAdapter);




        //// TODO: careful when landscape changes then all items are refreshed even if previously removed

       // ArrayList<String> hasharray = getArguments().getStringArrayList("hashArray");
       // ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getActivity()  , android.R.layout.simple_list_item_activated_1, hasharray );
       // setListAdapter(new PopupAdapter(hasharray));
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
        final String item = (String) view.getTag();



        // Create a PopupMenu, giving it the clicked view for an anchor
        PopupMenu popup = new PopupMenu(getActivity(), view);

        // Inflate our menu resource into the PopupMenu's Menu
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        Log.w("popup",(myPopupAdapter==null?"isnull":"is not null"));

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

                        final TerminateSaAsyncTask tersa =  new TerminateSaAsyncTask(getActivity(),item);
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
                        //do magic

                    case R.id.menu_show_nmap:


                }
                return false;
            }
        });

        //show the PopupMenu
        popup.show();
    }



}

