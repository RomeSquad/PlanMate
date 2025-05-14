# 🗂️ PlanMate - Task Management CLI App

PlanMate is a Command-Line Interface (CLI) task management application developed using the **Kotlin** programming language. It follows the **Test-Driven Development (TDD)** approach and adheres to **SOLID principles**, making it a maintainable and extensible system for project and task collaboration.

---

## 🚀 Features

### 👥 User Management
- Two types of users: **admin** and **mate**
- User authentication with **hashed passwords (MD5)**
- **Admins** can create users of type **mate**

### 📋 Project & Task Management
- **Admins** can create, edit, and delete **projects** and **states**
- **Mates** can create, edit, and delete **tasks** within projects
- **Dynamic states**: not hardcoded (e.g., TODO, In Progress, Done) and editable by admins per project
- Projects are isolated; each has its own set of tasks and states

### 🧾 Task Display
- Display tasks in **swimlanes UI** within the terminal grouped by state

### 📜 Audit System
- Full change history of tasks and projects, by ID
- Tracks **who**, **what**, and **when** for each change (e.g., user `abc` changed task `XYZ-001` from `InProgress` to `InDevReview` at `2025/05/24 8:00 PM`)

---

## 🧠 Architecture

PlanMate follows a **layered architecture** divided into:

### 1. `data`
- Responsible for data storage and retrieval
- Stores data in **CSV files** (to start) and supports **MongoDB migration**
- Repositories like `AuthenticationRepository`, `ProjectsRepository`, etc.
- Follows **Dependency Inversion**: logic depends on abstractions

### 2. `logic`
- Contains all business logic and **core entities**
- Pure Kotlin classes and interfaces with 100% **unit test coverage**
- Implements all business rules without knowledge of data or UI

### 3. `ui`
- Handles CLI interaction with the user
- Only accesses the `logic` layer

