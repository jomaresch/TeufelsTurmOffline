package com.dex.teufelsturmoffline.model;

public class ProcessUpdate {
    public boolean firstUpdate;
    public String head, sub;
    public int progress, progressMax;

    public ProcessUpdate(boolean firstUpdate, String head, String sub, int progress, int progressMax) {
        this.firstUpdate = firstUpdate;
        this.head = head;
        this.sub = sub;
        this.progress = progress;
        this.progressMax = progressMax;
    }
}
