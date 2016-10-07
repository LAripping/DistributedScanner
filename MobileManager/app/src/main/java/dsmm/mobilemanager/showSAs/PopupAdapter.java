package dsmm.mobilemanager.showSAs;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import dsmm.mobilemanager.R;

/**
 * Created by apostolis on 28/1/2016.
 */

public class PopupAdapter extends ArrayAdapter<String> {
    SAListFragment fragment;
    MainActivity activity;



    public PopupAdapter(ArrayList<String> items, MainActivity activity,SAListFragment fragment) {
        super(activity, R.layout.list_item, android.R.id.text1, items);
        this.fragment=fragment;
        this.activity=activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        // Let ArrayAdapter inflate the layout and set the text
        View view = super.getView(position, convertView, container);

        // BEGIN_INCLUDE(button_popup)
        // Retrieve the popup button from the inflated view
        View popupButton = view.findViewById(R.id.button_popup);

        // Set the item as the button's tag so it can be retrieved later
        popupButton.setTag(getItem(position));

        // Set the fragment instance as the OnClickListener
        popupButton.setOnClickListener(fragment);
        return view;
    }

}


