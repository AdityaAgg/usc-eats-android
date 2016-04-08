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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sparksc.usceats.FeastObjects.FoodItem;
import com.sparksc.usceats.FeastObjects.Meal;
import com.sparksc.usceats.FeastObjects.Menu;
import com.sparksc.usceats.FeastObjects.Section;
import com.sparksc.usceats.utils.DiningHallUtils;

import java.util.ArrayList;
import java.util.Calendar;
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
    String diningHallType;
    DiningHallUtils.MealTime selectedMealTime;
    USCDatabaseManager databaseManager;
    String selectedDay;
    ArrayList<Section> sections = new ArrayList<>();

    // endregion

    // region Fundamentals

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dining_hall, container, false);
        ButterKnife.bind(this, view);
        //databaseManager.refreshMenus();
        diningHallType = getArguments().getString("Dining Hall ID");
        selectedMealTime = DiningHallUtils.MealTime
                .values()[getArguments().getInt(DiningHallUtils.MEAL_TIME)];
        selectedDay = DiningHallUtils.getDay();
        databaseManager=new USCDatabaseManager(getContext());
        setupRecyclerView();

        return view;
    }

    // endregion

    // region Interactions

    public void onMealTimeChanged(DiningHallUtils.MealTime mealTime) {

        Log.d("Nikhil", "Meal Time: " + mealTime);

        FeastforAndroid feastforAndroid=FeastforAndroid.getInstance(getContext());
        Menu menuofDay=feastforAndroid.getMenuOnDayandRestaurant(Calendar.getInstance().getTime(), diningHallType);
        if (selectedMealTime != mealTime && sections != null && selectedDay!=null) {
            selectedMealTime = mealTime;
            sections.clear();
            getArguments().putInt(DiningHallUtils.MEAL_TIME, mealTime.ordinal());
            if(menuofDay!=null)
            switch (mealTime) {
                case BREAKFAST:

                    Meal meal= menuofDay.getMeal("Breakfast");
                    if(meal!=null)
                        if (meal.getSections()!=null)
                            for (Section section:meal.getSections()) {
                                sections.add(section);
                            }
                    break;
                case LUNCH:
                    meal=menuofDay.getMeal("Lunch");
                    if(meal!=null)
                        if(meal.getSections()!=null)
                            for (Section section:meal.getSections()) {
                                sections.add(section);
                            }
                    break;
                case DINNER:
                    meal=menuofDay.getMeal("Dinner");
                    if(meal!=null)
                        if(meal.getSections()!=null)
                            for (Section section:meal.getSections()) {
                                sections.add(section);
                            }
                    break;
            }
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }

    // endregion

    // region Helpers

    private void setupRecyclerView() {
        FeastforAndroid feastforAndroid=FeastforAndroid.getInstance(getContext());
        Menu menu = feastforAndroid.getMenuOnDayandRestaurant(Calendar.getInstance().getTime(), diningHallType);
        if(menu!=null) {
            if (selectedMealTime == DiningHallUtils.MealTime.BREAKFAST) {
                if(menu.getMeal("Breakfast")!=null)
                sections = menu.getMeal("Breakfast").getSections();
            } else if (selectedMealTime == DiningHallUtils.MealTime.LUNCH) {
                sections = menu.getMeal("Lunch").getSections();
            } else if (selectedMealTime == DiningHallUtils.MealTime.DINNER) {
                if(menu.getMeal("Dinner")!=null) {
                    sections = menu.getMeal("Dinner").getSections();
                }
            }
        } else {
            sections=new ArrayList<>();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerViewAdapter = new MenuRecyclerViewAdapter(this.getContext(), sections);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    // endregion

    // region Static Classes

    /**
     * Adapter for displaying results of searching for other users in a {@link RecyclerView}.
     */
    private static class MenuRecyclerViewAdapter
            extends RecyclerView.Adapter<MenuRecyclerViewAdapter.ViewHolder> {

        private List<Section> sections;
        private Context context;

        public MenuRecyclerViewAdapter(@NonNull Context context,
                                       @NonNull List<Section> items) {
            this.sections = items;
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
            Section section = sections.get(position);
            viewHolder.setData(section);
        }

        @Override
        public int getItemCount() {
            return sections.size();
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

            public void setData(Section section){
                diningHallStationHeaderView.setText(section.getName());
                FoodListAdapter foodListAdapter = new FoodListAdapter(context, R.id.food_list, section.getFoodItems());
                NonScrollListView foodListView= (NonScrollListView)view.findViewById(R.id.food_list);
                foodListView.setAdapter(foodListAdapter);
            }

            public class FoodListAdapter extends ArrayAdapter<FoodItem> {

                public FoodListAdapter(Context context, int resource, List<FoodItem> items) {
                    super(context, resource, items);
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    View v = convertView;

                    if (v == null) {
                        LayoutInflater vi;
                        vi = LayoutInflater.from(getContext());
                        v = vi.inflate(R.layout.item_row, null);
                    }
                    TextView foodItemName=(TextView)v.findViewById(R.id.food_item_text);
                    foodItemName.setText(getItem(position).getFoodName());
                    ImageView imageView=(ImageView)v.findViewById(R.id.dietary_identifier);
                    if(getItem(position).isV()){
                        imageView.setImageResource(R.drawable.iconvegan);
                    } else if(getItem(position).isVT()){
                        imageView.setImageResource(R.drawable.iconvegetarian);
                    } else {
                        imageView.setVisibility(View.GONE);
                    }


                    return v;
                }




            }
        }
    }

    // endregion

}
