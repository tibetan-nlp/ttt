Ant's Javadoc task requires that a package list be named package-list,
and thus you give the task the name of the directory containing that
file.  Thus, this directory must exist so that our API docs can link
to the JDOM classes if we choose to do so.  See the Javadoc
documentation concerning the '-linkoffline' option.

package-list was downloaded from .../apidocs/package-list.  (I.e.,
it's a sibling of JDOM's index.html file.)
