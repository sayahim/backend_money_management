package com.himorfosis.moneymanagement.controller;

import com.himorfosis.moneymanagement.entity.UsersEntity;
import com.himorfosis.moneymanagement.exception.AccountIncorrectException;
import com.himorfosis.moneymanagement.exception.AccountUsedException;
import com.himorfosis.moneymanagement.exception.DataNotCompleteException;
import com.himorfosis.moneymanagement.exception.MessageException;
import com.himorfosis.moneymanagement.model.AuthenticateResponse;
import com.himorfosis.moneymanagement.model.JwtRequest;
import com.himorfosis.moneymanagement.model.StatusResponse;
import com.himorfosis.moneymanagement.model.UserResponse;
import com.himorfosis.moneymanagement.repository.UsersRepository;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

//    MsgState msgState;

    @PostMapping(value = "register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public StatusResponse create(
            @RequestPart(value = "name", required = true) @Valid String getName,
            @RequestPart(value = "email", required = true) @Valid String getEmail,
            @RequestPart(value = "password", required = true) @Valid String getPassword,
            @RequestPart(value = "password_confirm", required = true) @Valid String getPasswordConfirm
    ) {

        StatusResponse status = new StatusResponse();
        UsersEntity item = new UsersEntity();

        Boolean statusEmail;

        UsersEntity emailValidation = usersRepo.findByEmail(getEmail);

        if (emailValidation != null) {
            throw new AccountUsedException("Email");
        } else {

            if (!getName.isEmpty() && !getEmail.isEmpty() && !getPassword.isEmpty() && !getPasswordConfirm.isEmpty()) {

                // validate email
                try {

                    String validateEmail = getEmail.substring(getEmail.indexOf('@'), getEmail.length());
                    isLog(validateEmail);
                    if (validateEmail.toLowerCase().equals("@gmail.com")) {

                        //check password
                        if (getPassword.equals(getPasswordConfirm)) {

                            // encrypt password
                            String encodedPassword = new BCryptPasswordEncoder().encode(getPassword);
                            //generate username
                            String generateUsername = getEmail.substring(0, getEmail.indexOf('@'));

                            // set data
                            item.setName(getName);
                            item.setEmail(getEmail);
                            item.setUsername(generateUsername);
                            item.setPassword(encodedPassword);
                            item.setCreated_at(DateSetting.timestamp());
                            item.setUpdated_at(DateSetting.timestamp());

                            // save data
                            usersRepo.save(item);

                            // set response callback
                            status.setStatus(200);
                            status.setMessage(MsgState.SUCCESS);
                        } else {
                            throw new MessageException(MsgState.Pass_not_match);
                        }
                    } else {
                        throw new MessageException(MsgState.Email_not_valid);
                    }

                } catch (Exception e) {
                    String errorMessage = e.toString();
                    String validateEmail = errorMessage.substring(errorMessage.indexOf(':'), errorMessage.length());
                    throw new MessageException(validateEmail);
                }

            } else {
                throw new DataNotCompleteException();
            }

        }

        return status;
    }

//    @PostMapping(value = "authenticate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<AuthenticateResponse> createAuthenticationToken(
//            @RequestPart(value = "email", required = true) @Valid String getEmail,
//            @RequestPart(value = "password", required = true) @Valid String getPassword
//    ) throws Exception {

    @RequestMapping(value = "authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        isLog("authenticate here");
        isLog("username : " + authenticationRequest.getUsername());
        isLog("pass : " + authenticationRequest.getPassword());
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = jwtSecurityDetailService.loadUserByUsername(authenticationRequest.getUsername());
        isLog("userDetails : " + userDetails.getUsername());
        final String token = jwtSecurityToken.generateToken(userDetails);
        isLog("token : " + token);
        return ResponseEntity.ok(new AuthenticateResponse(token));

    }

    private void isLog(String message) {
        Util.log(TAG, message);
    }

    @PostMapping(value = "login", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserResponse login(
            @RequestPart(value = "email", required = true) @Valid String getEmail,
            @RequestPart(value = "password", required = true) @Valid String getPassword) {

//        authenticate(getEmail, getPassword);

        UserResponse dataUser;

        if (!getEmail.isEmpty() && !getPassword.isEmpty()) {

            UsersEntity item = usersRepo.findByEmail(getEmail);

            Util.log(TAG, "item data : " + item);

            if (item == null) {

                throw new AccountIncorrectException();

            } else {

                if (item.getPassword().equals(getPassword)) {

                    dataUser = new UserResponse(
                            Encryption.setEncrypt(String.valueOf(item.getId())),
                            item.getName(),
                            item.getEmail(),
                            item.getPhone_number(),
                            item.getImage(),
                            item.getToken(),
                            item.getActive(),
                            item.getCreated_at(),
                            item.getUpdated_at()
                    );


                    // set data
//                    item.setId(Encryption.setEncrypt(String.valueOf(data.getId())));
//                    item.setName(data.getName());
//                    item.setEmail(data.getEmail());
//                    item.setImage(data.getImage());
//                    item.setToken(data.getToken());
//                    item.setActive(data.getActive());
//                    item.setPassword(Encryption.setEncrypt(data.getPassword()));
//                    item.setCreated_at(DateSetting.timestamp());
//                    item.setUpdated_at(DateSetting.timestamp());

                } else {
                    throw new AccountIncorrectException();
                }

            }

        } else {
            throw new DataNotCompleteException();
        }

        return dataUser;
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


}
