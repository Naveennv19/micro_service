////////////////////////////////////////////////////////////////////////////////         Sky Ride         \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\


* About :

    SkyRide is an all-in-one mobility service that links passengers with trusted drivers for safe and reliable transportation, offering airport rides, 
    city travel, long-distance journeys, and hourly rentals.

 
* Key Features:

    - Multi-role system: Separate dashboards for customers, drivers, and administrators.
    - Flexible booking: Instant rides or scheduled trips with date/time selection.
    - Real-time management: Driver assignment, and ride status updates.
    - Business-friendly: Corporate accounts and billing features.
    - 24/7 availability: Round-the-clock service with professional drivers.
    - Safety-focused: Background-checked drivers.
    
* User Roles:

    - Customers: Book rides, track trips, manage bookings.
    - Drivers: Accept rides, track earnings, manage availability.
    - Admins: Oversee operations, assign drivers, monitor system performance.




* Frameworks used in frontend:
    
    - React - Frontend JavaScript library.
    - Vite - Fast build tool and development server.
    - TypeScript - Type-safe JavaScript.
    
    - Tailwind CSS - Utility-first CSS framework
    
           //Routing & State Management\\
    - React Router DOM - Client-side routing
    - React Hook Form - Form state management
    
    - Zod - Schema validation
    - JWT Decode - JWT token handling
    - Axios - HTTP client for API calls


* Tool / Frameworks used in backend:

    - Spring Boot - Main framework for building RESTful APIs and handling HTTP requests/responses
    - Spring Web - Enables the creation of REST endpoints using @RestController, @RequestMapping, etc.
    - Spring Data JPA - Simplifies database operations using repositories (e.g., BookingRepository)
    
    - Hibernate - ORM (Object-Relational Mapping) provider used by JPA to map Java objects to database tables
    - Lombok - Reduces boilerplate code with annotations like @RequiredArgsConstructor, @Builder, @Getter, etc.
    - JWT - Handles authentication via tokens using a utility like JwtAuthService



* How i built :
    
    steps:
        1) Created a folder in desktop to build cab-micro-service application.
        2) Open spring.io create maven project with required dependence's.
        3) Extract the zip file.
        4) Setup postgres database ( Create a server in pgadmin4 and then database ).
        5) Connect backend to database by database name created in pgadmin4.
        6) Build models ( user model, booking model).
        7) Build controllers to perform actions in the backend ( adminController, bookingController, customerController, userController ).
        8) Use of jwt token for maintaining authenticity and authorization of login credentions.
        9) Repository does interacting with the database ( userRepository, bookingRepository).
        


