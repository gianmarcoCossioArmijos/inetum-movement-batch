package com.inetum.movement_batch.writer;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import java.util.List;
import com.inetum.movement_batch.dto.RestResponse;

@Component
public class MovementWriter implements ItemWriter<RestResponse> {

    @Override
    public void write(List<? extends RestResponse> list) {
        list.forEach(resp -> {
            System.out.println("===== RESPONSE FROM REST ENDPOINT =====");
            System.out.println(resp);
            System.out.println("======================================");
        });
    }
}
