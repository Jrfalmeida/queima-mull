QUEIMA MULL - projeto Android (versao 2)

O que mudou:
- O relatorio agora fica DENTRO do aplicativo (app/src/main/assets/index.html). Funciona sem internet e sem login.
- Botao "Salvar PDF": abre a impressao do Android; escolha "Salvar como PDF" e a pasta.
- Tamanhos em dp (botoes e logo no tamanho certo) e ajuste para Android 15 (targetSdk 34).

Como gerar o APK pelo GitHub:
1. Crie um repositorio chamado QueimaMULL e envie TODOS os arquivos e pastas (inclusive a pasta oculta .github).
2. Aba Actions > "Gerar APK Queima MULL" > Run workflow.
3. Ao terminar, baixe o artefato "Queima-MULL-APK", extraia o app-debug.apk e instale.

Os valores digitados ficam guardados no proprio aparelho.
