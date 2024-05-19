import static sql.Keyword.valueOf;

import model.Column;
import model.ColumnTable;
import model.GroupBy;
import model.HavingClause;
import model.Join;
import model.Query;
import model.Source;
import model.WhereClause;
import sql.Condition;
import sql.Keyword;
import sql.Operation;
import types.JoinType;
import types.SourceType;

public class SQLParser {

  public static final String NEW_LINE = "\n";
  public static final String SPACE = "\\s+";
  public static final String DOT = ".";
  public static final String DOT_REGEX = "\\.";

  public static final String COMMA_SPACE = ", ";
  public static final String OPENING_P = "(";
  public static final String CLOSING_P = ")";

  public static final String ALIAS = " AS ";
  public static final String ON = " ON ";

  public static final String EQ = " = ";


  private SQLParser() {
  }

  public static Query parseSQLFromString(String queryString) throws Exception {
    Query resultQuery = new Query();
    queryString = adjustNewLines(queryString);
    String[] choppedQueryLineByLine = queryString.split(NEW_LINE);
    for (String choppedQueryLine : choppedQueryLineByLine) {
      parseChoppedQueryLine(choppedQueryLine.trim(), resultQuery);
    }

    return resultQuery;
  }

  private static String adjustNewLines(String queryString) {
    queryString = queryString.replace("\n", " ");
    String[] choppedQuery = queryString.toUpperCase().split(SPACE);
    for (String s : choppedQuery) {
      try {
        Keyword k = valueOf(s);
        int index = queryString.indexOf(s);
        if (!k.equals(Keyword.SELECT) && !(queryString.substring(index - 2, index)
            .contains(NEW_LINE))) {
          queryString = queryString.substring(0, index) + NEW_LINE + queryString.substring(index);
        }
      } catch (IllegalArgumentException ignored) {
      }
    }
    return queryString;
  }

  private static void parseChoppedQueryLine(String choppedQueryLine, Query query) throws Exception {
    String operation = choppedQueryLine.toUpperCase().split(SPACE)[0];
    switch (valueOf(operation)) {
      case SELECT:
        constructColumns(choppedQueryLine.substring(operation.length() + 1), query);
        break;
      case FROM:
        constructFromSources(choppedQueryLine.substring(operation.length() + 1), query);
        break;
      case LEFT:
        constructJoins(choppedQueryLine.substring(JoinType.LEFT_JOIN.getValue().length() + 1),
            query, JoinType.LEFT_JOIN);
        break;
      case RIGHT:
        constructJoins(choppedQueryLine.substring(JoinType.LEFT_JOIN.getValue().length() + 1),
            query, JoinType.RIGHT_JOIN);
        break;
      case INNER:
        constructJoins(choppedQueryLine.substring(JoinType.LEFT_JOIN.getValue().length() + 1),
            query, JoinType.INNER_JOIN);
        break;
      case FULL:
        constructJoins(choppedQueryLine.substring(JoinType.LEFT_JOIN.getValue().length() + 1),
            query, JoinType.FULL_JOIN);
        break;
      case GROUP:
        constructGroup(choppedQueryLine.substring("GROUP BY".length() + 1), query);
        break;
      case WHERE:
        constructWhereClause(choppedQueryLine.substring(operation.length() + 1), query);
        break;
      case HAVING:
        constructHavingClause(choppedQueryLine.substring(operation.length() + 1), query);
        break;
      case LIMIT:
        query.setLimit(Integer.parseInt(choppedQueryLine.substring(operation.length() + 1, choppedQueryLine.length() - 1)));
        break;
      default:
        throw new Exception(
            "Something went wrong when chopping. Each Line should start with a special keyword");
    }
  }

  private static void constructHavingClause(String line, Query query) {
    HavingClause havingClause = new HavingClause();
    if (line.toUpperCase().contains(String.format(" %s ", Condition.OR))) {
      String[] temp = line.toUpperCase().split(String.format(" %s ", Condition.OR));
      for (String t : temp) {
        havingClause.getOr().add(t);
      }
    } else if (line.toUpperCase().contains(String.format(" %s ", Condition.AND))) {
      String[] temp = line.toUpperCase().split(String.format(" %s ", Condition.AND));
      for (String t : temp) {
        havingClause.getAnd().add(t);
      }
    } else {
      havingClause.setCondition(line);
    }

    query.setHavingClause(havingClause);
  }

