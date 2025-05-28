package daniil.dobris.authorizationbot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class TelegramUserEntity {

    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String photoUrl;
}
