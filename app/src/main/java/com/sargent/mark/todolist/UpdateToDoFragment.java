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




public class UpdateToDoFragment extends DialogFragment  implements AdapterView.OnItemSelectedListener{

    private EditText mDescriptionEditText;
    private DatePicker mDatePicker;
    private Button mUpdateButton;
    private final String TAG = "updatetodofragment";
    private int mIdToBeUpdated;
    private ToDoItem mToDoItem = null;
    private OnUpdateToDoListener mListener = null;
    private Spinner mCategorySpinner;


    public void setValues(int id, ToDoItem item){
        mToDoItem = item;
        mIdToBeUpdated = id;
    }



    public interface OnUpdateToDoListener {
        void onUpdateToDo(int id, ToDoItem item);
    }

    public void setOnUpdateToDoListener(OnUpdateToDoListener listener){
        mListener = listener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do_adder, container, false);
        mDescriptionEditText = (EditText) view.findViewById(R.id.toDo);
        mDatePicker = (DatePicker) view.findViewById(R.id.datePicker);
        mUpdateButton = (Button) view.findViewById(R.id.add);
        mCategorySpinner = (Spinner) view.findViewById(R.id.spinner_categories);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, Util.CATEGORIES);
        mCategorySpinner.setAdapter(adapter);
        mCategorySpinner.setOnItemSelectedListener(this);
        int index = Util.indexOfCategory(mToDoItem.getCategory());
        if(index >= 0) {
            mCategorySpinner.setSelection(index);
        }

        String dateString = mToDoItem.getDueDate();
        int year = Util.extractYear(dateString);
        int month = Util.extractMonth(dateString);
        int day = Util.extractDay(dateString);
        String description = mToDoItem.getDescription();
        mDatePicker.updateDate(year, month, day);

        mDescriptionEditText.setText(description);

        mUpdateButton.setText("Update");
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = mDescriptionEditText.getText().toString();
                String dateString = Util.formatDate(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
                boolean complete = mToDoItem.isCompleted();
                String category = (String)mCategorySpinner.getSelectedItem();
                ToDoItem item = new ToDoItem(description, dateString, complete, category);

                mListener.onUpdateToDo(mIdToBeUpdated, item);
                UpdateToDoFragment.this.dismiss();
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