package model;

import java.util.ArrayList;
import java.util.List;

public class HavingClause {

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

  public HavingClause() {
    this.and = new ArrayList<>();
    this.or = new ArrayList<>();
  }
}
