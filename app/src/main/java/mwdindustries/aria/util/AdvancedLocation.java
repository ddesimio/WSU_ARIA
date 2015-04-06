package mwdindustries.aria.util;

import android.location.Location;

public class AdvancedLocation {
    public Location getLocation() {
        return l;
    }

    public void setLocation(Location l) {
        this.l = l;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public float distanceTo(AdvancedLocation dest){
        return l.distanceTo(dest.getLocation());
    }

    public float bearingTo(AdvancedLocation dest){
        return l.bearingTo(dest.getLocation());
    }

    private Location l;
    private String name;
    private String information;


}