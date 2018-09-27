# TempTunes

## Descrição

Essa é uma aplicação escrita em Java, usando SpringBoot, que se comunica com a API do OpenWeather (https://openweathermap.org/api)
e do Spotify (https://developer.spotify.com/documentation/web-api/) para, a partir do nome de uma cidade, ou de coordenadas lat
e long, sugerir uma playlist a partir do clima dessa localidade.

## Como rodar em um ambiente local

Essa aplicação utiliza o Docker (https://www.docker.com/) e sua ferramenta de Docker Compose para subir a aplicação e suas
dependências em ambiente local. Certifique-se de ter o docker instalado em seu computador. Caso não tenha, a empresa do Docker 
disponibiliza instruções para instalação (https://www.docker.com/products/docker-desktop).
Para rodar a aplicação, após um clone, rode o comando, no diretório raiz do projeto:

```sh
$ docker-compose build
```

E depois:

```sh
$ docker-compose up
```

Uma chamada HTTP `GET` para o endereço `localhost:8080/actuator/health` deve indicar que o serviço está rodando.

## Endpoints

O serviço provê dois endpoints, ambas chamadas HTTP `GET`:
```
localhost:8080/playlists/city/London
```
```
localhost:8080/playlists/lat/-3.71839/lng/-38.5434
```

Ambas retornam uma chamada no formato: 

```
{
  "code": 200,
  "errorDetails": null,
  "data": {
    "name": "Pop Up",
    "description": "As faixas que estão com tudo e os grandes hits em um único lugar! Foto: Avril Lavigne",
    "songs": [
      {
        "title": "In My Mind"
      },
      {
        "title": "Head Above Water"
      },
      ...
    ]
  }
} 
```      

Ou, caso algum erro aconteça:
```
{
  "code": 404,
  "errorDetails": {
    "title": "error.city.not.found",
    "message": "City MelhorCidadeDoMundo not found."
  },
  "data": null
}
```
Ambas com o devido código HTTP preenchido nos cabeçalhos da resposta.

## Tecnologia e frameworks

Abaixo são explicadas algumas tecnologias utilizadas para desenvolvimento dessa aplicação

### Docker e Docker Compose

Um dos requisitos não-funcionais que foi requisitado foi o de ser possível fazer o deploy em ambiente local facilmente. 
Sem o docker, seria necessário instalar uma série de dependências (JDK, Maven) e ter um banco Redis acessível pela 
aplicação para que fosse possível rodar a aplicação localmente.

O docker-compose já roda o comando `mvn clean package`, executa a aplicação SpringBoot e sobe um banco Redis, tudo com dois
comandos, utilizando o contexto dos containers para isso. O docker-compose inclusive permite já inserir informações em 
variáveis de ambiente, como api keys e secrets, que em produção seriam administrados por algum sistema de governança e que
localmente precisariam ser adicionadas em variáveis de ambiente locais.

Em uma aplicação em produção, não seria necessário, contudo, utilizar uma imagem docker como base que tivesse o Maven e que
fizesse a operação de package, uma vez que operações de CI/CD já teriam o arquivo Jar compilado em um outro contexto, e 
a regra da imagem Docker estaria descorrelacioado com questão do build desse Jar. Mas essa imagem com Maven foi utilizada
aqui para fins de facilidade.

### Java e SpringBoot

A escolha por eles foi feita por fins de familiaridade com a tecnologia. Criar um exception handler, tratar com injeção
de dependência, colocar o actuator dentro da aplicação, implementar padrões de projeto, cuidar do gerenciamento de 
dependências: tudo é mais fácil (e rápido) com a familiaridade com a linguagem e o framework.

### Feign

Novamente, uma escolha por familiaridade, para um client HTTP, com bastante documentação e uma comunidade ativa. Existe uma
integração do Feign com o Spring, mas foi utilizado o FeignCore nesse projeto, apenas, por fins de simplicidade.

O principal problema do Feign é disparar exceptions ao invés de um objeto contendo informações de erro. Dessa forma,
o ideal é encapsular as APIs para que não sejam feitos tratamentos de fluxo via exception, como é feito com o fluxo de
reautenticação em caso de token inválido ao consumir a API do Spotify. Esse é um ponto
claro de melhoria desse código.

### Actuator

Com a idéia da resiliência, foi colocado um mínimo de suporte a monitoramento, utilizando o Actuator do Spring, para alguma
entidade externa conseguir verificar ao menos se a aplicação está rodando. Pode-se melhorar esse ponto, colocando outros pontos
de monitoramento, como a comunicação com o banco Redis.

### Exception Handling e padronização da resposta da API

Quis-se, olhando para a ideia Restful, padronizar a resposta, devolvendo informações de erro (caso aconteça) e o código HTTP
de retorno, de forma a guiar minimamente o usuário sobre possíveis problemas e suas soluções. Para isso, foi implementado um
tratamento de exceções, que encapsula o retorno sempre em um formato padronizado. 

Pode-se melhorar, ainda, a resposta da aplicação,
por exemplo, para a resposta para endereços inexistentes (404), que acontece antes da requisição alcançar nossos Controllers.
Um `GET` para `http://localhost:8080/playlist/city/MelhorCidadeDoMundo`, por exemplo, ainda retorna uma resposta padrão do SpringBoot.

### Padrão Factory para regra de decisão de que Playlist responder para qual clima

Foi utilizado um padrão Factory nesse caso, para que, caso novas regras de negócio sobre o clima sejam implementadas, basta
que se crie uma nova classe que implemente a interface `SpotifyPlaylistByTemperatureSolver` e que seja um componente Spring
para que a regra passe a valer. Idealmente, nosso endpoint pode retornar uma lista de Playlists, para ser resiliente caso
aconteça uma interseção entre duas ou mais regras. Mas mantive a aplicação retornando apenas uma, devido as requisições
iniciais do projeto.

### Redis e Redisson

O Redis foi inserido dentro da arquitetura devido a necessidade da aplicação ser resistente a falhas e ter alta disponibilidade.
O Spotify possui um sistema de autenticação via token, que é obtido através de uma chamada, enviando uma combinação do client id
e do client secret cadastrados na aplicação no Spotify. Esse token obtido, precisa ser enviado em toda chamada subsequente.
Então duas situações se criam: a primeira é que esse token não pode ser armazenado em memória, pois dessa forma cada instância
da aplicação teria o próprio token - talvez o Spotify tenha um limite de tokens ativos simultaneamente, ou pode passar a ter, o que iria 
invalidar completamente a aplicação de ter uma natureza distribuída -  e cada aplicação precisaria administrar o ciclo de vida do
próprio token. Ou seja, toda nova instância que subisse precisaria fazer o processo de autenticação do Spotify, muito embora
outras aplicações tivesse Tokens válidos. Além disso, se o Spotify possuir limite de Tokens ativos, subir múltiplas instâncias
simultaneamente poderia gerar condições de corrida inesperadas.

Para isso, foi criado um registro dentro do Redis para guardar um token único que pode ser utilizado por todas as instâncias.
Isso leva a segunda situação: caso esse token único fique inválido, múltiplas instâncias podem tentar regerar o token, fazendo 
com que existam múltiplos tokens desnecessariamente, sobrescritos no banco. Para resolver esse problema, foi criado um registro lock, no 
próprio Redis, de forma que apenas uma instância tentará revalidar o token. As demais threads, retornam um erro para o usuário - como
um circuit breaker - até que o token seja revalidado. O ideal seria, talvez, nesse caso, atrasar a resposta das demais threads para que
elas aguardem o token ser revalidado, para tentar novamente puxar a informação do Spotify.

O Redisson foi utilizado por dar suporte ao uso de Bucket, para armazenar a informação do token, e o uso de Lock, para lockar o
processo de autenticação. Além disso, sua implementação do Lock fornece um watchdog, para que, caso a instância que executou o 
lock morra durante o processo de reautenticação, essa trava seja reaberta após um tempo de timeout, para que uma outra thread possa
terminar o serviço.

Além de tudo isso, o Redis pode ser usado para função de cache para as APIs de terceiros, integrado com o Spring, embora essa função não 
tenha sido implementada, mas é sugerida como melhoria.

### Testes unitários

Foram desenvolvidos alguns testes unitários na aplicação em alguns pontos-chave, utilizando o junit. Idealmente, a cobertura de testes
seria bem maior, mas por efeito de tempo, alguns pontos foram priorizados nos testes. 

Alguns refactorings podem ser feitos para melhorar a qualidade dos testes. Alguns fluxos
de exceção de API não foram testados porque não se conseguia lançar uma FeignException mockada.
Tendo a necessidade, portanto, de se criar um wrapper em cima desses Serviços de API que lancem
exceções que se tem controle, para que fluxos de tratamento de exceções possam ser testados. Um exemplo,
é o processo de reautenticação no Spotify no caso de um token inválido.
