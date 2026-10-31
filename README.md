# Commercial Fleet Insurance CRM App 🚚

A test production-grade, distributed **Microservice Architecture** designed to track commercial trucking assets, automate fleet policy underwriting, and enforce live compliance tracking. 

Built as a cloud-native platform, this project showcases secure data modeling, automated tiered financial logic, and persistent storage orchestrations running inside a local **Kubernetes (Rancher Desktop)** cluster.

---

## 🏗️ Architecture & Component Overview

The system is deployed as a **Mono-Repo** consisting of two decoupled Spring Boot microservices interacting via internal cluster networking, backed by a persistent relational database:

*   **`insurance-backend` (Port 8080):** The core REST API and Business Logic Engine. Enforces domain data immutability, manages state mutations, and runs the tiered premium rating system.
*   **`insurance-frontend` (Port 8081):** A lightweight Thymeleaf-driven user interface wrapped in Bootstrap CSS. It acts as an API consumer, displaying real-time compliance badges and providing dynamic asset enrollment.
*   **PostgreSQL Database (Port 5432):** The relational persistence layer, backed by a Kubernetes `PersistentVolumeClaim` mapping directly to the physical storage disk to guarantee zero data loss on pod life-cycles.

---

## 🧠 Core Business Logic Features

### 1. Data Integrity & Immutability Constraints
To prevent commercial insurance and contract fraud, core identifiers are explicitly hardened at both the application memory and database layers (`updatable = false`):
*   **Policy Numbers, VINs, and License Plates** are permanently locked upon initial registration. Any physical alteration or contract modification requires issuing a brand new record block.

### 2. Tiered Fleet Premium Calculator Engine
The application self-governs its underwriting using an internal automated rating engine based on commercial fleet size brackets (\$200 base contract processing fee + scaling truck rates):
*   **Small Operators (1–2 Trucks):** \$600.00 / monthly per truck.
*   **Medium Fleets (3–5 Trucks):** \$500.00 / monthly per truck *(Volume Discount Applied)*.
*   **Large Enterprises (6+ Trucks):** \$400.00 / monthly per truck *(Maximum Volume Discount Applied)*.

### 3. Dynamic Re-Rating & Asset Appending
When an agent appends a new truck asset to an existing owner's fleet, the system automatically checks the updated inventory size, re-evaluates the volume discount bracket, and dynamically scales the owner's monthly premium bill in a single, safe transaction.

---

## 🚀 Local Deployment Guide (MacBook / Rancher Desktop)

### 1. Prerequisites
Ensure **Rancher Desktop** is installed with the Kubernetes engine enabled, and local CLI tools configured (`kubectl`, `docker`).

### 2. Build the Microservice Containers
To inject images directly into Rancher's internal image store without pushing to an external registry, set shell context and compile using the optimized multi-stage `Dockerfiles`:

```bash
# Point shell to Rancher container runtime context
eval \$(rdctl shell)

# Build Backend Data Engine
cd insurance-backend
docker build -t insurance-backend:local .

# Build Frontend UI
cd ../insurance-frontend
docker build -t insurance-frontend:local .
```

### 3. Orchestrate the Kubernetes Stack
Deploy the entire persistent microservice mesh using the unified manifest:

```bash
cd ../k8s
kubectl apply -f insurance-stack.yaml
```

Verify that the local storage claims bind and the service pods reach a running execution state:
```bash
kubectl get pvc
kubectl get pods -w
```

### 4. Access the Live Platform
Once the orchestration initializes successfully, open browser and navigate to:
👉 **`http://localhost:32081`**

*   **Compliance Shield Dashboard:** Review active driver compliance tracking (Carlos Martinez renders as **Green/Active**, while Alpha Logistics flashes **Red/Expired** via database initialization seeds).
*   **Persistence Validation Test:** Append a truck to an operator, delete all running cluster pods manually via terminal (`kubectl delete pods --all`), and refresh the browser. The auto-healing cluster will spin up brand-new containers instantly while data remains perfectly saved inside the PostgreSQL volume directory.
