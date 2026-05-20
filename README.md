# Sistema de Gerenciamento de Academia (Gym Manager)

O **Gym Manager** é uma aplicação de linha de comando desenvolvida em Java com persistência de dados no banco de dados PostgreSQL. O sistema foi projetado para gerenciar fluxos operacionais de uma academia de ginástica, controlando cadastros de alunos, instrutores, planos de assinatura, agendamento de aulas coletivas e o histórico de frequência.

Este projeto foi desenvolvido como parte de um trabalho interdisciplinar para as disciplinas de **Programação Orientada a Objetos** e **Banco de Dados**.

## 🚀 Funcionalidades Principais

- **Gerenciamento Completo (CRUD):** Alunos, Instrutores, Planos, Aulas e Frequências.
- **Regra de Negócio Complexa:** Validação automatizada de planos ativos, cálculo de vencimento de assinaturas, controle de capacidade máxima de vagas por aula e prevenção de conflitos de horários de aulas para o mesmo aluno.
- **Controle de Frequência:** Registro de entrada de alunos com métricas automáticas (total de visitas e data da última visita).
- **Relatórios:** Emissão de relatórios de ocupação de aulas e histórico de frequência por período.

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java (POO)
- **Banco de Dados:** PostgreSQL
- **Conectividade:** JDBC (Java Database Connectivity)
- **Versionamento:** Git & GitHub

## 🧱 Arquitetura do Projeto

O código está organizado seguindo o padrão de divisões lógicas (camadas):
- `entities`: Classes de modelo que representam as tabelas do banco de dados utilizando os pilares de Encapsulamento.
- `dao` (Data Access Object): Camada responsável por conter as instruções SQL (Insert, Update, Delete, Select) e interagir diretamente com o PostgreSQL.
- `services`: Concentra as regras de negócio e validações lógicas do sistema.
- `database`: Gerenciamento e abertura de conexões com o banco de dados.
- `main`: Ponto de entrada do programa contendo a interface de menus via linha de comando.

## 👥 Integrantes do Grupo
- Gabriel Nascimento
- Isaque Valim
- João Victor
- Matheus Henrique
- Nilson Chagas
- Ryan Catão
- Vinícius Vieira