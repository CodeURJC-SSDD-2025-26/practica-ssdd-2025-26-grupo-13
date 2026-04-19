package es.mqm.webapp.model;

import es.mqm.webapp.service.GeoUtils;
// includes additional information of products which cannot be stored in the database as they're relative to the 
// user seeing it and obtained in execution time
public class ExtendedProduct implements Comparable<ExtendedProduct> {
    private Product p;
    private Integer distance;
    private Boolean interestingCategory;
    private Double score;

    public ExtendedProduct(Product p, int distance) {
        this.p = p;
        this.distance = distance;
    }

    public ExtendedProduct(Product p) {
        this.p = p;
        this.distance = null;
    }

    public ExtendedProduct(Product p, User viewer) {
        this.p = p;
        this.distance = viewer != null ?  GeoUtils.calculateDistance(viewer.getLocation(), p.getUser().getLocation()).intValue() : null;
    }

    public ExtendedProduct(Product p, User viewer, Boolean interestingCategory) {
        this.p = p;
        this.distance = viewer != null ?  GeoUtils.calculateDistance(viewer.getLocation(), p.getUser().getLocation()).intValue() : null;
        this.interestingCategory = interestingCategory;
        if (this.distance == null || this.interestingCategory == null) {
            this.score = 0.0; 
        } else {
            this.score =  this.distance - (interestingCategory ? 1.0 : 0.0) * 35; 
        }
    } // idea: interesting category = 35 km of difference. 30 km interesting category product wins 0 km non-interesting category product, 40 km loses, etc. 

    public Product getP() {
        return p;
    }
    public Integer getDistance() {
        return distance;
    }
    public Double getScore() {
        return score;
    }
    public Boolean getInteresting() {
        return interestingCategory;
    }

    @Override 
    public int compareTo(ExtendedProduct ep ) {
        return this.score.compareTo(ep.getScore());
    }
}
