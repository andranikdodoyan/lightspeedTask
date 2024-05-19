package model;

import java.util.ArrayList;
import java.util.List;

public class WhereClause {

  List<String> and;
  List<String> or;

  String condition;

  public List<String> getAnd() {
    return and;
  }

  public List<String> getOr() {
    return or;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }

  public WhereClause() {
    this.and = new ArrayList<>();
    this.or = new ArrayList<>();
  }
}
