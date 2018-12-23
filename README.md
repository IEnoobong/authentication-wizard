[![Build Status](https://travis-ci.org/IEnoobong/authentication-wizard.svg?branch=master)](https://travis-ci.org/IEnoobong/authentication-wizard)
[![Maintainability](https://api.codeclimate.com/v1/badges/7468d0f832a62a9f41c1/maintainability)](https://codeclimate.com/github/IEnoobong/authentication-wizard/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/7468d0f832a62a9f41c1/test_coverage)](https://codeclimate.com/github/IEnoobong/authentication-wizard/test_coverage)
[![codecov](https://codecov.io/gh/IEnoobong/authentication-wizard/branch/master/graph/badge.svg)](https://codecov.io/gh/IEnoobong/authentication-wizard) 
# Authentication Wizard
An account-password backend system in Java, using Spring Boot that meets the following criteria

- The user can sign up as a new user. They are expected to see an email in their inbox. 

- The user can click the link in the email to verify the account.

- The user can log in only after their email address is verified.

- The user cannot create another account with the same email from an existing account.

- A hacker if committing Man-in-the-middle attacks cannot know the plain-text password.

- The database owner (You) cannot store plaintext password or SHA256-hashed password in your DB.

- Fields in the input object must be checked if they are required and if they have the right data types.

## Building 

