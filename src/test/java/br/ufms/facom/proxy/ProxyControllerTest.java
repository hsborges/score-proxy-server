package br.ufms.facom.proxy;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import br.ufms.facom.proxy.utils.FakeGenerator;


@SpringBootTest
@AutoConfigureMockMvc
public class ProxyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldHandleConcurrentRequests() throws Exception {
        int threadCount = 20;

        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    MvcResult result = this.mockMvc.perform(get("/proxy/score")
                            .param("cpf", FakeGenerator.cpf())
                            .accept(MediaType.APPLICATION_JSON))
                        .andReturn();
                    int status = result.getResponse().getStatus();
                    if (status >= 200 && status < 300) successCount.incrementAndGet();
                } catch (Exception e) {
                    System.err.println(e);
                } finally {
                    latch.countDown();
                }
            }).start();
        }
        latch.await();
        assertThat(successCount.get()).isEqualTo(threadCount);
    }

    @Test
    void shouldCacheResponses() throws Exception {
        String cpf = FakeGenerator.cpf();
        long start = System.currentTimeMillis();
        long firstDuration = 0;
        for (int i = 0; i < 20; i++) {
            this.mockMvc.perform(get("/proxy/score")
                    .param("cpf", cpf)
                    .accept(MediaType.APPLICATION_JSON))
                .andReturn();
            firstDuration = System.currentTimeMillis() - start;
        }
        long duration = System.currentTimeMillis() - start;
        assertThat(duration).isLessThanOrEqualTo(firstDuration * 2);
    }

}
