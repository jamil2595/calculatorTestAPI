import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.BeforeClass;
import org.junit.Test;

public class CalculatorApiTests {

    @BeforeClass
    public static void setup() {
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://ansokan.bigbank.se/api/")
                .setBasePath("/v1")
                .addHeader("Content-Type", "application/json")
                .build();
    }

    @Test
    public void postPayloadReturns200WhenRequestBodyIsCorrect() {
        String requestBody = "{\"maturity\":120,\"productType\":\"LOANSE02\",\"amount\":97350,\"interestRate\":10.95,\"monthlyPaymentDay\":27,\"administrationFee\":40,\"conclusionFee\":695,\"currency\":\"SEK\"}";

        Response response = RestAssured.given()
                .body(requestBody)
                .post("/loan/calculate");

        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test
    public void postPayloadReturns200WithCorrectTotalRepayableAmount() {
        String requestBody = "{\"maturity\":120,\"productType\":\"LOANSE02\",\"amount\":97350,\"interestRate\":10.95,\"monthlyPaymentDay\":27,\"administrationFee\":40,\"conclusionFee\":695,\"currency\":\"SEK\"}";

        Response response = RestAssured.given()
                .body(requestBody)
                .post("/loan/calculate");
        ResponseBody responseBody = response.getBody();

        assertThat(responseBody.jsonPath().getDouble("totalRepayableAmount")).isEqualTo(165498.98);
    }

    @Test
    public void postPayloadReturns200WithCorrectMonthlyPaymentAmount() {
        String requestBody = "{\"maturity\":120,\"productType\":\"LOANSE02\",\"amount\":97350,\"interestRate\":10.95,\"monthlyPaymentDay\":27,\"administrationFee\":40,\"conclusionFee\":695,\"currency\":\"SEK\"}";

        Response response = RestAssured.given()
                .body(requestBody)
                .post("/loan/calculate");
        ResponseBody responseBody = response.getBody();

        assertThat(responseBody.jsonPath().getDouble("monthlyPayment")).isEqualTo(1379.16);
    }

    @Test
    public void postPayloadReturns200WithCorrectApr() {
        String requestBody = "{\"maturity\":120,\"productType\":\"LOANSE02\",\"amount\":97350,\"interestRate\":10.95,\"monthlyPaymentDay\":27,\"administrationFee\":40,\"conclusionFee\":695,\"currency\":\"SEK\"}";

        Response response = RestAssured.given()
                .body(requestBody)
                .post("/loan/calculate");
        ResponseBody responseBody = response.getBody();
        assertThat(responseBody.jsonPath().getDouble("apr")).isEqualTo(12.7);
    }

    @Test
    public void postPayloadReturns409WhenMaturityIsMissing() {
        String requestBody = "{\"productType\":\"LOANSE02\",\"amount\":97350,\"interestRate\":10.95,\"monthlyPaymentDay\":27,\"administrationFee\":40,\"conclusionFee\":695,\"currency\":\"SEK\"}";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/loan/calculate");
        ResponseBody responseBody = response.getBody();

        assertThat(responseBody.jsonPath().getList("message").get(0)).isEqualTo("should have required property 'maturity'");
        assertThat(response.getStatusCode()).isEqualTo(400);
    }

    @Test
    public void postPayloadReturns409WhenProductTypeIsMissing() {
        String requestBody = "{\"maturity\":120,\"amount\":97350,\"interestRate\":10.95,\"monthlyPaymentDay\":27,\"administrationFee\":40,\"conclusionFee\":695,\"currency\":\"SEK\"}";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/loan/calculate");
        ResponseBody responseBody = response.getBody();

        assertThat(responseBody.jsonPath().getList("message").get(0)).isEqualTo("should have required property 'productType'");
        assertThat(response.getStatusCode()).isEqualTo(400);
    }

    @Test
    public void postPayloadReturns409WhenAmountIsMissing() {
        String requestBody = "{\"maturity\":120,\"productType\":\"LOANSE02\",\"interestRate\":10.95,\"monthlyPaymentDay\":27,\"administrationFee\":40,\"conclusionFee\":695,\"currency\":\"SEK\"}";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/loan/calculate");
        ResponseBody responseBody = response.getBody();

        assertThat(responseBody.jsonPath().getList("message").get(0)).isEqualTo("should have required property 'amount'");
        assertThat(response.getStatusCode()).isEqualTo(400);
    }

    @Test
    public void postPayloadReturns409WhenInterestRateIsMissing() {
        String requestBody = "{\"maturity\":120,\"productType\":\"LOANSE02\",\"amount\":97350,\"monthlyPaymentDay\":27,\"administrationFee\":40,\"conclusionFee\":695,\"currency\":\"SEK\"}";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/loan/calculate");
        ResponseBody responseBody = response.getBody();

        assertThat(responseBody.jsonPath().getList("message").get(0)).isEqualTo("should have required property 'interestRate'");
        assertThat(response.getStatusCode()).isEqualTo(400);
    }

    @Test
    public void postPayloadReturns409WhenMonthlyPaymentIsMissing() {
        String requestBody = "{\"maturity\":120,\"productType\":\"LOANSE02\",\"amount\":97350,\"interestRate\":10.95,\"administrationFee\":40,\"conclusionFee\":695,\"currency\":\"SEK\"}";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/loan/calculate");
        ResponseBody responseBody = response.getBody();

        assertThat(responseBody.jsonPath().getList("message").get(0)).isEqualTo("should have required property 'monthlyPaymentDay'");
        assertThat(response.getStatusCode()).isEqualTo(400);
    }

    @Test
    public void postPayloadReturns409WhenAdministrationFeeIsMissing() {
        String requestBody = "{\"maturity\":120,\"productType\":\"LOANSE02\",\"amount\":97350,\"interestRate\":10.95,\"monthlyPaymentDay\":27,\"conclusionFee\":695,\"currency\":\"SEK\"}";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/loan/calculate");
        ResponseBody responseBody = response.getBody();

        assertThat(responseBody.jsonPath().getList("message").get(0)).isEqualTo("should have required property 'administrationFee'");
        assertThat(response.getStatusCode()).isEqualTo(400);
    }

    @Test
    public void postPayloadReturns409WhenConclusionFeeIsMissing() {
        String requestBody = "{\"maturity\":120,\"productType\":\"LOANSE02\",\"amount\":97350,\"interestRate\":10.95,\"monthlyPaymentDay\":27,\"administrationFee\":40,\"currency\":\"SEK\"}";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/loan/calculate");
        ResponseBody responseBody = response.getBody();

        assertThat(responseBody.jsonPath().getList("message").get(0)).isEqualTo("should have required property 'conclusionFee'");
        assertThat(response.getStatusCode()).isEqualTo(400);
    }

    @Test
    public void postPayloadReturns409WhenCurrencyIsMissing() {
        String requestBody = "{\"maturity\":120,\"productType\":\"LOANSE02\",\"amount\":97350,\"interestRate\":10.95,\"monthlyPaymentDay\":27,\"administrationFee\":40,\"conclusionFee\":695,}";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/loan/calculate");

        assertThat(response.getStatusCode()).isEqualTo(400);
    }

    @Test
    public void postPayloadReturns409WhenMaturityIsString() {
        String requestBody = "{\"maturity\":asdf,\"productType\":\"LOANSE02\",\"amount\":97350,\"interestRate\":10.95,\"monthlyPaymentDay\":27,\"administrationFee\":40,\"conclusionFee\":695,\"currency\":\"SEK\"}";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/loan/calculate");

        assertThat(response.getStatusCode()).isEqualTo(400);
    }

    @Test
    public void postPayloadConvertsFloatMaturityToInteger() {
        String requestBodyFloat = "{\"maturity\":12.1,\"productType\":\"LOANSE02\",\"amount\":97350,\"interestRate\":10.95,\"monthlyPaymentDay\":27,\"administrationFee\":40,\"conclusionFee\":695,\"currency\":\"SEK\"}";
        Response responseFloat = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBodyFloat)
                .post("/loan/calculate");
        ResponseBody responseBodyFloat = responseFloat.getBody();
        assertThat(responseFloat.getStatusCode()).isEqualTo(200);

        String requestBodyInt = "{\"maturity\":12,\"productType\":\"LOANSE02\",\"amount\":97350,\"interestRate\":10.95,\"monthlyPaymentDay\":27,\"administrationFee\":40,\"conclusionFee\":695,\"currency\":\"SEK\"}";
        Response responseInt = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBodyInt)
                .post("/loan/calculate");
        ResponseBody responseBodyInt = responseInt.getBody();
        assertThat(responseInt.getStatusCode()).isEqualTo(200);

