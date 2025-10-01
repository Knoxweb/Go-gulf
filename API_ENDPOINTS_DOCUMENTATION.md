# Go Gulf Apps - API Endpoints Documentation

## Overview

This document provides a comprehensive list of all API endpoints used across the Go Gulf transportation platform apps:
- **iOS Passenger App** (Go GulfCoast)
- **Android Passenger App** (Go GulfCoast)
- **iOS Driver App** (GoGulf Driver)

## Base URLs

### Passenger Apps (iOS & Android)
- **Production:** `https://app.go-gulfcoast.com/api/passenger/`
- **API Token:** `RK9AIh0GFJ640FZhe1eSTUpgoU1h48OmwqrbbDFGL9zqbtREix1hxUr`

### Driver App (iOS)
- **Production:** `https://app.go-gulfcoast.com/api/driver/`
- **API Token:** `RK9AIh0GFJ640FZhe1eSTUpgoU1h48OmwqrbbDFGL9zqbtREix1hxUr`

### External APIs
- **Google Maps Autocomplete:** `https://maps.googleapis.com/maps/api/place/autocomplete/json`
- **Google Maps Place Details:** `https://maps.googleapis.com/maps/api/place/details/json`

---

## Passenger App Endpoints

### Authentication & Registration

| Endpoint | Method | Description | Used In |
|----------|--------|-------------|---------|
| `login` | POST | Login with UID and mobile number | iOS, Android |
| `email-login` | POST | Login with email and password | iOS, Android |
| `social-login` | POST | Social media login (Google) | iOS |
| `register` | POST | Register new passenger account | iOS, Android |
| `first-update` | POST | First-time profile update after registration | iOS, Android |
| `first-social-update` | POST | First update for social login users | iOS |
| `forget-password` | POST | Request password reset | iOS, Android |
| `forget-password-opt-check` | POST | Verify OTP for password reset | iOS, Android |
| `forget-password-reset` | POST | Reset password with new credentials | iOS, Android |
| `status` | GET | Check app/user status | iOS, Android |
| `passenger/session-check` | POST | Verify active session | Android |
| `passenger/verification` | POST | Send verification code | Android |
| `passenger/authentication` | POST | Authenticate user | Android |
| `passenger/resend-verification` | POST | Resend verification code | Android |
| `passenger/verify-code` | POST | Verify code | Android |

### Profile Management

| Endpoint | Method | Description | Used In |
|----------|--------|-------------|---------|
| `update` | POST | Update user profile | iOS, Android |
| `update-profile` | POST | Update profile information | iOS |
| `profile-update/{endpoint}` | POST | Update specific profile field | iOS |
| `passenger/update-profile` | POST | Update passenger profile | Android |
| `passenger/profile` | GET | Get passenger profile | Android |
| `reset-password` | POST | Reset user password | iOS, Android |
| `language-update` | POST | Update language preference | iOS |

### Dashboard & Status

| Endpoint | Method | Description | Used In |
|----------|--------|-------------|---------|
| `dashboard-task` | GET | Get dashboard tasks and info | iOS |
| `passenger/dashboard` | GET | Get passenger dashboard data | Android |

### Payment & Cards

| Endpoint | Method | Description | Used In |
|----------|--------|-------------|---------|
| `add-card` | POST | Add new payment card | iOS, Android |
| `update-card/{id}` | POST | Update existing card | iOS |
| `activate-card/{id}` | POST | Activate/set default card | iOS, Android |
| `delete-card/{id}` | DELETE/POST | Delete payment card | iOS, Android |
| `passenger/card` | GET | Get saved cards | Android |
| `passenger/make-payment` | POST | Process payment | Android |
| `passenger/capture-payment` | POST | Capture payment | Android |

### Booking & Quotes

| Endpoint | Method | Description | Used In |
|----------|--------|-------------|---------|
| `calculate-fare` | POST | Calculate fare for route | iOS, Android |
| `quote-request` | POST | Request ride quote | iOS |
| `confirm-booking/{id}` | POST | Confirm booking | iOS, Android |
| `retry-booking/{id}` | POST | Retry failed booking | iOS, Android |
| `cancel-booking-request/{id}` | POST | Cancel booking request | iOS, Android |
| `cancel-booking/{id}` | POST | Cancel confirmed booking | iOS, Android |
| `booking-promo-code` | POST | Apply promo code to booking | iOS |
| `quote/flight-status` | POST | Check flight status | Android |
| `quote` | POST | Get quote | Android |
| `quote/request-job` | POST | Request job | Android |
| `quote/re-request-job` | POST | Re-request job | Android |
| `quote/cancel-requested-job` | POST | Cancel requested job | Android |

### Current Ride & Tracking

