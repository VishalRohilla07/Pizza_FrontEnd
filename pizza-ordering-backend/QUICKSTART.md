# Quick Start Guide - Pizza Ordering System

## 🚀 Getting Started in 5 Minutes

### Step 1: Prerequisites Check

Ensure you have installed:
- ✅ Java 17 or higher: `java -version`
- ✅ MySQL 8.0 or higher: `mysql --version`
- ✅ Maven 3.6 or higher: `mvn -version`
- ✅ Node.js 16+ (for frontend): `node --version`

### Step 2: Database Setup

```bash
# Start MySQL and create database
mysql -u root -p
```

```sql
CREATE DATABASE pizza_ordering_db;
EXIT;
```

### Step 3: Configure Backend

Navigate to backend and update configuration:

```bash
cd /Users/Vishu/Downloads/crust-connect-system-main/pizza-ordering-backend
```

Edit `src/main/resources/application.properties`:

```properties
# Update these if your MySQL credentials are different
spring.datasource.username=root
spring.datasource.password=root

# Add your Stripe test keys (get from https://dashboard.stripe.com/test/apikeys)
stripe.api.key=sk_test_YOUR_STRIPE_SECRET_KEY
stripe.webhook.secret=whsec_YOUR_WEBHOOK_SECRET
```

### Step 4: Run Backend

```bash
# Build and run
mvn clean install
mvn spring-boot:run
```

✅ Backend running on `http://localhost:8080`

**Default Users Created:**
- Admin: `admin@pizza.com` / `admin123`
- Customer: `user@pizza.com` / `user123`

### Step 5: Load Sample Data

The application automatically creates users. To add pizzas:

```bash
# Connect to MySQL
mysql -u root -p pizza_ordering_db

# Run the SQL from data.sql
source /Users/Vishu/Downloads/crust-connect-system-main/pizza-ordering-backend/src/main/resources/data.sql
```

### Step 6: Test the API

```bash
# Test login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@pizza.com","password":"user123"}'

# Copy the token from response and test getting pizzas
curl -X GET http://localhost:8080/api/pizzas \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### Step 7: Run Frontend (Optional)

```bash
cd /Users/Vishu/Downloads/crust-connect-system-main
npm install
npm run dev
```

✅ Frontend running on `http://localhost:5173`

---

## 🧪 Quick API Test

### 1. Register New User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "test123"
  }'
```

### 2. Add to Cart
```bash
curl -X POST http://localhost:8080/api/cart/items \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"pizzaId": 1}'
```

### 3. View Cart
```bash
curl -X GET http://localhost:8080/api/cart \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 4. Place Order
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## 📋 API Endpoints Summary

### Public Endpoints
- `POST /api/auth/register` - Register
- `POST /api/auth/login` - Login
- `GET /api/pizzas` - Browse menu

### Customer Endpoints (Requires JWT)
- `GET /api/cart` - View cart
- `POST /api/cart/items` - Add to cart
- `POST /api/orders` - Place order
- `GET /api/orders` - Order history

### Admin Endpoints (Requires Admin JWT)
- `POST /api/pizzas` - Add pizza
- `GET /api/admin/orders` - View all orders
- `PUT /api/admin/orders/{id}/status` - Update order status

---

## 🔧 Troubleshooting

### "Access denied for user 'root'"
Update MySQL credentials in `application.properties`

### "Table doesn't exist"
Ensure `spring.jpa.hibernate.ddl-auto=update` in properties

### "JWT token expired"
Login again to get a new token (tokens expire after 24 hours)

### "Stripe API key invalid"
Add your test keys from Stripe dashboard

---

## 📚 Full Documentation

- **[Backend README](file:///Users/Vishu/Downloads/crust-connect-system-main/pizza-ordering-backend/README.md)** - Complete API docs
- **[Walkthrough](file:///Users/Vishu/.gemini/antigravity/brain/63502060-7e12-4fe3-b6b2-17e0623a6bd7/walkthrough.md)** - Implementation details
- **[Implementation Plan](file:///Users/Vishu/.gemini/antigravity/brain/63502060-7e12-4fe3-b6b2-17e0623a6bd7/implementation_plan.md)** - Architecture design

---

## ✅ What's Included

✨ **Complete Backend Features:**
- JWT Authentication
- Role-based Access Control
- Cart Management
- Order Processing
- Stripe Payment Integration
- Real-time WebSocket Updates
- Admin Dashboard APIs
- Comprehensive Error Handling

🎯 **Ready for Production:**
- Clean architecture
- Validation at all layers
- Security best practices
- Proper exception handling
- Logging and monitoring ready

---

## 🎉 You're All Set!

Your pizza ordering backend is ready to use. Start the server and begin testing the APIs!

For frontend integration, you'll need to:
1. Create API service layer in React
2. Add Axios with JWT interceptor
3. Connect to backend endpoints
4. Implement Stripe Elements for payment
5. Add WebSocket client for real-time updates
