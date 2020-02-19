package com.github.thstock.symb

import java.util.Locale

import com.github.javafaker.Faker
import org.junit.Assert._
import org.scalatest.FunSuite
import scala.collection.parallel.CollectionConverters._
// import scala.collection.parallel.CollectionConverters._

class SymTest extends FunSuite {

  test("testSymbols") {
    val testee = new Sym
    assertEquals("JJ", testee.symbolOf("Jo", "Ja"))
    assertEquals("JJA", testee.symbolOf("Jo", "Ja"))
    assertEquals("JJO", testee.symbolOf("Jo", "Ja"))

    // permutationen oder zahlen
    assertEquals("JOJ", testee.symbolOf("Jo", "Ja"))
    assertEquals("JAJ", testee.symbolOf("Jo", "Ja"))
    assertEquals("JAO", testee.symbolOf("Jo", "Ja"))
    assertEquals("JOA", testee.symbolOf("Jo", "Ja"))
  }

  test("testSymbols4") {
    val testee = new Sym(Map(
      Seq("a") -> "TS",
      Seq("b") -> "TST",
      Seq("c") -> "TSA",
      Seq("d") -> "TSR",
    ))

    // permutationen oder zahlen
    assertEquals("TSTA", testee.symbolOf("Tar", "Sta"))
    assertEquals("TSTR", testee.symbolOf("Tar", "Sta"))

    assertEquals("TSTAA", testee.symbolOf("Tar", "Sta"))
    assertEquals("TSTAR", testee.symbolOf("Tar", "Sta"))

    assertEquals("TSTAAR", testee.symbolOf("Tar", "Sta"))

    assertEquals("TST1", testee.symbolOf("Tar", "Sta"))
  }

  test("testSymbols3") {
    val testee = new Sym
    assertEquals("TS", testee.symbolOf("Tarina", "Staples"))
    assertEquals("TST", testee.symbolOf("Tarina", "Staples"))
    assertEquals("TSA", testee.symbolOf("Tarina", "Staples"))
    assertEquals("TSR", testee.symbolOf("Tarina", "Staples"))

  }

  test("testFixMappings") {
    val testee = new Sym(Map(Seq("Torben", "Bart") -> "TOBA",
      Seq("Tobias", "Ten") -> "TT"),
    )
    assertEquals("TOBA", testee.symbolOf("Torben", "Bart"))
    assertEquals("TB", testee.symbolOf("Torben", "Bart"))
    assertEquals("TTE", testee.symbolOf("Torben", "Ten"))
  }

  test("testSymbolsCounter") {
    val testee = new Sym
    assertEquals("JJ", testee.symbolOf("J", "J"))
    assertEquals("JJ1", testee.symbolOf("J", "J"))
    assertEquals("AJ", testee.symbolOf("A", "J"))
    assertEquals("AJ1", testee.symbolOf("A", "J"))
    assertEquals("JJ2", testee.symbolOf("J", "J"))
  }

  test("testSymbols_ws") {
    val testee = new Sym
    assertEquals("ABC", testee.symbolOf("Ar Bla", "Cho"))
    assertEquals("ABCH", testee.symbolOf("Ar Bla", "Cho"))
  }

  test("testSymbols_empty") {
    val testee = new Sym
    assertEquals("AB", testee.symbolOf("Ar Bla", ""))
    assertEquals("ABL", testee.symbolOf("Ar Bla", ""))
    assertEquals("ABR", testee.symbolOf("Ar Bla", ""))
  }

  test("testSymbols_dash") {
    val testee = new Sym
    assertEquals("ABC", testee.symbolOf("Af Bla", "Cho"))
    assertEquals("ABCH", testee.symbolOf("Af-Bla", "Cho"))
  }

  test("testSymbols_empty_lastword") {
    val testee = new Sym
    assertEquals("ABCT", testee.symbolOf("Af Bla", "Cho (Test)"))
    assertEquals("ABCTE", testee.symbolOf("Af Bla", "Cho (Test)"))
    assertEquals("ABCTH", testee.symbolOf("Af Bla", "Cho (Test)"))
  }

  test("testSymbols_specials") {
    val testee = new Sym
    assertEquals("OA", testee.symbolOf("Ölu", "Älu)"))
    assertEquals("ÆA", testee.symbolOf("Æla", "Ålu)"))
  }

  test("fake") {
    val faker = new Faker(Locale.GERMANY)
    val testee = new Sym
    val names = List.tabulate(5_000)(_ => faker.name().firstName() + " " + faker.name().lastName())
    // TODO check ÖÄÜ
    // TODO check numbers
    names.par.map(n => (n, testee.symbolOf(n))).seq.sortBy(_._2.length).foreach(println)
  }

}
