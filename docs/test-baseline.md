
# Initial Test Baseline

## Environment
- JDK: 21.0.12.1
- Maven: 3.9.12
- Existing project compilation target: Java 8

## Results
Command: `mvn clean test`

- Tests: 26
- Passed: 25
- Failed: 1
- Errors: 0
- Skipped: 0

## Known Issues

### Producer Validation
`TestSistema.testRegistrarTamboERROR4` fails.

Expected: `ERROR_4`
Actual: `OK`

`Sistema.registrarTambo()` does not validate whether
the referenced producer exists.

### External Browser Side Effect
`Sistema.mapaEstado()` opens Google Static Maps from
the application logic.

The API rejects unauthenticated requests.
Tests should not open browsers or require external services.

## Next Steps
- Fix missing producer validation in a separate bugfix PR.
- Decouple map visualization from business logic.
- Migrate Maven and compilation settings to Java 21.
- Expand algorithm regression tests.
