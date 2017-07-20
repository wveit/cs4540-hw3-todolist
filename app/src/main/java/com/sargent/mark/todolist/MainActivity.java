/*
 *  Wilbert:
 *      # Removed all database code and placed it in 'MyDatabase' class. This Activity only
 *          interacts with the database only through a 'MyDatabase' object
 *      # Before launching a fragment, this Activity registers with the fragment as a listener. That
 *          way, when a result is obtained in the fragment, this Activity will be notified with
 *          a callback method.
 *      # This class is also registered as listener with ToDoListAdapter class so that it can be
 *          notified when a ToDoListItem is clicked on or when a 'completed' CheckBox is toggled.
 *      # A Menu and MenuItem were added to this Activity so that the "filter" can be turned on and
 *          off.
 *      # A Spinner was added to this Activity so that the list of ToDoItems can be filtered by
 *          category. This spinner and its associated filter ability can be turned on and off
 *          in the menu.
 */

package com.sargent.mark.todolist;


import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.sargent.mark.todolist.data.MyDatabase;
import com.sargent.mark.todolist.data.ToDoItem;
import com.sargent.mark.todolist.data.Util;




public class MainActivity extends AppCompatActivity
        implements AddToDoFragment.OnAddToDoListener,
                    UpdateToDoFragment.OnUpdateToDoListener,
                    ToDoListAdapter.ItemClickListener,
                    ToDoListAdapter.ItemCheckCompleteListener,
                    AdapterView.OnItemSelectedListener {



    private RecyclerView rv;
    private FloatingActionButton button;
    private MyDatabase db;
    private ToDoListAdapter adapter;
    private Spinner mFilterSpinner;
    private LinearLayout mFilterLayout;
    private boolean mFilterIsActive;
    private MenuItem mFilterMenuItem;
    private final String TAG = "mainactivity";

    ////////////////////////////////////////
    //
    //  Activity Methods
    //
    /////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (FloatingActionButton) findViewById(R.id.addToDo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                AddToDoFragment frag = new AddToDoFragment();
                frag.setOnAddToDoListener(MainActivity.this);
                frag.show(fm, "addtodofragment");
            }
        });

        rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));

        mFilterSpinner = (Spinner)findViewById(R.id.spinner_filter);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, Util.CATEGORIES);
        mFilterSpinner.setAdapter(adapter);
        mFilterSpinner.setOnItemSelectedListener(this);

        mFilterLayout = (LinearLayout)findViewById(R.id.layout_filter);
        mFilterIsActive = false;


        db = new MyDatabase(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        db.open();

        if(mFilterIsActive){
            filterOn();
        }
        else{
            filterOff();
        }

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int id =  (int)viewHolder.itemView.getTag();
                Log.d(TAG, "passing id: " + id);
                db.deleteItem(id);
                db.selectAll();
                resetAdapter();
            }
        }).attachToRecyclerView(rv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        mFilterMenuItem = (MenuItem)menu.findItem(R.id.action_toggle_filter);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_toggle_filter) {

            /* Add code to toggle visibility and effectivness of filter */
            if(mFilterIsActive){
                filterOff();
            }
            else{
                filterOn();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetAdapter(){
        adapter = new ToDoListAdapter(db, this, this);
        rv.setAdapter(adapter);
        Log.d("DB", Util.printDB(db));
    }

    private void filterOff(){
        mFilterIsActive = false;
        mFilterLayout.setVisibility(View.GONE);
        db.selectAll();
        resetAdapter();
        if(mFilterMenuItem != null)
            mFilterMenuItem.setTitle("Turn On Filter");
        else
            Log.d(TAG, "Didn't work");
    }

    private void filterOn(){
        mFilterIsActive = true;
        mFilterLayout.setVisibility(View.VISIBLE);
        db.selectAllOfCategory((String)mFilterSpinner.getSelectedItem());
        resetAdapter();
        if(mFilterMenuItem != null)
            mFilterMenuItem.setTitle("Turn Off Filter");
        else
            Log.d(TAG, "Didn't work");
    }

    ////////////////////////////////////////
    //
    //  Listener Callbacks
    //
    /////////////////////////////////////////

    @Override
    public void addToDo(ToDoItem item){
        db.addItem(item);
        db.selectAll();
        resetAdapter();
    }

    @Override
    public void onUpdateToDo(int id, ToDoItem item) {
        db.updateItem(id, item);
        db.selectAll();
        resetAdapter();
    }

    @Override
    public void onItemClick(int id, ToDoItem item) {
        FragmentManager fm = getSupportFragmentManager();
        UpdateToDoFragment frag = new UpdateToDoFragment();
        frag.setOnUpdateToDoListener(this);
        frag.setValues(id, item);
        frag.show(fm, "updatetodofragment");
    }

    @Override
    public void onItemCheckComplete(int id, ToDoItem item) {
        db.updateItem(id, item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        db.selectAllOfCategory((String)mFilterSpinner.getSelectedItem());
        resetAdapter();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
