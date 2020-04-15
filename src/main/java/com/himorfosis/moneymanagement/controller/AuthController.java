package com.himorfosis.moneymanagement.controller;

import com.himorfosis.moneymanagement.entity.UsersEntity;
import com.himorfosis.moneymanagement.exception.*;
import com.himorfosis.moneymanagement.model.AuthenticateResponse;
import com.himorfosis.moneymanagement.model.JwtRequest;
import com.himorfosis.moneymanagement.model.ResponseStatus;
import com.himorfosis.moneymanagement.model.StatusResponse;
import com.himorfosis.moneymanagement.model.UserResponse;
import com.himorfosis.moneymanagement.repository.UsersRepository;
import com.himorfosis.moneymanagement.security.encryption.UserEncrypt;
import com.himorfosis.moneymanagement.security.jwt.JwtSecurityDetailService;
import com.himorfosis.moneymanagement.security.jwt.JwtSecurityToken;
import com.himorfosis.moneymanagement.service.ImageStorageService;
import com.himorfosis.moneymanagement.state.MsgState;
import com.himorfosis.moneymanagement.utilities.DateSetting;
import com.himorfosis.moneymanagement.security.encryption.Encryption;
import com.himorfosis.moneymanagement.utilities.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/")
public class AuthController {

    // update password
    // register
    // login

    private String TAG = "AuthController";

    @Autowired
    UsersRepository usersRepo;
    @Autowired
    ImageStorageService imageStorageService;
    @Autowired
    private JwtSecurityToken jwtSecurityToken;
    @Autowired
    private JwtSecurityDetailService jwtSecurityDetailService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(value = "register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public StatusResponse create(
            @RequestParam MultiValueMap<String,String> paramMap) {

        String getName = paramMap.getFirst("name");
        String getEmail = paramMap.getFirst("email");
        String getPassword = paramMap.getFirst("password");
        String getPasswordConfirm = paramMap.getFirst("password_confirm");

        StatusResponse status = new StatusResponse();
        UsersEntity item = new UsersEntity();

        UsersEntity emailValidation = usersRepo.findByEmail(getEmail);

        if (emailValidation != null) {
            isAccountUsedException("Email");
        } else {

            if (!getName.isEmpty() && !getEmail.isEmpty() && !getPassword.isEmpty() && !getPasswordConfirm.isEmpty()) {

                // validate email
                try {

                    String validateEmail = getEmail.substring(getEmail.indexOf('@'), getEmail.length());
                    isLog(validateEmail);
                    if (validateEmail.toLowerCase().equals("@gmail.com")) {

                        //check password
                        if (getPassword.equals(getPasswordConfirm)) {

                            // set data
                            item.setName(getName);
                            item.setEmail(getEmail);
                            item.setPassword(getPassword);
                            item.setCreated_at(DateSetting.timestamp());
                            item.setUpdated_at(DateSetting.timestamp());

                            // save data
                            usersRepo.save(item);

                            // set response callback
                            status.setStatus(200);
                            status.setMessage(MsgState.SUCCESS);
                        } else {
                            isError(MsgState.Pass_not_match);
                        }
                    } else {
                        isError(MsgState.Email_not_valid);
                    }

                } catch (Exception e) {
                    String errorMessage = e.toString();
                    String validateEmail = errorMessage.substring(errorMessage.indexOf(""), errorMessage.length());
                    isError(validateEmail);
                }

            } else {
                throw new DataNotCompleteException();
            }

        }

        return status;
    }

    @PostMapping(value = "login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> login(
            @RequestParam MultiValueMap<String,String> paramMap) throws Exception {

        String getEmail = paramMap.getFirst("email");
        String getPassword = paramMap.getFirst("password");

        authenticate(getEmail, Encryption.setEncrypt(getPassword));
        final UserDetails userDetails = jwtSecurityDetailService.loadUserByUsername(getEmail);
        final String token = jwtSecurityToken.generateToken(userDetails);

        UserResponse dataUser;

        if (!getEmail.isEmpty() && !getPassword.isEmpty()) {

            UsersEntity item = usersRepo.findByEmail(getEmail);

            if (item == null) {
                throw new AccountIncorrectException();
            } else {

                isLog(item.getId().toString());

                if (item.getPassword().equals(getPassword)) {

                    dataUser = new UserResponse(
                            UserEncrypt.generateEncrypt(String.valueOf(item.getId())),
                            item.getName(),
                            item.getEmail(),
                            item.getPhone_number(),
                            item.getImage(),
                            token,
                            item.getActive(),
                            item.getCreated_at(),
                            item.getUpdated_at()
                    );

                    // update data
                    UsersEntity update = new UsersEntity();
                    update.setId(item.getId());
                    update.setName(item.getName());
                    update.setImage(item.getImage());
                    update.setEmail(item.getEmail());
                    update.setPassword(item.getPassword());
                    update.setPhone_number(item.getPhone_number());
                    update.setActive(item.getActive());
                    update.setCreated_at(item.getCreated_at());
                    update.setUpdated_at(DateSetting.timestamp());
                    update.setToken(token);
                    // update
                    usersRepo.save(update);


                } else {
                    throw new AccountIncorrectException();
                }
            }

        } else {
            throw new DataNotCompleteException();
        }

        return ResponseEntity.ok(dataUser);
    }

    @PostMapping(value = "logout", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> logout(
            @RequestParam MultiValueMap<String,String> paramMap) throws Exception {

        String getUserId = paramMap.getFirst("user_id");

        String userId = UserEncrypt.generateDecrypt(getUserId);
        isLog(UserEncrypt.generateDecrypt(getUserId));
        UsersEntity item = usersRepo.findById(Long.valueOf(userId))
                .orElseThrow(() -> new ResourceNotFoundException(getUserId));

        // response data
        UsersEntity update = new UsersEntity();
        StatusResponse status = new StatusResponse();

        if (item != null) {

            update.setId(item.getId());
            update.setName(item.getName());
            update.setImage(item.getImage());
            update.setEmail(item.getEmail());
            update.setPassword(item.getPassword());
            update.setPhone_number(item.getPhone_number());
            update.setActive(item.getActive());
            update.setCreated_at(item.getCreated_at());
            update.setUpdated_at(DateSetting.timestamp());
            // update
            usersRepo.save(update);

            // set response callback
            status.setStatus(200);
            status.setMessage(MsgState.SUCCESS);

        } else {
            isLog(MsgState.FAILED);
        }

        return ResponseEntity.ok(status);

    }


    private void authenticate(String username, String password) throws Exception {
        isLog("authenticate : ");
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            isLog("INVALID_CREDENTIALS : " + e);
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            isLog("INVALID_CREDENTIALS : " + e);
//            throw new Exception("INVALID_CREDENTIALS", e);
        }

    }

    private void isLog(String message) {
        Util.log(TAG, message);
    }

    private void isError(String message) {
        throw new MessageException(message);
    }
    private void isAccountUsedException(String message) {
        throw new AccountUsedException(message);
    }

}
