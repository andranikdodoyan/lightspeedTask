package model;

import java.util.ArrayList;
import java.util.List;
import types.JoinType;

public class Query {

  private List<Column> columns;

  private List<Source> fromSource;

  private List<Join> joins;

  private List<GroupBy> group;

  private WhereClause whereClause;

  private HavingClause havingClause;

  private Integer limit;

  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  public void setHavingClause(HavingClause havingClause) {
    this.havingClause = havingClause;
  }

  public Query() {
    this.columns = new ArrayList<>();
    this.fromSource = new ArrayList<>();
    this.joins = new ArrayList<>();
    this.group = new ArrayList<>();
  }

  public List<Column> getColumns() {
    return columns;
  }

  public List<Source> getFromSource() {
    return fromSource;
  }


  public List<Join> getJoins() {
    return joins;
  }

  public List<GroupBy> getGroup() {
    return group;
  }

  public void setWhereClause(WhereClause whereClause) {
    this.whereClause = whereClause;
  }
}
