package daniil.dobris.authorizationbot.service;

import daniil.dobris.authorizationbot.entity.TelegramUserEntity;
import daniil.dobris.authorizationbot.dto.TelegramUser;
import daniil.dobris.authorizationbot.repository.TelegramUserRepository;
import org.springframework.stereotype.Service;

@Service
public class TelegramUserService {

    private final TelegramUserRepository telegramUserRepository;

    public TelegramUserService(TelegramUserRepository telegramUserRepository) {
        this.telegramUserRepository = telegramUserRepository;
    }

    public void saveOrUpdate(TelegramUser telegramUser) {
        TelegramUserEntity entity = new TelegramUserEntity();
        entity.setId(telegramUser.id());
        entity.setFirstName(telegramUser.firstName());
        entity.setLastName(telegramUser.lastName());
        entity.setUsername(telegramUser.userName());
        entity.setPhotoUrl(telegramUser.photoUrl());
        telegramUserRepository.save(entity);
    }
}