| Endpoint | Method | Description | Used In |
|----------|--------|-------------|---------|
| `quote/current-ride` | GET | Get current ride details | Android |
| `passenger/dod-cancel-booking` | POST | Driver on duty cancel booking | Android |
| `passenger/near-by-driver` | POST | Get nearby drivers | Android |
| `booking-chat-driver/{id}` | POST | Chat with driver | iOS |
| `booking-call-driver/{id}` | POST | Call driver | iOS |
| `booking-special-instruction/{id}` | POST | Update special instructions | iOS |
| `booking-flight-number/{id}` | POST | Update flight number | iOS |
| `booking-checkup-time/{id}` | POST | Update checkup time | iOS |

### Ratings & Reviews

| Endpoint | Method | Description | Used In |
|----------|--------|-------------|---------|
| `booking-review/{id}` | POST | Submit booking review | iOS, Android |
| `booking-skip-review/{id}` | POST | Skip review | iOS, Android |
| `booking-tip/{id}` | POST | Add tip to booking | iOS |
| `booking-skip-tip/{id}` | POST | Skip tip | iOS |
| `passenger/rating` | POST | Rate driver | Android |

### Booking History & Schedule

| Endpoint | Method | Description | Used In |
|----------|--------|-------------|---------|
| `passenger/bookings?type={type}` | GET | Get bookings by type (completed, cancelled) | Android |
| `passenger/schedule?type={type}` | GET | Get scheduled bookings | Android |
| `passenger/booking-detail?booking_id={id}` | GET | Get booking details | Android |
| `passenger/booking-cancel` | POST | Cancel booking | Android |
| `cancel-info/{id}` | GET | Get cancellation info | iOS |

### Addresses & Shortcuts

| Endpoint | Method | Description | Used In |
|----------|--------|-------------|---------|
| `add-address` | POST | Add favorite address | iOS, Android |
| `edit-address/{id}` | POST | Edit address | iOS, Android |
| `delete-address/{id}` | DELETE/POST | Delete address | iOS, Android |

### Lead Passengers

| Endpoint | Method | Description | Used In |
|----------|--------|-------------|---------|
| `add-lead-passenger` | POST | Add lead passenger | iOS |
| `edit-lead-passenger/{id}` | POST | Edit lead passenger | iOS |
| `delete-lead-passenger/{id}` | DELETE | Delete lead passenger | iOS |

### Invoices & Receipts

| Endpoint | Method | Description | Used In |
|----------|--------|-------------|---------|
| `invoice-pdf/{id}` | GET | Download invoice PDF | iOS, Android |
| `invoice-pdf` | POST | Download multiple invoices | iOS |
| `invoice-status/{id}` | GET | Get invoice status | iOS |
| `invoice-retry/{id}` | POST | Retry invoice generation | iOS |
| `passenger/invoice` | GET | Get invoices | Android |
| `passenger/invoice/download` | POST | Download invoice | Android |

### Notifications

| Endpoint | Method | Description | Used In |
|----------|--------|-------------|---------|
| `notification-read/{id}` | POST | Mark notification as read | iOS, Android |
| `passenger/notification` | GET | Get notifications | Android |
| `passenger/read-notification` | POST | Mark as read | Android |

### Support & Help

| Endpoint | Method | Description | Used In |
|----------|--------|-------------|---------|
| `support` | POST | Submit support ticket | iOS, Android |
| `passenger/support` | POST | Submit support request | Android |

### Account Management

| Endpoint | Method | Description | Used In |
|----------|--------|-------------|---------|
| `logout` | POST | Logout user | iOS, Android |
| `delete-account` | POST | Delete user account | iOS, Android |
| `passenger/account-delete` | POST | Delete passenger account | Android |
| `passenger/device-token` | POST | Update device token | Android |
| `passenger/accept-terms` | POST | Accept terms and conditions | Android |

### Fleet & Vehicles

| Endpoint | Method | Description | Used In |
|----------|--------|-------------|---------|
| `fleet` | GET | Get available fleet/vehicles | iOS |
| `driver-sort-orders` | POST | Sort driver orders | iOS |
| `drivers` | POST | Update driver preferences | iOS |

---

## Driver App Endpoints

### Authentication & Registration

| Endpoint | Method | Description |
|----------|--------|-------------|
| `login` | POST | Driver login with UID and mobile |
| `email-login` | POST | Login with email and password |
| `social-login` | POST | Social media login |
| `register` | POST | Register new driver account |
| `first-update` | POST | First-time profile update |
| `first-social-update` | POST | First update for social login |
| `forget-password` | POST | Request password reset |
| `forget-password-opt-check` | POST | Verify OTP for password reset |
| `forget-password-reset` | POST | Reset password |
| `status` | GET | Check driver status |

### Profile & Account

