package core.clients;

import core.settings.ApiEndpoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class APIClient {

    private final String baseUrl;

    public APIClient(){
        this.baseUrl = determineBaseUrl();
    }

    private String determineBaseUrl() {
        String environment = System.getProperty("env", "test");
        String configFileName = "application-" + environment + ".properties";
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFileName)) {
            if (input == null) {
                throw new IllegalStateException("Configuration file not found: " + configFileName);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load configuration file: " + configFileName, e);
        }
        return properties.getProperty("baseUrl");
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    // Общая заготовка запроса: адрес + заголовки. given() пишем ОДИН раз - здесь
    private RequestSpecification getRequestSpec() {
        return RestAssured.given()
                .baseUri(baseUrl)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
    }

    // GET зпрос на эндпоинт /ping
    public Response ping() {
        return getRequestSpec()                    // дано: адрес и заголовки
                .when()                            // когда: отправляем запрос
                .get(ApiEndpoints.PING.getPath())  // GET на /ping
                .then()                            // затем: проверяем ответ
                .statusCode(201)                   // ожидаем 201 Created
                .extract()                         // распаковываем...
                .response();                       // ...весь ответ целиком
    }

    public Response getBooking() {
        return getRequestSpec()
                .when()
                .get(ApiEndpoints.BOOKING.getPath())
                .then()
                .statusCode(200)                   // здесь ожидаем 200 OK
                .extract()
                .response();
    }
}
