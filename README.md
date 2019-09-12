# OptimisticConcurrency

This is a sample JavaFX app that grabs data from a MySQL database and updates it.
It handles **null values** and **optimisitc concurrency** (will not allow you to change a value that has been changed since you accessed the data)

---
## SETUP
This project relies on a MySQL database named sample. It is supplied as a .sql file. Import it into PhpMyAdmin and create a user:

user: **admin**

password: **password**

Give it full privileges over the sample database (or at the minimum, **SELECT** & **UPDATE**)

---
## Take note:

- In the **Person class** we declare an **Integer** instead of **int** to hold our age variable - **Integer is nullable**

- In the **Controller class** -> `onAction_uxSave()` eventhandler, we check whether or not the value in the TextField is null and set the value appropriately

- In the **PersonDB class** -> `getPersonList()` method, look at the **while** loop. We use the `rs.wasNull()` method to check if **the previous rs query was null** - if it is assign `null` to the varaible