        String expectedOutput = "{\"totalRepayableAmount\":103180.06,\"monthlyPayment\":8598.34,\"apr\":14.48}";

        assertThat(responseBodyInt.asString()).isEqualTo(expectedOutput);
        assertThat(responseBodyFloat.asString()).isEqualTo(expectedOutput);
    }

    @Test
    public void postPayloadReturns409WhenProductTypeIsInteger() {
        String requestBody = "{\"maturity\":120,\"productType\":15,\"amount\":97350,\"interestRate\":10.95,\"monthlyPaymentDay\":27,\"administrationFee\":40,\"conclusionFee\":695,\"currency\":\"SEK\"}";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/loan/calculate");
        ResponseBody responseBody = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(400);
        assertThat(responseBody.jsonPath().getList("message").get(0)).isEqualTo("should be string");
    }

    @Test
    public void postPayloadReturns409WhenProductTypeIsFloat() {
        String requestBody = "{\"maturity\":120,\"productType\":15.5,\"amount\":97350,\"interestRate\":10.95,\"monthlyPaymentDay\":27,\"administrationFee\":40,\"conclusionFee\":695,\"currency\":\"SEK\"}";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/loan/calculate");
        ResponseBody responseBody = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(400);
        assertThat(responseBody.jsonPath().getList("message").get(0)).isEqualTo("should be string");
    }

    @Test
    public void postPayloadReturns409WhenAmountIsString() {
        String requestBody = "{\"maturity\":120,\"productType\":\"LOANSE02\",\"amount\":\"test\",\"interestRate\":10.95,\"monthlyPaymentDay\":27,\"administrationFee\":40,\"conclusionFee\":695,\"currency\":\"SEK\"}";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/loan/calculate");
        ResponseBody responseBody = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(400);
        assertThat(responseBody.jsonPath().getList("message").get(0)).isEqualTo("should be number");
    }

    @Test
    public void postPayloadReturns409WhenInterestRateIsString() {
        String requestBody = "{\"maturity\":120,\"productType\":\"LOANSE02\",\"amount\":97350,\"interestRate\":\"test\",\"monthlyPaymentDay\":27,\"administrationFee\":40,\"conclusionFee\":695,\"currency\":\"SEK\"}";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/loan/calculate");
        ResponseBody responseBody = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(400);
        assertThat(responseBody.jsonPath().getList("message").get(0)).isEqualTo("should be number");
    }

    @Test
    public void postPayloadReturns409WhenMonthlyPaymentDayIsString() {
        String requestBody = "{\"maturity\":120,\"productType\":\"LOANSE02\",\"amount\":97350,\"interestRate\":10.95,\"monthlyPaymentDay\":\"test\",\"administrationFee\":40,\"conclusionFee\":695,\"currency\":\"SEK\"}";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/loan/calculate");
        ResponseBody responseBody = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(400);
        assertThat(responseBody.jsonPath().getList("message").get(0)).isEqualTo("should be number");
    }

    @Test
    public void postPayloadConvertsFloatMonthlyPaymentDayToInteger() {
        String requestBodyFloat = "{\"maturity\":120,\"productType\":\"LOANSE02\",\"amount\":97350,\"interestRate\":10.95,\"monthlyPaymentDay\":27.6,\"administrationFee\":40,\"conclusionFee\":695,\"currency\":\"SEK\"}";
        Response responseFloat = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBodyFloat)
                .post("/loan/calculate");
        ResponseBody responseBodyFloat = responseFloat.getBody();
        assertThat(responseFloat.getStatusCode()).isEqualTo(200);

        String requestBodyInt = "{\"maturity\":120,\"productType\":\"LOANSE02\",\"amount\":97350,\"interestRate\":10.95,\"monthlyPaymentDay\":27,\"administrationFee\":40,\"conclusionFee\":695,\"currency\":\"SEK\"}";
        Response responseInt = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBodyInt)
                .post("/loan/calculate");
        ResponseBody responseBodyInt = responseInt.getBody();
        assertThat(responseInt.getStatusCode()).isEqualTo(200);

        String expectedOutput = "{\"totalRepayableAmount\":165498.99,\"monthlyPayment\":1379.16,\"apr\":12.7}";

        assertThat(responseBodyInt.asString()).isEqualTo(expectedOutput);
        assertThat(responseBodyFloat.asString()).isEqualTo(expectedOutput);
    }

    @Test
    public void postPayloadReturns409WhenAdministrationFeeIsString() {
        String requestBody = "{\"maturity\":120,\"productType\":\"LOANSE02\",\"amount\":97350,\"interestRate\":10.95,\"monthlyPaymentDay\":27,\"administrationFee\":\"SEK\",\"conclusionFee\":695,\"currency\":\"SEK\"}";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/loan/calculate");
        ResponseBody responseBody = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(400);
        assertThat(responseBody.jsonPath().getList("message").get(0)).isEqualTo("should be number");
    }

    @Test
    public void postPayloadReturns409WhenConclusionFeeIsString() {
        String requestBody = "{\"maturity\":120,\"productType\":\"LOANSE02\",\"amount\":97350,\"interestRate\":10.95,\"monthlyPaymentDay\":27,\"administrationFee\":40,\"conclusionFee\":\"test\",\"currency\":\"SEK\"}";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/loan/calculate");
        ResponseBody responseBody = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(400);
        assertThat(responseBody.jsonPath().getList("message").get(0)).isEqualTo("should be number");
    }

    @Test
    public void postPayloadReturns409WhenCurrencyIsInteger() {
        String requestBody = "{\"maturity\":120,\"productType\":\"LOANSE02\",\"amount\":97350,\"interestRate\":10.95,\"monthlyPaymentDay\":27,\"administrationFee\":40,\"conclusionFee\":695,\"currency\":27}";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/loan/calculate");
        ResponseBody responseBody = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(400);
        assertThat(responseBody.jsonPath().getList("message").get(0)).isEqualTo("should be string");
    }

}

