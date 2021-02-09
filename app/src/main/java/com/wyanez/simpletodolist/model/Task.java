package com.wyanez.simpletodolist.model;

import java.io.Serializable;
import java.util.Calendar;

public class Task  implements Serializable {
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
        String strDeadline = String.format("%d/%d/%d",deadline.get(Calendar.YEAR), deadline.get(Calendar.MONTH)+1,deadline.get(Calendar.DAY_OF_MONTH));
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", tags='" + tags + '\'' +
                ", deadline='" + strDeadline + '\'' +
                ", priority='" + priority + '\'' +
                ", active=" + active +
                '}';
    }
}
