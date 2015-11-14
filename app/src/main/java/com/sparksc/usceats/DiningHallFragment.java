package com.sparksc.usceats;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sparksc.usceats.models.DiningHallStation;
import com.sparksc.usceats.utils.DiningHallUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A generic fragment for displaying menu information about a dining hall.
 */
public class DiningHallFragment extends Fragment {

    // region Variables

    public static final String TAG = "DiningHallFragment";

    @Bind(R.id.menu) RecyclerView recyclerView;

    MenuRecyclerViewAdapter recyclerViewAdapter;
    DiningHallUtils.DiningHallType diningHallType;
    DiningHallUtils.MealTime selectedMealTime;
    ArrayList<DiningHallStation> diningHallStations = new ArrayList<>();

    // endregion

    // region Fundamentals

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dining_hall, container, false);
        ButterKnife.bind(this, view);

        diningHallType = DiningHallUtils.DiningHallType
                .values()[getArguments().getInt(DiningHallUtils.DINING_HALL_TYPE)];
        selectedMealTime = DiningHallUtils.MealTime
                .values()[getArguments().getInt(DiningHallUtils.MEAL_TIME)];

        setupRecyclerView();

        return view;
    }

    // endregion

    // region Interactions

    public void onMealTimeChanged(DiningHallUtils.MealTime mealTime) {
        // TODO don't use fake data
        Log.d("Nikhil", "Meal Time: " + mealTime);
        if (selectedMealTime != mealTime && diningHallStations != null) {
            selectedMealTime = mealTime;
            diningHallStations.clear();
            getArguments().putInt(DiningHallUtils.MEAL_TIME, mealTime.ordinal());
            switch (mealTime) {
                case BREAKFAST:
                    for (int i = 0; i < 10; i++) {
                        ArrayList<String> testList = new ArrayList<>();
                        testList.add("Eggs");
                        testList.add("Bacon");
                        testList.add("Pancakes");
                        DiningHallStation d = new DiningHallStation(testList, "Breakfast");
                        diningHallStations.add(d);
                    }
                    break;
                case BRUNCH:
                    for (int i = 0; i < 10; i++) {
                        ArrayList<String> testList = new ArrayList<>();
                        testList.add("Watermelon");
                        testList.add("Grapes");
                        testList.add("Apples");
                        DiningHallStation d = new DiningHallStation(testList, "Fruit Bar");
                        diningHallStations.add(d);
                    }
                    break;
                case LUNCH:
                    for (int i = 0; i < 10; i++) {
                        ArrayList<String> testList = new ArrayList<>();
                        testList.add("Tomatoes");
                        testList.add("Cucumbers");
                        testList.add("Spinach");
                        testList.add("Carrots");
                        DiningHallStation d = new DiningHallStation(testList, "Salad");
                        diningHallStations.add(d);
                    }
                    break;
                case DINNER:
                    for (int i = 0; i < 10; i++) {
                        ArrayList<String> testList = new ArrayList<>();
                        testList.add("Burgers");
                        testList.add("Hot Dogs");
                        DiningHallStation d = new DiningHallStation(testList, "Grill");
                        diningHallStations.add(d);
                    }
                    break;
            }
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }

    // endregion

    // region Helpers

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerViewAdapter = new MenuRecyclerViewAdapter(this.getContext(), diningHallStations);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    // endregion

    // region Static Classes

    /**
     * Adapter for displaying results of searching for other users in a {@link RecyclerView}.
     */
    private static class MenuRecyclerViewAdapter
            extends RecyclerView.Adapter<MenuRecyclerViewAdapter.ViewHolder> {

        private List<DiningHallStation> values;
        private Context context;

        public MenuRecyclerViewAdapter(@NonNull Context context,
                                       @NonNull List<DiningHallStation> items) {
            this.values = items;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.menu_item, parent, false);
            return new ViewHolder(context, view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            DiningHallStation diningHallStation = values.get(position);
            viewHolder.diningHallStationHeaderView.setText(diningHallStation.getName());
            viewHolder.clearTextViews();
            ArrayList<String> foodItems = diningHallStation.getFoodItems();
            for (String foodItem : foodItems) {
                viewHolder.addTextView(foodItem);
            }
        }

        @Override
        public int getItemCount() {
            return values.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public Context context;
            public ArrayList<TextView> menuItems;
            public final View view;
            public final TextView diningHallStationHeaderView;
            public final LinearLayout cardLayout;

            public ViewHolder(Context context, View view) {
                super(view);
                this.context = context;
                this.view = view;
                diningHallStationHeaderView = (TextView) view.findViewById(R.id.dining_hall_station_header_view);
                cardLayout = (LinearLayout) view.findViewById(R.id.card_layout);
                menuItems = new ArrayList<>();
            }

            public void addTextView(String text) {
                if (context != null) {
                    // create a new textview
                    final TextView rowTextView = new TextView(context);

                    // set some properties of rowTextView or something
                    rowTextView.setText(text);

                    // add the textview to the linearlayout
                    cardLayout.addView(rowTextView);

                    // save reference to the textview
                    menuItems.add(rowTextView);
                }
            }

            public void clearTextViews() {
                for (TextView textView : menuItems) {
                    cardLayout.removeView(textView);
                }
                menuItems.clear();
            }
        }
    }

    // endregion

}
