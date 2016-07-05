package logic

import models.Entry
import org.scalatest.{FreeSpec, Matchers}

class DataTest extends FreeSpec with Matchers {
  import Data._

  val entryOne = Entry("Title", "text")
  val entryTwo = Entry("New Title", "more text")

  "addEntry" - {
    "adds an entry to empty data" in {
      addEntry(Entry("Title", "text"), Nil) shouldEqual List(entryOne)
    }

    "appends an entry to existing data" in {
      addEntry(entryTwo, List(entryOne)) shouldEqual List(entryOne, entryTwo)
    }
  }

  "removeEntry" - {
    "doesn't throw an exception if it tries to remove something from empty data" in {
      removeEntry(2, List(entryOne))
    }

    "doesn't throw an exception if it tries to remove something that doesn't exist" in {
      removeEntry(3, Nil)
    }

    "removes the (0-based) index from the data" in {
      removeEntry(1, List(entryOne, entryTwo)) shouldEqual List(entryOne)
    }

    "removes first (0) index from the data" in {
      removeEntry(0, List(entryOne, entryTwo)) shouldEqual List(entryTwo)
    }
  }
}
