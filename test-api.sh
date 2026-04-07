#!/bin/bash

BASE_URL="http://localhost:8080/clientes"
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

pass=0
fail=0

test() {
  local name="$1"
  local expected_code="$2"
  local actual_code="$3"

  if [ "$actual_code" -eq "$expected_code" ]; then
    echo -e "${GREEN}PASS${NC} - $name (HTTP $actual_code)"
    ((pass++))
  else
    echo -e "${RED}FAIL${NC} - $name (esperado $expected_code, foi $actual_code)"
    ((fail++))
  fi
}

echo -e "${YELLOW}========================================${NC}"
echo -e "${YELLOW}  Testes da API - Customer Endpoints${NC}"
echo -e "${YELLOW}========================================${NC}"
echo ""

# ---------- CREATE ----------
echo -e "\n${YELLOW}--- CREATE ---${NC}"

# 1. Criar cliente válido
echo -e "\nTeste 1: Criar cliente válido"
RESP=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{"name":"João Silva","cpf":"12345678901","email":"joao@email.com","phone":"11999999999"}')
CODE=$(echo "$RESP" | tail -1)
echo "$RESP" | head -1
test "Criar cliente válido" 201 "$CODE"

# 2. CPF duplicado
echo -e "\nTeste 2: CPF duplicado"
RESP=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{"name":"João Duplicado","cpf":"12345678901","email":"outro@email.com","phone":"11888888888"}')
CODE=$(echo "$RESP" | tail -1)
echo "$RESP" | head -1
test "CPF duplicado" 409 "$CODE"

# 3. Email duplicado
echo -e "\nTeste 3: Email duplicado"
RESP=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{"name":"Email Duplicado","cpf":"98765432100","email":"joao@email.com","phone":"11777777777"}')
CODE=$(echo "$RESP" | tail -1)
echo "$RESP" | head -1
test "Email duplicado" 409 "$CODE"

# 4. CPF inválido (com letras)
echo -e "\nTeste 4: CPF com caracteres inválidos"
RESP=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","cpf":"abc","email":"test@email.com","phone":"11999999999"}')
CODE=$(echo "$RESP" | tail -1)
echo "$RESP" | head -1
test "CPF inválido" 400 "$CODE"

# 5. CPF com tamanho errado
echo -e "\nTeste 5: CPF com menos de 11 dígitos"
RESP=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","cpf":"123","email":"test2@email.com","phone":"11999999999"}')
CODE=$(echo "$RESP" | tail -1)
echo "$RESP" | head -1
test "CPF curto" 400 "$CODE"

# 6. Email inválido
echo -e "\nTeste 6: Email inválido"
RESP=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","cpf":"11122233344","email":"invalido","phone":"11999999999"}')
CODE=$(echo "$RESP" | tail -1)
echo "$RESP" | head -1
test "Email inválido" 400 "$CODE"

# 7. Nome em branco
echo -e "\nTeste 7: Nome em branco"
RESP=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{"name":"","cpf":"55566677788","email":"valido@email.com","phone":"11999999999"}')
CODE=$(echo "$RESP" | tail -1)
echo "$RESP" | head -1
test "Nome em branco" 400 "$CODE"

# 8. Corpo vazio
echo -e "\nTeste 8: Corpo vazio"
RESP=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{}')
CODE=$(echo "$RESP" | tail -1)
echo "$RESP" | head -1
test "Corpo vazio" 400 "$CODE"

# ---------- FIND ALL ----------
echo -e "\n${YELLOW}--- FIND ALL (paginação) ---${NC}"

# 9. Listar todos
echo -e "\nTeste 9: Listar todos os clientes"
RESP=$(curl -s -w "\n%{http_code}" "$BASE_URL?page=0&size=10")
CODE=$(echo "$RESP" | tail -1)
echo "$RESP" | head -1 | python3 -c "import sys,json; print(json.dumps(json.load(sys.stdin),indent=2))" 2>/dev/null || echo "$RESP" | head -1
test "Listar todos" 200 "$CODE"

