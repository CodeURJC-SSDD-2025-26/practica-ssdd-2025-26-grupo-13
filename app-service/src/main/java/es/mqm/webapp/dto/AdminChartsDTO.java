package es.mqm.webapp.dto;

import java.util.List;

public record AdminChartsDTO(
    long userCounter,
    long productCounter,
    long orderCounter,
    List<Integer> categoriesSold,
    List<String> categoriesSoldLabels,
    List<Integer> newUsersPerMonth,
    List<Integer> reviewsRating
) {
}
