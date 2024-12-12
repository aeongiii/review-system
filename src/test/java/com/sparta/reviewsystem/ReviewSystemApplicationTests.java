package com.sparta.reviewsystem;

import com.sparta.reviewsystem.entity.Product;
import com.sparta.reviewsystem.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // MockMvc 객체 자동으로 구성
class ReviewSystemApplicationTests {

    @Autowired private MockMvc mvc; // MockMvc 객체 주입받음

    @Autowired private ProductRepository productRepository;

    private Long productId;

    @BeforeEach
    void setUp() { // 테스트용 데이터 생성
        Product product = new Product(); // ID는 자동 생성
        product.setReviewCount(0L); // 리뷰 개수
        product.setScore(0f); // float 타입 점수

        Product savedProduct = productRepository.save(product);

        productId = savedProduct.getId(); // 저장된 ID를 저장
    }

    @Transactional
    @Test
    @DisplayName("@Post - 리뷰 등록 성공 테스트")
    void successCreateReview() throws Exception{

        // 요청 데이터를 Json으로 직접 작성
        String requestDto =
"""
{
	"userId": 1,
	"score": 4,
	"content": "이걸 사용하고 제 인생이 달라졌습니다."
}
""";

        // Json파일과 이미지파일 만들기
        MockMultipartFile requestFile = new MockMultipartFile(
                "requestDto", // 서버에서 받을 필드명
                "", // Json데이터의 경우 파일 이름을 지정하지 않는다.
                "application/json", // 데이터 형식
                requestDto.getBytes() // JSON 데이터를 바이트로 변환
        );

        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile",
                "image1.png",
                "image/png",
                "dummy image context".getBytes()
        );

        // POST 요청 전송
        mvc.perform(MockMvcRequestBuilders.multipart("/products/" + productId + "/reviews")
                .file(requestFile) // JSON 데이터 포함
                .file(imageFile) // 이미지 파일 포함
                .contentType(MediaType.MULTIPART_FORM_DATA)) // Content-type 뭘로 처리할건지 설정
                .andExpect(status().isOk()) // HTTP 200(OK) 응답 기대
                .andDo(print()); // 응답 내용 출력


    }

}
