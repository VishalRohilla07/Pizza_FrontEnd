# Pizza Ordering & Management System - Backend

Production-ready Spring Boot backend for online pizza ordering system with JWT authentication, Stripe payment integration, and real-time order tracking.

## Tech Stack

- **Java 17**
- **Spring Boot 3.2.2**
- **Spring Security** with JWT
- **Spring Data JPA** with MySQL
- **Stripe** for payment processing
- **WebSocket** for real-time updates
- **Maven** for build management

## Features

### Authentication & Security
- JWT-based authentication
- Role-based access control (CUSTOMER, ADMIN)
- BCrypt password encryption
- Secure endpoints with Spring Security

### Customer Features
- Register and login
- Browse available pizzas
- Add pizzas to cart
- Update cart quantities
- Place orders
- Pay via Stripe
- Track order status in real-time
- View order history
- Cancel orders (if eligible)

### Admin Features
- View all orders with pagination
- Filter orders by status
- Update order status
- Manage pizza menu (CRUD operations)
- Real-time order notifications

### Payment Integration
- Stripe payment intent creation
- Webhook handling for payment events
- Automatic order confirmation on payment success

### Real-time Updates
- WebSocket integration for order status changes
- Live notifications to customers and admins

## Prerequisites

1. **Java 17** or higher
2. **MySQL 8.0** or higher
3. **Maven 3.6** or higher
4. **Stripe Account** (for payment integration)

## Setup Instructions

### 1. Database Setup

Create MySQL database:
```sql
CREATE DATABASE pizza_ordering_db;
```

### 2. Configuration

Update `src/main/resources/application.properties`:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/pizza_ordering_db
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD

# Stripe (replace with your keys)
stripe.api.key=sk_test_YOUR_STRIPE_SECRET_KEY
stripe.webhook.secret=whsec_YOUR_WEBHOOK_SECRET

# JWT Secret (generate a secure random string)
jwt.secret=YOUR_256_BIT_SECRET_KEY
```

### 3. Build and Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The server will start on `http://localhost:8080`

### 4. Initialize Sample Data

The application will automatically create tables. To add sample pizzas, run the SQL in `src/main/resources/data.sql`.

To create admin and test users:
```bash
# Use the registration endpoint or manually insert with BCrypt hashed passwords
# admin@pizza.com / admin123
# user@pizza.com / user123
```

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user

### Pizzas
- `GET /api/pizzas` - Get all available pizzas (public)
- `GET /api/pizzas/{id}` - Get pizza by ID (public)
- `POST /api/pizzas` - Create pizza (admin only)
- `PUT /api/pizzas/{id}` - Update pizza (admin only)
- `DELETE /api/pizzas/{id}` - Delete pizza (admin only)

### Cart
- `GET /api/cart` - Get user's cart
- `POST /api/cart/items` - Add item to cart
- `PUT /api/cart/items/{pizzaId}` - Update item quantity
- `DELETE /api/cart/items/{pizzaId}` - Remove item
- `DELETE /api/cart` - Clear cart

### Orders
- `POST /api/orders` - Create order from cart
- `GET /api/orders` - Get user's orders
- `GET /api/orders/{id}` - Get order details
- `POST /api/orders/{id}/cancel` - Cancel order

### Admin
- `GET /api/admin/orders` - Get all orders (paginated)
- `PUT /api/admin/orders/{id}/status` - Update order status

### Payment
- `POST /api/payment/create-intent` - Create payment intent
- `POST /api/payment/webhook` - Stripe webhook handler

### WebSocket
- Connect to `/ws` for real-time updates
- Subscribe to `/topic/orders/{userId}` for user-specific updates
- Subscribe to `/topic/admin/orders` for admin updates

## Testing

### Using cURL

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@test.com","password":"password123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@test.com","password":"password123"}'

# Get pizzas (use token from login response)
curl -X GET http://localhost:8080/api/pizzas \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Project Structure

```
src/main/java/com/pizza/ordering/
├── controller/          # REST API controllers
├── service/            # Business logic
├── repository/         # Data access layer
├── entity/             # JPA entities
├── dto/                # Data transfer objects
├── config/             # Configuration classes
├── security/           # Security components
├── exception/          # Custom exceptions
├── util/               # Utility classes
└── PizzaOrderingApplication.java
```

## Security Features

- JWT tokens expire after 24 hours
- Passwords encrypted with BCrypt (strength 10)
- CORS configured for frontend origins
- Role-based endpoint protection
- SQL injection prevention via JPA
- Proper error handling without exposing sensitive data

## Error Handling

All errors return standardized JSON response:
```json
{
  "timestamp": "2024-02-15T07:00:00",
  "status": 400,
  "message": "Error description",
  "data": null
}
```

## Development

### Running Tests
```bash
mvn test
```

### Building for Production
```bash
mvn clean package
java -jar target/ordering-1.0.0.jar
```

## Troubleshooting

### Database Connection Issues
- Verify MySQL is running
- Check database credentials in application.properties
- Ensure database exists

### JWT Token Issues
- Verify jwt.secret is set correctly
- Check token expiration time
- Ensure Authorization header format: `Bearer <token>`

### Stripe Integration
- Use test mode keys for development
- Configure webhook endpoint in Stripe dashboard
- Test with Stripe test cards: 4242 4242 4242 4242

## License

This project is for educational purposes.

## Support

For issues or questions, please create an issue in the repository.
