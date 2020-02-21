package com.github.thstock.symb

import java.text.Normalizer
import java.util.concurrent.CopyOnWriteArraySet

import com.google.common.base.{CharMatcher, Splitter}

import scala.annotation.tailrec
import scala.jdk.CollectionConverters._

object Sym {
  def of(fm:java.util.Map[java.util.List[String], String]) =
    new Sym(fm.asScala.map(entry => entry.copy(entry._1.asScala.toList)).toMap)
}

class Sym(fixedMappings: Map[Seq[String], String] = Map.empty) {
  private val known: CopyOnWriteArraySet[String] = new CopyOnWriteArraySet();

  private def letterMatcher() = CharMatcher.javaLetter()

  def symbolOf(elems: String*): String = {
    symbolOf(elems.asJava)
  }

  def symbolOf(elems: java.util.List[String]): String = {
    val words: Seq[String] = elems.asScala.toSeq
      .flatMap(in => Splitter.on(letterMatcher().negate()).split(in).asScala)
      .map(s => Normalizer.normalize(s, Normalizer.Form.NFD))
      .filterNot(_.isBlank)

    selectSymbol(words)
  }

  @tailrec
  private def tryAdd(sym: String, next: Seq[Char], org: Seq[Char], counter: Int): Option[String] = {

    if (next == Nil) {
      val emptyOrgDropped = org.drop(counter) == Nil
      val dropC = if (emptyOrgDropped) {
        1
      } else {
        0
      }

      val newNext = if (counter > 2 || emptyOrgDropped) {
        val nums: Seq[Char] = "123456789".toSeq
        org.drop(counter + 1) ++ nums
      } else {
        org.drop(counter + 1)
        }.drop(dropC)
      val head = if (!emptyOrgDropped) {
        org.drop(counter).head
      } else {
        newNext.head
      }
      tryAdd(sym + head, newNext, org, counter + 1)
    } else {
      val str = (sym + next.head).trim
      if (!known.contains(str) && !fixedMappings.values.toSet.contains(str)) {
        Some(add(str))
      } else {
        tryAdd(sym, next.drop(1), org, counter)
      }
    }
  }

  @tailrec
  private def charsOf(words: Seq[String], drop: Int = 1, result: Seq[Char] = Nil): Seq[Char] = {
    val r = words.map(word => word.drop(drop)).filter(_.nonEmpty)
      .map(w => w.charAt(0))
    if (r == Nil) {
      result
    } else {
      charsOf(words, drop + 1, result ++ r)
    }
  }

  private def add(sym: String): String = {
    if (known.add(sym)) {
      sym
    } else {
      throw new IllegalStateException(s"${sym} already known")
    }
  }

  private def selectSymbol(words: Seq[String]): String = synchronized {
    val fixedResult = fixedMappings.get(words)
    if (fixedResult.isDefined && !known.contains(fixedResult.get)) {
      add(fixedResult.get)
    } else {
      val upperWords = words.map(_.toUpperCase())
      val simple = upperWords.map(w => w.charAt(0)).mkString("")
      if (known.contains(simple) || fixedMappings.values.toSet.contains(simple)) {
        val next = (' ' +: charsOf(upperWords.reverse))

        tryAdd(simple, next, next, 1).get
      } else {
        add(simple)
      }
    }
  }


}
