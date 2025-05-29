# Telegram WebApp Auth — Spring Boot + JWT

## Описание

Это веб-приложение на Spring Boot позволяет аутентифицировать пользователей через Telegram WebApp.

После входа через Telegram:
- Пользователь проверяется через `initData` (HMAC-подпись от Telegram)
- Ему выдается JWT токен
- Токен сохраняется в HttpOnly Cookie
- После этого пользователь может заходить на защищенные страницы (`/profile`)
- Пользователь сохраняется в базу данных (PostgreSQL)

---

## Что нужно изменить

### 1. Указать токен вашего Telegram бота

В файле `application.yml`:

```yaml
telegram:
  bot-token: "ВАШ_ТОКЕН_БОТА"


