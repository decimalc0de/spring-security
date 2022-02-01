package com.decimalcode.qmed.api.__default;

import com.decimalcode.qmed.api.counsel.service.CounselModel;
import com.decimalcode.qmed.api.counsel.service.CounselService;
import com.decimalcode.qmed.config.ApplicationContextImpl;
import com.decimalcode.qmed.exception.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

/**
 * Use the class to register default Counselling
 */
@Component
public class DefaultCounselling implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;
    private final CounselService counselService;
    private final ResourceLoader resourceLoader;

    @Autowired
    public DefaultCounselling(CounselService counselService,
                              ResourceLoader resourceLoader) {
        this.counselService = counselService;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) return;
        try
        {
            /*
             * Initialize Mapper
             */
            ObjectMapper objectMapper = ApplicationContextImpl.getBean(ObjectMapper.class);
            /*
             * Read Json file
             */
            Resource resource = resourceLoader.getResource(
                "classpath:templates/default-diagnosis.json"
            );
            InputStream dbAsStream = resource.getInputStream();
            /*
             * Map Json file content to Counsel-Model
             */
            CounselModel[] counselModel = objectMapper.readValue(dbAsStream, CounselModel[].class);
            System.out.println("counselModel " + counselModel[0].dosage().size());
            /*
             * Persist Counsel-Model
             */
            counselService.putAllCounsel(List.of(counselModel));
        }
        catch (Exception e) { throw new ApiException("Internal server error, while initialing default values"); }

        alreadySetup = true;
    }
}
