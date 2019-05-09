package TrelloApiAutomation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class RestAPITest extends BoardBaseTest {

    String keyID = "";
    String tokenID = "";
    String listname;

    @Test(priority = 1)
    public void getBoardLists() {

        Response response = given().queryParam("key", "")
                .queryParam("token", "")
                .pathParam("id", boardId)
                .when().get("1/boards/{id}/lists/");


        response.then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract();

        System.out.println(response.statusCode());

        List<Map<String, ?>> boardlist = response.jsonPath().getList("$");

        Assert.assertEquals(response.getStatusCode(), 200);
        System.out.println("Response code of get list --> " + response.statusCode());


        for (int i = 0; i < boardlist.size(); i++) {
            System.out.println(boardlist.get(i).get("id").toString());
            System.out.println(boardlist.get(i).get("name").toString());

        }

        listId = boardlist.get(0).get("id").toString();
        String listname = boardlist.get(0).get("name").toString();
        System.out.println("List ID: " + listId);
        System.out.println("List Name: " + listname);


    }

    @Test(priority = 2)
    public void createCard() {

        RequestSpecification requestSpecification = given()
                .queryParam("key", keyID)
                .queryParam("token", tokenID)
                .queryParam("name", "TestCard")
                .queryParam("idList", listId)
                .contentType(ContentType.JSON)
                .log().all();


        Response response = requestSpecification.when()
                .post("1/cards");
        //System.out.println("Response body after creating card--> " + response.body());

        //System.out.println("Response code from create card -->" + response.statusCode());

        response.then()
                .statusCode(200);

        Assert.assertEquals(response.getStatusCode(), 200);
        //Assert.assertEquals(response.get);

        Map<Object,Object> map = response.jsonPath().getMap("$");
        System.out.println("Card ID: " + map.get("id"));
        System.out.println("Card Name: " + map.get("name"));
        String cardId = map.get("id").toString();
        System.out.println("Card Id = " + cardId);

    }


    @Test(priority = 3)
    public void updateCard(){

        System.out.println("Card Id:" +cardId);
        RequestSpecification requestSpecification = given()
                .pathParam("id",cardId)
                .queryParam("key", keyID)
                .queryParam("token", tokenID)
                .queryParam("name", "Modified Card")
                .log().all();


        Response response = requestSpecification.when()
                .put("1/cards/{id}");
        System.out.println("Response body after updating the card:"+ response.body());

        System.out.println("Response code from update card:" + response.statusCode());

        response.then()
                .statusCode(200) ;

        Assert.assertEquals(response.getStatusCode(),200);

        Map<Object, Object> map = response.jsonPath().getMap("$");
        System.out.println("Card ID: " + map.get("id"));
        System.out.println("Card Name: " + map.get("name"));
        cardId = map.get("id").toString();
        System.out.println("Card Id = "+ cardId);


    }


    @Test(priority = 4)
    public void deleteteCard(){

        System.out.println("CardId = "+ cardId);
        RequestSpecification requestSpecification = given()
                .pathParam("id",cardId)
                .queryParam("key", keyID)
                .queryParam("token", tokenID)
                .log().all();


        Response response = requestSpecification.when()
                .delete("1/cards/{id}");

        response.then()
                .statusCode(200) ;
        Assert.assertEquals(response.getStatusCode(),200);

        System.out.println("Response body after deleting the card: "+ response.body());

        System.out.println("Response code from delete card :" + response.statusCode());


    }

}