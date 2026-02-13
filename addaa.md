# Assignment No 3: Software Modeling Design (UML)
## Project Topic: AI Expense Tracker for Employee Reimbursement

**System Summary:**  
The AI Expense Tracker helps organizations manage employee expense reimbursements. Employees create **reports** and scan/upload receipts **inside a report only**. The system extracts receipt data (date, merchant, items, total, currency) and stores it. HR reviews, comments, approves, and marks receipts paid (one-by-one or bulk). Admin oversees everything and can manage organizations and users.

**Actors (Users):**
- **Employee**: creates reports, adds/scans receipts in own reports, exports/prints own report
- **HR**: reviews org employees’ reports/receipts, comments, approves, marks paid, creates employees
- **Admin**: everything HR can do + creates organizations + creates users + assigns roles

---

# 1) Use Case Diagram
> Mermaid does not have a dedicated UML use case syntax everywhere, so we represent it cleanly using a flowchart layout (standard in many submissions).

```mermaid
flowchart LR
  %% Actors
  EMP([Employee])
  HR([HR])
  ADM([Admin])

  %% System Boundary
  subgraph SYS[AI Expense Tracker System]
    UC1((Login))
    UC2((Create Report))
    UC3((View Reports))
    UC4((View Report Details))
    UC5((Scan/Upload Receipt))
    UC6((Extract Receipt Data))
    UC7((Edit Receipt))
    UC8((Delete Receipt))
    UC9((Export/Print Report))
    UC10((Add Comment))
    UC11((Approve Receipt))
    UC12((Bulk Approve Receipts))
    UC13((Mark Receipt Paid))
    UC14((Bulk Mark Paid))
    UC15((Create Employee Account))
    UC16((Create Organization))
    UC17((Assign Roles / Manage Users))
  end

  %% Employee Actions
  EMP --> UC1
  EMP --> UC2
  EMP --> UC3
  EMP --> UC4
  EMP --> UC5
  EMP --> UC7
  EMP --> UC8
  EMP --> UC9

  %% HR Actions
  HR --> UC1
  HR --> UC3
  HR --> UC4
  HR --> UC10
  HR --> UC11
  HR --> UC12
  HR --> UC13
  HR --> UC14
  HR --> UC15

  %% Admin Actions
  ADM --> UC1
  ADM --> UC3
  ADM --> UC4
  ADM --> UC10
  ADM --> UC11
  ADM --> UC12
  ADM --> UC13
  ADM --> UC14
  ADM --> UC15
  ADM --> UC16
  ADM --> UC17

  %% Internal include relations
  UC5 --> UC6

```

## 2) Sequence Diagram (Employee scans receipt inside a report)

> Scenario: Employee opens a report, uploads a receipt, system extracts data, stores receipt.

```mermaid
sequenceDiagram
  autonumber
  actor Employee
  participant UI as Vite React UI
  participant API as Express API
  participant OCR as AI/OCR Service
  participant DB as Supabase DB

  Employee->>UI: Open Report Details (/:id)
  UI->>API: GET /api/reports/:id/receipts (auth)
  API->>DB: Select receipts by report_id + org rules (RLS)
  DB-->>API: Receipts list
  API-->>UI: Receipts list
  UI-->>Employee: Show receipts + "Scan Receipt" button

  Employee->>UI: Upload receipt image
  UI->>API: POST /api/reports/:id/receipts (image + auth)
  API->>DB: Verify report belongs to employee + org membership
  DB-->>API: OK

  API->>OCR: Extract receipt data (merchant/date/items/total/currency)
  OCR-->>API: Parsed receipt data (JSON)

  API->>DB: Insert receipt (report_id, org_id, created_by, parsed fields)
  DB-->>API: Insert success + receipt id
  API-->>UI: 201 Created + receipt object
  UI-->>Employee: Show receipt in list + totals updated
```

# 3) Activity Diagram (Swimlane) — End-to-end reimbursement flow

> Swimlanes are represented using Mermaid subgraphs (lanes).

```mermaid
flowchart TB
  %% Swimlane: Employee
  subgraph L1[Employee Lane]
    E1([Start])
    E2[Login]
    E3[Create Report]
    E4[Open Report Details]
    E5[Upload or Scan Receipt]
    E6{More receipts?}
    E7[Export or Print Report]
    E8([End])
  end

  %% Swimlane: System
  subgraph L2[System Lane]
    S1[Validate session and role]
    S2[Extract receipt data using AI OCR]
    S3[Store receipt in database]
    S4[Update totals]
    S5[Lock receipt after approval]
  end

  %% Swimlane: HR
  subgraph L3[HR Lane]
    H1[View employee reports]
    H2[Open report receipts]
    H3[Add comment with name]
    H4{Approve?}
    H5[Approve single or bulk]
    H6{Mark paid?}
    H7[Mark paid single or bulk]
  end

  %% Flow
  E1 --> E2 --> S1 --> E3 --> E4 --> E5 --> S2 --> S3 --> S4 --> E6
  E6 -- Yes --> E5
  E6 -- No --> E7 --> H1 --> H2 --> H3 --> H4
  H4 -- Yes --> H5 --> S5 --> H6
  H4 -- No --> H6
  H6 -- Yes --> H7 --> E8
  H6 -- No --> E8

```

# 4) Class Diagram (Core domain model)

> This class diagram focuses on entities and relationships.

```mermaid
classDiagram
  direction LR

  class Organization {
    +uuid id
    +string name
    +datetime created_at
    +datetime updated_at
  }

  class User {
    +uuid id
    +string full_name
    +string email
    +uuid org_id
    +Role role
  }

  class Report {
    +uuid id
    +uuid org_id
    +uuid created_by
    +string name
    +string business_unit
    +string approver
    +string month
    +text business_justification
    +ReportStatus status
    +datetime created_at
    +datetime updated_at
  }

  class Receipt {
    +uuid id
    +uuid org_id
    +uuid report_id
    +uuid created_by
    +date date
    +string company_name
    +numeric total_amount
    +string currency
    +json items
    +string image_url
    +bool is_paid
    +datetime approved_at
    +uuid approved_by
    +datetime paid_at
    +uuid paid_by
    +datetime created_at
    +datetime updated_at
  }

  class ReceiptComment {
    +uuid id
    +uuid org_id
    +uuid receipt_id
    +uuid author_id
    +string author_name_snapshot
    +text content
    +datetime created_at
  }

  class AppLicense {
    +int id
    +uuid org_id
    +datetime paid_until
    +string last_payment_intent_id
    +datetime updated_at
  }

  class Role {
    <<enumeration>>
    employee
    hr
    admin
  }

  class ReportStatus {
    <<enumeration>>
    draft
    submitted
    approved
    paid
    rejected
  }

  Organization "1" --> "many" User : has
  Organization "1" --> "many" Report : owns
  Organization "1" --> "1" AppLicense : license
  User "1" --> "many" Report : creates
  Report "1" --> "many" Receipt : contains
  Receipt "1" --> "many" ReceiptComment : has
  User "1" --> "many" ReceiptComment : writes
```

