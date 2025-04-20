# BTO System
by SC2002_FCSB_GRP5

A modular Build-To-Order (BTO) flat management system developed in Java, designed for use by Applicants, HDB Officers, and HDB Managers. The system supports core HDB functionalities such as flat application, officer project registration, flat booking approval, and enquiry handling.

## Features

### Applicants
- View available BTO projects
- Submit BTO applications
- Withdraw existing applications
- Submit, edit, delete and view enquiries

### HDB Officers
- View available BTO projects
- Submit BTO applications
- Withdraw existing applications
- Submit, edit, delete and view enquiries
- Register for managing a BTO project
- Do flat bookings
- Generate booking receipts
- Respond to enquiries

### HDB Managers
- View all project applications and statistics
- Respond to enquiries
- Manage officer registrations and approvals
- Manage applicant registrations and approvals 

---

## System Architecture

The application follows the MVC architecture with clear separation of concerns:

- **Model**: Contains entity classes like `Applicant`, `Application`, `BTOProject`
- **View**: Role-specific views handle all user I/O via CLI
- **Controller**: Delegates user actions to services
- **Service**: Core business logic (e.g., applying, booking, validation)
- **Persistence**: CSV-based data storage using custom loaders

---

## SOLID Principles

- **SRP**: Services and file managers are strictly separated
- **OCP**: Loaders support new user/project types without modifying core logic
- **LSP**: `User` superclass ensures substitutability of `Applicant`, `Officer`, and `Manager`
- **ISP**: Views and services are segregated by user role
- **DIP**: Services depend on abstract data loaders, not concrete CSV logic

---

## 💾 Data Storage

All data is stored in `.csv` files:
- `ApplicantList.csv`
- `OfficerList.csv`
- `ManagerList.csv`
- `ProjectList.csv`
- `ApplicationList.csv`
- `EnquiryList.csv`

---

## Tools Used

- **Java (JDK 17)** — core language
- **Eclipse** — development environment
- **Visual Paradigm** — UML diagram creation
- **Mermaid Live Editor** — sequence diagram generation

---

## Running the Project

1. Clone the repository or download the source code.
2. Open the project in Eclipse (or your preferred IDE).
3. Run `Main.java`.
4. Choose your role and interact via the CLI menus.

---

## Documentation

### Javadoc
Javadoc documentation for all public classes and methods is available under the `/docs` directory (if generated). Includes service layer, controllers, and model entities.

### Diagrams
- UML Class diagram
- Sequence diagram 

