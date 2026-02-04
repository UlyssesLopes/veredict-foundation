# Veredict Foundation

**Veredict Foundation** é o núcleo técnico imutável do ecossistema Veredict.  
Este repositório define **contratos, modelos e algoritmos determinísticos** que servem como base para qualquer engine, runtime ou produto que implemente decisões regulatórias usando o Veredict.

> ⚠️ Este repositório **não executa regras**, **não interpreta leis** e **não contém runtime**.  
> Ele define apenas **estruturas canônicas, determinísticas e auditáveis**.

---

## 🎯 Objetivo

Garantir que qualquer decisão produzida por sistemas Veredict seja:

- **Determinística** (mesmo input → mesmo output)
- **Auditável** (hashes e contratos estáveis)
- **Versionada** (nenhuma quebra silenciosa)
- **Engine-agnostic** (independente de runtime, parser ou stack)

---

## 🧱 Princípios Inquebráveis

- Veredict **não interpreta leis** → o cliente define a interpretação
- Execução deve ser **determinística**
- Auditoria é **first-class** e **append-only**
- **Control Plane** separado do **Data Plane**
- Contratos **versionados** (v1, v2, …)
- Nenhuma mudança breaking sem nova versão

---

## 🛠️ Stack

- **Java 21**
- **Maven (multi-module)**
- ❌ Sem Spring
- ❌ Sem banco de dados
- ❌ Sem runtime de execução

Build sempre via **Maven Wrapper** (`./mvnw`).

---

## 📦 Estrutura do Repositório

```text
veredict-foundation/
├─ pom.xml                    # Aggregator (root)
├─ README.md
└─ modules/
   ├─ decision-contract/      # Decision Contract v1
   ├─ canonicalization/       # Canonicalization v1 (JCS-like)
   ├─ crypto-hash/            # Deterministic hashing v1
   ├─ dsl-model/              # DSL Model v1 (AST-only)
   └─ test-fixtures/          # Shared golden files (se aplicável)
📐 Módulos
1️⃣ Decision Contract (decision-contract)
Define o contrato canônico de entrada e saída de uma decisão Veredict.

Inclui:

DecisionRequestV1

DecisionResponseV1

DecisionOutcome

DecisionStatus

RuleBundleRef

JSON Schema (decision-contract-v1.json)

ObjectMapper determinístico (ordem estável, sem ambiguidade)

📌 Garantia
O mesmo payload serializado sempre gera a mesma representação JSON.

2️⃣ Canonicalization (canonicalization)
Responsável por transformar JSON arbitrário em uma forma canônica determinística (JCS-like).

Inclui:

CanonicalizerV1 (API)

JcsLikeCanonicalizerV1 (implementação)

Testes golden com entradas e saídas congeladas

📌 Garantia
Dois JSONs semanticamente iguais → bytes idênticos após canonicalização.

3️⃣ Crypto Hash (crypto-hash)
Define hashing determinístico para auditoria e rastreabilidade.

Inclui:

HashingV1 (API)

Sha256HashingV1 (implementação)

Geração de:

contextHash

bundleHash

decisionId

Golden hashes congelados

Known SHA-256 vectors

📌 Garantia
Hashes são imutáveis, reproduzíveis e independentes de runtime.

4️⃣ DSL Model (dsl-model)
Define o modelo abstrato (AST) de regras Veredict.

Inclui:

Expressões booleanas (ExprV1)

Operadores (AND, OR, comparações, exists, null)

Tipos (ValueV1, ValueType)

Regras (RuleV1)

Bundles (RuleBundleV1)

Outcomes (OutcomeV1)

🚫 Não inclui

Parser

Engine

Execução

Avaliação de contexto

📌 Garantia
O DSL Model é engine-agnostic e estável por versão.

🧪 Testes
Todos os módulos seguem:

Testes de determinismo

Golden files congelados

Zero warnings

Zero skipped tests

Rodar tudo:
./mvnw clean test
Rodar um módulo específico:
./mvnw -pl modules/crypto-hash -am test
🔐 Versionamento
Cada módulo é versionado por namespace (v1)

Mudanças breaking exigem:

novo package (v2)

coexistência com versões anteriores

Exemplo:

io.veredict.dslmodel.v1
io.veredict.dslmodel.v2
🧾 Padrão de Commits
Todos os commits seguem o padrão:

EPIC-XXX: descrição clara e auditável
Exemplo:

EPIC-001: deterministic hashing v1 with frozen golden vectors
📚 ADRs e Documentação
Decisões arquiteturais estão documentadas no repositório:

👉 veredict-docs
(links diretos devem ser adicionados conforme ADRs forem formalizados)

🚀 O que vem depois
Este repositório é foundation-only.

Próximos níveis do ecossistema Veredict incluem:

DSL textual (parser)

Rule Engine

Control Plane

Data Plane

Runtime distribuído

Auditoria regulatória

Nenhum desses componentes vive aqui.

🛑 Aviso Final
Se você está procurando:

execução de regras

avaliação de contexto

interpretação legal

side effects

👉 este não é o repositório certo.

Aqui definimos apenas verdades imutáveis sobre decisões Veredict.
