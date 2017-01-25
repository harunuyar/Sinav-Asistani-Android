package com.harunuyar.studentassistant.Notifier;


public abstract class Notifier {
    
    private boolean aDayAgo;
    private boolean aWeekAgo;
    
    protected Notifier(boolean aDayAgo, boolean aWeekAgo){
        setaDayAgo(aDayAgo);
        setaWeekAgo(aWeekAgo);
    }

    public abstract void notifyUser(Object info) throws Exception;

    public void setaDayAgo(boolean aDayAgo) {
        this.aDayAgo = aDayAgo;
    }

    public void setaWeekAgo(boolean aWeekAgo) {
        this.aWeekAgo = aWeekAgo;
    }

    public boolean isaDayAgo() {
        return aDayAgo;
    }

    public boolean isaWeekAgo() {
        return aWeekAgo;
    }
    
}
