# Expense Tracker API............
A simple REST API to track your daily expenses. Built with Spring Boot and MySQL.

# What does it do ?

1.Create an account and login securely
2.Add categories (like Food, Transport, HealthCare)
3.Track expenses with date and amount
4.View all expenses anytime

# Tech i used
-> Java 21
-> Spring Boot
-> MySQL
-> JWT for auth
-> Maven

# Project Structure
src/main/java/
-> controller/     (handles requests)
-> service/        (business logic)
-> repository/     (database queries)
-> entities/       (database tables)
-> dto/            (request/response objects)
-> security/       (JWT and auth stuff)

# Other useful endpoints:
-> GET /api/expense - See all expenses
-> GET /api/category/get - See all categories
-> PUT /api/expense/update/{categoryId} - Update an expense
-> DELETE /api/expense/delete/{categoryId} - Delete an expense

# Database tables
The app creates 3 tables....
-> users - stores user accounts
-> categories - expense categories
-> expenses - all expense records with amount and date

# Things i learned
JWT authentication is tricky but powerful.Always use DTOs to avoid circular reference errors.

# Common issues I faced
Make sure your JWT token is valid and in the header as Bearer token.
Check if CustomUserDetails is properly returning the user object and 
password must be BCrypt encoded during registration.
