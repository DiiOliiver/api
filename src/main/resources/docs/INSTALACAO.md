
## Instalação

#### 1 - Clonando o projeto
```powershell
    git clone https://github.com/DiiOliiver/api.git
    cd api
```

#### 2 - Levantar ambiente docker compose
```powershell
    docker compose up -d
```

#### 3 - Gerando chave privada e publica com chave OpenSSL
```powershell
    choco install openssl -y
    # reiniciar terminal depois de instalado
    cd .\src\main\resources\

    # Gerar chave privada de 2048 bits
    openssl genpkey -algorithm RSA -out app.key -pkeyopt rsa_keygen_bits:2048

    # Gerar chave pública a partir da chave privada
    openssl rsa -in app.key -pubout -out app.pub
```
Dentro do arquivo application-dev.properties adicionar:
```text
    # JWT
    jwt.public.key=classpath:app.pub
    jwt.private.key=classpath:app.key
```

#### 4 - Acessando e levantando o projeto:\
    Acessando o banco de dados levantado, crie o database db_contact.

#### 5 - Configurando o arquivo application-dev.properties:

```text
    # Informe o usuário e senha para se conectar ao banco de dados
    spring.datasource.username=postgres
    spring.datasource.password=postgres
    
    # Informe as chaves presentes no HubSpot
    spring.security.oauth2.client.registration.hubspot.client-id=c5d6d9cb-cd26-49ec-bf1f-90548ee444b0
    spring.security.oauth2.client.registration.hubspot.client-secret=f8350ad5-abd7-49a5-a4a0-3bd8ae7264a5

    # Os escopos determinam as permissões do seu aplicativo para acessar dados ou executar ações no HubSpot.
    spring.security.oauth2.client.registration.hubspot.scope=crm.lists.read%20crm.lists.write%20crm.objects.contacts.read%20crm.objects.contacts.write%20oauth

    # JWT: Caminho das chaves privadas e publicas
    jwt.public.key=classpath:app.pub
    jwt.private.key=classpath:app.key
    6 - Gerando e populando as tabelas

    # Informe o usuário e senha para se conectar ao banco de dados
    spring.flyway.user=postgres
    spring.flyway.password=postgres

```
#### 6 - Instalando e configurando o ngrok

```powershell
    choco install ngrok -y
    # reiniciar terminal depois de instalado
```
    Para ativar sua conta no ngrok, pegue seu authtoken em:
🔗 https://dashboard.ngrok.com/get-started/your-authtoken


```powershell
    ngrok config add-authtoken SEU_AUTHTOKEN_AQUI
    ngrok http 8082
```

#### 7 - Acessos/Links

    # Acessos após levantar o projeto:
    • Springdoc OpenAPI: http://localhost:8082/swagger-ui/index.html
    • Endpoints do projeto: http://localhost:8000/api/v1

    # Links:
    • HubSpot Developers: https://developers.hubspot.com/
    • HubSpot: https://www.hubspot.com/

#### 8 - Observação
O projeto possui dois tipos de autenticão, Token JWT (Acesso local) e Token OAuth (Acesso a HubSpot).
Por este motivo estarei subindo o dump do Insommia no qual realizei alguns dos meus testes para auxiliar na identficação destes endpoints.

Download: [Insonmia](api_insomnia.yaml)

> [Inicio](../../../../README.md)