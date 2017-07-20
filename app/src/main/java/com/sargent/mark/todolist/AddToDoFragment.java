/*
 *  Wilbert:
 *      # Added Spinner in order to allow setting the category of the ToDoItem.
 *      # Added interface so that this fragment could implement the observer/listener pattern...
 *          whichever class launches this fragment will register as a listener so that it will
 *          be notified of the options chosen by the user.
 */

package com.sargent.mark.todolist;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.sargent.mark.todolist.data.ToDoItem;
import com.sargent.mark.todolist.data.Util;

import java.util.Calendar;


public class AddToDoFragment extends DialogFragment implements AdapterView.OnItemSelectedListener{



    private EditText mDescriptionEditText;
    private DatePicker mDatePicker;
    private Button mAddButton;
    private Spinner mCategorySpinner;
    private final String TAG = "addtodofragment";
    private OnAddToDoListener mListener = null;



    public interface OnAddToDoListener {
        void addToDo(ToDoItem item);
    }

    public void setOnAddToDoListener(OnAddToDoListener listener){
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do_adder, container, false);
        mDescriptionEditText = (EditText) view.findViewById(R.id.toDo);
        mDatePicker = (DatePicker) view.findViewById(R.id.datePicker);
        mAddButton = (Button) view.findViewById(R.id.add);
        mCategorySpinner = (Spinner)view.findViewById(R.id.spinner_categories);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, Util.CATEGORIES);
        mCategorySpinner.setAdapter(adapter);
        mCategorySpinner.setOnItemSelectedListener(this);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        mDatePicker.updateDate(year, month, day);


        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = mDescriptionEditText.getText().toString();
                String date = Util.formatDate(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
                boolean isComplete = false;
                String category = (String)mCategorySpinner.getSelectedItem();

                ToDoItem item = new ToDoItem(description, date, isComplete, category);
                if(mListener != null){
                    mListener.addToDo(item);
                }
                AddToDoFragment.this.dismiss();
            }
        });

        return view;
    }

    ////////////////
    //
    //  Callbacks
    //
    ////////////////

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}



