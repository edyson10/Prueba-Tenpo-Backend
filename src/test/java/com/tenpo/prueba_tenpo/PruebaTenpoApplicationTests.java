package com.tenpo.prueba_tenpo;

import com.tenpo.prueba_tenpo.Client.PercentageClient;
import com.tenpo.prueba_tenpo.DTO.CalculationRequestDto;
import com.tenpo.prueba_tenpo.Service.Impl.CalculationServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PruebaTenpoApplicationTests {

    @Test
    void contextLoads() {
        PercentageClient client = () -> 10.0;
        CalculationServiceImpl service = new CalculationServiceImpl(client);

        var res = service.calculate(new CalculationRequestDto(5.0, 5.0));
        assertEquals(10.0, res.sum(), 0.0001);
        assertEquals(10.0, res.percentageApplied(), 0.0001);
        assertEquals(11.0, res.result(), 0.0001);
    }

}
