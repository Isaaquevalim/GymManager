````md id="rvm4xg"
# 🏋️ Gym Manager

![Java](https://img.shields.io/badge/Java-17-orange)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![Status](https://img.shields.io/badge/status-em%20desenvolvimento-green)
![License](https://img.shields.io/badge/license-Academic-lightgrey)

O **Gym Manager** é um sistema de gerenciamento de academias desenvolvido em Java com PostgreSQL, focado no controle de alunos, funcionários, matrículas, pagamentos, aulas coletivas e frequência.

O projeto foi desenvolvido com foco em **Programação Orientada a Objetos (POO)**, persistência de dados e aplicação de regras de negócio reais, utilizando arquitetura em camadas e JDBC para comunicação com o banco de dados.

Este projeto foi desenvolvido como parte de um trabalho interdisciplinar para as disciplinas de **Programação Orientada a Objetos** e **Banco de Dados**.

---

# 🚀 Funcionalidades

## 📌 Gerenciamento Completo (CRUD)

O sistema possui operações completas de cadastro, edição, listagem e remoção para:

- Alunos
- Funcionários
- Salas
- Matrículas
- Pagamentos
- Aulas
- Equipamentos
- Frequências
- Manutenções
- Serviços Externos

---

## 📌 Regras de Negócio

O sistema foi desenvolvido para simular fluxos reais de uma academia, aplicando validações automáticas e regras operacionais.

### ✔️ Controle de Matrículas
- Validação de vigência dos contratos
- Associação automática entre aluno e plano
- Controle de datas de início e vencimento

### ✔️ Controle de Aulas
- Limite máximo de alunos por sala
- Associação entre aula, instrutor e ambiente
- Controle de horários e datas

### ✔️ Controle Financeiro
- Registro de pagamentos
- Controle de status de mensalidades
- Consolidação de receitas e despesas

### ✔️ Controle de Equipamentos
- Status operacional dos equipamentos
- Histórico de manutenção
- Controle de custos de reparo

---

# 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java
- **Paradigma:** Programação Orientada a Objetos (POO)
- **Banco de Dados:** PostgreSQL
- **Persistência:** JDBC
- **Versionamento:** Git & GitHub

---

# 🧱 Arquitetura do Projeto

O projeto segue uma arquitetura em camadas (**Layered Architecture**), promovendo separação de responsabilidades, organização e facilidade de manutenção.

## 📂 Estrutura do Projeto

```bash
src/
├── entities/
├── dao/
├── database/
└── main/
```

---

## 📌 Camadas

### `entities`
Camada responsável pelas entidades de domínio do sistema.

As classes representam diretamente as tabelas do banco de dados utilizando:
- Encapsulamento
- Getters e Setters
- Associações entre objetos
- Modelagem orientada a objetos

---

### `dao`
Implementa o padrão **DAO (Data Access Object)**.

Essa camada encapsula:
- Operações SQL
- Conexão JDBC
- CRUD completo
- Consultas ao PostgreSQL

Cada DAO é responsável exclusivamente pela comunicação com sua respectiva tabela.

---

### `database`
Responsável pela abertura, gerenciamento e encerramento das conexões com o PostgreSQL.

---

### `main`
Camada responsável pela interface CLI (**Command Line Interface**), menus interativos e captura de entradas do usuário via `Scanner`.

Também realiza:
- Validações preventivas
- Tratamento de exceções
- Navegação do sistema

---

# 🗄️ Estrutura do Banco de Dados

O banco de dados foi modelado utilizando conceitos relacionais e integridade referencial, contendo 10 entidades principais.

---

## 📌 Entidades

### 👤 Aluno
Armazena os dados dos alunos matriculados na academia.

- CPF utilizado como chave primária
- Dados pessoais
- Controle de cadastro

---

### 👨‍🏫 Funcionário
Responsável pelo cadastro da equipe da academia.

- Instrutores
- Funcionários administrativos
- Cargo e salário

---

### 🏢 Sala
Mapeia os ambientes físicos da academia.

- Tipo da sala
- Capacidade máxima
- Controle de utilização

---

### 📄 Matrícula
Gerencia os contratos e planos dos alunos.

- Vigência
- Valor
- Tipo de plano
- Associação com alunos

---

### 💳 Pagamento
Controla as mensalidades e transações financeiras.

- Valor
- Data
- Status do pagamento

---

### 🏋️ Aula
Gerencia a grade de aulas coletivas.

- Instrutor responsável
- Sala vinculada
- Horários e datas

---

### ⚙️ Equipamento
Controla os equipamentos da academia.

- Quantidade
- Status operacional
- Distribuição por sala

---

### ✅ Frequência
Tabela associativa responsável pelo check-in dos alunos nas aulas.

---

### 🔧 Manutenção
Registra reparos e custos relacionados aos equipamentos.

---

### 🧾 Serviços Externos
Gerencia despesas terceirizadas e custos operacionais.

---

# 🔐 Regras de Negócio e Validações

O sistema foi desenvolvido para garantir integridade e segurança dos dados.

---

## ✔️ Integridade Referencial

O PostgreSQL foi configurado com:
- `ON DELETE CASCADE`
- `ON DELETE SET NULL`

Garantindo:
- Exclusão automática de registros dependentes
- Consistência do banco de dados
- Limpeza automática de relacionamentos

---

## ✔️ Blindagem de Entradas

O sistema utiliza tratamento de exceções com `try-catch`.

### Validações implementadas:
- Datas no padrão `dd/MM/yyyy`
- Conversão automática de valores monetários
- Prevenção de falhas de entrada
- Validação de tipos de dados

---

## ✔️ Associação de Planos

A lógica de planos foi incorporada diretamente na entidade `Matrícula`, evitando redundância estrutural no banco de dados.

---

# 📊 Controle de Frequência

O controle de presença foi implementado utilizando relacionamento Muitos-para-Muitos (N:M).

---

## ✔️ Funcionamento do Check-in

A `FrequenciaDAO` registra:
- CPF do aluno
- ID da aula
- Data de presença

Sem alterar diretamente os dados da aula ou do aluno.

---

## ✔️ Validações

Para registrar presença:
- O aluno precisa existir
- A aula precisa existir
- Os identificadores precisam ser válidos

---

# 💰 Painel Financeiro e Dashboard

O sistema possui um fluxo gerencial responsável pelo balanço financeiro da academia.

---

## 📈 Receitas

O `PagamentoDAO` realiza:
```sql
SELECT SUM(valor)
FROM pagamento
WHERE status = 'Pago';
```

Consolidando todas as mensalidades quitadas.

---

## 📉 Despesas Variáveis

O sistema soma:
- Custos de manutenção
- Reparos de equipamentos
- Serviços terceirizados
- Custos operacionais

---

## 👨‍💼 Despesas Fixas

O `FuncionarioDAO` percorre toda a equipe ativa somando:
- Salários
- Custos da folha salarial

---

## 📌 Resultado Final

O sistema calcula automaticamente:
```text
Lucro/Prejuízo = Receitas - Despesas
```

Gerando um painel analítico para auxílio gerencial.

---

# ⚙️ Como Executar

## 📋 Pré-requisitos

- Java 17+
- PostgreSQL
- Git

---

## 📥 Clone o repositório

```bash
git clone https://github.com/seuusuario/gym-manager.git
```

---

## 🗄️ Crie o banco de dados

```sql
CREATE DATABASE gym_manager;
```

---

## ▶️ Execute o script SQL

```bash
psql -U postgres -d gym_manager -f database.sql
```

---

## ▶️ Execute a aplicação

```bash
javac Main.java
java Main
```

---

# 💡 Conceitos Aplicados

- Programação Orientada a Objetos
- Encapsulamento
- Associações entre objetos
- JDBC
- CRUD
- DAO Pattern
- Arquitetura em camadas
- Persistência de dados
- Modelagem relacional
- Integridade referencial
- Tratamento de exceções
- Relacionamentos N:M
- Regras de negócio

---

# 📸 Demonstração

## 🖥️ Menu Principal

```bash
==============================
         GYM MANAGER
==============================

1 - Gerenciar Alunos
2 - Gerenciar Funcionários
3 - Gerenciar Matrículas
4 - Gerenciar Aulas
5 - Registrar Frequência
6 - Painel Financeiro
0 - Sair

Escolha uma opção:
```

---

# 🔮 Melhorias Futuras

- Interface gráfica com JavaFX
- API REST com Spring Boot
- Sistema de autenticação
- Dashboard web
- Exportação de relatórios PDF
- Testes automatizados
- Dockerização do projeto

---

# 👥 Integrantes do Grupo

- Gabriel Nascimento
- Isaque Valim
- João Victor
- Matheus Henrique
- Nilson Chagas
- Ryan Catão
- Vinícius Vieira

---

# 📄 Licença

Este projeto foi desenvolvido para fins acadêmicos.
````
