package tests;

import core.clients.APIClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApiClientTest {

//    @Test
//    public void baseUrlIsLoadedFromProperties() {
//        APIClient apiClient = new APIClient();
//
//        assertNotNull(apiClient.getBaseUrl(), "baseUrl не прочитан из файла настроек");
//        assertTrue(apiClient.getBaseUrl().startsWith("http"), "baseUrl должен начинаться с http");
//        assertFalse(apiClient.getBaseUrl().endsWith("/"), "baseUrl не должен оканчиваться слэшем");
//        // Вывод: тест зелёный, если файл лежит в resources и содержит ключ baseUrl
//    }
}