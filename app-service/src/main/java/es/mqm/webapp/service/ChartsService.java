package es.mqm.webapp.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.mqm.webapp.dto.AdminChartsDTO;
import es.mqm.webapp.model.CategoryData;

@Service
public class ChartsService {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private OrderService orderService;

    public AdminChartsDTO getAdminChartsData() {
        long userCounter = userService.count();
        long productCounter = productService.count();
        long orderCounter = orderService.count();

        List<Integer> categoriesSold = new ArrayList<>();
        List<String> categoriesSoldLabels = new ArrayList<>();
        for (CategoryData.Category category : CategoryData.CATEGORIES) {
            categoriesSold.add(productService.countByCategory(category.slug()));
            categoriesSoldLabels.add(category.name());
        }

        List<Integer> reviewsRating = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            reviewsRating.add(reviewService.findByRating(i).size());
        }

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.withDayOfYear(1);
        int[] monthCounts = new int[12];
        for (Object[] row : userService.countUsersByMonthBetween(startDate, today)) {
            if (row == null || row.length < 3 || row[0] == null || row[1] == null || row[2] == null) {
                continue;
            }
            int month = ((Number) row[1]).intValue();
            int count = ((Number) row[2]).intValue();
            if (month < 1 || month > 12) {
                continue;
            }
            monthCounts[month - 1] = count;
        }
        List<Integer> newUsersPerMonth = new ArrayList<>(12);
        for (int i = 0; i < 12; i++) {
            newUsersPerMonth.add(monthCounts[i]);
        }

        return new AdminChartsDTO(
            userCounter,
            productCounter,
            orderCounter,
            categoriesSold,
            categoriesSoldLabels,
            newUsersPerMonth,
            reviewsRating
        );
    }
}
