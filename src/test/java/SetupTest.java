import data.TestDto;
import org.budtz.HiberSpeed;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

public class SetupTest {

    @Test
    public void testSetup(){
        HiberSpeed hiberSpeed = new HiberSpeed("data",
                "pg", System.getenv("hiberPass"),
                "testpg-db.caprover.diplomportal.dk:5432/pg", HiberSpeed.Dialect.postgreSQL, HiberSpeed.SchemaGeneration.update,
                true);
        try (Session session = hiberSpeed.openSession()) {
            Transaction transaction = session.beginTransaction();
            TestDto testDto = new TestDto();
            session.persist(testDto);
            transaction.commit();
        }
    }
}
