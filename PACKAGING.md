# Empacotamento (jlink + jpackage) — Linux e Windows

Objetivo: fornecer um executável/instalador que inicia **backend (Spring Boot)** + **UI (Swing)** em um único processo JVM com limites de memória padrão para evitar travar máquinas com pouca RAM.

Requisitos:
- JDK 17+ (recomendado 21) com `jlink`/`jpackage` disponível
- Para Windows: WiX Toolset (para criar instaladores `.msi` / `.exe`) — instale e adicione ao PATH
- Maven

Scripts incluídos:
- `scripts/package-linux.sh` — gera um app image com um runtime mínimo (jlink) e configura opções de JVM (`-Xmx512m`)
- `scripts/package-windows.bat` — script para Windows (execute em Windows or CI runner)

Como gerar (Linux):
1. No root do projeto: `chmod +x scripts/package-linux.sh`
2. `./scripts/package-linux.sh`
3. Resultado em `dist/` (app image). Para criar instalador use `jpackage --type deb` substituindo `--type app-image`.

Como gerar (Windows):
1. Abra um terminal PowerShell/Command Prompt com WiX no PATH
2. `scripts\package-windows.bat`
3. Resultado em `dist\` (exemplo `.exe` quando disponível)

Notas importantes:
- `jpackage` costuma precisar ser executado no mesmo SO para gerar instaladores nativos (não é totalmente cross-platform). Para criar instaladores Windows a partir de Linux, use runners/VMs do Windows no CI (GitHub Actions, etc.).
- Ajuste a lista de módulos em `jlink` caso sua app use módulos adicionais (ex.: `javafx.*` se passar a empacotar JavaFX UI no mesmo artefato).

Recomendações para minimizar uso de recursos:
- O launcher usa opções de JVM por padrão (`-Xmx512m`, `-XX:MaxRAMPercentage=60.0`) — você pode ajustá-las no script ou no instalador.
- Em `src/main/resources/application.properties` definimos `server.tomcat.threads.max=20` para reduzir threads do servidor incorporado.

Se quiser, eu posso:
- Automatizar a criação de um `jlink` mais enxuto (calcular módulos automaticamente),
- Adicionar um job de CI (GitHub Actions) que gera artefatos para Linux e Windows, ou
- Trocar a UI Swing pelo JavaFX numa build unificada (exige inclusão de módulos JavaFX no runtime image).

Quer que eu continue e adicione o job do GitHub Actions para gerar o instalador Windows e Linux automaticamente? (recomendado para cross-build).