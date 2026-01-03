package com.lbytech.lbytech_backend_new;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.lbytech.lbytech_backend_new.pojo.entity.User;
import com.lbytech.lbytech_backend_new.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class LbytechBackendNewApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private IUserService userService;

    @Autowired
    private MockMvc mockMvc;

    private final String SALT = "lbytech";

    @Value("${test.password}")
    private String TEST_PASSWORD;

    /**
     * 添加50000个测试用户
     */
    @Test
    void addUser() {
        for (int i = 0; i < 50000; i++) {
            String encryptedPassword = DigestUtil.md5Hex(TEST_PASSWORD + SALT);
            User user = new User("test" + i + "@qq.com", encryptedPassword, LocalDateTime.now());
            userService.save(user);
        }
    }

    @Test
    void testLoginAndExportTokenToCsv() throws Exception {
        List<User> list = userService.list();
        list = list.stream()
                .filter(u -> u.getId() >= 10009 && u.getId() <= 60008)
                .collect(Collectors.toList());

        try (PrintWriter writer = new PrintWriter(new FileWriter("token_output.csv", false))) {
            // 表头
            writer.println("userEmail,token,timestamp");

            for (User user : list) {
                String testUseEmail = user.getEmail();

                Map<String, String> requestBody = Map.of(
                        "email", testUseEmail,
                        "password", TEST_PASSWORD
                );
                String requestJson = JSONUtil.toJsonStr(requestBody);
                MvcResult result = mockMvc.perform(post("/user/loginByPassword")
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

                String content = result.getResponse().getContentAsString();
                assertThat(content).isNotEmpty();

                String data = JSONUtil.parseObj(content).getStr("data");
                String token = JSONUtil.parseObj(data).getStr("token");

                writer.printf("%s,%s,%s%n", testUseEmail, token, LocalDateTime.now());

                System.out.println("写入 CSV：" + testUseEmail + " -> " + token);
            }
        }
    }

}