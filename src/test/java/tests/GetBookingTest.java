package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import models.Booking;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingTest {
    private APIClient apiClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        apiClient = new APIClient();
    }

    @Test
    public void getBookingShouldReturnListOfIds() throws JsonProcessingException {
        Response response = apiClient.getBooking();
        assertThat(response.getStatusCode()).isEqualTo(200);

        // 1. Тело ответа -> обычная строка
        String responseBody = response.getBody().asString();

        // 2. Строка -> список объектов Booking
        List<Booking> bookings = objectMapper.readValue(
                responseBody, new TypeReference<List<Booking>>() {});

        // 3. Список не пустой, и каждый id - положительное число
        assertThat(bookings).isNotEmpty();
        for (Booking booking : bookings) {
            assertThat(booking.getBookingid()).isGreaterThan(0);
        }

        //Такой тест уже «хороший»: он проверяет статус, структуру ответа, непустоту списка и корректность каждого значения.
        // Тот же результат можно получить и другими инструментами - полезно знать оба способа:
        // Вариант без Jackson-модели: достаём список чисел через JsonPath REST Assured
        List<Integer> ids = response.jsonPath().getList("bookingid", Integer.class);
        assertThat(ids).isNotEmpty().allMatch(id -> id > 0);

        // Вариант с массивом вместо списка - REST Assured сам вызовет Jackson
        Booking[] bookingsArray = response.as(Booking[].class);
        assertThat(bookingsArray).hasSizeGreaterThan(0);
    }
}