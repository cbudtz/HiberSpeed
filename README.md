# HiberSpeed
A convention over configuration Hibernate wrapper for postgreSQL or MySQL.  
* Uses reflection to map entities and wraps cumbersome setup steps with defaults. 
* Uses c3p0 for connection pool
* Comes packed with MySQL and PostgreSQL (default) drivers

# Usage:
```
// "data": java package for @Entities
HiberSpeed hiberSpeed = new HiberSpeed("data",
  "username", "pass",
  "someurl.com:5432/defaultdb", HiberSpeed.Dialect.postgreSQL, HiberSpeed.SchemaGeneration.update,
  true);
try (Session session = hiberSpeed.openSession()) {
  Transaction transaction = session.beginTransaction();
  TestDto testDto = new TestDto();
  session.persist(testDto);
  transaction.commit();
}
  
```
