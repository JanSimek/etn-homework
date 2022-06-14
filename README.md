[![Build Actions Status](https://github.com/JanSimek/etn-homework/actions/workflows/Build.yml/badge.svg)](https://github.com/JanSimek/etn-homework/actions) [![codebeat badge](https://codebeat.co/badges/b1050c0a-7806-4434-8b59-d2d7958358fe)](https://codebeat.co/projects/github-com-jansimek-etn-homework-master)

# etnRest homework

## Usage

To run automated tests execute the following command:

```gradle test```

To start the application run:

```gradle bootRun```

Or use the provided wrapper `gradlew`/`gradlew.bat` script.

## API

| Endpoint                                    | Method | Description                  | Response codes                                                                   |
|---------------------------------------------|--------|------------------------------|----------------------------------------------------------------------------------|
| api/v1/frameworks                           | GET    | List of all frameworks       | 200                                                                              |
| api/v1/frameworks                           | POST   | Create a new framework       | 201 (success)<br/>409 (framework already exists)                                 |
| api/v1/frameworks/{id}                      | PUT    | Update an existing framework | 204 (success)<br/>404 (ID does not exit)                                         |
| api/v1/frameworks/{id}                      | DELETE | Delete an existing framework | 204 (success)<br/>404 (ID does not exit)                                         |
| api/v1/frameworks/{name}/versions/{version} | POST   | Add a new framework version  | 201 (success)<br/>404 (framework does not exit)<br/>409 (version already exists) |
| api/v1/frameworks/search                    | GET    | Search for a framework       | 200 (success)                                                                    |