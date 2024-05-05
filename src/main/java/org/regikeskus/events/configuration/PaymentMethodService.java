package org.regikeskus.events.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.StreamSupport;

@Getter
@Service
public class PaymentMethodService {

    private List<String> paymentMethods;

    public PaymentMethodService(List<String> paymentMethods) throws IOException {
        this.paymentMethods = paymentMethods;

        loadPaymentMethods();
    }

    private void loadPaymentMethods() throws IOException {
        InputStream input = getClass().getResourceAsStream("/payment-methods.json");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(input);
        paymentMethods = StreamSupport.stream(node.get("paymentMethods").spliterator(), false)
                .map(JsonNode::asText)
                .toList();  // Using Java 16 Stream.toList()
    }

}
