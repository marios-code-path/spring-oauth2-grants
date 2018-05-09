package com.santas.cap.demo;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Collection;

@RestController
@AllArgsConstructor
public class SignageController {

    SignageRepository signageRepository;

    CapOneOAuth2Redirector capOneOAuth2Redirector;

    @GetMapping("/byDocId/{docId}")
    public Collection<Signage> getByDocId(@PathVariable("docId") String docId) {
        return signageRepository.getByDocId(docId);
    }

    @GetMapping("/connect/{network}")
    public void connect(@PathVariable("network") String networkConnect, HttpServletResponse response) throws Exception {
        if (networkConnect.matches("capone")) {
            response.sendRedirect(capOneOAuth2Redirector.getRedirectUrl());
        } else {
            response.sendRedirect("/"); // TODO: Send somewhere responsable
        }
    }
}

@RestController
class UserController {

    UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/user/{username}")
    public User getByUsername(@PathVariable("userName") String username, Principal principal) {
        return userRepository.getUsersByName(username);
    }
}