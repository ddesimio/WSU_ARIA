package mwdindustries.aria.util;

import android.location.Location;
import android.location.LocationManager;

public class AdvancedLocation {
    public static enum BuildingScope {NEAR, MID, FAR};

    public AdvancedLocation() {
        l = new Location(LocationManager.GPS_PROVIDER);
        name = "";
        shortname = "";
        information = "";
        buildingDistance = 300;
        scope = BuildingScope.MID;
    }

    public Location getLocation() {
        return l;
    }

    public void setLocation(Location l) {
        this.l = l;
    }

    public double getLatitude() { return l.getLatitude(); }

    public void setLatitude(double lat) { this.l.setLatitude(lat); }

    public double getLongitude() { return l.getLongitude(); }

    public void setLongitude(double lon) { this.l.setLongitude(lon); }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortname() { return shortname; }

    public void setShortname(String shortname) { this.shortname = shortname; }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public double getBuildingDistance() {
        return buildingDistance;
    }

    public void setBuildingDistance(double buildingDistance) { this.buildingDistance = buildingDistance; }

    public float distanceTo(AdvancedLocation dest){ return l.distanceTo(dest.getLocation()); }

    public float bearingTo(AdvancedLocation dest){
        return l.bearingTo(dest.getLocation());
    }

    public float distanceTo(Location dest){ return l.distanceTo(dest); }

    public float bearingTo(Location dest){
        return l.bearingTo(dest);
    }

    private Location l;
    private String name;
    private String shortname;
    private String information;
    private double buildingDistance;
    private BuildingScope scope;
}