| Endpoint | Method | Description |
|----------|--------|-------------|
| `update` | POST | Update driver profile |
| `update-profile` | POST | Update profile information |
| `profile-update/{endpoint}` | POST | Update specific profile field |
| `reset-password` | POST | Reset password |
| `language-update` | POST | Update language preference |
| `logout` | POST | Logout driver |
| `delete-account` | POST | Delete driver account |

### Location & Availability

| Endpoint | Method | Description |
|----------|--------|-------------|
| `update-location` | POST | Update driver location |
| `online` | POST | Set online/offline status |
| `change-availability` | POST | Change availability status |

### Job Management

| Endpoint | Method | Description |
|----------|--------|-------------|
| `job-accept/{id}` | POST | Accept job offer |
| `job-reject/{id}` | POST | Reject job offer |
| `job-start/{id}` | POST | Start accepted job |
| `job-cancel/{id}` | POST | Cancel job |
| `job-dod/{id}` | POST | Driver on duty (arrived at pickup) |
| `job-pob/{id}` | POST | Passenger on board (trip started) |
| `job-completed/{id}` | POST | Complete job/trip |
| `job-no-show-up/{id}` | POST | Mark passenger as no-show |
| `job-change-driver/{id}` | POST | Assign job to different driver |

### Ratings & Reviews

| Endpoint | Method | Description |
|----------|--------|-------------|
| `job-review/{id}` | POST | Submit job review |
| `job-skip-review/{id}` | POST | Skip review |

### Communication

| Endpoint | Method | Description |
|----------|--------|-------------|
| `job-chat-passenger/{id}` | POST | Chat with passenger |
| `job-call-passenger/{id}` | POST | Call passenger |

### Fleet & Vehicle Management

| Endpoint | Method | Description |
|----------|--------|-------------|
| `fleets` | POST | Add/manage fleet |
| `fleet-documents/{id}` | POST | Upload fleet documents |
| `fleet-status/{id}` | POST | Update fleet status (active/inactive) |
| `documents` | POST | Upload driver documents |

### Earnings & Statements

| Endpoint | Method | Description |
|----------|--------|-------------|
| `invoice-pdf/{id}` | GET | Download invoice PDF |
| `invoice-pdf` | POST | Download multiple invoices |
| `statement-pdf/{id}` | GET | Download statement PDF |

### Banking

| Endpoint | Method | Description |
|----------|--------|-------------|
| `bank` | POST | Add/update bank account |

### Company & Chauffeur

| Endpoint | Method | Description |
|----------|--------|-------------|
| `add-chauffeur` | POST | Add chauffeur |
| `remove-chauffeur` | POST | Remove chauffeur |
| `accept-reject-chauffeur` | POST | Accept/reject chauffeur invitation |
| `leave-company` | POST | Leave company |

### Notifications

| Endpoint | Method | Description |
|----------|--------|-------------|
| `notification-read/{id}` | POST | Mark notification as read |

### Support

| Endpoint | Method | Description |
|----------|--------|-------------|
| `support` | POST | Submit support ticket |

### Dashboard

| Endpoint | Method | Description |
|----------|--------|-------------|
| `dashboard-task` | GET | Get dashboard tasks |

### Booking Management

| Endpoint | Method | Description |
|----------|--------|-------------|
| `confirm-booking/{id}` | POST | Confirm booking |
| `cancel-booking-request/{id}` | POST | Cancel booking request |
| `cancel-info/{id}` | GET | Get cancellation info |

---

## Firebase Endpoints

### Passenger App

| Endpoint | Collection/Path | Description |
|----------|-----------------|-------------|
| `passenger_legal_notice` | Firestore | Legal notices for passengers |
| `passenger_term_of_use` | Firestore | Terms of use |
| `passenger_privacy_policy` | Firestore | Privacy policy |
| `promo_info` | Firestore | Promotional information |
| `bookings/{booking_id}` | Realtime DB | Real-time booking updates |
| `driver_location/{driver_id}` | Realtime DB | Real-time driver location |

### Driver App

| Endpoint | Collection/Path | Description |
|----------|-----------------|-------------|
| `driver_jobs` | Realtime DB | Real-time job offers |
| `driver_location/{driver_id}` | Realtime DB | Driver location updates |
| `bookings/{booking_id}` | Realtime DB | Real-time booking updates |

---

## Google Maps API Endpoints

### Autocomplete

**Endpoint:** `https://maps.googleapis.com/maps/api/place/autocomplete/json`

**Parameters:**
- `input` - Search query
- `location` - User's current location (lat,lng)
- `radius` - Search radius
- `components` - Country filter
- `key` - API key

**Used In:** iOS Passenger App, Android Passenger App

### Place Details

**Endpoint:** `https://maps.googleapis.com/maps/api/place/details/json`

**Parameters:**
- `placeid` - Google Place ID
- `key` - API key

**Used In:** iOS Passenger App

---

## API Request Headers

### Standard Headers

All API requests include the following headers:

