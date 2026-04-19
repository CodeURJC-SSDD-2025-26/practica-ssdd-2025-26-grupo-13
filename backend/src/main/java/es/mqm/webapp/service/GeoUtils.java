package es.mqm.webapp.service;
import es.mqm.webapp.model.Location;

public class GeoUtils {

    public static final double haversine(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

    public static final Double calculateDistance(Location l1, Location l2) {
        double startLat = l1.getLatitude();
        double startLong = l1.getLongitude();
        double endLat = l2.getLatitude();
        double endLong = l2.getLongitude();

        double dLat = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));

        startLat = Math.toRadians(startLat);
        endLat = Math.toRadians(endLat);

        double a = haversine(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversine(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return Math.floor(6371.0 * c);
    }
}
