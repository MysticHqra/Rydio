# Rydio Platform - GitHub Copilot Instructions

## Project Overview
Rydio is a modern peer-to-peer vehicle rental platform built with React TypeScript frontend and Spring Boot backend. The platform enables users to list their own vehicles and rent from others in their community.

## Architecture
- **Frontend**: React 18 + TypeScript + Tailwind CSS
- **Backend**: Spring Boot 3.2.0 + Spring Security + JWT
- **Database**: H2 (development) / PostgreSQL (production ready)
- **Authentication**: JWT-based with BCrypt password hashing

## Key Features
- User authentication and authorization (USER/ADMIN roles)
- Peer-to-peer vehicle marketplace
- Smart AI-powered vehicle recommendations
- Comprehensive admin dashboard
- Real-time vehicle availability
- Professional vehicle images integration
- Mobile-responsive design

## Development Guidelines
- Follow TypeScript best practices and maintain type safety
- Use functional components with React hooks
- Implement proper error handling and loading states
- Follow Spring Boot conventions and DDD principles
- Use JPA/Hibernate for database operations
- Implement comprehensive exception handling
- Write tests for critical business logic

## Code Patterns
- Use custom hooks for reusable logic
- Implement protected routes based on authentication and roles
- Use context for global state management
- Follow RESTful API design principles
- Use DTOs for request/response objects
- Implement proper validation on both frontend and backend

## Recent Enhancements
- Fixed backend server startup issues (endpoint mapping conflicts)
- Integrated real vehicle data into smart recommendations
- Added comprehensive test vehicle data with professional images
- Enhanced peer-to-peer vehicle sharing capabilities
- Updated documentation with current feature set

## Project Status
✅ **Production Ready** - Complete peer-to-peer vehicle rental platform with comprehensive features, real data integration, and professional image assets.