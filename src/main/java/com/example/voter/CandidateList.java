package com.example.voter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CandidateList {

    private Environment environment;

    private RabbitTemplate rabbitTemplate;

    private DirectExchange directExchange;

    @Autowired
    public CandidateList(Environment environment, RabbitTemplate rabbitTemplate, DirectExchange directExchange) {
        this.environment = environment;
        this.rabbitTemplate = rabbitTemplate;
        this.directExchange = directExchange;
    }


    List<String> getCandidatesSyncHttp(String election) {
        List<String> candidatesRemote = new ArrayList<>();
        String candidateServiceHostname = environment.getProperty("services.candidate.host");
        String candidateServicePort = environment.getProperty("services.candidate.port");
        String candidateServiceResourceUrl = String.format("http://%s:%s/candidates/summary?election=%s",
                candidateServiceHostname, candidateServicePort, election);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(candidateServiceResourceUrl, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(responseEntity.getBody());
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayNode candidatesArray = (ArrayNode) rootNode.get("candidates");

        for (int i = 0; i < candidatesArray.size(); i++) {
            JsonNode jsonNode = candidatesArray.get(i);
            if (jsonNode.isTextual()) {
                candidatesRemote.add(jsonNode.asText());
            }
        }

        List<String> candidatesSorted = candidatesRemote.subList(0, candidatesRemote.size());
        return candidatesSorted;
    }

    /**
     * Produces query message containing election
     * Consumes candidate list based on election query
     */
    @SuppressWarnings("unchecked")
    List<String> getCandidatesMessageRpc(String election) {
        System.out.println("Sending RPC request message for list of candidates...");
        String requestMessage = election; //"2016 Presidential Election";
        List<String> candidatesRemote;
        candidatesRemote = (List<String>) rabbitTemplate.convertSendAndReceive(
                directExchange.getName(),"rpc", requestMessage);

        for (String candidate : candidatesRemote) {
            System.out.println(candidate);
        }

        List<String> candidatesSorted = candidatesRemote.subList(0, candidatesRemote.size());
        return candidatesSorted;
    }
}
