package com.example.adminportal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class AdminportalController {

    @Value("${limitsapi.url}")
    private String limitsapiUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;


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

        model.addAttribute("dailyLimit", callLimitsApi());
        return "home";  // This is the login page or landing page
    }

    private String callLimitsApi() {

        // Extract the authentication token from the SecurityContext
        OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        // Get the access token from the OAuth2AuthorizedClientService
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                authenticationToken.getAuthorizedClientRegistrationId(),
                authenticationToken.getName()
        );

        // Get access token from the authorized client
        String accessToken = authorizedClient.getAccessToken().getTokenValue();

        // Prepare the headers with Bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        // Build the request
        HttpEntity<String> request = new HttpEntity<>(headers);

        String response = "Unable to get data from LimitsApi";
        try {
            response = restTemplate.exchange(limitsapiUrl, HttpMethod.GET, request, String.class).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
