package TrelloApiAutomation;

import com.jayway.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Map;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;


public class BoardBaseTest {

    String keyID = "";
    String tokenID = "";
    String boardId,carId,listId;


    @BeforeSuite
    public void setUp() {
        RestAssured.baseURI = "https://api.trello.com/";
         //String boardId = createBoard();
    }

    @BeforeTest
    public String createBoard() {
        RequestSpecification requestSpecification = given()
                .queryParam("key", keyID)
                .queryParam("token", tokenID)
                .queryParam("name", "Spreeboardtest").log().all()
                .contentType(ContentType.JSON);

        Response response = requestSpecification.when().
                post("1/boards/");
        System.out.println(response.body());

        response.then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath("createboard.jscon"))
                .extract()
                .response();


        Map<Object, Object> map = response.jsonPath().getMap("$");
        System.out.println("Board ID: " + map.get("id"));
        System.out.println("Board Name: " + map.get("name"));
        boardId = map.get("id").toString();
        System.out.println(boardId);
        //Assert.assertEquals(map.get("name").toString(), "Spreeboardtest");
        return boardId;
    }


    @AfterTest
    public void teardown() {

        RequestSpecification requestSpecification = given()
                .queryParam("key", keyID)
                .queryParam("token", tokenID)
                .pathParam("boardID", boardId)
                .contentType(ContentType.JSON);

        Response response = requestSpecification.when().
                delete("1/boards/{boardID}");

        response.then()
                .statusCode(200).log();
    }

}