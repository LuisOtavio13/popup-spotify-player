# Como executar (rápido) 

Este guia mostra só como **rodar** a aplicação — backend (Spring Boot) + UI (popup) — em desenvolvimento e a partir de um artefato empacotado.

## Pré-requisitos
- Java 17 ou superior instalado (recomendado Java 21)
- Maven
- (Opcional para empacotar) JDK com jlink/jpackage para criar instaladores

## Variáveis de ambiente
Crie um arquivo `.env` na raiz do projeto com 3 variáveis:

```
SPOTIFY_CLIENT_ID=your_spotify_client_id_here
SPOTIFY_CLIENT_SECRET=your_spotify_client_secret_here
SPOTIFY_REDIRECT_URI=http://127.0.0.1:3000/callback
```

Carregar no Linux/macOS (na mesma sessão onde vai rodar a app):

```
export $(cat .env | xargs)
# ou
set -a && source .env && set +a
```

No PowerShell (Windows):

```
$env:SPOTIFY_CLIENT_ID = "your_spotify_client_id_here"
$env:SPOTIFY_CLIENT_SECRET = "..."
$env:SPOTIFY_REDIRECT_URI = "http://127.0.0.1:3000/callback"
```

> Observação: `.env` já está em `.gitignore` — **não** comite segredos.

---

## Rodar em modo desenvolvimento (recomendado)
A partir da raiz do projeto:

```
# com .env carregado
mvn -DskipTests spring-boot:run
```

Isso inicia o backend e a UI Swing é iniciada automaticamente pelo backend (classe `UIStarter`).

## Rodar somente a UI (opcional)
Entre na pasta `UI` e rode:

```
cd UI
mvn -DskipTests javafx:run
```

## Gerar artefato e rodar o jar

```
mvn -DskipTests package
java -jar target/*-SNAPSHOT.jar
```

## Empacotar com jpackage (Linux / Windows)
- Linux (exemplo):

```
chmod +x scripts/package-linux.sh
./scripts/package-linux.sh
```

- Windows (execute em Windows):

```
scripts\package-windows.bat
```

Após empacotar, execute o binário/instalador gerado em `dist/`.

---

## Dicas rápidas de troubleshooting
- Se não abrir a UI, verifique se a máquina não está em modo *headless* (DISPLAY no Linux) e se o processo Spring está rodando (porta 8080 por padrão).
- Se `/login` não redirecionar corretamente, verifique `SPOTIFY_REDIRECT_URI` e a configuração do app no Spotify Dashboard.
- Logs aparecem no console onde você executou `mvn spring-boot:run` ou no terminal onde rodar o jar.