  private static void constructWhereClause(String line, Query query) {
    WhereClause whereClause = new WhereClause();
    if (line.toUpperCase().contains(String.format(" %s ", Condition.OR))) {
      String[] temp = line.toUpperCase().split(String.format(" %s ", Condition.OR));
      for (String t : temp) {
        whereClause.getOr().add(t);
      }
    } else if (line.toUpperCase().contains(String.format(" %s ", Condition.AND))) {
      String[] temp = line.toUpperCase().split(String.format(" %s ", Condition.AND));
      for (String t : temp) {
        whereClause.getAnd().add(t);
      }
    } else {
      whereClause.setCondition(line);
    }

    query.setWhereClause(whereClause);
  }


  private static void constructColumns(String line, Query query) {
    String[] choppedColumnsAsString = line.split(COMMA_SPACE);
    for (String columnString : choppedColumnsAsString) {
      Operation operation = null;
      String table = null;
      String alias = null;
      String column;

      if (columnString.toUpperCase().contains(ALIAS)) {
        int aliasIndex = columnString.toUpperCase().indexOf(ALIAS);
        alias = columnString.substring(aliasIndex + ALIAS.length());
        columnString = columnString.substring(0, aliasIndex);
      }

      if (columnString.contains(OPENING_P) && columnString.contains(CLOSING_P)) {
        int opening = columnString.indexOf(OPENING_P);
        int closing = columnString.indexOf(CLOSING_P);
        operation = Operation.valueOf(columnString.toUpperCase().substring(0, opening));
        columnString = columnString.substring(opening + 1, closing);
      }

      if (columnString.contains(DOT)) {
        int dotIndex = columnString.indexOf(DOT);
        table = columnString.substring(0, dotIndex);
        column = columnString.substring(dotIndex + 1);
      } else {
        column = columnString;
      }

      Column columnParsed = new Column(column, operation, alias, table);
      query.getColumns().add(columnParsed);
    }

  }

  private static void constructFromSources(String line, Query query) {
    String[] choppedFromSourcesAsString = line.split(COMMA_SPACE);
    for (String fromSource : choppedFromSourcesAsString) {
      Source sourceParsed = parseSource(fromSource, SourceType.IMPLICIT);
      query.getFromSource().add(sourceParsed);

    }
  }

  private static Source parseSource(String source, SourceType type) {
    String alias = null;

    if (source.toUpperCase().contains(ALIAS)) {
      int aliasIndex = source.toUpperCase().indexOf(ALIAS);
      alias = source.substring(aliasIndex + ALIAS.length());
      source = source.substring(0, aliasIndex);
    }

    String table = source;
    return new Source(table, alias, type);
  }

  private static void constructJoins(String line, Query query, JoinType joinType) {
    String[] choppedJoinsAsString = line.split(ON);

    String tableHand = choppedJoinsAsString[0];
    Source table = parseSource(tableHand, SourceType.EXPLICIT);
    query.getFromSource().add(table);

    String onClause = choppedJoinsAsString[1].substring(1, choppedJoinsAsString[1].length() - 1);
    String[] choppedOnClause = onClause.split(EQ);

    ColumnTable columnTable1 = new ColumnTable(choppedOnClause[0].split(DOT_REGEX)[0],
        choppedOnClause[0].split(DOT_REGEX)[1]);
    ColumnTable columnTable2 = new ColumnTable(choppedOnClause[1].split(DOT_REGEX)[0],
        choppedOnClause[1].split(DOT_REGEX)[1]);

    Join join = new Join(columnTable1, columnTable2, joinType);
    query.getJoins().add(join);

  }

  private static void constructGroup(String line, Query query) {
    String[] choppedGroupsAsString = line.split(COMMA_SPACE);

    for (String group : choppedGroupsAsString) {
      String[] groupArray = group.split(DOT_REGEX);
      query.getGroup().add(new GroupBy(groupArray[0], groupArray[1]));

    }
  }

}
