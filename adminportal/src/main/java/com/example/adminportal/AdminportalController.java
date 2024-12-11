package com.example.adminportal;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminportalController {

    @GetMapping("/")
    public String index(@AuthenticationPrincipal OidcUser user, Model model) {
        // Retrieve user information from the OAuth2 token (Azure AD)
        model.addAttribute("fullName", user.getUserInfo().getFullName());
        model.addAttribute("email", user.getUserInfo().getEmail());
        model.addAttribute("givenName", user.getUserInfo().getGivenName());
        model.addAttribute("familyName", user.getUserInfo().getFamilyName());
        model.addAttribute("pictureUrl", user.getUserInfo().getPicture());
        model.addAttribute("userId", user.getUserInfo().getSubject());
        model.addAttribute("makerRole", "No");
        model.addAttribute("checkerRole", "No");

        Object groups = user.getAttribute("groups");
        if (groups instanceof List) {
            for (String groupID : (List<String>) groups) {
                if (groupID.equals("d2d52b91-9f55-4875-af61-a289540677d7")) {
                    model.addAttribute("makerRole", "Yes");
                }
                if (groupID.equals("a44c1a69-d09b-4915-a9bd-805051d26175")) {
                    model.addAttribute("checkerRole", "Yes");
                }
            }
        }
        return "home";  // This is the login page or landing page
    }

    @GetMapping(value = "/ssl-test")
    public String inbound(){
        return "Inbound TLS is working!!";
    }
}
