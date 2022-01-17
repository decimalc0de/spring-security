package com.decimalcode.qmed.api.token;

import com.decimalcode.qmed.api._repositories.ITokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Service
@Transactional
public class PurgeToken {

    private final ITokenRepository tokenRepository;

    @Autowired
    public PurgeToken(ITokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Scheduled(cron = "0 1 1,13 * * ?", zone = "CST")
    public void deleteExpiredToken() {
        OffsetDateTime now = OffsetDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        tokenRepository.deleteAllExpiredToken(now);
    }

}
