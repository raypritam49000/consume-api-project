package com.consume.api;

import com.consume.api.constants.HttpMethod;
import com.consume.api.constants.UserAPIConstants;
import com.consume.api.response.GenericApiResponse;
import com.consume.api.utility.HttpUtility;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class Application {

    public static void main(String[] args) {

        GenericApiResponse<List<Map<String, Object>>> getUsersResponse = HttpUtility.sendHttpRequest(UserAPIConstants.GET_USER_API_URL, HttpMethod.GET, HttpUtility.createDefaultHeaders(), new HashMap<String, Object>(), new TypeToken<List<Map<String, Object>>>() {
        }.getType());

        System.out.println("getUsersResponse : " + getUsersResponse);

        Map<String, Object> createUserRequest = new LinkedHashMap<String, Object>();
        createUserRequest.put("name", "John Doe");
        createUserRequest.put("username", "johndoe");
        createUserRequest.put("email", "john.doe@example.com");

        GenericApiResponse<Map<String, Object>> createUserResponse = HttpUtility.sendHttpRequest(UserAPIConstants.CREATE_USER_API_URL, HttpMethod.POST, HttpUtility.createDefaultHeaders(), createUserRequest, new TypeToken<Map<String, Object>>() {
        }.getType());
        System.out.println("createUserResponse : " + createUserResponse);

        GenericApiResponse<Map<String, Object>> deleteUserResponse = HttpUtility.sendHttpRequest(UserAPIConstants.DELETE_USER_API_URL + "/" + 1, HttpMethod.DELETE, HttpUtility.createDefaultHeaders(), new HashMap<String, Object>(), new TypeToken<Map<String, Object>>() {
        }.getType());

        System.out.println("deleteUserResponse : " + deleteUserResponse);

        Map<String, Object> updateUserRequest = new LinkedHashMap<String, Object>();
        updateUserRequest.put("name", "Pritam Ray");
        updateUserRequest.put("username", "pritamray");
        updateUserRequest.put("email", "pritam.ray@gmail.com");

        GenericApiResponse<Map<String, Object>> updateUserResponse = HttpUtility.sendHttpRequest(UserAPIConstants.UPDATE_USER_API_URL + "/" + 1, HttpMethod.PUT, HttpUtility.createDefaultHeaders(), updateUserRequest, new TypeToken<Map<String, Object>>() {
        }.getType());

        System.out.println("updateUserResponse : " + updateUserResponse);

        GenericApiResponse<List<Map<String, Object>>> getUsers = HttpUtility.sendHttpRequest(UserAPIConstants.GET_USER_API_URL, HttpMethod.GET, HttpUtility.createDefaultHeaders(), new HashMap<String, Object>(), (Class) List.class);
        System.out.println("getUsers : " + getUsers);

        GenericApiResponse<Map<String, Object>> getUser = HttpUtility.sendHttpRequest(UserAPIConstants.GET_USER_API_URL + "/" + 1, HttpMethod.GET, HttpUtility.createDefaultHeaders(), new HashMap<String, Object>(), new TypeToken<Map<String, Object>>() {
        }.getType());
        System.out.println("getUser : " + getUser);

    }
}
