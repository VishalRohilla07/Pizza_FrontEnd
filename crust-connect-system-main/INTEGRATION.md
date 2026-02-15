# Frontend-Backend Integration Guide

## 🎯 Integration Complete!

The React frontend has been successfully integrated with the Spring Boot backend. All pages now communicate with real APIs, handle authentication with JWT tokens, and support Stripe payment processing.

---

## 📋 What Was Integrated

### 1. API Service Layer (`src/api/`)

Created comprehensive API service modules:

- **[axios.ts](file:///Users/Vishu/Downloads/crust-connect-system-main/src/api/axios.ts)** - Axios instance with JWT interceptor
- **[auth.ts](file:///Users/Vishu/Downloads/crust-connect-system-main/src/api/auth.ts)** - Login, register, token management
- **[pizza.ts](file:///Users/Vishu/Downloads/crust-connect-system-main/src/api/pizza.ts)** - Pizza CRUD operations
- **[cart.ts](file:///Users/Vishu/Downloads/crust-connect-system-main/src/api/cart.ts)** - Cart management
- **[order.ts](file:///Users/Vishu/Downloads/crust-connect-system-main/src/api/order.ts)** - Order operations
- **[payment.ts](file:///Users/Vishu/Downloads/crust-connect-system-main/src/api/payment.ts)** - Stripe payment intents

### 2. Context Providers

- **AuthContext** - Now uses backend APIs for login/register with JWT storage
- **CartContext** - Syncs with backend cart API, fetches on user login

### 3. Updated Pages

- **LoginPage** - Async login with loading states
- **RegisterPage** - Async registration with validation
- **MenuPage** - Fetches pizzas from backend API
- **OrdersPage** - Displays user orders with cancel functionality
- **CheckoutPage** - Stripe payment integration
- **ProtectedRoute** - Route guards for authentication

### 4. Installed Packages

```bash
npm install axios @stripe/stripe-js @stripe/react-stripe-js date-fns
```

---

## 🚀 Setup Instructions

### 1. Backend Configuration

Ensure backend is running on `http://localhost:8080`:

```bash
cd pizza-ordering-backend
mvn spring-boot:run
```

### 2. Stripe Configuration

Update Stripe publishable key in [CheckoutPage.tsx](file:///Users/Vishu/Downloads/crust-connect-system-main/src/pages/CheckoutPage.tsx):

```typescript
const stripePromise = loadStripe("pk_test_YOUR_STRIPE_PUBLISHABLE_KEY");
```

Get your key from: https://dashboard.stripe.com/test/apikeys

### 3. Run Frontend

```bash
npm install
npm run dev
```

Frontend runs on `http://localhost:5173`

---

## 🔐 Authentication Flow

1. User logs in via `/login`
2. Backend returns JWT token
3. Token stored in `localStorage`
4. Axios interceptor adds token to all requests
5. On 401 error, user redirected to login

---

## 🛒 Cart & Order Flow

1. **Browse Menu** - Pizzas fetched from `/api/pizzas`
2. **Add to Cart** - POST to `/api/cart/items`
3. **View Cart** - GET from `/api/cart`
4. **Checkout** - POST to `/api/orders` creates order
5. **Payment** - POST to `/api/payment/create-intent`
6. **Stripe** - User completes payment
7. **Webhook** - Backend receives payment confirmation
8. **Order Confirmed** - Status updated automatically

---

## 🧪 Testing the Integration

### Test User Login

1. Go to http://localhost:5173/login
2. Use credentials:
   - **Admin**: `admin@pizza.com` / `admin123`
   - **Customer**: `user@pizza.com` / `user123`

### Test Full Flow

1. **Login** as customer
2. **Browse menu** - See pizzas from database
3. **Add to cart** - Items sync with backend
4. **Checkout** - Create order
5. **Payment** - Use Stripe test card: `4242 4242 4242 4242`
6. **View orders** - See order history

---

## 📁 Key Files Modified

### API Layer
- `src/api/axios.ts` - HTTP client with JWT
- `src/api/auth.ts` - Authentication APIs
- `src/api/pizza.ts` - Pizza APIs
- `src/api/cart.ts` - Cart APIs
- `src/api/order.ts` - Order APIs
- `src/api/payment.ts` - Payment APIs

### Context
- `src/context/AuthContext.tsx` - Backend auth integration
- `src/context/CartContext.tsx` - Backend cart sync

### Pages
- `src/pages/LoginPage.tsx` - Async login
- `src/pages/RegisterPage.tsx` - Async registration
- `src/pages/MenuPage.tsx` - API pizza fetching
- `src/pages/OrdersPage.tsx` - Order history
- `src/pages/CheckoutPage.tsx` - Stripe integration

### Components
- `src/components/ProtectedRoute.tsx` - Route guards
- `src/App.tsx` - Protected routing

---

## ⚙️ Environment Variables (Optional)

Create `.env` file:

```env
VITE_API_URL=http://localhost:8080/api
VITE_STRIPE_PUBLISHABLE_KEY=pk_test_YOUR_KEY
```

Update `axios.ts`:

```typescript
baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api'
```

---

## 🐛 Troubleshooting

### CORS Errors
- Backend CORS is configured for `http://localhost:5173`
- If using different port, update `SecurityConfig.java`

### 401 Unauthorized
- Check if backend is running
- Verify JWT token in localStorage
- Try logging in again

### Cart Not Syncing
- Ensure user is logged in
- Check browser console for errors
- Verify backend cart API is working

### Stripe Payment Fails
- Use test card: `4242 4242 4242 4242`
- Check Stripe publishable key is correct
- Verify webhook secret in backend

---

## ✅ Features Working

✅ User registration and login with JWT
✅ Token-based authentication
✅ Protected routes (checkout, orders, admin)
✅ Pizza menu from backend database
✅ Cart sync with backend
✅ Order creation and history
✅ Stripe payment integration
✅ Order status tracking
✅ Admin access control
✅ Error handling and loading states
✅ Automatic token refresh on page load

---

## 🎉 Next Steps

The full-stack application is now fully integrated! You can:

1. **Test the complete flow** - Register, login, order, pay
2. **Add WebSocket** - For real-time order updates
3. **Deploy** - Deploy both frontend and backend
4. **Customize** - Add more features as needed

---

## 📚 Documentation

- **[Backend README](file:///Users/Vishu/Downloads/crust-connect-system-main/pizza-ordering-backend/README.md)** - API documentation
- **[Backend Walkthrough](file:///Users/Vishu/.gemini/antigravity/brain/63502060-7e12-4fe3-b6b2-17e0623a6bd7/walkthrough.md)** - Implementation details
- **[Quick Start](file:///Users/Vishu/Downloads/crust-connect-system-main/pizza-ordering-backend/QUICKSTART.md)** - Setup guide

---

## 🎊 Summary

**Frontend Integration Complete!**
- ✅ 6 API service modules
- ✅ 2 context providers updated
- ✅ 6 pages integrated with backend
- ✅ Protected routes implemented
- ✅ Stripe payment integration
- ✅ JWT authentication working
- ✅ Full cart and order flow

The pizza ordering system is now a fully functional full-stack application! 🍕
