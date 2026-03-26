package es.mqm.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import es.mqm.webapp.model.User;
import es.mqm.webapp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CommonModelAttributes {

    @Autowired
    private UserService userService;

    @ModelAttribute("currentUser")
    public User currentUser(HttpServletRequest request) {
        if (request.getUserPrincipal() == null) {
            return null;
        }
        String email = request.getUserPrincipal().getName();
        return userService.findByEmail(email).orElse(null);
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin(HttpServletRequest request) {
        User user = currentUser(request);
        if (user == null) {
            return false;
        }
        return user.getRoles().contains("ADMIN");
    }
}
