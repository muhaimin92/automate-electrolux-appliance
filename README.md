
# Automate Electrolux Appliances

This project provides a Spring Boot application to automate Electrolux appliances using a set of rules. The application integrates with Electrolux's API to control appliances based on conditions specified by the user.

## Getting Started

### Prerequisites

- Java 11 or higher
- MongoDB Atlas account (for the database)
- Electrolux Developer account (for API access) - [Electrolux Developer](https://developer.electrolux.one/)
- Visit [Electrolux API Documentation](https://developer.electrolux.one/documentation) for more details on API Endpoints.

### Configuration

First, configure the application properties. You can get the necessary keys and tokens from the Electrolux Developer portal.

Create a `application.properties` file in `src/main/resources` with the following content:

```properties
spring.application.name=automate-electrolux-appliances
spring.data.mongodb.uri=YOUR_MONGODB_URI

api.base-url=https://api.developer.electrolux.one
api.api-key=YOUR_API_KEY
api.access-token=YOUR_ACCESS_TOKEN
api.refresh-token=YOUR_REFRESH_TOKEN
```

Replace `YOUR_MONGODB_URI`, `YOUR_API_KEY`, `YOUR_ACCESS_TOKEN`, and `YOUR_REFRESH_TOKEN` with your actual values.

### API Endpoints

#### ApplianceController

- **GET /api/v1/appliances**
  - Description: Retrieves all appliance.
  - Response: List of `Appliance` .

- **GET /api/v1/appliances/{applianceId}/state**
  - Description: Retrieves the current state of the specified appliance.
  - Response: `ApplianceState` object containing the state details.

- **PUT /api/v1/appliances/{applianceId}/command**
  - Description: Sends a command to the specified appliance to change its state.
  - Request Body: 
    ```json
    {
      "sleepMode": "On"
    }
    ```
  - Response: `200 OK` on success.

#### ApplianceRuleController

- **POST /api/v1/rules**
  - Description: Creates a new rule for an appliance.
  - Request Body:
    ```json
    {
      "applianceId": "914922156_00:40614786-443E0745DB37",
      "action": [
        {
          "executeCommand": {
            "sleepMode": "On"
          }
        }
      ],
      "condition": [
        {
          "property": "targetTemperatureF",
          "value": 71.6,
          "operator": "eq"
        }
      ]
    }
    ```
  - Response: Created `ApplianceRule` object.

- **GET /api/v1/rules**
  - Description: Retrieves all rules.
  - Response: List of `ApplianceRule` objects.

- **DELETE /api/v1/rules/{ruleId}**
  - Description: Deletes the specified rule.
  - Response: `200 OK` on success.

### Setting Up and Configuring Rules

1. **Create a Rule:**
   - Use the `POST /api/v1/rules` endpoint to create a rule.
   - The request body should contain the `applianceId`, `action`, and `condition` as shown in the example above.

2. **Rule Structure:**
   - **Action:**
     - Specifies the command to execute when the rule is triggered.
     - Example: `{ "sleepMode": "On" }`
   - **Condition:**
     - Specifies the condition to be evaluated.
     - Example: `{ "property": "targetTemperatureF", "value": 71.6, "operator": "eq" }`
   - **Operator:**
     - The operator defines the type of comparison to be made.
       - `eq`: Equals
       - `neq`: Not equals
       - `gt`: Greater than
       - `lt`: Less than
       - `gte`: Greater than or equal to
       - `lte`: Less than or equal to
     - Example: `eq` means the condition is met if the `property` equals the `value`.

3. **Rule Evaluation:**
   - The application periodically (every minute) checks the state of each appliance and evaluates the conditions of all rules.
   - If a condition is met, the specified action is executed by sending a command to the appliance.


### Troubleshooting

- Ensure that your `application.properties` file is correctly configured with valid values.
- Make sure your MongoDB Atlas database is accessible.
- Check the logs for any errors during API interactions. The logs will provide detailed information about request and response bodies for debugging purposes.

### Contributing

Feel free to open issues or submit pull requests for any improvements or bug fixes.

### License

This project is licensed under the MIT License.

---
