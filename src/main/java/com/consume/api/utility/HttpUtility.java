package com.consume.api.utility;

import com.consume.api.constants.HttpMethod;
import com.consume.api.response.GenericApiResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpUtility {
    private static final Logger LOGGER = Logger.getLogger(HttpUtility.class.getName());
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();

    // ANSI color codes
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    public static Map<String, String> createDefaultHeaders() {
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("Content-Type", "application/json; utf-8");
        headers.put("Accept", "application/json");
        return headers;
    }

    public static <T> GenericApiResponse<T> sendHttpRequest(String urlString, HttpMethod httpMethod, Map<String, String> headers, Map<String, Object> requestBodyMap, Type responseType) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(httpMethod.name());
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            LOGGER.log(Level.INFO, ANSI_YELLOW + "Sending {0} request to URL: {1}" + ANSI_RESET, new Object[]{httpMethod, urlString});

            setRequestHeaders(connection, headers);

            if (isRequestBodyRequired(httpMethod)) {
                writeRequestBody(connection, requestBodyMap);
                LOGGER.log(Level.INFO, ANSI_YELLOW + "Request Body: {0}" + ANSI_RESET, GSON.toJson(requestBodyMap));
            }

            GenericApiResponse<T> response = processResponse(connection, responseType);

            LOGGER.log(Level.INFO, ANSI_GREEN + "Response Code: {0}" + ANSI_RESET, connection.getResponseCode());
            LOGGER.log(Level.INFO, ANSI_GREEN + "Response Body: {0}" + ANSI_RESET, response.getData());

            return response;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ANSI_RED + "HTTP request failed" + ANSI_RESET, e);
            return new GenericApiResponse<>(500, "Internal_Server_Error", null);
        } finally {
            if (Objects.nonNull(connection)) {
                connection.disconnect();
            }
        }
    }

    public static <T> GenericApiResponse<T> sendHttpRequest(String urlString, HttpMethod httpMethod, Map<String, String> headers, Map<String, Object> requestBodyMap, Class<T> responseType) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(httpMethod.name());
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            LOGGER.log(Level.INFO, ANSI_YELLOW + "Sending {0} request to URL: {1}" + ANSI_RESET, new Object[]{httpMethod, urlString});

            setRequestHeaders(connection, headers);

            if (isRequestBodyRequired(httpMethod)) {
                writeRequestBody(connection, requestBodyMap);
                LOGGER.log(Level.INFO, ANSI_YELLOW + "Request Body: {0}" + ANSI_RESET, GSON.toJson(requestBodyMap));
            }

            GenericApiResponse<T> response = processResponse(connection, responseType);

            LOGGER.log(Level.INFO, ANSI_GREEN + "Response Code: {0}" + ANSI_RESET, connection.getResponseCode());
            LOGGER.log(Level.INFO, ANSI_GREEN + "Response Body: {0}" + ANSI_RESET, response.getData());

            return response;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ANSI_RED + "HTTP request failed" + ANSI_RESET, e);
            return new GenericApiResponse<>(500, "Internal_Server_Error", null);
        } finally {
            if (Objects.nonNull(connection)) {
                connection.disconnect();
            }
        }
    }

    // Helper method to set request headers
    private static void setRequestHeaders(HttpURLConnection connection, Map<String, String> headers) {
        if (Objects.nonNull(headers) && !headers.isEmpty()) {
            headers.forEach((key, value) -> {
                LOGGER.log(Level.INFO, ANSI_YELLOW + "Setting header: {0} = {1}" + ANSI_RESET, new Object[]{key, value});
                connection.setRequestProperty(key, value);
            });
        }
    }

    // Helper method to check if the request body is required
    private static boolean isRequestBodyRequired(HttpMethod httpMethod) {
        return httpMethod == HttpMethod.POST || httpMethod == HttpMethod.PUT;
    }

    // Helper method to write the request body
    private static void writeRequestBody(HttpURLConnection connection, Map<String, Object> requestBodyMap) throws IOException {
        if (Objects.nonNull(requestBodyMap) && !requestBodyMap.isEmpty()) {
            connection.setDoOutput(true);
            String requestBody = GSON.toJson(requestBodyMap);
            LOGGER.log(Level.INFO, ANSI_YELLOW + "Request Body: {0}" + ANSI_RESET, requestBody);
            try (OutputStream os = connection.getOutputStream()) {
                os.write(requestBody.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }
            LOGGER.log(Level.INFO, ANSI_YELLOW + "Request body sent successfully." + ANSI_RESET);
        }
    }

    // Helper method to process the HTTP response
    private static <T> GenericApiResponse<T> processResponse(HttpURLConnection connection, Type responseType) throws IOException {
        int responseCode = connection.getResponseCode();
        String responseMessage = connection.getResponseMessage();
        LOGGER.log(Level.INFO, ANSI_GREEN + "HTTP Response Code: {0}, Message: {1}" + ANSI_RESET, new Object[]{responseCode, responseMessage});

        if ((responseCode == HttpURLConnection.HTTP_OK && "OK".equalsIgnoreCase(responseMessage)) || (responseCode == HttpURLConnection.HTTP_CREATED && "Created".equalsIgnoreCase(responseMessage))) {
            Reader reader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
            T response = GSON.fromJson(reader, responseType);
            LOGGER.log(Level.INFO, ANSI_GREEN + "Response Body: {0}" + ANSI_RESET, response);
            return new GenericApiResponse<>(responseCode, responseMessage, response);
        } else {
            LOGGER.log(Level.WARNING, ANSI_RED + "Unexpected HTTP response: {0} {1}" + ANSI_RESET, new Object[]{responseCode, responseMessage});
            return new GenericApiResponse<>(responseCode, responseMessage, null);
        }
    }

    // Helper method to process the HTTP response
    private static <T> GenericApiResponse<T> processResponse(HttpURLConnection connection, Class<T> responseType) throws IOException {
        int responseCode = connection.getResponseCode();
        String responseMessage = connection.getResponseMessage();
        LOGGER.log(Level.INFO, ANSI_GREEN + "HTTP Response Code: {0}, Message: {1}" + ANSI_RESET, new Object[]{responseCode, responseMessage});

        if ((responseCode == HttpURLConnection.HTTP_OK && "OK".equalsIgnoreCase(responseMessage)) || (responseCode == HttpURLConnection.HTTP_CREATED && "Created".equalsIgnoreCase(responseMessage))) {
            Reader reader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
            T response = GSON.fromJson(reader, responseType);
            LOGGER.log(Level.INFO, ANSI_GREEN + "Response Body: {0}" + ANSI_RESET, response);
            return new GenericApiResponse<>(responseCode, responseMessage, response);
        } else {
            LOGGER.log(Level.WARNING, ANSI_RED + "Unexpected HTTP response: {0} {1}" + ANSI_RESET, new Object[]{responseCode, responseMessage});
            return new GenericApiResponse<>(responseCode, responseMessage, null);
        }
    }
}
