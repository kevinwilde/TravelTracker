package kevinwilde.traveltracker;

import java.io.Serializable;

/**
 * Created by Kevin Wilde on 12/22/2015.
 */
public class Memory implements Serializable {
    long id;
    double latitude;
    double longitude;
    String city;
    String country;
    String notes;
}
