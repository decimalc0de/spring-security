package com.decimalcode.qmed.api.counsel.service;

import com.decimalcode.qmed.api._repositories.ICounselRepository;
import com.decimalcode.qmed.exception.ApiException;
import com.decimalcode.qmed.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CounselService {
    private final ICounselRepository counselRepository;

    @Autowired
    public CounselService(ICounselRepository counselRepository) {
        this.counselRepository = counselRepository;
    }

    public ApiResponse<CounselModel> findDiagnosis(String diagnosis){
        Optional<CounselModel> optionalDiagnosis = counselRepository.findByName(diagnosis);
        if(optionalDiagnosis.isPresent()) {
            CounselModel counselModel = optionalDiagnosis.get();
            return new ApiResponse<>(
                    true,
                    "Found counselling by " + counselModel.counsellor(),
                    counselModel,
                    HttpStatus.OK.name(),
                    200
            );
        }
        throw new ApiException("No counsellor is available for this particular diagnosis");
    }

    public void putAllCounsel(List<CounselModel> counselModel) {
        counselRepository.saveAllAndFlush(counselModel);
    }

}
