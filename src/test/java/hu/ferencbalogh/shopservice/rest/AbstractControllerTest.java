package hu.ferencbalogh.shopservice.rest;

import hu.ferencbalogh.shopservice.dto.CreateOrderRequest;
import hu.ferencbalogh.shopservice.dto.CreateProductRequest;
import hu.ferencbalogh.shopservice.dto.ListOrdersResponse;
import hu.ferencbalogh.shopservice.dto.UpdateProductRequest;
import hu.ferencbalogh.shopservice.entity.Product;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
public abstract class AbstractControllerTest {

    @Autowired
    private ServletContext servletContext;

    @Value("${api.datetime.format}")
    private String dateFormat;

    @Value("${api.datetime.timezone}")
    private String defaultTimeZone;

    protected RestTemplate restTemplate = new RestTemplate();

    private DateTimeFormatter dateTimeFormatter;

    protected String baseUrl;

    @PostConstruct
    private void initialize() {
        dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
    }

    @LocalServerPort
    private void createBaseUrl(int port) {
        baseUrl = "http://localhost:" + port + servletContext.getContextPath();
    }

    protected void updateProduct(Product product) {
        UpdateProductRequest request = new UpdateProductRequest();
        request.setId(product.getId());
        request.setName(product.getName());
        request.setPrice(product.getPrice());

        ResponseEntity responseEntity = restTemplate.postForEntity(baseUrl + "/product/update", request, Object.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    protected void createProduct(Product product) {
        CreateProductRequest request = new CreateProductRequest(product.getName(), product.getPrice());
        Integer response = restTemplate.postForObject(baseUrl + "/product/create", request, Integer.class);
        assertNotNull(response);
        product.setId(response);
    }

    protected List<Product> getAllProducts() {
        return restTemplate.exchange(
                baseUrl + "/product/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Product>>() {
                }).getBody();
    }

    private String formatDateInDefaultTimeZone(ZonedDateTime from) {
        return from.toInstant().atZone(ZoneId.of(defaultTimeZone)).format(dateTimeFormatter);
    }

    protected List<ListOrdersResponse> getAllOrders(ZonedDateTime from, ZonedDateTime to) {
        String url = baseUrl + "/order/list";
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
        if (from != null) {
            uriBuilder.queryParam("from", formatDateInDefaultTimeZone(from));

        }
        if (to != null) {
            uriBuilder.queryParam("to", formatDateInDefaultTimeZone(to));
        }

        return restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ListOrdersResponse>>() {
                }).getBody();
    }

    protected Integer createOrder(CreateOrderRequest createOrderRequest) {
        Integer id = restTemplate.postForObject(baseUrl + "/order/create", createOrderRequest, Integer.class);
        assertNotNull(id);
        return id;
    }

    protected void recalculateOrderPrices(int orderId) {
        ResponseEntity response = restTemplate.postForEntity(baseUrl + "/order/" + orderId + "/recalculate", null, Object.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }
}