# ---------- GET BY ID ----------
echo -e "\n${YELLOW}--- GET BY ID ---${NC}"

# 10. Buscar cliente existente
echo -e "\nTeste 10: Buscar cliente existente (ID 1)"
RESP=$(curl -s -w "\n%{http_code}" "$BASE_URL/1")
CODE=$(echo "$RESP" | tail -1)
echo "$RESP" | head -1 | python3 -c "import sys,json; print(json.dumps(json.load(sys.stdin),indent=2))" 2>/dev/null || echo "$RESP" | head -1
test "Buscar cliente existente" 200 "$CODE"

# 11. Buscar cliente inexistente
echo -e "\nTeste 11: Buscar cliente inexistente (ID 999)"
RESP=$(curl -s -w "\n%{http_code}" "$BASE_URL/999")
CODE=$(echo "$RESP" | tail -1)
echo "$RESP" | head -1
test "Buscar cliente inexistente" 404 "$CODE"

# ---------- UPDATE ----------
echo -e "\n${YELLOW}--- UPDATE ---${NC}"

# 12. Atualizar cliente existente
echo -e "\nTeste 12: Atualizar cliente existente (ID 1)"
RESP=$(curl -s -w "\n%{http_code}" -X PUT "$BASE_URL/1" \
  -H "Content-Type: application/json" \
  -d '{"name":"João Atualizado","cpf":"12345678901","email":"joao.novo@email.com","phone":"11888888888"}')
CODE=$(echo "$RESP" | tail -1)
echo "$RESP" | head -1
test "Atualizar cliente" 200 "$CODE"

# 13. Atualizar cliente inexistente
echo -e "\nTeste 13: Atualizar cliente inexistente (ID 999)"
RESP=$(curl -s -w "\n%{http_code}" -X PUT "$BASE_URL/999" \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","cpf":"12345678901","email":"test@email.com","phone":"11999999999"}')
CODE=$(echo "$RESP" | tail -1)
echo "$RESP" | head -1
test "Atualizar cliente inexistente" 404 "$CODE"

# 14. Atualizar com CPF duplicado
echo -e "\nTeste 14: Atualizar com CPF de outro cliente"
RESP=$(curl -s -w "\n%{http_code}" -X PUT "$BASE_URL/1" \
  -H "Content-Type: application/json" \
  -d '{"name":"João","cpf":"98765432100","email":"joao.novo@email.com","phone":"11888888888"}')
CODE=$(echo "$RESP" | tail -1)
echo "$RESP" | head -1
test "Atualizar com CPF duplicado" 409 "$CODE"

# ---------- DELETE ----------
echo -e "\n${YELLOW}--- DELETE ---${NC}"

# 15. Deletar cliente existente
echo -e "\nTeste 15: Deletar cliente existente (ID 1)"
RESP=$(curl -s -w "\n%{http_code}" -X DELETE "$BASE_URL/1")
CODE=$(echo "$RESP" | tail -1)
echo "$RESP" | head -1
test "Deletar cliente" 200 "$CODE"

# 16. Deletar cliente inexistente
echo -e "\nTeste 16: Deletar cliente inexistente (ID 999)"
RESP=$(curl -s -w "\n%{http_code}" -X DELETE "$BASE_URL/999")
CODE=$(echo "$RESP" | tail -1)
echo "$RESP" | head -1
test "Deletar cliente inexistente" 404 "$CODE"

# ---------- SUMMARY ----------
echo ""
echo -e "${YELLOW}========================================${NC}"
echo -e "${YELLOW}  Resultado Final${NC}"
echo -e "${YELLOW}========================================${NC}"
echo -e "  ${GREEN}Pass: $pass${NC}  ${RED}Fail: $fail${NC}"
echo -e "  Total: $((pass + fail))"
echo -e "${YELLOW}========================================${NC}"

if [ $fail -eq 0 ]; then
  echo -e "${GREEN}Todos os testes passaram!${NC}"
  exit 0
else
  echo -e "${RED}Alguns testes falharam!${NC}"
  exit 1
fi
