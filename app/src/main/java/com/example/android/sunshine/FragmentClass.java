package com.example.android.sunshine;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by vikashkumarbijarnia on 17/07/16.
 */
public class FragmentClass extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_view, container, false);

        String[] arr=new String[3];
        arr[0]="today cool 45/89";
        arr[1]="today cool 45/33";
        arr[2]="today cool 45/56";

        ArrayList<String> weekForcast=new ArrayList<>(Arrays.asList(arr));
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_view,R.id.list_item_forecast_textview,weekForcast);
        ListView list=(ListView)rootView.findViewById(R.id.listView_forecast);
        list.setAdapter(arrayAdapter);
        return rootView;
    }
}
