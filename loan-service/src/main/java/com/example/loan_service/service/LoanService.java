package com.example.loan_service.service;

import com.example.loan_service.entities.LoanEntity;
import com.example.loan_service.entities.LoanToolEntity;
import com.example.loan_service.models.GetClient;
import com.example.loan_service.models.Kardex;
import com.example.loan_service.models.LoanResponse;
import com.example.loan_service.models.Price;
import com.example.loan_service.models.Tool;
import com.example.loan_service.repository.LoanRepository;
import com.example.loan_service.repository.LoanToolRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanToolRepository loanToolRepository;

    @Autowired
    RestTemplate restTemplate;

    // Find Loan
    public ArrayList<LoanEntity> getLoans() {
        return (ArrayList<LoanEntity>) loanRepository.findAll();
    }

    // Create Loan with conditions (migrated from monolith to microservices)
    public LoanEntity saveLoan(LoanEntity loanEntity, List<Long> toolIds) throws Exception {
        // 1. Get client and verify if it exists
        Long idClient = loanEntity.getIdClient();
        String clientUrl = String.format("http://client-service/client/%d", idClient);
        GetClient client = restTemplate.getForObject(clientUrl, GetClient.class);

        if (client == null) {
            throw new Exception("Cliente no encontrado");
        }

        // 2. Verify if client is restricted
        if ("RESTRINGIDO".equals(client.getStateClient())) {
            throw new Exception("El cliente se encuentra restringido");
        }

        // 3. Get all loans of client and verify if it has debt
        List<LoanEntity> loans = loanRepository.findByIdClient(idClient);
        boolean hasDebt = false;
        int activeLoansCount = 0;

        for (LoanEntity l : loans) {
            // Count active loans
            if ("ACTIVO".equals(l.getStateLoan())) {
                activeLoansCount++;
            }
            // Check for debts
            int penalty = l.getPenaltyLoan();
            if (penalty > 0 && "ACTIVO".equals(l.getStateLoan())) {
                hasDebt = true;
            }
        }

        // 4. Verify if client has 5 active loans
        if (activeLoansCount >= 5) {
            throw new Exception("El cliente ya tiene 5 prestamos activos");
        }

        // 5. Verify if client has debt
        if (hasDebt) {
            throw new Exception("El cliente tiene deudas pendientes");
        }

        // 6. Verify if end date is before start date
        Date initDate = loanEntity.getInitDate();
        Date endDate = loanEntity.getEndDate();

        if (endDate.before(initDate)) {
            throw new Exception("La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        // 7. Calculate days of loan
        java.time.LocalDate start = initDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        java.time.LocalDate end = endDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        long days = java.time.temporal.ChronoUnit.DAYS.between(start, end) + 1;

        // 8. Get all tools and verify if they exist
        List<Tool> completeTools = new ArrayList<>();
        for (Long toolId : toolIds) {
            String toolUrl = String.format("http://tool-service/tool/%d", toolId);
            Tool tool = restTemplate.getForObject(toolUrl, Tool.class);

            if (tool == null) {
                throw new Exception("Herramienta no encontrada con id: " + toolId);
            }
            completeTools.add(tool);
        }

        // 9. Calculate total of loan and update stock of tools
        int total = 0;
        HttpHeaders toolHeaders = new HttpHeaders();
        toolHeaders.setContentType(MediaType.APPLICATION_JSON);

        for (Tool tool : completeTools) {
            // Update stock of tool using PUT endpoint
            tool.setStockTool(tool.getStockTool() - 1);

            // Update state if stock is 0
            if (tool.getStockTool() == 0) {
                tool.setStateTool("BAJA");
            }

            // Call PUT endpoint to update tool
            String toolUpdateUrl = "http://tool-service/tool/";
            org.springframework.http.HttpEntity<Tool> toolRequest = new org.springframework.http.HttpEntity<>(tool,
                    toolHeaders);
            restTemplate.put(toolUpdateUrl, toolRequest);

            // Add daily charge to total
            total += tool.getDailyCharge();

            // Create kardex for each tool
            Kardex kardex = new Kardex();
            kardex.setDateKardex(new Date());
            kardex.setIdTool(tool.getIdTool());
            kardex.setNameTool(tool.getNameTool());
            kardex.setStateTool("PRESTAMO");

            // Save kardex via POST endpoint
            String kardexUrl = "http://kardex-service/kardex/";
            HttpHeaders kardexHeaders = new HttpHeaders();
            kardexHeaders.setContentType(MediaType.APPLICATION_JSON);
            org.springframework.http.HttpEntity<Kardex> kardexRequest = new org.springframework.http.HttpEntity<>(
                    kardex, kardexHeaders);
            restTemplate.postForObject(kardexUrl, kardexRequest, Kardex.class);
        }

        // 10. Calculate total price
        total = Math.toIntExact(total * days);

        // 11. Set loan variables
        loanEntity.setHourLoan(java.time.LocalTime.now());
        loanEntity.setStateLoan("ACTIVO");

        // 12. Save loan first to get the ID
        LoanEntity savedLoan = loanRepository.save(loanEntity);

        // 13. Create price in price-service
        Price priceObj = new Price();
        priceObj.setIdLoan(savedLoan.getIdLoan());
        priceObj.setPrice(total);

        String priceUrl = "http://price-service/price/";
        HttpHeaders priceHeaders = new HttpHeaders();
        priceHeaders.setContentType(MediaType.APPLICATION_JSON);
        org.springframework.http.HttpEntity<Price> priceRequest = new org.springframework.http.HttpEntity<>(priceObj,
                priceHeaders);
        Price createdPrice = restTemplate.postForObject(priceUrl, priceRequest, Price.class);

        // 14. Update loan with price ID
        if (createdPrice != null) {
            savedLoan.setIdPrice(createdPrice.getIdPrice());
            savedLoan = loanRepository.save(savedLoan);
        }

        // 15. Create LoanTool associations
        for (Long toolId : toolIds) {
            LoanToolEntity loanTool = new LoanToolEntity();
            loanTool.setIdLoan(savedLoan.getIdLoan());
            loanTool.setIdTool(toolId);
            loanToolRepository.save(loanTool);
        }

        return savedLoan;
    }

    // Find Loan by Id
    public Optional<LoanEntity> findById(Long id) {
        return loanRepository.findById(id);
    }
    
    // Find Loan by Id with tools and total (enriched)
    public LoanResponse findByIdEnriched(Long id) {
        Optional<LoanEntity> loanOpt = loanRepository.findById(id);
        
        if (!loanOpt.isPresent()) {
            return null;
        }
        
        LoanEntity loan = loanOpt.get();
        List<Tool> tools = findToolById(loan.getIdLoan());
        
        // Calculate total from tool prices
        int total = tools.stream()
                .mapToInt(Tool::getDailyCharge)
                .sum();
        
        // Calculate days between initDate and endDate
        long diffMillis = loan.getEndDate().getTime() - loan.getInitDate().getTime();
        long days = diffMillis / (1000 * 60 * 60 * 24) + 1;
        
        total = (int) (total * days);
        
        // Create response DTO
        LoanResponse loanResponse = new LoanResponse();
        loanResponse.setIdLoan(loan.getIdLoan());
        loanResponse.setInitDate(loan.getInitDate());
        loanResponse.setHourLoan(loan.getHourLoan());
        loanResponse.setEndDate(loan.getEndDate());
        loanResponse.setStateLoan(loan.getStateLoan());
        loanResponse.setPenaltyLoan(loan.getPenaltyLoan());
        loanResponse.setIdPrice(loan.getIdPrice());
        loanResponse.setIdClient(loan.getIdClient());
        loanResponse.setTool(tools);
        loanResponse.setTotalLoan(total);
        
        return loanResponse;
    }

    // Update Loan
    public LoanEntity updateLoan(LoanEntity loanEntity) {
        return loanRepository.save(loanEntity);
    }

    // Delete Loam by id
    public boolean deleteLoan(Long id) throws Exception {
        try {
            loanRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // Loans by rut of client
    public List<LoanResponse> findByRutClient(String rut) {

        String url = String.format("http://client-service/client/by-rut/%s", rut);
        GetClient client = restTemplate.getForObject(url, GetClient.class);
        Long idClient = client.getIdClient();

        List<LoanEntity> loans = loanRepository.findByIdClient(idClient);
        List<LoanResponse> response = new ArrayList<>();
        
        System.out.println("DEBUG: Processing " + loans.size() + " loans");
        
        // Enrich each loan with tools and total price
        for (LoanEntity loan : loans) {
            List<Tool> tools = findToolById(loan.getIdLoan());
            
            System.out.println("DEBUG: Found " + tools.size() + " tools for loan " + loan.getIdLoan());
            
            // Calculate total from tool prices
            int total = tools.stream()
                    .mapToInt(Tool::getDailyCharge)
                    .sum();
            
            // Calculate days between initDate and endDate
            long diffMillis = loan.getEndDate().getTime() - loan.getInitDate().getTime();
            long days = diffMillis / (1000 * 60 * 60 * 24) + 1;
            
            total = (int) (total * days);
            
            System.out.println("DEBUG: Calculated total: " + total + " for " + days + " days");
            
            // Create response DTO
            LoanResponse loanResponse = new LoanResponse();
            loanResponse.setIdLoan(loan.getIdLoan());
            loanResponse.setInitDate(loan.getInitDate());
            loanResponse.setHourLoan(loan.getHourLoan());
            loanResponse.setEndDate(loan.getEndDate());
            loanResponse.setStateLoan(loan.getStateLoan());
            loanResponse.setPenaltyLoan(loan.getPenaltyLoan());
            loanResponse.setIdPrice(loan.getIdPrice());
            loanResponse.setIdClient(loan.getIdClient());
            loanResponse.setTool(tools);
            loanResponse.setTotalLoan(total);
            
            System.out.println("DEBUG: LoanResponse created: " + loanResponse);
            
            response.add(loanResponse);
        }
        
        System.out.println("DEBUG: Returning " + response.size() + " loan responses");
        
        return response;
    }

    // Get tool by id loan
    public List<Tool> findToolById(Long id) {
        List<Long> idsTool = loanToolRepository.findAllByIdLoan(id);

        List<Tool> tools = idsTool.stream()
                .map(idTool -> {
                    String url = String.format("http://tool-service/tool/%d", idTool);
                    return restTemplate.getForObject(url, Tool.class);
                })
                .toList();

        return tools;
    }

    // Get all loans by date range
    public List<LoanEntity> getLoansByDateRange(Date startDate, Date endDate) throws Exception {
        if (startDate == null || endDate == null) {
            throw new Exception("Las fechas no pueden ser nulas");
        }
        if (endDate.before(startDate)) {
            throw new Exception("La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        List<LoanEntity> allLoans = loanRepository.findAll();
        List<LoanEntity> filteredLoans = new ArrayList<>();

        for (LoanEntity loan : allLoans) {
            Date initDate = loan.getInitDate();
            if (initDate != null && !initDate.before(startDate) && !initDate.after(endDate)) {
                filteredLoans.add(loan);
            }
        }
        return filteredLoans;
    }

    @Transactional
    public LoanEntity updatePenaltyLoan(Long id) {
        // Search loan by id
        LoanEntity loanEntity = loanRepository.findByIdLoan(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + id));
        Long idClient = loanEntity.getIdClient();

        // Get client of loan
        String url = String.format("http://client-service/client/%d", idClient);
        GetClient client = restTemplate.getForObject(url, GetClient.class);

        // Search finish date of loan
        Date finishDate = loanEntity.getEndDate();
        // Search today date
        Date todayDate = new Date();

        // Verify if loan is finished
        if (loanEntity.getStateLoan().equals("FINALIZADO")) {
            return loanEntity;
        }

        // Verify if loan is finished and update penalty
        if (finishDate.before(todayDate)) {
            long diffMillis = todayDate.getTime() - finishDate.getTime();
            long diffDays = diffMillis / (1000 * 60 * 60 * 24);

            // Get all tools of loan
            List<Long> idsTool = loanToolRepository.findAllByIdLoan(id);
            List<Tool> tools = idsTool.stream()
                    .map(idTool -> {
                        String url2 = String.format("http://tool-service/tool/%d", idTool);
                        return restTemplate.getForObject(url2, Tool.class);
                    })
                    .toList();

            // Calculate total penalty
            int totalPenalty = 0;
            for (Tool tool : tools) {
                totalPenalty += tool.getLateCharge() * diffDays;
            }

            // Update state client
            client.setStateClient("RESTRINGIDO");
            String updateUrl = "http://client-service/client/";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            org.springframework.http.HttpEntity<GetClient> requestEntity = new org.springframework.http.HttpEntity<>(
                    client, headers);
            restTemplate.put(updateUrl, requestEntity);

            // Update loan
            loanEntity.setPenaltyLoan(totalPenalty);
            loanRepository.save(loanEntity);
            return loanEntity;
        }

        return loanEntity;
    }

    // Finalize loan with conditions
    public LoanEntity finalizeLoan(Long id, int totalValueLoan) {
        Kardex kardex = new Kardex();
        kardex.setDateKardex(new java.util.Date());

        // Search loan by id
        LoanEntity loanEntity = loanRepository.findByIdLoan(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + id));

        Long idClient = loanEntity.getIdClient();

        // Get client of loan
        String url = String.format("http://client-service/client/%d", idClient);
        GetClient client = restTemplate.getForObject(url, GetClient.class);

        // Get all tools of loan
        List<Long> idsTool = loanToolRepository.findAllByIdLoan(id);
        List<Tool> tools = idsTool.stream()
                .map(idTool -> {
                    String url2 = String.format("http://tool-service/tool/%d", idTool);
                    return restTemplate.getForObject(url2, Tool.class);
                })
                .toList();

        // Update stock tool and save kardex
        for (Tool tool : tools) {
            // Update stock tool and state tool if is needed
            tool.setStockTool(tool.getStockTool() + 1);
            if (tool.getStockTool() == 1) {
                tool.setStateTool("ACTIVA");
            }

            // Call PUT endpoint of tool-service to update tool
            String toolUpdateUrl = "http://tool-service/tool/";
            HttpHeaders toolHeaders = new HttpHeaders();
            toolHeaders.setContentType(MediaType.APPLICATION_JSON);
            org.springframework.http.HttpEntity<Tool> toolRequest = new org.springframework.http.HttpEntity<>(tool,
                    toolHeaders);
            restTemplate.put(toolUpdateUrl, toolRequest);

            // Prepare kardex for this tool
            kardex.setIdTool(tool.getIdTool());
            kardex.setNameTool(tool.getNameTool());
            kardex.setStateTool("DEVOLUCIÓN");

            // Call POST endpoint of kardex-service to save kardex
            String kardexSaveUrl = "http://kardex-service/kardex/";
            HttpHeaders kardexHeaders = new HttpHeaders();
            kardexHeaders.setContentType(MediaType.APPLICATION_JSON);
            org.springframework.http.HttpEntity<Kardex> kardexRequest = new org.springframework.http.HttpEntity<>(
                    kardex, kardexHeaders);
            restTemplate.postForObject(kardexSaveUrl, kardexRequest, Kardex.class);
        }

        // Verify if client has debt
        if (client.getStateClient().equals("RESTRINGIDO")) {
            boolean hasDebt = false;
            // Get all loans of this client from local repository
            List<LoanEntity> loans = loanRepository.findByIdClient(idClient);
            for (LoanEntity l : loans) {
                int penalty = l.getPenaltyLoan();
                // Fix: use .equals() instead of == for String comparison
                if (penalty > 0 && "ACTIVO".equals(l.getStateLoan())) {
                    hasDebt = true;
                    break;
                }
            }
            // If client has no debt, change state to ACTIVO
            if (!hasDebt) {
                client.setStateClient("ACTIVO");
                // Update client through client-service endpoint
                String clientUpdateUrl = "http://client-service/client/";
                HttpHeaders clientHeaders = new HttpHeaders();
                clientHeaders.setContentType(MediaType.APPLICATION_JSON);
                org.springframework.http.HttpEntity<GetClient> clientRequest = new org.springframework.http.HttpEntity<>(
                        client, clientHeaders);
                restTemplate.put(clientUpdateUrl, clientRequest);
            }
        }

        // Create price in price-service instead of saving it in loan entity
        Price priceObj = new Price();
        priceObj.setIdLoan(id);
        priceObj.setPrice(totalValueLoan);

        String priceUrl = "http://price-service/price/";
        HttpHeaders priceHeaders = new HttpHeaders();
        priceHeaders.setContentType(MediaType.APPLICATION_JSON);
        org.springframework.http.HttpEntity<Price> priceRequest = new org.springframework.http.HttpEntity<>(priceObj,
                priceHeaders);
        restTemplate.postForObject(priceUrl, priceRequest, Price.class);

        // Update loan status to FINALIZADO
        loanEntity.setStateLoan("FINALIZADO");
        return loanRepository.save(loanEntity);

    }

    ////////////////////// LOAN TOOL //////////////////////

    // Create LoanTool association
    public LoanToolEntity saveLoanTool(LoanToolEntity loanToolEntity) {
        return loanToolRepository.save(loanToolEntity);
    }

    // Get all LoanTools
    public ArrayList<LoanToolEntity> getAllLoanTools() {
        return (ArrayList<LoanToolEntity>) loanToolRepository.findAll();
    }

    // Delete LoanTool by id
    public boolean deleteLoanTool(Long id) throws Exception {
        try {
            loanToolRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}