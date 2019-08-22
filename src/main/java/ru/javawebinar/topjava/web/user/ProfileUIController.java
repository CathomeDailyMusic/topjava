package ru.javawebinar.topjava.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.validation.Valid;

@Controller
@RequestMapping("/profile")
public class ProfileUIController extends AbstractUserController {
    @Autowired
    MessageSource messageSource;

    public static final String DB_ERROR_CODE = "users_unique_email_idx";

    @GetMapping
    public String profile() {
        return "profile";
    }

    @PostMapping
    public String updateProfile(@Valid UserTo userTo, BindingResult result, SessionStatus status, ModelMap model) {
        if (result.hasErrors()) {
            return "profile";
        } else {
            try {
                super.update(userTo, SecurityUtil.authUserId());
                SecurityUtil.get().update(userTo);
                status.setComplete();
                return "redirect:/meals";
            } catch (DataIntegrityViolationException e) {
                result.rejectValue("email", DB_ERROR_CODE,
                        messageSource.getMessage(DB_ERROR_CODE, null, LocaleContextHolder.getLocale()));
                return "profile";
            }
        }
    }

    @GetMapping("/register")
    public String register(ModelMap model) {
        model.addAttribute("userTo", new UserTo());
        model.addAttribute("register", true);
        return "profile";
    }

    @PostMapping("/register")
    public String saveRegister(@Valid UserTo userTo, BindingResult result, SessionStatus status, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("register", true);
            return "profile";
        } else {
            try {
                super.create(userTo);
                status.setComplete();
                return "redirect:/login?message=app.registered&username=" + userTo.getEmail();
            } catch (DataIntegrityViolationException e) {
                model.addAttribute("register", true);
                result.rejectValue("email", DB_ERROR_CODE,
                        messageSource.getMessage(DB_ERROR_CODE, null, LocaleContextHolder.getLocale()));
                return "profile";
            }
        }
    }
}