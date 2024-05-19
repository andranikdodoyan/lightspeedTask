import model.Query;

public class Main {

  public static void main(String[] args) {
    String query = "SELECT a.name AS nsd, count(book.id), sum(book.cost) \n"
        + "FROM author AS a "
        + "LEFT JOIN book ON (author.id = book.author_id) "
        + "GROUP BY author.name "
        + "HAVING COUNT(*) > 1 AND SUM(book.cost) > 500 "
        + "LIMIT 10;";

    String query2 = "SELECT * FROM CUSTOMERS \n"
        + "WHERE AGE = 25 OR salary < 4500";

    try {
      Query r1 = SQLParser.parseSQLFromString(query);
      Query r2 = SQLParser.parseSQLFromString(query2);

    } catch (Exception e) {

    }

  }
}
