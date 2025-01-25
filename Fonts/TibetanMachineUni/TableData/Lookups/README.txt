The line breaks in this directory are mac-style even when I do a
checkout onto a Linux box, which means that I can't use grep on Linux
properly.  The following perl one-liner will fix this, but I wouldn't
check the files in if I were you without testing on all platforms to
see that the tools (MS Volt, I assume) still work with them.

Magical one-liner on Linux:
  perl -pi -e 's/\r/\n/g' *.vtl
Untested magical one-liner on Win32:
  perl -pi -e 's/\r/\r\n/g' *.vtl
