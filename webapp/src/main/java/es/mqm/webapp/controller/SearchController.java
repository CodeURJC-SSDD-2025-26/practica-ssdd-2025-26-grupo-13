package es.mqm.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.mqm.webapp.model.CategoryData;
import es.mqm.webapp.model.Product;
import es.mqm.webapp.service.ProductService;

@Controller
public class SearchController extends BaseController{

    private static final int PAGE_SIZE = 20;

    @Autowired
    private ProductService productService;

    @GetMapping("/search")
    public String index(Model model,
            @RequestParam(required = false, value = "search") String searchParam,
            @RequestParam(required = false, value = "category") String category,
            @RequestParam(required = false, value = "date") String date,
            @RequestParam(required = false, value = "minPrice") String minPrice,
            @RequestParam(required = false, value = "maxPrice") String maxPrice,
            @RequestParam(required = false, value = "location") String location,
            @RequestParam(value = "page", defaultValue = "0") int page) {

        if (page < 0)
            page = 0;

        Page<Product> productPage;
        boolean hasSearch = searchParam != null && !searchParam.isEmpty();
        boolean hasCategory = category != null && !category.isEmpty() && !category.equals("todas");

        productPage = productService.searchProducts(searchParam, category, location, date, minPrice, maxPrice, page, PAGE_SIZE);

        if (page >= productPage.getTotalPages() && productPage.getTotalPages() > 0) {
            page = productPage.getTotalPages() - 1;
            productPage = productService.searchProducts(searchParam, category, location, date, minPrice, maxPrice, page, PAGE_SIZE);
        }

        model.addAttribute("cssfile", "search");
        model.addAttribute("loggedin", true);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("categories", CategoryData.CATEGORIES);

        model.addAttribute("search", searchParam != null ? searchParam : "");
        model.addAttribute("category", category != null ? category : "");
        model.addAttribute("date", date != null ? date : "");
        model.addAttribute("minPrice", minPrice != null ? minPrice : "");
        model.addAttribute("maxPrice", maxPrice != null ? maxPrice : "");
        model.addAttribute("location", location != null ? location : "");

        model.addAttribute("isTodas", !hasCategory);
        model.addAttribute("isRopa", "ropa".equals(category));
        model.addAttribute("isInformatica", "informatica".equals(category));
        model.addAttribute("isAutomoviles", "automoviles".equals(category));
        model.addAttribute("isElectrodomesticos", "electrodomesticos".equals(category));
        model.addAttribute("isLibros", "libros".equals(category));
        model.addAttribute("isOtros", "otros".equals(category));

        model.addAttribute("isDateAny", date == null || date.isEmpty());
        model.addAttribute("isDateHoy", "hoy".equals(date));
        model.addAttribute("isDate7", "7".equals(date));
        model.addAttribute("isDate30", "30".equals(date));

        int totalPages = productPage.getTotalPages() == 0 ? 1 : productPage.getTotalPages();
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("hasPrev", productPage.hasPrevious());
        model.addAttribute("hasNext", productPage.hasNext());
        model.addAttribute("prevPage", page - 1);
        model.addAttribute("nextPage", page + 1);
        model.addAttribute("showPagination", totalPages > 1);

        model.addAttribute("baseQuery", buildBaseQuery(searchParam, category, date, minPrice, maxPrice, location));

        return "search";
    }

    private String buildBaseQuery(String search, String category, String date,
            String minPrice, String maxPrice, String location) {
        StringBuilder sb = new StringBuilder();
        appendParam(sb, "search", search);
        appendParam(sb, "category", category);
        appendParam(sb, "date", date);
        appendParam(sb, "minPrice", minPrice);
        appendParam(sb, "maxPrice", maxPrice);
        appendParam(sb, "location", location);
        return sb.toString();
    }

    private void appendParam(StringBuilder sb, String key, String value) {
        if (value != null && !value.isEmpty()) {
            if (sb.length() > 0)
                sb.append("&");
            sb.append(key).append("=").append(value);
        }
    }
}