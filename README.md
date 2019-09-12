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

```java
Integer age;        // nullable
```

- In the **Controller class** -> `onAction_uxSave()` eventhandler, we check whether or not the value in the TextField is null and set the value appropriately

```java
// Check if the textField is an int (ternary operator)
Integer age = isNumeric(uxAge.getText()) ? Integer.parseInt(uxAge.getText()) : null;
```

- In the **PersonDB class** -> `getPersonList()` method, look at the **while** loop. We use the `rs.wasNull()` method to check if **the previous rs query was null** - if it is assign `null` to the varaible **so that our object contains null values when it is read from the database**

```java
Integer age = rs.getInt(4);         // nullable
if(rs.wasNull())
	age = null;

returnValue.add(new Person(id, name, nickname, age));
```

- In the **PersonDB class** -> updatePerson(Person oldPerson, Person newPerson)` method, the sql update query checks more than just the id - **it checks that every newPerson value matches the oldPerson value (all the AND statements)**. Also note that we **check whether the old values were `null` (the IS NULL AND ? IS NULL statements)**

```java
// Build the sql query
String sql = "UPDATE persons " +
              "SET `name` = ?," +
              	   "`nickname` = ?, " +
                   "`age` = ? " +
              "WHERE `id` = ? " +
                 "AND `name` = ? " +
                 "AND (`nickname` = ? OR " +
                      "`nickname` IS NULL AND ? IS NULL) " +
                 "AND (`age` = ? OR " +
                      "`age` IS NULL AND ? IS NULL)";
```

- Further down in the **PersonDB class** -> `updatePerson(Person oldPerson, Person newPerson)` method, when we prepare the statement with the **?** parameters, we **check whether or not our newPerson values are null** and use the `stmt.setNull(int parameterIndex, Types columnType)` method to add a `NULL` to the database column.

```java
// Nullable - nickname : String
if(newPerson.getNickname() == null || newPerson.getNickname().isEmpty()) {
	System.out.println("Nickname is null");
        stmt.setNull(2, Types.VARCHAR);
}
else {
	stmt.setString(2, newPerson.getNickname());
}
```

- Further down in the **PersonDB class** -> `updatePerson(Person oldPerson, Person newPerson)` method, when we compare the oldPerson values, note that **we only check the NULL values of the age (Integer) - not the nickname (String)**. This is because the `setInt(int parameterIndex, int value)` **assigns an int - not an Integer (not nullable)**

```java
// Nullable - age : Integer
if(oldPerson.getAge() == null || oldPerson.getAge() == 0) {
	System.out.println("Age is null");
        stmt.setNull(8, Types.INTEGER);
}
else {
	stmt.setInt(8, oldPerson.getAge());
}
```


