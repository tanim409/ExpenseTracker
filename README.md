# Expense Tracker API............
A simple REST API to track your daily expenses. Built with Spring Boot and MySQL.

# What does it do ?

1.Create an account and login securely
2.Add categories (like Food, Transport, HealthCare)
3.Track expenses with date and amount
4.View all expenses anytime

# Tech i used
1. Java 21
2. Spring Boot
3. MySQL
4. JWT for auth
5. Maven

# Project Structure
src/main/java/
1. controller/     (handles requests)
2. service/        (business logic)
3. repository/     (database queries)
4. entities/       (database tables)
5. dto/            (request/response objects)
6. security/       (JWT and auth stuff)

# Other useful endpoints:
1. GET /api/expense - See all expenses
2. GET /api/category/get - See all categories
3. PUT /api/expense/update/{categoryId} - Update an expense
4. DELETE /api/expense/delete/{categoryId} - Delete an expense

# Database tables
The app creates 3 tables....
1. users - stores user accounts
2. categories - expense categories
3. expenses - all expense records with amount and date

# Things i learned
JWT authentication is tricky but powerful.Always use DTOs to avoid circular reference errors.

# Common issues I faced
Make sure your JWT token is valid and in the header as Bearer token.
Check if CustomUserDetails is properly returning the user object and 
password must be BCrypt encoded during registration.
