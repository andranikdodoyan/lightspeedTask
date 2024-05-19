package sql;

public enum Condition {
//  IN("IN"),
//  NOT_IN("NOT IN"),
  EQ("="),
  GR(">"),
  SM("<"),
  LIKE("LIKE"),
  AND("AND"),
  OR("OR");

  private String value;

  public String getValue() {
    return value;
  }

  Condition(String value) {
    this.value = value;
  }
}
