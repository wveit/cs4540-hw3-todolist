/*
 *  Wilbert:
 *      # Added interfaces and listener variables so that the MainActivity can register as a
 *          listener and be notified when a ToDoItem is clicked or when a completed checkbox is
 *          toggled.
 *      # Removed code that dealt directly with SQLiteDatabase or Cursor objects. This code was
 *          moved to 'MyDatabase' class. This class' code was updated to interact with database
 *          through a MyDatabase object passed to it from MainActivity
 *      # The ItemHolder class was updated to include variables and views to represent the two new
 *          fields that were added (category and completion status).
 */

package com.sargent.mark.todolist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.sargent.mark.todolist.data.MyDatabase;
import com.sargent.mark.todolist.data.ToDoItem;




public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ItemHolder> {

    private MyDatabase mDatabase;
    private ItemClickListener mItemClickListener;
    private ItemCheckCompleteListener mItemCheckCompleteListener;
    private String TAG = "ToDoListAdapter";


    ////////////////////////////////////////
    //
    //  Interfaces
    //
    /////////////////////////////////////////

    public interface ItemClickListener {
        public void onItemClick(int id, ToDoItem toDoItem);
    }

    public interface ItemCheckCompleteListener{
        public void onItemCheckComplete(int id, ToDoItem toDoItem);
    }

    ////////////////////////////////////////
    //
    //  Adapter Methods
    //
    /////////////////////////////////////////

    public ToDoListAdapter(MyDatabase db, ItemClickListener itemClickListener, ItemCheckCompleteListener itemCheckCompleteListener){
        mDatabase = db;
        mItemClickListener = itemClickListener;
        mItemCheckCompleteListener = itemCheckCompleteListener;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item, parent, false);
        ItemHolder holder = new ItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mDatabase.getNumCursorRows();
    }



    ////////////////////////////////////////
    //
    //  ViewHolder
    //
    /////////////////////////////////////////


    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CheckBox.OnCheckedChangeListener {
        TextView descriptionTextView;
        TextView categoryTextView;
        TextView dueDateTextView;
        CheckBox completedCheckBox;

        String dueDate;
        String description;
        boolean isComplete;
        String category;
        int id;


        ItemHolder(View view) {
            super(view);
            descriptionTextView = (TextView) view.findViewById(R.id.description);
            categoryTextView = (TextView) view.findViewById(R.id.tv_category);
            dueDateTextView = (TextView) view.findViewById(R.id.dueDate);
            completedCheckBox = (CheckBox) view.findViewById(R.id.cb_completed);

            view.setOnClickListener(this);
            completedCheckBox.setOnCheckedChangeListener(this);

        }

        public void bind(int pos) {
            mDatabase.moveCursorToRow(pos);
            id = mDatabase.getId();

            dueDate = mDatabase.getDate();
            dueDateTextView.setText(dueDate);

            category = mDatabase.getCategory();
            categoryTextView.setText(category);

            description = mDatabase.getDescription();
            descriptionTextView.setText(description);

            isComplete = mDatabase.getCompleted();
            completedCheckBox.setChecked(isComplete);

            this.itemView.setTag(id);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            mItemClickListener.onItemClick(id, new ToDoItem(description, dueDate, isComplete, category));
            Log.d(TAG, "clicked");
        }

        @Override
        public void onCheckedChanged(CompoundButton checkbox, boolean isChecked){
            isComplete = isChecked;
            mItemCheckCompleteListener.onItemCheckComplete(id, new ToDoItem(description, dueDate, isComplete, category));
        }
    }

}
