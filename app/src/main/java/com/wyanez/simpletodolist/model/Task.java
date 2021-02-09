package com.wyanez.simpletodolist.model;

import com.wyanez.simpletodolist.util.Utilities;

import java.io.Serializable;
import java.util.Calendar;

public class Task  implements Serializable {
    private int id;
    private String title;
    private String description;
    private String tags;
    private Calendar deadline;
    private int priority;
    private boolean active;

    public Task(){}

    public Task(String title, String description, String tags, Calendar deadline, int priority, boolean active) {
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.deadline = deadline;
        this.priority = priority;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Calendar getDeadline() {
        return deadline;
    }

    public String getDeadlineYMD(){
        return Utilities.CalendarToStringYmd(deadline);
    }

    public void setDeadline(Calendar deadline) {
        this.deadline = deadline;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", tags='" + tags + '\'' +
                ", deadline='" + getDeadlineYMD() + '\'' +
                ", priority=" + priority +
                ", active=" + active +
                '}';
    }
}
