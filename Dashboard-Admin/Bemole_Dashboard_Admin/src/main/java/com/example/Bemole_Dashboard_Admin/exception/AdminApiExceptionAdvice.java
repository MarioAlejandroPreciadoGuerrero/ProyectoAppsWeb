package com.example.Bemole_Dashboard_Admin.exception;

import com.example.Bemole_Dashboard_Admin.session.AdminSessionKeys;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class AdminApiExceptionAdvice {
    @ExceptionHandler(ApiClientException.class)
    public String manejarApiException(ApiClientException exception, HttpSession session, RedirectAttributes redirectAttributes) {
        int status = exception.getStatus();

        if (status == 401) {
            session.removeAttribute(AdminSessionKeys.ADMIN_SESSION);

            session.invalidate();

            return "redirect:/login?sesionExpirada";
        }

        if (status == 403) {
            return "redirect:/acceso-denegado";
        }

        redirectAttributes.addFlashAttribute("errorGlobal", exception.getMessage());

        return "redirect:/admin";
    }
}
