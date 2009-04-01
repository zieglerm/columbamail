// The contents of this file are subject to the Mozilla Public License Version 1.1
// (the "License"); you may not use this file except in compliance with the
// License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
// Software distributed under the License is distributed on an "AS IS" basis,
// WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
// for the specific language governing rights and
// limitations under the License.
//
// The Original Code is "The Columba Project"
//
// The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
// Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
// All Rights Reserved.
package org.columba.ristretto.examples.imapsync;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

/**
 * ListTools class
 * 
 * @author timo
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class ListTools {
  /**
   * Subtracts two Lists in O(n * log n) that contain Objects that implement the Comparable Interface. The Result is in
   * List a and sorted. Be aware that List b gets also sorted!
   * 
   * @param a
   * @param b
   */
  public static void substract(List a, List b, Comparator c) {
    ListIterator aIt;
    ListIterator bIt;

    if ((a.size() == 0) || (b.size() == 0)) {
      return;
    }

    Collections.sort(a, c);
    Collections.sort(b, c);

    aIt = a.listIterator();
    bIt = b.listIterator();

    Object aVal;
    Object bVal;

    aVal = aIt.next();
    bVal = bIt.next();

    boolean loop = true;
    int compareResult;

    while (loop) {
      compareResult = c.compare(aVal, bVal);

      if (compareResult < 0) { // a < b

        if (aIt.hasNext()) {
          aVal = aIt.next();
        } else {
          return;
        }
      } else if (compareResult == 0) { // a == b
        aIt.remove();

        if (aIt.hasNext()) {
          aVal = aIt.next();
        } else {
          return;
        }

        if (bIt.hasNext()) {
          bVal = bIt.next();
        } else {
          return;
        }
      } else { // a > b

        if (bIt.hasNext()) {
          bVal = bIt.next();
        } else {
          return;
        }
      }
    }
  }
}