* building routes:

    1) Registration ( customer, driver ):
        
        Method: POST
        URL: http://localhost:8081/api/user/register
        
        Description: Registers a new customer or driver role based.
        Request Headers: (json) Content-Type: application/json
        Request Body: 
            {
                "name": "customer_1",
                "email": "customer_1@example.com",
                "password": "securepassword123",
                "phone": "123456789",
                "role": "CUSTOMER"
            }
        
        Response:
            {
                "message": "User registered successfully"
            }


    2) Login ( role : admin || customer || driver ).
        
        Method: POST
        URL: http://localhost:8081/api/user/login
        
        Description: Authenticates a user and returns a JWT.
        Request Headers: (json) Content-Type: application/json
        Request Body: 
            {
                "email": "customer_1@example.com",
                "password": "securepassword123"
            }

         Response:
            {
                "role": "CUSTOMER",
                "phone": "9873543210",
                "name": "customer_1",
                "id": 3,
                "email": "customer_1@example.com",
                "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjdXN0b21lcl8xQGV4YW1wbGUuY29tIiwiaWF0IjoxNzUxMDMzNjQyLCJleHAiOjE3NTE2Mzg0NDJ9.v-m9SeLCIOZJjSsOwdGcDX2iwZTtxnu7dEZoTGxZJPI"
            }
            
            
    3) Profile.
        
        Method: GET
        URL: http://localhost:8081/api/customers/profile
        
        Description: Get customer profile using JWT token.
        Request Header:
            Authorization: Bearer <token>
            Content-Type: application/json

        Response:
            {
                "id": 3,
                "name": "customer_1",
                "email": "customer_1@example.com",
                "password": "securepassword123",
                "phone": "9873543210",
                "role": "CUSTOMER",
                "status": "AVAILABLE",
                "createdAt": "2025-06-20T16:23:11.074892"
            }

    
    4) Get bookings.
        
        Method: GET
        URL: http://localhost:8080/user/booking
        
        Description: Get all bookings of customer logged-in.
        Request Header:
            Authorization: Bearer <token>
            Content-Type: application/json

        Response:
            [
                {
                    "id": 1,
                    "pickupLocation": "Whitefield",
                    "dropLocation": "Ooty",
                    "date": "2025-06-25",
                    "time": "10:30",
                    "status": "assigned",
                    "driverId": 2,
                    "rideType": "outstation_travel",
                    "driverName": "John Driver",
                    "customerName": "customer_1"
                },
                {
                    "id": 8,
                    "pickupLocation": "yelahanka",
                    "dropLocation": "nandi hills",
                    "date": "2025-06-25",
                    "time": "10:30",
                    "status": "assigned",
                    "driverId": 18,
                    "rideType": "local_travel",
                    "driverName": "tom Driver",
                    "customerName": "customer_1"
                },
                {
                    "id": 14,
                    "pickupLocation": "yelahanka",
                    "dropLocation": "nandi hills",
                    "date": "2025-06-25",
                    "time": "10:30",
                    "status": "pending",
                    "driverId": null,
                    "rideType": "local_travel",
                    "driverName": null,
                    "customerName": "customer_1"
                }
            ]


    6) Booking.
        
        Method: POST
        URL: http://localhost:8080/user/booking
        
        Description: customer booking trip.
        Request Header:
            Authorization: Bearer <token>
            Content-Type: application/json

        Request Body: 
            {
                "type": "LOCAL_TRAVEL",
                "pickupLoc": "yelahanka",
                "dropLoc": "nandi hills",
                "packageHrs": "8",
                "dateTime": "2025-06-25",
                "time": "10:30",
                "returnDateTime": "2025-06-27",
                "returnTime": "18:00"
              }
        
        Response:
            {
                "message": "Booking created successfully",
                "bookingId": 14
            }


    7) Complete Booking By Driver.
        
        Method: POST
        URL: http://localhost:8080/user/driver/complete-booking/{bookingId}
        
        Description: Once the trip is done, Then driver completes the assigned ride.
        Request Header:
            Authorization: Bearer <token>
            Content-Type: application/json

       
        
        Response:
            {
                "message": "Booking marked as completed"
            }


    8) Admin assign driver.
        
        Method: POST
        URL: http://localhost:8080/user/booking
        
        Description: Admin assign driver who are available to booking.
        Request Header:
            Authorization: Bearer <token>
            Content-Type: application/json

        Request Body: 
            {
                "type": "LOCAL_TRAVEL",
                "pickupLoc": "yelahanka",
                "dropLoc": "nandi hills",
                "packageHrs": "8",
                "dateTime": "2025-06-25",
                "time": "10:30",
                "returnDateTime": "2025-06-27",
                "returnTime": "18:00"
              }
        
        Response:
            {
                "message": "Booking created successfully",
                "bookingId": 14
            }
            
            
    9) Admin assign driver.
        
        Method: POST
        URL: http://localhost:8080/user/booking
        
        Description: Admin assign driver who are available to booking.
        Request Header:
            Authorization: Bearer <token>
            Content-Type: application/json

        Request Body: 
            {
                "type": "LOCAL_TRAVEL",
                "pickupLoc": "yelahanka",
                "dropLoc": "nandi hills",
                "packageHrs": "8",
                "dateTime": "2025-06-25",
                "time": "10:30",
                "returnDateTime": "2025-06-27",
                "returnTime": "18:00"
              }
        
        Response:
            {
                "message": "Booking created successfully",
                "bookingId": 14
            }

    
    10) Get drivers.
        
        Method: POST
        URL: http://localhost:8080/user/driver
        
        Description: get all drivers.
        Request Header:
            Authorization: Bearer <token>
            Content-Type: application/json
        
        Response:
            [
                {
                    "isAvailable": false,
                    "email": "john.driver@example.com",
                    "name": "John Driver",
                    "id": 2
                },
                {
                    "isAvailable": false,
                    "email": "mike.driver@example.com",
                    "name": "mike Driver",
                    "id": 17
                },
                {
                    "isAvailable": false,
                    "email": "tom.driver@example.com",
                    "name": "tom Driver",
                    "id": 18
                },
                {
                    "isAvailable": true,
                    "email": "adam.driver@example.com",
                    "name": "adam driver",
                    "id": 22
                },
                {
                    "isAvailable": true,
                    "email": "park.driver@example.com",
                    "name": "park driver",
                    "id": 25
                },
                {
                    "isAvailable": true,
                    "email": "grace.driver@example.com",
                    "name": "grace",
                    "id": 26
                },
                {
                    "isAvailable": true,
                    "email": "tony.driver@example.com",
                    "name": "tony driver",
                    "id": 24
                }
            ]
        
        

        11) Get all bookings.
        
        Method: POST
        URL: http://localhost:8080/user/bookings
        
        Description: get all drivers.
        Request Header:
            Authorization: Bearer <token>
            Content-Type: application/json
        
        Response:
            [
                {
                    "id": 1,
                    "pickupLocation": "Whitefield",
                    "dropLocation": "Ooty",
                    "date": "2025-06-25",
                    "time": "10:30",
                    "status": "assigned",
                    "driverId": 2,
                    "rideType": "outstation_travel",
                    "driverName": "John Driver",
                    "customerName": "customer_1"
                },
                {
                    "id": 3,
                    "pickupLocation": "Whitefield",
                    "dropLocation": "Ooty",
                    "date": "2025-06-25",
                    "time": "10:30",
                    "status": "assigned",
                    "driverId": 2,
                    "rideType": "airport_travel",
                    "driverName": "John Driver",
                    "customerName": "customer_5"
                },
                {
                    "id": 2,
                    "pickupLocation": "Tata nagar",
                    "dropLocation": "Mysuru",
                    "date": "2025-06-25",
                    "time": "10:30",
                    "status": "assigned",
                    "driverId": 17,
                    "rideType": "airport_travel",
                    "driverName": "mike Driver",
                    "customerName": "John Doe"
                },
                {
                    "id": 4,
                    "pickupLocation": "hyd",
                    "dropLocation": "chennai",
                    "date": "2025-06-25",
                    "time": "10:30",
                    "status": "assigned",
                    "driverId": 17,
                    "rideType": "airport_travel",
                    "driverName": "mike Driver",
                    "customerName": "customer_7"
                },
                {
                    "id": 5,
                    "pickupLocation": "ooty",
                    "dropLocation": "bangalore",
                    "date": "2025-06-25",
                    "time": "10:30",
                    "status": "completed",
                    "driverId": 18,
                    "rideType": "airport_travel",
                    "driverName": "tom Driver",
                    "customerName": "customer_7"
                },
                {
                    "id": 6,
                    "pickupLocation": "1212",
                    "dropLocation": "213321",
                    "date": "2025-06-25",
                    "time": "10:30",
                    "status": "completed",
                    "driverId": 18,
                    "rideType": "airport_travel",
                    "driverName": "tom Driver",
                    "customerName": "customer_7"
                },
                {
                    "id": 7,
                    "pickupLocation": "sdfvsfdfasf",
                    "dropLocation": "sdadfsadfas",
                    "date": "2025-06-25",
                    "time": "10:30",
                    "status": "completed",
                    "driverId": 18,
                    "rideType": "airport_travel",
                    "driverName": "tom Driver",
                    "customerName": "customer_7"
                },
                {
                    "id": 8,
                    "pickupLocation": "yelahanka",
                    "dropLocation": "nandi hills",
                    "date": "2025-06-25",
                    "time": "10:30",
                    "status": "assigned",
                    "driverId": 18,
                    "rideType": "local_travel",
                    "driverName": "tom Driver",
                    "customerName": "customer_1"
                },
                {
                    "id": 9,
                    "pickupLocation": "23e2we",
                    "dropLocation": "2w3e2w3",
                    "date": "3322-02-23",
                    "time": "02:32",
                    "status": "pending",
                    "driverId": null,
                    "rideType": "local_travel",
                    "driverName": null,
                    "customerName": "customer_5"
                },
                {
                    "id": 10,
                    "pickupLocation": "a",
                    "dropLocation": "b",
                    "date": "2025-02-12",
                    "time": "12:32",
                    "status": "pending",
                    "driverId": null,
                    "rideType": "local_travel",
                    "driverName": null,
                    "customerName": "customer_11"
                },
                {
                    "id": 12,
                    "pickupLocation": "tata nagar",
                    "dropLocation": "mysuru",
                    "date": "2025-05-30",
                    "time": "09:30",
                    "status": "completed",
                    "driverId": 22,
                    "rideType": "local_travel",
                    "driverName": "adam driver",
                    "customerName": "customer_13"
                },
                {
                    "id": 11,
                    "pickupLocation": "tata nagar",
                    "dropLocation": "white field",
                    "date": "2025-06-29",
                    "time": "20:30",
                    "status": "completed",
                    "driverId": 22,
                    "rideType": "local_travel",
                    "driverName": "adam driver",
                    "customerName": "customer_12"
                },
                {
                    "id": 13,
                    "pickupLocation": "bangalore",
                    "dropLocation": "chennai",
                    "date": "2025-06-29",
                    "time": "08:30",
                    "status": "completed",
                    "driverId": 24,
                    "rideType": "outstation_travel",
                    "driverName": "tony driver",
                    "customerName": "customer_14"
                },
                {
                    "id": 14,
                    "pickupLocation": "yelahanka",
                    "dropLocation": "nandi hills",
                    "date": "2025-06-25",
                    "time": "10:30",
                    "status": "pending",
                    "driverId": null,
                    "rideType": "local_travel",
                    "driverName": null,
                    "customerName": "customer_1"
                }
            ]
