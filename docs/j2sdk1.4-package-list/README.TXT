Ant's Javadoc task requires that a package list be named package-list,
and thus you give the task the name of the directory containing that
file.  Thus, this directory must exist so that our API docs will link
to java.lang.Object, javax.swing.JFrame, etc.

package-list was downloaded from .../api/package-list.  (I.e., it's a
sibling of j2sdk-1.4's index.html file.)