```
Content-Type: application/json
Authorization: Bearer {access_token}
Accept: application/json
```

### iOS Apps Additional Headers

```
app-token: {app_token}
lang: {language_code}
```

### Android App Additional Headers

```
app-token: {app_token}
language: {language_code}
```

---

## HTTP Methods Used

| Method | Usage |
|--------|-------|
| **GET** | Retrieve data (profile, bookings, invoices, etc.) |
| **POST** | Create/update data, submit forms, authentication |
| **DELETE** | Remove data (cards, addresses, etc.) |
| **PUT** | Update existing resources (less common) |

---

## Response Format

### Success Response

```json
{
  "status": true,
  "message": "Success message",
  "data": {
    // Response data
  }
}
```

### Error Response

```json
{
  "status": false,
  "message": "Error message",
  "errors": {
    // Field-specific errors
  }
}
```

---

## Authentication Flow

### Passenger Apps

1. **Phone Login:**
   - Send phone number → Firebase Auth
   - Receive OTP → Verify
   - Call `login` endpoint with UID
   - Receive access token

2. **Email Login:**
   - Call `email-login` with credentials
   - Receive access token

3. **Social Login:**
   - Authenticate with Google
   - Call `social-login` with token
   - Receive access token

### Driver App

Same flow as passenger apps with driver-specific endpoints.

---

## Pagination

Endpoints that return lists typically support pagination:

**Query Parameters:**
- `page` - Page number (default: 1)
- `limit` - Items per page (default: 10)
- `type` - Filter type (completed, pending, etc.)

**Example:**
```
GET /passenger/bookings?type=completed&page=1&limit=20
```

---

## File Upload Endpoints

### Multipart Form Data

Endpoints that accept file uploads use `multipart/form-data`:

- `register` - Profile image
- `first-update` - Profile image
- `update` - Profile image
- `fleet-documents/{id}` - Vehicle documents (driver)
- `documents` - Driver documents

**Form Fields:**
- `profile_image` - Image file
- `{field_name}` - Other form fields

---

## Real-Time Updates

### Firebase Realtime Database Paths

**Passenger App:**
- `/bookings/{booking_id}/status` - Booking status changes
- `/bookings/{booking_id}/driver_location` - Driver location updates
- `/bookings/{booking_id}/chat` - Chat messages

**Driver App:**
- `/driver_jobs/{driver_id}` - New job offers
- `/bookings/{booking_id}/status` - Booking status changes
- `/bookings/{booking_id}/chat` - Chat messages

---

## Rate Limiting

- Standard rate limit: 100 requests per minute per user
- Burst limit: 20 requests per second
- Location updates: Every 5-10 seconds (driver app)

---

## Error Codes

| Code | Description |
|------|-------------|
| 200 | Success |
| 201 | Created |
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |
| 422 | Validation Error |
| 429 | Too Many Requests |
| 500 | Internal Server Error |

---

## API Versioning

Current API version: **v1** (implicit in base URL)

Future versions will use explicit versioning:
- `https://app.go-gulfcoast.com/api/v2/passenger/`

---

## Security

### Authentication
- Bearer token authentication
- Token expiration: 30 days
- Refresh token mechanism (if implemented)

### Data Encryption
- All API calls use HTTPS
- Sensitive data encrypted in transit
- Payment data tokenized (Stripe)

### API Token
- Static app token for additional security layer
- Validates app authenticity
- Prevents unauthorized API access

---

## Testing Endpoints

### Development/Staging
- Base URL: `https://api.go-gulfcoast.com/api/` (commented out in code)
- Test Stripe Key: `pk_test_p81rsCxWfc5fhCDq72t9aOZP00TomPa4p4`

### Production
- Base URL: `https://app.go-gulfcoast.com/api/`
- Production Stripe Key: Configured via environment variables

---

## Notes

1. **Endpoint Variations:** Some endpoints have slight variations between iOS and Android apps but serve the same purpose.

2. **Firebase Integration:** Real-time features use Firebase Realtime Database alongside REST API.

3. **Google Maps:** Location services use Google Maps API for autocomplete and geocoding.

4. **Stripe Integration:** Payment processing handled via Stripe SDK with backend confirmation.

5. **VoIP Notifications:** Driver app uses VoIP push notifications for instant job alerts (iOS).

6. **Background Location:** Driver app continuously updates location in background.

7. **API Evolution:** Some older endpoints (commented out in code) suggest API has been refactored over time.

---

## Summary

This documentation covers **100+ API endpoints** across:
- **Passenger Apps:** ~70 endpoints
- **Driver App:** ~50 endpoints
- **External APIs:** Google Maps, Firebase
- **Payment:** Stripe integration

All apps communicate with a centralized backend API, with role-specific endpoints for passengers and drivers. Real-time features are handled through Firebase, while REST APIs handle CRUD operations and business logic.
