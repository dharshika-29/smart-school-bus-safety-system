# Smart School Bus Safety System

**Frontend:** React (Vite) &nbsp;|&nbsp; **Backend:** Spring Boot (Java) &nbsp;|&nbsp; **Database:** MySQL

```
smart-bus-safety/
├── backend/     Spring Boot (Java) + MySQL
└── frontend/    React (Vite)
```

Rendu servers **separate-a** run aagum: backend `:8080`, frontend `:5173`.
Backend-la CORS already React dev server-ku allow pannirukken.

---
## 1. Prerequisites (install pannunga)

| Tool | Version |
|---|---|
| JDK | 21 |
| Maven | 3.9+ (IntelliJ-la already built-in) |
| MySQL | 8.x |
| Node.js | 20+ |

---
## 2. MySQL setup

MySQL-la login pannitu:
```sql
CREATE DATABASE bus_safety;
```
Tables automatic-a create aagum (`spring.jpa.hibernate.ddl-auto=update`), manual-a create panna vendam.

---
## 3. Backend run panna

```bash
cd backend
```
`src/main/resources/application.properties`-la idha update pannunga:
```properties
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
app.jwt.secret=<32+ character random string>
app.device.key=<oru vera random string>
```

IntelliJ-la open pannitu `SmartBusSafetyApplication.java` run pannunga, **or** terminal-la:
```bash
mvn spring-boot:run
```
Server start aagum: `http://localhost:8080`
Demo bus (`TN-45-1234`) automatic-a create aagum first run-la.

Check: `http://localhost:8080/api/health` → `{"status":"ok"}`

---
## 4. Frontend run panna

```bash
cd frontend
npm install
npm run dev
```
Browser-la open: `http://localhost:5173`

`.env` file-la already API URL set pannirukken:
```
VITE_API_URL=http://localhost:8080/api
```

---
## 5. API endpoints

| Method | URL | Who | Auth |
|---|---|---|---|
| GET | `/api/health` | Anyone | - |
| POST | `/api/auth/signup` | Parent | - |
| POST | `/api/auth/login` | Parent | - |
| GET | `/api/auth/me` | Parent | JWT |
| GET | `/api/bus/my` | Parent | JWT |
| GET | `/api/notifications` | Parent | JWT |
| PUT | `/api/bus/{busNumber}/location` | GPS device / driver app | header `x-device-key` |

Bus location update test panna (curl):
```bash
curl -X PUT http://localhost:8080/api/bus/TN-45-1234/location \
  -H "Content-Type: application/json" \
  -H "x-device-key: YOUR_DEVICE_KEY" \
  -d '{"status":"RUNNING","locationName":"Anna Nagar","lat":13.0878,"lng":80.2085}'
```
Status values: `NOT_STARTED`, `RUNNING`, `STOPPED`, `REACHED_SCHOOL`

---
## 6. Deployment (free-tier options)

### Database — Railway or Aiven (free MySQL)
Idhu maathi maathi aagum, so deploy panra time-la current free MySQL host-a naan check panni sollaren; அப்போ enkitta sollunga "MySQL free hosting details venum" nu.

### Backend — Render (Web Service, Docker or "Native Environment: Java")
1. GitHub-la repo push pannunga (`backend/` folder-oda).
2. Render → New → Web Service → repo select.
3. Build Command: `mvn clean package -DskipTests`
4. Start Command: `java -jar target/smart-bus-safety-0.0.1-SNAPSHOT.jar`
5. Environment variables: `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `APP_JWT_SECRET`, `APP_DEVICE_KEY`, `APP_CORS_ALLOWED_ORIGINS` (spring maps `SPRING_DATASOURCE_URL` env var to `spring.datasource.url` property automatically).

### Frontend — Vercel or Netlify
1. Repo-la `frontend/` root directory-a set pannunga.
2. Build Command: `npm run build`, Output: `dist`
3. Environment variable: `VITE_API_URL` = unga deployed backend URL + `/api`

Deploy pannura pothu correct steps venumna sollunga, adha time-la current Render/Railway UI-a check pannitu exact steps tharen.

---
## 7. Security notes
- Passwords BCrypt-oda hash pannirukkom (plain text save aagadhu).
- JWT token 7 days expire aagum.
- Bus location update endpoint parent JWT-oda access panna mudiyadhu — thani `x-device-key` venum.
- `.env` (frontend) and `application.properties`-la irukkura real secrets GitHub-ku push panna koodaadhu — production-la environment variables use pannunga.
