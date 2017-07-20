package com.sargent.mark.todolist.data;

/*
 *  Wilbert:
 *      # Added two new data members to this class, along with associated getters and setters
 */

public class ToDoItem {
    private String description;
    private String dueDate;
    private boolean completed;
    private String category;

    public ToDoItem(){
        description = "";
        dueDate = "";
        completed = false;
        category = "";
    }

    public ToDoItem(String description, String dueDate, boolean completed, String category) {
        this.description = description;
        this.dueDate = dueDate;
        this.completed = completed;
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted(){
        return completed;
    }

    public void setCompleted(boolean completed){
        this.completed = completed;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }
}
