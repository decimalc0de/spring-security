package com.decimalcode.qmed.api.token;

import com.decimalcode.qmed.api._repositories.ITokenRepository;
import com.decimalcode.qmed.api.users.services.UserEntity;
import com.decimalcode.qmed.api._repositories.IUserRepository;
import com.decimalcode.qmed.response.ApiResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.decimalcode.qmed.exception.custom.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class TokenServiceImpl implements ITokenService {

    private final ITokenRepository tokenRepository;
    private final IUserRepository membersRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TokenServiceImpl(ModelMapper modelMapper,
                            IUserRepository membersRepository,
                            ITokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
        this.membersRepository = membersRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public void persistToken(UserEntity appUser, String token) {
        try{
            TokenEntity tokenEntity = new TokenEntity(appUser, token);
            tokenRepository.save(tokenEntity);
        }
        catch (Exception exception) { throw new ApiException("TokenServiceImpl.persistToken : " + exception.getMessage()); }
    }

    @Override
    @Transactional
    public ApiResponse<TokenEntity> confirmRegistrationEmailToken(UserEntity appUser, String token) {
        String message = "Email confirmation token is either broken or expired";
        try{
            OffsetDateTime now = OffsetDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
            Optional<TokenEntity> optionalToken =
                    tokenRepository.findFirstByAppUserAndTokenAndExpiryTime(appUser, token, now);
            if(optionalToken.isPresent()) {
                message = "Your account is successfully confirmed, please proceed to Login page";
                UserEntity user = optionalToken.get().getAppUser();
                user.setEnabled(true);
                membersRepository.save(user);
                return new ApiResponse<>(message, optionalToken.get());
            }
            throw new ApiException(message);
        }
        catch (Exception exception) { throw new ApiException(exception.getMessage()); }
    }

}
