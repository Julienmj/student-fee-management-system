package models;

import java.math.BigDecimal;

/** Simple course descriptor used in registrar and student views. */
public class Course {
  private final int id;
  private final String program;
  private final String name;
  private final BigDecimal price;

  public Course(int id, String program, String name, BigDecimal price) {
    this.id = id;
    this.program = program;
    this.name = name;
    this.price = price;
  }

  public int getId() {
    return id;
  }

  public String getProgram() {
    return program;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getPrice() {
    return price;
  }

  @Override
  public String toString() {
    return name + " (" + price + " RWF)";
  }
}


