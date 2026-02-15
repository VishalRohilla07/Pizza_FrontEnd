#!/bin/bash

# Configuration
BASE_URL="http://localhost:8084/api"
EMAIL="testuser$(date +%s)@example.com"
PASSWORD="password123"

echo "Testing Backend Connectivity..."
echo "Base URL: $BASE_URL"

# 1. Test Public Endpoint (Pizzas)
echo "\n1. Testing GET /pizzas (Public)..."
curl -v "$BASE_URL/pizzas"
echo "\n"

# 2. Test Registration
echo "2. Testing POST /auth/register..."
REGISTER_PAYLOAD="{\"email\": \"$EMAIL\", \"password\": \"$PASSWORD\", \"name\": \"Test User\"}"
echo "Payload: $REGISTER_PAYLOAD"
curl -v -H "Content-Type: application/json" -d "$REGISTER_PAYLOAD" "$BASE_URL/auth/register"
echo "\n"

# 3. Test Login
echo "3. Testing POST /auth/login..."
LOGIN_PAYLOAD="{\"email\": \"$EMAIL\", \"password\": \"$PASSWORD\"}"
echo "Payload: $LOGIN_PAYLOAD"
curl -v -H "Content-Type: application/json" -d "$LOGIN_PAYLOAD" "$BASE_URL/auth/login"
echo "\n"
