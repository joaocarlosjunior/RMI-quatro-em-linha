# Quatro em Linha com Java RMI
- Instituição: Universidade Estadual de Santa Cruz
- Curso: Ciência da Computação
- Disciplina: DEC000100 - SISTEMAS DISTRIBUIDOS
- Docente: Dr. Paulo Andre Sperandio Giacomin
- Discente: João Carlos Ribas Chaves Júnior

<br>Este é um projeto desenvolvido para a disciplina de Sistemas Distribuidos que implementa o jogo Quatro em Linha em Java usando RMI (Remote Method Invocation).

## Descrição

O Quatro em Linha é um jogo de estratégia em que dois jogadores alternam para colocar peças em um tabuleiro vertical. O objetivo é conectar quatro peças consecutivas em linha, seja horizontalmente, verticalmente ou diagonalmente, antes do seu oponente.

Este projeto utiliza Java RMI para permitir que dois jogadores joguem o Quatro em Linha em diferentes máquinas, mas conectados através da rede.

## Funcionalidades

* Interface de usuário simples e intuitiva.
* Dois jogadores podem jogar em máquinas diferentes através da rede usando RMI.
* O jogo verifica automaticamente se um jogador ganhou após cada jogada.

## Requisitos
Java Development Kit (JDK) 8 ou superior.

## Como Jogar
Serão necessario 3 terminais.
1. Clone este repositório para o seu computador:
```sh
git clone git@github.com:joaocarlosjunior/RMI-quatro-em-linha.git
```
2. Verifique a versão do seu JDK e altere no pom.xml para versão instalada em sua máquina:
```sh
java -version
```
3. Navegue até o diretório clonado:
```sh
cd RMI-quatro-em-linha
```
4. Em um terminal compile e empacote o projeto:
- Linux/unix
```sh
./mvnw package
```
- Windows
```sh
mvnw.cmd package
```
5. Navegue até o diretório do pacote gerado:
```sh
cd target
```
6. Execute o servidor
```sh
java -cp RMI-quatro-em-linha-1.0-SNAPSHOT.jar servidor.Servidor  
```
7. Em outros dois terminais execute os clientes
```sh
java -cp RMI-quatro-em-linha-1.0-SNAPSHOT.jar cliente.Cliente localhost [nome do jogador]
``` 


## Contribuição

Contribuições são bem-vindas! Se você encontrar algum problema ou quiser adicionar novos recursos, sinta-se à vontade para abrir uma issue ou enviar um pull request.

## Contato
- [LinkedIn](https://www.linkedin.com/in/joaocarlosjr/)

