package com.telecom.telecom_service_provisioning.service.implementations;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.telecom.telecom_service_provisioning.dto.AvailedServices;
import com.telecom.telecom_service_provisioning.dto.UserDetailsDto;
import com.telecom.telecom_service_provisioning.model.InternetServiceAvailed;
import com.telecom.telecom_service_provisioning.model.PendingRequest;
import com.telecom.telecom_service_provisioning.model.TvServiceAvailed;
import com.telecom.telecom_service_provisioning.model.User;
import com.telecom.telecom_service_provisioning.repository.PendingRequestRepository;

@Service
public class UserService {

    @Autowired
    private AvailedInternetServiceManager availedInternetService;

    @Autowired
    private AuthenticationServiceImpl authService;

    @Autowired
    private AvailedTvServiceManager availedTvService;

    @Autowired
    private PendingRequestRepository pendingRequestRepo;

    @Autowired
    private FeedbackService feedbackService;    

    public AvailedServices getAllSubscribedServices() {
        Integer userId = authService.getCurrentUserDetails().getUserId();
        java.util.List<InternetServiceAvailed> availedInternetServices = availedInternetService.getActiveSubscribedServices(userId);
        java.util.List<TvServiceAvailed> availedTvServices = availedTvService.getActiveSubscribedServices(userId);
        
        AvailedServices availedServices = new AvailedServices();
        if (availedInternetServices.isEmpty() && availedTvServices.isEmpty()) {
            return availedServices;
        }
        availedServices.setInternetServicesAvailed(availedInternetServices); 
        availedServices.setTvServicesAvailed(availedTvServices); 
        return availedServices;
    }

    public void deactivateInternetService(Integer availedServiceId, LocalDate startDate) throws Exception {
        availedInternetService.deactivateService(availedServiceId, startDate);
        //If he has any services in q, activate it
    }

    public void deactivateTvService(Integer availedServiceId, LocalDate startDate)throws Exception{
        availedTvService.deactivateService(availedServiceId, startDate);
        //If he has any services in q, activate it
    }

    public UserDetailsDto getUserDetails() {
        UserDetailsDto user = new UserDetailsDto();
        User cur = authService.getCurrentUserDetails();
        user.setEmail(cur.getEmail());
        user.setUsername(cur.getUsername());
        user.setUserRole(cur.getRole());
        user.setAddress(cur.getAddress());
        user.setPhonenumber(cur.getPhonenumber());
        return user;
    }

    public List<PendingRequest> getAllPendingRequests() {
        Integer userId = authService.getCurrentUserDetails().getUserId();
        return pendingRequestRepo.findByUserId(userId);
    }

    public void createTvServiceFeedback(Integer availedTvServiceId, String feedback) throws Exception {
        feedbackService.createTvServiceFeedback(availedTvServiceId, feedback);
    }

    public void createInternetServiceFeedback(Integer availedInternetServiceId, String feedback) throws Exception {
        feedbackService.createInternetServiceFeedback(availedInternetServiceId, feedback);
    }
    
}
