package daniil.dobris.authorizationbot.repository;

import daniil.dobris.authorizationbot.entity.TelegramUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelegramUserRepository extends JpaRepository<TelegramUserEntity, Long> {
}
