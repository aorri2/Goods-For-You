package com.aorri2.goodsforyou.product.api;

import org.springframework.http.MediaType;

import com.aorri2.goodsforyou.product.presentation.request.ProductRequest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class ProductSteps {
	public static ExtractableResponse<Response> 상품_생성_요청(ProductRequest productRequest) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(productRequest)
			.when()
			.post(ProductApiTest.ADD_PRODUCT_PATH)
			.then()
			.log().all().extract();
	}
}
