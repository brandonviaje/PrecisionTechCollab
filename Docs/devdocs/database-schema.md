# Database Schema

## Tables

### users
| Field      | Type     | Notes              |
|------------|----------|--------------------|
| id         | UUID     | Primary key        |
| email      | String   | Unique             |
| password   | String   | Hashed             |

### projects
| Field        | Type     | Notes             |
|--------------|----------|-------------------|
| id           | UUID     | Primary key       |
| owner_id     | UUID     | FK → users.id     |
| created_at   | Date     | Default: now()    |

> ERD diagram available in `/docs/images/erd.png`
