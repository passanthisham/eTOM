#!/bin/bash

# Base URL
BASE_URL="http://localhost:8080/api/problems"

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo "Testing Service Problem Management API Endpoints"
echo "=============================================="

# Test 1: Create a new problem
echo -e "\n${GREEN}Test 1: Creating a new problem${NC}"
CREATE_RESPONSE=$(curl -s -X POST -H "Content-Type: application/json" -d '{
    "title": "Network Connectivity Issue",
    "description": "Users reporting intermittent network connectivity",
    "priority": "HIGH",
    "severity": "HIGH",
    "category": "NETWORK",
    "status": "NEW",
    "createdBy": "test-user"
}' $BASE_URL)

echo "Response: $CREATE_RESPONSE"
PROBLEM_ID=$(echo $CREATE_RESPONSE | grep -o '"id":"[^"]*' | cut -d'"' -f4)

# Test 2: Get all problems
echo -e "\n${GREEN}Test 2: Getting all problems${NC}"
curl -s -X GET $BASE_URL | json_pp

# Test 3: Get specific problem
echo -e "\n${GREEN}Test 3: Getting problem with ID: $PROBLEM_ID${NC}"
curl -s -X GET "$BASE_URL/$PROBLEM_ID" | json_pp

# Test 4: Update problem
echo -e "\n${GREEN}Test 4: Updating problem${NC}"
curl -s -X PUT -H "Content-Type: application/json" -d '{
    "title": "Updated Network Issue",
    "description": "Updated description",
    "priority": "HIGH",
    "severity": "HIGH",
    "category": "NETWORK",
    "status": "IN_PROGRESS",
    "assignedTo": "support-team"
}' "$BASE_URL/$PROBLEM_ID" | json_pp

# Test 5: Acknowledge problem
echo -e "\n${GREEN}Test 5: Acknowledging problem${NC}"
curl -s -X POST "$BASE_URL/$PROBLEM_ID/acknowledge" | json_pp

# Test 6: Search problems
echo -e "\n${GREEN}Test 6: Searching problems${NC}"
curl -s -X GET "$BASE_URL/search?status=IN_PROGRESS&priority=HIGH&category=NETWORK" | json_pp

# Test 7: Unacknowledge problem
echo -e "\n${GREEN}Test 7: Unacknowledging problem${NC}"
curl -s -X POST "$BASE_URL/$PROBLEM_ID/unacknowledge" | json_pp

# Test 8: Delete problem
echo -e "\n${GREEN}Test 8: Deleting problem${NC}"
curl -s -X DELETE "$BASE_URL/$PROBLEM_ID"

echo -e "\n${GREEN}All tests completed!${NC}" 