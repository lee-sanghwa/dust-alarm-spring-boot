package com.dustalarm.rest;

import com.dustalarm.common.DustAlarmCustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.dustalarm.model.User;
import com.dustalarm.service.DustAlarmService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;

import static com.dustalarm.common.DustAlarmCommon.setUserAgent2Users;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private DustAlarmService dustAlarmService;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> handleUserRequest(
        HttpServletRequest request,
        @RequestParam(value = "uuid", required = false) String uuid,
        @RequestParam(name = "page", defaultValue = "1") Integer pageNo,
        @RequestHeader(value = "User-Agent", required = false) String userAgent
    ) {
        if (uuid != null) {
            User user = this.dustAlarmService.findUserByUuid(uuid);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } else {
            Collection<User> users = this.dustAlarmService.findAllUsers(pageNo);

            setUserAgent2Users(users, userAgent);

            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(new DustAlarmCustomResponse.Builder()
                    .count(this.dustAlarmService.findCountUsers())
                    .results(users)
                    .setNextPreviousUrl(request.getRequestURI(), pageNo)
                    .Build(), HttpStatus.OK);
            }
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<User> addUser(@RequestBody @Valid User user,
                                        BindingResult bindingResult,
                                        UriComponentsBuilder ucBuilder) {
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (user == null)) {
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJSON());
            return new ResponseEntity<User>(headers, HttpStatus.BAD_REQUEST);
        } else {
            this.dustAlarmService.saveUser(user);
            headers.setLocation(ucBuilder.path("api/users/{id}").buildAndExpand(user.getId()).toUri());
            return new ResponseEntity<User>(user, headers, HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<User> getUser(@PathVariable("userId") int userId) {
        User user = this.dustAlarmService.findUserById(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<User> updateUser(@PathVariable("userId") int userId,
                                           @RequestBody @Valid User user,
                                           BindingResult bindingResult,
                                           UriComponentsBuilder ucBuilder) {
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (user == null)) {
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJSON());
            return new ResponseEntity<User>(headers, HttpStatus.BAD_REQUEST);
        } else {
            User currentUser = this.dustAlarmService.findUserById(userId);
            if (currentUser == null) {
                return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
            } else {
                currentUser.setUuid(user.getUuid());
                currentUser.setPushToken(user.getPushToken());
                this.dustAlarmService.saveUser(currentUser);
                headers.setLocation(ucBuilder.path("api/users/{id}").buildAndExpand(currentUser.getId()).toUri());
                return new ResponseEntity<User>(currentUser, headers, HttpStatus.OK);
            }
        }
    }
}
