package com.example.report_service.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetClient {
    private Long idClient;
    private String rutClient;
    private String nameClient;
    private String stateClient;
    private String emailClient;
    private String phoneNumberClient;
}
