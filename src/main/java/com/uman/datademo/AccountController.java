package com.uman.datademo;

import com.uman.datademo.Account;
import com.uman.datademo.AccountService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Validated
public class AccountController {
    private final AccountService accountService;

    // @GetMapping("/all")
    // public ResponseEntity<List<Account>> getAllAccounts() {
    //     List<Account> accounts = accountService.getAllAccounts();
    //     return ResponseEntity.ok(accounts);
    // }

    // @GetMapping("/{userId}")
    // public ResponseEntity<Account> getAccountById(@PathVariable Integer userId) {
    //     return accountService.getAccountById(userId)
    //             .map(ResponseEntity::ok)
    //             .orElse(ResponseEntity.notFound().build());
    // }

    // @PostMapping("/create")
    // public ResponseEntity<Account> createAccount(@RequestBody Account account) {
    //     Account createdAccount = accountService.createAccount(account);
    //     return ResponseEntity.ok(createdAccount);
    // }

    // @DeleteMapping("/delete/{userId}")
    // public ResponseEntity<Void> deleteAccount(@PathVariable Integer userId) {
    //     accountService.deleteAccount(userId);
    //     return ResponseEntity.noContent().build();
    // }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String Index() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getName());
        return "index";
    }


    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public String SignIn(@RequestParam(name = "error", required = false) String error, Model model, Principal principal) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName().equals("anonymousUser")) {
            if (error != null) {
                model.addAttribute("errorMessage", "Invalid Username or Password");
            }
            
            return "signin";
        } else {
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public String SignInPOST() {
        return "signin";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String SignUp(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName().equals("anonymousUser")) {
            model.addAttribute("signUpModel", new SignUpModel());
            return "/signup";
        } else {
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String SignUpPOST(@Valid @ModelAttribute("signUpModel") SignUpModel signUpModel, BindingResult bindingResult) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
        if (!signUpModel.isPasswordMatching()) {
            bindingResult.rejectValue("confPassword", "error.confPassword", "Passwords do not match");
            return "/signup";
        }
            System.out.println("error");
            return "/signup";
        }
        System.out.println(signUpModel.getFullName());
        System.out.println(signUpModel.getUsername());
        System.out.println(signUpModel.getPassword());
        System.out.println(signUpModel.getConfPassword());
        System.out.println(signUpModel.getEmail());
        Account newAccount = new Account();
        newAccount.setUserName(signUpModel.getUsername());
        newAccount.setUserEmail(signUpModel.getEmail());
        newAccount.setUserPass(passwordEncoder.encode(signUpModel.getPassword()));
        newAccount.setEnabled(true);
        accountService.createAccount(newAccount);
        System.out.println("Added Data");
        return "redirect:/";
    }

    // @GetMapping("/login")
    // public String loginPage() {
    //     return "/signin";
    // }
}
